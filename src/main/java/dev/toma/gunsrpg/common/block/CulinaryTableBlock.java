package dev.toma.gunsrpg.common.block;

import dev.toma.gunsrpg.common.container.CulinaryTableContainer;
import dev.toma.gunsrpg.common.tileentity.CulinaryTableTileEntity;
import dev.toma.gunsrpg.common.tileentity.SkilledWorkbenchTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

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
    public IContainerProvider getContainerProvider(World world, BlockPos pos) {
        return (id, inv, ent) -> new CulinaryTableContainer(id, inv, (CulinaryTableTileEntity) world.getBlockEntity(pos));
    }

    @Override
    public ITextComponent getTitleComponent() {
        return TITLE;
    }
}
