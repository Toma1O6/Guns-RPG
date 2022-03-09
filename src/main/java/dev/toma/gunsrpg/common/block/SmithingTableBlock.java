package dev.toma.gunsrpg.common.block;

import dev.toma.gunsrpg.common.container.SmithingTableContainer;
import dev.toma.gunsrpg.common.tileentity.SkilledWorkbenchTileEntity;
import dev.toma.gunsrpg.common.tileentity.SmithingTableTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class SmithingTableBlock extends SkilledWorkbenchBlock {

    private static final ITextComponent TITLE = new TranslationTextComponent("container.smithing_table");

    public SmithingTableBlock(String name) {
        super(name);
    }

    @Nullable
    @Override
    public SkilledWorkbenchTileEntity createTileEntity(BlockState state, IBlockReader reader) {
        return new SmithingTableTileEntity();
    }

    @Override
    public IContainerProvider getContainerProvider(World world, BlockPos pos) {
        return (id, inv, ent) -> new SmithingTableContainer(id, inv, (SmithingTableTileEntity) world.getBlockEntity(pos));
    }

    @Override
    public ITextComponent getTitleComponent() {
        return TITLE;
    }
}
