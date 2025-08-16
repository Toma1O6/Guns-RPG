package dev.toma.gunsrpg.util.math;

import lib.toma.animations.AnimationUtils;

public class Vec2f {

    public float x;
    public float y;

    public Vec2f() {
        this(0, 0);
    }

    public Vec2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void add(float x, float y) {
        this.x += x;
        this.y += y;
    }

    public void scale(float scale) {
        this.x *= scale;
        this.y *= scale;
    }

    public void copyFrom(Vec2f vec) {
        this.x = vec.x;
        this.y = vec.y;
    }

    public float lerpX(float x, float delta) {
        return AnimationUtils.linearInterpolate(this.x, x, delta);
    }

    public float lerpY(float y, float delta) {
        return AnimationUtils.linearInterpolate(this.y, y, delta);
    }
}
