package dev.toma.gunsrpg.common.block;

import dev.toma.gunsrpg.common.container.CrateContainer;
import dev.toma.gunsrpg.common.tileentity.ILootGenerator;
import dev.toma.gunsrpg.common.tileentity.InventoryTileEntity;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public abstract class AbstractCrateBlock extends BaseBlock {

    public AbstractCrateBlock(String name, Properties properties) {
        super(name, properties);
    }

    public abstract ITextComponent getContainerTitle();

    public abstract boolean shouldDestroyEmptyBlock();

    public abstract <T extends InventoryTileEntity & ILootGenerator> T getNewBlockEntity();

    @Override
    public final void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!shouldDestroyEmptyBlock())
            return;
        TileEntity entity = world.getBlockEntity(pos);
        if (entity instanceof ILootGenerator) {
            ILootGenerator generator = (ILootGenerator) entity;
            if (generator.isEmptyInventory()) {
                world.destroyBlock(pos, false);
            }
        }
    }

    @Override
    public final void onRemove(BlockState state, World world, BlockPos pos, BlockState oldState, boolean p_196243_5_) {
        if (!state.is(oldState.getBlock())) {
            ModUtils.dropInventoryItems(world, pos);
        }
        super.onRemove(state, world, pos, oldState, p_196243_5_);
    }

    @Override
    public final boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public final TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return this.getNewBlockEntity();
    }

    @Override
    public final INamedContainerProvider getMenuProvider(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedContainerProvider(
                (windowID, playerInventory, player) -> new CrateContainer<>(windowID, playerInventory, (InventoryTileEntity & ILootGenerator) world.getBlockEntity(pos)),
                this.getContainerTitle()
        );
    }

    @Override
    public final ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        if (world.isClientSide)
            return ActionResultType.SUCCESS;
        else {
            NetworkHooks.openGui((ServerPlayerEntity) player, state.getMenuProvider(world, pos), pos);
            return ActionResultType.CONSUME;
        }
    }

    @Override
    public final List<ItemStack> getDrops(BlockState p_220076_1_, LootContext.Builder p_220076_2_) {
        return Collections.emptyList();
    }
}
