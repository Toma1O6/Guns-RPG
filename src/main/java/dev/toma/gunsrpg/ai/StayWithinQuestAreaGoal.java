package dev.toma.gunsrpg.ai;

import dev.toma.gunsrpg.common.quests.quest.area.QuestArea;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.Heightmap;

public final class StayWithinQuestAreaGoal extends Goal {

    private final MobEntity mob;
    private final QuestArea area;

    public StayWithinQuestAreaGoal(MobEntity mob, QuestArea area) {
        this.mob = mob;
        this.area = area;
    }

    @Override
    public boolean canUse() {
        return !this.area.isInArea(this.mob);
    }

    @Override
    public void start() {
        BlockPos centerPos = this.area.getCenter();
        int x = centerPos.getX();
        int z = centerPos.getZ();
        int y = this.mob.level.getHeight(Heightmap.Type.WORLD_SURFACE, x, z);
        this.mob.getNavigation().moveTo(x + 0.5, y, z + 0.5, 1.2D);
        this.mob.setSprinting(true);
    }

    @Override
    public void stop() {
        this.mob.setSprinting(false);
    }
}
