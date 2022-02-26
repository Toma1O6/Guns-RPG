package dev.toma.gunsrpg.api.common.attribute;

public interface ITickableModifier extends IAttributeModifier {

    void tick();

    boolean shouldRemove();
}
