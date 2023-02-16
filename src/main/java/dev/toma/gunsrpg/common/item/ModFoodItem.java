package dev.toma.gunsrpg.common.item;

import com.mojang.datafixers.util.Pair;
import dev.toma.gunsrpg.ModTabs;
import dev.toma.gunsrpg.util.SkillUtil;
import dev.toma.gunsrpg.util.helper.NumberHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class ModFoodItem extends BaseItem {

    private final List<IFoodBuff> foodBuffs = new ArrayList<>();

    public ModFoodItem(String name, Food food) {
        super(name, new Properties().tab(ModTabs.ITEM_TAB).food(Objects.requireNonNull(food)));
        for (Pair<EffectInstance, Float> pair : food.getEffects()) {
            this.addCustomBuff(new PotionBuff(pair.getFirst()));
        }
    }

    public ModFoodItem heal(int amount, boolean withAttributes) {
        return this.addCustomBuff(new HealthBuff(amount, withAttributes));
    }

    public ModFoodItem heal(int amount) {
        return this.heal(amount, false);
    }

    public ModFoodItem addCustomBuff(IFoodBuff foodBuff) {
        this.foodBuffs.add(foodBuff);
        this.foodBuffs.sort(Comparator.comparingInt(IFoodBuff::orderIndex));
        return this;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity entity) {
        if (isEdible()) {
            if (entity instanceof PlayerEntity && !world.isClientSide) {
                PlayerEntity player = (PlayerEntity) entity;
                for (IFoodBuff foodBuff : foodBuffs) {
                    foodBuff.accept(player);
                }
            }
            return entity.eat(world, stack);
        }
        return stack;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> components, ITooltipFlag flag) {
        for (IFoodBuff foodBuff : foodBuffs) {
            components.add(foodBuff.getTooltip());
        }
    }

    public interface IFoodBuff extends Consumer<PlayerEntity> {

        ITextComponent getTooltip();

        default int orderIndex() {
            return 0;
        }
    }

    private static class HealthBuff implements IFoodBuff {

        private final int amount;
        private final boolean applyAttributes;
        private final ITextComponent tooltip;

        private HealthBuff(int amount, boolean applyAttributes) {
            this.amount = amount;
            this.applyAttributes = applyAttributes;
            ITextComponent hpComponent = new StringTextComponent(String.valueOf(amount)).withStyle(TextFormatting.GREEN);
            this.tooltip = new TranslationTextComponent("item.gunsrpg.food_buff.health", hpComponent).withStyle(TextFormatting.GRAY);
        }

        @Override
        public void accept(PlayerEntity player) {
            if (applyAttributes) {
                SkillUtil.heal(player, amount);
            } else {
                player.heal(amount);
            }
        }

        @Override
        public ITextComponent getTooltip() {
            return tooltip;
        }
    }

    private static class PotionBuff implements IFoodBuff {

        private final ITextComponent tooltip;

        private PotionBuff(EffectInstance instance) {
            Effect effect = instance.getEffect();
            int amplifierVal = instance.getAmplifier() + 1;
            ITextComponent effectComponent = new StringTextComponent(effect.getDisplayName().getString() + " " + NumberHelper.toRomanLiteral(amplifierVal)).withStyle(TextFormatting.YELLOW);
            ITextComponent timeComponent = new StringTextComponent(String.valueOf(instance.getDuration() / 20)).withStyle(TextFormatting.GREEN);
            this.tooltip = new TranslationTextComponent("item.gunsrpg.food_buff.effect", effectComponent, timeComponent).withStyle(TextFormatting.GRAY);
        }

        @Override
        public void accept(PlayerEntity player) {
        }

        @Override
        public ITextComponent getTooltip() {
            return tooltip;
        }

        @Override
        public int orderIndex() {
            return Integer.MAX_VALUE;
        }
    }
}
