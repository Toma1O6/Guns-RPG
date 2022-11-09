package dev.toma.gunsrpg.config;

import dev.toma.configuration.config.Configurable;
import dev.toma.gunsrpg.GunsRPG;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public final class SkillsConfig {

    @Configurable
    @Configurable.Range(min = 1, max = 20)
    @Configurable.Comment("Specify minimum nutrition value for Well Fed skill to be triggered")
    public final int wellFedTriggerValue = 14;

    @Configurable
    @Configurable.StringPattern(value = "[a-z0-9_.-]+:[a-z0-9/._-]+", defaultValue = "gunsrpg:like_a_cat_i")
    @Configurable.Comment("Skills which can be access via quick-activation, max 5 supported")
    public final String[] boundSkills = {
            "gunsrpg:like_a_cat_i",
            "gunsrpg:iron_buddy_i",
            "gunsrpg:god_help_us"
    };

    @Configurable
    @Configurable.StringPattern(value = "[a-z0-9_.-]+:[a-z0-9/._-]+", defaultValue = "minecraft:pig")
    @Configurable.Comment("Mobs listed here cannot be insta-killed")
    @Configurable.ChangeCallback(method = "onSkullCrusherBlacklistUpdate")
    public final String[] skullCrusherIgnoredMobs = { "gunsrpg:bloodmoon_golem" };

    public boolean isInstantKillAllowed(EntityType<?> type) {
        return !instantKillBlackList.contains(type);
    }

    private final Set<EntityType<?>> instantKillBlackList = new LinkedHashSet<>();
    private String[] onSkullCrusherBlacklistUpdate(String[] inputs) {
        instantKillBlackList.clear();
        for (String string : inputs) {
            ResourceLocation id = new ResourceLocation(string);
            if (ForgeRegistries.ENTITIES.containsKey(id)) {
                instantKillBlackList.add(ForgeRegistries.ENTITIES.getValue(id));
            } else {
                GunsRPG.log.warn("Found unknown entity ID in config under 'skullCrusherIgnoredMobs' field: {}", string);
            }
        }
        return this.instantKillBlackList.stream().map(e -> e.getRegistryName().toString()).toArray(String[]::new);
    }
}
