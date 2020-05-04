package dev.toma.gunsrpg.config.util;

import toma.config.object.builder.ConfigBuilder;

public class Coord2Di {

    private String name;
    public int x, y;

    public Coord2Di(String name) {
        this(name, 0, 0);
    }

    public Coord2Di(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public ConfigBuilder asConfig(ConfigBuilder builder) {
        return builder
                .push().name(name).init()
                .addInt(x).name("X").add(t -> x = t.value())
                .addInt(y).name("Y").add(t -> y = t.value())
                .pop();
    }
}
