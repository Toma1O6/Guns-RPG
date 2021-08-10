package lib.toma.animations.pipeline.render;

import net.minecraftforge.eventbus.api.IEventBus;

public interface IRenderPipeline {

    void register(IEventBus bus);

    void setAnimator(IAnimator animator);

    void setHandAnimator(IHandAnimator animator);

    void setHandRenderer(IHandRenderer handRenderer);

    void setItemRenderer(IItemRenderer itemRenderer);

    void setPreAnimateCallback(IAnimateCallback callback);

    void setPostAnimateCallback(IAnimateCallback callback);

    IAnimator getAnimator();

    IHandAnimator getHandAnimator();

    IHandRenderer getHandRenderer();

    IItemRenderer getItemRenderer();
}
