package com.wf.firearms.client.render.weapon;

import com.wf.firearms.client.model.WeaponModels;
import com.wf.firearms.client.model.weapon.AbstractWeaponModel;
import com.wf.firearms.client.render.AbstractWeaponRenderer;

/** 加特林：有 CGM 时用 mini_gun 外观；此处仅为无 CGM 时的占位。 */
public class MinigunRenderer extends AbstractWeaponRenderer {

    public MinigunRenderer() {
        super("minigun");
    }

    @Override
    public AbstractWeaponModel getWeaponModel() {
        return WeaponModels.THOMPSON;
    }
}
