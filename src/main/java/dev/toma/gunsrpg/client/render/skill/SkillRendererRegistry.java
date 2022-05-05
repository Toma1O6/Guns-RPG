package dev.toma.gunsrpg.client.render.skill;

import dev.toma.gunsrpg.api.client.IHudSkillRenderer;
import dev.toma.gunsrpg.api.client.ISkillRenderer;
import dev.toma.gunsrpg.api.common.skill.ICooldown;
import dev.toma.gunsrpg.api.common.skill.ISkill;
import dev.toma.gunsrpg.common.skills.core.SkillType;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public final class SkillRendererRegistry {

    private static final Set<SkillType<?>> DISPLAYABLE = new LinkedHashSet<>();
    private static final Map<SkillType<?>, ISkillRenderer<?>> RENDERER_MAP = new HashMap<>();

    public static void registerHudDisplay(SkillType<?> type) {
        DISPLAYABLE.add(type);
    }

    public static <S extends ISkill> void registerRenderer(ISkillRenderer<S> renderer, SkillType<S> type) {
        RENDERER_MAP.put(type, renderer);
        if (renderer instanceof IHudSkillRenderer) {
            registerHudDisplay(type);
        }
    }

    @SafeVarargs
    public static <S extends ISkill> void registerRenderers(ISkillRenderer<S> renderer, SkillType<S>... types) {
        for (SkillType<S> type : types) {
            registerRenderer(renderer, type);
        }
    }

    @SuppressWarnings("unchecked")
    public static <S extends ISkill> ISkillRenderer<S> getRendererFor(SkillType<S> type) {
        return (ISkillRenderer<S>) RENDERER_MAP.get(type);
    }

    public static Set<SkillType<?>> getDisplayableSkills() {
        return DISPLAYABLE;
    }

    public static <S extends ISkill & ICooldown> IHudSkillRenderer<S> getHudRenderer(SkillType<S> type) {
        return (IHudSkillRenderer<S>) getRendererFor(type);
    }
}
