package dev.toma.gunsrpg.client.screen.skill.placement;

import java.util.Collection;
import java.util.List;

public interface IPlacement<T, D extends IPlacementData> {

    void initData(List<T> list);

    D getPlacementData(T t);
}
