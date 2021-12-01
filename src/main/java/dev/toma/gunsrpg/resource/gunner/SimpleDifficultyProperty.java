package dev.toma.gunsrpg.resource.gunner;

import net.minecraft.world.Difficulty;

public class SimpleDifficultyProperty<P> implements IDifficultyProperty<P> {

    private final P prop;

    public SimpleDifficultyProperty(P prop) {
        this.prop = prop;
    }

    @Override
    public P getProperty(Difficulty difficulty) {
        return prop;
    }
}
