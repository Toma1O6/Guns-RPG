package dev.toma.gunsrpg.common.tileentity;

import dev.toma.gunsrpg.common.block.SkilledWorkbenchBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class MedstationBlock extends SkilledWorkbenchBlock {

    private static final ITextComponent TITLE = new TranslationTextComponent("container.medstation");

    public MedstationBlock(String name) {
        super(name);
    }

    @Override
    public ITextComponent getTitleComponent() {
        return TITLE;
    }

    @Nullable
    @Override
    public SkilledWorkbenchTileEntity createTileEntity(BlockState state, IBlockReader reader) {
        return new MedstationTileEntity();
    }
}
