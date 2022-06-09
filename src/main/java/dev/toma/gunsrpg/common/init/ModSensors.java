package dev.toma.gunsrpg.common.init;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.ai.QuestPlayerSensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(GunsRPG.MODID)
public final class ModSensors {

    public static final SensorType<QuestPlayerSensor> QUEST_PLAYER_SENSOR = null;
}
