package lib.toma.animations;

import lib.toma.animations.pipeline.IAnimationPipeline;
import lib.toma.animations.screen.HandRenderScreen;
import lib.toma.animations.screen.animator.Animator;
import lib.toma.animations.screen.animator.AnimatorScreen;
import lib.toma.animations.serialization.AnimationLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

public final class AnimationCompatLayer {

    public int handConfigKey = GLFW.GLFW_KEY_KP_9;
    public int animatorKey = GLFW.GLFW_KEY_KP_8;
    final HandRenderAPI handRenderAPI;
    final UtilScreenFactory screenFactory;
    final AnimationPipeline pipeline;
    final AnimationLoader loader;
    public static final Logger logger = LogManager.getLogger("AnimationLib");

    private KeyBinding handConfigs;
    private KeyBinding animator;

    private AnimationCompatLayer() {
        handRenderAPI = new HandRenderAPI();
        screenFactory = new UtilScreenFactory();
        pipeline = new AnimationPipeline();
        loader = new AnimationLoader();
    }

    public void setup() {
        logger.info("Setting up animation lib [developer mode]");
        handConfigs = registerKey("tools.animation.handConfig", handConfigKey);
        animator = registerKey("tools.animation.animator", animatorKey);
        logger.info("Key binds registered: {} - handConfigs, {} - animator", handConfigs.getKey().getName(), animator.getKey().getName());
        MinecraftForge.EVENT_BUS.addListener(this::handleKeys);
        logger.info("Key listener registered");
        Animator animator = Animator.get();
        loader().addListener(animator::onAnimationsLoaded);
        logger.info("Animation lib - READY");
    }

    public AnimationLoader loader() {
        return loader;
    }

    public IHandRenderAPI getHandRenderAPI() {
        return handRenderAPI;
    }

    public IUtilScreenFactory getScreenFactory() {
        return screenFactory;
    }

    public IAnimationPipeline pipeline() {
        return pipeline;
    }

    private KeyBinding registerKey(String name, int keycode) {
        KeyBinding bind = new KeyBinding(name, keycode, "tools.animation");
        ClientRegistry.registerKeyBinding(bind);
        return bind;
    }

    private void handleKeys(InputEvent.KeyInputEvent event) {
        IUtilScreenFactory factory = getScreenFactory();
        Minecraft mc = Minecraft.getInstance();
        if (handConfigs.isDown()) {
            mc.setScreen(factory.getHandConfigScreen());
        } else if (animator.isDown()) {
            mc.setScreen(factory.getAnimatorScreen());
        }
    }

    private static final AnimationCompatLayer INSTANCE = new AnimationCompatLayer();

    public static AnimationCompatLayer instance() {
        return INSTANCE;
    }

    private static class HandRenderAPI implements IHandRenderAPI {

        boolean dev;
        RenderConfig.Mutable right = new RenderConfig.Mutable();
        RenderConfig.Mutable left = new RenderConfig.Mutable();

        @Override
        public boolean isDevMode() {
            return dev;
        }

        @Override
        public IRenderConfig right() {
            return right;
        }

        @Override
        public IRenderConfig left() {
            return left;
        }
    }

    private class UtilScreenFactory implements IUtilScreenFactory {

        @Override
        public HandRenderScreen getHandConfigScreen() {
            HandRenderAPI api = AnimationCompatLayer.this.handRenderAPI;
            return new HandRenderScreen(api.left, api.right, active -> api.dev = active, api.isDevMode());
        }

        @Override
        public AnimatorScreen getAnimatorScreen() {
            return new AnimatorScreen();
        }
    }
}