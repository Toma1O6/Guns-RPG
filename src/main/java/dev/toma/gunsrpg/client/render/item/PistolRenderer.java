package dev.toma.gunsrpg.client.render.item;

import dev.toma.gunsrpg.client.model.ModelPistol;
import dev.toma.gunsrpg.common.ModRegistry;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;

public class PistolRenderer extends TileEntityItemStackRenderer {

    private final ModelPistol p1911 = new ModelPistol();

    @Override
    public void renderByItem(ItemStack stack, float partialTicks) {
        if(stack.getItem() == ModRegistry.GRPGItems.PISTOL) {
            p1911.doRender();
        }
    }
}
