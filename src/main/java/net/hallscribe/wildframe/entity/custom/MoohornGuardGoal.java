package net.hallscribe.wildframe.entity.custom;

import net.minecraft.world.entity.ai.goal.Goal;
import org.jspecify.annotations.Nullable;

import java.util.EnumSet;

public class MoohornGuardGoal extends Goal {
    private static final double SEARCH_RANGE = 48.0;
    private static final double TOO_FAR_SQR = 18.0 * 18.0;
    private static final double ARRIVE_SQR = 8.0 * 8.0;
    private static final int CHECK_INTERVAL = 10;

    private final Moohorn mob;
    private final double speed;
    private @Nullable Moohorn baby;
    private int checkDelay;
    private int repathDelay;

    public MoohornGuardGoal(final Moohorn mob, final double speed) {
        this.mob = mob;
        this.speed = speed;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.mob.isBaby() || this.mob.isVehicle() || this.mob.getTarget() == null) {
            return false;
        }
        if (--this.checkDelay > 0) {
            return false;
        }
        this.checkDelay = CHECK_INTERVAL;
        this.baby = this.findBabyToGuard();
        return this.baby != null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.baby != null
                && this.baby.isAlive()
                && this.mob.getTarget() != null
                && !this.mob.isVehicle()
                && this.baby.getGuardian() == this.mob
                && this.mob.distanceToSqr(this.baby) > ARRIVE_SQR;
    }

    @Override
    public void start() {
        this.repathDelay = 0;
    }

    @Override
    public void tick() {
        if (this.baby == null) return;
        this.mob.getLookControl().setLookAt(this.baby, 30.0F, 30.0F);
        if (--this.repathDelay <= 0) {
            this.repathDelay = 10;
            this.mob.getNavigation().moveTo(this.baby, this.speed);
        }
    }

    @Override
    public void stop() {
        this.baby = null;
        this.mob.getNavigation().stop();
    }

    private @Nullable Moohorn findBabyToGuard() {
        Moohorn best = null;
        double bestDist = Double.MAX_VALUE;
        for (Moohorn b : this.mob.level().getEntitiesOfClass(Moohorn.class,
                this.mob.getBoundingBox().inflate(SEARCH_RANGE, 16.0, SEARCH_RANGE),
                o -> o != this.mob && o.isBaby() && o.isAlive())) {
            double d = this.mob.distanceToSqr(b);
            if (d <= TOO_FAR_SQR || d >= bestDist) continue;

            Moohorn current = b.getGuardian();
            boolean free = current == null || !current.isAlive() || current.isBaby()
                    || current.distanceToSqr(b) > 64.0 * 64.0;
            if (free || current == this.mob) {
                best = b;
                bestDist = d;
            }
        }
        if (best != null) {
            best.setGuardian(this.mob);
        }
        return best;
    }
}