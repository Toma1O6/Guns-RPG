package com.wf.firearms.client.render.weapon;

import com.wf.firearms.client.model.WeaponModels;
import com.wf.firearms.client.model.weapon.AbstractWeaponModel;
import com.wf.firearms.client.render.AbstractWeaponRenderer;

public class S686Renderer extends AbstractWeaponRenderer {

    public S686Renderer() {
        super("s686");
    }

    @Override
    public AbstractWeaponModel getWeaponModel() {
        return WeaponModels.S686;
    }
}
