package dev.toma.gunsrpg.common.item;

import dev.toma.gunsrpg.ModTabs;
import dev.toma.gunsrpg.client.animation.ModAnimations;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.common.AnimationPaths;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.entity.GrenadeEntity;
import dev.toma.gunsrpg.common.init.ModEntities;
import dev.toma.gunsrpg.common.init.Skills;
import lib.toma.animations.AnimationEngine;
import lib.toma.animations.api.*;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class GrenadeItem extends BaseItem implements IAnimationEntry, ICustomUseDuration {

    private final int blastSize;
    private final boolean explodeOnImpact;

    public GrenadeItem(String name, int blastRadius, boolean explodeOnImpact) {
        super(name, new Properties().tab(ModTabs.ITEM_TAB).stacksTo(10));
        this.blastSize = blastRadius;
        this.explodeOnImpact = explodeOnImpact;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 45;
    }

    @Override
    public int getUseDuration(int defaultDuration, ItemStack stack, PlayerEntity player) {
        if (PlayerData.hasActiveSkill(player, Skills.GRENADIER)) {
            return (int) (defaultDuration * 0.5);
        }
        return defaultDuration;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, World world, LivingEntity livingEntity) {
        if (!world.isClientSide) {
            world.addFreshEntity(new GrenadeEntity(ModEntities.GRENADE.get(), world, livingEntity, 80, blastSize, explodeOnImpact, itemStack.getItem()));
            if (livingEntity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) livingEntity;
                if (!player.isCreative()) {
                    itemStack.shrink(1);
                }
                player.getCooldowns().addCooldown(this, 5);
            }
        }
        return itemStack;
    }

    @Override
    public void releaseUsing(ItemStack itemStack, World world, LivingEntity entity, int timeLeft) {
        if (world.isClientSide) {
            stopThrowAnimation();
        }
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (world.isClientSide) {
            startThrowAnimation();
        }
        player.startUsingItem(hand);
        return ActionResult.pass(stack);
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.HEAL_CONFIG;
    }

    @Override
    public IRenderConfig left() {
        return RenderConfigs.HEAL_CONFIG;
    }

    @Override
    public boolean disableVanillaAnimations() {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    private void stopThrowAnimation() {
        IAnimationPipeline pipeline = AnimationEngine.get().pipeline();
        pipeline.remove(ModAnimations.GRENADE);
    }

    @OnlyIn(Dist.CLIENT)
    private void startThrowAnimation() {
        AnimationEngine engine = AnimationEngine.get();
        IAnimationPipeline pipeline = engine.pipeline();
        IAnimationLoader loader = engine.loader();
        IKeyframeProvider provider = loader.getProvider(AnimationPaths.GRENADE);
        int originalDuration = this.getUseDuration(ItemStack.EMPTY);
        PlayerEntity player = Minecraft.getInstance().player;
        pipeline.insert(ModAnimations.GRENADE, new Animation(provider, this.getUseDuration(originalDuration, ItemStack.EMPTY, player) - 2));
    }
}
