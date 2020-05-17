package dev.toma.gunsrpg.common.skilltree;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.ModRegistry;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.HashMap;
import java.util.Map;

public class SkillTreeEntry {

    public SkillTreeEntry[] child = new SkillTreeEntry[0];
    public ResourceLocation registryName;
    public GunItem gun;
    public int requiredKillCount;

    public Item iconItem;
    public final ITextComponent displayName;
    public final ITextComponent description;

    private SkillTreeEntry(String key) {
        this.registryName = GunsRPG.makeResource(key);
        SkillTreeEntry v = List.SKILLS.put(registryName, this);
        if(v != null) throw new IllegalStateException("Registering duplicate skill tree entry!");
        this.displayName = new TextComponentTranslation(String.format("tree.%s.title", key));
        this.description = new TextComponentTranslation(String.format("tree.%s.description", key));
    }

    public SkillTreeEntry setGun(GunItem item) {
        if(gun != null) return this;
        this.gun = item;
        for(SkillTreeEntry child : child) {
            child.setGun(item);
        }
        return this;
    }

    public SkillTreeEntry setChilds(SkillTreeEntry... entries) {
        this.child = entries;
        return this;
    }

    public SkillTreeEntry requires(int kills) {
        this.requiredKillCount = kills;
        return this;
    }

    public SkillTreeEntry icon(Item item) {
        this.iconItem = item;
        return this;
    }

    public EntryInstance makeInstance() {
        return new EntryInstance(this);
    }

    public static class List {

        protected static Map<ResourceLocation, SkillTreeEntry> SKILLS = new HashMap<>();
        public static SkillTreeEntry root;

