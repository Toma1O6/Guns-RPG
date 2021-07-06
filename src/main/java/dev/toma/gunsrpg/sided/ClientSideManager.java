package dev.toma.gunsrpg.sided;

import dev.toma.gunsrpg.client.animation.AnimationProcessor;
import dev.toma.gunsrpg.client.animation.ScriptLoader;

public class ClientSideManager {
    private static final ScriptLoader SCRIPT_LOADER = new ScriptLoader();
    private static final AnimationProcessor ANIMATION_PROCESSOR = new AnimationProcessor();

    public static ScriptLoader scripts() {
        return SCRIPT_LOADER;
    }

    public static AnimationProcessor processor() {
        return ANIMATION_PROCESSOR;
    }
}
