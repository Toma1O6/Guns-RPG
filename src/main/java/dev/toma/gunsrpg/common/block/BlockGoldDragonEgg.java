package dev.toma.gunsrpg.common.block;

import dev.toma.gunsrpg.common.entity.EntityGoldDragon;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.boss.dragon.phase.PhaseType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class BlockGoldDragonEgg extends GRPGBlock {

    public BlockGoldDragonEgg(String name) {
        super(name, Properties.of(Material.EGG));
        registerDefaultState(getStateDefinition().any().setValue(BlockStateProperties.LIT, false));
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        if (world.isClientSide)
            return ActionResultType.SUCCESS;
        else {
            ItemStack stack = player.getItemInHand(hand);
            if (stack.getItem() == Items.FLINT_AND_STEEL) {
                if (!state.getValue(BlockStateProperties.LIT)) {
                    world.setBlock(pos, state.setValue(BlockStateProperties.LIT, true), 3);
                    stack.hurt(1, player.getRandom(), (ServerPlayerEntity) player);
                }
            }
            return ActionResultType.CONSUME;
        }
    }

    @Override
    public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.getValue(BlockStateProperties.LIT)) {
            world.addParticle(ParticleTypes.FLAME, pos.getX() + random.nextDouble(), pos.getY(), pos.getZ() + random.nextDouble(), 0.0, 0.05, 0.0);
        }
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!world.isClientSide && state.getValue(BlockStateProperties.LIT)) {
            boolean spawned = false;
            if (world.canSeeSky(pos)) {
                EntityGoldDragon dragon = new EntityGoldDragon(world);
                dragon.setPos(pos.getX(), pos.getY() + 30, pos.getZ());
                dragon.getPhaseManager().setPhase(PhaseType.HOLDING_PATTERN);
                world.addFreshEntity(dragon);
                spawned = true;
            }
            world.destroyBlock(pos, !spawned);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.LIT);
    }
}
