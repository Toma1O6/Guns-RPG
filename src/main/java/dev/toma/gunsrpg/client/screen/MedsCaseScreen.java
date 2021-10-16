package dev.toma.gunsrpg.client.screen;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.container.GrenadeCaseContainer;
import dev.toma.gunsrpg.common.container.MedsCaseContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class MedsCaseScreen extends GenericContainerScreen<MedsCaseContainer> {

    private static final ResourceLocation BACKGROUND = GunsRPG.makeResource("textures/screen/meds_case.png");

    public MedsCaseScreen(MedsCaseContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
    }

    @Override
    public ResourceLocation getBackgroundImage() {
        return BACKGROUND;
    }
}
