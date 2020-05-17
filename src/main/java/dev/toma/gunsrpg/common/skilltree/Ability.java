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
    public static final Type PISTOL_WOOD_AMMO = registerStatic();
    public static final Type PISTOL_STONE_AMMO = registerStatic();
    public static final Type PISTOL_IRON_AMMO = registerStatic();
    public static final Type PISTOL_GOLD_AMMO = registerStatic();
    public static final Type PISTOL_DIAMOND_AMMO = registerStatic();
    public static final Type PISTOL_EMERALD_AMMO = registerStatic();
    public static final Type PISTOL_AMETHYST_AMMO = registerStatic();
    public static final Type SMG_WOOD_AMMO = registerStatic();
    public static final Type SMG_STONE_AMMO = registerStatic();
    public static final Type SMG_IRON_AMMO = registerStatic();
    public static final Type SMG_GOLD_AMMO = registerStatic();
    public static final Type SMG_DIAMOND_AMMO = registerStatic();
    public static final Type SMG_EMERALD_AMMO = registerStatic();
    public static final Type SMG_AMETHYST_AMMO = registerStatic();
    public static final Type AR_WOOD_AMMO = registerStatic();
    public static final Type AR_STONE_AMMO = registerStatic();
    public static final Type AR_IRON_AMMO = registerStatic();
    public static final Type AR_GOLD_AMMO = registerStatic();
    public static final Type AR_DIAMOND_AMMO = registerStatic();
    public static final Type AR_EMERALD_AMMO = registerStatic();
    public static final Type AR_AMETHYST_AMMO = registerStatic();
    public static final Type SR_WOOD_AMMO = registerStatic();
    public static final Type SR_STONE_AMMO = registerStatic();
    public static final Type SR_IRON_AMMO = registerStatic();
    public static final Type SR_GOLD_AMMO = registerStatic();
    public static final Type SR_DIAMOND_AMMO = registerStatic();
    public static final Type SR_EMERALD_AMMO = registerStatic();
    public static final Type SR_AMETHYST_AMMO = registerStatic();
    public static final UnlockableType PISTOL_QUICKDRAW = registerUnlockable(null, "Quickdraw", "30% faster pistol reload speed").requires(list -> needsLevel("pistol_1", list)).save();
    public static final UnlockableType PISTOL_EXTENDED = registerUnlockable(null, "Extended Mag", "Increase pistol magazine kapacity to 13").requires(list -> needsLevel("pistol_1", list)).save();
    public static final UnlockableType PISTOL_TOUGH_SPRING = registerUnlockable(null, "Tought Spring", "Increases pistol firerate").requires(list -> needsLevel("pistol_5", list)).save();
    public static final UnlockableType PISTOL_CARBON_BARREL = registerUnlockable(null, "Carbon Barrel", "Decreases recoil").requires(list -> needsLevel("pistol_5", list)).save();
    public static final UnlockableType PISTOL_SUPPRESSOR = registerUnlockable(null, "Suppressor", "Mobs won't be able to hear you").requires(list -> needsLevel("pistol_5", list)).save();
    public static final UnlockableType PISTOL_HEAVY_BULLETS = registerUnlockable(null, "Heavy Bullets", "Can slow down mobs on hit").requires(list -> needsLevel("pistol_5", list)).save();
    public static final UnlockableType DUAL_WIELD = registerUnlockable(null, "Dual Wielding", "Allows you to carry pistol in both hands").requires(list -> needsLevel("pistol_8", list)).save();

    public boolean enabled;
    public final Type type;

    public Ability(Type type) {
        this(type, false);
    }

    public Ability(Type type, boolean enabled) {
        this.type = type;
        this.enabled = enabled;
    }

    public void toggle() {
        if(type.canDisable) {
            enabled = !enabled;
        }
    }

    private static UnlockableBuilder registerUnlockable(ResourceLocation icon, String key, String... lines) {
        return new UnlockableBuilder(icon, key, lines);
    }

    private static Type registerStatic() {
        Type type = new Type(false, null, null);
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

        @Nullable public String title;
        public String[] lines;
        @Nullable public ResourceLocation icon;
        public boolean canDisable;

        public Type(boolean canDisable, @Nullable ResourceLocation icon, @Nullable String key, String... lines) {
            this.canDisable = canDisable;
            this.title = key;
            this.icon = icon;
            this.lines = lines;
        }
    }

    public static class UnlockableType extends Type {

        public final Predicate<List<EntryInstance>> levelRequirement;
        public final Vec2Di skillTreeVec;

        public UnlockableType(UnlockableBuilder builder) {
            super(true, builder.icon, builder.key, builder.lines);
            this.levelRequirement = builder.predicate;
            this.skillTreeVec = builder.location;
            UNLOCKABLE_ABILITY_TYPES.add(this);
        }
    }

    private static class UnlockableBuilder {
        private ResourceLocation icon;
        private String key;
        private String[] lines;
        private Predicate<List<EntryInstance>> predicate = l -> true;
        private Vec2Di location;

        private UnlockableBuilder(ResourceLocation location, String key, String... lines) {
            this.icon = location;
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

        private UnlockableType save() {
            return new UnlockableType(this);
        }
    }
}
