package dev.toma.gunsrpg.common.block;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.ModTabs;
import dev.toma.gunsrpg.common.ModRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class GRPGBlock extends Block {

    public GRPGBlock(String name) {
        super(Material.ROCK);
        this.setCreativeTab(ModTabs.BLOCK_TAB);
        this.setUnlocalizedName(name);
        this.setRegistryName(GunsRPG.makeResource(name));
        this.setSoundType(SoundType.STONE);
        this.setHarvestLevel("pickaxe", 2);
        this.setHardness(2.3F);
        this.setResistance(12.0F);
        ModRegistry.registerItemBlock(this);
    }
}
