package dev.toma.gunsrpg.common.block;

import dev.toma.gunsrpg.common.container.AirdropContainer;
import dev.toma.gunsrpg.common.tileentity.AirdropTileEntity;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
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
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BlockAirdrop extends GRPGBlock {

    private static final ITextComponent TITLE = new TranslationTextComponent("container.airdrop");

    public BlockAirdrop(String name) {
        super(name, Properties.of(Material.METAL).sound(SoundType.METAL).strength(-1.0F, 3600000.0F).noDrops().randomTicks());
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        TileEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof AirdropTileEntity) {
            AirdropTileEntity airdrop = (AirdropTileEntity) tileEntity;
            if (airdrop.isEmpty()) {
                world.destroyBlock(pos, false);
            }
        }
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState oldState, boolean p_196243_5_) {
        if (!state.is(oldState.getBlock())) {
            ModUtils.dropInventoryItems(world, pos);
        }
        super.onRemove(state, world, pos, oldState, p_196243_5_);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new AirdropTileEntity();
    }

    @Override
    public INamedContainerProvider getMenuProvider(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedContainerProvider(
                (windowID, playerInventory, player) -> new AirdropContainer(windowID, playerInventory, (AirdropTileEntity) world.getBlockEntity(pos)),
                TITLE
        );
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        if (world.isClientSide)
            return ActionResultType.SUCCESS;
        else {
            NetworkHooks.openGui((ServerPlayerEntity) player, state.getMenuProvider(world, pos), pos);
            return ActionResultType.CONSUME;
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState p_220076_1_, LootContext.Builder p_220076_2_) {
        return Collections.emptyList();
    }
}
