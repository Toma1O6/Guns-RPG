package dev.toma.gunsrpg.api.common.data;

import dev.toma.gunsrpg.world.cap.events.WorldEventSpec;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.function.Supplier;

public interface IWorldEventSpec extends INBTSerializable<CompoundNBT> {

    static IWorldEventSpec of(String name, Supplier<Integer> cycle, IWorldEventHandler handler) {
        return new WorldEventSpec(name, cycle, handler);
    }

    String getEventName();

    boolean isEventActive();

    void tick(World world, long currentDay);
}
