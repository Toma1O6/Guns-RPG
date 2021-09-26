package dev.toma.gunsrpg.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import java.util.Objects;
import java.util.Set;

public class TrapBlock extends BaseBlock {

    public static final VoxelShape SHAPE = VoxelShapes.box(0.0, 0.0, 0.0, 1.0, 0.25, 1.0);
    private final ITrapReaction reaction;

    public TrapBlock(String name, Properties properties, ITrapReaction reaction) {
        super(name, properties);
        this.reaction = reaction;
    }

    @Override
    public void entityInside(BlockState state, World level, BlockPos pos, Entity entity) {
        applyTrapEffects(level, pos, entity);
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState p_220071_1_, IBlockReader p_220071_2_, BlockPos p_220071_3_, ISelectionContext p_220071_4_) {
        return VoxelShapes.empty();
    }

    @Override
    public boolean isPathfindable(BlockState p_196266_1_, IBlockReader p_196266_2_, BlockPos p_196266_3_, PathType p_196266_4_) {
        return true;
    }

    @Override
    public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, FluidState fluid) {
        if (reaction.requiresSpecialTool()) {
            applyTrapEffects(world, pos, player);
            return true;
        }
        return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
    }

    @Override
    public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (level.isClientSide) {
            return ActionResultType.PASS;
        } else {
            if (reaction.requiresSpecialTool()) {
                if (reaction.isValidDefuseTool(level, pos, player, hand)) {
                    level.destroyBlock(pos, true);
                    player.getItemInHand(hand).hurt(1, level.getRandom(), (ServerPlayerEntity) player);
                } else {
                    applyTrapEffects(level, pos, player);
                }
            }
            return ActionResultType.SUCCESS;
        }
    }

    @Override
    public void neighborChanged(BlockState p_220069_1_, World p_220069_2_, BlockPos p_220069_3_, Block p_220069_4_, BlockPos p_220069_5_, boolean p_220069_6_) {
        if (!canSurvive(p_220069_1_, p_220069_2_, p_220069_3_)) {
            p_220069_2_.destroyBlock(p_220069_3_, true);
        }
    }

    @Override
    public boolean canSurvive(BlockState state, IWorldReader reader, BlockPos pos) {
        BlockPos below = pos.below();
        BlockState stateBelow = reader.getBlockState(below);
        VoxelShape shape = stateBelow.getCollisionShape(reader, below);
        return stateBelow.getMaterial().isSolid() && shape.max(Direction.Axis.Y) == 1.0;
    }

    protected void onEntityCaught(World world, BlockPos pos, Entity victim) {

    }

    private void applyTrapEffects(World world, BlockPos pos, Entity victim) {
        if (reaction.applyTrapEffects(world, pos, victim)) {
            onEntityCaught(world, pos, victim);
        }
    }

    public interface ITrapReaction {

        boolean applyTrapEffects(World level, BlockPos pos, Entity entity);

        boolean requiresSpecialTool();

        /**
         * Called when player right clicks block and when {@link ITrapReaction#requiresSpecialTool()} returns true
         * @param level Active level
         * @param pos Block position
         * @param player Player who rightclicked this block
         * @param hand Used hand
         * @return Whether this tool can be used for defusal
         */
        boolean isValidDefuseTool(World level, BlockPos pos, PlayerEntity player, Hand hand);
    }

    public static class MineReaction implements ITrapReaction {

        private final float explosionPower;
        private final Explosion.Mode explosionMode;

        public MineReaction(float explosionPower) {
            this(explosionPower, Explosion.Mode.NONE);
        }

        public MineReaction(float explosionPower, Explosion.Mode explosionMode) {
            this.explosionPower = Math.max(1.0F, explosionPower);
            this.explosionMode = Objects.requireNonNull(explosionMode);
        }

        @Override
        public boolean applyTrapEffects(World level, BlockPos pos, Entity entity) {
            if (!level.isClientSide) {
                level.destroyBlock(pos, false);
                level.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, explosionPower, explosionMode);
            }
            return true;
        }

        @Override
        public boolean requiresSpecialTool() {
            return true;
        }

        @Override
        public boolean isValidDefuseTool(World level, BlockPos pos, PlayerEntity player, Hand hand) {
            ItemStack stack = player.getItemInHand(hand);
            Set<ToolType> toolTypes = stack.getToolTypes();
            return toolTypes.contains(ToolType.SHOVEL);
        }
    }
}
