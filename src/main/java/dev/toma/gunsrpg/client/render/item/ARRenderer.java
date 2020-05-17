package dev.toma.gunsrpg.client.render.item;

import dev.toma.gunsrpg.client.model.ModelAR;
import dev.toma.gunsrpg.common.ModRegistry;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;

public class ARRenderer extends TileEntityItemStackRenderer {

    private final ModelAR model = new ModelAR();

    @Override
    public void renderByItem(ItemStack stack, float partialTicks) {
        if(stack.getItem() == ModRegistry.GRPGItems.ASSAULT_RIFLE) {
            model.doRender();
        }
    }
}
