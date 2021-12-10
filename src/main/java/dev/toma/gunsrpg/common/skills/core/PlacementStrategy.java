package dev.toma.gunsrpg.common.skills.core;

import dev.toma.gunsrpg.client.screen.skill.placement.CollectionPlacement;
import dev.toma.gunsrpg.client.screen.skill.placement.IPlacement;
import dev.toma.gunsrpg.client.screen.skill.placement.ListPlacement;
import dev.toma.gunsrpg.client.screen.skill.placement.TreePlacement;

import java.util.function.Supplier;

public enum PlacementStrategy {

    TREE(TreePlacement::tree),
    COLLECTION(CollectionPlacement::collection),
    LIST(ListPlacement::list);

    final Supplier<IPlacement<?, ?>> placementSupplier;

    PlacementStrategy(Supplier<IPlacement<?, ?>> placementSupplier) {
        this.placementSupplier = placementSupplier;
    }
}
