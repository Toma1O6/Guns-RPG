package dev.toma.gunsrpg.common.item;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.ModTabs;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.ISkillProvider;
import dev.toma.gunsrpg.client.animation.ModAnimations;
import dev.toma.gunsrpg.common.LootStashDetectorHandler;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.ModItems;
import dev.toma.gunsrpg.common.init.Skills;
import lib.toma.animations.AnimationEngine;
import lib.toma.animations.AnimationUtils;
import lib.toma.animations.api.Animation;
import lib.toma.animations.api.IAnimationPipeline;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.UUID;

public class StashDetectorItem extends BaseItem {

    public static final ResourceLocation CHARGE_BATTERY_ANIMATION = GunsRPG.makeResource("stash_detector/change_batteries");
    public static final ResourceLocation USE_ANIMATION = GunsRPG.makeResource("stash_detector/use");

    public StashDetectorItem(String name) {
        super(name, new Properties().tab(ModTabs.ITEM_TAB).durability(200));
    }

    public static boolean isValidBatterySource(ItemStack stack) {
        return stack.getItem() == ModItems.BATTERY;
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        IPlayerData data = PlayerData.getUnsafe(player);
        ISkillProvider provider = data.getSkillProvider();
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getDamageValue() >= stack.getMaxDamage()) {
            return ActionResult.pass(stack);
        }
        if (!provider.hasSkill(Skills.TREASURE_HUNTER_I)) {
            if (!level.isClientSide) {
                ((ServerPlayerEntity) player).sendMessage(new StringTextComponent("You need Treasure Hunter skill in order to use this item"), ChatType.GAME_INFO, Util.NIL_UUID);
            }
            return ActionResult.fail(stack);
        }
        UUID uuid = player.getUUID();
        boolean using = LootStashDetectorHandler.isUsing(uuid);
        if (using) {
            LootStashDetectorHandler.stopUsing(uuid);
        } else {
            LootStashDetectorHandler.initiateUsing(player);
        }
        if (level.isClientSide) {
            playUseAnimation();
        }
        return ActionResult.pass(stack);
    }

    @OnlyIn(Dist.CLIENT)
    private void playUseAnimation() {
        IAnimationPipeline pipeline = AnimationEngine.get().pipeline();
        pipeline.insert(ModAnimations.STASH_DETECTOR, AnimationUtils.createAnimation(USE_ANIMATION, provider -> new Animation(provider, 30)));
    }
}
