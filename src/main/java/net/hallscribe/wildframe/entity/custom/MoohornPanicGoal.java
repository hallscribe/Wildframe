package net.hallscribe.wildframe.entity.custom;

import net.minecraft.resources.Identifier;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class MoohornPanicGoal extends PanicGoal {
    private static final double SAFE_DISTANCE = 24.0;
    private static final double FEAR_TRIGGER_RANGE = 12.0;
    private static final int MEMORY_CHECK_INTERVAL = 10;
    private static final double PARENT_SEARCH_RANGE = 15.0;
    private static final double PARENT_ARRIVE_SQR = 3.0 * 3.0;
    private static final Identifier SPEED_ID = Identifier.fromNamespaceAndPath("wildframe", "baby_panic_boost");
    private static final AttributeModifier SPEED_BOOST = new AttributeModifier(
            SPEED_ID, 1.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);

    private int rallyDelay;

    private final Moohorn moohorn;
    private @Nullable LivingEntity fearTarget;
    private @Nullable LivingEntity memoryKiller;
    private @Nullable Moohorn parent;
    private boolean alarmMode;
    private boolean memoryMode;
    private int repathDelay;
    private int memoryCheckDelay;

    public MoohornPanicGoal(final Moohorn moohorn, final double speedModifier) {
        super(moohorn, speedModifier,
                m -> m.isBaby() ? DamageTypeTags.PANIC_CAUSES : DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES);
        this.moohorn = moohorn;
    }

    @Override
    public boolean canUse() {
        if (this.moohorn.getControllingPassenger() != null) {
            return false;
        }
        if (this.moohorn.isBaby()) {
            if (this.moohorn.hasGroupAlarm()) {
                return true;
            }
            this.memoryKiller = null;
            if (--this.memoryCheckDelay <= 0) {
                this.memoryCheckDelay = MEMORY_CHECK_INTERVAL;
                this.memoryKiller = this.moohorn.findFearedKillerWithin(FEAR_TRIGGER_RANGE);
                if (this.memoryKiller != null) {
                    return true;
                }
            }
        }
        if (this.moohorn.getLastHurtByMob() == null) {
            return false;
        }
        return super.canUse();
    }

    @Override
    public void start() {
        this.alarmMode = false;
        this.memoryMode = false;
        this.parent = null;

        if (this.moohorn.isBaby() && this.moohorn.hasGroupAlarm()) {
            this.startAlarm();
            return;
        }
        if (this.moohorn.isBaby() && this.memoryKiller != null) {
            this.startMemory();
            return;
        }

        super.start();
        this.fearTarget = this.moohorn.getLastHurtByMob();
        if (this.moohorn.isBaby() && this.fearTarget != null) {
            this.moohorn.rallyAdultsAgainst(this.fearTarget);
        }

        boolean runs = this.moohorn.isBaby() && !this.moohorn.hasAdultNearby();
        this.moohorn.setPanicRuns(runs);
        if (runs) {
            this.addBoost();
        }
    }

    private void startAlarm() {
        this.alarmMode = true;
        this.fearTarget = this.moohorn.getGroupThreat();
        if (this.fearTarget == null) {
            Player nearest = this.moohorn.level().getNearestPlayer(this.moohorn, 16.0);
            if (nearest != null && !nearest.isCreative() && !nearest.isSpectator()) {
                this.fearTarget = nearest;
            }
        }
        this.moohorn.clearGroupAlarm();

        this.rallyDelay = 40;
        if (this.fearTarget != null) {
            this.moohorn.rallyAdultsAgainst(this.fearTarget);
        }

        this.parent = this.findNearestAdult();
        this.repathDelay = 0;
        this.moohorn.setPanicRuns(true);
        this.addBoost();
    }

    private void startMemory() {
        this.memoryMode = true;
        this.fearTarget = this.memoryKiller;
        this.moohorn.setPanicRuns(true);
        this.addBoost();
        this.rallyDelay = 40;
        this.moohorn.rallyAdultsAgainst(this.memoryKiller);
    }

    @Override
    public boolean canContinueToUse() {
        if (this.memoryMode) {
            return this.isFearActive();
        }
        if (this.alarmMode) {
            if (this.parent != null) {
                return this.parent.isAlive()
                        && this.moohorn.distanceToSqr(this.parent) > PARENT_ARRIVE_SQR;
            }
            return this.isFearActive();
        }
        if (this.isFearActive()) {
            return true;
        }
        return super.canContinueToUse();
    }

    @Override
    public void tick() {
        if ((this.memoryMode || this.alarmMode) && this.fearTarget != null && --this.rallyDelay <= 0) {
            this.rallyDelay = 40;
            this.moohorn.rallyAdultsAgainst(this.fearTarget);
        }
        if (this.alarmMode && this.parent != null) {
            if (!this.parent.isAlive()) {
                this.parent = this.findNearestAdult();
            }
            if (this.parent != null) {
                if (--this.repathDelay <= 0) {
                    this.repathDelay = 10;
                    this.moohorn.getNavigation().moveTo(this.parent, this.speedModifier);
                }
                return;
            }
        }

        if (!this.alarmMode && !this.memoryMode) {
            super.tick();
        }
        this.fleeFromThreat();
    }

    @Override
    public void stop() {
        super.stop();
        this.fearTarget = null;
        this.memoryKiller = null;
        this.parent = null;
        this.alarmMode = false;
        this.memoryMode = false;
        this.moohorn.setPanicRuns(false);
        AttributeInstance speed = this.moohorn.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speed != null) {
            speed.removeModifier(SPEED_ID);
        }
    }

    private boolean isFearActive() {
        return this.fearTarget != null && this.fearTarget.isAlive()
                && this.moohorn.distanceToSqr(this.fearTarget) < SAFE_DISTANCE * SAFE_DISTANCE;
    }

    private void fleeFromThreat() {
        if (this.fearTarget != null && this.moohorn.getNavigation().isDone()) {
            Vec3 away = DefaultRandomPos.getPosAway(this.moohorn, 16, 7, this.fearTarget.position());
            if (away != null) {
                this.moohorn.getNavigation().moveTo(away.x, away.y, away.z, this.speedModifier);
            }
        }
    }

    private @Nullable Moohorn findNearestAdult() {
        Moohorn guardian = this.moohorn.getGuardian();
        if (guardian != null && guardian.isAlive() && !guardian.isBaby()
                && this.moohorn.distanceToSqr(guardian) < 32.0 * 32.0) {
            return guardian;
        }
        Moohorn best = null;
        double bestDist = Double.MAX_VALUE;
        for (Moohorn other : this.moohorn.level().getEntitiesOfClass(Moohorn.class,
                this.moohorn.getBoundingBox().inflate(PARENT_SEARCH_RANGE, 8.0, PARENT_SEARCH_RANGE),
                o -> o != this.moohorn && !o.isBaby() && o.isAlive())) {
            double d = this.moohorn.distanceToSqr(other);
            if (d < bestDist) {
                bestDist = d;
                best = other;
            }
        }
        return best;
    }

    private void addBoost() {
        AttributeInstance speed = this.moohorn.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speed != null && !speed.hasModifier(SPEED_ID)) {
            speed.addTransientModifier(SPEED_BOOST);
        }
    }
}