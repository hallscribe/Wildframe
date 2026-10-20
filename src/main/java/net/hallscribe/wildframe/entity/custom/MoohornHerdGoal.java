package net.hallscribe.wildframe.entity.custom;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public class MoohornHerdGoal extends Goal {
    private static final double HERD_RADIUS = 25.0;
    private static final double REGROUP_RADIUS = 12.0;
    private static final double SEARCH_RANGE = 64.0;
    private static final double ARRIVE_LAST_POS_SQR = 6.0 * 6.0;
    private static final int CHECK_INTERVAL = 20;
    private static final int REPATH_INTERVAL = 20;
    private static final int RETURN_TIMEOUT = 20 * 45;

    private final Moohorn mob;
    private final double speed;

    private @Nullable Moohorn rallyPoint;
    private @Nullable Vec3 lastHerdPos;
    private int checkDelay;
    private int repathDelay;
    private int returnTicks;

    public MoohornHerdGoal(final Moohorn mob, final double speed) {
        this.mob = mob;
        this.speed = speed;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.mob.isBaby() || this.mob.isVehicle() || this.mob.getTarget() != null) {
            return false;
        }
        if (--this.checkDelay > 0) {
            return false;
        }
        this.checkDelay = CHECK_INTERVAL + this.mob.getRandom().nextInt(10);

        List<Moohorn> mates = this.mob.getHerdAdults(SEARCH_RANGE);
        if (!mates.isEmpty()) {
            Vec3 centre = centreOf(mates);
            Moohorn anchor = closestTo(centre, mates);
            this.lastHerdPos = anchor.position();
            if (flatDistSqr(this.mob.position(), centre) > HERD_RADIUS * HERD_RADIUS) {
                this.rallyPoint = anchor;
                this.returnTicks = 0;
                return true;
            }
            return false;
        }

        if (this.lastHerdPos != null) {
            this.rallyPoint = null;
            this.returnTicks = 0;
            return true;
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.mob.isBaby() || this.mob.isVehicle() || this.mob.getTarget() != null) {
            return false;
        }
        if (++this.returnTicks > RETURN_TIMEOUT) {
            this.lastHerdPos = null;
            return false;
        }
        if (this.rallyPoint != null) {
            return this.rallyPoint.isAlive()
                    && flatDistSqr(this.mob.position(), this.rallyPoint.position()) > REGROUP_RADIUS * REGROUP_RADIUS;
        }
        return this.lastHerdPos != null;
    }

    @Override
    public void start() {
        this.repathDelay = 0;
    }

    @Override
    public void tick() {
        if (this.rallyPoint == null) {
            if (this.lastHerdPos == null) return;

            if (flatDistSqr(this.mob.position(), this.lastHerdPos) < ARRIVE_LAST_POS_SQR) {
                List<Moohorn> mates = this.mob.getHerdAdults(SEARCH_RANGE);
                if (mates.isEmpty()) {
                    this.lastHerdPos = null;
                    return;
                }
                this.rallyPoint = closestTo(centreOf(mates), mates);
            } else {
                if (--this.repathDelay <= 0) {
                    this.repathDelay = REPATH_INTERVAL;
                    this.mob.getNavigation().moveTo(this.lastHerdPos.x, this.lastHerdPos.y, this.lastHerdPos.z, this.speed);
                }
                return;
            }
        }

        if (--this.repathDelay <= 0) {
            this.repathDelay = REPATH_INTERVAL;
            this.mob.getNavigation().moveTo(this.rallyPoint, this.speed);
        }
    }

    @Override
    public void stop() {
        this.rallyPoint = null;
        this.mob.getNavigation().stop();
    }

    private static Vec3 centreOf(final List<Moohorn> mates) {
        double x = 0, y = 0, z = 0;
        for (Moohorn m : mates) {
            x += m.getX();
            y += m.getY();
            z += m.getZ();
        }
        int n = mates.size();
        return new Vec3(x / n, y / n, z / n);
    }

    private static Moohorn closestTo(final Vec3 point, final List<Moohorn> mates) {
        Moohorn best = mates.get(0);
        double bestDist = Double.MAX_VALUE;
        for (Moohorn m : mates) {
            double d = flatDistSqr(m.position(), point);
            if (d < bestDist) {
                bestDist = d;
                best = m;
            }
        }
        return best;
    }

    private static double flatDistSqr(final Vec3 a, final Vec3 b) {
        double dx = a.x - b.x;
        double dz = a.z - b.z;
        return dx * dx + dz * dz;
    }
}