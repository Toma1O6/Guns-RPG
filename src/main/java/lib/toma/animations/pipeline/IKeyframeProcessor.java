package lib.toma.animations.pipeline;

import com.mojang.blaze3d.matrix.MatrixStack;
import lib.toma.animations.pipeline.frame.IKeyframe;

public interface IKeyframeProcessor {

    void combine(IKeyframe currentFrame, IKeyframe oldFrame, MatrixStack matrix);
}
