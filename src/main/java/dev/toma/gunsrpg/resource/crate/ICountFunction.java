package dev.toma.gunsrpg.resource.crate;

import net.minecraft.util.ResourceLocation;

public interface ICountFunction {

    CountFunctionType<?> getFunctionType();

    int getCount();
}
