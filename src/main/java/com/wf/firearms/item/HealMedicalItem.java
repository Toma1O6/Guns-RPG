package com.wf.firearms.item;

import com.wf.firearms.gameplay.MedicalEffectService;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/** 读条完成后：固定回血和/或按已损失生命百分比回血，部分附带 Buff。 */
public class HealMedicalItem extends MedicalUseItem {
    public enum Kind {
        FIELD_BANDAGE(6f, 0.05, "bandage_effect"),
        ANALGETICS(0f, 0.08, "bandage_effect"),
        PAINKILLERS(0f, 0.12, "bandage_effect"),
        MORPHINE(0f, 0.20, "morphine_effect"),
        ADRENALINE(12f, 0.0, "adrenaline_effect"),
        STEROIDS(18f, 0.0, "steroids_effect");

        private final float flatHeal;
        private final double missingHealthPercent;
        private final String effectPerk;

        Kind(float flatHeal, double missingHealthPercent, String effectPerk) {
            this.flatHeal = flatHeal;
            this.missingHealthPercent = missingHealthPercent;
            this.effectPerk = effectPerk;
        }
    }

    private final Kind kind;

    public HealMedicalItem(net.minecraft.world.item.Item.Properties props, int useTicks, Kind kind) {
        super(props, useTicks);
        this.kind = kind;
    }

    @Override
    public void appendHoverText(
            ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        String key = "item.gunsrpg." + kind.name().toLowerCase() + ".tooltip";
        tooltip.add(Component.translatable(key).withStyle(ChatFormatting.DARK_GRAY));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide && entity instanceof Player player) {
            MedicalEffectService.applyAfterMedUse(
                    player,
                    kind.flatHeal,
                    kind.missingHealthPercent,
                    kind.effectPerk,
                    () -> applyBuffs(player));
        }
        return super.finishUsingItem(stack, level, entity);
    }

    private void applyBuffs(Player player) {
        switch (kind) {
            case MORPHINE -> {
                MedicalEffectService.applyOrUpgradeEffect(player, MobEffects.REGENERATION, 15 * 20, 2);
                MedicalEffectService.applyOrUpgradeEffect(player, MobEffects.DAMAGE_BOOST, 45 * 20, 2);
                MedicalEffectService.applyOrUpgradeEffect(player, MobEffects.DAMAGE_RESISTANCE, 60 * 20, 1);
            }
            case ADRENALINE ->
                    MedicalEffectService.applyOrUpgradeEffect(player, MobEffects.DIG_SPEED, 60 * 20, 2);
            case STEROIDS -> {
                MedicalEffectService.applyOrUpgradeEffect(player, MobEffects.DAMAGE_BOOST, 60 * 20, 1);
                MedicalEffectService.applyOrUpgradeEffect(player, MobEffects.JUMP, 60 * 20, 2);
            }
            default -> {}
        }
    }
}
