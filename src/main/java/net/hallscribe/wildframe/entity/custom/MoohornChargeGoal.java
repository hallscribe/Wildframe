package net.hallscribe.wildframe.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class MoohornChargeGoal extends Goal {
    private enum Phase { APPROACH, WINDUP, CHARGE, RECOVER }

    private enum Combo {
        NORMAL(30, 12, 1.00, 30, 6.0),
        BULLDOZER(20, 14, 1.15, 35, 4.0),
        SHAKER(15, 18, 1.00, 30, 6.0),
        STRIKER(20, 8, 1.30, 35, 6.0),
        OBSERVER(15, 16, 1.00, 30, 3.0);

        final int weight;
        final int windup;
        final double speed;
        final int recover;
        final double overshoot;

        Combo(int weight, int windup, double speed, int recover, double overshoot) {
            this.weight = weight;
            this.windup = windup;
            this.speed = speed;
            this.recover = recover;
            this.overshoot = overshoot;
        }
    }

    private static final double CHARGE_START_RANGE_SQR = 10.0 * 10.0;
    private static final int LEG_MAX_TICKS = 40;
    private static final int BULLDOZER_REAIMS = 2;
    private static final double OBSERVER_LEAD = 4.0;
    private static final double SHAKER_RADIUS = 12.0;
    private static final Identifier SHAKER_SLOW_ID = Identifier.fromNamespaceAndPath("wildframe", "shaker_slow");
    private static final AttributeModifier SHAKER_SLOW = new AttributeModifier(
            SHAKER_SLOW_ID, -0.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

    private static final int TERRAIN_CHECK_INTERVAL = 5;
    private static final int MELEE_COOLDOWN = 20;
    private static final int REACH_CHECK_INTERVAL = 20;
    private static final int UNREACHABLE_LIMIT = 3;

    private final Moohorn mob;
    private final double approachSpeed;
    private final double chargeSpeed;

    private Phase phase = Phase.APPROACH;
    private Combo combo = Combo.NORMAL;
    private int phaseTicks;
    private int repathDelay;
    private int checkDelay;
    private int attackCooldown;
    private int reaimsLeft;
    private int reachCheckDelay;
    private int unreachableChecks;
    private boolean hitThisLeg;
    private Vec3 chargeEnd = Vec3.ZERO;

    private Vec3 lastTargetPos;
    private Vec3 targetVelocity = Vec3.ZERO;

    private final List<LivingEntity> slowed = new ArrayList<>();

    public MoohornChargeGoal(Moohorn mob, double approachSpeed, double chargeSpeed) {
        this.mob = mob;
        this.approachSpeed = approachSpeed;
        this.chargeSpeed = chargeSpeed;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.mob.getTarget();
        return target != null && target.isAlive() && !this.mob.isVehicle();
    }

    private boolean isLaneUsable(Vec3 aim) {
        Path path = this.mob.getNavigation().createPath(BlockPos.containing(aim), 0);
        if (path == null || !path.canReach()) return false;
        double dist = this.mob.position().distanceTo(aim);
        return path.getNodeCount() <= dist + 3;
    }

    private boolean startLeg(Vec3 aim) {
        this.chargeEnd = aim.add(this.flatDirTo(aim).scale(this.combo.overshoot));
        this.hitThisLeg = false;
        this.phaseTicks = LEG_MAX_TICKS;
        return this.mob.getNavigation().moveTo(this.chargeEnd.x, aim.y, this.chargeEnd.z,
                this.chargeSpeed * this.combo.speed);
    }

    private void abortCharge() {
        this.clearShaker();
        this.phase = Phase.APPROACH;
        this.checkDelay = 20;
        this.repathDelay = 0;
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void start() {
        this.phase = Phase.APPROACH;
        this.repathDelay = 0;
        this.checkDelay = 0;
        this.attackCooldown = 0;
        this.lastTargetPos = null;
        this.targetVelocity = Vec3.ZERO;
        this.reachCheckDelay = 0;
        this.unreachableChecks = 0;
        this.clearShaker();
    }

    @Override
    public void stop() {
        this.clearShaker();
        this.mob.getNavigation().stop();
    }

    @Override
    public void tick() {
        LivingEntity target = this.mob.getTarget();
        if (target == null) return;

        Vec3 pos = target.position();
        if (this.lastTargetPos != null) {
            Vec3 delta = pos.subtract(this.lastTargetPos);
            this.targetVelocity = this.targetVelocity.scale(0.7).add(delta.scale(0.3));
        }
        this.lastTargetPos = pos;

        switch (this.phase) {
            case APPROACH -> this.tickApproach(target);
            case WINDUP -> this.tickWindup(target);
            case CHARGE -> this.tickCharge(target);
            case RECOVER -> this.tickRecover();
        }
    }


    private void tickApproach(LivingEntity target) {
        this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
        if (this.attackCooldown > 0) this.attackCooldown--;

        if (this.attackCooldown <= 0
                && this.mob.getBoundingBox().inflate(0.5).intersects(target.getBoundingBox())) {
            this.attackCooldown = MELEE_COOLDOWN;
            this.mob.doHurtTarget((ServerLevel) this.mob.level(), target);
        }

        if (--this.reachCheckDelay <= 0) {
            this.reachCheckDelay = REACH_CHECK_INTERVAL;
            if (this.canReachTarget(target)) {
                this.unreachableChecks = 0;
            } else if (++this.unreachableChecks >= UNREACHABLE_LIMIT) {
                this.giveUp(target);
                return;
            }
        }

        if (this.mob.distanceToSqr(target) < CHARGE_START_RANGE_SQR && --this.checkDelay <= 0) {
            this.checkDelay = TERRAIN_CHECK_INTERVAL;
            if (this.mob.getSensing().hasLineOfSight(target)
                    && this.isLaneUsable(target.position())) {
                this.combo = this.pickCombo();
                this.beginWindup();
                return;
            }
        }

        if (--this.repathDelay <= 0) {
            this.repathDelay = 10;
            this.mob.getNavigation().moveTo(target, this.approachSpeed);
        }
    }

    private boolean canReachTarget(LivingEntity target) {
        if (this.mob.getBoundingBox().inflate(1.5).intersects(target.getBoundingBox())) return true;
        Path path = this.mob.getNavigation().createPath(target, 0);
        return path != null && path.canReach();
    }

    private void giveUp(LivingEntity target) {
        this.mob.markUnreachable(target);
        this.mob.setTarget(null);
        this.mob.getNavigation().stop();
        this.unreachableChecks = 0;
    }

    private void beginWindup() {
        this.mob.getNavigation().stop();
        this.mob.setDeltaMovement(0.0, this.mob.getDeltaMovement().y, 0.0);
        this.phase = Phase.WINDUP;
        this.phaseTicks = this.combo.windup;
    }

    private void tickWindup(LivingEntity target) {
        this.mob.getLookControl().setLookAt(target, 60.0F, 30.0F);
        if (--this.phaseTicks <= 0) {
            this.beginCharge(target);
        }
    }

    private void beginCharge(LivingEntity target) {
        Vec3 aim = this.aimPoint(target);
        if (!this.isLaneUsable(aim) || !this.startLeg(aim)) {
            this.abortCharge();
            return;
        }
        this.reaimsLeft = this.combo == Combo.BULLDOZER ? BULLDOZER_REAIMS : 0;
        this.phase = Phase.CHARGE;
    }

    private void tickCharge(LivingEntity target) {
        if (this.combo == Combo.SHAKER) this.applyShaker();

        AABB sweep = this.mob.getBoundingBox().expandTowards(this.mob.getDeltaMovement()).inflate(0.5);
        if (!this.hitThisLeg && sweep.intersects(target.getBoundingBox())) {
            this.hitThisLeg = true;
            this.mob.doHurtTarget((ServerLevel) this.mob.level(), target);
            this.mob.getNavigation().stop();
            this.endCharge();
            return;
        }

        boolean legDone = this.mob.getNavigation().isDone() || --this.phaseTicks <= 0;
        if (legDone) {
            if (this.reaimsLeft > 0) this.reaim(target); else this.endCharge();
        }
    }

    private void reaim(LivingEntity target) {
        this.reaimsLeft--;
        Vec3 aim = target.position();
        if (!this.isLaneUsable(aim) || !this.startLeg(aim)) {
            this.endCharge();
        }
    }

    private void endCharge() {
        this.clearShaker();
        this.phase = Phase.RECOVER;
        this.phaseTicks = this.combo.recover;
    }

    private void tickRecover() {
        this.mob.getNavigation().stop();
        LivingEntity target = this.mob.getTarget();
        if (target != null) {
            this.mob.getLookControl().setLookAt(target, 10.0F, 10.0F);
        }
        if (--this.phaseTicks <= 0) {
            this.phase = Phase.APPROACH;
            this.checkDelay = 0;
        }
    }

    private Combo pickCombo() {
        int totalWeight = 0;
        for (Combo c : Combo.values()) {
            totalWeight += c.weight;
        }
        int random = this.mob.getRandom().nextInt(totalWeight);
        for (Combo c : Combo.values()) {
            random -= c.weight;
            if (random < 0) return c;
        }
        return Combo.NORMAL;
    }

    private void clearShaker() {
        for (LivingEntity entity : this.slowed) {
            AttributeInstance speedAttr = entity.getAttribute(Attributes.MOVEMENT_SPEED);
            if (speedAttr != null) {
                speedAttr.removeModifier(SHAKER_SLOW_ID);
            }
        }
        this.slowed.clear();
    }

    private Vec3 aimPoint(LivingEntity target) {
        Vec3 targetPos = target.position();
        if (this.combo == Combo.OBSERVER && this.targetVelocity.lengthSqr() > 0.01) {
            Vec3 lead = this.targetVelocity.normalize().scale(OBSERVER_LEAD);
            targetPos = targetPos.add(lead);
        }
        return targetPos;
    }

    private Vec3 flatDirTo(Vec3 target) {
        double dx = target.x - this.mob.getX();
        double dz = target.z - this.mob.getZ();
        double dist = Math.sqrt(dx * dx + dz * dz);
        if (dist < 0.001) return Vec3.ZERO;
        return new Vec3(dx / dist, 0, dz / dist);
    }

    private void applyShaker() {
        Vec3 mobPos = this.mob.position();
        AABB radiusBox = new AABB(
                mobPos.x - SHAKER_RADIUS, mobPos.y - 2.0, mobPos.z - SHAKER_RADIUS,
                mobPos.x + SHAKER_RADIUS, mobPos.y + 2.0, mobPos.z + SHAKER_RADIUS
        );

        List<LivingEntity> nearby = this.mob.level().getEntitiesOfClass(LivingEntity.class, radiusBox);
        for (LivingEntity entity : nearby) {
            if (entity == this.mob) continue;
            if (!this.slowed.contains(entity)) {
                AttributeInstance speedAttr = entity.getAttribute(Attributes.MOVEMENT_SPEED);
                if (speedAttr != null && !speedAttr.hasModifier(SHAKER_SLOW_ID)) {
                    speedAttr.addTransientModifier(SHAKER_SLOW);
                    this.slowed.add(entity);
                }
            }
        }
    }
}