package dev.toma.gunsrpg.client.render.item;

import dev.toma.gunsrpg.client.model.ModelSMG;
import dev.toma.gunsrpg.common.ModRegistry;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;

public class SMGRenderer extends TileEntityItemStackRenderer {

    private ModelSMG modelSMG = new ModelSMG();

    @Override
    public void renderByItem(ItemStack p_192838_1_, float partialTicks) {
        if(p_192838_1_.getItem() == ModRegistry.GRPGItems.SMG) {
            modelSMG.doRender();
        }
    }
}
