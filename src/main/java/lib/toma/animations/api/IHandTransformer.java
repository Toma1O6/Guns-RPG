package lib.toma.animations.api;

/**
 * Handles hand transforms for each hand side
 */
public interface IHandTransformer {

    /**
     * Returns render transform for right hand
     * @return Render transform for right hand
     */
    IRenderConfig right();

    /**
     * Returns render transform for left hand
     * @return Render transform for left hand
     */
    IRenderConfig left();
}
