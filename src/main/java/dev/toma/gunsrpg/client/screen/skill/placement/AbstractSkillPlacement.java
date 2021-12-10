package dev.toma.gunsrpg.client.screen.skill.placement;

import dev.toma.gunsrpg.common.skills.core.SkillType;

import java.util.IdentityHashMap;
import java.util.Map;

public abstract class AbstractSkillPlacement extends AbstractPlacement<SkillType<?>, SkillPlacementData> {

    @Override
    public Map<SkillType<?>, SkillPlacementData> createInternalMap() {
        return new IdentityHashMap<>();
    }
}
