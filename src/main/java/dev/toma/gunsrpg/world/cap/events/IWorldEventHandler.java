package dev.toma.gunsrpg.world.cap.events;

import net.minecraft.world.World;

public interface IWorldEventHandler {
    void eventStarted(World world);

    void eventFinished(World world);

    boolean canTriggerEvent(World world);
}
