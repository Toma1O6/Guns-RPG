package dev.toma.gunsrpg.integration.questing.task.item;

import com.mojang.serialization.Codec;
import dev.toma.gunsrpg.common.init.QuestRegistry;
import dev.toma.questing.utils.IdentifierHolder;
import net.minecraft.util.ResourceLocation;

public final class ItemProviderType<S extends ItemProvider> implements IdentifierHolder {

    public static final Codec<ItemProvider> CODEC = QuestRegistry.ITEM_PROVIDERS.dispatch("type", ItemProvider::getType, type -> type.codec);
    private final ResourceLocation identifier;
    private final Codec<S> codec;

    public ItemProviderType(ResourceLocation identifier, Codec<S> codec) {
        this.identifier = identifier;
        this.codec = codec;
    }

    @Override
    public ResourceLocation getIdentifier() {
        return identifier;
    }
}
