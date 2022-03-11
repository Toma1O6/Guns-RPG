package dev.toma.gunsrpg.common.attribute;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.attribute.IAttributeId;
import dev.toma.gunsrpg.api.common.attribute.ICombinedAttribute;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class Attribs {

    private static final Map<ResourceLocation, IAttributeId> KEY_MAP = new HashMap<>();

    // crafting, rewards
    public static final IAttributeId GUNPOWDER_OUTPUT           = createInternal("gunpowder_out", 0);
    public static final IAttributeId BONEMEAL_OUTPUT            = createInternal("bonemeal_out", 0);
    public static final IAttributeId BLAZEPOWDER_OUTPUT         = createInternal("blazepowder_out", 0);
    public static final IAttributeId AMMO_OUTPUT                = createInternal("ammo_output", 0);
    public static final IAttributeId QUEST_AMMO                 = createInternal("quest_ammo");
    // debuffs, healing
    public static final IAttributeId HEAL_BOOST                 = createInternal("heal_boost");
    public static final IAttributeId ADRENALINE_EFFECT          = createInternal("adrenaline_effect");
    public static final IAttributeId MORPHINE_EFFECT            = createInternal("morphine_effect");
    public static final IAttributeId STEROIDS_EFFECT            = createInternal("steroids_effect");
    public static final IAttributeId POTION_EFFECT              = createInternal("potion_effect");
    public static final IAttributeId ANTIDOTE_EFFECT            = createInternal("antidote_effect");
    public static final IAttributeId VACCINE_EFFECT             = createInternal("vaccine_effect");
    public static final IAttributeId SPLINT_EFFECT              = createInternal("splint_effect");
    public static final IAttributeId BANDAGE_EFFECT             = createInternal("bandage_effect");
    public static final IAttributeId HEMOSTAT_EFFECT            = createInternal("hemostat_effect");
    public static final IAttributeId CALCIUM_SHOT_EFFECT        = createInternal("calcium_shot_effect");
    public static final IAttributeId VITAMINS_EFFECT            = createInternal("vitamins_effect");
    public static final IAttributeId PROPITAL_EFFECT            = createInternal("propital_effect");
    public static final IAttributeId POISON_RESISTANCE          = createInternal("poison_resistance", 0.0);
    public static final IAttributeId POISON_DELAY               = createInternal("poison_delay", 140);
    public static final IAttributeId POISON_BLOCK               = createInternal("poison_block", 0);
    public static final IAttributeId INFECTION_RESISTANCE       = createInternal("infection_resistance", 0.0);
    public static final IAttributeId INFECTION_DELAY            = createInternal("infection_delay", 200);
    public static final IAttributeId INFECTION_BLOCK            = createInternal("infection_block", 0);
    public static final IAttributeId FRACTURE_RESISTANCE        = createInternal("fracture_resistance", 0.0);
    public static final IAttributeId FRACTURE_DELAY             = createInternal("fracture_delay", 240);
    public static final IAttributeId FRACTURE_BLOCK             = createInternal("fracture_block", 0);
    public static final IAttributeId BLEED_RESISTANCE           = createInternal("bleed_resistance", 0.0);
    public static final IAttributeId BLEED_DELAY                = createInternal("bleed_delay", 120);
    public static final IAttributeId BLEED_BLOCK                = createInternal("bleed_block", 0);
    public static final IAttributeId EXPLOSION_RESISTANCE       = createInternal("explosion_resistance", 0.0);
    public static final IAttributeId FALL_RESISTANCE            = createInternal("fall_resistance", 0.0);
    // global weapon attributes
    public static final IAttributeId MELEE_DAMAGE               = createInternal("melee_damage", 0); // bonus damage
    public static final IAttributeId MELEE_COOLDOWN             = createInternal("melee_cooldown");
    public static final IAttributeId BOW_DAMAGE                 = createInternal("bow_damage");
    public static final IAttributeId PISTOL_DAMAGE              = createInternal("pistol_damage");
    public static final IAttributeId SMG_DAMAGE                 = createInternal("smg_damage");
    public static final IAttributeId SHOTGUN_DAMAGE             = createInternal("shotgun_damage");
    public static final IAttributeId AR_DAMAGE                  = createInternal("ar_damage");
    public static final IAttributeId DMR_DAMAGE                 = createInternal("dmr_damage");
    public static final IAttributeId SR_DAMAGE                  = createInternal("sr_damage");
    public static final IAttributeId SILENT_WEAPON_DAMAGE       = createInternal("silent_weapon_damage");
    public static final IAttributeId LOUD_WEAPON_DAMAGE         = createInternal("loud_weapon_damage");
    public static final IAttributeId HEADSHOT_DAMAGE            = createInternal("headshot_damage");
    public static final IAttributeId RECOIL_CONTROL             = createInternal("recoil_control");
    public static final IAttributeId RELOAD_SPEED               = createInternal("reload_speed");
    public static final IAttributeId WEAPON_DURABILITY          = createInternal("weapon_durability");
    public static final IAttributeId JAM_CHANCE                 = createInternal("weapon_jamming");
    public static final IAttributeId WEAPON_NOISE               = createInternal("weapon_noise");
    public static final IAttributeId UNJAMMING_SPEED            = createInternal("unjamming_speed");
    public static final IAttributeId REPAIR_PENALTY             = createInternal("repair_penalty", 0.7);
    // general gameplay attributes
    public static final IAttributeId DAMAGE_TAKEN               = createInternal("damage_taken");
    public static final IAttributeId FALL_DAMAGE                = createInternal("fall_damage");
    public static final IAttributeId DIGGING_SPEED              = createInternal("digging_speed"); // shovel
    public static final IAttributeId MINING_SPEED               = createInternal("mining_speed"); // pickaxe
    public static final IAttributeId WOODCUTTING_SPEED          = createInternal("woodcutting_speed"); // axe
    public static final IAttributeId MOVEMENT_SPEED             = createInternal("movement_speed");
    public static final IAttributeId PERK_BOOK_CHANCE           = createInternal("perk_book_chance");
    public static final IAttributeId SKILL_BOOK_CHANCE          = createInternal("skill_book_chance");
    public static final IAttributeId INSTANT_KILL               = createInternal("instant_kill", 0.0);
    // skill attributes
    public static final IAttributeId LIKE_A_CAT_EFFECT          = createInternal("like_a_cat_effect");
    public static final IAttributeId AIRDROP_CALL_COOLDOWN      = createInternal("airdrop_call_cooldown");
    public static final IAttributeId SECOND_CHANCE_COOLDOWN     = createInternal("second_chance_cooldown");
    public static final IAttributeId WELL_FED_DURATION          = createInternal("well_fed_duration");
    public static final IAttributeId MOTHERLODE_BONUS           = createInternal("motherlode_bonus");
    public static final IAttributeId IRON_BUDDY_COOLDOWN        = createInternal("iron_buddy_cooldown");
    // weapon
    public static final IAttributeId M1911_RELOAD_SPEED         = createInternal("m1911_reload_speed", 35);
    public static final IAttributeId M1911_MAG_CAPACITY         = createInternal("m1911_mag_capacity", 7);
    public static final IAttributeId M1911_FIRERATE             = createInternal("m1911_firerate", 4);
    public static final IAttributeId M1911_HORIZONTAL_RECOIL    = createInternal("m1911_horizontal_recoil", 1.4);
    public static final IAttributeId M1911_VERTICAL_RECOIL      = createInternal("m1911_vertical_recoil", 3.1);
    public static final IAttributeId M1911_NOISE                = createInternal("m1911_noise");
    public static final IAttributeId UMP45_RELOAD_SPEED         = createInternal("ump45_reload_speed", 52);
    public static final IAttributeId UMP45_MAG_CAPACITY         = createInternal("ump45_mag_capacity", 25);
    public static final IAttributeId UMP45_VERTICAL_RECOIL      = createInternal("ump45_vertical_recoil", 1.8);
    public static final IAttributeId UMP45_FIRERATE             = createInternal("ump45_firerate", 3);
    public static final IAttributeId UMP45_NOISE                = createInternal("ump45_noise");
    public static final IAttributeId CROSSBOW_RELOAD_SPEED      = createInternal("crossbow_reload_speed", 60);
    public static final IAttributeId CROSSBOW_MAG_CAPACITY      = createInternal("crossbow_mag_capacity");
    public static final IAttributeId S1897_RELOAD_SPEED         = createInternal("s1897_reload_speed", 17);
    public static final IAttributeId S1897_MAG_CAPACITY         = createInternal("s1897_mag_capacity", 5);
    public static final IAttributeId S1897_FIRERATE             = createInternal("s1897_firerate", 25);
    public static final IAttributeId S1897_PELLET_SPREAD        = createInternal("s1897_pellet_spread");
    public static final IAttributeId SKS_FIRERATE               = createInternal("sks_firerate", 7);
    public static final IAttributeId SKS_VERTICAL_RECOIL        = createInternal("sks_vertical_recoil", 4.7);
    public static final IAttributeId SKS_HORIZONTAL_RECOIL      = createInternal("sks_horizontal_recoil", 1.3);
    public static final IAttributeId SKS_MAG_CAPACITY           = createInternal("sks_mag_capacity", 10);
    public static final IAttributeId SKS_NOISE                  = createInternal("sks_noise");
    public static final IAttributeId KAR98K_VERTICAL_RECOIL     = createInternal("kar98k_vertical_recoil", 8.0);
    public static final IAttributeId KAR98K_HORIZONTAL_RECOIL   = createInternal("kar98k_horizontal_recoil", 0.9);
    public static final IAttributeId KAR98K_MAG_CAPACITY        = createInternal("kar98k_mag_capacity", 5);
    public static final IAttributeId KAR98K_RELOAD_SPEED        = createInternal("kar98k_reload_speed", 30);
    public static final IAttributeId KAR98K_NOISE               = createInternal("kar98k_noise");
    public static final IAttributeId KAR98K_FIRERATE            = createInternal("kar98k_firerate", 40);
    public static final IAttributeId KAR98K_HS_DAMAGE           = createInternal("kar98k_hs_damage");

    // combined
    public static final ICombinedAttribute M1911_RELOAD         = ICombinedAttribute.of(RELOAD_SPEED, M1911_RELOAD_SPEED);
    public static final ICombinedAttribute M1911_VERTICAL       = ICombinedAttribute.of(RECOIL_CONTROL, M1911_VERTICAL_RECOIL);
    public static final ICombinedAttribute M1911_HORIZONTAL     = ICombinedAttribute.of(RECOIL_CONTROL, M1911_HORIZONTAL_RECOIL);
    public static final ICombinedAttribute M1911_LOUDNESS       = ICombinedAttribute.of(WEAPON_NOISE, M1911_NOISE);
    public static final ICombinedAttribute UMP45_VERTICAL       = ICombinedAttribute.of(RECOIL_CONTROL, UMP45_VERTICAL_RECOIL);
    public static final ICombinedAttribute UMP45_LOUDNESS       = ICombinedAttribute.of(WEAPON_NOISE, UMP45_NOISE);
    public static final ICombinedAttribute CROSSBOW_RELOAD      = ICombinedAttribute.of(RELOAD_SPEED, CROSSBOW_RELOAD_SPEED);
    public static final ICombinedAttribute S1897_RELOAD         = ICombinedAttribute.of(RELOAD_SPEED, S1897_RELOAD_SPEED);
    public static final ICombinedAttribute SKS_VERTICAL         = ICombinedAttribute.of(RECOIL_CONTROL, SKS_VERTICAL_RECOIL);
    public static final ICombinedAttribute SKS_HORIZONTAL       = ICombinedAttribute.of(RECOIL_CONTROL, SKS_HORIZONTAL_RECOIL);
    public static final ICombinedAttribute SKS_LOUDNESS         = ICombinedAttribute.of(WEAPON_NOISE, SKS_NOISE);
    public static final ICombinedAttribute KAR98K_RELOAD        = ICombinedAttribute.of(RELOAD_SPEED, KAR98K_RELOAD_SPEED);
    public static final ICombinedAttribute KAR98K_VERTICAL      = ICombinedAttribute.of(RECOIL_CONTROL, KAR98K_VERTICAL_RECOIL);
    public static final ICombinedAttribute KAR98K_HORIZONTAL    = ICombinedAttribute.of(RECOIL_CONTROL, KAR98K_HORIZONTAL_RECOIL);
    public static final ICombinedAttribute KAR98K_LOUDNESS      = ICombinedAttribute.of(WEAPON_NOISE, KAR98K_NOISE);
    public static final ICombinedAttribute KAR98K_HEADSHOT      = ICombinedAttribute.of(HEADSHOT_DAMAGE, KAR98K_HS_DAMAGE);

    public static IAttributeId find(ResourceLocation id) {
        return KEY_MAP.get(id);
    }

    public static Set<ResourceLocation> listKeys() {
        return Collections.unmodifiableSet(KEY_MAP.keySet());
    }

    public static <I extends IAttributeId> I registerId(I id) {
        KEY_MAP.put(id.getId(), id);
        return id;
    }

    private static AttributeId createInternal(String name) {
        return createInternal(name, 1.0D);
    }

    private static AttributeId createInternal(String name, double base) {
        return createInternal(name, base, Attribute::new);
    }

    private static AttributeId createInternal(String name, double base, AttributeId.IAttribFactory factory) {
        AttributeId id = AttributeId.create(GunsRPG.makeResource(name), base, factory);
        return registerId(id);
    }

    private Attribs() {}
}
