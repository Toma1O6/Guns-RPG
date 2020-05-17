package dev.toma.gunsrpg.client.render.item;

import dev.toma.gunsrpg.client.model.ModelSR;
import dev.toma.gunsrpg.common.ModRegistry;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;

public class SRRenderer extends TileEntityItemStackRenderer {

    private final ModelSR model = new ModelSR();

    @Override
    public void renderByItem(ItemStack p_192838_1_, float partialTicks) {
        if(p_192838_1_.getItem() == ModRegistry.GRPGItems.SNIPER_RIFLE) {
            model.doRender();
        }
    }
}
