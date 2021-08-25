package lib.toma.animations.api;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;

/**
 * @author Toma
 */
public interface IAnimation {

    /**
     * Animates specified stage. For example right hand.
     *  @param stage Stage which is currently being animated.
     * @param matrixStack PoseStack containing all transforms which will be applied.
     * @param typeBuffer Render type buffer
     * @param light Light at player's location
     * @param overlay Overlay tint
     */
    void animate(AnimationStage stage, MatrixStack matrixStack, IRenderTypeBuffer typeBuffer, int light, int overlay);

    /**
     * Called on game tick, useful for updating tickable animations for example
     */
    void gameTick();

    /**
     * Called when frame is being rendered, mainly used for updating deltaRenderTime variable for interpolations
     * @param deltaRenderTime Current delta time between ticks
     */
    void renderTick(float deltaRenderTime);

    /**
     * Marks animation as finished and ready for removal
     * @return whether this animation can be removed from tracked animations
     */
    boolean hasFinished();
}
