package dev.toma.gunsrpg.api.common.data;

import net.minecraft.world.World;

public interface IWorldEventHandler {
    void eventStarted(World world);

    void eventFinished(World world);

    boolean canTriggerEvent(World world);
}
