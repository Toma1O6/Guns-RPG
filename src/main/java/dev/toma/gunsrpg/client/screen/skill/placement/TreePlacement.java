package dev.toma.gunsrpg.client.screen.skill.placement;

import dev.toma.gunsrpg.common.skills.core.SkillType;

import java.util.List;

public class TreePlacement extends AbstractSkillPlacement {

    private static final TreePlacement INSTANCE = new TreePlacement();

    public static IPlacement<SkillType<?>, SkillPlacementData> tree() {
        return INSTANCE;
    }

    @Override
    public void initData(List<SkillType<?>> list) {
        map.clear();
    }
}
