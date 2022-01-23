package dev.toma.gunsrpg.common.init;

import net.minecraft.item.Food;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class ModFoods {

    public static final Food BACON_BURGER = nutritionAndSaturation(14, 18).meat().build();
    public static final Food FISH_AND_CHIPS = nutritionAndSaturation(12, 16).meat().build();
    public static final Food GARDEN_SOUP = nutritionAndSaturation(11, 14).build();
    public static final Food CHICKED_DINNER = nutritionAndSaturation(14, 16).meat().build();
    public static final Food DELUXE_MEAL = nutritionAndSaturation(18, 20).meat().effect(() -> new EffectInstance(Effects.DAMAGE_BOOST, 400), 1.0F).build();
    public static final Food MEATY_STEW_XXL = nutritionAndSaturation(20, 20).meat().effect(() -> new EffectInstance(Effects.REGENERATION, 300), 1.0F).build();
    public static final Food RABBIT_CREAMY_SOUP = nutritionAndSaturation(16, 19).meat().effect(() -> new EffectInstance(Effects.JUMP, 500, 1), 1.0F).build();
    public static final Food SHEPHERDS_PIE = nutritionAndSaturation(17, 20).build();
    public static final Food FRUIT_SALAD = nutritionAndSaturation(10, 11).build();
    public static final Food EGG_SALAD = nutritionAndSaturation(11, 16).build();
    public static final Food CHOCOLATE_GLAZED_APPLE_PIE = nutritionAndSaturation(16, 17).effect(() -> new EffectInstance(Effects.MOVEMENT_SPEED, 400), 1.0F).build();
    public static final Food FRIED_EGG = nutritionAndSaturation(3, 2).build();
    public static final Food FRIES = nutritionAndSaturation(5, 8).effect(() -> new EffectInstance(Effects.FIRE_RESISTANCE, 200), 1.0F).build();
    public static final Food CHICKEN_NUGGETS = nutritionAndSaturation(3, 2).build();
    public static final Food SCHNITZEL = nutritionAndSaturation(9, 17).effect(() -> new EffectInstance(Effects.DAMAGE_RESISTANCE, 200), 1.0F).build();
    public static final Food RAW_DOUGHNUT = nutritionAndSaturation(4, 2).effect(() -> new EffectInstance(Effects.HUNGER, 600, 2), 1.0F).build();
    public static final Food DOUGHNUT = nutritionAndSaturation(11, 15).effect(() -> new EffectInstance(Effects.DIG_SPEED, 500), 1.0F).build();
    public static final Food SUSHI_MAKI = nutritionAndSaturation(4, 2).effect(() -> new EffectInstance(Effects.WATER_BREATHING, 400), 1.0F).build();

    private static Food.Builder nutritionAndSaturation(int nutrition, int saturation) {
        float modifier = saturation / (nutrition * 2.0F);
        return new Food.Builder().nutrition(nutrition).saturationMod(modifier);
    }
}
