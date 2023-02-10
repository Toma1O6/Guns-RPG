package dev.toma.gunsrpg.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.model.ModelRenderer;

public class RocketTurretModel extends AbstractTurretModel {

    private final ModelRenderer turret;
    private final ModelRenderer leg1;
    private final ModelRenderer leg2;
    private final ModelRenderer leg3;
    private final ModelRenderer cube_r1;
    private final ModelRenderer gun;

    @Override
    public void setTurretRotations(float xRot, float xRotO, float yRot, float yRotO, float partialTicks) {
        rotateModelAccordingToEntity(gun, xRot, xRotO, yRot, yRotO, partialTicks);
    }

    @Override
    public void renderToBuffer(MatrixStack stack, IVertexBuilder builder, int light, int overlay, float red, float green, float blue, float alpha) {
        turret.render(stack, builder, light, overlay, red, green, blue, alpha);
    }

    public RocketTurretModel() {
        texWidth = 512;
        texHeight = 512;

        turret = new ModelRenderer(this);
        turret.setPos(0.0F, 24.0F, -0.25F);
        turret.texOffs(0, 0).addBox(-1.0F, -15.0F, -0.5F, 2.0F, 3.0F, 2.0F, 0.0F, false);

        leg1 = new ModelRenderer(this);
        leg1.setPos(0.0F, 0.0F, 0.0F);
        turret.addChild(leg1);
        setRotationAngle(leg1, -0.5236F, 0.0F, 0.0F);
        leg1.texOffs(0, 0).addBox(-0.5F, -12.0F, -7.0F, 1.0F, 16.0F, 1.0F, 0.0F, false);

        leg2 = new ModelRenderer(this);
        leg2.setPos(0.0F, 0.0F, 0.0F);
        turret.addChild(leg2);
        setRotationAngle(leg2, -0.5236F, -2.3562F, 0.0F);
        leg2.texOffs(0, 0).addBox(-0.5F, -11.5F, -8.0F, 1.0F, 16.0F, 1.0F, 0.0F, false);

        leg3 = new ModelRenderer(this);
        leg3.setPos(0.0F, 0.0F, 0.0F);
        turret.addChild(leg3);


        cube_r1 = new ModelRenderer(this);
        cube_r1.setPos(0.0F, 0.0F, 0.0F);
        leg3.addChild(cube_r1);
        setRotationAngle(cube_r1, -0.5236F, 2.3562F, 0.0F);
        cube_r1.texOffs(0, 0).addBox(-0.5F, -11.5F, -8.0F, 1.0F, 16.0F, 1.0F, 0.0F, false);

        gun = new ModelRenderer(this);
        gun.setPos(0.0F, -17.0F, 0.5F);
        turret.addChild(gun);
        gun.texOffs(197, 81).addBox(-2.0F, -2.0F, -12.0F, 4.0F, 4.0F, 24.0F, 0.0F, false);
        gun.texOffs(27, 163).addBox(-2.5F, -2.5F, -11.9F, 5.0F, 5.0F, 2.0F, 0.0F, false);
        gun.texOffs(27, 163).addBox(-2.5F, -2.5F, 9.9F, 5.0F, 5.0F, 2.0F, 0.0F, false);
        gun.texOffs(27, 163).addBox(-1.85F, -2.2F, -0.5F, 4.0F, 5.0F, 1.0F, 0.0F, false);
    }
}
