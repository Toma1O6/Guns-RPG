package com.wf.firearms.client.render.weapon;

import com.wf.firearms.client.model.WeaponModels;
import com.wf.firearms.client.model.weapon.AbstractWeaponModel;
import com.wf.firearms.client.render.AbstractWeaponRenderer;

public class DesertEagleRenderer extends AbstractWeaponRenderer {

    public DesertEagleRenderer() {
        super("desert_eagle");
    }

    @Override
    public AbstractWeaponModel getWeaponModel() {
        return WeaponModels.DESERT_EAGLE;
    }
}
