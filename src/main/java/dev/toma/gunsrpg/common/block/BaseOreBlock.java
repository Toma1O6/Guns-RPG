package dev.toma.gunsrpg.common.block;

import dev.toma.gunsrpg.common.init.CommonRegistry;
import net.minecraft.block.OreBlock;

public class BaseOreBlock extends OreBlock {

    public BaseOreBlock(String name, Properties properties) {
        super(properties);
        setRegistryName(name);
        CommonRegistry.registerItemBlock(this);
    }
}
