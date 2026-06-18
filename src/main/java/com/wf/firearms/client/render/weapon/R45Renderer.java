package com.wf.firearms.client.render.weapon;

import com.wf.firearms.client.model.WeaponModels;
import com.wf.firearms.client.model.weapon.AbstractWeaponModel;
import com.wf.firearms.client.render.AbstractWeaponRenderer;

public class R45Renderer extends AbstractWeaponRenderer {

    public R45Renderer() {
        super("r45");
    }

    @Override
    public AbstractWeaponModel getWeaponModel() {
        return WeaponModels.R45;
    }
}
