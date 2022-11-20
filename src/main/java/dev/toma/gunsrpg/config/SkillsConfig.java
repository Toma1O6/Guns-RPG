package dev.toma.gunsrpg.config;

import dev.toma.configuration.client.IValidationHandler;
import dev.toma.configuration.config.Configurable;
import dev.toma.configuration.config.validate.ValidationResult;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.init.ModRegistries;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.LinkedHashSet;
import java.util.Set;

public final class SkillsConfig {

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
    @Configurable.StringPattern(value = "[a-z0-9_.-]+:[a-z0-9/._-]+", defaultValue = "minecraft:pig")
    @Configurable.Comment("Mobs listed here cannot be insta-killed")
    @Configurable.ValueUpdateCallback(method = "onSkullCrusherBlacklistUpdate")
    @Configurable.Gui.CharacterLimit(64)
    public String[] skullCrusherIgnoredMobs = { "gunsrpg:bloodmoon_golem" };

    public boolean isInstantKillAllowed(EntityType<?> type) {
        return !instantKillBlackList.contains(type);
    }

    private final Set<EntityType<?>> instantKillBlackList = new LinkedHashSet<>();

    private void onSkullCrusherBlacklistUpdate(String[] inputs, IValidationHandler handler) {
        instantKillBlackList.clear();
        String lastInvalidId = null;
        for (String string : inputs) {
            ResourceLocation id = new ResourceLocation(string);
            if (ForgeRegistries.ENTITIES.containsKey(id)) {
                instantKillBlackList.add(ForgeRegistries.ENTITIES.getValue(id));
            } else {
                lastInvalidId = string;
                GunsRPG.log.warn("Found unknown entity ID '{}' in config under 'skullCrusherIgnoredMobs' field", string);
            }
        }
        if (lastInvalidId != null) {
            handler.setValidationResult(ValidationResult.warn(new TranslationTextComponent("text.config.validation.invalid_id.entity", lastInvalidId)));
        }
    }

    private void validateSkillIds(String[] skills, IValidationHandler handler) {
        for (String s : skills) {
            ResourceLocation id = new ResourceLocation(s);
            if (!ModRegistries.SKILLS.containsKey(id)) {
                handler.setValidationResult(ValidationResult.warn(new TranslationTextComponent("text.config.validation.invalid_id.skill", s)));
                break;
            }
        }
    }
}
