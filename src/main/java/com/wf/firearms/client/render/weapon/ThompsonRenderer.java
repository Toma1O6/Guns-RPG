package com.wf.firearms.client.render.weapon;

import com.wf.firearms.client.model.WeaponModels;
import com.wf.firearms.client.model.weapon.AbstractWeaponModel;
import com.wf.firearms.client.render.AbstractWeaponRenderer;

public class ThompsonRenderer extends AbstractWeaponRenderer {

    public ThompsonRenderer() {
        super("thompson");
    }

    @Override
    public AbstractWeaponModel getWeaponModel() {
        return WeaponModels.THOMPSON;
    }
}
