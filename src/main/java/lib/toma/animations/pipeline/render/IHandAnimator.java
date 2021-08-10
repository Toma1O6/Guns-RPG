package lib.toma.animations.pipeline.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import lib.toma.animations.IRenderConfig;
import lib.toma.animations.pipeline.IAnimationPipeline;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.HandSide;

import java.util.function.Function;

public interface IHandAnimator {

    void animateHands(MatrixStack poseStack, IRenderTypeBuffer buffer, int light, float equipProgress,
                      Function<HandSide, IRenderConfig> selector, IAnimationPipeline pipeline);
}
