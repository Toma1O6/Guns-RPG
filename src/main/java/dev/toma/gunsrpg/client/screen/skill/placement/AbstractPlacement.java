package dev.toma.gunsrpg.client.screen.skill.placement;

import java.util.Map;

public abstract class AbstractPlacement<T, D extends IPlacementData> implements IPlacement<T, D> {

    protected final Map<T, D> map;

    public AbstractPlacement() {
        this.map = this.createInternalMap();
    }

    public abstract Map<T, D> createInternalMap();

    @Override
    public D getPlacementData(T t) {
        return map.get(t);
    }
}
