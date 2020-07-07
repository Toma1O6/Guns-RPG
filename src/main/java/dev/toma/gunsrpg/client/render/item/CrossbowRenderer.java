package dev.toma.gunsrpg.client.render.item;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.model.ModelCrossbow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class CrossbowRenderer extends TileEntityItemStackRenderer {

    private static final ResourceLocation TEXTURE = GunsRPG.makeResource("textures/items/crossbow.png");
    private final ModelCrossbow crossbow = new ModelCrossbow();

    @Override
    public void renderByItem(ItemStack stack) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
        crossbow.doRender(stack);
    }
}
