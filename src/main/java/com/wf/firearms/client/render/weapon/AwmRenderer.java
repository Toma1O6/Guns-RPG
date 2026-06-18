package com.wf.firearms.client.render.weapon;

import com.wf.firearms.client.model.WeaponModels;
import com.wf.firearms.client.model.weapon.AbstractWeaponModel;
import com.wf.firearms.client.render.AbstractWeaponRenderer;

/** AWM 栓动狙击；模型暂复用 Kar98k，贴图走统一 weapon atlas。 */
public class AwmRenderer extends AbstractWeaponRenderer {

    public AwmRenderer() {
        super("awm");
    }

    @Override
    public AbstractWeaponModel getWeaponModel() {
        return WeaponModels.KAR98K;
    }
}
