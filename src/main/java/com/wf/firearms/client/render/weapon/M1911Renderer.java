package com.wf.firearms.client.render.weapon;

import com.wf.firearms.client.model.WeaponModels;
import com.wf.firearms.client.model.weapon.AbstractWeaponModel;
import com.wf.firearms.client.render.AbstractWeaponRenderer;

public class M1911Renderer extends AbstractWeaponRenderer {

    public M1911Renderer() {
        super("m1911");
    }

    @Override
    public AbstractWeaponModel getWeaponModel() {
        return WeaponModels.M1911;
    }
}
