package dev.toma.gunsrpg.common.block;

import dev.toma.gunsrpg.common.tileentity.ILootGenerator;
import dev.toma.gunsrpg.common.tileentity.InventoryTileEntity;
import dev.toma.gunsrpg.common.tileentity.MilitaryCrateTileEntity;
import net.minecraft.block.material.Material;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ToolType;

public class MilitaryCrateBlock extends AbstractCrateBlock {

    private static final ITextComponent TITLE = new TranslationTextComponent("container.crate");

    public MilitaryCrateBlock(String name) {
        super(name, Properties.of(Material.WOOD).strength(2.5F).harvestTool(ToolType.AXE));
    }

    @Override
    public ITextComponent getContainerTitle() {
        return TITLE;
    }

    @Override
    public boolean shouldDestroyEmptyBlock() {
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends InventoryTileEntity & ILootGenerator> T getNewBlockEntity() {
        return (T) new MilitaryCrateTileEntity();
    }
}
