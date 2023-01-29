package dev.toma.gunsrpg.common.debuffs.sources;

import com.mojang.serialization.Codec;
import dev.toma.gunsrpg.common.init.ModRegistries;
import dev.toma.gunsrpg.util.Identifiable;
import net.minecraft.util.ResourceLocation;

public final class DebuffSourceType<S extends DebuffSource> implements Identifiable {

    public static final Codec<DebuffSource> SOURCE_CODEC = ModRegistries.DEBUFF_SOURCE_TYPES.dispatch("type", DebuffSource::getType, t -> t.codec);
    private final ResourceLocation identifier;
    private final Codec<S> codec;

    public DebuffSourceType(ResourceLocation identifier, Codec<S> codec) {
        this.identifier = identifier;
        this.codec = codec;
    }

    @Override
    public ResourceLocation getIdentifier() {
        return identifier;
    }
}
