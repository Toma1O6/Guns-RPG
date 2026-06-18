package com.wf.firearms.combat;

import net.minecraft.world.item.Item;

import java.util.List;

public record FirearmSpec(
        String weaponKey,
        WeaponClass weaponClass,
        float baseDamage,
        int fireIntervalTicks,
        boolean defaultAutomatic,
        float projectileSpeed,
        float spread,
        int pelletCount,
        List<Item> acceptedAmmo,
        int magazineSize,
        ReloadStyle reloadStyle,
        int reloadTicks,
        int shellReloadTicks,
        int reloadPrepTicks,
        int unjamTicks,
        float jamChancePerShot,
        float aimSpreadMultiplier,
        boolean supportsFireModeSwitch,
        int burstShots,
        int burstIntervalTicks) {

    /** @deprecated 使用 {@link #defaultAutomatic()} */
    public boolean automatic() {
        return defaultAutomatic();
    }

    public static Builder builder(String weaponKey) {
        return new Builder(weaponKey);
    }

    public static final class Builder {
        private final String weaponKey;
        private WeaponClass weaponClass = WeaponClass.OTHER;
        private float baseDamage = 5f;
        private int fireIntervalTicks = 10;
        private boolean defaultAutomatic;
        private float projectileSpeed = 8f;
        private float spread = 0.03f;
        private int pelletCount = 1;
        private List<Item> acceptedAmmo = List.of();
        private int magazineSize = 12;
        private ReloadStyle reloadStyle = ReloadStyle.FULL_MAG;
        private int reloadTicks = 40;
        private int shellReloadTicks = 22;
        private int reloadPrepTicks;
        private int unjamTicks = 35;
        private float jamChancePerShot = 0.012f;
        private float aimSpreadMultiplier = 0.35f;
        private boolean supportsFireModeSwitch;
        private int burstShots = 1;
        private int burstIntervalTicks = 3;

        private Builder(String weaponKey) {
            this.weaponKey = weaponKey;
        }

        public Builder weaponClass(WeaponClass c) {
            this.weaponClass = c;
            return this;
        }

        public Builder baseDamage(float d) {
            this.baseDamage = d;
            return this;
        }

        public Builder fireIntervalTicks(int t) {
            this.fireIntervalTicks = t;
            return this;
        }

        public Builder automatic(boolean a) {
            this.defaultAutomatic = a;
            return this;
        }

        public Builder projectileSpeed(float s) {
            this.projectileSpeed = s;
            return this;
        }

        public Builder spread(float s) {
            this.spread = s;
            return this;
        }

        public Builder pelletCount(int count) {
            this.pelletCount = Math.max(1, count);
            return this;
        }

        public Builder ammo(List<Item> items) {
            this.acceptedAmmo = List.copyOf(items);
            return this;
        }

        public Builder magazineSize(int size) {
            this.magazineSize = size;
            return this;
        }

        public Builder reloadTicks(int ticks) {
            this.reloadTicks = ticks;
            return this;
        }

        public Builder reloadStyle(ReloadStyle style) {
            this.reloadStyle = style;
            return this;
        }

        public Builder shellReloadTicks(int ticks) {
            this.shellReloadTicks = ticks;
            return this;
        }

        public Builder reloadPrepTicks(int ticks) {
            this.reloadPrepTicks = ticks;
            return this;
        }

        public Builder unjamTicks(int ticks) {
            this.unjamTicks = ticks;
            return this;
        }

        public Builder jamChance(float chance) {
            this.jamChancePerShot = chance;
            return this;
        }

        public Builder aimSpreadMultiplier(float mult) {
            this.aimSpreadMultiplier = mult;
            return this;
        }

        public Builder fireModeSwitch(boolean enabled) {
            this.supportsFireModeSwitch = enabled;
            return this;
        }

        /** 单次扳机连射次数（如 S686 快速双发 = 2）。 */
        public Builder burstShots(int count) {
            this.burstShots = Math.max(1, count);
            return this;
        }

        /** 连射各发之间的间隔 tick（仅 burstShots > 1 时生效）。 */
        public Builder burstIntervalTicks(int ticks) {
            this.burstIntervalTicks = Math.max(1, ticks);
            return this;
        }

        public FirearmSpec build() {
            return new FirearmSpec(
                    weaponKey,
                    weaponClass,
                    baseDamage,
                    fireIntervalTicks,
                    defaultAutomatic,
                    projectileSpeed,
                    spread,
                    pelletCount,
                    acceptedAmmo,
                    magazineSize,
                    reloadStyle,
                    reloadTicks,
                    shellReloadTicks,
                    reloadPrepTicks,
                    unjamTicks,
                    jamChancePerShot,
                    aimSpreadMultiplier,
                    supportsFireModeSwitch,
                    burstShots,
                    burstIntervalTicks);
        }
    }
}
