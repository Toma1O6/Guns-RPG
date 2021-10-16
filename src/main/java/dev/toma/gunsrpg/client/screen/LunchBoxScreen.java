package dev.toma.gunsrpg.client.screen;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.container.LunchBoxContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class LunchBoxScreen extends GenericContainerScreen<LunchBoxContainer> {

    private static final ResourceLocation BACKGROUND = GunsRPG.makeResource("textures/screen/lunchbox.png");

    public LunchBoxScreen(LunchBoxContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
    }

    @Override
    public ResourceLocation getBackgroundImage() {
        return BACKGROUND;
    }
}
