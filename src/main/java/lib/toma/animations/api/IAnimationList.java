package lib.toma.animations.api;

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
     * Enqueues supplied animation for playing
     * @param animation Animation to play
     */
    void enqueue(A animation);

    /**
     * @return Amount of active animations in this list
     */
    int size();
}
