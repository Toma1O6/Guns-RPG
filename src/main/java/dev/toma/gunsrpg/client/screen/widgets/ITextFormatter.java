package dev.toma.gunsrpg.client.screen.widgets;

@FunctionalInterface
public interface ITextFormatter<T> {

    String getFormatted(T t);
}
