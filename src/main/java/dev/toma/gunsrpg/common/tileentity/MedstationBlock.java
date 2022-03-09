package dev.toma.gunsrpg.common.tileentity;

import dev.toma.gunsrpg.common.block.SkilledWorkbenchBlock;
import dev.toma.gunsrpg.common.container.MedstationContainer;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

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

    @Override
    public IContainerProvider getContainerProvider(World world, BlockPos pos) {
        return (id, inv, ent) -> new MedstationContainer(id, inv, (MedstationTileEntity) world.getBlockEntity(pos));
    }

    @Nullable
    @Override
    public SkilledWorkbenchTileEntity createTileEntity(BlockState state, IBlockReader reader) {
        return new MedstationTileEntity();
    }
}
