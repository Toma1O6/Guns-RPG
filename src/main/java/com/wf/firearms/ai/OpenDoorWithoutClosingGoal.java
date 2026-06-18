package com.wf.firearms.ai;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;

/** 开门后不自动关门（血月破门用）。 */
public class OpenDoorWithoutClosingGoal extends OpenDoorGoal {
    public OpenDoorWithoutClosingGoal(Mob mob) {
        super(mob, false);
    }

    @Override
    public void stop() {}
}
