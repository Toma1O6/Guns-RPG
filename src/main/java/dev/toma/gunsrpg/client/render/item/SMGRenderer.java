package dev.toma.gunsrpg.client.render.item;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.model.ModelSMG;
import dev.toma.gunsrpg.common.init.GRPGItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class SMGRenderer extends TileEntityItemStackRenderer {

    private static final ResourceLocation TEXTURE = GunsRPG.makeResource("textures/items/ump45.png");
    private final ModelSMG modelSMG = new ModelSMG();

    @Override
    public void renderByItem(ItemStack p_192838_1_) {
        if(p_192838_1_.getItem() == GRPGItems.SMG) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
            modelSMG.render(p_192838_1_);
        }
    }
}
