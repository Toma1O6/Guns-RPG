package dev.toma.gunsrpg.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class RocketModel extends Model {

    private final ModelRenderer rocket;
    private final ModelRenderer zp;
    private final ModelRenderer zn;
    private final ModelRenderer tail;
    private final ModelRenderer top;
    private final ModelRenderer bottom;
    private final ModelRenderer left;
    private final ModelRenderer right;

    public RocketModel() {
        super(RenderType::entityCutout);
        texWidth = 512;
        texHeight = 512;

        rocket = new ModelRenderer(this);
        rocket.setPos(0.0F, 24.0F, -2.0F);
        rocket.texOffs(0, 70).addBox(-1.0F, -5.85F, -10.0F, 2.0F, 1.0F, 25.0F, 0.0F, false);
        rocket.texOffs(96, 37).addBox(-1.5F, -4.85F, 14.75F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        rocket.texOffs(96, 37).addBox(-1.5F, -4.85F, -11.75F, 3.0F, 3.0F, 2.0F, 0.0F, false);
        rocket.texOffs(96, 37).addBox(-1.0F, -4.35F, -13.25F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        rocket.texOffs(2, 77).addBox(-1.0F, -2.0F, -10.0F, 2.0F, 1.0F, 25.0F, 0.0F, false);
        rocket.texOffs(2, 76).addBox(-2.4142F, -4.4142F, -10.0F, 1.0F, 2.0F, 25.0F, 0.0F, false);
        rocket.texOffs(0, 72).addBox(1.4142F, -4.4142F, -10.0F, 1.0F, 2.0F, 25.0F, 0.0F, false);

        zp = new ModelRenderer(this);
        zp.setPos(0.0F, 0.0F, 0.0F);
        rocket.addChild(zp);
        setRotationAngle(zp, 0.0F, 0.0F, 0.7854F);
        zp.texOffs(0, 78).addBox(-3.4295F, -4.8437F, -10.0F, 2.0F, 1.0F, 25.0F, 0.0F, false);
        zp.texOffs(0, 76).addBox(-3.4142F, -1.0F, -10.0F, 2.0F, 1.0F, 25.0F, 0.0F, false);

        zn = new ModelRenderer(this);
        zn.setPos(0.0F, 0.0F, 0.0F);
        rocket.addChild(zn);
        setRotationAngle(zn, 0.0F, 0.0F, -0.7854F);
        zn.texOffs(2, 77).addBox(1.4142F, -1.0F, -10.0F, 2.0F, 1.0F, 25.0F, 0.0F, false);
        zn.texOffs(2, 80).addBox(1.4295F, -4.8437F, -10.0F, 2.0F, 1.0F, 25.0F, 0.0F, false);

        tail = new ModelRenderer(this);
        tail.setPos(0.0F, 0.0F, 7.0F);
        rocket.addChild(tail);


        top = new ModelRenderer(this);
        top.setPos(0.0F, 0.0F, 0.0F);
        tail.addChild(top);
        setRotationAngle(top, 0.5236F, 0.0F, 0.0F);
        top.texOffs(77, 79).addBox(-0.5F, -3.75F, 5.25F, 1.0F, 3.0F, 8.0F, 0.0F, false);
        top.texOffs(77, 79).addBox(-0.5F, -11.75F, -9.0179F, 1.0F, 2.0F, 6.0F, 0.0F, false);

        bottom = new ModelRenderer(this);
        bottom.setPos(0.0F, 0.0F, 0.0F);
        tail.addChild(bottom);
        setRotationAngle(bottom, -0.5236F, 0.0F, 0.0F);
        bottom.texOffs(102, 92).addBox(-0.5F, -5.25F, 1.25F, 1.0F, 3.0F, 8.0F, 0.0F, false);
        bottom.texOffs(102, 92).addBox(-0.5F, 4.0F, -13.0179F, 1.0F, 2.0F, 6.0F, 0.0F, false);

        left = new ModelRenderer(this);
        left.setPos(0.0F, 0.0F, 0.0F);
        tail.addChild(left);
        setRotationAngle(left, 0.0F, 0.5236F, 0.0F);
        left.texOffs(87, 84).addBox(-2.3F, -3.9F, 3.25F, 3.0F, 1.0F, 8.0F, 0.0F, false);
        left.texOffs(87, 84).addBox(6.7F, -3.9F, -11.0179F, 2.0F, 1.0F, 6.0F, 0.0F, false);

        right = new ModelRenderer(this);
        right.setPos(0.0F, 0.0F, 0.0F);
        tail.addChild(right);
        setRotationAngle(right, 0.0F, -0.5236F, 0.0F);
        right.texOffs(87, 84).addBox(-0.65F, -3.9F, 3.75F, 3.0F, 1.0F, 8.0F, 0.0F, false);
        right.texOffs(87, 84).addBox(-8.65F, -3.9F, -10.5179F, 2.0F, 1.0F, 6.0F, 0.0F, false);
    }

    @Override
    public void renderToBuffer(MatrixStack matrix, IVertexBuilder builder, int light, int overlay, float red, float green, float blue, float alpha) {
        rocket.render(matrix, builder, light, overlay, red, green, blue, alpha);
    }

    private void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
