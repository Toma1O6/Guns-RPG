package dev.toma.gunsrpg.client.screen.widgets;

@FunctionalInterface
public interface IClickResponder<T> {

    void onElementClicked(T element);
}
