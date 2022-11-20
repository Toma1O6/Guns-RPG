package dev.toma.gunsrpg.config.client;

import dev.toma.configuration.client.IValidationHandler;
import dev.toma.configuration.config.Configurable;
import dev.toma.configuration.config.validate.ValidationResult;
import dev.toma.gunsrpg.util.object.LazyLoader;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistries;

public class HeldLayerSettingsConfig implements IHeldLayerSettings {

    @Configurable
    @Configurable.Comment({
            "Rendering mode",
            "DEFAULT - Renders actual held weapon, might have larger perfomarmance impact",
            "STATIC - Renders item which is configured below",
            "NONE - No held item is rendered"
    })
    public Mode mode;

    @Configurable
    @Configurable.StringPattern(
            value = "[a-z]+:[a-z0-9]+",
            defaultValue = "minecraft:air",
            errorDescriptor = "text.config.validation.error.format.resourcelocation"
    )
    @Configurable.Comment("ID of item to be rendered with STATIC held item render mode")
    @Configurable.ValueUpdateCallback(method = "validateItemId")
    @Configurable.Gui.CharacterLimit(64)
    public String itemId;
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

    private void validateItemId(String itemId, IValidationHandler handler) {
        ResourceLocation location = new ResourceLocation(itemId);
        if (!ForgeRegistries.ITEMS.containsKey(location)) {
            handler.setValidationResult(ValidationResult.warn(new TranslationTextComponent("text.config.validation.invalid_id.item", itemId)));
        }
    }
}
