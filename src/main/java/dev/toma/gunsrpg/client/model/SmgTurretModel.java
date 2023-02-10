package dev.toma.gunsrpg.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.model.ModelRenderer;

public class SmgTurretModel extends AbstractTurretModel {

    private final ModelRenderer turret;
    private final ModelRenderer gun;
    private final ModelRenderer x45;
    private final ModelRenderer bb_main;

    @Override
    public void setTurretRotations(float xRot, float xRotO, float yRot, float yRotO, float partialTicks) {
        rotateModelAccordingToEntity(gun, xRot, xRotO, yRot, yRotO, partialTicks);
    }

    @Override
    public void renderToBuffer(MatrixStack stack, IVertexBuilder builder, int light, int overlay, float red, float green, float blue, float alpha) {
        turret.render(stack, builder, light, overlay, red, green, blue, alpha);
        bb_main.render(stack, builder, light, overlay, red, green, blue, alpha);
    }

    public SmgTurretModel() {
        texWidth = 512;
        texHeight = 512;

        turret = new ModelRenderer(this);
        turret.setPos(0.0F, 24.0F, -0.25F);


        gun = new ModelRenderer(this);
        gun.setPos(0.0F, -20.9688F, -0.1875F);
        turret.addChild(gun);
        gun.texOffs(69, 75).addBox(-1.0F, 0.4688F, -24.3125F, 2.0F, 2.0F, 24.0F, 0.0F, false);
        gun.texOffs(69, 70).addBox(-1.0F, -2.2813F, -19.3125F, 2.0F, 2.0F, 24.0F, 0.0F, false);
        gun.texOffs(1, 148).addBox(-4.0F, -4.0313F, -2.3125F, 8.0F, 1.0F, 12.0F, 0.0F, false);
        gun.texOffs(4, 152).addBox(3.0F, -3.0313F, -2.3125F, 1.0F, 6.0F, 12.0F, 0.0F, false);
        gun.texOffs(4, 152).addBox(-4.0F, 2.9688F, -9.3125F, 8.0F, 1.0F, 19.0F, 0.0F, false);
        gun.texOffs(25, 148).addBox(-3.0F, -3.0313F, 8.6875F, 6.0F, 6.0F, 1.0F, 0.0F, false);
        gun.texOffs(25, 148).addBox(-4.0F, -3.0313F, -2.3125F, 1.0F, 6.0F, 12.0F, 0.0F, false);
        gun.texOffs(98, 36).addBox(-3.0F, -3.0313F, -1.3125F, 6.0F, 6.0F, 1.0F, 0.0F, false);

        x45 = new ModelRenderer(this);
        x45.setPos(0.0F, 16.9688F, -1.3125F);
        gun.addChild(x45);
        setRotationAngle(x45, -0.7854F, 0.0F, 0.0F);
        x45.texOffs(29, 152).addBox(3.0F, -14.2426F, -15.5563F, 1.0F, 10.0F, 1.0F, 0.0F, false);
        x45.texOffs(4, 152).addBox(3.0F, -13.2426F, -14.5563F, 1.0F, 8.0F, 1.0F, 0.0F, false);
        x45.texOffs(4, 152).addBox(3.0F, -12.2426F, -13.5563F, 1.0F, 6.0F, 1.0F, 0.0F, false);
        x45.texOffs(4, 152).addBox(3.0F, -11.2426F, -12.5563F, 1.0F, 4.0F, 1.0F, 0.0F, false);
        x45.texOffs(37, 167).addBox(3.0F, -10.2426F, -11.5563F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        x45.texOffs(20, 145).addBox(-4.0F, -14.2426F, -15.5563F, 1.0F, 10.0F, 1.0F, 0.0F, false);
        x45.texOffs(20, 145).addBox(-4.0F, -13.2426F, -14.5563F, 1.0F, 8.0F, 1.0F, 0.0F, false);
        x45.texOffs(20, 145).addBox(-4.0F, -12.2426F, -13.5563F, 1.0F, 6.0F, 1.0F, 0.0F, false);
        x45.texOffs(20, 145).addBox(-4.0F, -11.2426F, -12.5563F, 1.0F, 4.0F, 1.0F, 0.0F, false);
        x45.texOffs(20, 145).addBox(-4.0F, -10.2426F, -11.5563F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        bb_main = new ModelRenderer(this);
        bb_main.setPos(0.0F, 24.0F, 0.0F);
        bb_main.texOffs(0, 83).addBox(-7.0F, -1.0F, -7.0F, 14.0F, 1.0F, 14.0F, 0.0F, false);
        bb_main.texOffs(10, 86).addBox(7.0F, -1.0F, -4.0F, 2.0F, 1.0F, 8.0F, 0.0F, false);
        bb_main.texOffs(10, 93).addBox(-9.0F, -1.0F, -4.0F, 2.0F, 1.0F, 8.0F, 0.0F, false);
        bb_main.texOffs(16, 88).addBox(-4.0F, -1.0F, -9.0F, 8.0F, 1.0F, 2.0F, 0.0F, false);
        bb_main.texOffs(19, 97).addBox(-4.0F, -1.0F, 7.0F, 8.0F, 1.0F, 2.0F, 0.0F, false);
        bb_main.texOffs(10, 98).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 1.0F, 8.0F, 0.0F, false);
        bb_main.texOffs(27, 99).addBox(-2.0F, -2.0F, 4.0F, 4.0F, 1.0F, 2.0F, 0.0F, false);
        bb_main.texOffs(16, 86).addBox(-2.0F, -2.0F, -6.0F, 4.0F, 1.0F, 2.0F, 0.0F, false);
        bb_main.texOffs(22, 95).addBox(4.0F, -2.0F, -2.0F, 2.0F, 1.0F, 4.0F, 0.0F, false);
        bb_main.texOffs(16, 85).addBox(-6.0F, -2.0F, -2.0F, 2.0F, 1.0F, 4.0F, 0.0F, false);
        bb_main.texOffs(13, 91).addBox(-2.0F, -17.9F, -3.0F, 4.0F, 16.0F, 6.0F, 0.0F, false);
        bb_main.texOffs(24, 92).addBox(-3.0F, -17.9F, -2.0F, 6.0F, 16.0F, 4.0F, 0.0F, false);
    }
}
