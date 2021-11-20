package dev.toma.gunsrpg.client.screen.skill;

import dev.toma.gunsrpg.util.math.IDimensions;

public interface IViewFactory<V extends View, T> {

    V create(T data, IDimensions dimensions);
}
