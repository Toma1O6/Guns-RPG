package lib.toma.animations.api;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;

/**
 * Handles animation playing, scheduling and removing.
 *
 * @author Toma
 */
public interface IAnimationPipeline {

    /**
     * Creates new animation from provided type and schedules it for playing.
     * If type provided by you doesn't have custom {@link IAnimationCreator} defined
     * (meaning {@link AnimationType#hasCreator()} returns {@code false}) exception will be logged
     * and no animation will be played.
     *
     * @param type Animation type
     * @param <A> Type of animation
     */
    <A extends IAnimation> void insert(AnimationType<A> type);

    /**
     * Insert supplied animation instance into this pipeline.
     * If animation is {@code null} exception is logged and no animation is played.
     *
     * @param type Animation type
     * @param animation Animation instance
     * @param <A> Type of animation
     */
    <A extends IAnimation> void insert(AnimationType<A> type, A animation);

    /**
     * Schedules animation for playing in specified amount of ticks (20 ticks = 1 second).
     * Exception is logged when the {@link AnimationType} provided doesn't have custom {@link IAnimationCreator} defined
     * (meaning {@link AnimationType#hasCreator()} returns {@code false}) and no animation is scheduled.
     * Animation is automatically played after specified amount of ticks, no further action is required to be done by you.
     *
     * @param type Animation type
     * @param gameTickDelay Delay in ticks
     * @param <A> Type of animation
     */
    <A extends IAnimation> void scheduleInsert(AnimationType<A> type, int gameTickDelay);

    /**
     * Schedules animation for playing in specified amount of ticks (20 ticks = 1 second).
     * After the time is up, animation instance is created from supplier and inserted into this
     * pipeline.
     *
     * @param type Animation type
     * @param animation Animation instance
     * @param gameTickDelay Delay in ticks
     * @param <A> Type of animation
     */
    <A extends IAnimation> void scheduleInsert(AnimationType<A> type, A animation, int gameTickDelay);

    /**
     * Stops and removes animation with specified animation type.
     *
     * @param type Animation type to remove
     */
    void remove(AnimationType<?> type);

    /**
     * Removed scheduled animation with specified animation type.
     *
     * @param type Animation type to remove
     */
    void removeScheduled(AnimationType<?> type);

    /**
     * Returns animation instance which is running.
     *
     * @param type Animation type to retrieve
     * @param <A> Type of animation
     * @return Active animation or {@code null} when no such animation exists
     */
    <A extends IAnimation> A get(AnimationType<A> type);

    /**
     * Determine whether animation is active
     * @param type Type of animation
     * @return {@code true} if the animation is currently active
     */
    boolean has(AnimationType<?> type);

    /**
     * Called every game tick to update animation progress
     */
    void handleGameTick();

    /**
     * Processes each frame pass. Used to interpolate animation progress
     * @param deltaRenderTime Current delta render time (also known as partialTicks)
     */
    void processFrame(float deltaRenderTime);

    /**
     * Processes all animations which have defined keyframes for current stage
     * @param stage Stage which is being processed
     * @param matrix Pose stack
     * @param buffer Render type buffer
     * @param light Light at player's location
     * @param overlay Overlay tint
     */
    void animateStage(AnimationStage stage, MatrixStack matrix, IRenderTypeBuffer buffer, int light, int overlay);
}
