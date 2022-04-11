package dev.toma.gunsrpg.common.attribute;

import dev.toma.gunsrpg.api.common.attribute.IAttributeModifier;
import dev.toma.gunsrpg.api.common.attribute.IValueFormatter;

import static dev.toma.gunsrpg.util.Constants.ModifierIds.*;

public final class Modifiers {

    /* Crafting modifiers */
    public static final IAttributeModifier GUNPOWDER_I                  = new AttributeModifier(GUNPOWDER_CRAFTING, AttributeOps.SUM, 2).named("count");
    public static final IAttributeModifier GUNPOWDER_II                 = new AttributeModifier(GUNPOWDER_CRAFTING, AttributeOps.SUM, 4).named("count");
    public static final IAttributeModifier GUNPOWDER_III                = new AttributeModifier(GUNPOWDER_CRAFTING, AttributeOps.SUM, 6).named("count");
    public static final IAttributeModifier BONEMEAL_I                   = new AttributeModifier(BONEMEAL_CRAFTING, AttributeOps.SUM, 4).named("count");
    public static final IAttributeModifier BONEMEAL_II                  = new AttributeModifier(BONEMEAL_CRAFTING, AttributeOps.SUM, 5).named("count");
    public static final IAttributeModifier BONEMEAL_III                 = new AttributeModifier(BONEMEAL_CRAFTING, AttributeOps.SUM, 6).named("count");
    public static final IAttributeModifier BLAZEPOWDER_I                = new AttributeModifier(BLAZEPOWEDER_CRAFTING, AttributeOps.SUM, 2).named("count");
    public static final IAttributeModifier BLAZEPOWDER_II               = new AttributeModifier(BLAZEPOWEDER_CRAFTING, AttributeOps.SUM, 3).named("count");
    public static final IAttributeModifier BLAZEPOWDER_III              = new AttributeModifier(BLAZEPOWEDER_CRAFTING, AttributeOps.SUM, 4).named("count");
    public static final IAttributeModifier MASTER_AMMO                  = new AttributeModifier("F932A216-605A-4C07-9017-EA3E8A22B46F", AttributeOps.SUM, 1).named("ammo");
    /* General modifiers */
    public static final IAttributeModifier ACROBATICS_FALL_I            = new AttributeModifier(FALL_RESIST, AttributeOps.SUM, 0.2).named("fall", IValueFormatter.PERCENT);
    public static final IAttributeModifier ACROBATICS_FALL_II           = new AttributeModifier(FALL_RESIST, AttributeOps.SUM, 0.45).named("fall", IValueFormatter.PERCENT);
    public static final IAttributeModifier ACROBATICS_FALL_III          = new AttributeModifier(FALL_RESIST, AttributeOps.SUM, 0.7).named("fall", IValueFormatter.PERCENT);
    public static final IAttributeModifier ACROBATICS_EXPLOSION_I       = new AttributeModifier(EXPLOSION_RESIST, AttributeOps.SUM, 0.1).named("explosion", IValueFormatter.PERCENT);
    public static final IAttributeModifier ACROBATICS_EXPLOSION_II      = new AttributeModifier(EXPLOSION_RESIST, AttributeOps.SUM, 0.25).named("explosion", IValueFormatter.PERCENT);
    public static final IAttributeModifier ACROBATICS_EXPLOSION_III     = new AttributeModifier(EXPLOSION_RESIST, AttributeOps.SUM, 0.4).named("explosion", IValueFormatter.PERCENT);
    public static final IAttributeModifier CHOPPING_I                   = new AttributeModifier(CHOPPING_SPEED, AttributeOps.MULB, 0.2).named("speed", IValueFormatter.PERCENT);
    public static final IAttributeModifier CHOPPING_II                  = new AttributeModifier(CHOPPING_SPEED, AttributeOps.MULB, 0.4).named("speed", IValueFormatter.PERCENT);
    public static final IAttributeModifier CHOPPING_III                 = new AttributeModifier(CHOPPING_SPEED, AttributeOps.MULB, 0.6).named("speed", IValueFormatter.PERCENT);
    public static final IAttributeModifier CHOPPING_IV                  = new AttributeModifier(CHOPPING_SPEED, AttributeOps.MULB, 0.8).named("speed", IValueFormatter.PERCENT);
    public static final IAttributeModifier CHOPPING_V                   = new AttributeModifier(CHOPPING_SPEED, AttributeOps.MULB, 1.0).named("speed", IValueFormatter.PERCENT);
    public static final IAttributeModifier MINING_I                     = new AttributeModifier(MINING_SPEED, AttributeOps.MULB, 0.2).named("speed", IValueFormatter.PERCENT);
    public static final IAttributeModifier MINING_II                    = new AttributeModifier(MINING_SPEED, AttributeOps.MULB, 0.4).named("speed", IValueFormatter.PERCENT);
    public static final IAttributeModifier MINING_III                   = new AttributeModifier(MINING_SPEED, AttributeOps.MULB, 0.6).named("speed", IValueFormatter.PERCENT);
    public static final IAttributeModifier MINING_IV                    = new AttributeModifier(MINING_SPEED, AttributeOps.MULB, 0.8).named("speed", IValueFormatter.PERCENT);
    public static final IAttributeModifier MINING_V                     = new AttributeModifier(MINING_SPEED, AttributeOps.MULB, 1.0).named("speed", IValueFormatter.PERCENT);
    public static final IAttributeModifier DIGGING_I                    = new AttributeModifier(DIGGING_SPEED, AttributeOps.MULB, 0.15).named("speed", IValueFormatter.PERCENT);
    public static final IAttributeModifier DIGGING_II                   = new AttributeModifier(DIGGING_SPEED, AttributeOps.MULB, 0.30).named("speed", IValueFormatter.PERCENT);
    public static final IAttributeModifier DIGGING_III                  = new AttributeModifier(DIGGING_SPEED, AttributeOps.MULB, 0.45).named("speed", IValueFormatter.PERCENT);
    public static final IAttributeModifier DIGGING_IV                   = new AttributeModifier(DIGGING_SPEED, AttributeOps.MULB, 0.60).named("speed", IValueFormatter.PERCENT);
    public static final IAttributeModifier DIGGING_V                    = new AttributeModifier(DIGGING_SPEED, AttributeOps.MULB, 0.80).named("speed", IValueFormatter.PERCENT);
    public static final IAttributeModifier DAMAGE_I                     = new AttributeModifier(STRONG_MUSCLES, AttributeOps.SUM, 1).named("damage");
    public static final IAttributeModifier DAMAGE_II                    = new AttributeModifier(STRONG_MUSCLES, AttributeOps.SUM, 2).named("damage");
    public static final IAttributeModifier DAMAGE_III                   = new AttributeModifier(STRONG_MUSCLES, AttributeOps.SUM, 4).named("damage");
    public static final IAttributeModifier AGILITY_I                    = new AttributeModifier(AGILITY, AttributeOps.MUL, 0.95).named("speed", IValueFormatter.INV_PERCENT);
    public static final IAttributeModifier AGILITY_II                   = new AttributeModifier(AGILITY, AttributeOps.MUL, 0.90).named("speed", IValueFormatter.INV_PERCENT);
    public static final IAttributeModifier AGILITY_III                  = new AttributeModifier(AGILITY, AttributeOps.MUL, 0.80).named("speed", IValueFormatter.INV_PERCENT);
    public static final IAttributeModifier INSTANT_KILL_I               = new AttributeModifier(SKULL_CRUSHER, AttributeOps.SUM, 0.03).named("chance", IValueFormatter.PERCENT);
    public static final IAttributeModifier INSTANT_KILL_II              = new AttributeModifier(SKULL_CRUSHER, AttributeOps.SUM, 0.05).named("chance", IValueFormatter.PERCENT);
    public static final IAttributeModifier INSTANT_KILL_III             = new AttributeModifier(SKULL_CRUSHER, AttributeOps.SUM, 0.07).named("chance", IValueFormatter.PERCENT);
    public static final IAttributeModifier REPAIR_MAN_I                 = new AttributeModifier(REPAIR_MAN, AttributeOps.SUM, 0.10).named("repair");
    public static final IAttributeModifier REPAIR_MAN_II                = new AttributeModifier(REPAIR_MAN, AttributeOps.SUM, 0.15).named("repair");
    public static final IAttributeModifier REPAIR_MAN_III               = new AttributeModifier(REPAIR_MAN, AttributeOps.SUM, 0.20).named("repair");
    public static final IAttributeModifier WEAPON_DURABILITY_I          = new AttributeModifier(WEAPON_DURABILITY, AttributeOps.MUL, 0.95).named("durability");
    public static final IAttributeModifier WEAPON_DURABILITY_II         = new AttributeModifier(WEAPON_DURABILITY, AttributeOps.MUL, 0.95).named("durability");
    public static final IAttributeModifier WEAPON_DURABILITY_III        = new AttributeModifier(WEAPON_DURABILITY, AttributeOps.MUL, 0.95).named("durability");
    public static final IAttributeModifier JAMMING_I                    = new AttributeModifier(WEAPON_JAMMING, AttributeOps.MUL, 0.95).named("jamming");
    public static final IAttributeModifier JAMMING_II                   = new AttributeModifier(WEAPON_JAMMING, AttributeOps.MUL, 0.85).named("jamming");
    public static final IAttributeModifier JAMMING_III                  = new AttributeModifier(WEAPON_JAMMING, AttributeOps.MUL, 0.75).named("jamming");
    /* Debuff modifiers */
    public static final IAttributeModifier DEBUFF_RESIST_I              = new AttributeModifier(DEBUFF_RESISTANCE, AttributeOps.SUM, 0.15).named("resist", IValueFormatter.PERCENT);
    public static final IAttributeModifier DEBUFF_DELAY_I               = new AttributeModifier(DEBUFF_DELAY, AttributeOps.SUM, 20).named("delay", IValueFormatter.SECONDS);
    public static final IAttributeModifier DEBUFF_RESIST_II             = new AttributeModifier(DEBUFF_RESISTANCE, AttributeOps.SUM, 0.30).named("resist", IValueFormatter.PERCENT);
    public static final IAttributeModifier DEBUFF_DELAY_II              = new AttributeModifier(DEBUFF_DELAY, AttributeOps.SUM, 40).named("delay", IValueFormatter.SECONDS);
    public static final IAttributeModifier DEBUFF_RESIST_III            = new AttributeModifier(DEBUFF_RESISTANCE, AttributeOps.SUM, 0.45).named("resist", IValueFormatter.PERCENT);
    public static final IAttributeModifier DEBUFF_DELAY_III             = new AttributeModifier(DEBUFF_DELAY, AttributeOps.SUM, 60).named("delay", IValueFormatter.SECONDS);
    /* Common attachments */
    public static final IAttributeModifier TOUGH_SPRING                 = new AttributeModifier("131CDD15-9B12-43BD-9242-F086C915473A", AttributeOps.SUB, 1).named("rate");
    public static final IAttributeModifier QUICKDRAW_MAG                = new AttributeModifier("640F448C-136C-470B-A9DD-95B9D51BF502", AttributeOps.MUL, 0.8).named("speed", IValueFormatter.INV_PERCENT);
    public static final IAttributeModifier QUIVER                       = new AttributeModifier("A1696B44-D082-498C-9E96-3CB584DA0DA7", AttributeOps.MUL, 0.75).named("speed", IValueFormatter.INV_PERCENT);
    public static final IAttributeModifier VERTICAL_GRIP                = new AttributeModifier("1CD5BFD7-6FB6-41A8-83F9-D5FD554269C8", AttributeOps.MUL, 0.7);
    public static final IAttributeModifier BULLET_LOOPS                 = new AttributeModifier("DF644018-1CF2-4DB4-83E8-0F2A65FDD984", AttributeOps.MUL, 0.75).named("speed", IValueFormatter.INV_PERCENT);
    public static final IAttributeModifier CHEEKPAD                     = new AttributeModifier("531F8EF1-15FA-4BF0-883D-F6B445644CC4", AttributeOps.MUL, 0.75);
    public static final IAttributeModifier NOISE                        = new AttributeModifier("2485AB60-AF4F-4074-A689-DFE4A083CC19", AttributeOps.MUL, 0.2);
    public static final IAttributeModifier CARBON_BARREL                = new AttributeModifier("C0C19080-45A4-48C4-AB4E-D383D1FA8A70", AttributeOps.MUL, 0.7);
    public static final IAttributeModifier COMPENSATOR                  = new AttributeModifier("D792939F-AE5E-47A2-906E-BBA7430594A9", AttributeOps.MUL, 0.8);
    /* Specific attachments */
    public static final IAttributeModifier M1911_EXTENDED_MAG           = new AttributeModifier("205CBB5C-0D78-4ABB-A623-F7AB91AF256C", AttributeOps.SUM, 6).named("capacity");
    public static final IAttributeModifier R45_EXTENDED_DRUM            = new AttributeModifier("ED6581E6-8C4E-45AD-9B8C-E014717B16EB", AttributeOps.SUM, 3).named("capacity");
    public static final IAttributeModifier DEAGLE_EXTENDED_MAG          = new AttributeModifier("A52A4D8E-1DA5-40A8-8215-6384CD721C42", AttributeOps.SUM, 5).named("capacity");
    public static final IAttributeModifier UMP45_EXTENDED               = new AttributeModifier("26C18B3B-29AC-4A76-95C8-F1251EA78537", AttributeOps.SUM, 15).named("capacity");
    public static final IAttributeModifier THOMPSON_EXTENDED            = new AttributeModifier("97F9C1A6-F2C7-4E63-8479-D1A75D6C0F5A", AttributeOps.SUM, 20).named("capacity");
    public static final IAttributeModifier VECTOR_EXTENDED              = new AttributeModifier("856037EC-5EF3-45E6-9635-BCD2009DB4AC", AttributeOps.SUM, 16).named("capacity");
    public static final IAttributeModifier VECTOR_EXTENDED_MK2          = new AttributeModifier("0282F2EF-13A2-49A5-A2DB-9BB00B9055CB", AttributeOps.SUM, 17).named("capacity");
    public static final IAttributeModifier CROSSBOW_EXTENDED            = new AttributeModifier("C997FD4C-9156-40CD-A226-6F4B02F5B298", AttributeOps.SUM, 2).named("capacity");
    public static final IAttributeModifier CROSSBOW_REPEATER            = new AttributeModifier("6899C770-39CA-465E-8715-4D5D47512ED4", AttributeOps.MUL, 1.25);
    public static final IAttributeModifier CHUKONU_EXTENDED             = new AttributeModifier("84158AAA-3361-483A-A2F2-06B98238A819", AttributeOps.SUM, 4).named("capacity");
    public static final IAttributeModifier SKS_EXTENDED                 = new AttributeModifier("6BC9638B-0A31-4522-BDE7-DA68F0AEA194", AttributeOps.SUM, 10).named("capacity");
    public static final IAttributeModifier SKS_ADAPTIVE                 = new AttributeModifier("6FA1DA42-2CF1-41A2-B548-A056DB214927", AttributeOps.SUB, 3);
    public static final IAttributeModifier S1897_EXTENDED               = new AttributeModifier("29BBE0CB-B953-4EF1-AB49-036A39058A0D", AttributeOps.SUM, 3).named("capacity");
    public static final IAttributeModifier S1897_FAST_PUMP              = new AttributeModifier("68F45BF3-85BF-4F5D-AF80-85771DECF85F", AttributeOps.SUB, 10);
    public static final IAttributeModifier S12K_DRUM                    = new AttributeModifier("623192D9-CE52-44E6-A0E0-C99AB29CF01D", AttributeOps.SUM, 7).named("capacity");
    public static final IAttributeModifier S12K_SPRING                  = new AttributeModifier("E24EEFD0-4BBD-4346-8938-03FB75227537", AttributeOps.SUB, 3).named("rate");
    public static final IAttributeModifier AKM_EXTENDED                 = new AttributeModifier("3125E4EA-1EC1-42A3-8ADB-34D9AFBD81FB", AttributeOps.SUM, 15).named("capacity");
    public static final IAttributeModifier HK416_EXTENDED               = new AttributeModifier("F781C227-0B56-4E3D-A0FB-B5A8A50C2DC9", AttributeOps.SUM, 10).named("capacity");
    public static final IAttributeModifier HK416_EXTENDED_MK2           = new AttributeModifier("866ECD71-95AC-4604-87F5-B6438168CD3C", AttributeOps.SUM, 10).named("capacity");
    public static final IAttributeModifier AUG_EXTENDED                 = new AttributeModifier("0A8A51D8-F311-4F56-B17B-F0DFCFAFB50E", AttributeOps.SUM, 10).named("capacity");
    public static final IAttributeModifier AUG_EXTENDED_MK2             = new AttributeModifier("2C8B724B-C21A-48A7-AE30-86616407060A", AttributeOps.SUM, 10).named("capacity");
    public static final IAttributeModifier VSS_EXTENDED                 = new AttributeModifier("45D5A204-8FFD-4DC3-AA3A-3024B0215DC8", AttributeOps.SUM, 10).named("capacity");
    public static final IAttributeModifier MK14_EXTENDED                = new AttributeModifier("64935CB1-F12A-4584-B6E2-FAC0E8FCF5A4", AttributeOps.SUM, 10).named("capacity");
    public static final IAttributeModifier MK14_EXTENDED_MK2            = new AttributeModifier("C2CF66E2-38F1-48FA-AC7B-A8CAC952C11E", AttributeOps.SUM, 10).named("capacity");
    public static final IAttributeModifier MK14_HEADSHOT                = new AttributeModifier("292E426D-4C62-47B3-8EC1-295CBC2A979B", AttributeOps.MULB, 0.2).named("hs_damage", IValueFormatter.PERCENT);
    public static final IAttributeModifier KAR98K_FAST_HANDS_ROF        = new AttributeModifier("7F330435-D62F-4921-B2A3-3B06F37C2FEC", AttributeOps.MUL, 0.6).named("rate", IValueFormatter.INV_PERCENT);
    public static final IAttributeModifier KAR98K_FAST_HANDS_RELOAD     = new AttributeModifier("CAE9F279-C696-46A3-8E09-8BC21460F12D", AttributeOps.MUL, 0.5).named("speed", IValueFormatter.INV_PERCENT);
    public static final IAttributeModifier KAR98K_DEAD_EYE              = new AttributeModifier("3EF88EFE-B623-4F78-A272-B2E981A9A3E1", AttributeOps.MULB, 0.25).named("hs_damage", IValueFormatter.PERCENT);
    public static final IAttributeModifier WINCHESTER_EXTENDED          = new AttributeModifier("59D27486-9F92-4300-AEBC-AA61907F02BC", AttributeOps.SUM, 6).named("capacity");
    public static final IAttributeModifier WINCHESTER_FAST_HANDS        = new AttributeModifier("9A3816A9-AABF-4DCD-A6B4-B960962B271C", AttributeOps.MUL, 0.8).named("rate", IValueFormatter.INV_PERCENT);
    public static final IAttributeModifier AWM_EXTENDED                 = new AttributeModifier("C7D72E09-C3C3-4C07-AD21-C89E551187CD", AttributeOps.SUM, 3).named("capacity");
    public static final IAttributeModifier AWM_FAST_HANDS               = new AttributeModifier("E114D178-708A-4387-BC98-ABC9792E4B49", AttributeOps.MUL, 0.8).named("rate", IValueFormatter.INV_PERCENT);
    public static final IAttributeModifier AWM_HEADSHOT                 = new AttributeModifier("F84D36CB-49CF-4E0E-AABF-0524CA690C1D", AttributeOps.MULB, 0.3).named("hs_damage", IValueFormatter.PERCENT);
    public static final IAttributeModifier GL_RELOAD                    = new AttributeModifier("227EE37D-E8A1-4A62-8FCD-E64D260B27E0", AttributeOps.MUL, 0.75).named("speed", IValueFormatter.INV_PERCENT);
    public static final IAttributeModifier GL_EXTENDED                  = new AttributeModifier("C5E2EF87-53E6-4FFD-9121-55DF3AA5DDA7", AttributeOps.SUM, 2).named("capacity");
    public static final IAttributeModifier GL_FIRERATE                  = new AttributeModifier("C665BF36-89B8-4AAF-8CE4-803555AFA061", AttributeOps.SUB, 3).named("rate");
    public static final IAttributeModifier RL_RELOAD                    = new AttributeModifier("08C7700F-7E86-4E85-BE9B-6236C7F08F0E", AttributeOps.MUL, 0.75).named("speed", IValueFormatter.INV_PERCENT);
}
