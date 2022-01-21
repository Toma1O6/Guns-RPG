package dev.toma.gunsrpg.client.screen;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.container.LunchBoxContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class LunchBoxScreen extends GenericContainerScreen<LunchBoxContainer> {

    private static final ResourceLocation BACKGROUND = GunsRPG.makeResource("textures/screen/lunch_box.png");

    public LunchBoxScreen(LunchBoxContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
        imageHeight = 156;
    }

    @Override
    public ResourceLocation getBackgroundImage() {
        return BACKGROUND;
    }
}
