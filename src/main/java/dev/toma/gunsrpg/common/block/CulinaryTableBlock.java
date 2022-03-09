package dev.toma.gunsrpg.common.block;

import dev.toma.gunsrpg.common.tileentity.CulinaryTableTileEntity;
import dev.toma.gunsrpg.common.tileentity.SkilledWorkbenchTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class CulinaryTableBlock extends SkilledWorkbenchBlock {

    private static final ITextComponent TITLE = new TranslationTextComponent("container.culinary_table");

    public CulinaryTableBlock(String name) {
        super(name);
    }

    @Nullable
    @Override
    public SkilledWorkbenchTileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CulinaryTableTileEntity();
    }

    @Override
    public ITextComponent getTitleComponent() {
        return TITLE;
    }
}
