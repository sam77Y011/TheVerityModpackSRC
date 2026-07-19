package net.tripleteamsstudio.unknownversionpt4.server.goal;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public class StareAtPlayerGoal extends Goal {

    private final Mob mob;
    private final double range;
    private Player targetPlayer;

    public StareAtPlayerGoal(Mob mob, double range) {
        this.mob = mob;
        this.range = range;
        this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        Player nearest = mob.level().getNearestPlayer(mob, range);
        if (nearest == null || nearest.isSpectator() || nearest.isCreative()) {
            return false;
        }
        this.targetPlayer = nearest;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return targetPlayer != null
                && targetPlayer.isAlive()
                && mob.distanceToSqr(targetPlayer) <= range * range
                && !targetPlayer.isSpectator()
                && !targetPlayer.isCreative();
    }

    @Override
    public void start() {
        mob.getNavigation().stop();
    }

    @Override
    public void stop() {
        targetPlayer = null;
    }

    @Override
    public void tick() {
        if (targetPlayer == null) return;

        double dx = targetPlayer.getX() - mob.getX();
        double dz = targetPlayer.getZ() - mob.getZ();
        float targetYaw = (float) (Mth.atan2(dz, dx) * (180D / Math.PI)) - 90F;

        mob.setYRot(targetYaw);
        mob.yBodyRot = targetYaw;
        mob.yHeadRot = targetYaw;
        mob.getLookControl().setLookAt(targetPlayer, 180F, 180F);

        mob.getNavigation().stop();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}