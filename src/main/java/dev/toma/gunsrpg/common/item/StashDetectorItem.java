package dev.toma.gunsrpg.common.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.ModTabs;
import dev.toma.gunsrpg.api.client.IProgressionDetailProvider;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.ISkillProvider;
import dev.toma.gunsrpg.client.animation.ModAnimations;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.StashDetectorRenderer;
import dev.toma.gunsrpg.common.LootStashDetectorHandler;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.ModItems;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.util.SkillUtil;
import dev.toma.gunsrpg.util.locate.ammo.ItemLocator;
import dev.toma.gunsrpg.util.math.IVec2i;
import lib.toma.animations.AnimationEngine;
import lib.toma.animations.AnimationUtils;
import lib.toma.animations.api.Animation;
import lib.toma.animations.api.IAnimationEntry;
import lib.toma.animations.api.IAnimationPipeline;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ChatType;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.UUID;

@OnlyIn(value = Dist.CLIENT, _interface = IProgressionDetailProvider.class)
public class StashDetectorItem extends BaseItem implements IAnimationEntry, IProgressionDetailProvider {

    public static final ResourceLocation CHARGE_BATTERY_ANIMATION = GunsRPG.makeResource("stash_detector/change_batteries");
    public static final ResourceLocation TURN_ON_ANIMATION = GunsRPG.makeResource("stash_detector/turn_on");
    public static final ResourceLocation TURN_OFF_ANIMATION = GunsRPG.makeResource("stash_detector/turn_off");

    public StashDetectorItem(String name) {
        super(name, new Properties().tab(ModTabs.ITEM_TAB).durability(210).setISTER(() -> StashDetectorRenderer::new));
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
                ((ServerPlayerEntity) player).sendMessage(SkillUtil.getMissingSkillText(Skills.TREASURE_HUNTER_I), ChatType.GAME_INFO, Util.NIL_UUID);
            }
            return ActionResult.fail(stack);
        }
        if (level.isClientSide) {
            playUseAnimation(player.getUUID());
        }
        return ActionResult.pass(stack);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public boolean disableVanillaAnimations() {
        return false;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.HEAL_CONFIG;
    }

    @Override
    public IRenderConfig left() {
        return RenderConfigs.HEAL_CONFIG;
    }

    @OnlyIn(Dist.CLIENT)
    private void playUseAnimation(UUID uuid) {
        IAnimationPipeline pipeline = AnimationEngine.get().pipeline();
        ResourceLocation location = LootStashDetectorHandler.isUsing(uuid) ? TURN_OFF_ANIMATION : TURN_ON_ANIMATION;
        pipeline.insert(ModAnimations.STASH_DETECTOR, AnimationUtils.createAnimation(location, provider -> new Animation(provider, 35)));
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return 0xffff00;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void draw(MatrixStack matrix, FontRenderer font, IVec2i position, int xOffset, int yOffset, int width, int height, PlayerEntity player, IPlayerData data) {
        String label = player.isCreative() ? GunItem.LABEL_INF.getString() : String.valueOf(ItemLocator.sum(player.inventory, StashDetectorItem::isValidBatterySource));
        Minecraft client = Minecraft.getInstance();
        int labelLeft = position.x() + xOffset + width - font.width(label);
        font.draw(matrix, label, labelLeft, position.y() + yOffset + 6, 0xFFFFFF);
        client.getItemRenderer().renderGuiItem(ModItems.BATTERY.getDefaultInstance(), labelLeft - 18, position.y() + yOffset);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public int getWidth(FontRenderer font, PlayerEntity player, IPlayerData data) {
        String label = player.isCreative() ? GunItem.LABEL_INF.getString() : String.valueOf(ItemLocator.sum(player.inventory, StashDetectorItem::isValidBatterySource));
        return 18 + font.width(label);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public int getHeight(FontRenderer font, PlayerEntity player, IPlayerData data) {
        return 16;
    }

    public enum StatusEvent {
        ON,
        OFF,
        TOGGLE
    }
}
