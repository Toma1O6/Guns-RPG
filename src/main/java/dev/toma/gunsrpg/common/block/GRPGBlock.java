package dev.toma.gunsrpg.common.block;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.init.CommonRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class GRPGBlock extends Block {

    public GRPGBlock(String name, Properties properties) {
        super(properties);
        this.setRegistryName(GunsRPG.makeResource(name));
        CommonRegistry.registerItemBlock(this);
    }

    public static GRPGBlock basic(String name, Material material) {
        return new GRPGBlock(name, Properties.of(material));
    }
}
