package com.wf.firearms.client.render.weapon;

import com.wf.firearms.client.model.WeaponModels;
import com.wf.firearms.client.model.weapon.AbstractWeaponModel;
import com.wf.firearms.client.render.AbstractWeaponRenderer;

public class VectorRenderer extends AbstractWeaponRenderer {

    public VectorRenderer() {
        super("vector");
    }

    @Override
    public AbstractWeaponModel getWeaponModel() {
        return WeaponModels.VECTOR;
    }
}
