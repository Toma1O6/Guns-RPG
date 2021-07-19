package dev.toma.gunsrpg.common.block;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.init.CommonRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BaseBlock extends Block {

    public BaseBlock(String name, Properties properties) {
        super(properties);
        this.setRegistryName(GunsRPG.makeResource(name));
        CommonRegistry.registerItemBlock(this);
    }

    public static BaseBlock basic(String name, Material material) {
        return new BaseBlock(name, Properties.of(material));
    }
}
