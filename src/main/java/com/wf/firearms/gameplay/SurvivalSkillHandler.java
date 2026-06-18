package com.wf.firearms.gameplay;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.data.PlayerFirearmsData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = GunsRpg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class SurvivalSkillHandler {
    private static final UUID AGILITY_SPEED =
            UUID.fromString("A1B2C3D4-E5F6-4789-A012-3456789ABCDE");
    private static final UUID ADRENALINE_ATTACK =
            UUID.fromString("B2C3D4E5-F6A7-4890-B123-456789ABCDEF");
    private static final UUID ADRENALINE_SPEED =
            UUID.fromString("C3D4E5F6-A7B8-4901-C234-56789ABCDEF0");

    private SurvivalSkillHandler() {}

    @SubscribeEvent(priority = net.minecraftforge.eventbus.api.EventPriority.LOW)
    public static void onMeleeHurt(LivingHurtEvent event) {
        if (event.getEntity().level().isClientSide) {
            return;
        }
        DamageSource src = event.getSource();
        if (!(src.getEntity() instanceof Player player)) {
            return;
        }
        if (GunKillHandler.isFirearmBulletDamage(src)) {
            return;
        }
        if (src.getDirectEntity() != player) {
            return;
        }
        if (!(player.getMainHandItem().getItem() instanceof TieredItem)) {
            return;
        }

        float amount = event.getAmount();
        float muscles = SurvivalSkillService.strongMusclesBonus(player);
        if (muscles > 0.0F) {
            amount += muscles;
        }
        if (SurvivalSkillService.rollSkullCrusher(player)) {
            amount *= SurvivalSkillService.skullCrusherCritMultiplier(player);
        }
        if (amount != event.getAmount()) {
            event.setAmount(amount);
        }
    }

    @SubscribeEvent
    public static void onEat(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity().level().isClientSide) {
            return;
        }
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        ItemStack stack = event.getItem();
        FoodProperties food = stack.getItem().getFoodProperties(stack, player);
        if (food == null) {
            return;
        }
        if (food.getNutrition() < SurvivalSkillService.wellFedNutritionThreshold()) {
            return;
        }
        float chance = SurvivalSkillService.wellFedChance(player);
        if (chance <= 0.0F || player.getRandom().nextFloat() >= chance) {
            return;
        }
        int duration = SurvivalSkillService.wellFedAbsorptionDurationTicks(player);
        int amplifier = SurvivalSkillService.wellFedAbsorptionAmplifier(player);
        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, duration, amplifier, false, true, true));
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onDeath(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide) {
            return;
        }
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        if (SurvivalSkillService.secondChanceTier(player) <= 0) {
            return;
        }
        if (SurvivalSkillService.getSecondChanceCooldown(player) > 0) {
            return;
        }
        if (event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return;
        }
        event.setCanceled(true);
        player.setHealth(SurvivalSkillService.secondChanceHeal(player));
        player.addEffect(
                new MobEffectInstance(MobEffects.REGENERATION, 200, Math.max(0, SurvivalSkillService.secondChanceTier(player) - 1)));
        SurvivalSkillService.setSecondChanceCooldown(player, SurvivalSkillService.secondChanceCooldownTicks(player));
        player.sendSystemMessage(net.minecraft.network.chat.Component.literal("§e[生存]§r 第二次机会已触发"));
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide) {
            return;
        }
        Player player = event.player;
        SurvivalSkillService.tickSecondChanceCooldown(player);
        syncAgilitySpeed(player);
        syncAdrenaline(player);
    }

    private static void syncAgilitySpeed(Player player) {
        float bonus = SurvivalSkillService.agilityMoveSpeedBonus(player);
        AttributeInstance attr = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attr == null) {
            return;
        }
        attr.removeModifier(AGILITY_SPEED);
        if (bonus > 0.0F) {
            attr.addTransientModifier(
                    new AttributeModifier(AGILITY_SPEED, "gunsrpg_agility", bonus, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
    }

    private static void syncAdrenaline(Player player) {
        AttributeInstance attack = player.getAttribute(Attributes.ATTACK_SPEED);
        AttributeInstance speed = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attack != null) {
            attack.removeModifier(ADRENALINE_ATTACK);
        }
        if (speed != null) {
            speed.removeModifier(ADRENALINE_SPEED);
        }
        if (!SurvivalSkillService.adrenalineActive(player)) {
            return;
        }
        float atkBonus = SurvivalSkillService.adrenalineAttackSpeedBonus(player);
        if (attack != null && atkBonus > 0.0F) {
            attack.addTransientModifier(
                    new AttributeModifier(ADRENALINE_ATTACK, "gunsrpg_adrenaline_atk", atkBonus, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
        if (speed != null) {
            speed.addTransientModifier(
                    new AttributeModifier(ADRENALINE_SPEED, "gunsrpg_adrenaline_move", atkBonus * 0.5F, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
    }
}
