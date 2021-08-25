package lib.toma.animations.api;

import net.minecraftforge.eventbus.api.IEventBus;

/**
 * Render pipeline API. Allows direct modifications of render systems.
 *
 * @author Toma
 */
public interface IRenderPipeline {

    /**
     * Registers this render pipeline to specified event bus (should be {@link net.minecraftforge.common.MinecraftForge#EVENT_BUS})
     * and enables animation processing/rendering
     * @param bus Target event bus
     */
    void register(IEventBus bus);

    /**
     * Sets custom {@link IAnimator}
     * @param animator Animator implementation to be used
     */
    void setAnimator(IAnimator animator);

    /**
     * Sets custom {@link IHandAnimator}
     * @param animator Hand animator implementation to be used
     */
    void setHandAnimator(IHandAnimator animator);

    /**
     * Sets custom {@link IHandRenderer}
     * @param handRenderer Hand renderer implementation to be used
     */
    void setHandRenderer(IHandRenderer handRenderer);

    /**
     * Sets custom {@link IItemRenderer}
     * @param itemRenderer Item renderer implementation to be used
     */
    void setItemRenderer(IItemRenderer itemRenderer);

    /**
     * Sets {@link IAnimateCallback} which is called before animation processing
     * @param callback Callback method
     */
    void setPreAnimateCallback(IAnimateCallback callback);

    /**
     * Sets {@link IAnimateCallback} which is called after animation processing
     * @param callback Callback method
     */
    void setPostAnimateCallback(IAnimateCallback callback);

    /**
     * @return Gets currently used Animator
     */
    IAnimator getAnimator();

    /**
     * @return Gets currently used Hand animator
     */
    IHandAnimator getHandAnimator();

    /**
     * @return Gets current used Hand renderer
     */
    IHandRenderer getHandRenderer();

    /**
     * @return Gets currently used Item renderer
     */
    IItemRenderer getItemRenderer();
}
