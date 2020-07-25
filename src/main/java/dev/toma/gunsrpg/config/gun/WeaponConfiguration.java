package dev.toma.gunsrpg.config.gun;

import toma.config.object.builder.ConfigBuilder;

public class WeaponConfiguration {

    private final String name;
    public float damage, velocity;
    public int effect;
    public float recoilHorizontal, recoilVertical;
    public int normal, upgraded;

    public WeaponConfiguration(String name, float damage, float velocity, int effect, float recoilHorizontal, float recoilVertical, int rof, int urof) {
        this.name = name;
        this.damage = damage;
        this.velocity = velocity;
        this.effect = effect;
        this.recoilHorizontal = recoilHorizontal;
        this.recoilVertical = recoilVertical;
        this.normal = rof;
        this.upgraded = urof;
    }

    public WeaponConfiguration(String name, float damage, float velocity, int effect, float recoilHorizontal, float recoilVertical, int rof) {
        this(name, damage, velocity, effect, recoilHorizontal, recoilVertical, rof, rof);
    }

    public ConfigBuilder build(ConfigBuilder builder) {
        return builder
                .push().name(name).init()
                .addFloat(damage).name("Damage").range(0.0F, 50.0F).add(t -> damage = t.value())
                .addFloat(velocity).name("Bullet velocity").range(0.5F, 30.0F).add(t -> velocity = t.value())
                .addInt(effect).name("Gravity start").range(0, Integer.MAX_VALUE).add(t -> effect = t.value())
                .addFloat(recoilHorizontal).name("Horizontal recoil").range(0.0F, 10.0F).add(t -> recoilHorizontal = t.value())
                .addFloat(recoilVertical).name("Vertical recoil").range(0.0F, 10.0F).add(t -> recoilVertical = t.value())
                .addInt(normal).name("Rate of Fire").range(1, 60).add(t -> normal = t.value())
                .addInt(upgraded).name("Upgraded Rate of Fire").range(1, 60).add(t -> upgraded = t.value())
                .pop();
    }
}
