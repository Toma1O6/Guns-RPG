package com.wf.firearms.client.model;

import com.wf.firearms.client.model.weapon.AbstractWeaponModel;
import com.wf.firearms.client.model.weapon.AKMModel;
import com.wf.firearms.client.model.weapon.DesertEagleModel;
import com.wf.firearms.client.model.weapon.Kar98kModel;
import com.wf.firearms.client.model.weapon.M1911Model;
import com.wf.firearms.client.model.weapon.R45Model;
import com.wf.firearms.client.model.weapon.S686Model;
import com.wf.firearms.client.model.weapon.ThompsonModel;
import com.wf.firearms.client.model.weapon.VectorModel;
import com.wf.firearms.client.model.weapon.VssModel;
import com.wf.firearms.client.model.weapon.WinchesterModel;

public final class WeaponModels {
    private WeaponModels() {}

    public static final AbstractWeaponModel M1911 = new M1911Model();
    public static final AbstractWeaponModel R45 = new R45Model();
    public static final AbstractWeaponModel DESERT_EAGLE = new DesertEagleModel();
    public static final AbstractWeaponModel THOMPSON = new ThompsonModel();
    public static final AbstractWeaponModel VECTOR = new VectorModel();
    public static final AbstractWeaponModel AKM = new AKMModel();
    public static final AbstractWeaponModel VSS = new VssModel();
    public static final AbstractWeaponModel KAR98K = new Kar98kModel();
    public static final AbstractWeaponModel WINCHESTER = new WinchesterModel();
    public static final AbstractWeaponModel S686 = new S686Model();
    /** 仅无 CGM 时占位；正常由 {@code cgm:mini_gun} 显示。 */
    public static final AbstractWeaponModel MINIGUN = new ThompsonModel();
}
