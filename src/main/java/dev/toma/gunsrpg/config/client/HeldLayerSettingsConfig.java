package dev.toma.gunsrpg.config.client;

import dev.toma.configuration.config.Configurable;
import dev.toma.gunsrpg.util.object.LazyLoader;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class HeldLayerSettingsConfig implements IHeldLayerSettings {

    @Configurable
    public final Mode mode;

    @Configurable
    @Configurable.StringPattern(
            value = "[a-z]+:[a-z0-9]+",
            defaultValue = "minecraft:air",
            errorDescriptor = "gunsrpg.config.error.invalid_id"
    )
    public final String itemId;
    private final LazyLoader<ItemStack> renderItem;

    public HeldLayerSettingsConfig(Mode mode, String id) {
        this.mode = mode;
        this.itemId = id;
        this.renderItem = new LazyLoader<>(() -> {
            ResourceLocation location = new ResourceLocation(itemId);
            Item item = ForgeRegistries.ITEMS.getValue(location);
            return new ItemStack(item);
        });
    }

    @Override
    public Mode getRenderingMode() {
        return mode;
    }

    @Override
    public ItemStack getRenderItem() {
        return renderItem.get();
    }
}
