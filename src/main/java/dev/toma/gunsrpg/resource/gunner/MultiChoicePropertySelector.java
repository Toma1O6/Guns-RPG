package dev.toma.gunsrpg.resource.gunner;

import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;

public class MultiChoicePropertySelector<P> implements IDifficultyProperty<P> {

    private final P[] props;

    public MultiChoicePropertySelector(P[] props) {
        this.props = props;
    }

    @Override
    public P getProperty(Difficulty difficulty) {
        int index = difficulty.getId() - 1;
        int safeIndex = MathHelper.clamp(index, 0, props.length - 1);
        return props[safeIndex];
    }
}
