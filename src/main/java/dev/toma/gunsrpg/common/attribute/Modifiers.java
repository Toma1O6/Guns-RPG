package dev.toma.gunsrpg.common.attribute;

public class Modifiers {

    /* Common attachments */
    public static final IAttributeModifier DUAL_WIELD = new AttributeModifier("0B46D76F-2357-4346-80D8-CB68302B0346", AttributeOps.MUL, 2.0);
    public static final IAttributeModifier TOUGH_SPRING = new AttributeModifier("131CDD15-9B12-43BD-9242-F086C915473A", AttributeOps.SUM, -1);
    public static final IAttributeModifier QUICKDRAW_MAG = new AttributeModifier("640F448C-136C-470B-A9DD-95B9D51BF502", AttributeOps.MUL, 0.7);
    public static final IAttributeModifier VERTICAL_GRIP = new AttributeModifier("1CD5BFD7-6FB6-41A8-83F9-D5FD554269C8", AttributeOps.MUL, 0.7);

    /* Specific attachments */
    public static final IAttributeModifier M1911_EXTENDED_MAG = new AttributeModifier("205CBB5C-0D78-4ABB-A623-F7AB91AF256C", AttributeOps.SUM, 6);
    public static final IAttributeModifier M1911_CARBON_BARREL = new AttributeModifier("C0C19080-45A4-48C4-AB4E-D383D1FA8A70", AttributeOps.MUL, 0.65);
    public static final IAttributeModifier UMP45_EXTENDED = new AttributeModifier("26C18B3B-29AC-4A76-95C8-F1251EA78537", AttributeOps.SUM, 15);
}
