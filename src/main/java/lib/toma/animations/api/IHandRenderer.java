package lib.toma.animations.api;

import com.mojang.blaze3d.matrix.MatrixStack;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.HandSide;

import java.util.function.Function;

public interface IHandRenderer {
    void renderHand(MatrixStack stack, HandSide side, Function<HandSide, IRenderConfig> selector, IRenderTypeBuffer buffer, int light);
}
