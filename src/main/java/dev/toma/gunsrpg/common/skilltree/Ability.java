package dev.toma.gunsrpg.common.skilltree;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.math.Vec2Di;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Ability {

    public static final List<Type> LEVEL_REWARD_TYPES = new ArrayList<>();
    public static final List<UnlockableType> UNLOCKABLE_ABILITY_TYPES = new ArrayList<>();
    public static final Type PISTOL_WOOD_AMMO = registerStatic("pistol_wood_ammo");
    public static final Type PISTOL_STONE_AMMO = registerStatic("pistol_stone_ammo");
    public static final Type PISTOL_IRON_AMMO = registerStatic("pistol_iron_ammo");
    public static final Type PISTOL_GOLD_AMMO = registerStatic("pistol_gold_ammo");
    public static final Type PISTOL_DIAMOND_AMMO = registerStatic("pistol_diamond_ammo");
    public static final Type PISTOL_EMERALD_AMMO = registerStatic("pistol_emerald_ammo");
    public static final Type PISTOL_AMETHYST_AMMO = registerStatic("pistol_amethyst_ammo");
    public static final Type SMG_WOOD_AMMO = registerStatic("smg_wood_ammo");
    public static final Type SMG_STONE_AMMO = registerStatic("smg_stone_ammo");
    public static final Type SMG_IRON_AMMO = registerStatic("smg_iron_ammo");
    public static final Type SMG_GOLD_AMMO = registerStatic("smg_gold_ammo");
    public static final Type SMG_DIAMOND_AMMO = registerStatic("smg_diamond_ammo");
    public static final Type SMG_EMERALD_AMMO = registerStatic("smg_emerald_ammo");
    public static final Type SMG_AMETHYST_AMMO = registerStatic("smg_amethyst_ammo");
    public static final Type AR_WOOD_AMMO = registerStatic("ar_wood_ammo");
    public static final Type AR_STONE_AMMO = registerStatic("ar_stone_ammo");
    public static final Type AR_IRON_AMMO = registerStatic("ar_iron_ammo");
    public static final Type AR_GOLD_AMMO = registerStatic("ar_gold_ammo");
    public static final Type AR_DIAMOND_AMMO = registerStatic("ar_diamond_ammo");
    public static final Type AR_EMERALD_AMMO = registerStatic("ar_emerald_ammo");
    public static final Type AR_AMETHYST_AMMO = registerStatic("ar_amethyst_ammo");
    public static final Type SR_WOOD_AMMO = registerStatic("sr_wood_ammo");
    public static final Type SR_STONE_AMMO = registerStatic("sr_stone_ammo");
    public static final Type SR_IRON_AMMO = registerStatic("sr_iron_ammo");
    public static final Type SR_GOLD_AMMO = registerStatic("sr_gold_ammo");
    public static final Type SR_DIAMOND_AMMO = registerStatic("sr_diamond_ammo");
    public static final Type SR_EMERALD_AMMO = registerStatic("sr_emerald_ammo");
    public static final Type SR_AMETHYST_AMMO = registerStatic("sr_amethyst_ammo");
    public static final Type SG_WOOD_AMMO = registerStatic("sg_wood_ammo");
    public static final Type SG_STONE_AMMO = registerStatic("sg_stone_ammo");
    public static final Type SG_IRON_AMMO = registerStatic("sg_iron_ammo");
    public static final Type SG_GOLD_AMMO = registerStatic("sg_gold_ammo");
    public static final Type SG_DIAMOND_AMMO = registerStatic("sg_diamond_ammo");
    public static final Type SG_EMERALD_AMMO = registerStatic("sg_emerald_ammo");
    public static final Type SG_AMETHYST_AMMO = registerStatic("sg_amethyst_ammo");
    public static final UnlockableType PISTOL_QUICKDRAW = registerUnlockable("pistol_quickdraw", "Quickdraw", "30% faster pistol reload speed").requires(list -> needsLevel("pistol_1", list)).save();
    public static final UnlockableType PISTOL_EXTENDED = registerUnlockable("pistol_extended", "Extended Mag", "Increase pistol magazine kapacity to 13").requires(list -> needsLevel("pistol_1", list)).save();
    public static final UnlockableType PISTOL_TOUGH_SPRING = registerUnlockable("pistol_tough_spring", "Tought Spring", "Increases pistol firerate").requires(list -> needsLevel("pistol_5", list)).save();
    public static final UnlockableType PISTOL_CARBON_BARREL = registerUnlockable("pistol_carbon_barrel", "Carbon Barrel", "Decreases recoil").requires(list -> needsLevel("pistol_5", list)).save();
    public static final UnlockableType PISTOL_SUPPRESSOR = registerUnlockable("pistol_suppressor", "Suppressor", "Mobs won't be able to hear you").requires(list -> needsLevel("pistol_5", list)).save();
    public static final UnlockableType PISTOL_HEAVY_BULLETS = registerUnlockable("pistol_heavy_bullets", "Heavy Bullets", "Can slow down mobs on hit").requires(list -> needsLevel("pistol_5", list)).save();
    public static final UnlockableType DUAL_WIELD = registerUnlockable("dual_wield", "Dual Wielding", "Allows you to carry pistol in both hands").requires(list -> needsLevel("pistol_8", list)).save();

    public boolean enabled;
    public final Type type;

    public Ability(Type type) {
        this(type, false);
    }

    public Ability(Type type, boolean enabled) {
        this.type = type;
        this.enabled = enabled;
    }

    public void setAbilityEnabled(boolean enable) {
        this.enabled = enable;
    }

    public void toggle() {
        if(type.canDisable) {
            enabled = !enabled;
        }
    }

    public static Type findProperty(String key) {
        for(Type type : LEVEL_REWARD_TYPES) {
            if(type.name.equals(key)) {
                return type;
            }
        }
        return null;
    }

    public static UnlockableType findSkill(String key) {
        for(UnlockableType type : UNLOCKABLE_ABILITY_TYPES) {
            if(type.name.equals(key)) {
                return type;
            }
        }
        return null;
    }

    private static UnlockableBuilder registerUnlockable(String name, String key, String... lines) {
        return new UnlockableBuilder(name, key, lines);
    }

    private static Type registerStatic(String name) {
        Type type = new Type(name, false, null, null);
        LEVEL_REWARD_TYPES.add(type);
        return type;
    }

    private static boolean needsLevel(String treeEntryName, List<EntryInstance> list) {
        return ModUtils.contains(treeEntryName, list, (s, i) -> i.getType().registryName.toString().equals(s));
    }

    private static ResourceLocation getIcon(String name) {
        return new ResourceLocation(GunsRPG.MODID, String.format("textures/icons/%s.png", name));
    }

    public static class Type {

        public String name;
        @Nullable public String title;
        public String[] lines;
        @Nullable public ResourceLocation icon;
        public boolean canDisable;

        public Type(String name, boolean canDisable, @Nullable ResourceLocation icon, @Nullable String key, String... lines) {
            this.name = name;
            this.canDisable = canDisable;
            this.title = key;
            this.icon = icon;
            this.lines = lines;
        }

        public Ability newAbilityInstance() {
            return new Ability(this);
        }
    }

    public static class UnlockableType extends Type {

        public final int price;
        public final Predicate<List<EntryInstance>> levelRequirement;
        public final Vec2Di skillTreeVec;

        public UnlockableType(UnlockableBuilder builder) {
            super(builder.name, true, new ResourceLocation(GunsRPG.MODID, builder.name), builder.key, builder.lines);
            this.levelRequirement = builder.predicate;
            this.skillTreeVec = builder.location;
            this.price = builder.price;
            UNLOCKABLE_ABILITY_TYPES.add(this);
        }
    }

    private static class UnlockableBuilder {
        private String name;
        private String key;
        private String[] lines;
        private Predicate<List<EntryInstance>> predicate = l -> true;
        private Vec2Di location;
        private int price;

        private UnlockableBuilder(String name, String key, String... lines) {
            this.name = name;
            this.key = key;
            this.lines = lines;
        }

        private UnlockableBuilder requires(Predicate<List<EntryInstance>> predicate) {
            this.predicate = predicate;
            return this;
        }

        private UnlockableBuilder pos(int x, int y) {
            this.location = new Vec2Di(x, y);
            return this;
        }

        private UnlockableBuilder costs(int i) {
            this.price = i;
            return this;
        }

        private UnlockableType save() {
            return new UnlockableType(this);
        }
    }
}
