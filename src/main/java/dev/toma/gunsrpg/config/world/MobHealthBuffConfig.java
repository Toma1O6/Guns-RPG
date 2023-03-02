package dev.toma.gunsrpg.config.world;

import dev.toma.configuration.client.IValidationHandler;
import dev.toma.configuration.config.Configurable;
import dev.toma.configuration.config.validate.ValidationResult;
import dev.toma.gunsrpg.GunsRPG;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class MobHealthBuffConfig {

    @Configurable
    @Configurable.DecimalRange(min = 0.0F, max = 1.0F)
    @Configurable.Comment("Chance that mobs will spawn with 2x more HP")
    @Configurable.Gui.NumberFormat("#.###")
    public float health2xChance = 0.50F;

    @Configurable
    @Configurable.DecimalRange(min = 0.0F, max = 1.0F)
    @Configurable.Comment("Chance that mobs will spawn with 3x more HP")
    @Configurable.Gui.NumberFormat("#.###")
    public float health3xChance = 0.20F;

    @Configurable
    @Configurable.DecimalRange(min = 0.0F, max = 1.0F)
    @Configurable.Comment("Chance that mobs will spawn with 4x more HP")
    @Configurable.Gui.NumberFormat("#.###")
    public float health4xChance = 0.04F;

    @Configurable
    @Configurable.Comment("List of entities which won't be affected by health buffs")
    @Configurable.ValueUpdateCallback(method = "onHealthBuffValidate")
    @Configurable.Gui.CharacterLimit(64)
    public String[] healthBuffBlacklist = {
            "minecraft:ender_dragon",
            "minecraft:wither",
            "minecraft:iron_golem",
            "gunsrpg:rocket_angel",
            "gunsrpg:bloodmoon_golem",
            "gunsrpg:gold_dragon",
            "gunsrpg:zombie_nightmare"
    };

    private final Lazy<Set<EntityType<?>>> healthBuffBlacklistedEntities = Lazy.of(() -> {
        Set<EntityType<?>> set = new HashSet<>();
        loadHealthBuffBlacklist(set, healthBuffBlacklist);
        return set;
    });

    public void onHealthBuffValidate(String[] values, IValidationHandler handler) {
        Set<EntityType<?>> blacklist = healthBuffBlacklistedEntities.get();
        blacklist.clear();
        String invalidId = loadHealthBuffBlacklist(blacklist, values);
        if (invalidId != null) {
            handler.setValidationResult(ValidationResult.warn(new TranslationTextComponent("text.config.validation.invalid_id.entity", invalidId)));
        }
    }

    public boolean isHealthBuffDisabled(EntityType<?> type) {
        return healthBuffBlacklistedEntities.get().contains(type);
    }

    private String loadHealthBuffBlacklist(Collection<EntityType<?>> output, String[] entityIds) {
        String lastInvalidId = null;
        for (String entityId : entityIds) {
            ResourceLocation location = new ResourceLocation(entityId);
            if (ForgeRegistries.ENTITIES.containsKey(location)) {
                output.add(ForgeRegistries.ENTITIES.getValue(location));
            } else {
                lastInvalidId = entityId;
                GunsRPG.log.warn("Found unknown entity ID '{}' in config under 'mobHealthBuffs' field", entityId);
            }
        }
        return lastInvalidId;
    }
}
