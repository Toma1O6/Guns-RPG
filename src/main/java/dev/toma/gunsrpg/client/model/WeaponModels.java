package dev.toma.gunsrpg.client.model;

import dev.toma.gunsrpg.client.model.weapon.*;

public final class WeaponModels {
    private WeaponModels() {}

    public static final AbstractWeaponModel M1911 = new M1911Model();
    public static final AbstractWeaponModel R45 = new R45Model();
    public static final AbstractWeaponModel DESERT_EAGLE = new DesertEagleModel();
    public static final AbstractWeaponModel WOODEN_CROSSBOW = new WoodenCrossbowModel();
    public static final AbstractWeaponModel UMP_45 = new Ump45Model();
    public static final AbstractWeaponModel THOMPSON = new ThompsonModel();
    public static final AbstractWeaponModel VECTOR = new VectorModel();
    public static final AbstractWeaponModel S1897 = new S1897Model();
    public static final AbstractWeaponModel S686 = new S686Model();
    public static final AbstractWeaponModel S12K = new S12kModel();
    public static final AbstractWeaponModel SKS = new SksModel();
    public static final AbstractWeaponModel AKM = new AKMModel();
    public static final AbstractWeaponModel KAR98K = new Kar98kModel();
    public static final AbstractWeaponModel WINCHESTER = new WinchesterModel();
    public static final AbstractWeaponModel MK14 = new Mk14Model();

    public static final AbstractAttachmentModel SCOPE = new ScopeModel();
    public static final AbstractAttachmentModel SUPPRESSOR = new SuppressorModel();
    public static final AbstractAttachmentModel REFLEX = new ReflexSightModel();
}
