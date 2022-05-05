package dev.toma.gunsrpg.config.client;

import dev.toma.configuration.api.IConfigWriter;
import dev.toma.configuration.api.IObjectSpec;
import dev.toma.configuration.api.Restrictions;
import dev.toma.configuration.api.type.EnumType;
import dev.toma.configuration.api.type.ObjectType;
import dev.toma.configuration.api.type.StringType;
import dev.toma.gunsrpg.util.object.LazyLoader;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.regex.Pattern;

public class HeldLayerConfig extends ObjectType implements IHeldLayerConfig {

    private static final Pattern ITEM_ID = Pattern.compile("[a-z]+:[a-z0-9]+");

    private final EnumType<Mode> mode;
    private final StringType itemId;
    private final LazyLoader<ItemStack> renderItem;

    public HeldLayerConfig(IObjectSpec spec, Mode mode, String id) {
        super(spec);
        IConfigWriter writer = spec.getWriter();
        this.mode = writer.writeEnum("Render mode", mode);
        this.itemId = writer.writeRestrictedString("Static item ID", id, Restrictions.restrictStringByPattern(ITEM_ID, false, "Incorrect ID format"));
        this.renderItem = new LazyLoader<>(() -> {
            ResourceLocation location = new ResourceLocation(itemId.get());
            Item item = ForgeRegistries.ITEMS.getValue(location);
            return new ItemStack(item);
        });
    }

    @Override
    public Mode getRenderingMode() {
        return mode.get();
    }

    @Override
    public ItemStack getRenderItem() {
        return renderItem.get();
    }
}
