package dev.toma.gunsrpg.common.attribute;

public interface IAttributeProvider {

    IAttribute getAttribute(IAttributeId id);

    void tick();

    void addAttribute(IAttributeId id);

    void removeAttribute(IAttributeId id);

    void removeAttribute(IAttribute attribute);

    void setAttributeValue(IAttributeId id, double value);

    double getAttributeValue(IAttributeId id);

    boolean hasAttribute(IAttributeId id);
}
