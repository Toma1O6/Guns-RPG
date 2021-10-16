package dev.toma.gunsrpg.client.screen;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.container.AmmoCaseContainer;
import dev.toma.gunsrpg.common.container.GrenadeCaseContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class GrenadeCaseScreen extends GenericContainerScreen<GrenadeCaseContainer> {

    private static final ResourceLocation BACKGROUND = GunsRPG.makeResource("textures/screen/grenade_case.png");

    public GrenadeCaseScreen(GrenadeCaseContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
    }

    @Override
    public ResourceLocation getBackgroundImage() {
        return BACKGROUND;
    }
}
