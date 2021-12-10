package dev.toma.gunsrpg.client.screen.skill.placement;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// TODO perk as type parameter
public class ListPlacement extends AbstractPlacement<Object, PerkPlacementData> {

    private static final ListPlacement INSTANCE = new ListPlacement();

    public static IPlacement<Object, PerkPlacementData> list() {
        return INSTANCE;
    }

    @Override
    public Map<Object, PerkPlacementData> createInternalMap() {
        return new LinkedHashMap<>();
    }

    @Override
    public void initData(List<Object> list) {

    }
}
