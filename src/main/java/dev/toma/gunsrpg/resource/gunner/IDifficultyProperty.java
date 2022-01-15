package dev.toma.gunsrpg.resource.gunner;

import net.minecraft.world.Difficulty;

@FunctionalInterface
public interface IDifficultyProperty<P> {

    P getProperty(Difficulty difficulty);
}
