package com.wf.firearms.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public final class ModFoods {
    private ModFoods() {}

    public static final FoodProperties BACON_BURGER = food(14, 18).meat().build();
    public static final FoodProperties FISH_AND_CHIPS = food(12, 16).meat().build();
    public static final FoodProperties GARDEN_SOUP = food(11, 14).build();
    public static final FoodProperties CHICKEN_DINNER = food(14, 16).meat().build();
    public static final FoodProperties DELUXE_MEAL = food(18, 20)
            .meat()
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 400), 1.0f)
            .build();
    public static final FoodProperties MEATY_STEW_XXL = food(20, 20)
            .meat()
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 300), 1.0f)
            .build();
    public static final FoodProperties RABBIT_CREAMY_SOUP = food(16, 19)
            .meat()
            .effect(() -> new MobEffectInstance(MobEffects.JUMP, 500, 1), 1.0f)
            .build();
    public static final FoodProperties SHEPHERDS_PIE = food(17, 20).build();
    public static final FoodProperties FRUIT_SALAD = food(10, 11).build();
    public static final FoodProperties EGG_SALAD = food(11, 16).build();
    public static final FoodProperties CHOCOLATE_GLAZED_APPLE_PIE = food(16, 17)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 400), 1.0f)
            .build();
    public static final FoodProperties FRIED_EGG = food(3, 2).build();
    public static final FoodProperties FRIES = food(5, 8)
            .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200), 1.0f)
            .build();
    public static final FoodProperties CHICKEN_NUGGETS = food(3, 2).build();
    public static final FoodProperties SCHNITZEL = food(9, 17)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200), 1.0f)
            .build();
    public static final FoodProperties RAW_DOUGHNUT = food(4, 2)
            .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 600), 1.0f)
            .build();
    public static final FoodProperties DOUGHNUT = food(11, 15)
            .effect(() -> new MobEffectInstance(MobEffects.DIG_SPEED, 500), 1.0f)
            .build();
    public static final FoodProperties SUSHI_MAKI = food(4, 2)
            .effect(() -> new MobEffectInstance(MobEffects.WATER_BREATHING, 400), 1.0f)
            .build();

    private static FoodProperties.Builder food(int nutrition, int saturation) {
        float modifier = saturation / (nutrition * 2.0f);
        return new FoodProperties.Builder().nutrition(nutrition).saturationMod(modifier);
    }
}
