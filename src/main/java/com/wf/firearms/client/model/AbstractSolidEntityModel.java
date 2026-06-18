package com.wf.firearms.client.model;

import com.wf.firearms.client.model.legacy.LegacyModel;
import com.wf.firearms.client.model.legacy.LegacyModelRenderer;

public abstract class AbstractSolidEntityModel extends LegacyModel {

    public void setRotationAngle(LegacyModelRenderer renderer, float x, float y, float z) {
        renderer.xRot = x;
        renderer.yRot = y;
        renderer.zRot = z;
    }
}
