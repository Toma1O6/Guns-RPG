package com.wf.firearms.client.render.weapon;

import com.wf.firearms.client.model.WeaponModels;
import com.wf.firearms.client.model.weapon.AbstractWeaponModel;
import com.wf.firearms.client.render.AbstractWeaponRenderer;

public class AkmRenderer extends AbstractWeaponRenderer {

    public AkmRenderer() {
        super("akm");
    }

    @Override
    public AbstractWeaponModel getWeaponModel() {
        return WeaponModels.AKM;
    }
}
