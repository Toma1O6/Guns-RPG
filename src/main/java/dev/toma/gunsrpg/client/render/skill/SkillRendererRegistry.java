package dev.toma.gunsrpg.client.render.skill;

import dev.toma.gunsrpg.api.client.ISkillRenderer;
import dev.toma.gunsrpg.api.common.skill.ISkill;
import dev.toma.gunsrpg.common.skills.core.SkillType;

import java.util.HashMap;
import java.util.Map;

public final class SkillRendererRegistry {

    private static final Map<SkillType<?>, ISkillRenderer<?>> RENDERER_MAP = new HashMap<>();

    public static <S extends ISkill> void registerRenderer(ISkillRenderer<S> renderer, SkillType<S> type) {
        RENDERER_MAP.put(type, renderer);
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
}
