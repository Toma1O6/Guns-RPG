package lib.toma.animations.api;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

/**
 * Animation lists allow you to play multiple animations of same type at the same time.
 * To play animation, use either {@link AnimationList#enqueue(AnimationType, IAnimation)} or
 * {@link AnimationList#enqueue(IAnimation)} if you have instance access.
 *
 * @param <A> Type of animations used by this list
 *
 * @author Toma
 */
public interface IAnimationList<A extends IAnimation> extends IAnimation {

    /**
     * Special animation processing in case you need to render more models into this animation
     * @param stage Processed stage
     * @param poseStack Current pose stack
     * @param vertexBuilder Vertex builder
     * @param light Light at player's location
     * @param overlay Overlay tint
     */
    void animateSpecial(AnimationStage stage, MatrixStack poseStack, IVertexBuilder vertexBuilder, int light, int overlay);

    /**
     * Allows you to render additional stuff
     * @param poseStack Current pose stack
     * @param vertexBuilder Vertex builder
     * @param light Light at player's location
     * @param overlay Overlay tint
     */
    void renderAdditional(MatrixStack poseStack, IVertexBuilder vertexBuilder, int light, int overlay);

    /**
     * Enqueues supplied animation for playing
     * @param animation Animation to play
     */
    void enqueue(A animation);

    /**
     * @return Amount of active animations in this list
     */
    int size();
}
