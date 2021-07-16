package dev.toma.gunsrpg.client.model;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public abstract class AbstractSolidEntityModel extends Model implements IRotatableRenderer {

    public AbstractSolidEntityModel() {
        super(RenderType::entitySolid);
    }

    @Override
    public final void setRotationAngle(ModelRenderer renderer, float x, float y, float z) {
        renderer.xRot = x;
        renderer.yRot = y;
        renderer.zRot = z;
    }
}
