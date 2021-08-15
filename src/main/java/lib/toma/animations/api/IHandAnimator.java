package lib.toma.animations.api;

import com.mojang.blaze3d.matrix.MatrixStack;
import lib.toma.animations.api.IRenderConfig;
import lib.toma.animations.api.IAnimationPipeline;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.HandSide;

import java.util.function.Function;

public interface IHandAnimator {

    void animateHands(MatrixStack poseStack, IRenderTypeBuffer buffer, int light, float equipProgress,
                      Function<HandSide, IRenderConfig> selector, IAnimationPipeline pipeline);
}
