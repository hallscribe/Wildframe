package net.hallscribe.wildframe.entity.custom;

import net.hallscribe.wildframe.entity.WildframeEntities;
import net.hallscribe.wildframe.item.WildframeItems;
import net.hallscribe.wildframe.particle.WildframeParticles;
import net.hallscribe.wildframe.sounds.WildframeSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import java.util.List;
import java.util.UUID;

public class Moohorn extends Animal implements NeutralMob, PlayerRideableJumping {
    private boolean groupAlarm;
    private @Nullable LivingEntity groupThreat;

    private static final int FUR_INTERVAL_TICKS = 3 * 60 * 5;

    private int furTime = FUR_INTERVAL_TICKS;

    private static final EntityDataAccessor<Boolean> DATA_RUNNING_ID = SynchedEntityData.defineId(Moohorn.class, EntityDataSerializers.BOOLEAN);
    private static final Identifier ANGER_SPEED_MODIFIER_ID = Identifier.fromNamespaceAndPath("wildframe", "anger_speed_boost");
    private static final AttributeModifier ANGER_SPEED_MODIFIER = new AttributeModifier(
        ANGER_SPEED_MODIFIER_ID, 2.8, AttributeModifier.Operation.ADD_MULTIPLIED_BASE
    );
    public final AnimationState attackAnimationState = new AnimationState();
    public int attackAnimationTick = 0;
    private long persistentAngerEndTime;
    private @Nullable EntityReference<LivingEntity> persistentAngerTarget;
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);

    private static final EntityDataAccessor<Boolean> DATA_RIDE_SPRINT_ID = SynchedEntityData.defineId(Moohorn.class, EntityDataSerializers.BOOLEAN);

    private static final int CHARGE_MIN_TICKS = 20;
    private static final int CHARGE_MAX_TICKS = 120;
    private static final int CHARGE_COOLDOWN_TICKS = 100;
    private static final float RIDE_WALK_MULT = 0.65F;
    private static final float RIDE_SPRINT_MULT = 1.2F;
    private static final float RIDE_CHARGE_MULT = 2.8F;
    private static final float RIDE_CHARGE_TURN = 6.0F;
    private static final int RIDE_HIT_INTERVAL = 15;
    private static final int RUN_SOUND_INTERVAL_TICKS = 8;
    private static final int BREATH_SOUND_INTERVAL_TICKS = 16;
    private static final int NEVER = -1000;
    private static final double STEP_SOUND_SPEED_EPSILON = 0.0004D;
    private static final float HAY_HEAL_AMOUNT = 6.0F;

    private static final int WATER_RAMP_TICKS = 30;
    private static final double FLOAT_BASE = 0.25;
    private static final double FLOAT_BOB  = 0.15;
    private int waterRideTicks;


    private boolean panicRuns;
    private double lastSoundX;
    private double lastSoundZ;
    private boolean movedForSound;

    private float airSeek;
    private float airSeekO;

    public float getAirSeek(final float partialTick) {
        return Mth.lerp(partialTick, this.airSeekO, this.airSeek);
    }

    private void tickAirSeek() {
        this.airSeekO = this.airSeek;
        boolean seeking = this.isInWater() && this.getControllingPassenger() != null;
        float target = seeking ? 1.0F : 0.0F;
        this.airSeek += (target - this.airSeek) * 0.15F;
        if (Math.abs(target - this.airSeek) < 0.001F) {
            this.airSeek = target;
        }
    }

    private int rideChargeTimer = 0;
    private int lastRunSoundTick = NEVER;
    private int lastBreathSoundTick = NEVER;
    private int lastRefuseTick = -100;
    private @Nullable AABB rideBoxO;
    private final Map<Integer, Integer> rideHits = new HashMap<>();

    private @Nullable Moohorn guardian;

    @Nullable Moohorn getGuardian() {
        return this.guardian;
    }

    void setGuardian(final @Nullable Moohorn guardian) {
        this.guardian = guardian;
    }

    void raiseGroupAlarm(final @Nullable LivingEntity threat) {
        this.groupAlarm = true;
        this.groupThreat = threat;
    }

    boolean hasGroupAlarm() {
        return this.groupAlarm;
    }

    @Nullable LivingEntity getGroupThreat() {
        return this.groupThreat;
    }

    void clearGroupAlarm() {
        this.groupAlarm = false;
        this.groupThreat = null;
    }

    void rallyAdultsAgainst(final LivingEntity threat) {
        if (threat instanceof Player player && (player.isCreative() || player.isSpectator())) {
            return;
        }
        for (Moohorn adult : this.level().getEntitiesOfClass(Moohorn.class,
                this.getBoundingBox().inflate(24.0, 8.0, 24.0),
                o -> o != this && !o.isBaby() && o.isAlive() && !o.isVehicle() && o.getTarget() == null)) {
            adult.setTarget(threat);
        }
    }

    @Override
    public void die(final DamageSource source) {
        super.die(source);
        if (this.level() instanceof ServerLevel level) {
            LivingEntity killer = source.getEntity() instanceof LivingEntity living ? living : null;
            if (killer instanceof Player p && (p.isCreative() || p.isSpectator())) {
                killer = null;
            }
            for (Moohorn baby : level.getEntitiesOfClass(Moohorn.class,
                    this.getBoundingBox().inflate(24.0, 8.0, 24.0),
                    other -> other != this && other.isBaby() && other.isAlive())) {
                baby.raiseGroupAlarm(killer);
                if (!this.isBaby()) {
                    baby.rememberKiller(killer);
                }
            }
        }
    }

    void setPanicRuns(final boolean value) {
        this.panicRuns = value;
    }

    boolean hasAdultNearby() {
        return !this.level().getEntitiesOfClass(Moohorn.class,
                this.getBoundingBox().inflate(8.0, 4.0, 8.0),
                other -> other != this && !other.isBaby()).isEmpty();
    }

    private void updateMovedForSound() {
        double dx = this.getX() - this.lastSoundX;
        double dz = this.getZ() - this.lastSoundZ;
        double distSqr = dx * dx + dz * dz;
        this.movedForSound = distSqr > STEP_SOUND_SPEED_EPSILON;
        this.lastSoundX = this.getX();
        this.lastSoundZ = this.getZ();
    }

    private @Nullable UUID fearedKillerId;

    void rememberKiller(final @Nullable LivingEntity killer) {
        if (killer != null && killer != this) {
            this.fearedKillerId = killer.getUUID();
        }
    }

    @Nullable LivingEntity findFearedKillerWithin(final double range) {
        if (this.fearedKillerId == null) {
            return null;
        }
        final UUID id = this.fearedKillerId;
        List<LivingEntity> found = this.level().getEntitiesOfClass(LivingEntity.class,
                this.getBoundingBox().inflate(range),
                e -> e.isAlive() && !e.isSpectator() && e.getUUID().equals(id)
                        && !(e instanceof Player p && p.isCreative()));
        return found.isEmpty() ? null : found.get(0);
    }

    public Moohorn(final EntityType<? extends Moohorn> type, final Level level) {
        super(type, level);
        this.getNavigation().setCanFloat(true);
    }

    @Override
    public double getFluidJumpThreshold() {
        return 0.8;
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(Items.HAY_BLOCK);
    }

    @Override
    public AgeableMob getBreedOffspring(final ServerLevel level, final AgeableMob partner) {
        return WildframeEntities.MOOHORN.create(level, EntitySpawnReason.BREEDING);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MoohornPanicGoal(this, 2.0));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25, i -> i.is(Items.HAY_BLOCK), false));
        this.goalSelector.addGoal(4, new MoohornGuardGoal(this, 1.4D));
        this.goalSelector.addGoal(5, new MoohornChargeGoal(this, 1.2D, 1.6D));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new FollowParentGoal(this, 1.25));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new MoohornHurtByTargetGoal());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 16, true, false, this::isAngryAt));
        this.targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    private class MoohornHurtByTargetGoal extends HurtByTargetGoal {
        public MoohornHurtByTargetGoal() {
            super(Moohorn.this);
        }

        @Override
        public void start() {
            super.start();
            this.alertOthers();
            if (Moohorn.this.isBaby()) {
                this.stop();
            }
        }

        @Override
        protected void alertOther(final Mob other, final LivingEntity hurtByMob) {
            if (other instanceof Moohorn && !other.isBaby()) {
                super.alertOther(other, hurtByMob);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.rideChargeTimer > 0) {
            this.rideChargeTimer--;
        }
        if (this.getControllingPassenger() == null && this.rideChargeTimer > CHARGE_COOLDOWN_TICKS) {
            this.rideChargeTimer = CHARGE_COOLDOWN_TICKS;
        }

        if (this.level().isClientSide()) {
            this.spawnRunParticles();
            this.tickAirSeek();
            if (this.attackAnimationTick > 0) {
                this.attackAnimationTick--;
                if (!this.attackAnimationState.isStarted()) {
                    this.attackAnimationState.start(this.tickCount);
                }
            } else if (this.attackAnimationState.isStarted()) {
                this.attackAnimationState.stop();
            }
        } else {
            if (this.attackAnimationTick > 0) {
                this.attackAnimationTick--;
            }

            if (this.getControllingPassenger() instanceof Player rider) {
                this.tickRiddenServer(rider);
            } else {
                this.rideBoxO = null;
                this.setRideSprinting(false);

                LivingEntity t = this.getTarget();
                if (t != null && this.distanceToSqr(t) > 32.0 * 32.0) {
                    this.setTarget(null);
                }

                final boolean angry = this.getTarget() != null || this.isAngry();
                final AttributeInstance speed = this.getAttribute(Attributes.MOVEMENT_SPEED);
                if (angry) {
                    if (speed != null && !speed.hasModifier(ANGER_SPEED_MODIFIER_ID)) {
                        speed.addTransientModifier(ANGER_SPEED_MODIFIER);
                    }
                } else if (speed != null) {
                    speed.removeModifier(ANGER_SPEED_MODIFIER_ID);
                }
                this.setRunning(angry || this.panicRuns);
            }

            this.playMovementSoundsIfNeeded();
            this.updateRunningBreathSound();

            this.playMovementSoundsIfNeeded();
            this.updateRunningBreathSound();
            if (this.level() instanceof ServerLevel serverLevel) {
                this.tickFurDrop(serverLevel);
            }
        }
    }

    public static AttributeSupplier.Builder createMoohornAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 36.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.09D)
                .add(Attributes.STEP_HEIGHT, 1.25D)
                .add(Attributes.FOLLOW_RANGE, 16.0D)
                .add(Attributes.TEMPT_RANGE, 10.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.6D)
                .add(Attributes.ATTACK_KNOCKBACK, 1.5D)
                .add(Attributes.ATTACK_DAMAGE, 9.0);
    }

    public static boolean checkMoohornSpawnRules(
            final EntityType<Moohorn> type, final LevelAccessor level, final EntitySpawnReason spawnReason, final BlockPos pos, final RandomSource random
    ) {
        return checkAnimalSpawnRules(type, level, spawnReason, pos, random);
    }

    @Override
    public SpawnGroupData finalizeSpawn(
            final ServerLevelAccessor level, final DifficultyInstance difficulty, final EntitySpawnReason spawnReason, @Nullable SpawnGroupData groupData
    ) {
        groupData = super.finalizeSpawn(level, difficulty, spawnReason, groupData);

        if (spawnReason == EntitySpawnReason.NATURAL && random.nextFloat() < 0.2f) {
            this.setBaby(true);
        }

        return groupData;
    }


    @Override
    public void travel(Vec3 travelVector) {
        if (this.isVehicle()
                && this.getControllingPassenger() instanceof Player passenger) {

            this.setYRot(passenger.getYRot());
            this.yRotO = this.getYRot();
            this.setXRot(passenger.getXRot() * 0.5F);
            this.setRot(this.getYRot(), this.getXRot());
            this.yBodyRot = this.getYRot();
            this.yHeadRot = this.getYRot();

            if (this.isInWater()) {
                this.waterRideTicks = Math.min(this.waterRideTicks + 1, WATER_RAMP_TICKS);

                float forward = (float) travelVector.z;
                float sideways = (float) travelVector.x;

                this.setSpeed(this.getSpeed() * 0.8F);
                super.travel(new Vec3(sideways, 0.0, forward));

                Vec3 velocity = this.getDeltaMovement();

                float ramp = this.waterRideTicks / (float) WATER_RAMP_TICKS;
                ramp = ramp * ramp * (3.0F - 2.0F * ramp);

                double h = this.getBbHeight();
                double wave = Math.sin(this.tickCount * 0.12);
                double targetDepth = h * (FLOAT_BASE + FLOAT_BOB * wave * ramp);

                double depthError = this.getFluidHeight(FluidTags.WATER) - targetDepth;
                double targetY = Mth.clamp(depthError * 0.12, -0.05, 0.06);

                double newY = Mth.lerp(0.25, velocity.y, targetY);
                this.setDeltaMovement(velocity.x, newY, velocity.z);
                return;
            }

            this.waterRideTicks = 0;
        }

        super.travel(travelVector);
    }


    private void tickFurDrop(final ServerLevel level) {
        if (!this.isAlive() || this.isBaby()) {
            return;
        }
        if (--this.furTime <= 0) {
            this.furTime = FUR_INTERVAL_TICKS;
            ItemEntity fur = new ItemEntity(level, this.getX(), this.getY() + 0.5, this.getZ(),
                    new ItemStack(WildframeItems.FUR));
            fur.setDefaultPickUpDelay();
            level.addFreshEntity(fur);
            this.gameEvent(GameEvent.ENTITY_ACTION);
        }
    }

    @Override
    public float getSecondsToDisableBlocking() {
        return 5.0F;
    }

    @Override
    public InteractionResult mobInteract(final Player player, final InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);

        if (this.isVehicle() || this.isBaby()) {
            return super.mobInteract(player, hand);
        }

        if (this.isFood(held) && this.getHealth() < this.getMaxHealth()) {
            if (!this.level().isClientSide()) {
                this.heal(HAY_HEAL_AMOUNT);
                this.usePlayerItem(player, hand, held);
                this.playEatingSound();
                this.gameEvent(GameEvent.EAT);
            }
            return InteractionResult.SUCCESS;
        }

        if (this.isVehicle() || this.isBaby()) {
            return super.mobInteract(player, hand);
        }

        if (this.isFood(held)) {
            return super.mobInteract(player, hand);
        }

        if (held.is(Items.SADDLE) && (this.isAngry() || this.getTarget() != null)) {
            return this.refuseRider();
        }

        if (!held.isEmpty()) {
            InteractionResult result = held.interactLivingEntity(player, this, hand);
            if (result.consumesAction()) {
                return result;
            }
            return this.refuseRider();
        }

        if (hand == InteractionHand.MAIN_HAND && !player.getOffhandItem().isEmpty()) {
            return InteractionResult.PASS;
        }

        if (!this.isSaddled()) {
            return InteractionResult.PASS;
        }
        if (this.isAngry() || this.getTarget() != null) {
            return this.refuseRider();
        }

        if (!this.level().isClientSide()) {
            this.getNavigation().stop();
            player.setYRot(this.getYRot());
            player.setXRot(this.getXRot());
            player.startRiding(this);
        }
        return InteractionResult.SUCCESS;
    }

    private InteractionResult refuseRider() {
        if (!this.level().isClientSide() && this.tickCount - this.lastRefuseTick > 20) {
            this.lastRefuseTick = this.tickCount;
            this.makeSound(this.isBaby() ? WildframeSoundEvents.MOOHORN_BABY_ANGRY : WildframeSoundEvents.MOOHORN_ANGRY);
            this.attackAnimationTick = 30;
            this.level().broadcastEntityEvent(this, (byte) 4);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean canUseSlot(final EquipmentSlot slot) {
        if (slot == EquipmentSlot.SADDLE) {
            return this.isAlive() && !this.isBaby() && !this.isAngry() && this.getTarget() == null;
        }
        return super.canUseSlot(slot);
    }

    @Override
    public @Nullable LivingEntity getControllingPassenger() {
        return this.isSaddled() && this.getFirstPassenger() instanceof Player passenger ? passenger : super.getControllingPassenger();
    }

    @Override
    protected void updateControlFlags() {
        super.updateControlFlags();
        if (this.getControllingPassenger() instanceof Player) {
            this.goalSelector.setControlFlag(Goal.Flag.MOVE, false);
            this.goalSelector.setControlFlag(Goal.Flag.LOOK, false);
        }
    }

    @Override
    public boolean canSprint() {
        return true;
    }

    @Override
    protected Vec3 getRiddenInput(final Player controller, final Vec3 selfInput) {
        if (this.isRideCharging()) {
            return new Vec3(0.0, 0.0, 1.0);
        }
        float sideways = controller.xxa * 0.5F;
        float forward = controller.zza;
        if (forward <= 0.0F) {
            forward *= 0.25F;
        }
        return new Vec3(sideways, 0.0, forward);
    }

    @Override
    protected float getRiddenSpeed(final @NonNull Player controller) {
        float base = (float) this.getAttributeBaseValue(Attributes.MOVEMENT_SPEED);
        if (this.isRideCharging()) return base * RIDE_CHARGE_MULT;
        if (controller.isSprinting()) return base * RIDE_SPRINT_MULT;
        return base * RIDE_WALK_MULT;
    }

    @Override
    protected void tickRidden(final @NonNull Player controller, final @NonNull Vec3 riddenInput) {
        super.tickRidden(controller, riddenInput);
        float yaw = controller.getYRot();
        if (this.isRideCharging()) {
            float diff = Mth.wrapDegrees(yaw - this.getYRot());
            yaw = this.getYRot() + Mth.clamp(diff, -RIDE_CHARGE_TURN, RIDE_CHARGE_TURN);
        }
        this.setRot(yaw, controller.getXRot() * 0.5F);
        this.yRotO = this.yBodyRot = this.yHeadRot = this.getYRot();
    }

    @Override
    protected void positionRider(final Entity passenger, final Entity.MoveFunction moveFunction) {
        super.positionRider(passenger, moveFunction);
        if (passenger instanceof LivingEntity living) {
            living.yBodyRot = this.yBodyRot;
            float f = Mth.cos(this.getYRot() * Mth.DEG_TO_RAD);
            float f1 = Mth.sin(this.getYRot() * Mth.DEG_TO_RAD);
            float forwardOffset = -0.25F;
            float upOffset = 0.3F;
            moveFunction.accept(passenger, passenger.getX() + f1 * forwardOffset, passenger.getY() + upOffset, passenger.getZ() - f * forwardOffset);
        }
    }

    public boolean isRideCharging() {
        return this.rideChargeTimer > CHARGE_COOLDOWN_TICKS;
    }

    public boolean isRideSprinting() {
        return this.entityData.get(DATA_RIDE_SPRINT_ID);
    }

    private void setRideSprinting(final boolean value) {
        this.entityData.set(DATA_RIDE_SPRINT_ID, value);
    }

    private void startRideCharge(final int jumpAmount) {
        float scale = this.getPlayerJumpPendingScale(jumpAmount);
        float t = Mth.clamp((scale - 0.4F) / 0.6F, 0.0F, 1.0F);
        int duration = Mth.floor(Mth.lerp(t, (float) CHARGE_MIN_TICKS, (float) CHARGE_MAX_TICKS));
        this.rideChargeTimer = duration + CHARGE_COOLDOWN_TICKS;
        this.rideHits.clear();
    }

    @Override
    public void onPlayerJump(final int jumpAmount) {
        if (this.isSaddled() && this.rideChargeTimer == 0 && this.onGround()) {
            this.startRideCharge(jumpAmount);
        }
    }

    @Override
    public void handleStartJump(final int jumpScale) {
        if (this.isSaddled() && this.rideChargeTimer <= 10 && this.onGround()) {
            this.startRideCharge(jumpScale);
            this.makeSound(this.isBaby() ? WildframeSoundEvents.MOOHORN_BABY_ANGRY : WildframeSoundEvents.MOOHORN_ANGRY);
            this.gameEvent(GameEvent.ENTITY_ACTION);
        }
    }

    @Override
    public void handleStopJump() {
    }

    @Override
    public boolean canJump() {
        return this.isSaddled() && !this.isBaby();
    }

    @Override
    public int getJumpCooldown() {
        return this.rideChargeTimer;
    }

    private void tickRiddenServer(final Player rider) {
        if (this.getTarget() != null) {
            this.setTarget(null);
        }
        AttributeInstance speed = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speed != null) {
            speed.removeModifier(ANGER_SPEED_MODIFIER_ID);
        }

        boolean charging = this.isRideCharging();
        this.setRunning(charging);
        this.setRideSprinting(!charging && rider.isSprinting());

        if (charging && this.level() instanceof ServerLevel serverLevel) {
            this.rideChargeHit(serverLevel, rider);
        } else {
            this.rideBoxO = null;
        }
    }

    private void rideChargeHit(final ServerLevel level, final Player rider) {
        AABB now = this.getBoundingBox();
        AABB sweep = (this.rideBoxO == null ? now : now.minmax(this.rideBoxO)).inflate(0.5, 0.0, 0.5);
        this.rideBoxO = now;

        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, sweep)) {
            if (target == this || target == rider || target instanceof Moohorn
                    || this.hasPassenger(target) || !target.isAlive()) continue;
            Integer last = this.rideHits.get(target.getId());
            if (last != null && this.tickCount - last < RIDE_HIT_INTERVAL) continue;
            this.rideHits.put(target.getId(), this.tickCount);
            this.doHurtTarget(level, target);
        }
    }

    private @Nullable Vec3 getDismountLocationInDirection(final Vec3 direction, final LivingEntity passenger) {
        double targetX = this.getX() + direction.x;
        double targetY = this.getBoundingBox().minY;
        double targetZ = this.getZ() + direction.z;
        BlockPos.MutableBlockPos targetBlockPos = new BlockPos.MutableBlockPos();

        for (Pose dismountPose : passenger.getDismountPoses()) {
            targetBlockPos.set(targetX, targetY, targetZ);
            double dismountJumpLimit = this.getBoundingBox().maxY + 0.75;
            do {
                double blockFloorHeight = this.level().getBlockFloorHeight(targetBlockPos);
                if (targetBlockPos.getY() + blockFloorHeight > dismountJumpLimit) break;
                if (DismountHelper.isBlockFloorValid(blockFloorHeight)) {
                    AABB poseBox = passenger.getLocalBoundsForPose(dismountPose);
                    Vec3 location = new Vec3(targetX, targetBlockPos.getY() + blockFloorHeight, targetZ);
                    if (DismountHelper.canDismountTo(this.level(), passenger, poseBox.move(location))) {
                        passenger.setPose(dismountPose);
                        return location;
                    }
                }
                targetBlockPos.move(Direction.UP);
            } while (targetBlockPos.getY() < dismountJumpLimit);
        }
        return null;
    }

    @Override
    public @NonNull Vec3 getDismountLocationForPassenger(final LivingEntity passenger) {
        Vec3 mainDir = getCollisionHorizontalEscapeVector(this.getBbWidth(), passenger.getBbWidth(),
                this.getYRot() + (passenger.getMainArm() == HumanoidArm.RIGHT ? 90.0F : -90.0F));
        Vec3 main = this.getDismountLocationInDirection(mainDir, passenger);
        if (main != null) return main;

        Vec3 offDir = getCollisionHorizontalEscapeVector(this.getBbWidth(), passenger.getBbWidth(),
                this.getYRot() + (passenger.getMainArm() == HumanoidArm.LEFT ? 90.0F : -90.0F));
        Vec3 off = this.getDismountLocationInDirection(offDir, passenger);
        return off != null ? off : this.position();
    }

    @Override
    protected void readAdditionalSaveData(final ValueInput input) {
        super.readAdditionalSaveData(input);
        this.readPersistentAngerSaveData(this.level(), input);
        this.furTime = input.getIntOr("FurTime", FUR_INTERVAL_TICKS);
        String saved = input.getStringOr("FearedKiller", "");
        if (!saved.isEmpty()) {
            try {
                this.fearedKillerId = UUID.fromString(saved);
            } catch (IllegalArgumentException ignored) {
            }
        }
    }

    @Override
    protected void addAdditionalSaveData(final ValueOutput output) {
        super.addAdditionalSaveData(output);
        this.addPersistentAngerSaveData(output);
        output.putInt("FurTime", this.furTime);
        if (this.fearedKillerId != null) {
            output.putString("FearedKiller", this.fearedKillerId.toString());
        }
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setTimeToRemainAngry(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    @Override
    public void setPersistentAngerEndTime(final long endTime) {
        this.persistentAngerEndTime = endTime;
    }

    @Override
    public long getPersistentAngerEndTime() {
        return this.persistentAngerEndTime;
    }

    @Override
    public void setPersistentAngerTarget(final @Nullable EntityReference<LivingEntity> persistentAngerTarget) {
        this.persistentAngerTarget = persistentAngerTarget;
    }

    @Override
    public @Nullable EntityReference<LivingEntity> getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    protected void defineSynchedData(final SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(DATA_RIDE_SPRINT_ID, false);
        entityData.define(DATA_RUNNING_ID, false);
    }

    public boolean isRunning() {
        return this.getEntityData().get(DATA_RUNNING_ID);
    }

    public void setRunning(final boolean running) {
        this.getEntityData().set(DATA_RUNNING_ID, running);
    }

    public int getAttackAnimationRemainingTicks() {
        return this.attackAnimationTick;
    }

    @Override
    public boolean doHurtTarget(final ServerLevel level, final Entity target) {
        this.attackAnimationTick = 30;
        level.broadcastEntityEvent(this, (byte)4);
        return super.doHurtTarget(level, target);
    }

    @Override
    public boolean hurtServer(final ServerLevel level, final DamageSource source, final float damage) {
        if (source.getEntity() instanceof Player attacker
                && attacker.getVehicle() instanceof Moohorn mount
                && mount != this) {
            attacker.stopRiding();
            mount.refuseRider();
        }
        return super.hurtServer(level, source, damage);
    }

    @Override
    public void handleEntityEvent(final byte id) {
        if (id == 4) {
            this.attackAnimationTick = 30;
            if (this.level().isClientSide()) {
                this.attackAnimationState.start(this.tickCount);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    private void spawnRunParticles() {
        if (!this.isRunning() || !this.onGround() || this.isInWater()) {
            return;
        }
        if (this.tickCount % 2 != 0) {
            return;
        }

        double dx = this.getX() - this.xo;
        double dz = this.getZ() - this.zo;
        if (dx * dx + dz * dz < STEP_SOUND_SPEED_EPSILON) {
            return;
        }

        float yaw = this.yBodyRot * Mth.DEG_TO_RAD;
        double back = this.isBaby() ? 0.8 : 1.4;
        double baseX = this.getX() + Mth.sin(yaw) * back;
        double baseZ = this.getZ() - Mth.cos(yaw) * back;

        for (int i = 0; i < 2; i++) {
            double ox = (this.random.nextDouble() - 0.5) * 0.8;
            double oz = (this.random.nextDouble() - 0.5) * 0.8;
            this.level().addParticle(WildframeParticles.RUN_SMOKE, baseX + ox, this.getY() + 0.1, baseZ + oz, 0.0, 0.02, 0.0);
        }
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource source) {
        return this.isBaby() ? WildframeSoundEvents.MOOHORN_BABY_HURT : WildframeSoundEvents.MOOHORN_HURT;
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return this.isBaby() ? WildframeSoundEvents.MOOHORN_BABY_AMBIENT : WildframeSoundEvents.MOOHORN_AMBIENT;
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return this.isBaby() ? WildframeSoundEvents.MOOHORN_BABY_DEATH : WildframeSoundEvents.MOOHORN_DEATH;
    }

    @Override
    protected void playEatingSound() {
        this.level().playSound(null, this, WildframeSoundEvents.MOOHORN_EAT, SoundSource.NEUTRAL, 1.0F, Mth.randomBetween(this.level().getRandom(), 0.8F, 1.2F));
    }

    @Override
    protected void playAttackSound() {
        this.playSound(WildframeSoundEvents.MOOHORN_ATTACK, 1.0F, 1.0F);
    }

    private BlockState getGroundState() {
        BlockPos below = this.getOnPos();
        BlockState state = this.level().getBlockState(below);
        BlockState above = this.level().getBlockState(below.above());
        return above.is(Blocks.SNOW) ? above : state;
    }

    private void playMovementSoundsIfNeeded() {

        this.updateMovedForSound();
        boolean moving = this.movedForSound;

        if (!this.onGround() || this.isInWater() || !moving) {
            return;
        }

        BlockState ground = this.getGroundState();
        if (ground.isAir()) {
            return;
        }

        if (this.isRunning() || this.isRideCharging()) {
            if (this.tickCount - this.lastRunSoundTick >= RUN_SOUND_INTERVAL_TICKS) {
                this.lastRunSoundTick = this.tickCount;
                this.playSound(this.getGallopSound(ground), 0.28F, 1.0F);
            }
        }
    }

    private void updateRunningBreathSound() {
        boolean activeRun = this.isRunning() || this.isRideCharging();
        if (!activeRun) {
            this.lastBreathSoundTick = NEVER;
            return;
        }
        if (this.tickCount - this.lastBreathSoundTick >= BREATH_SOUND_INTERVAL_TICKS) {
            this.lastBreathSoundTick = this.tickCount;
            this.playSound(WildframeSoundEvents.MOOHORN_BREATHE, 0.7F, Mth.randomBetween(this.level().getRandom(), 0.9F, 1.1F));
        }
    }

    @Override
    protected void playStepSound(final BlockPos pos, final BlockState blockState) {
        if (this.isRunning() || this.isRideCharging()) {
            return;
        }
        SoundType soundType = blockState.getSoundType();
        this.playSound(soundType.getStepSound(), soundType.getVolume() * 0.3F, soundType.getPitch());
    }

    private SoundEvent getGallopSound(final BlockState state) {
        boolean isSoft = state.is(BlockTags.DIRT)
                || state.is(BlockTags.SAND)
                || state.is(BlockTags.SNOW)
                || state.is(Blocks.GRASS_BLOCK)
                || state.is(Blocks.MUD)
                || state.is(Blocks.CLAY)
                || state.is(Blocks.GRAVEL)
                || state.is(Blocks.SOUL_SAND)
                || state.is(Blocks.SOUL_SOIL)
                || state.is(Blocks.FARMLAND);
        return isSoft ? WildframeSoundEvents.MOOHORN_SOFT : WildframeSoundEvents.MOOHORN_WOOD;
    }
}
