package dev.toma.gunsrpg.common.skilltree;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.math.Vec2Di;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static dev.toma.gunsrpg.common.ModRegistry.GRPGItems.*;

@Deprecated
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
    public static final Type CROSSBOW_WOOD_AMMO = registerStatic("crossbow_wood_ammo");
    public static final Type CROSSBOW_STONE_AMMO = registerStatic("crossbow_stone_ammo");
    public static final Type CROSSBOW_IRON_AMMO = registerStatic("crossbow_iron_ammo");
    public static final Type CROSSBOW_GOLD_AMMO = registerStatic("crossbow_gold_ammo");
    public static final Type CROSSBOW_DIAMOND_AMMO = registerStatic("crossbow_diamond_ammo");
    public static final Type CROSSBOW_EMERALD_AMMO = registerStatic("crossbow_emerald_ammo");
    public static final Type CROSSBOW_AMETHYST_AMMO = registerStatic("crossbow_amethyst_ammo");
    // PISTOL
    public static final UnlockableType PISTOL_QUICKDRAW = registerDynamic("pistol_quickdraw", "Quickdraw", "30% faster pistol reload speed").icon("quickdraw").pos(124, 30).requires(list -> needsLevel("pistol_1", list)).gun(() -> PISTOL).save();
    public static final UnlockableType PISTOL_EXTENDED = registerDynamic("pistol_extended", "Extended Mag", "Increase pistol magazine capacity to 13").icon("extended").pos(164, 30).requires(list -> needsLevel("pistol_1", list)).gun(() -> PISTOL).save();
    public static final UnlockableType PISTOL_TOUGH_SPRING = registerDynamic("pistol_tough_spring", "Tough Spring", "Increases pistol firerate").icon("tough_spring").pos(204, 30).requires(list -> needsLevel("pistol_5", list)).gun(() -> PISTOL).save();
    public static final UnlockableType PISTOL_CARBON_BARREL = registerDynamic("pistol_carbon_barrel", "Carbon Barrel", "Decreases recoil").icon("carbon_barrel").pos(244, 30).requires(list -> needsLevel("pistol_5", list)).gun(() -> PISTOL).save();
    public static final UnlockableType PISTOL_SUPPRESSOR = registerDynamic("pistol_suppressor", "Suppressor", "Adds suppressor on your weapon").icon("suppressor").pos(284, 30).requires(list -> needsLevel("pistol_5", list)).gun(() -> PISTOL).save();
    public static final UnlockableType PISTOL_HEAVY_BULLETS = registerDynamic("heavy_bullets", "Heavy Bullets", "Can slow down mobs on hit").pos(324, 30).requires(list -> needsLevel("pistol_5", list)).gun(() -> PISTOL).save();
    public static final UnlockableType DUAL_WIELD = registerDynamic("dual_wield", "Dual Wielding", "Allows you to carry pistol in both hands").pos(364, 30).requires(list -> needsLevel("pistol_8", list)).gun(() -> PISTOL).save();
    // SMG
    public static final UnlockableType SMG_QUICKDRAW = registerDynamic("smg_quickdraw", "Quickdraw", "20% faster SMG reload speed").icon("quickdraw").pos(376, 222).requires(list -> needsLevel("smg_1", list)).gun(() -> SMG).save();
    public static final UnlockableType SMG_EXTENDED = registerDynamic("smg_extended", "Extended Mag", "Increases SMG magaine capacity to 30").icon("extended").pos(416, 222).requires(list -> needsLevel("smg_1", list)).gun(() -> SMG).save();
    public static final UnlockableType SMG_VERTICAL_GRIP = registerDynamic("smg_vertical_grip", "Vertical Grip", "Decreases vertical recoil").icon("vertical_grip").pos(456, 222).requires(list -> needsLevel("smg_1", list)).gun(() -> SMG).save();
    public static final UnlockableType SMG_TOUGH_SPRING = registerDynamic("smg_tough_spring", "Tough Spring", "Increases firerate").icon("tough_spring").pos(496, 222).requires(list -> needsLevel("smg_5", list)).gun(() -> SMG).save();
    public static final UnlockableType SMG_RED_DOT = registerDynamic("smg_red_dot", "Red Dot Sight", "Adds red dot sight on your weapon").icon("red_dot").pos(536, 222).requires(list -> needsLevel("smg_5", list)).gun(() -> SMG).save();
    public static final UnlockableType SMG_SUPPRESSOR = registerDynamic("smg_suppressor", "Suppressor", "Adds suppressor on your weapon").icon("suppressor").pos(576, 222).requires(list -> needsLevel("smg_5", list)).gun(() -> SMG).save();
    public static final UnlockableType COMMANDO = registerDynamic("commando", "Commando", "Grants following buffs after each kill:", "> 5s speed boost", "> 3s regeneration II").pos(616, 222).requires(list -> needsLevel("smg_8", list)).gun(() -> SMG).save();
    //CROSSBOW (628, 414)
    public static final UnlockableType QUIVER = registerDynamic("crossbow_quiver", "Quiver", "35% faster reload time").pos(628, 414).requires(list -> needsLevel("crossbow_1", list)).gun(() -> CROSSBOW).save();
    public static final UnlockableType POISONED_BOLTS = registerDynamic("poisoned_bolts", "Poisoned Bolt", "Poisons everything it hits!").pos(668, 414).requires(list -> needsLevel("crossbow_1", list)).gun(() -> CROSSBOW).save();
    public static final UnlockableType HUNTER = registerDynamic("hunter", "Hunter", "+4HP on kill", "Grants 2x more loot").pos(708, 414).requires(list -> needsLevel("crossbow_1", list)).gun(() -> CROSSBOW).save();
    public static final UnlockableType TOUGH_BOWSTRING = registerDynamic("tough_bowstring", "Tough Bowstring", "Increases bolt velocity").pos(748, 414).requires(list -> needsLevel("crossbow_5", list)).gun(() -> CROSSBOW).save();
    public static final UnlockableType CROSSBOW_PENETRATOR = registerDynamic("crossbow_penetrator", "Penetrator", "Arrows can penetrate multiple enemies").icon("penetrator").pos(788, 414).requires(list -> needsLevel("crossbow_5", list)).gun(() -> CROSSBOW).save();
    public static final UnlockableType CROSSBOW_SCOPE = registerDynamic("crossbow_scope", "Scope", "Adds optic with 4x magnification").pos(828, 414).icon("scope").requires(list -> needsLevel("crossbow_5", list)).gun(() -> CROSSBOW).save();
    public static final UnlockableType CROSSBOW_REPEATER = registerDynamic("crossbow_repeater", "Repeater", "+2 bolt reload", "§c-25% reloading speed").pos(868, 414).requires(list -> needsLevel("crossbow_8", list)).gun(() -> CROSSBOW).save();
    //SHOTGUN (880, 606)
    public static final UnlockableType BULLET_LOOPS = registerDynamic("bullet_loops", "Bullet Loops", "Increases reload speed by 20%").pos(880, 606).requires(list -> needsLevel("sg_1", list)).gun(() -> SHOTGUN).save();
    public static final UnlockableType SG_EXTENDED = registerDynamic("sg_extended", "Extended Mag", "Increases magazine capacity to 8").icon("extended").pos(920, 606).requires(list -> needsLevel("sg_1", list)).gun(() -> SHOTGUN).save();
    public static final UnlockableType PUMP_IN_ACTION = registerDynamic("pump_in_action", "Pump In Action", "Increases firerate").pos(960, 606).requires(list -> needsLevel("sg_5", list)).gun(() -> SHOTGUN).save();
    public static final UnlockableType CHOKE = registerDynamic("choke", "Choke", "Decreases pellet spread").pos(1000, 606).requires(list -> needsLevel("sg_5", list)).gun(() -> SHOTGUN).save();
    public static final UnlockableType NEVER_GIVE_UP = registerDynamic("never_give_up", "Never Give Up", "Grants following buffs after kill:", "5s of Resistance I").pos(1040, 606).requires(list -> needsLevel("sg_5", list)).gun(() -> SHOTGUN).save();
    public static final UnlockableType EXTENDED_BARREL = registerDynamic("extended_barrel", "Extended Barrel", "Increases pellet velocity").pos(1080, 606).requires(list -> needsLevel("sg_8", list)).gun(() -> SHOTGUN).save();
    // AR (1132, 798)
    public static final UnlockableType AR_TOUGH_SPRING = registerDynamic("ar_tough_spring", "Tough Spring", "Increases firerate").icon("tough_spring").pos(1132, 798).requires(list -> needsLevel("ar_1", list)).gun(() -> ASSAULT_RIFLE).save();
    public static final UnlockableType AR_VERTICAL_GRIP = registerDynamic("ar_vertical_grip", "Vertical Grip", "Decreases vertical recoil").icon("vertical_grip").pos(1172, 798).requires(list -> needsLevel("ar_1", list)).gun(() -> ASSAULT_RIFLE).save();
    public static final UnlockableType AR_EXTENDED = registerDynamic("ar_extended", "Extended Mag", "Increases magazine capacity to 20").icon("extended").pos(1212, 798).requires(list -> needsLevel("ar_5", list)).gun(() -> ASSAULT_RIFLE).save();
    public static final UnlockableType AR_RED_DOT = registerDynamic("ar_red_dot", "Red Dot Sight", "Adds red dot sight on your weapon").icon("red_dot").pos(1252, 798).requires(list -> needsLevel("ar_5", list)).gun(() -> ASSAULT_RIFLE).save();
    public static final UnlockableType AR_SUPPRESSOR = registerDynamic("ar_suppressor", "Suppressor", "Adds suppressor on your weapon").icon("suppressor").pos(1292, 798).requires(list -> needsLevel("ar_5", list)).gun(() -> ASSAULT_RIFLE).save();
    public static final UnlockableType AR_CHEEKPAD = registerDynamic("ar_cheekpad", "Cheekpad", "Decreases recoil").icon("cheekpad").pos(1332, 798).requires(list -> needsLevel("ar_5", list)).gun(() -> ASSAULT_RIFLE).save();
    public static final UnlockableType ADAPTIVE_CHAMBERING = registerDynamic("adaptive_chambering", "Adaptive Chambering", "Unlocks FULL-AUTO Firemode").pos(1372, 798).requires(list -> needsLevel("ar_8", list)).gun(() -> ASSAULT_RIFLE).save();
    // SR (1384, 990)
    public static final UnlockableType SR_SCOPE = registerDynamic("sr_scope", "Scope", "Adds 6x scope on your weapon").icon("scope").pos(1384, 990).requires(list -> needsLevel("sr_1", list)).gun(() -> SNIPER_RIFLE).save();
    public static final UnlockableType SR_CHEEKPAD = registerDynamic("sr_cheekpad", "Cheekpad", "Decreases recoil").icon("cheekpad").pos(1424, 990).requires(list -> needsLevel("sr_1", list)).gun(() -> SNIPER_RIFLE).save();
    public static final UnlockableType SR_EXTENDED = registerDynamic("sr_extended", "Extended Mag", "Increases magazine capacity to 10").icon("extended").pos(1464, 990).requires(list -> needsLevel("sr_5", list)).gun(() -> SNIPER_RIFLE).save();
    public static final UnlockableType SR_SUPPRESSOR = registerDynamic("sr_suppressor", "Suppressor", "Adds suppressor on your weapon").icon("suppressor").pos(1504, 990).requires(list -> needsLevel("sr_5", list)).gun(() -> SNIPER_RIFLE).save();
    public static final UnlockableType FAST_HANDS = registerDynamic("fast_hands", "Fast Hands", "40% faster reload speed").pos(1544, 990).requires(list -> needsLevel("sr_5", list)).gun(() -> SNIPER_RIFLE).save();
    public static final UnlockableType SR_PENETRATOR = registerDynamic("sr_penetrator", "Penetrator", "Bullets can penetrate multiple enemies").icon("penetrator").pos(1584, 990).requires(list -> needsLevel("sr_5", list)).gun(() -> SNIPER_RIFLE).save();
    public static final UnlockableType DEAD_EYE = registerDynamic("dead_eye", "Dead Eye", "+50% headshot damage").pos(1624, 990).requires(list -> needsLevel("sr_8", list)).gun(() -> SNIPER_RIFLE).save();

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

    private static UnlockableBuilder registerDynamic(String name, String key, String... lines) {
        return new UnlockableBuilder(name, key, lines);
    }

    private static Type registerStatic(String name) {
        Type type = new Type(name, false, null, null);
        LEVEL_REWARD_TYPES.add(type);
        return type;
    }

    private static boolean needsLevel(String treeEntryName, List<EntryInstance> list) {
        return ModUtils.contains(treeEntryName, list, (s, i) -> i.getType().registryName.getResourcePath().equals(s));
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

        public final Supplier<GunItem> gun;
        public final int price = 1;
        public final Predicate<List<EntryInstance>> levelRequirement;
        public final Vec2Di skillTreeVec;

        public UnlockableType(UnlockableBuilder builder) {
            super(builder.name, true, builder.icon != null ? builder.icon : Ability.getIcon(builder.name), builder.key, builder.lines);
            this.gun = builder.gun;
            this.levelRequirement = builder.predicate;
            this.skillTreeVec = builder.location;
            UNLOCKABLE_ABILITY_TYPES.add(this);
        }
    }

    private static class UnlockableBuilder {
        private final String name;
        private final String key;
        private final String[] lines;
        private Predicate<List<EntryInstance>> predicate = l -> true;
        private Vec2Di location;
        private ResourceLocation icon;
        private Supplier<GunItem> gun;

        private UnlockableBuilder(String name, String key, String... lines) {
            this.name = name;
            this.key = key;
            this.lines = lines;
        }

        private UnlockableBuilder gun(Supplier<GunItem> supplier) {
            this.gun = supplier;
            return this;
        }

        private UnlockableBuilder icon(String key) {
            this.icon = Ability.getIcon(key);
            return this;
        }

        private UnlockableBuilder requires(Predicate<List<EntryInstance>> predicate) {
            this.predicate = predicate;
            return this;
        }

        private UnlockableBuilder pos(int x, int y) {
            this.location = new Vec2Di(x, y);
            return this;
        }

        private UnlockableType save() {
            return new UnlockableType(this);
        }
    }
}
