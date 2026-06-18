package com.wf.firearms.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/** 特色食物：原版饱食 + 可选即时治疗。 */
public class GunsRpgFoodItem extends Item {
    private final FoodProperties foodProps;
    private final int instantHeal;

    public GunsRpgFoodItem(FoodProperties food) {
        this(food, 0);
    }

    public GunsRpgFoodItem(FoodProperties food, int instantHeal) {
        super(new Properties().food(food));
        this.foodProps = food;
        this.instantHeal = instantHeal;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack result = super.finishUsingItem(stack, level, entity);
        if (!level.isClientSide && instantHeal > 0 && entity instanceof Player player) {
            player.heal(instantHeal);
        }
        return result;
    }

    @Override
    public void appendHoverText(
            ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        if (instantHeal > 0) {
            tooltip.add(Component.translatable("item.gunsrpg.food_buff.health", instantHeal)
                    .withStyle(net.minecraft.ChatFormatting.DARK_GRAY));
        }
        if (foodProps != null) {
            for (var pair : foodProps.getEffects()) {
                MobEffectInstance effect = pair.getFirst();
                if (effect != null) {
                    tooltip.add(Component.translatable(
                                    "item.gunsrpg.food_buff.effect",
                                    effect.getEffect().getDisplayName(),
                                    effect.getDuration() / 20)
                            .withStyle(net.minecraft.ChatFormatting.DARK_GRAY));
                }
            }
        }
    }

    public static GunsRpgFoodItem of(FoodProperties food) {
        return new GunsRpgFoodItem(food);
    }

    public static GunsRpgFoodItem ofHeal(FoodProperties food, int heal) {
        return new GunsRpgFoodItem(food, heal);
    }
}
