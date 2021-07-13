package dev.toma.gunsrpg.world.cap.events;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.function.Supplier;

public interface IWorldEventSpec extends INBTSerializable<CompoundNBT> {

    String getEventName();

    boolean isEventActive();

    void tick(World world, long currentDay);

    static IWorldEventSpec of(String name, Supplier<Integer> cycle, IWorldEventHandler handler) {
        return new WorldEventSpec(name, cycle, handler);
    }
}
