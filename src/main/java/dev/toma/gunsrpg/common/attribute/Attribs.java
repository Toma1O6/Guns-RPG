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
    public static final IAttributeId QUEST_AMMO                 = createInternal("quests_ammo");
    public static final IAttributeId QUEST_EXPLOSIVES           = createInternal("quests_explosives");
    public static final IAttributeId QUEST_VISIBLE_REWARD       = createInternal("quests_visible_reward", 3);
    public static final IAttributeId QUEST_MEDS                 = createInternal("quests_meds", 0);
    public static final IAttributeId QUEST_ORBS                 = createInternal("quests_orbs", 0);
    public static final IAttributeId QUEST_PERKBOOK             = createInternal("quests_perkbook", 0);
    public static final IAttributeId QUEST_FLARE                = createInternal("quests_flare", 0);
    // debuffs, healing
    public static final IAttributeId ADRENALINE_EFFECT          = createInternal("adrenaline_effect");
    public static final IAttributeId MORPHINE_EFFECT            = createInternal("morphine_effect");
    public static final IAttributeId STEROIDS_EFFECT            = createInternal("steroids_effect");
    public static final IAttributeId ANTIDOTE_EFFECT            = createInternal("antidote_effect", 40);
    public static final IAttributeId VACCINE_EFFECT             = createInternal("vaccine_effect", 50);
    public static final IAttributeId SPLINT_EFFECT              = createInternal("splint_effect", 35);
    public static final IAttributeId BANDAGE_EFFECT             = createInternal("bandage_effect", 25);
    public static final IAttributeId HEMOSTAT_EFFECT            = createInternal("hemostat_effect", 1200);
    public static final IAttributeId CALCIUM_SHOT_EFFECT        = createInternal("calcium_shot_effect", 1200);
    public static final IAttributeId VITAMINS_EFFECT            = createInternal("vitamins_effect", 1200);
    public static final IAttributeId PROPITAL_EFFECT            = createInternal("propital_effect", 1200);
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
    public static final IAttributeId FALL_RESISTANCE            = createInternal("fall_resistance");
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
    public static final IAttributeId HEAL_BOOST                 = createInternal("heal_boost", 0);
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
    public static final IAttributeId M1911_RELOAD_SPEED         = createInternal("m1911_reload_speed", 45);
    public static final IAttributeId M1911_MAG_CAPACITY         = createInternal("m1911_mag_capacity", 7);
    public static final IAttributeId M1911_FIRERATE             = createInternal("m1911_firerate", 4);
    public static final IAttributeId M1911_HORIZONTAL_RECOIL    = createInternal("m1911_horizontal_recoil", 1.4);
    public static final IAttributeId M1911_VERTICAL_RECOIL      = createInternal("m1911_vertical_recoil", 3.1);
    public static final IAttributeId M1911_NOISE                = createInternal("m1911_noise");
    public static final IAttributeId R45_FAST_HANDS             = createInternal("r45_fast_hands", 17);
    public static final IAttributeId R45_VERTICAL_RECOIL        = createInternal("r45_vertical_recoil", 4.5);
    public static final IAttributeId R45_HORIZONTAL_RECOIL      = createInternal("r45_horizontal_recoil", 1.8);
    public static final IAttributeId R45_MAG_CAPACITY           = createInternal("r45_mag_capacity", 6);
    public static final IAttributeId R45_FIRERATE               = createInternal("r45_firerate", 15);
    public static final IAttributeId R45_NOISE                  = createInternal("r45_noise");
    public static final IAttributeId DEAGLE_RELOADING           = createInternal("deagle_reloading", 60);
    public static final IAttributeId DEAGLE_FIRERATE            = createInternal("deagle_firerate", 9);
    public static final IAttributeId DEAGLE_HORIZONTAL_RECOIL   = createInternal("deagle_horizontal_recoil", 2.0);
    public static final IAttributeId DEAGLE_VERTICAL_RECOIL     = createInternal("deagle_vertical_recoil", 4.4);
    public static final IAttributeId DEAGLE_MAG_CAPACITY        = createInternal("deagle_mag_capacity", 7);
    public static final IAttributeId UMP45_RELOAD_SPEED         = createInternal("ump45_reload_speed", 60);
    public static final IAttributeId UMP45_MAG_CAPACITY         = createInternal("ump45_mag_capacity", 25);
    public static final IAttributeId UMP45_VERTICAL_RECOIL      = createInternal("ump45_vertical_recoil", 2.1);
    public static final IAttributeId UMP45_FIRERATE             = createInternal("ump45_firerate", 3);
    public static final IAttributeId UMP45_NOISE                = createInternal("ump45_noise");
    public static final IAttributeId THOMPSON_RELOAD_SPEED      = createInternal("thompson_reload_speed", 75);
    public static final IAttributeId THOMPSON_MAG_CAPACITY      = createInternal("thompson_mag_capacity", 30);
    public static final IAttributeId THOMPSON_VERTICAL_RECOIL   = createInternal("thompson_vertical_recoil", 2.6);
    public static final IAttributeId THOMPSON_FIRERATE          = createInternal("thompson_firerate", 3);
    public static final IAttributeId THOMPSON_NOISE             = createInternal("thompson_noise");
    public static final IAttributeId VECTOR_RELOAD_SPEED        = createInternal("vector_reload_speed", 70);
    public static final IAttributeId VECTOR_VERTICAL_RECOIL     = createInternal("vector_vertical_recoil", 1.7);
    public static final IAttributeId VECTOR_MAG_CAPACITY        = createInternal("vector_mag_capacity", 17);
    public static final IAttributeId VECTOR_LOUDNESS            = createInternal("vector_loudness");
    public static final IAttributeId CROSSBOW_RELOAD_SPEED      = createInternal("crossbow_reload_speed", 60);
    public static final IAttributeId CROSSBOW_MAG_CAPACITY      = createInternal("crossbow_mag_capacity");
    public static final IAttributeId CHUKONU_RELOAD_SPEED       = createInternal("chukonu_reload_speed", 28);
    public static final IAttributeId CHUKONU_MAG_CAPACITY       = createInternal("chukonu_mag_capacity", 6);
    public static final IAttributeId CHUKONU_FIRERATE           = createInternal("chukonu_firerate", 4);
    public static final IAttributeId S1897_RELOAD_SPEED         = createInternal("s1897_reload_speed", 22);
    public static final IAttributeId S1897_MAG_CAPACITY         = createInternal("s1897_mag_capacity", 5);
    public static final IAttributeId S1897_FIRERATE             = createInternal("s1897_firerate", 25);
    public static final IAttributeId S686_RELOAD_SPEED          = createInternal("s686_reload_speed", 70);
    public static final IAttributeId S12K_RELOAD_SPEED          = createInternal("s12k_reload_speed", 80);
    public static final IAttributeId S12K_MAG_CAPACITY          = createInternal("s12k_mag_capacity", 5);
    public static final IAttributeId S12K_VERTICAL_RECOIL       = createInternal("s12k_vertical_recoil", 4.2);
    public static final IAttributeId S12K_HORIZONTAL_RECOIL     = createInternal("s12k_horizontal_recoil", 2.2);
    public static final IAttributeId S12K_FIRERATE              = createInternal("s12k_firerate", 6);
    public static final IAttributeId S12K_LOUDNESS              = createInternal("s12k_loudness");
    public static final IAttributeId AKM_RELOAD_SPEED           = createInternal("akm_reload_speed", 75);
    public static final IAttributeId AKM_MAG_CAPACITY           = createInternal("akm_mag_capacity", 30);
    public static final IAttributeId AKM_LOUDNESS               = createInternal("akm_loudness");
    public static final IAttributeId AKM_FIRERATE               = createInternal("akm_firerate", 3);
    public static final IAttributeId HK416_RELOAD_SPEED         = createInternal("hk416_reload_speed", 70);
    public static final IAttributeId HK416_VERTICAL_RECOIL      = createInternal("hk416_vertical_recoil", 2.6);
    public static final IAttributeId HK416_HORIZONTAL_RECOIL    = createInternal("hk416_horizontal_recoil", 1.4);
    public static final IAttributeId HK416_MAG_CAPACITY         = createInternal("hk416_mag_capacity", 30);
    public static final IAttributeId HK416_LOUDNESS             = createInternal("hk416_loudness");
    public static final IAttributeId AUG_RELOAD_SPEED           = createInternal("aug_reload_speed", 75);
    public static final IAttributeId AUG_MAG_CAPACITY           = createInternal("aug_mag_capacity", 30);
    public static final IAttributeId AUG_VERTICAL_RECOIL        = createInternal("aug_vertical_recoil", 2.7);
    public static final IAttributeId AUG_HORIZONTAL_RECOIL      = createInternal("aug_horizontal_recoil", 0.6);
    public static final IAttributeId AUG_LOUDNESS               = createInternal("aug_loudness");
    public static final IAttributeId SKS_FIRERATE               = createInternal("sks_firerate", 7);
    public static final IAttributeId SKS_VERTICAL_RECOIL        = createInternal("sks_vertical_recoil", 4.7);
    public static final IAttributeId SKS_HORIZONTAL_RECOIL      = createInternal("sks_horizontal_recoil", 1.3);
    public static final IAttributeId SKS_MAG_CAPACITY           = createInternal("sks_mag_capacity", 10);
    public static final IAttributeId SKS_NOISE                  = createInternal("sks_noise");
    public static final IAttributeId VSS_RELOAD_SPEED           = createInternal("vss_reload_speed", 75);
    public static final IAttributeId VSS_MAG_CAPACITY           = createInternal("vss_mag_capacity", 10);
    public static final IAttributeId VSS_VERTICAL_RECOIL        = createInternal("vss_vertical_recoil", 3.6);
    public static final IAttributeId VSS_HORIZONTAL_RECOIL      = createInternal("vss_horizontal_recoil", 1.7);
    public static final IAttributeId VSS_FIRERATE               = createInternal("vss_firerate", 3);
    public static final IAttributeId MK14_RELOAD_SPEED          = createInternal("mk14_reload_speed", 75);
    public static final IAttributeId MK14_VERTICAL_RECOIL       = createInternal("mk14_vertical_recoil", 3.9);
    public static final IAttributeId MK14_HORIZONTAL_RECOIL     = createInternal("mk14_horizontal_recoil", 2.0);
    public static final IAttributeId MK14_MAG_CAPACITY          = createInternal("mk14_mag_capacity", 10);
    public static final IAttributeId MK14_FIRERATE              = createInternal("mk14_firerate", 3);
    public static final IAttributeId MK14_LOUDNESS              = createInternal("mk14_loudness");
    public static final IAttributeId MK14_HS_DAMAGE             = createInternal("mk14_hs_damage");
    public static final IAttributeId KAR98K_VERTICAL_RECOIL     = createInternal("kar98k_vertical_recoil", 8.0);
    public static final IAttributeId KAR98K_HORIZONTAL_RECOIL   = createInternal("kar98k_horizontal_recoil", 0.9);
    public static final IAttributeId KAR98K_MAG_CAPACITY        = createInternal("kar98k_mag_capacity", 5);
    public static final IAttributeId KAR98K_RELOAD_SPEED        = createInternal("kar98k_reload_speed", 30);
    public static final IAttributeId KAR98K_NOISE               = createInternal("kar98k_noise");
    public static final IAttributeId KAR98K_FIRERATE            = createInternal("kar98k_firerate", 40);
    public static final IAttributeId KAR98K_HS_DAMAGE           = createInternal("kar98k_hs_damage");
    public static final IAttributeId WINCHESTER_RELOAD_SPEED    = createInternal("winchester_reload_speed", 25);
    public static final IAttributeId WINCHESTER_MAG_CAPACITY    = createInternal("winchester_mag_capacity", 8);
    public static final IAttributeId WINCHESTER_FIRERATE        = createInternal("winchester_firerate", 25);
    public static final IAttributeId AWM_RELOAD_SPEED           = createInternal("awm_reload_speed", 100);
    public static final IAttributeId AWM_MAG_CAPACITY           = createInternal("awm_mag_capacity", 5);
    public static final IAttributeId AWM_VERTICAL_RECOIL        = createInternal("awm_vertical_recoil", 5.5f);
    public static final IAttributeId AWM_HORIZONTAL_RECOIL      = createInternal("awm_horizontal_recoil", 3.3f);
    public static final IAttributeId AWM_LOUDNESS               = createInternal("awm_loudness");
    public static final IAttributeId AWM_FIRERATE               = createInternal("awm_firerate", 36);
    public static final IAttributeId AWM_HS_DAMAGE              = createInternal("awm_hs_damage");
    public static final IAttributeId GL_RELOAD_SPEED            = createInternal("gl_reload_speed", 20);
    public static final IAttributeId GL_FIRERATE                = createInternal("gl_firerate", 15);
    public static final IAttributeId GL_MAG_CAPACITY            = createInternal("gl_mag_capacity", 4);
    public static final IAttributeId RL_RELOAD_SPEED            = createInternal("rl_reload_speed", 35);

    // combined
    public static final ICombinedAttribute M1911_RELOAD         = ICombinedAttribute.of(RELOAD_SPEED, M1911_RELOAD_SPEED);
    public static final ICombinedAttribute M1911_VERTICAL       = ICombinedAttribute.of(RECOIL_CONTROL, M1911_VERTICAL_RECOIL);
    public static final ICombinedAttribute M1911_HORIZONTAL     = ICombinedAttribute.of(RECOIL_CONTROL, M1911_HORIZONTAL_RECOIL);
    public static final ICombinedAttribute M1911_LOUDNESS       = ICombinedAttribute.of(WEAPON_NOISE, M1911_NOISE);
    public static final ICombinedAttribute R45_RELOAD           = ICombinedAttribute.of(RELOAD_SPEED, R45_FAST_HANDS);
    public static final ICombinedAttribute R45_VERTICAL         = ICombinedAttribute.of(RECOIL_CONTROL, R45_VERTICAL_RECOIL);
    public static final ICombinedAttribute R45_HORIZONTAL       = ICombinedAttribute.of(RECOIL_CONTROL, R45_HORIZONTAL_RECOIL);
    public static final ICombinedAttribute R45_LOUDNESS         = ICombinedAttribute.of(WEAPON_NOISE, R45_NOISE);
    public static final ICombinedAttribute DEAGLE_RELOAD        = ICombinedAttribute.of(RELOAD_SPEED, DEAGLE_RELOADING);
    public static final ICombinedAttribute DEAGLE_HORIZONTAL    = ICombinedAttribute.of(RECOIL_CONTROL, DEAGLE_HORIZONTAL_RECOIL);
    public static final ICombinedAttribute DEAGLE_VERTICAL      = ICombinedAttribute.of(RECOIL_CONTROL, DEAGLE_VERTICAL_RECOIL);
    public static final ICombinedAttribute UMP45_RELOAD         = ICombinedAttribute.of(RELOAD_SPEED, UMP45_RELOAD_SPEED);
    public static final ICombinedAttribute UMP45_VERTICAL       = ICombinedAttribute.of(RECOIL_CONTROL, UMP45_VERTICAL_RECOIL);
    public static final ICombinedAttribute UMP45_LOUDNESS       = ICombinedAttribute.of(WEAPON_NOISE, UMP45_NOISE);
    public static final ICombinedAttribute THOMPSON_RELOAD      = ICombinedAttribute.of(RELOAD_SPEED, THOMPSON_RELOAD_SPEED);
    public static final ICombinedAttribute THOMPSON_VERTICAL    = ICombinedAttribute.of(RECOIL_CONTROL, THOMPSON_VERTICAL_RECOIL);
    public static final ICombinedAttribute THOMPSON_LOUDNESS    = ICombinedAttribute.of(WEAPON_NOISE, THOMPSON_NOISE);
    public static final ICombinedAttribute VECTOR_RELOAD        = ICombinedAttribute.of(RELOAD_SPEED, VECTOR_RELOAD_SPEED);
    public static final ICombinedAttribute VECTOR_VERTICAL      = ICombinedAttribute.of(RECOIL_CONTROL, VECTOR_VERTICAL_RECOIL);
    public static final ICombinedAttribute VECTOR_NOISE         = ICombinedAttribute.of(WEAPON_NOISE, VECTOR_LOUDNESS);
    public static final ICombinedAttribute CROSSBOW_RELOAD      = ICombinedAttribute.of(RELOAD_SPEED, CROSSBOW_RELOAD_SPEED);
    public static final ICombinedAttribute CHUKONU_RELOAD       = ICombinedAttribute.of(RELOAD_SPEED, CHUKONU_RELOAD_SPEED);
    public static final ICombinedAttribute S1897_RELOAD         = ICombinedAttribute.of(RELOAD_SPEED, S1897_RELOAD_SPEED);
    public static final ICombinedAttribute S686_RELOAD          = ICombinedAttribute.of(RELOAD_SPEED, S686_RELOAD_SPEED);
    public static final ICombinedAttribute S12K_RELOAD          = ICombinedAttribute.of(RELOAD_SPEED, S12K_RELOAD_SPEED);
    public static final ICombinedAttribute S12K_VERTICAL        = ICombinedAttribute.of(RECOIL_CONTROL, S12K_VERTICAL_RECOIL);
    public static final ICombinedAttribute S12K_HORIZONTAL      = ICombinedAttribute.of(RECOIL_CONTROL, S12K_HORIZONTAL_RECOIL);
    public static final ICombinedAttribute S12K_NOISE           = ICombinedAttribute.of(WEAPON_NOISE, S12K_LOUDNESS);
    public static final ICombinedAttribute AKM_RELOAD           = ICombinedAttribute.of(RELOAD_SPEED, AKM_RELOAD_SPEED);
    public static final ICombinedAttribute AKM_NOISE            = ICombinedAttribute.of(WEAPON_NOISE, AKM_LOUDNESS);
    public static final ICombinedAttribute HK416_RELOAD         = ICombinedAttribute.of(RELOAD_SPEED, HK416_RELOAD_SPEED);
    public static final ICombinedAttribute HK416_VERTICAL       = ICombinedAttribute.of(RECOIL_CONTROL, HK416_VERTICAL_RECOIL);
    public static final ICombinedAttribute HK416_HORIZONTAL     = ICombinedAttribute.of(RECOIL_CONTROL, HK416_HORIZONTAL_RECOIL);
    public static final ICombinedAttribute HK416_NOISE          = ICombinedAttribute.of(WEAPON_NOISE, HK416_LOUDNESS);
    public static final ICombinedAttribute AUG_RELOAD           = ICombinedAttribute.of(RELOAD_SPEED, AUG_RELOAD_SPEED);
    public static final ICombinedAttribute AUG_VERTICAL         = ICombinedAttribute.of(RECOIL_CONTROL, AUG_VERTICAL_RECOIL);
    public static final ICombinedAttribute AUG_HORIZONTAL       = ICombinedAttribute.of(RECOIL_CONTROL, AUG_HORIZONTAL_RECOIL);
    public static final ICombinedAttribute AUG_NOISE            = ICombinedAttribute.of(WEAPON_NOISE, AUG_LOUDNESS);
    public static final ICombinedAttribute SKS_VERTICAL         = ICombinedAttribute.of(RECOIL_CONTROL, SKS_VERTICAL_RECOIL);
    public static final ICombinedAttribute SKS_HORIZONTAL       = ICombinedAttribute.of(RECOIL_CONTROL, SKS_HORIZONTAL_RECOIL);
    public static final ICombinedAttribute SKS_LOUDNESS         = ICombinedAttribute.of(WEAPON_NOISE, SKS_NOISE);
    public static final ICombinedAttribute VSS_RELOAD           = ICombinedAttribute.of(RELOAD_SPEED, VSS_RELOAD_SPEED);
    public static final ICombinedAttribute VSS_VERTICAL         = ICombinedAttribute.of(RECOIL_CONTROL, VSS_VERTICAL_RECOIL);
    public static final ICombinedAttribute VSS_HORIZONTAL       = ICombinedAttribute.of(RECOIL_CONTROL, VSS_HORIZONTAL_RECOIL);
    public static final ICombinedAttribute MK14_RELOAD          = ICombinedAttribute.of(RELOAD_SPEED, MK14_RELOAD_SPEED);
    public static final ICombinedAttribute MK14_VERTICAL        = ICombinedAttribute.of(RECOIL_CONTROL, MK14_VERTICAL_RECOIL);
    public static final ICombinedAttribute MK14_HORIZONTAL      = ICombinedAttribute.of(RECOIL_CONTROL, MK14_HORIZONTAL_RECOIL);
    public static final ICombinedAttribute MK14_NOISE           = ICombinedAttribute.of(WEAPON_NOISE, MK14_LOUDNESS);
    public static final ICombinedAttribute MK14_HEADSHOT        = ICombinedAttribute.of(HEADSHOT_DAMAGE, MK14_HS_DAMAGE);
    public static final ICombinedAttribute KAR98K_RELOAD        = ICombinedAttribute.of(RELOAD_SPEED, KAR98K_RELOAD_SPEED);
    public static final ICombinedAttribute KAR98K_VERTICAL      = ICombinedAttribute.of(RECOIL_CONTROL, KAR98K_VERTICAL_RECOIL);
    public static final ICombinedAttribute KAR98K_HORIZONTAL    = ICombinedAttribute.of(RECOIL_CONTROL, KAR98K_HORIZONTAL_RECOIL);
    public static final ICombinedAttribute KAR98K_LOUDNESS      = ICombinedAttribute.of(WEAPON_NOISE, KAR98K_NOISE);
    public static final ICombinedAttribute KAR98K_HEADSHOT      = ICombinedAttribute.of(HEADSHOT_DAMAGE, KAR98K_HS_DAMAGE);
    public static final ICombinedAttribute WINCHESTER_RELOAD    = ICombinedAttribute.of(RELOAD_SPEED, WINCHESTER_RELOAD_SPEED);
    public static final ICombinedAttribute AWM_RELOAD           = ICombinedAttribute.of(RELOAD_SPEED, AWM_RELOAD_SPEED);
    public static final ICombinedAttribute AWM_VERTICAL         = ICombinedAttribute.of(RECOIL_CONTROL, AWM_VERTICAL_RECOIL);
    public static final ICombinedAttribute AWM_HORIZONTAL       = ICombinedAttribute.of(RECOIL_CONTROL, AWM_HORIZONTAL_RECOIL);
    public static final ICombinedAttribute AWM_NOISE            = ICombinedAttribute.of(WEAPON_NOISE, AWM_LOUDNESS);
    public static final ICombinedAttribute AWM_HEADSHOT         = ICombinedAttribute.of(HEADSHOT_DAMAGE, AWM_HS_DAMAGE);
    public static final ICombinedAttribute GL_RELOAD            = ICombinedAttribute.of(RELOAD_SPEED, GL_RELOAD_SPEED);
    public static final ICombinedAttribute RL_RELOAD            = ICombinedAttribute.of(RELOAD_SPEED, RL_RELOAD_SPEED);

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
