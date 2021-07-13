package dev.toma.gunsrpg.client.render.item;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.model.ModelPistol;
import dev.toma.gunsrpg.common.init.GRPGItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class PistolRenderer extends TileEntityItemStackRenderer {

    private static final ResourceLocation TEXTURE = GunsRPG.makeResource("textures/items/p1911.png");
    private final ModelPistol p1911 = new ModelPistol();

    @Override
    public void renderByItem(ItemStack stack) {
        if(stack.getItem() == GRPGItems.PISTOL) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
            p1911.render(stack);
        }
    }
}
