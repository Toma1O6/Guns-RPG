package dev.toma.gunsrpg.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootTable;
import net.minecraftforge.common.ToolType;

import java.util.Collections;
import java.util.List;

public class CrystalOre extends BaseBlock implements ICustomizableDrops {

    public CrystalOre(String name) {
        super(name, Properties.of(Material.STONE).strength(3.3F).harvestTool(ToolType.PICKAXE).harvestLevel(2));
    }

    @Override
    public List<ItemStack> getCustomDrops(LootTable table, LootContext context) {
        return Collections.emptyList();
    }
}
