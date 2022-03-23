package dev.toma.gunsrpg.common.attribute;

import dev.toma.gunsrpg.api.common.attribute.IAttributeModifier;
import dev.toma.gunsrpg.api.common.attribute.IValueFormatter;
import dev.toma.gunsrpg.util.Constants;

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
    public static final IAttributeModifier MASTER_BOLT                  = new AttributeModifier("A10BC07F-641B-4CB9-A753-D4506667C4CA", AttributeOps.SUM, 2).named("bolt");
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
    public static final IAttributeModifier DUAL_WIELD                   = new AttributeModifier("0B46D76F-2357-4346-80D8-CB68302B0346", AttributeOps.MUL, 2.0);
    public static final IAttributeModifier TOUGH_SPRING                 = new AttributeModifier("131CDD15-9B12-43BD-9242-F086C915473A", AttributeOps.SUB, 1).named("rate");
    public static final IAttributeModifier QUICKDRAW_MAG                = new AttributeModifier("640F448C-136C-470B-A9DD-95B9D51BF502", AttributeOps.MUL, 0.7).named("speed", IValueFormatter.INV_PERCENT);
    public static final IAttributeModifier VERTICAL_GRIP                = new AttributeModifier("1CD5BFD7-6FB6-41A8-83F9-D5FD554269C8", AttributeOps.MUL, 0.7);
    public static final IAttributeModifier BULLET_LOOPS                 = new AttributeModifier("DF644018-1CF2-4DB4-83E8-0F2A65FDD984", AttributeOps.MUL, 0.8).named("speed", IValueFormatter.INV_PERCENT);
    public static final IAttributeModifier CHEEKPAD                     = new AttributeModifier("531F8EF1-15FA-4BF0-883D-F6B445644CC4", AttributeOps.MUL, 0.75);
    public static final IAttributeModifier NOISE                        = new AttributeModifier("2485AB60-AF4F-4074-A689-DFE4A083CC19", AttributeOps.MUL, 0.2);
    /* Specific attachments */
    public static final IAttributeModifier M1911_EXTENDED_MAG           = new AttributeModifier("205CBB5C-0D78-4ABB-A623-F7AB91AF256C", AttributeOps.SUM, 6).named("capacity");
    public static final IAttributeModifier M1911_CARBON_BARREL          = new AttributeModifier("C0C19080-45A4-48C4-AB4E-D383D1FA8A70", AttributeOps.MUL, 0.65);
    public static final IAttributeModifier UMP45_EXTENDED               = new AttributeModifier("26C18B3B-29AC-4A76-95C8-F1251EA78537", AttributeOps.SUM, 15).named("capacity");
    public static final IAttributeModifier CROSSBOW_EXTENDED            = new AttributeModifier("C997FD4C-9156-40CD-A226-6F4B02F5B298", AttributeOps.SUM, 2).named("capacity");
    public static final IAttributeModifier CROSSBOW_QUIVER              = new AttributeModifier("A1696B44-D082-498C-9E96-3CB584DA0DA7", AttributeOps.MUL, 0.65).named("speed", IValueFormatter.INV_PERCENT);
    public static final IAttributeModifier CROSSBOW_REPEATER            = new AttributeModifier("6899C770-39CA-465E-8715-4D5D47512ED4", AttributeOps.MUL, 1.25);
    public static final IAttributeModifier SKS_EXTENDED                 = new AttributeModifier("6BC9638B-0A31-4522-BDE7-DA68F0AEA194", AttributeOps.SUM, 10).named("capacity");
    public static final IAttributeModifier SKS_ADAPTIVE                 = new AttributeModifier("6FA1DA42-2CF1-41A2-B548-A056DB214927", AttributeOps.SUB, 2);
    public static final IAttributeModifier S1897_EXTENDED               = new AttributeModifier("29BBE0CB-B953-4EF1-AB49-036A39058A0D", AttributeOps.SUM, 3).named("capacity");
    public static final IAttributeModifier S1897_FAST_PUMP              = new AttributeModifier("68F45BF3-85BF-4F5D-AF80-85771DECF85F", AttributeOps.SUB, 10);
    public static final IAttributeModifier S1897_CHOKE                  = new AttributeModifier("4A2A2F96-8EF1-4E6B-882D-1B88910D0D11", AttributeOps.MUL, 0.7).named("capacity");
    public static final IAttributeModifier KAR98K_EXTENDED              = new AttributeModifier("195ED54A-A61C-4A25-A603-9BC15889033F", AttributeOps.SUM, 5).named("capacity");
    public static final IAttributeModifier KAR98K_FAST_HANDS_ROF        = new AttributeModifier("7F330435-D62F-4921-B2A3-3B06F37C2FEC", AttributeOps.MUL, 0.6).named("rate", IValueFormatter.INV_PERCENT);
    public static final IAttributeModifier KAR98K_FAST_HANDS_RELOAD     = new AttributeModifier("CAE9F279-C696-46A3-8E09-8BC21460F12D", AttributeOps.MUL, 0.5).named("speed", IValueFormatter.INV_PERCENT);
    public static final IAttributeModifier KAR98K_DEAD_EYE              = new AttributeModifier("3EF88EFE-B623-4F78-A272-B2E981A9A3E1", AttributeOps.MUL, 2.5).named("hs_damage", IValueFormatter.PERCENT);
}
