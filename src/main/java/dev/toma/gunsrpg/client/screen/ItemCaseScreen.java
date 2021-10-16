package dev.toma.gunsrpg.client.screen;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.container.ItemCaseContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ItemCaseScreen extends GenericContainerScreen<ItemCaseContainer> {

    private static final ResourceLocation BACKGROUND = GunsRPG.makeResource("textures/screen/item_case.png");

    public ItemCaseScreen(ItemCaseContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
    }

    @Override
    public ResourceLocation getBackgroundImage() {
        return BACKGROUND;
    }
}
