package dev.toma.gunsrpg.client.render.item;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.model.ModelShotgun;
import dev.toma.gunsrpg.common.init.GRPGItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class SGRenderer extends TileEntityItemStackRenderer {

    private static final ResourceLocation TEXTURE = GunsRPG.makeResource("textures/items/sawedoff.png");
    private final ModelShotgun model = new ModelShotgun();

    @Override
    public void renderByItem(ItemStack itemStackIn) {
        if(itemStackIn.getItem() == GRPGItems.SHOTGUN) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
            model.doRender(itemStackIn);
        }
    }
}
