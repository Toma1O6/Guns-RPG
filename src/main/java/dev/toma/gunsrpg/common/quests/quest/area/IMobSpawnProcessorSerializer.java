package dev.toma.gunsrpg.common.quests.quest.area;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.nbt.CompoundNBT;

public interface IMobSpawnProcessorSerializer<P extends IMobSpawnProcessor> {

    P deserialize(MobSpawnProcessorType<P> type, JsonElement element) throws JsonParseException;

    void toNbt(P processor, CompoundNBT nbt);

    P fromNbt(MobSpawnProcessorType<P> type, CompoundNBT nbt);
}
