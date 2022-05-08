package dev.toma.gunsrpg.api.common.attribute;

public interface IAttributeProvider {

    IAttribute getAttribute(IAttributeId id);

    void tick();

    void addAttribute(IAttributeId id);

    double getAttributeValue(IAttributeId id);
}
