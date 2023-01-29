package dev.toma.gunsrpg.common.debuffs.event;

import com.mojang.serialization.Codec;
import dev.toma.gunsrpg.common.init.ModRegistries;
import dev.toma.gunsrpg.util.Identifiable;
import net.minecraft.util.ResourceLocation;

public final class DebuffStageEventType<E extends DebuffStageEvent> implements Identifiable {

    public static final Codec<DebuffStageEvent> EVENT_CODEC = ModRegistries.DEBUFF_STAGE_EVENT_TYPES.dispatch("type", DebuffStageEvent::getType, t -> t.codec);
    private final ResourceLocation typeIdentifier;
    private final Codec<E> codec;

    public DebuffStageEventType(ResourceLocation typeIdentifier, Codec<E> codec) {
        this.typeIdentifier = typeIdentifier;
        this.codec = codec;
    }

    @Override
    public ResourceLocation getIdentifier() {
        return typeIdentifier;
    }
}
