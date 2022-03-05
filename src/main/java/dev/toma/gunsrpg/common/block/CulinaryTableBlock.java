package dev.toma.gunsrpg.common.block;

import dev.toma.gunsrpg.common.container.CulinaryTableContainer;
import dev.toma.gunsrpg.common.tileentity.CulinaryTableTileEntity;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class CulinaryTableBlock extends BaseHorizontalBlock {

    private static final ITextComponent TITLE = new TranslationTextComponent("container.culinary_table");

    public CulinaryTableBlock(String name) {
        super(name, Properties.of(Material.WOOD).strength(2.2F).noOcclusion());
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        if (world.isClientSide)
            return ActionResultType.SUCCESS;
        else {
            NetworkHooks.openGui((ServerPlayerEntity) player, getMenuProvider(state, world, pos), pos);
            return ActionResultType.CONSUME;
        }
    }

    @Override
    public INamedContainerProvider getMenuProvider(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedContainerProvider((id, inventory, player) -> new CulinaryTableContainer(id, inventory, (CulinaryTableTileEntity) world.getBlockEntity(pos)), TITLE);
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState oldState, boolean p_196243_5_) {
        if (!state.is(oldState.getBlock())) {
            ModUtils.dropInventoryItems(world, pos);
        }
        super.onRemove(state, world, pos, oldState, p_196243_5_);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CulinaryTableTileEntity();
    }
}
