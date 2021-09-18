package dev.toma.gunsrpg.common.attribute;

public interface ITickableModifier extends IAttributeModifier {

    void tick();

    boolean shouldRemove();
}
