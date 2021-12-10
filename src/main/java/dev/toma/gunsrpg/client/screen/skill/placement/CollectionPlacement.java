package dev.toma.gunsrpg.client.screen.skill.placement;

import dev.toma.gunsrpg.common.skills.core.SkillType;

import java.util.List;

public class CollectionPlacement extends AbstractSkillPlacement {

    private static final CollectionPlacement INSTANCE = new CollectionPlacement();

    public static IPlacement<SkillType<?>, SkillPlacementData> collection() {
        return INSTANCE;
    }

    @Override
    public void initData(List<SkillType<?>> list) {
        // reset
        map.clear();

        // grid calculation
        int total = list.size();
        int gridLength = 2;
        while (gridLength * gridLength < total) {
            ++gridLength;
        }

        // placements
        for (int i = 0; i < total; i++) {
            int x = i % gridLength;
            int y = i / gridLength;
            UiPosition position = UiPosition.offsetSquares(3, x, y);
            SkillType<?> context = list.get(i);
            map.put(context, new SkillPlacementData(context, position, SkillPlacementData.DisplayStyle.DEFAULT));
        }
    }
}
