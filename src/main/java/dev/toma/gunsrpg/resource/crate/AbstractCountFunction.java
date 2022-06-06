package dev.toma.gunsrpg.resource.crate;

import net.minecraft.util.ResourceLocation;

public abstract class AbstractCountFunction implements ICountFunction {

    private final ResourceLocation id;

    public AbstractCountFunction(ResourceLocation id) {
        this.id = id;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }
}
