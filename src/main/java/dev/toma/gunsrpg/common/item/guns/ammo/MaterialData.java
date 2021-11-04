package dev.toma.gunsrpg.common.item.guns.ammo;

public class MaterialData implements IMaterialData {

    public static final MaterialData EMPTY = new MaterialData(0.0F, 0.0F, 0.0F);

    private final float recoil, durability, jamChance;

    public MaterialData(float recoil, float durability, float jamChance) {
        this.recoil = recoil;
        this.durability = durability;
        this.jamChance = jamChance;
    }

    @Override
    public float getAddedRecoil() {
        return recoil;
    }

    @Override
    public float getAddedDurability() {
        return durability;
    }

    @Override
    public float getAddedJamChance() {
        return jamChance;
    }
}
