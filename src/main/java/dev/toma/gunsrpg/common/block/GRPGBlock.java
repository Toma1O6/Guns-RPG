package dev.toma.gunsrpg.common.block;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.ModTabs;
import dev.toma.gunsrpg.common.init.CommonRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class GRPGBlock extends Block {

    public GRPGBlock(String name, Material material) {
        super(material);
        this.setCreativeTab(ModTabs.BLOCK_TAB);
        this.setUnlocalizedName(name);
        this.setRegistryName(GunsRPG.makeResource(name));
        CommonRegistry.registerItemBlock(this);
    }
}
