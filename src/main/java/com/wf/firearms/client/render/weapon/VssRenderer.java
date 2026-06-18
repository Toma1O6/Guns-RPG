package com.wf.firearms.client.render.weapon;

import com.wf.firearms.client.model.WeaponModels;
import com.wf.firearms.client.model.weapon.AbstractWeaponModel;
import com.wf.firearms.client.render.AbstractWeaponRenderer;

public class VssRenderer extends AbstractWeaponRenderer {

    public VssRenderer() {
        super("vss");
    }

    @Override
    public AbstractWeaponModel getWeaponModel() {
        return WeaponModels.VSS;
    }
}