        public static void init() {
            SkillTreeEntry sg8 = new SkillTreeEntry("sg_8").requires(1024).icon(Items.EXPERIENCE_BOTTLE);
            SkillTreeEntry sg7 = new SkillTreeEntry("sg_7").setChilds(sg8).requires(512).icon(ModRegistry.GRPGItems.AMETHYST_AMMO_12G);
            SkillTreeEntry sg6 = new SkillTreeEntry("sg_6").setChilds(sg7).requires(256).icon(ModRegistry.GRPGItems.EMERALD_AMMO_12G);
            SkillTreeEntry sg5 = new SkillTreeEntry("sg_5").setChilds(sg6).requires(128).icon(ModRegistry.GRPGItems.DIAMOND_AMMO_12G);
            SkillTreeEntry sg4 = new SkillTreeEntry("sg_4").setChilds(sg5).requires(64).icon(ModRegistry.GRPGItems.GOLD_AMMO_12G);
            SkillTreeEntry sg3 = new SkillTreeEntry("sg_3").setChilds(sg4).requires(32).icon(ModRegistry.GRPGItems.IRON_AMMO_12G);
            SkillTreeEntry sg2 = new SkillTreeEntry("sg_2").setChilds(sg3).requires(16).icon(ModRegistry.GRPGItems.STONE_AMMO_12G);
            SkillTreeEntry sg1 = new SkillTreeEntry("sg_1").setChilds(sg2).icon(ModRegistry.GRPGItems.WOODEN_AMMO_12G);
            SkillTreeEntry sr8 = new SkillTreeEntry("sr_8").requires(1024).icon(Items.EXPERIENCE_BOTTLE);
            SkillTreeEntry sr7 = new SkillTreeEntry("sr_7").setChilds(sr8).requires(512).icon(ModRegistry.GRPGItems.AMETHYST_AMMO_762MM);
            SkillTreeEntry sr6 = new SkillTreeEntry("sr_6").setChilds(sr7).requires(256).icon(ModRegistry.GRPGItems.EMERALD_AMMO_762MM);
            SkillTreeEntry sr5 = new SkillTreeEntry("sr_5").setChilds(sr6).requires(128).icon(ModRegistry.GRPGItems.DIAMOND_AMMO_762MM);
            SkillTreeEntry sr4 = new SkillTreeEntry("sr_4").setChilds(sr5, sg1).requires(64).icon(ModRegistry.GRPGItems.SHOTGUN);
            SkillTreeEntry sr3 = new SkillTreeEntry("sr_3").setChilds(sr4).requires(32).icon(ModRegistry.GRPGItems.IRON_AMMO_762MM);
            SkillTreeEntry sr2 = new SkillTreeEntry("sr_2").setChilds(sr3).requires(16).icon(ModRegistry.GRPGItems.STONE_AMMO_762MM);
            SkillTreeEntry sr1 = new SkillTreeEntry("sr_1").setChilds(sr2).setGun(ModRegistry.GRPGItems.SNIPER_RIFLE).icon(ModRegistry.GRPGItems.WOODEN_AMMO_762MM);
            SkillTreeEntry ar8 = new SkillTreeEntry("ar_8").requires(1024).icon(Items.EXPERIENCE_BOTTLE);
            SkillTreeEntry ar7 = new SkillTreeEntry("ar_7").setChilds(ar8).requires(512).icon(ModRegistry.GRPGItems.AMETHYST_AMMO_556MM);
            SkillTreeEntry ar6 = new SkillTreeEntry("ar_6").setChilds(ar7).requires(256).icon(ModRegistry.GRPGItems.EMERALD_AMMO_556MM);
            SkillTreeEntry ar5 = new SkillTreeEntry("ar_5").setChilds(ar6).requires(128).icon(ModRegistry.GRPGItems.DIAMOND_AMMO_556MM);
            SkillTreeEntry ar4 = new SkillTreeEntry("ar_4").setChilds(ar5, sr1).requires(64).icon(ModRegistry.GRPGItems.SNIPER_RIFLE);
            SkillTreeEntry ar3 = new SkillTreeEntry("ar_3").setChilds(ar4).requires(32).icon(ModRegistry.GRPGItems.IRON_AMMO_556MM);
            SkillTreeEntry ar2 = new SkillTreeEntry("ar_2").setChilds(ar3).requires(16).icon(ModRegistry.GRPGItems.STONE_AMMO_556MM);
            SkillTreeEntry ar1 = new SkillTreeEntry("ar_1").setChilds(ar2).setGun(ModRegistry.GRPGItems.ASSAULT_RIFLE).icon(ModRegistry.GRPGItems.WOODEN_AMMO_556MM);
            SkillTreeEntry smg8 = new SkillTreeEntry("smg_8").requires(1024).icon(Items.EXPERIENCE_BOTTLE);
            SkillTreeEntry smg7 = new SkillTreeEntry("smg_7").setChilds(smg8).requires(512).icon(ModRegistry.GRPGItems.AMETHYST_AMMO_45ACP);
            SkillTreeEntry smg6 = new SkillTreeEntry("smg_6").setChilds(smg7).requires(256).icon(ModRegistry.GRPGItems.EMERALD_AMMO_45ACP);
            SkillTreeEntry smg5 = new SkillTreeEntry("smg_5").setChilds(smg6).requires(128).icon(ModRegistry.GRPGItems.DIAMOND_AMMO_45ACP);
            SkillTreeEntry smg4 = new SkillTreeEntry("smg_4").setChilds(smg5, ar1).requires(64).icon(ModRegistry.GRPGItems.ASSAULT_RIFLE);
            SkillTreeEntry smg3 = new SkillTreeEntry("smg_3").setChilds(smg4).requires(32).icon(ModRegistry.GRPGItems.IRON_AMMO_45ACP);
            SkillTreeEntry smg2 = new SkillTreeEntry("smg_2").setChilds(smg3).requires(16).icon(ModRegistry.GRPGItems.STONE_AMMO_45ACP);
            SkillTreeEntry smg1 = new SkillTreeEntry("smg_1").setChilds(smg2).setGun(ModRegistry.GRPGItems.SMG).icon(ModRegistry.GRPGItems.WOODEN_AMMO_45ACP);
            SkillTreeEntry pistol8 = new SkillTreeEntry("pistol_8").requires(1024).icon(Items.EXPERIENCE_BOTTLE);
            SkillTreeEntry pistol7 = new SkillTreeEntry("pistol_7").setChilds(pistol8).requires(512).icon(ModRegistry.GRPGItems.AMETHYST_AMMO_9MM);
            SkillTreeEntry pistol6 = new SkillTreeEntry("pistol_6").setChilds(pistol7).requires(256).icon(ModRegistry.GRPGItems.EMERALD_AMMO_9MM);
            SkillTreeEntry pistol5 = new SkillTreeEntry("pistol_5").setChilds(pistol6).requires(128).icon(ModRegistry.GRPGItems.DIAMOND_AMMO_9MM);
            SkillTreeEntry pistol4 = new SkillTreeEntry("pistol_4").setChilds(pistol5, smg1).requires(64).icon(ModRegistry.GRPGItems.SMG);
            SkillTreeEntry pistol3 = new SkillTreeEntry("pistol_3").setChilds(pistol4).requires(32).icon(ModRegistry.GRPGItems.IRON_AMMO_9MM);
            SkillTreeEntry pistol2 = new SkillTreeEntry("pistol_2").setChilds(pistol3).requires(16).icon(ModRegistry.GRPGItems.STONE_AMMO_9MM);
            root = new SkillTreeEntry("pistol_1").setChilds(pistol2).setGun(ModRegistry.GRPGItems.PISTOL).icon(ModRegistry.GRPGItems.WOODEN_AMMO_9MM);
        }
    }
}
