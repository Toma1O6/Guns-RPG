package dev.toma.gunsrpg.client.model.weapon;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.client.animation.ModAnimations;
import dev.toma.gunsrpg.common.init.Skills;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;

public class AwmModel extends AbstractWeaponModel {

    private final ModelRenderer awm;
    private final ModelRenderer bone10;
    private final ModelRenderer bone33;
    private final ModelRenderer bone28;
    private final ModelRenderer bone30;
    private final ModelRenderer bone31;
    private final ModelRenderer bone32;
    private final ModelRenderer bone11;
    private final ModelRenderer grip;
    private final ModelRenderer bone21;
    private final ModelRenderer bone27;
    private final ModelRenderer bone22;
    private final ModelRenderer bone13;
    private final ModelRenderer bone14;
    private final ModelRenderer bone15;
    private final ModelRenderer bone16;
    private final ModelRenderer bone17;
    private final ModelRenderer bone18;
    private final ModelRenderer bone19;
    private final ModelRenderer bone20;
    private final ModelRenderer bone23;
    private final ModelRenderer bone24;
    private final ModelRenderer bone25;
    private final ModelRenderer bone26;
    private final ModelRenderer magazine;
    private final ModelRenderer bullet;
    private final ModelRenderer bullet2;
    private final ModelRenderer ironsights;
    private final ModelRenderer bone38;
    private final ModelRenderer bone39;
    private final ModelRenderer bone36;
    private final ModelRenderer bone37;
    private final ModelRenderer bolt_case;
    private final ModelRenderer bone;
    private final ModelRenderer bone12;
    private final ModelRenderer bone4;
    private final ModelRenderer bone2;
    private final ModelRenderer bone3;
    private final ModelRenderer bolt;
    private final ModelRenderer catch_;
    private final ModelRenderer bone7;
    private final ModelRenderer bone8;
    private final ModelRenderer bone6;
    private final ModelRenderer rotatingPart3;
    private final ModelRenderer bone5;
    private final ModelRenderer stock;
    private final ModelRenderer bone9;
    private final ModelRenderer bone29;
    private final ModelRenderer bone41;
    private final ModelRenderer bone49;
    private final ModelRenderer bone50;
    private final ModelRenderer bone52;
    private final ModelRenderer bone42;
    private final ModelRenderer bone44;
    private final ModelRenderer bone45;
    private final ModelRenderer bone47;
    private final ModelRenderer bone48;
    private final ModelRenderer bone51;
    private final ModelRenderer bone46;
    private final ModelRenderer bone40;
    private final ModelRenderer bone43;

    @Override
    protected void renderWeapon(ItemStack stack, IPlayerData data, MatrixStack matrix, IVertexBuilder builder, int light, int overlay) {
        matrix.mulPose(Vector3f.YN.rotationDegrees(90f));
        awm.render(matrix, builder, light, overlay);
        if (!data.getSkillProvider().hasSkill(Skills.AWM_SCOPE)) {
            ironsights.render(matrix, builder, light, overlay);
        }
        stock.render(matrix, builder, light, overlay);
    }

