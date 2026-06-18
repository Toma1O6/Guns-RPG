package com.wf.firearms.client.render.weapon;

import com.wf.firearms.client.model.WeaponModels;
import com.wf.firearms.client.model.weapon.AbstractWeaponModel;
import com.wf.firearms.client.render.AbstractWeaponRenderer;

/** 温彻斯特杠杆步枪（Win94 模型）。 */
public class WinchesterRenderer extends AbstractWeaponRenderer {

    public WinchesterRenderer() {
        super("winchester");
    }

    @Override
    public AbstractWeaponModel getWeaponModel() {
        return WeaponModels.WINCHESTER;
    }
}
