package dev.toma.gunsrpg.common.item.heal;

import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.client.animation.InfiniteAnimation;
import dev.toma.gunsrpg.client.animation.ModAnimations;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.S2C_AnimationPacket;
import lib.toma.animations.AnimationEngine;
import lib.toma.animations.api.IAnimation;
import lib.toma.animations.api.IAnimationPipeline;
import lib.toma.animations.api.IKeyframeProvider;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContinuousHealingItem extends AbstractHealItem<PlayerEntity> {

    private final int medPrepareDelay;

    private ContinuousHealingItem(Builder builder) {
        super(builder, new Properties().durability(builder.useCount));
        this.medPrepareDelay = builder.medPrepareDelay;
    }

    public static Builder define(String name) {
        return new Builder(name);
    }

    @Override
    public PlayerEntity getTargetObject(World world, PlayerEntity user, IPlayerData data) {
        return user;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return stack.getMaxDamage() * getUseTime() + 1;
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity entity, int timeLeft) {
        if (entity.level.isClientSide)
            return;
        int usingTime = getUseDuration(stack) - timeLeft;
        if (usingTime < medPrepareDelay)
            return;
        int ticksBeforeEffect = (usingTime - medPrepareDelay) % getUseTime();
        if (ticksBeforeEffect == 0 && entity instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) entity;
            applyAction(player);
            stack.hurtAndBreak(1, player, user -> user.broadcastBreakEvent(EquipmentSlotType.MAINHAND));
            if (!this.getUseCondition().test(player)) {
                player.stopUsingItem();
                NetworkManager.sendClientPacket(player, new S2C_AnimationPacket(S2C_AnimationPacket.Action.STOP, ModAnimations.HEAL));
            }
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity entity) {
        if (entity.level.isClientSide) {
            cancelHealAnimation();
        }
        return stack;
    }

    @Override
    protected int getAnimationLength() {
        return medPrepareDelay;
    }

    @Override
    protected IAnimation constructAnimation(IKeyframeProvider provider, int length) {
        return new InfiniteAnimation(provider, length);
    }

    private void cancelHealAnimation() {
        AnimationEngine engine = AnimationEngine.get();
        IAnimationPipeline pipeline = engine.pipeline();
        pipeline.remove(ModAnimations.HEAL);
    }

    public static class Builder extends HealBuilder<PlayerEntity, ContinuousHealingItem> {

        private int useCount;
        private int medPrepareDelay;

        private Builder(String name) {
            super(name);
        }

        public Builder uses(int useCount) {
            this.useCount = useCount;
            return this;
        }

        public Builder prepareIn(int ticks) {
            this.medPrepareDelay = ticks;
            return this;
        }

        @Override
        public ContinuousHealingItem build() {
            return new ContinuousHealingItem(this);
        }
    }
}
