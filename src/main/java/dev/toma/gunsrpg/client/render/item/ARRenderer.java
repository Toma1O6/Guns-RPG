package dev.toma.gunsrpg.client.render.item;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.model.ModelAR;
import dev.toma.gunsrpg.common.ModRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ARRenderer extends TileEntityItemStackRenderer {

    private static final ResourceLocation TEXTURE = GunsRPG.makeResource("textures/items/sks.png");
    private final ModelAR model = new ModelAR();

    @Override
    public void renderByItem(ItemStack stack, float partialTicks) {
        if(stack.getItem() == ModRegistry.GRPGItems.ASSAULT_RIFLE) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
            model.doRender();
        }
    }
}
