package dev.toma.gunsrpg.client.screen;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.container.AmmoCaseContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class AmmoCaseScreen extends GenericContainerScreen<AmmoCaseContainer> {

    private static final ResourceLocation BACKGROUND = GunsRPG.makeResource("textures/screen/ammo_case.png");

    public AmmoCaseScreen(AmmoCaseContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
    }

    @Override
    public ResourceLocation getBackgroundImage() {
        return BACKGROUND;
    }
}
