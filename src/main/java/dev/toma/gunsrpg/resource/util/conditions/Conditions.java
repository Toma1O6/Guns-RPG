package dev.toma.gunsrpg.resource.util.conditions;

import dev.toma.gunsrpg.GunsRPG;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public final class Conditions {

    private static final Map<ResourceLocation, ConditionType<?>> MAP = new HashMap<>();

    public static final ConditionType<SkillCondition> SKILL_CONDITION = register(GunsRPG.makeResource("skill"), new SkillCondition.Serializer());

    public static <C extends IRecipeCondition> ConditionType<C> register(ResourceLocation location, IConditionSerializer<C> serializer) {
        ConditionType<C> type = new ConditionType<>(location, serializer);
        MAP.put(location, type);
        return type;
    }

    @SuppressWarnings("unchecked")
    public static <C extends IRecipeCondition> ConditionType<C> find(ResourceLocation location) {
        return (ConditionType<C>) MAP.get(location);
    }
}
