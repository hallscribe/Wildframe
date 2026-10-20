package net.hallscribe.wildframe.entity.custom;

import net.minecraft.world.entity.ai.goal.Goal;
import org.jspecify.annotations.Nullable;

import java.util.EnumSet;

public class BabyFollowGuardianGoal extends Goal {
    private static final double START_SQR = 5.0 * 5.0;
    private static final double STOP_SQR = 3.0 * 3.0;
    private static final double GIVE_UP_SQR = 64.0 * 64.0;

    private final Moohorn mob;
    private final double speed;
    private @Nullable Moohorn leader;
    private int repathDelay;

    public BabyFollowGuardianGoal(final Moohorn mob, final double speed) {
        this.mob = mob;
        this.speed = speed;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    private @Nullable Moohorn findLeader() {
        Moohorn g = this.mob.getGuardian();
        if (g != null && g.isAlive() && !g.isBaby()) {
            return g;
        }
        Moohorn best = null;
        double bestDist = Double.MAX_VALUE;
        for (Moohorn a : this.mob.getHerdAdults(16.0)) {
            double d = this.mob.distanceToSqr(a);
            if (d < bestDist) {
                bestDist = d;
                best = a;
            }
        }
        return best;
    }

    @Override
    public boolean canUse() {
        if (!this.mob.isBaby() || this.mob.isVehicle()) return false;
        Moohorn l = this.findLeader();
        if (l == null) return false;
        double d = this.mob.distanceToSqr(l);
        if (d < START_SQR || d > GIVE_UP_SQR) return false;
        this.leader = l;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return this.leader != null && this.leader.isAlive() && !this.mob.isVehicle()
                && this.mob.distanceToSqr(this.leader) > STOP_SQR
                && this.mob.distanceToSqr(this.leader) < GIVE_UP_SQR;
    }

    @Override
    public void start() {
        this.repathDelay = 0;
    }

    @Override
    public void tick() {
        if (this.leader == null) return;
        if (--this.repathDelay <= 0) {
            this.repathDelay = 10;
            this.mob.getNavigation().moveTo(this.leader, this.speed);
        }
    }

    @Override
    public void stop() {
        this.leader = null;
        this.mob.getNavigation().stop();
    }
}
