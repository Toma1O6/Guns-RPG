package dev.toma.gunsrpg.resource.crate;

import com.mojang.serialization.Codec;
import net.minecraft.util.ResourceLocation;

public final class CountFunctionType<F extends ICountFunction> {

    public static final Codec<ICountFunction> CODEC = CountFunctionRegistry.INSTANCE.dispatch("function", ICountFunction::getFunctionType, t -> t.codec);
    private final ResourceLocation id;
    @Deprecated
    private final ICountFunctionAdapter<F> adapter;
    private final Codec<F> codec;

    public CountFunctionType(ResourceLocation id, ICountFunctionAdapter<F> adapter, Codec<F> codec) {
        this.id = id;
        this.adapter = adapter;
        this.codec = codec;
    }

    public ResourceLocation getId() {
        return id;
    }

    public ICountFunctionAdapter<F> getAdapter() {
        return adapter;
    }
}
