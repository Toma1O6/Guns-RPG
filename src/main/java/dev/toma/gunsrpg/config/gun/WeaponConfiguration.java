package dev.toma.gunsrpg.config.gun;

import net.minecraftforge.common.config.Config;

public class WeaponConfiguration {

    @Config.Name("Damage")
    @Config.Comment("Defines weapon damage")
    @Config.RangeDouble(min = 1.0)
    public float damage;

    @Config.Name("Velocity")
    @Config.Comment("Defines weapon velocity")
    @Config.RangeDouble(min = 0.1, max = 100.0)
    public float velocity;

    @Config.Name("Gravity resistant time")
    @Config.Comment("Defines how many ticks before gravity effect is applied")
    @Config.RangeInt(min = 0)
    public int effect;

    @Config.Name("Horizonal recoil")
    @Config.Comment("Defines weapon horizontal recoil")
    @Config.RangeDouble(min = 0)
    public float recoilHorizontal;

    @Config.Name("Vertical recoil")
    @Config.Comment("Defines weapon vertical recoil")
    @Config.RangeDouble(min = 0)
    public float recoilVertical;

    @Config.Name("Normal firerate")
    @Config.Comment("Defines delay after each shot with no firerate skill applied")
    @Config.RangeInt(min = 1, max = 1000)
    public int normal;

    @Config.Name("Upgraded firerate")
    @Config.Comment("Defines delay after each shot with firerate skill applied")
    @Config.RangeInt(min = 1, max = 1000)
    public int upgraded;

    public WeaponConfiguration(float damage, float velocity, int effect, float recoilHorizontal, float recoilVertical, int rof, int urof) {
        this.damage = damage;
        this.velocity = velocity;
        this.effect = effect;
        this.recoilHorizontal = recoilHorizontal;
        this.recoilVertical = recoilVertical;
        this.normal = rof;
        this.upgraded = urof;
    }

    public WeaponConfiguration(float damage, float velocity, int effect, float recoilHorizontal, float recoilVertical, int rof) {
        this(damage, velocity, effect, recoilHorizontal, recoilVertical, rof, rof);
    }
}
