package dev.toma.gunsrpg.config;

import dev.toma.configuration.client.IValidationHandler;
import dev.toma.configuration.config.Configurable;
import dev.toma.configuration.config.validate.ValidationResult;
import dev.toma.gunsrpg.common.init.ModRegistries;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public final class SkillsConfig {

    @Configurable
    @Configurable.Synchronized
    @Configurable.Comment("Allows you to disable all skills for players")
    public boolean disableAllSkills = false;

    @Configurable
    @Configurable.Range(min = 1, max = 20)
    @Configurable.Comment("Specify minimum nutrition value for Well Fed skill to be triggered")
    public int wellFedTriggerValue = 14;

    @Configurable
    @Configurable.StringPattern(value = "[a-z0-9_.-]+:[a-z0-9/._-]+", defaultValue = "gunsrpg:like_a_cat_i")
    @Configurable.ValueUpdateCallback(method = "validateSkillIds")
    @Configurable.Comment("Skills which can be access via quick-activation, max 5 supported")
    public String[] boundSkills = {
            "gunsrpg:like_a_cat_i",
            "gunsrpg:iron_buddy_i",
            "gunsrpg:god_help_us"
    };

    @Configurable
    @Configurable.ValueUpdateCallback(method = "onCountTrapKillsSettingChange")
    @Configurable.Comment({"Kills by traps will be awarded to their owners", "BEWARE: This may fail some of your quests"})
    public boolean countTrapKills = true;

    private void validateSkillIds(String[] skills, IValidationHandler handler) {
        for (String s : skills) {
            ResourceLocation id = new ResourceLocation(s);
            if (!ModRegistries.SKILLS.containsKey(id)) {
                handler.setValidationResult(ValidationResult.warn(new TranslationTextComponent("text.config.validation.invalid_id.skill", s)));
                break;
            }
        }
    }

    private void onCountTrapKillsSettingChange(boolean value, IValidationHandler handler) {
        if (value) {
            handler.setValidationResult(ValidationResult.warn(new TranslationTextComponent("text.config.validation.count_trap_kills_warning")));
        }
    }
}
