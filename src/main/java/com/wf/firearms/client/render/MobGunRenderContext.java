package com.wf.firearms.client.render;

/** 标记当前 BEWLR 来自持枪怪物手臂。 */
public final class MobGunRenderContext {
    private static final ThreadLocal<Boolean> ACTIVE = ThreadLocal.withInitial(() -> false);

    private MobGunRenderContext() {}

    public static void begin() {
        ACTIVE.set(true);
    }

    public static void end() {
        ACTIVE.remove();
    }

    public static boolean isActive() {
        return ACTIVE.get();
    }
}
