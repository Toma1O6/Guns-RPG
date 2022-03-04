package dev.toma.gunsrpg.common.block;

import net.minecraft.block.material.Material;

public class CulinaryTableBlock extends BaseHorizontalBlock {

    public CulinaryTableBlock(String name) {
        super(name, Properties.of(Material.WOOD).strength(2.2F).noOcclusion());
    }
}
