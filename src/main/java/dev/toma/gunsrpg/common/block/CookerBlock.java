package dev.toma.gunsrpg.common.block;

import dev.toma.gunsrpg.common.tileentity.CookerTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.Random;

public class CookerBlock extends BaseBlock {

    private static final ITextComponent TITLE = new TranslationTextComponent("container.cooker");

    public CookerBlock(String name) {
        super(name, Properties.of(Material.STONE).strength(2.2F, 16.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1));
        registerDefaultState(stateDefinition.any().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH).setValue(BlockStateProperties.LIT, false));
    }

    @Override
    public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.getValue(BlockStateProperties.LIT)) {
            double x = (double)pos.getX() + 0.5D;
            double y = pos.getY();
            double z = (double)pos.getZ() + 0.5D;
            if (random.nextDouble() <= 0.2) {
                world.playSound(null, x, y, z, SoundEvents.FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
            Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            Direction.Axis axis = direction.getAxis();
            double posOffset = 0.52D;
            double randomOffset = random.nextDouble() * 0.6D - 0.3D;
            double xPos = axis == Direction.Axis.X ? (double)direction.getStepX() * posOffset : randomOffset;
            double yPos = random.nextDouble() * 6.0D / 16.0D;
            double zPos = axis == Direction.Axis.Z ? (double)direction.getStepZ() * posOffset : randomOffset;
            world.addParticle(ParticleTypes.SMOKE, x + xPos, y + yPos, z + zPos, 0.0D, 0.0D, 0.0D);
            world.addParticle(ParticleTypes.FLAME, x + xPos, y + yPos, z + zPos, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        return state.getValue(BlockStateProperties.LIT) ? 12 : 0;
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState oldState, boolean p_196243_5_) {
        if (!state.is(oldState.getBlock())) {
            TileEntity tile = world.getBlockEntity(pos);
            if (tile instanceof CookerTileEntity) {
                CookerTileEntity tileEntity = (CookerTileEntity) tile;
                InventoryHelper.dropContents(world, pos, tileEntity);
                tileEntity.getRecipesForAwardsWithExp(world, Vector3d.atCenterOf(pos));
                world.updateNeighbourForOutputSignal(pos, this);
            }
        }
        super.onRemove(state, world, pos, oldState, p_196243_5_);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, World level, BlockPos pos) {
        return Container.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CookerTileEntity();
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING).add(BlockStateProperties.LIT);
    }

    // TODO --- all below
    @Override
    public ActionResultType use(BlockState p_225533_1_, World p_225533_2_, BlockPos p_225533_3_, PlayerEntity p_225533_4_, Hand p_225533_5_, BlockRayTraceResult p_225533_6_) {
        return super.use(p_225533_1_, p_225533_2_, p_225533_3_, p_225533_4_, p_225533_5_, p_225533_6_);
    }

    @Nullable
    @Override
    public INamedContainerProvider getMenuProvider(BlockState p_220052_1_, World p_220052_2_, BlockPos p_220052_3_) {
        return super.getMenuProvider(p_220052_1_, p_220052_2_, p_220052_3_);
    }
}
