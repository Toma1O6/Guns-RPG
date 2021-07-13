package dev.toma.gunsrpg.world.cap.events;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class WorldEventSpec implements IWorldEventSpec {
    private final String name;
    private final Supplier<Integer> cycle;
    private final IWorldEventHandler handler;
    private boolean active;
    private boolean old;

    public WorldEventSpec(String name, Supplier<Integer> cycle, IWorldEventHandler handler) {
        this.name = name;
        this.cycle = cycle;
        this.handler = handler;
    }

    @Override
    public void tick(World world, long currentDay) {
        if (disabled()) return;
        int dayCycle = cycle.get();
        active = currentDay > 0 && currentDay % dayCycle == 0 && handler.canTriggerEvent(world);
        if (active != old) {
            if (active)
                handler.eventStarted(world);
            else
                handler.eventFinished(world);
        }
        old = active;
    }

    @Override
    public boolean isEventActive() {
        return active;
    }

    @Override
    public String getEventName() {
        return name;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putBoolean("now", active);
        nbt.putBoolean("old", old);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        active = nbt.getBoolean("now");
        old = nbt.getBoolean("old");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorldEventSpec that = (WorldEventSpec) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public boolean disabled() {
        return cycle.get() < 0;
    }
}
