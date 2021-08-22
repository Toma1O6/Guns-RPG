package dev.toma.gunsrpg.common.block;

import dev.toma.gunsrpg.common.init.ModDamageSources;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;
import java.util.function.Consumer;

public class SpikeTrapBlock extends TrapBlock {

    public static final IntegerProperty DAMAGE_STATE = IntegerProperty.create("damage", 0, 3);
    private final float damageChance;

    public SpikeTrapBlock(String name, int durability, float damage, Properties properties) {
        this(name, durability, damage, entity -> {}, properties);
        registerDefaultState(stateDefinition.any().setValue(DAMAGE_STATE, 0));
    }

    public SpikeTrapBlock(String name, int durability, float damage, Consumer<Entity> effect, Properties properties) {
        super(name, properties, new SpikeReaction(damage, effect));
        this.damageChance = 4.0F / durability;
    }

    public static void ironSpikesInteract(Entity entity) {
        if (entity instanceof LivingEntity) {
            ((LivingEntity) entity).addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 60, 0));
        }
    }

    public static void diamondSpikesInteract(Entity entity) {
        if (entity instanceof LivingEntity) {
            LivingEntity living = (LivingEntity) entity;
            living.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 60, 1));
            living.addEffect(new EffectInstance(Effects.WEAKNESS, 60, 0));
        }
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(DAMAGE_STATE);
    }

    @Override
    protected void onEntityCaught(World world, BlockPos pos, Entity victim) {
        Random random = world.getRandom();
        if (random.nextFloat() < damageChance) {
            doDamage(world, pos, world.getBlockState(pos));
        }
    }

    private void doDamage(World world, BlockPos pos, BlockState state) {
        if (shouldBreak(state)) {
            world.destroyBlock(pos, false);
        } else if (state.is(this)) {
            int damage = state.getValue(DAMAGE_STATE);
            world.setBlock(pos, state.setValue(DAMAGE_STATE, damage + 1), 3);
        }
    }

    private boolean shouldBreak(BlockState state) {
        return state.is(this) && state.getValue(DAMAGE_STATE) == 3;
    }

    private static class SpikeReaction implements ITrapReaction {

        private final float damage;
        private final Consumer<Entity> action;

        public SpikeReaction(float damage, Consumer<Entity> action) {
            this.damage = damage;
            this.action = action;
        }

        @Override
        public boolean applyTrapEffects(World level, BlockPos pos, Entity entity) {
            boolean hurt = entity.hurt(ModDamageSources.SPIKE_DAMAGE, damage);
            if (hurt) {
                action.accept(entity);
            }
            return hurt;
        }

        @Override
        public boolean requiresSpecialTool() {
            return false;
        }

        @Override
        public boolean isValidDefuseTool(World level, BlockPos pos, PlayerEntity player, Hand hand) {
            return true;
        }
    }
}
