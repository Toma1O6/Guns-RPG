package dev.toma.gunsrpg.client.render.item;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.model.ModelSR;
import dev.toma.gunsrpg.common.ModRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class SRRenderer extends TileEntityItemStackRenderer {

    private static final ResourceLocation TEXTURE = GunsRPG.makeResource("textures/items/kar98k.png");
    private final ModelSR model = new ModelSR();

    @Override
    public void renderByItem(ItemStack p_192838_1_, float partialTicks) {
        if(p_192838_1_.getItem() == ModRegistry.GRPGItems.SNIPER_RIFLE) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
            model.doRender();
        }
    }
}
