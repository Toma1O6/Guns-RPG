package dev.toma.gunsrpg.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class CrystalStationBlock extends BaseBlock {

    public CrystalStationBlock(String name) {
        super(name, Properties.of(Material.STONE).strength(3.6F).harvestTool(ToolType.PICKAXE).harvestLevel(0).requiresCorrectToolForDrops().noOcclusion());
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
}
