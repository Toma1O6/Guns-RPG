package dev.toma.gunsrpg.config.gun;

import dev.toma.configuration.config.Configurable;
import dev.toma.gunsrpg.api.common.IJamConfig;
import lib.toma.animations.Easings;
import net.minecraft.item.ItemStack;

public final class JamConfig implements IJamConfig {

    @Configurable
    @Configurable.DecimalRange(min = 0.0, max = 1.0)
    @Configurable.Comment("Smallest possible weapon jam chance")
    @Configurable.Gui.NumberFormat("#.###")
    public float minChance;

    @Configurable
    @Configurable.DecimalRange(min = 0.0, max = 1.0)
    @Configurable.Comment("Largest possible weapon jam chance")
    @Configurable.Gui.NumberFormat("#.###")
    public float maxChance;

    @Configurable
    @Configurable.Comment({"Transformer function for actual jam chance calculations", "See https://easings.net for examples"})
    public Easings jamChanceTransformerFunction;

    public JamConfig(float minChance, float maxChance, Easings easing) {
        this.minChance = minChance;
        this.maxChance = maxChance;
        this.jamChanceTransformerFunction = easing;
    }

    @Override
    public float getJamChance(ItemStack stack) {
        float breakProgress = stack.getDamageValue() / (float) stack.getMaxDamage();
        return getJamChance(breakProgress);
    }

    @Override
    public float getJamChance(float breakProgress) {
        float progress = jamChanceTransformerFunction.ease(breakProgress);
        float diff = maxChance - minChance;
        return minChance + progress * diff;
    }
}
