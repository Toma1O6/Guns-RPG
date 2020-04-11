package dev.toma.gunsrpg.client.render.item;

import dev.toma.gunsrpg.client.model.ModelP1911;
import dev.toma.gunsrpg.common.ModRegistry;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;

public class PistolRenderer extends TileEntityItemStackRenderer {

    private final ModelP1911 p1911 = new ModelP1911();

    @Override
    public void renderByItem(ItemStack stack, float partialTicks) {
        if(stack.getItem() == ModRegistry.GRPGItems.PISTOL) {
            p1911.doRender();
        }
    }
}
