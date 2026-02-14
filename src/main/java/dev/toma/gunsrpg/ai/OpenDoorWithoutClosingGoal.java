package dev.toma.gunsrpg.ai;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.OpenDoorGoal;

public class OpenDoorWithoutClosingGoal extends OpenDoorGoal {

    public OpenDoorWithoutClosingGoal(MobEntity mob) {
        super(mob, false);
    }

    @Override
    public void stop() {
    }
}
