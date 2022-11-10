package dev.toma.gunsrpg.config.world;

import dev.toma.configuration.client.IValidationHandler;
import dev.toma.configuration.config.Configurable;
import dev.toma.configuration.config.validate.ValidationResult;
import dev.toma.gunsrpg.api.common.IGeneratorConfig;
import net.minecraft.util.text.TranslationTextComponent;

public class SimpleOreGenConfig implements IGeneratorConfig {

    @Configurable
    @Configurable.Range(min = 0, max = 128)
    @Configurable.Comment("Generation attempts per chunk")
    public int spawns;

    @Configurable
    @Configurable.Range(min = 1, max = 255)
    @Configurable.ChangeCallback(method = "onMinHeightValidate")
    @Configurable.Comment("Minimum generation height")
    public int minHeight;

    @Configurable
    @Configurable.Range(min = 1, max = 255)
    @Configurable.ChangeCallback(method = "onMaxHeightValidate")
    @Configurable.Comment("Maximum generation height")
    public int maxHeight;

    public SimpleOreGenConfig(int spawnAttempts, int minGenHeight, int maxGenHeight) {
        spawns = spawnAttempts;
        minHeight = minGenHeight;
        maxHeight = maxGenHeight;
    }

    public int getSpawnAttempts() {
        return spawns;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void onMinHeightValidate(int value, IValidationHandler handler) {
        if (value >= this.maxHeight) {
            handler.setValidationResult(ValidationResult.warn(new TranslationTextComponent("text.config.validation.ore_gen.min_height", value, maxHeight)));
        }
    }

    public void onMaxHeightValidate(int value, IValidationHandler handler) {
        if (value <= this.minHeight) {
            handler.setValidationResult(ValidationResult.warn(new TranslationTextComponent("text.config.validation.ore_gen.max_height", value, minHeight)));
        }
    }
}
