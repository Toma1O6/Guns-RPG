package dev.toma.gunsrpg.ai;

import dev.toma.gunsrpg.world.cap.WorldData;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import java.util.EnumSet;

public class BeAngryDuringBloodmoonGoal extends TargetGoal {

    private final EntityPredicate targetConditions;
    private int randomDelay;
    private PlayerEntity target;

    public BeAngryDuringBloodmoonGoal(MobEntity entity) {
        super(entity, false);
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
        targetConditions = new EntityPredicate().allowUnseeable().ignoreInvisibilityTesting().range(getFollowDistance()).selector(e -> e instanceof PlayerEntity && e.isAlive() && !((PlayerEntity) e).isCreative() && !e.isSpectator());
    }

    @Override
    public boolean canUse() {
        World level = mob.level;
        if (mob.getRandom().nextInt(10) != 0) {
            return false;
        } else {
            if (!WorldData.isBloodMoon(level))
                return false;
            findClosestPlayer();
            return target != null;
        }
    }

    @Override
    public void start() {
        mob.setTarget(target);
        super.start();
    }

    private void findClosestPlayer() {
        target = mob.level.getNearestPlayer(targetConditions, mob, mob.getX(), mob.getEyeY(), mob.getZ());
    }
}
