package lib.toma.animations;

import lib.toma.animations.api.*;
import lib.toma.animations.api.lifecycle.Registries;
import lib.toma.animations.engine.*;
import lib.toma.animations.engine.screen.HandRenderScreen;
import lib.toma.animations.engine.screen.animator.Animator;
import lib.toma.animations.engine.screen.animator.AnimatorScreen;
import lib.toma.animations.engine.serialization.AnimationLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.lwjgl.glfw.GLFW;

import java.util.stream.Stream;

public final class AnimationEngine {

    public static final Marker MARKER = MarkerManager.getMarker("Engine");
    public static final Logger logger = LogManager.getLogger("AnimationLib");
    private static final String ENGINE_VERSION = "1.4.2";

    public int handConfigKey = GLFW.GLFW_KEY_KP_9;
    public int animatorKey = GLFW.GLFW_KEY_KP_8;
    private final HandRenderAPI handRenderAPI;
    private final UtilScreenFactory screenFactory;
    private final AnimationPipeline pipeline;
    private final AnimationLoader loader;
    private final MainRenderPipeline renderPipeline;
    private final TickHandler tickHandler;

    private KeyBinding handConfigs;
    private KeyBinding animator;

    private AnimationEngine() {
        handRenderAPI = new HandRenderAPI();
        screenFactory = new UtilScreenFactory();
        pipeline = new AnimationPipeline();
        loader = new AnimationLoader();
        renderPipeline = new MainRenderPipeline();
        tickHandler = new TickHandler();
    }

    /**
     * Setup method. Call to enable animations
     * @param enableDeveloperTools Determines whether developer tools will be available.
     */
    public synchronized void startEngine(boolean enableDeveloperTools) {
        logger.info(MARKER, "Starting animation engine [{} mode]", enableDeveloperTools ? "Developer" : "User");
        logger.info(MARKER, "Engine version {}", ENGINE_VERSION);
        logger.info(MARKER, "Creating registries");
        initRegistries(enableDeveloperTools);
        IEventBus eventBus = MinecraftForge.EVENT_BUS;
        eventBus.addListener(tickHandler::onGameTick);
        eventBus.addListener(tickHandler::onFrameTick);
        ((IReloadableResourceManager) Minecraft.getInstance().getResourceManager()).registerReloadListener(loader);
        logger.info(MARKER, "Registered animation resource manager");
        if (enableDeveloperTools) {
            devSetup();
        }
        logger.info(MARKER, "Animation engine - READY");
    }

    private void initRegistries(boolean isDevMode) {
        Stream.of(Registries.ANIMATION_TYPES, Registries.ANIMATION_STAGES, Registries.EVENTS).map(iRegistry -> (Registry<?>) iRegistry).forEach(registry -> registry.load(isDevMode));
    }

    private void devSetup() {
        handConfigs = registerKey("tools.animation.handConfig", handConfigKey);
        animator = registerKey("tools.animation.animator", animatorKey);
        logger.info(MARKER, "Key binds registered: {} - handConfigs, {} - animator", handConfigs.getKey().getName(), animator.getKey().getName());
        MinecraftForge.EVENT_BUS.addListener(this::handleKeys);
        logger.info(MARKER, "Key listener registered");
        Animator animator = Animator.get();
        loader().addLoadingListener(animator::onAnimationsLoaded);
    }

    public IAnimationLoader loader() {
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

    public IRenderPipeline renderPipeline() {
        return renderPipeline;
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

    private static final AnimationEngine INSTANCE = new AnimationEngine();

    public static AnimationEngine get() {
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
            HandRenderAPI api = AnimationEngine.this.handRenderAPI;
            return new HandRenderScreen(api.left, api.right, active -> api.dev = active, api.isDevMode());
        }

        @Override
        public AnimatorScreen getAnimatorScreen() {
            return new AnimatorScreen();
        }
    }
}
