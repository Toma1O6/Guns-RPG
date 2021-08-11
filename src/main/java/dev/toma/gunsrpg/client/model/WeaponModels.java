package dev.toma.gunsrpg.client.model;

public final class WeaponModels {
    private WeaponModels() {}

    public static final AbstractWeaponModel M1911 = new M1911Model();
    public static final AbstractWeaponModel WOODEN_CROSSBOW = new WoodenCrossbowModel();
    public static final AbstractWeaponModel UMP_45 = new Ump45Model();
    public static final AbstractWeaponModel S1897 = new S1897Model();
    public static final AbstractWeaponModel SKS = new SksModel();
    public static final AbstractWeaponModel KAR98K = new Kar98kModel();
}
