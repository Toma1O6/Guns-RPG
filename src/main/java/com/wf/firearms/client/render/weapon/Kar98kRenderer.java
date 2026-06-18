package com.wf.firearms.client.render.weapon;

import com.wf.firearms.client.model.WeaponModels;
import com.wf.firearms.client.model.weapon.AbstractWeaponModel;
import com.wf.firearms.client.render.AbstractWeaponRenderer;

public class Kar98kRenderer extends AbstractWeaponRenderer {

    public Kar98kRenderer() {
        super("kar98k");
    }

    @Override
    public AbstractWeaponModel getWeaponModel() {
        return WeaponModels.KAR98K;
    }
}