    public AwmModel() {
        texWidth = 512;
        texHeight = 512;

        awm = new ModelRenderer(this);
        awm.setPos(0.0F, 24.0F, 0.0F);
        awm.texOffs(196, 86).addBox(-6.0F, -29.5F, -3.5F, 8.0F, 5.0F, 7.0F, 0.0F, true);
        awm.texOffs(196, 86).addBox(4.0F, -24.5F, -3.5F, 8.0F, 4.0F, 7.0F, 0.0F, true);
        awm.texOffs(196, 86).addBox(-4.0F, -24.5F, -3.5F, 8.0F, 4.0F, 7.0F, 0.0F, true);
        awm.texOffs(196, 86).addBox(-12.0F, -24.5F, -3.5F, 8.0F, 4.0F, 7.0F, 0.0F, true);
        awm.texOffs(196, 86).addBox(-20.0F, -24.5F, -3.5F, 8.0F, 4.0F, 7.0F, 0.0F, true);
        awm.texOffs(196, 86).addBox(-25.0F, -24.5F, -3.5F, 5.0F, 4.0F, 7.0F, 0.0F, true);
        awm.texOffs(196, 86).addBox(12.0F, -24.5F, -3.5F, 12.0F, 4.0F, 7.0F, 0.0F, true);
        awm.texOffs(196, 86).addBox(4.0F, -20.5F, -3.5F, 6.0F, 3.0F, 7.0F, 0.0F, true);
        awm.texOffs(196, 86).addBox(-1.0F, -20.5F, -3.5F, 5.0F, 3.0F, 7.0F, 0.0F, true);
        awm.texOffs(196, 86).addBox(-10.0F, -20.5F, -3.5F, 9.0F, 3.0F, 1.0F, 0.0F, true);
        awm.texOffs(196, 86).addBox(-10.0F, -20.5F, 2.5F, 9.0F, 3.0F, 1.0F, 0.0F, false);
        awm.texOffs(196, 86).addBox(11.0F, -20.5F, -3.5F, 8.0F, 1.0F, 7.0F, 0.0F, false);
        awm.texOffs(196, 86).addBox(12.0F, -19.5F, -3.5F, 6.0F, 1.0F, 7.0F, 0.0F, true);
        awm.texOffs(196, 86).addBox(12.7F, -18.5F, -3.5F, 4.0F, 1.0F, 7.0F, 0.0F, false);
        awm.texOffs(196, 86).addBox(13.2F, -17.5F, -3.5F, 2.0F, 1.0F, 7.0F, 0.0F, true);
        awm.texOffs(196, 86).addBox(32.0F, -26.5F, -3.5F, 8.0F, 6.0F, 7.0F, 0.0F, false);
        awm.texOffs(196, 86).addBox(24.0F, -26.5F, -3.5F, 8.0F, 6.0F, 7.0F, 0.0F, false);
        awm.texOffs(196, 86).addBox(31.0F, -20.5F, -3.5F, 9.0F, 14.0F, 7.0F, 0.0F, false);
        awm.texOffs(196, 86).addBox(40.0F, -26.5F, -3.5F, 9.0F, 20.0F, 7.0F, 0.0F, false);
        awm.texOffs(0, 65).addBox(49.0F, -27.5F, -3.5F, 2.0F, 23.0F, 7.0F, 0.0F, true);
        awm.texOffs(196, 86).addBox(25.0F, -8.5F, -3.5F, 6.0F, 4.0F, 7.0F, 0.0F, false);
        awm.texOffs(196, 86).addBox(19.0F, -8.5F, -3.5F, 6.0F, 4.0F, 7.0F, 0.0F, false);
        awm.texOffs(196, 86).addBox(12.0F, -25.0F, -3.5F, 12.0F, 1.0F, 7.0F, 0.0F, false);
        awm.texOffs(196, 86).addBox(5.0F, -25.0F, -3.5F, 7.0F, 1.0F, 7.0F, 0.0F, false);
        awm.texOffs(196, 86).addBox(2.0F, -25.0F, -3.5F, 3.0F, 1.0F, 7.0F, 0.0F, false);
        awm.texOffs(196, 86).addBox(-20.0F, -29.5F, -3.5F, 5.0F, 5.0F, 7.0F, 0.0F, true);
        awm.texOffs(196, 86).addBox(-25.0F, -29.5F, -3.5F, 5.0F, 5.0F, 7.0F, 0.0F, true);
        awm.texOffs(196, 86).addBox(-34.0F, -26.5F, -3.5F, 9.0F, 4.0F, 7.0F, 0.0F, true);
        awm.texOffs(196, 86).addBox(-43.0F, -26.5F, -3.5F, 9.0F, 4.0F, 7.0F, 0.0F, true);
        awm.texOffs(196, 86).addBox(-47.0F, -26.5F, -3.5F, 4.0F, 4.0F, 7.0F, 0.0F, true);
        awm.texOffs(196, 86).addBox(-54.0F, -26.5F, -3.5F, 7.0F, 3.0F, 7.0F, 0.0F, false);
        awm.texOffs(64, 44).addBox(-56.0F, -25.0F, -1.0F, 31.0F, 1.0F, 2.0F, 0.0F, false);
        awm.texOffs(64, 44).addBox(-87.0F, -25.0F, -1.0F, 31.0F, 1.0F, 2.0F, 0.0F, false);
        awm.texOffs(64, 44).addBox(-105.0F, -25.0F, -1.0F, 18.0F, 1.0F, 2.0F, 0.0F, false);
        awm.texOffs(196, 86).addBox(-77.0F, -27.9F, -1.5F, 1.0F, 3.0F, 3.0F, 0.0F, false);
        awm.texOffs(64, 44).addBox(-56.0F, -28.8284F, -1.0F, 31.0F, 1.0F, 2.0F, 0.0F, false);
        awm.texOffs(64, 44).addBox(-87.0F, -28.8284F, -1.0F, 31.0F, 1.0F, 2.0F, 0.0F, false);
        awm.texOffs(64, 44).addBox(-105.0F, -28.8284F, -1.0F, 18.0F, 1.0F, 2.0F, 0.0F, false);
        awm.texOffs(64, 44).addBox(-56.0F, -27.4142F, 1.4142F, 31.0F, 2.0F, 1.0F, 0.0F, false);
        awm.texOffs(64, 44).addBox(-87.0F, -27.4142F, 1.4142F, 31.0F, 2.0F, 1.0F, 0.0F, false);
        awm.texOffs(64, 44).addBox(-105.0F, -27.4142F, 1.4142F, 18.0F, 2.0F, 1.0F, 0.0F, false);
        awm.texOffs(64, 44).addBox(-56.0F, -27.4142F, -2.4142F, 31.0F, 2.0F, 1.0F, 0.0F, false);
        awm.texOffs(64, 44).addBox(-87.0F, -27.4142F, -2.4142F, 31.0F, 2.0F, 1.0F, 0.0F, false);
        awm.texOffs(64, 44).addBox(-105.0F, -27.4142F, -2.4142F, 18.0F, 2.0F, 1.0F, 0.0F, false);
        awm.texOffs(196, 86).addBox(-15.0F, -29.5F, -3.5F, 9.0F, 5.0F, 1.0F, 0.0F, true);
        awm.texOffs(196, 86).addBox(-11.0F, -30.2071F, -2.7929F, 13.0F, 1.0F, 3.0F, 0.0F, false);
        awm.texOffs(76, 85).addBox(0.0F, -32.0071F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);
        awm.texOffs(76, 85).addBox(-2.0F, -32.0071F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);
        awm.texOffs(76, 85).addBox(-4.0F, -32.0071F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);
        awm.texOffs(76, 85).addBox(-6.0F, -32.0071F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);
        awm.texOffs(76, 85).addBox(-8.0F, -32.0071F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);
        awm.texOffs(76, 85).addBox(-10.0F, -32.0071F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);
        awm.texOffs(76, 85).addBox(-12.0F, -32.0071F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);
        awm.texOffs(76, 85).addBox(-14.0F, -32.0071F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);
        awm.texOffs(76, 85).addBox(-16.0F, -32.0071F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);
        awm.texOffs(76, 85).addBox(-18.0F, -32.0071F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);
        awm.texOffs(76, 85).addBox(-20.0F, -32.0071F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);
        awm.texOffs(76, 85).addBox(-22.0F, -32.0071F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);
        awm.texOffs(76, 85).addBox(-24.0F, -32.0071F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);
        awm.texOffs(69, 93).addBox(-23.0F, -31.5071F, -1.866F, 1.0F, 0.0F, 2.0F, 0.0F, true);
        awm.texOffs(69, 93).addBox(-19.0F, -31.5071F, -1.866F, 1.0F, 0.0F, 2.0F, 0.0F, true);
        awm.texOffs(69, 93).addBox(-15.0F, -31.5071F, -1.866F, 1.0F, 0.0F, 2.0F, 0.0F, true);
        awm.texOffs(69, 93).addBox(-11.0F, -31.5071F, -1.866F, 1.0F, 0.0F, 2.0F, 0.0F, true);
        awm.texOffs(69, 93).addBox(-7.0F, -31.5071F, -1.866F, 1.0F, 0.0F, 2.0F, 0.0F, true);
        awm.texOffs(69, 93).addBox(-3.0F, -31.5071F, -1.866F, 1.0F, 0.0F, 2.0F, 0.0F, true);
        awm.texOffs(69, 93).addBox(-21.0F, -31.5071F, -1.866F, 1.0F, 0.0F, 2.0F, 0.0F, true);
        awm.texOffs(69, 93).addBox(-17.0F, -31.5071F, -1.866F, 1.0F, 0.0F, 2.0F, 0.0F, true);
        awm.texOffs(69, 93).addBox(-13.0F, -31.5071F, -1.866F, 1.0F, 0.0F, 2.0F, 0.0F, true);
        awm.texOffs(69, 93).addBox(-9.0F, -31.5071F, -1.866F, 1.0F, 0.0F, 2.0F, 0.0F, true);
        awm.texOffs(69, 93).addBox(-5.0F, -31.5071F, -1.866F, 1.0F, 0.0F, 2.0F, 0.0F, true);
        awm.texOffs(69, 93).addBox(-1.0F, -31.5071F, -1.866F, 1.0F, 0.0F, 2.0F, 0.0F, true);
        awm.texOffs(69, 93).addBox(-23.0F, -31.5071F, -0.134F, 1.0F, 0.0F, 2.0F, 0.0F, false);
        awm.texOffs(69, 93).addBox(-19.0F, -31.5071F, -0.134F, 1.0F, 0.0F, 2.0F, 0.0F, false);
        awm.texOffs(69, 93).addBox(-15.0F, -31.5071F, -0.134F, 1.0F, 0.0F, 2.0F, 0.0F, false);
        awm.texOffs(69, 93).addBox(-11.0F, -31.5071F, -0.134F, 1.0F, 0.0F, 2.0F, 0.0F, false);
        awm.texOffs(69, 93).addBox(-7.0F, -31.5071F, -0.134F, 1.0F, 0.0F, 2.0F, 0.0F, false);
        awm.texOffs(69, 93).addBox(-3.0F, -31.5071F, -0.134F, 1.0F, 0.0F, 2.0F, 0.0F, false);
        awm.texOffs(69, 93).addBox(-21.0F, -31.5071F, -0.134F, 1.0F, 0.0F, 2.0F, 0.0F, false);
        awm.texOffs(69, 93).addBox(-17.0F, -31.5071F, -0.134F, 1.0F, 0.0F, 2.0F, 0.0F, false);
        awm.texOffs(69, 93).addBox(-13.0F, -31.5071F, -0.134F, 1.0F, 0.0F, 2.0F, 0.0F, false);
        awm.texOffs(69, 93).addBox(-9.0F, -31.5071F, -0.134F, 1.0F, 0.0F, 2.0F, 0.0F, false);
        awm.texOffs(69, 93).addBox(-5.0F, -31.5071F, -0.134F, 1.0F, 0.0F, 2.0F, 0.0F, false);
        awm.texOffs(69, 93).addBox(-1.0F, -31.5071F, -0.134F, 1.0F, 0.0F, 2.0F, 0.0F, false);
        awm.texOffs(69, 88).addBox(-8.0F, -31.0071F, -1.5F, 9.0F, 1.0F, 3.0F, 0.0F, true);
        awm.texOffs(69, 88).addBox(-16.0F, -31.0071F, -1.5F, 8.0F, 1.0F, 3.0F, 0.0F, true);
        awm.texOffs(69, 88).addBox(-24.0F, -31.0071F, -1.5F, 8.0F, 1.0F, 3.0F, 0.0F, true);
        awm.texOffs(196, 86).addBox(-25.0F, -30.2071F, -2.7929F, 14.0F, 1.0F, 3.0F, 0.0F, true);
        awm.texOffs(196, 86).addBox(-11.0F, -30.2071F, -0.2071F, 13.0F, 1.0F, 3.0F, 0.0F, false);
        awm.texOffs(196, 86).addBox(-25.0F, -30.2071F, -0.2071F, 14.0F, 1.0F, 3.0F, 0.0F, true);
        awm.texOffs(196, 86).addBox(-15.0F, -29.5F, 0.5F, 9.0F, 1.0F, 3.0F, 0.0F, true);
        awm.texOffs(196, 86).addBox(-15.0F, -25.5F, 0.5F, 9.0F, 1.0F, 3.0F, 0.0F, true);

        bone10 = new ModelRenderer(this);
        bone10.setPos(-27.5F, -22.5F, 0.0F);
        awm.addChild(bone10);
        setRotationAngle(bone10, 0.0F, 0.0F, 0.1047F);
        bone10.texOffs(196, 86).addBox(-5.3046F, -0.2723F, -3.5F, 8.0F, 2.0F, 7.0F, 0.0F, true);
        bone10.texOffs(196, 86).addBox(-13.3046F, -0.2723F, -3.5F, 8.0F, 2.0F, 7.0F, 0.0F, true);
        bone10.texOffs(196, 86).addBox(-17.3046F, -0.2723F, -3.5F, 4.0F, 2.0F, 7.0F, 0.0F, true);

        bone33 = new ModelRenderer(this);
        bone33.setPos(0.3F, -13.0F, 0.0F);
        awm.addChild(bone33);
        setRotationAngle(bone33, 0.0F, 0.0F, -0.7854F);


        bone28 = new ModelRenderer(this);
        bone28.setPos(-15.5F, -19.0F, 0.0F);
        awm.addChild(bone28);
        setRotationAngle(bone28, 0.0F, 0.0F, 0.3491F);
        bone28.texOffs(196, 86).addBox(-0.3187F, -3.4716F, -3.5F, 6.0F, 3.0F, 1.0F, 0.0F, true);
        bone28.texOffs(196, 86).addBox(-3.3187F, -3.4716F, -3.5F, 3.0F, 3.0F, 7.0F, 0.0F, true);
        bone28.texOffs(196, 86).addBox(-33.7974F, 6.4847F, -3.5F, 3.0F, 1.0F, 7.0F, 0.0F, true);
        bone28.texOffs(196, 86).addBox(-0.3187F, -3.4716F, 2.5F, 6.0F, 3.0F, 1.0F, 0.0F, false);

        bone30 = new ModelRenderer(this);
        bone30.setPos(-63.0F, -24.5F, 0.0F);
        awm.addChild(bone30);
        setRotationAngle(bone30, -0.7854F, 0.0F, 0.0F);
        bone30.texOffs(64, 44).addBox(7.0F, 0.0607F, -2.3536F, 31.0F, 1.0F, 2.0F, 0.0F, false);
        bone30.texOffs(64, 44).addBox(-24.0F, 0.0607F, -2.3536F, 31.0F, 1.0F, 2.0F, 0.0F, false);
        bone30.texOffs(64, 44).addBox(-42.0F, 0.0607F, -2.3536F, 18.0F, 1.0F, 2.0F, 0.0F, false);
        bone30.texOffs(64, 44).addBox(7.0F, -2.3536F, 0.0607F, 31.0F, 2.0F, 1.0F, 0.0F, false);
        bone30.texOffs(64, 44).addBox(-24.0F, -2.3536F, 0.0607F, 31.0F, 2.0F, 1.0F, 0.0F, false);
        bone30.texOffs(64, 44).addBox(-42.0F, -2.3536F, 0.0607F, 18.0F, 2.0F, 1.0F, 0.0F, false);
        bone30.texOffs(64, 44).addBox(7.0F, -3.7678F, -2.3536F, 31.0F, 1.0F, 2.0F, 0.0F, false);
        bone30.texOffs(64, 44).addBox(-24.0F, -3.7678F, -2.3536F, 31.0F, 1.0F, 2.0F, 0.0F, false);
        bone30.texOffs(64, 44).addBox(-42.0F, -3.7678F, -2.3536F, 18.0F, 1.0F, 2.0F, 0.0F, false);
        bone30.texOffs(64, 44).addBox(7.0F, -2.3536F, -3.7678F, 31.0F, 2.0F, 1.0F, 0.0F, false);
        bone30.texOffs(64, 44).addBox(-24.0F, -2.3536F, -3.7678F, 31.0F, 2.0F, 1.0F, 0.0F, false);
        bone30.texOffs(64, 44).addBox(-42.0F, -2.3536F, -3.7678F, 18.0F, 2.0F, 1.0F, 0.0F, false);

        bone31 = new ModelRenderer(this);
        bone31.setPos(1.5F, -32.7071F, -4.0F);
        awm.addChild(bone31);
        setRotationAngle(bone31, -0.5236F, 0.0F, 0.0F);
        bone31.texOffs(69, 93).addBox(-1.5F, -1.8938F, 4.6801F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone31.texOffs(69, 93).addBox(-3.5F, -1.8938F, 4.6801F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone31.texOffs(69, 93).addBox(-5.5F, -1.8938F, 4.6801F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone31.texOffs(69, 93).addBox(-7.5F, -1.8938F, 4.6801F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone31.texOffs(69, 93).addBox(-9.5F, -1.8938F, 4.6801F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone31.texOffs(69, 93).addBox(-11.5F, -1.8938F, 4.6801F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone31.texOffs(69, 93).addBox(-13.5F, -1.8938F, 4.6801F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone31.texOffs(69, 93).addBox(-15.5F, -1.8938F, 4.6801F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone31.texOffs(69, 93).addBox(-17.5F, -1.8938F, 4.6801F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone31.texOffs(69, 93).addBox(-19.5F, -1.8938F, 4.6801F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone31.texOffs(69, 93).addBox(-21.5F, -1.8938F, 4.6801F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone31.texOffs(69, 93).addBox(-23.5F, -1.8938F, 4.6801F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone31.texOffs(69, 93).addBox(-25.5F, -1.8938F, 4.6801F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone31.texOffs(69, 93).addBox(-24.5F, -1.8938F, 5.6801F, 1.0F, 1.0F, 0.0F, 0.0F, true);
        bone31.texOffs(69, 93).addBox(-20.5F, -1.8938F, 5.6801F, 1.0F, 1.0F, 0.0F, 0.0F, true);
        bone31.texOffs(69, 93).addBox(-16.5F, -1.8938F, 5.6801F, 1.0F, 1.0F, 0.0F, 0.0F, true);
        bone31.texOffs(69, 93).addBox(-12.5F, -1.8938F, 5.6801F, 1.0F, 1.0F, 0.0F, 0.0F, true);
        bone31.texOffs(69, 93).addBox(-8.5F, -1.8938F, 5.6801F, 1.0F, 1.0F, 0.0F, 0.0F, true);
        bone31.texOffs(69, 93).addBox(-4.5F, -1.8938F, 5.6801F, 1.0F, 1.0F, 0.0F, 0.0F, true);
        bone31.texOffs(69, 93).addBox(-22.5F, -1.8938F, 5.6801F, 1.0F, 1.0F, 0.0F, 0.0F, true);
        bone31.texOffs(69, 93).addBox(-18.5F, -1.8938F, 5.6801F, 1.0F, 1.0F, 0.0F, 0.0F, true);
        bone31.texOffs(69, 93).addBox(-14.5F, -1.8938F, 5.6801F, 1.0F, 1.0F, 0.0F, 0.0F, true);
        bone31.texOffs(69, 93).addBox(-10.5F, -1.8938F, 5.6801F, 1.0F, 1.0F, 0.0F, 0.0F, true);
        bone31.texOffs(69, 93).addBox(-6.5F, -1.8938F, 5.6801F, 1.0F, 1.0F, 0.0F, 0.0F, true);
        bone31.texOffs(69, 93).addBox(-2.5F, -1.8938F, 5.6801F, 1.0F, 1.0F, 0.0F, 0.0F, true);

        bone32 = new ModelRenderer(this);
        bone32.setPos(0.5F, -35.3901F, 1.0131F);
        awm.addChild(bone32);
        setRotationAngle(bone32, 0.5236F, 0.0F, 0.0F);
        bone32.texOffs(86, 88).addBox(-0.5F, 1.9232F, -4.4349F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone32.texOffs(86, 88).addBox(-2.5F, 1.9232F, -4.4349F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone32.texOffs(86, 88).addBox(-4.5F, 1.9232F, -4.4349F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone32.texOffs(86, 88).addBox(-6.5F, 1.9232F, -4.4349F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone32.texOffs(86, 88).addBox(-8.5F, 1.9232F, -4.4349F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone32.texOffs(86, 88).addBox(-10.5F, 1.9232F, -4.4349F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone32.texOffs(86, 88).addBox(-12.5F, 1.9232F, -4.4349F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone32.texOffs(86, 88).addBox(-14.5F, 1.9232F, -4.4349F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone32.texOffs(86, 88).addBox(-16.5F, 1.9232F, -4.4349F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone32.texOffs(86, 88).addBox(-18.5F, 1.9232F, -4.4349F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone32.texOffs(86, 88).addBox(-20.5F, 1.9232F, -4.4349F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone32.texOffs(86, 88).addBox(-22.5F, 1.9232F, -4.4349F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone32.texOffs(86, 88).addBox(-24.5F, 1.9232F, -4.4349F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone32.texOffs(86, 88).addBox(-23.5F, 1.9232F, -4.4349F, 1.0F, 1.0F, 0.0F, 0.0F, true);
        bone32.texOffs(86, 88).addBox(-19.5F, 1.9232F, -4.4349F, 1.0F, 1.0F, 0.0F, 0.0F, true);
        bone32.texOffs(86, 88).addBox(-15.5F, 1.9232F, -4.4349F, 1.0F, 1.0F, 0.0F, 0.0F, true);
        bone32.texOffs(86, 88).addBox(-11.5F, 1.9232F, -4.4349F, 1.0F, 1.0F, 0.0F, 0.0F, true);
        bone32.texOffs(86, 88).addBox(-7.5F, 1.9232F, -4.4349F, 1.0F, 1.0F, 0.0F, 0.0F, true);
        bone32.texOffs(86, 88).addBox(-3.5F, 1.9232F, -4.4349F, 1.0F, 1.0F, 0.0F, 0.0F, true);
        bone32.texOffs(86, 88).addBox(-21.5F, 1.9232F, -4.4349F, 1.0F, 1.0F, 0.0F, 0.0F, true);
        bone32.texOffs(86, 88).addBox(-17.5F, 1.9232F, -4.4349F, 1.0F, 1.0F, 0.0F, 0.0F, true);
        bone32.texOffs(86, 88).addBox(-13.5F, 1.9232F, -4.4349F, 1.0F, 1.0F, 0.0F, 0.0F, true);
        bone32.texOffs(86, 88).addBox(-9.5F, 1.9232F, -4.4349F, 1.0F, 1.0F, 0.0F, 0.0F, true);
        bone32.texOffs(86, 88).addBox(-5.5F, 1.9232F, -4.4349F, 1.0F, 1.0F, 0.0F, 0.0F, true);
        bone32.texOffs(86, 88).addBox(-1.5F, 1.9232F, -4.4349F, 1.0F, 1.0F, 0.0F, 0.0F, true);

        bone11 = new ModelRenderer(this);
        bone11.setPos(-10.5F, -7.0F, 2.0F);
        awm.addChild(bone11);
        setRotationAngle(bone11, -0.7854F, 0.0F, 0.0F);
        bone11.texOffs(196, 86).addBox(-0.5F, -16.9706F, -15.8492F, 13.0F, 1.0F, 1.0F, 0.0F, true);
        bone11.texOffs(196, 86).addBox(-0.5F, -13.0208F, -19.799F, 13.0F, 1.0F, 1.0F, 0.0F, true);
        bone11.texOffs(196, 86).addBox(-14.5F, -16.9706F, -15.8492F, 14.0F, 1.0F, 1.0F, 0.0F, true);
        bone11.texOffs(196, 86).addBox(-14.5F, -13.0208F, -19.799F, 14.0F, 1.0F, 1.0F, 0.0F, true);

        grip = new ModelRenderer(this);
        grip.setPos(7.0F, 0.0F, 0.0F);
        awm.addChild(grip);


        bone21 = new ModelRenderer(this);
        bone21.setPos(4.0F, -11.6964F, 0.0F);
        grip.addChild(bone21);
        setRotationAngle(bone21, 0.0F, 0.0F, -2.0944F);
        bone21.texOffs(196, 86).addBox(-9.645F, -2.0146F, -3.5F, 9.0F, 7.0F, 7.0F, 0.0F, true);
        bone21.texOffs(196, 86).addBox(-0.645F, -2.0146F, -3.5F, 9.0F, 7.0F, 7.0F, 0.0F, true);
        bone21.texOffs(196, 86).addBox(-7.645F, 4.6925F, -0.2071F, 9.0F, 1.0F, 3.0F, 0.0F, false);
        bone21.texOffs(196, 86).addBox(-9.645F, -2.7217F, -0.2071F, 18.0F, 1.0F, 3.0F, 0.0F, false);
        bone21.texOffs(196, 86).addBox(-7.645F, 4.6925F, -2.7929F, 9.0F, 1.0F, 3.0F, 0.0F, false);
        bone21.texOffs(196, 86).addBox(-9.645F, -2.7217F, -2.7929F, 18.0F, 1.0F, 3.0F, 0.0F, false);

        bone27 = new ModelRenderer(this);
        bone27.setPos(-4.0F, 11.6964F, 0.0F);
        bone21.addChild(bone27);
        setRotationAngle(bone27, 0.0F, 0.0F, 0.1745F);
        bone27.texOffs(196, 86).addBox(-7.9401F, -12.5225F, -3.5F, 2.0F, 7.0F, 7.0F, 0.0F, false);

        bone22 = new ModelRenderer(this);
        bone22.setPos(0.0F, -3.3036F, 0.0F);
        bone21.addChild(bone22);
        setRotationAngle(bone22, 0.7854F, 0.0F, 0.0F);
        bone22.texOffs(196, 86).addBox(-7.645F, 7.3361F, -4.3863F, 14.0F, 1.0F, 1.0F, 0.0F, false);
        bone22.texOffs(196, 86).addBox(-9.645F, 2.3863F, 0.5634F, 12.0F, 1.0F, 1.0F, 0.0F, false);
        bone22.texOffs(196, 86).addBox(-9.645F, -1.5634F, -3.3863F, 12.0F, 1.0F, 1.0F, 0.0F, false);
        bone22.texOffs(196, 86).addBox(2.355F, 2.3863F, 0.5634F, 6.0F, 1.0F, 1.0F, 0.0F, false);
        bone22.texOffs(196, 86).addBox(2.355F, -1.5634F, -3.3863F, 6.0F, 1.0F, 1.0F, 0.0F, false);
        bone22.texOffs(196, 86).addBox(-7.645F, 3.3863F, -8.3361F, 14.0F, 1.0F, 1.0F, 0.0F, false);

        bone13 = new ModelRenderer(this);
        bone13.setPos(12.0F, -2.6964F, 0.0F);
        grip.addChild(bone13);
        setRotationAngle(bone13, 0.0F, 0.0F, -2.8798F);
        bone13.texOffs(196, 86).addBox(-3.4666F, 4.3832F, -3.5F, 7.0F, 2.0F, 7.0F, 0.0F, false);
        bone13.texOffs(196, 86).addBox(-3.4666F, 6.0903F, -0.2071F, 7.0F, 1.0F, 3.0F, 0.0F, false);
        bone13.texOffs(196, 86).addBox(-3.4666F, 6.0903F, -2.7929F, 7.0F, 1.0F, 3.0F, 0.0F, false);

        bone14 = new ModelRenderer(this);
        bone14.setPos(0.0F, -3.3036F, 0.0F);
        bone13.addChild(bone14);
        setRotationAngle(bone14, 0.7854F, 0.0F, 0.0F);
        bone14.texOffs(196, 86).addBox(-3.4666F, 8.3244F, -5.3747F, 7.0F, 1.0F, 1.0F, 0.0F, false);
        bone14.texOffs(196, 86).addBox(-3.4666F, 4.3747F, -9.3244F, 7.0F, 1.0F, 1.0F, 0.0F, false);

        bone15 = new ModelRenderer(this);
        bone15.setPos(20.3F, -3.1964F, 0.0F);
        grip.addChild(bone15);
        setRotationAngle(bone15, 0.0F, 0.0F, 2.618F);
        bone15.texOffs(196, 86).addBox(-5.7431F, 3.0835F, -3.5F, 7.0F, 3.0F, 7.0F, 0.0F, false);
        bone15.texOffs(196, 86).addBox(-10.8561F, -0.7211F, -3.5F, 7.0F, 6.0F, 7.0F, 0.0F, false);
        bone15.texOffs(196, 86).addBox(-5.7431F, 5.7906F, -0.2071F, 7.0F, 1.0F, 3.0F, 0.0F, false);
        bone15.texOffs(196, 86).addBox(-5.7431F, 5.7906F, -2.7929F, 7.0F, 1.0F, 3.0F, 0.0F, false);

        bone16 = new ModelRenderer(this);
        bone16.setPos(0.0F, -3.3036F, 0.0F);
        bone15.addChild(bone16);
        setRotationAngle(bone16, 0.7854F, 0.0F, 0.0F);
        bone16.texOffs(196, 86).addBox(-5.7431F, 8.1125F, -5.1628F, 7.0F, 1.0F, 1.0F, 0.0F, false);
        bone16.texOffs(196, 86).addBox(-5.7431F, 4.1628F, -9.1125F, 7.0F, 1.0F, 1.0F, 0.0F, false);

        bone17 = new ModelRenderer(this);
        bone17.setPos(25.8F, -8.9964F, 0.0F);
        grip.addChild(bone17);
        setRotationAngle(bone17, 0.0F, 0.0F, 1.8326F);
        bone17.texOffs(196, 86).addBox(-7.1409F, 2.2619F, -3.5F, 7.0F, 2.0F, 7.0F, 0.0F, false);
        bone17.texOffs(196, 86).addBox(-7.1409F, 3.969F, -0.2071F, 7.0F, 1.0F, 3.0F, 0.0F, false);
        bone17.texOffs(196, 86).addBox(-14.6296F, 12.0304F, -3.5F, 2.0F, 1.0F, 7.0F, 0.0F, true);
        bone17.texOffs(196, 86).addBox(-7.1409F, 3.969F, -2.7929F, 7.0F, 1.0F, 3.0F, 0.0F, false);

        bone18 = new ModelRenderer(this);
        bone18.setPos(0.0F, -3.3036F, 0.0F);
        bone17.addChild(bone18);
        setRotationAngle(bone18, 0.7854F, 0.0F, 0.0F);
        bone18.texOffs(196, 86).addBox(-7.1409F, 6.8244F, -3.8747F, 7.0F, 1.0F, 1.0F, 0.0F, false);
        bone18.texOffs(196, 86).addBox(-7.1409F, 2.8747F, -7.8244F, 7.0F, 1.0F, 1.0F, 0.0F, false);

        bone19 = new ModelRenderer(this);
        bone19.setPos(24.3F, -16.9964F, 0.0F);
        grip.addChild(bone19);
        setRotationAngle(bone19, 0.0F, 0.0F, 0.7854F);
        bone19.texOffs(196, 86).addBox(-4.5644F, -2.6359F, -3.5F, 4.0F, 2.0F, 7.0F, 0.0F, false);
        bone19.texOffs(196, 86).addBox(-6.3644F, -0.6359F, -3.5F, 7.0F, 2.0F, 7.0F, 0.0F, false);
        bone19.texOffs(196, 86).addBox(-6.3644F, 1.0712F, -0.2071F, 7.0F, 1.0F, 3.0F, 0.0F, false);
        bone19.texOffs(196, 86).addBox(-6.3644F, 1.0712F, -2.7929F, 7.0F, 1.0F, 3.0F, 0.0F, false);

        bone20 = new ModelRenderer(this);
        bone20.setPos(0.0F, -3.3036F, 0.0F);
        bone19.addChild(bone20);
        setRotationAngle(bone20, 0.7854F, 0.0F, 0.0F);
        bone20.texOffs(196, 86).addBox(-6.3644F, 4.7754F, -1.8257F, 7.0F, 1.0F, 1.0F, 0.0F, false);
        bone20.texOffs(196, 86).addBox(-6.3644F, 0.8257F, -5.7754F, 7.0F, 1.0F, 1.0F, 0.0F, false);

        bone23 = new ModelRenderer(this);
        bone23.setPos(16.7F, -20.3964F, 0.0F);
        grip.addChild(bone23);
        setRotationAngle(bone23, 0.0F, 0.0F, -0.0873F);
        bone23.texOffs(196, 86).addBox(-4.9816F, -0.5032F, -3.5F, 8.0F, 1.0F, 7.0F, 0.0F, false);
        bone23.texOffs(196, 86).addBox(-4.9816F, 0.2039F, -0.2071F, 8.0F, 1.0F, 3.0F, 0.0F, false);
        bone23.texOffs(196, 86).addBox(-4.9816F, 0.2039F, -2.7929F, 8.0F, 1.0F, 3.0F, 0.0F, false);

        bone24 = new ModelRenderer(this);
        bone24.setPos(0.0F, -3.3036F, 0.0F);
        bone23.addChild(bone24);
        setRotationAngle(bone24, 0.7854F, 0.0F, 0.0F);
        bone24.texOffs(196, 86).addBox(-4.9816F, 4.1622F, -1.2124F, 8.0F, 1.0F, 1.0F, 0.0F, false);
        bone24.texOffs(196, 86).addBox(-4.9816F, 0.2124F, -5.1622F, 8.0F, 1.0F, 1.0F, 0.0F, false);

        bone25 = new ModelRenderer(this);
        bone25.setPos(9.1F, -18.1964F, 0.0F);
        grip.addChild(bone25);
        setRotationAngle(bone25, 0.0F, 0.0F, -0.6109F);
        bone25.texOffs(196, 86).addBox(-2.8223F, 0.028F, -3.5F, 7.0F, 1.0F, 7.0F, 0.0F, true);
        bone25.texOffs(196, 86).addBox(-3.5223F, 0.7351F, -0.2071F, 8.0F, 1.0F, 3.0F, 0.0F, false);
        bone25.texOffs(196, 86).addBox(-3.5223F, 0.7351F, -2.7929F, 8.0F, 1.0F, 3.0F, 0.0F, false);

        bone26 = new ModelRenderer(this);
        bone26.setPos(0.0F, -3.3036F, 0.0F);
        bone25.addChild(bone26);
        setRotationAngle(bone26, 0.7854F, 0.0F, 0.0F);
        bone26.texOffs(196, 86).addBox(-3.5223F, 4.5377F, -1.588F, 8.0F, 1.0F, 1.0F, 0.0F, false);
        bone26.texOffs(196, 86).addBox(-3.5223F, 0.588F, -5.5377F, 8.0F, 1.0F, 1.0F, 0.0F, false);

        magazine = new ModelRenderer(this);
        magazine.setPos(-6.6929F, 8.6071F, 0.0F);
        setRotationAngle(magazine, 0.0F, 0.0F, 0.0873F);
        magazine.texOffs(103, 36).addBox(0.0929F, 4.6929F, -2.5F, 5.0F, 1.0F, 5.0F, 0.0F, true);
        magazine.texOffs(81, 12).addBox(-2.9071F, 3.6929F, -2.5F, 8.0F, 1.0F, 5.0F, 0.0F, true);
        magazine.texOffs(74, 40).addBox(-5.9071F, 2.6929F, -2.5F, 11.0F, 1.0F, 5.0F, 0.0F, true);
        magazine.texOffs(80, 22).addBox(-8.9071F, -6.3071F, -2.5F, 14.0F, 9.0F, 5.0F, 0.0F, true);
        magazine.texOffs(78, 30).addBox(-8.9071F, -10.3071F, -2.5F, 14.0F, 4.0F, 1.0F, 0.0F, true);
        magazine.texOffs(79, 26).addBox(4.0929F, -9.3071F, -1.5F, 1.0F, 3.0F, 3.0F, 0.0F, true);
        magazine.texOffs(79, 26).addBox(-8.9071F, -10.3071F, 1.5F, 14.0F, 4.0F, 1.0F, 0.0F, true);
        magazine.texOffs(82, 28).addBox(-8.9071F, -8.3071F, -1.5F, 1.0F, 2.0F, 3.0F, 0.0F, true);

        bullet = new ModelRenderer(this);
        bullet.setPos(-6.6929F, 8.6071F, 0.0F);
        setRotationAngle(bullet, 0.0F, 0.0F, 0.0873F);


        bullet2 = new ModelRenderer(this);
        bullet2.setPos(6.6929F, 10.3929F, 0.0F);
        bullet.addChild(bullet2);
        bullet2.texOffs(0, 491).addBox(-13.6F, -21.9F, -1.0F, 10.0F, 2.0F, 2.0F, 0.0F, true);
        bullet2.texOffs(0, 491).addBox(-14.2F, -21.3F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bullet2.texOffs(0, 491).addBox(-11.6F, -22.4F, -1.5F, 7.0F, 3.0F, 3.0F, 0.0F, true);
        bullet2.texOffs(0, 491).addBox(-3.6F, -22.4F, -1.5F, 1.0F, 3.0F, 3.0F, 0.0F, true);

        ironsights = new ModelRenderer(this);
        ironsights.setPos(0.0F, 24.0F, 1.2929F);
        ironsights.texOffs(2, 79).addBox(-2.6F, -32.0071F, -3.2929F, 2.0F, 1.0F, 4.0F, 0.0F, true);
        ironsights.texOffs(2, 79).addBox(-22.6F, -32.0071F, -3.2929F, 2.0F, 1.0F, 4.0F, 0.0F, true);
        ironsights.texOffs(2, 79).addBox(-2.6F, -33.0071F, -0.2929F, 2.0F, 1.0F, 1.0F, 0.0F, true);
        ironsights.texOffs(2, 79).addBox(-22.6F, -33.0071F, -0.2929F, 2.0F, 1.0F, 1.0F, 0.0F, true);
        ironsights.texOffs(2, 79).addBox(-2.6F, -33.0071F, -3.2929F, 2.0F, 1.0F, 1.0F, 0.0F, true);
        ironsights.texOffs(2, 79).addBox(-22.6F, -33.0071F, -3.2929F, 2.0F, 1.0F, 1.0F, 0.0F, true);
        ironsights.texOffs(2, 79).addBox(-2.1F, -32.9071F, -2.7929F, 1.0F, 1.0F, 3.0F, 0.0F, true);
        ironsights.texOffs(2, 79).addBox(-22.1F, -32.9071F, -2.7929F, 1.0F, 1.0F, 3.0F, 0.0F, true);
        ironsights.texOffs(2, 79).addBox(-2.1F, -33.9071F, -1.7929F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        ironsights.texOffs(2, 79).addBox(-22.1F, -33.9071F, -1.7929F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        ironsights.texOffs(2, 79).addBox(-2.1F, -36.6392F, -1.7929F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        ironsights.texOffs(2, 79).addBox(-2.1F, -35.2731F, -3.1589F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        ironsights.texOffs(2, 79).addBox(-2.1F, -35.2731F, -0.4269F, 1.0F, 1.0F, 1.0F, 0.0F, true);

        bone38 = new ModelRenderer(this);
        bone38.setPos(-21.6F, -33.5071F, -2.7929F);
        ironsights.addChild(bone38);
        setRotationAngle(bone38, -0.2618F, 0.0F, 0.0F);
        bone38.texOffs(2, 79).addBox(-1.0F, -2.1641F, 2.5442F, 2.0F, 2.0F, 1.0F, 0.0F, true);

        bone39 = new ModelRenderer(this);
        bone39.setPos(-21.6F, -33.5071F, 0.2071F);
        ironsights.addChild(bone39);
        setRotationAngle(bone39, 0.2618F, 0.0F, 0.0F);
        bone39.texOffs(2, 79).addBox(-1.0F, -2.1641F, -3.5442F, 2.0F, 2.0F, 1.0F, 0.0F, true);

        bone36 = new ModelRenderer(this);
        bone36.setPos(-1.6F, -33.4071F, -1.2929F);
        ironsights.addChild(bone36);
        setRotationAngle(bone36, -0.5236F, 0.0F, 0.0F);
        bone36.texOffs(2, 79).addBox(-0.5F, -0.317F, -1.183F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone36.texOffs(2, 79).addBox(-0.5F, -3.049F, -1.183F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone36.texOffs(2, 79).addBox(-0.5F, -1.683F, 0.183F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone36.texOffs(2, 79).addBox(-0.5F, -1.683F, -2.549F, 1.0F, 1.0F, 1.0F, 0.0F, true);

        bone37 = new ModelRenderer(this);
        bone37.setPos(-1.6F, -33.4071F, -1.2929F);
        ironsights.addChild(bone37);
        setRotationAngle(bone37, 0.5236F, 0.0F, 0.0F);
        bone37.texOffs(2, 79).addBox(-0.5F, -0.317F, 0.183F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone37.texOffs(2, 79).addBox(-0.5F, -3.049F, 0.183F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone37.texOffs(2, 79).addBox(-0.5F, -1.683F, -1.183F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone37.texOffs(2, 79).addBox(-0.5F, -1.683F, 1.549F, 1.0F, 1.0F, 1.0F, 0.0F, true);

        bolt_case = new ModelRenderer(this);
        bolt_case.setPos(3.0F, 24.0F, 0.0F);


        bone = new ModelRenderer(this);
        bone.setPos(0.0F, 0.0F, 0.0F);
        bolt_case.addChild(bone);
        bone.texOffs(36, 11).addBox(-1.0F, -29.0F, -3.0F, 6.0F, 4.0F, 6.0F, 0.0F, false);
        bone.texOffs(0, 0).addBox(-15.0F, -28.8F, -1.499F, 14.0F, 3.0F, 3.0F, 0.0F, false);
        bone.texOffs(100, 157).addBox(-18.001F, -28.5F, 2.0F, 9.0F, 3.0F, 1.0F, 0.0F, false);
        bone.texOffs(38, 10).addBox(6.0F, -28.0F, 2.0F, 2.0F, 3.0F, 1.0F, 0.0F, false);
        bone.texOffs(38, 10).addBox(5.0F, -28.0F, -3.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        bone.texOffs(46, 5).addBox(5.0F, -29.0F, -3.0F, 3.0F, 1.0F, 6.0F, 0.0F, false);
        bone.texOffs(46, 12).addBox(8.0F, -29.0F, -2.0F, 1.0F, 4.0F, 4.0F, 0.0F, false);
        bone.texOffs(75, 58).addBox(8.1914F, -28.0F, -1.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        bone.texOffs(34, 12).addBox(-1.0F, -30.0F, -2.0F, 9.0F, 1.0F, 4.0F, 0.0F, false);

        bone12 = new ModelRenderer(this);
        bone12.setPos(-12.5F, -4.5F, 3.5F);
        bone.addChild(bone12);
        setRotationAngle(bone12, 0.0F, 0.2618F, 0.0F);
        bone12.texOffs(45, 111).addBox(-4.0394F, -23.5F, -2.2279F, 3.0F, 2.0F, 1.0F, 0.0F, false);

        bone4 = new ModelRenderer(this);
        bone4.setPos(11.5F, -6.5F, 0.0F);
        bone.addChild(bone4);
        setRotationAngle(bone4, 0.0F, 0.0F, -0.7854F);
        bone4.texOffs(42, 15).addBox(13.1422F, -19.0919F, -2.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        bone4.texOffs(42, 15).addBox(13.1422F, -18.6776F, -2.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);

        bone2 = new ModelRenderer(this);
        bone2.setPos(11.5F, -4.0F, -2.5F);
        bone.addChild(bone2);
        setRotationAngle(bone2, 0.0F, 0.7854F, 0.0F);
        bone2.texOffs(42, 15).addBox(-3.1213F, -25.0F, -2.4142F, 1.0F, 4.0F, 1.0F, 0.0F, false);
        bone2.texOffs(42, 15).addBox(-5.9497F, -25.0F, 0.4142F, 1.0F, 4.0F, 1.0F, 0.0F, false);
        bone2.texOffs(42, 15).addBox(-3.1213F, -25.0F, -2.8284F, 1.0F, 4.0F, 1.0F, 0.0F, false);
        bone2.texOffs(42, 15).addBox(-6.3639F, -25.0F, 0.4142F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        bone3 = new ModelRenderer(this);
        bone3.setPos(6.5F, -6.5F, -2.5F);
        bone.addChild(bone3);
        setRotationAngle(bone3, -0.7854F, 0.0F, 0.0F);
        bone3.texOffs(42, 15).addBox(-7.5F, -16.9706F, -16.2635F, 9.0F, 1.0F, 1.0F, 0.0F, false);
        bone3.texOffs(42, 15).addBox(-7.5F, -16.5563F, -16.2635F, 9.0F, 1.0F, 1.0F, 0.0F, false);
        bone3.texOffs(42, 15).addBox(-7.5F, -19.799F, -13.0208F, 9.0F, 1.0F, 1.0F, 0.0F, false);
        bone3.texOffs(42, 15).addBox(-7.5F, -19.799F, -13.435F, 9.0F, 1.0F, 1.0F, 0.0F, false);

        bolt = new ModelRenderer(this);
        bolt.setPos(11.5F, -2.8F, 0.0F);
        setRotationAngle(bolt, -0.2793F, 0.0F, 0.0F);
        bolt.texOffs(15, 76).addBox(-3.5F, -0.5344F, 0.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);

        catch_ = new ModelRenderer(this);
        catch_.setPos(0.0F, 21.8743F, 7.1074F);
        bolt.addChild(catch_);
        catch_.texOffs(15, 76).addBox(-3.5F, -22.4087F, -3.5074F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        catch_.texOffs(15, 76).addBox(-3.5F, -22.4087F, -2.0932F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        catch_.texOffs(15, 76).addBox(-2.7929F, -22.4087F, -2.8003F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        catch_.texOffs(15, 76).addBox(-4.2071F, -22.4087F, -2.8003F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        catch_.texOffs(15, 76).addBox(-3.5F, -23.1158F, -2.8003F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        catch_.texOffs(15, 76).addBox(-3.5F, -21.7016F, -2.8003F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        bone7 = new ModelRenderer(this);
        bone7.setPos(1.5F, 3.6F, 3.6F);
        catch_.addChild(bone7);


        bone8 = new ModelRenderer(this);
        bone8.setPos(0.0F, 0.0F, 0.0F);
        bone7.addChild(bone8);
        setRotationAngle(bone8, 0.0F, -0.7854F, 0.0F);
        bone8.texOffs(15, 76).addBox(-7.8541F, -26.0087F, -0.7831F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone8.texOffs(15, 76).addBox(-7.147F, -26.0087F, -1.4902F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone8.texOffs(15, 76).addBox(-8.5612F, -26.0087F, -1.4902F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone8.texOffs(15, 76).addBox(-7.8541F, -26.0087F, -2.1973F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        bone6 = new ModelRenderer(this);
        bone6.setPos(0.0F, -0.7415F, 4.8071F);
        catch_.addChild(bone6);
        setRotationAngle(bone6, 0.0F, 0.0F, -0.7854F);
        bone6.texOffs(15, 76).addBox(13.0533F, -17.5888F, -7.6074F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone6.texOffs(15, 76).addBox(12.3462F, -16.8817F, -7.6074F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone6.texOffs(15, 76).addBox(12.3462F, -18.2959F, -7.6074F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone6.texOffs(15, 76).addBox(11.6391F, -17.5888F, -7.6074F, 1.0F, 1.0F, 1.0F, 0.0F, true);

        rotatingPart3 = new ModelRenderer(this);
        rotatingPart3.setPos(0.0F, 21.8743F, 11.7074F);
        bolt.addChild(rotatingPart3);


        bone5 = new ModelRenderer(this);
        bone5.setPos(0.0F, 0.0F, 0.0F);
        rotatingPart3.addChild(bone5);
        setRotationAngle(bone5, -0.7854F, 0.0F, 0.0F);
        bone5.texOffs(15, 76).addBox(-3.5F, -11.1125F, -21.5781F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone5.texOffs(15, 76).addBox(-3.5F, -11.8196F, -20.871F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone5.texOffs(15, 76).addBox(-3.5F, -10.4054F, -20.871F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone5.texOffs(15, 76).addBox(-3.5F, -11.1125F, -20.1639F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        stock = new ModelRenderer(this);
        stock.setPos(0.0F, 24.0F, 0.0F);


        bone9 = new ModelRenderer(this);
        bone9.setPos(43.0F, -15.99F, 1.19F);
        stock.addChild(bone9);
        setRotationAngle(bone9, 0.0F, 0.0F, -0.1309F);
        bone9.texOffs(89, 149).addBox(-8.0F, -2.0F, -5.19F, 3.0F, 2.0F, 1.0F, 0.0F, false);
        bone9.texOffs(22, 93).addBox(-5.1516F, -2.502F, -5.69F, 4.0F, 3.0F, 1.0F, 0.0F, false);

        bone29 = new ModelRenderer(this);
        bone29.setPos(43.0F, -15.99F, 1.19F);
        stock.addChild(bone29);
        setRotationAngle(bone29, 0.0436F, 0.0F, -0.1309F);
        bone29.texOffs(89, 149).addBox(-13.5241F, -1.9374F, -5.1927F, 6.0F, 2.0F, 1.0F, 0.0F, false);

        bone41 = new ModelRenderer(this);
        bone41.setPos(41.92F, -15.15F, 2.87F);
        stock.addChild(bone41);
        setRotationAngle(bone41, 0.0175F, 0.0F, -0.0873F);
        bone41.texOffs(89, 149).addBox(-13.5241F, -1.9374F, -5.1927F, 1.0F, 2.0F, 2.0F, 0.0F, false);

        bone49 = new ModelRenderer(this);
        bone49.setPos(70.92F, -15.15F, 2.87F);
        stock.addChild(bone49);
        setRotationAngle(bone49, 0.0175F, 0.0F, -0.0873F);
        bone49.texOffs(89, 149).addBox(-20.0554F, -5.8936F, -5.5707F, 1.0F, 2.0F, 6.0F, 0.0F, false);

        bone50 = new ModelRenderer(this);
        bone50.setPos(68.21F, -14.42F, 1.66F);
        stock.addChild(bone50);
        setRotationAngle(bone50, 0.0175F, 0.0F, -0.0873F);
        bone50.texOffs(89, 149).addBox(-27.0554F, -5.8936F, -5.5707F, 8.0F, 2.0F, 1.0F, 0.0F, false);

        bone52 = new ModelRenderer(this);
        bone52.setPos(56.21F, -11.19F, 2.65F);
        stock.addChild(bone52);
        setRotationAngle(bone52, 0.0175F, -0.0698F, 0.0349F);
        bone52.texOffs(89, 149).addBox(-23.7598F, -5.6986F, -5.3141F, 5.0F, 2.0F, 1.0F, 0.0F, false);

        bone42 = new ModelRenderer(this);
        bone42.setPos(42.31F, -15.15F, 2.87F);
        stock.addChild(bone42);
        setRotationAngle(bone42, 0.0175F, 0.1047F, -0.0873F);
        bone42.texOffs(89, 149).addBox(-13.5241F, -1.9374F, -5.1927F, 1.0F, 2.0F, 4.0F, 0.0F, false);

        bone44 = new ModelRenderer(this);
        bone44.setPos(30.1459F, -14.7774F, 2.1173F);
        stock.addChild(bone44);
        setRotationAngle(bone44, 0.0175F, -0.0524F, -0.0873F);
        bone44.texOffs(89, 149).addBox(-0.5856F, -0.9234F, 0.9705F, 2.0F, 2.0F, 1.0F, 0.0F, false);

        bone45 = new ModelRenderer(this);
        bone45.setPos(31.9659F, -15.0474F, 2.2173F);
        stock.addChild(bone45);
        setRotationAngle(bone45, 0.0F, 0.0F, -0.1745F);
        bone45.texOffs(89, 149).addBox(-0.5856F, -0.9234F, 0.9705F, 6.0F, 2.0F, 1.0F, 0.0F, false);

        bone47 = new ModelRenderer(this);
        bone47.setPos(43.4359F, -17.1774F, 2.2173F);
        stock.addChild(bone47);
        setRotationAngle(bone47, -0.0349F, 0.0F, -0.2269F);
        bone47.texOffs(89, 149).addBox(-0.644F, -0.6702F, 0.9793F, 6.0F, 2.0F, 1.0F, 0.0F, false);

        bone48 = new ModelRenderer(this);
        bone48.setPos(48.5059F, -17.1774F, 2.2173F);
        stock.addChild(bone48);
        setRotationAngle(bone48, -0.0349F, 0.1309F, -0.1571F);
        bone48.texOffs(89, 149).addBox(-0.009F, -1.7017F, 1.0906F, 3.0F, 2.0F, 1.0F, 0.0F, false);

        bone51 = new ModelRenderer(this);
        bone51.setPos(48.2959F, -16.9974F, -2.2273F);
        stock.addChild(bone51);
        setRotationAngle(bone51, 0.0349F, -0.2618F, -0.1571F);
        bone51.texOffs(89, 149).addBox(-0.009F, -1.7017F, -2.0906F, 3.0F, 2.0F, 1.0F, 0.0F, false);

        bone46 = new ModelRenderer(this);
        bone46.setPos(40.1203F, -16.3408F, 3.6877F);
        stock.addChild(bone46);
        setRotationAngle(bone46, 0.0F, 0.0524F, -0.1396F);
        bone46.texOffs(89, 149).addBox(-3.0F, -1.0F, -0.5F, 6.0F, 2.0F, 1.0F, 0.0F, false);

        bone40 = new ModelRenderer(this);
        bone40.setPos(38.52F, -15.29F, 7.29F);
        stock.addChild(bone40);
        setRotationAngle(bone40, 0.0436F, -0.5236F, -0.1309F);
        bone40.texOffs(89, 149).addBox(-13.5241F, -1.9374F, -5.1927F, 1.0F, 2.0F, 2.0F, 0.0F, false);

        bone43 = new ModelRenderer(this);
        bone43.setPos(38.52F, -14.36F, -7.29F);
        stock.addChild(bone43);
        setRotationAngle(bone43, -0.0436F, 0.5236F, -0.0436F);
        bone43.texOffs(89, 149).addBox(-13.5241F, -1.9374F, 3.1927F, 1.0F, 2.0F, 2.0F, 0.0F, false);

        setSpecialRenderer(ModAnimations.MAGAZINE, magazine);
        setSpecialRenderer(ModAnimations.BOLT, bolt);
        setSpecialRenderer(ModAnimations.BOLT_CARRIER, bolt_case);
        setSpecialRenderer(ModAnimations.BULLET, bullet);
    }
}
