package dev.toma.gunsrpg.client.screen.skill;

import dev.toma.gunsrpg.util.math.IDimensions;
import net.minecraft.client.Minecraft;

import java.util.function.Supplier;

public interface IViewFactory<V extends View, T> {

    Supplier<View> LOADING_VIEW_SUPPLIER = () -> new LoadingView(Minecraft.getInstance().getWindow());

    V create(T data, IDimensions dimensions);
}
