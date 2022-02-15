package dev.toma.gunsrpg.config.gun;

import dev.toma.configuration.api.IConfigWriter;
import dev.toma.configuration.api.IObjectSpec;
import dev.toma.configuration.api.type.DoubleType;
import dev.toma.configuration.api.type.EnumType;
import dev.toma.configuration.api.type.ObjectType;
import dev.toma.gunsrpg.api.common.IJamConfig;
import lib.toma.animations.Easings;
import net.minecraft.item.ItemStack;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public final class JamConfig extends ObjectType implements IJamConfig {

    private final DoubleType minChance;
    private final DoubleType maxChance;
    private final EnumType<Easings> easing;

    public JamConfig(IObjectSpec spec, float minChance, float maxChance, Easings easing) {
        super(spec);
        IConfigWriter writer = spec.getWriter();
        DecimalFormat format = new DecimalFormat("#.###");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        format.setDecimalFormatSymbols(symbols);
        this.minChance = writer.writeBoundedDouble("Base chance", minChance, 0.0, 1.0).setFormatting(format);
        this.maxChance = writer.writeBoundedDouble("Max chance", maxChance, 0.0, 1.0).setFormatting(format);
        this.easing = writer.writeEnum("Easing type", easing);
    }

    @Override
    public float getJamChance(ItemStack stack) {
        float breakProgress = stack.getDamageValue() / (float) stack.getMaxDamage();
        return getJamChance(breakProgress);
    }

    @Override
    public float getJamChance(float breakProgress) {
        float progress = easing.get().ease(breakProgress);
        float diff = maxChance.floatValue() - minChance.floatValue();
        return minChance.floatValue() + progress * diff;
    }
}
