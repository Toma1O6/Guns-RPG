package dev.toma.gunsrpg.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.common.init.Skills;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.item.ItemStack;

public class ModelSMG extends ModelWeapon {

    private final ModelRenderer receiver;
    private final ModelRenderer bone;
    private final ModelRenderer bone2;
    private final ModelRenderer bone3;
    private final ModelRenderer grip;
    private final ModelRenderer bone4;
    private final ModelRenderer bone5;
    private final ModelRenderer bone6;
    private final ModelRenderer magazine;
    private final ModelRenderer stock;
    private final ModelRenderer bone11;
    private final ModelRenderer bone7;
    private final ModelRenderer bone8;
    private final ModelRenderer bone9;
    private final ModelRenderer sights;
    private final ModelRenderer bone10;
    private final ModelRenderer bone12;
    private final ModelRenderer bone13;
    private final ModelRenderer bone14;
    private final ModelRenderer barrel;
    private final ModelRenderer suppressor;
    private final ModelRenderer supp1;
    private final ModelRenderer supp0;
    private final ModelRenderer rds;
    private final ModelRenderer a1;
    private final ModelRenderer a2;

    public ModelSMG() {
        texWidth = 256;
        texHeight = 256;

        receiver = new ModelRenderer(this);
        receiver.setPos(0.0F, 24.0F, 0.0F);

        bone = new ModelRenderer(this);
        bone.setPos(0.0F, 0.0F, 0.0F);
        receiver.addChild(bone);
        bone.texOffs(69, 10).addBox(-2.0F, -9.5F, -13.0F, 4.0F, 2.0F, 23.0F, 0.0F, false);
        bone.texOffs(69, 10).addBox(-1.5F, -8.0F, -4.0F, 3.0F, 4.0F, 9.0F, 0.0F, false);
        bone.texOffs(69, 10).addBox(-1.5F, -7.5F, 7.0F, 3.0F, 1.0F, 3.0F, 0.0F, false);
        bone.texOffs(69, 10).addBox(-2.0F, -12.5F, -24.0F, 4.0F, 4.0F, 11.0F, 0.0F, false);
        bone.texOffs(69, 10).addBox(-1.5F, -13.0F, -24.0F, 3.0F, 5.0F, 11.0F, 0.0F, false);
        bone.texOffs(69, 10).addBox(-2.0F, -11.5F, -13.0F, 4.0F, 2.0F, 7.0F, 0.0F, false);
        bone.texOffs(69, 10).addBox(-2.0F, -11.5F, 1.0F, 4.0F, 2.0F, 9.0F, 0.0F, false);
        bone.texOffs(69, 10).addBox(-1.0F, -11.5F, -6.0F, 3.0F, 2.0F, 7.0F, 0.0F, false);
        bone.texOffs(19, 20).addBox(-1.85F, -11.5F, -5.8F, 1.0F, 2.0F, 7.0F, 0.0F, false);
        bone.texOffs(69, 10).addBox(-2.0F, -13.5F, -14.0F, 4.0F, 2.0F, 24.0F, 0.0F, false);
        bone.texOffs(69, 10).addBox(-1.5F, -14.0F, -13.75F, 3.0F, 1.0F, 22.0F, 0.0F, false);
        bone.texOffs(4, 89).addBox(-1.7F, -11.7F, -12.6F, 4.0F, 4.0F, 22.0F, 0.0F, false);
        bone.texOffs(4, 89).addBox(-2.3F, -8.7F, -12.6F, 4.0F, 1.0F, 22.0F, 0.0F, true);
        bone.texOffs(4, 89).addBox(-2.3F, -11.7F, 1.4F, 4.0F, 3.0F, 8.0F, 0.0F, true);
        bone.texOffs(4, 89).addBox(-2.3F, -11.7F, -12.6F, 4.0F, 3.0F, 6.0F, 0.0F, true);

        bone2 = new ModelRenderer(this);
        bone2.setPos(0.0F, 0.0F, -7.0F);
        receiver.addChild(bone2);
        setRotationAngle(bone2, -0.1745F, 0.0F, 0.0F);
        bone2.texOffs(69, 10).addBox(-2.0F, -8.3755F, -3.3077F, 4.0F, 6.0F, 6.0F, 0.0F, false);
        bone2.texOffs(69, 10).addBox(-1.5F, -5.3755F, 2.6923F, 3.0F, 3.0F, 3.0F, 0.0F, false);
        bone2.texOffs(36, 146).addBox(-1.0F, -2.3755F, 2.4923F, 2.0F, 2.0F, 1.0F, 0.0F, false);

        bone3 = new ModelRenderer(this);
        bone3.setPos(0.0F, 0.0F, 0.0F);
        receiver.addChild(bone3);
        setRotationAngle(bone3, 0.6109F, 0.0F, 0.0F);
        bone3.texOffs(69, 10).addBox(-1.5F, -3.4087F, 6.3901F, 3.0F, 3.0F, 5.0F, 0.0F, false);

        grip = new ModelRenderer(this);
        grip.setPos(0.0F, 24.0F, 0.0F);


        bone4 = new ModelRenderer(this);
        bone4.setPos(0.0F, -1.0F, 0.75F);
        grip.addChild(bone4);
        setRotationAngle(bone4, 0.4363F, 0.0F, 0.0F);
        bone4.texOffs(105, 48).addBox(-1.0F, -2.2289F, 2.8445F, 2.0F, 7.0F, 4.0F, 0.0F, false);
        bone4.texOffs(105, 48).addBox(-0.5F, -0.4789F, 0.8445F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        bone5 = new ModelRenderer(this);
        bone5.setPos(0.0F, -1.0F, 0.75F);
        grip.addChild(bone5);
        setRotationAngle(bone5, -0.0873F, 0.0F, 0.0F);
        bone5.texOffs(105, 48).addBox(-0.5F, -0.9709F, -2.0081F, 1.0F, 1.0F, 3.0F, 0.0F, false);

        bone6 = new ModelRenderer(this);
        bone6.setPos(0.0F, -1.0F, 0.75F);
        grip.addChild(bone6);
        setRotationAngle(bone6, -0.6109F, 0.0F, 0.0F);
        bone6.texOffs(105, 48).addBox(-0.5F, 0.0292F, -2.7245F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        magazine = new ModelRenderer(this);
        magazine.setPos(0.0F, 24.0F, -7.0F);
        setRotationAngle(magazine, -0.1745F, 0.0F, 0.0F);
        magazine.texOffs(89, 94).addBox(-1.5F, -9.3755F, -2.8077F, 3.0F, 20.0F, 5.0F, 0.0F, false);
        magazine.texOffs(89, 94).addBox(-1.5F, 9.6245F, -3.8077F, 3.0F, 1.0F, 1.0F, 0.0F, false);

        stock = new ModelRenderer(this);
        stock.setPos(0.0F, 23.0F, 0.0F);


        bone11 = new ModelRenderer(this);
        bone11.setPos(-9.8F, 0.8F, 6.7F);
        stock.addChild(bone11);
        setRotationAngle(bone11, 0.0F, 0.7854F, 0.0F);
        bone11.texOffs(25, 93).addBox(4.5F, -11.2F, 10.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        bone7 = new ModelRenderer(this);
        bone7.setPos(0.0F, 0.0F, 0.0F);
        stock.addChild(bone7);
        bone7.texOffs(25, 93).addBox(-1.5F, -11.2F, 10.0F, 3.0F, 4.0F, 1.0F, 0.0F, false);
        bone7.texOffs(25, 93).addBox(-1.0F, -8.4F, 13.1641F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        bone7.texOffs(25, 93).addBox(-1.0F, -8.4F, 14.1641F, 2.0F, 3.0F, 1.0F, 0.0F, false);
        bone7.texOffs(25, 93).addBox(-1.5F, -9.4F, 15.1641F, 3.0F, 1.0F, 8.0F, 0.0F, false);
        bone7.texOffs(25, 93).addBox(-1.0F, -8.4F, 18.1641F, 2.0F, 5.0F, 1.0F, 0.0F, false);
        bone7.texOffs(25, 93).addBox(-1.5F, -9.4F, 23.1641F, 3.0F, 8.0F, 1.0F, 0.0F, false);
        bone7.texOffs(25, 93).addBox(-1.5F, -2.7417F, 21.6743F, 3.0F, 1.0F, 2.0F, 0.0F, false);
        bone7.texOffs(25, 93).addBox(-1.5F, -9.4F, 13.1641F, 3.0F, 1.0F, 2.0F, 0.0F, false);

        bone8 = new ModelRenderer(this);
        bone8.setPos(0.0F, -4.2F, 13.5F);
        stock.addChild(bone8);
        setRotationAngle(bone8, 1.0472F, 0.0F, 0.0F);
        bone8.texOffs(25, 93).addBox(-1.5F, -5.6651F, 2.8122F, 3.0F, 4.0F, 2.0F, 0.0F, false);
        bone8.texOffs(25, 93).addBox(-1.5F, -4.6651F, 1.3481F, 3.0F, 3.0F, 2.0F, 0.0F, false);
        bone8.texOffs(25, 93).addBox(-1.5F, -1.6651F, 1.3481F, 3.0F, 3.0F, 1.0F, 0.0F, false);

        bone9 = new ModelRenderer(this);
        bone9.setPos(0.0F, -4.2F, 13.5F);
        stock.addChild(bone9);
        setRotationAngle(bone9, 1.1345F, 0.0F, 0.0F);
        bone9.texOffs(25, 93).addBox(-1.5F, 1.4473F, 1.2266F, 3.0F, 7.0F, 1.0F, 0.0F, false);
        bone9.texOffs(25, 93).addBox(-1.0F, -4.4304F, -0.0694F, 2.0F, 1.0F, 2.0F, 0.0F, false);

        sights = new ModelRenderer(this);
        sights.setPos(0.0F, 24.0F, 0.0F);
        sights.texOffs(87, 41).addBox(-2.0F, -14.5F, 7.6F, 4.0F, 1.0F, 2.0F, 0.0F, false);
        sights.texOffs(87, 41).addBox(0.9F, -15.5F, 7.6F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        sights.texOffs(87, 41).addBox(0.3F, -15.2F, 8.2F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        sights.texOffs(87, 41).addBox(-1.3F, -15.2F, 8.2F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        sights.texOffs(87, 41).addBox(-1.9F, -15.5F, 7.6F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        sights.texOffs(87, 41).addBox(-0.5F, -15.5962F, -22.8128F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        bone10 = new ModelRenderer(this);
        bone10.setPos(1.4F, -10.0F, 8.6F);
        sights.addChild(bone10);
        setRotationAngle(bone10, -1.309F, 0.0F, 0.0F);
        bone10.texOffs(87, 41).addBox(-0.5F, -2.3894F, -5.0538F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        bone10.texOffs(87, 41).addBox(-3.3F, -2.3894F, -5.0538F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        bone12 = new ModelRenderer(this);
        bone12.setPos(0.0F, -9.0F, -22.9F);
        sights.addChild(bone12);
        setRotationAngle(bone12, 1.1345F, 0.0F, 0.0F);
        bone12.texOffs(87, 41).addBox(-0.5F, -1.4604F, 4.4979F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        bone13 = new ModelRenderer(this);
        bone13.setPos(0.0F, -9.0F, -22.9F);
        sights.addChild(bone13);
        setRotationAngle(bone13, 0.5236F, 0.0F, 0.0F);
        bone13.texOffs(87, 41).addBox(-0.5F, -3.5306F, 2.2396F, 1.0F, 3.0F, 2.0F, 0.0F, false);

        bone14 = new ModelRenderer(this);
        bone14.setPos(0.0F, -9.0F, -22.4F);
        sights.addChild(bone14);
        setRotationAngle(bone14, -0.0873F, 0.0F, 0.0F);
        bone14.texOffs(87, 41).addBox(-0.5F, -6.5351F, -0.9862F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        barrel = new ModelRenderer(this);
        barrel.setPos(0.0F, 24.0F, 0.0F);
        barrel.texOffs(30, 139).addBox(-1.5F, -11.0F, -31.0F, 2.0F, 2.0F, 7.0F, 0.0F, false);
        barrel.texOffs(30, 139).addBox(-2.0F, -11.5F, -26.5F, 3.0F, 3.0F, 2.0F, 0.0F, false);

        suppressor = new ModelRenderer(this);
        suppressor.setPos(0.0F, 14.0F, -42.8F);
        suppressor.texOffs(169, 149).addBox(-1.0F, 1.7321F, -2.1719F, 2.0F, 2.0F, 20.0F, 0.0F, false);
        suppressor.texOffs(169, 149).addBox(-3.7321F, -1.0F, -2.1719F, 3.0F, 2.0F, 20.0F, 0.0F, false);
        suppressor.texOffs(169, 149).addBox(0.7321F, -1.0F, -2.1719F, 3.0F, 2.0F, 20.0F, 0.0F, true);
        suppressor.texOffs(169, 149).addBox(-1.0F, -3.7321F, -2.1719F, 2.0F, 2.0F, 20.0F, 0.0F, false);
        suppressor.texOffs(169, 149).addBox(-1.0F, 0.7321F, -7.1719F, 2.0F, 3.0F, 5.0F, 0.0F, false);
        suppressor.texOffs(169, 149).addBox(-3.7321F, -1.0F, -7.1719F, 3.0F, 2.0F, 5.0F, 0.0F, false);
        suppressor.texOffs(169, 149).addBox(0.7321F, -1.0F, -7.1719F, 3.0F, 2.0F, 5.0F, 0.0F, true);
        suppressor.texOffs(169, 149).addBox(-1.0F, -3.7321F, -7.1719F, 2.0F, 3.0F, 5.0F, 0.0F, false);

        supp1 = new ModelRenderer(this);
        supp1.setPos(0.0F, 0.0F, 0.0F);
        suppressor.addChild(supp1);
        setRotationAngle(supp1, 0.0F, 0.0F, -0.5236F);
        supp1.texOffs(169, 149).addBox(-1.0F, -0.2679F, -2.1719F, 2.0F, 4.0F, 20.0F, 0.0F, false);
        supp1.texOffs(169, 149).addBox(-3.7321F, -1.0F, -2.1719F, 3.0F, 2.0F, 20.0F, 0.0F, false);
        supp1.texOffs(169, 149).addBox(-1.0F, -3.7321F, -2.1719F, 2.0F, 4.0F, 20.0F, 0.0F, true);
        supp1.texOffs(169, 149).addBox(0.7321F, -1.0F, -2.1719F, 3.0F, 2.0F, 20.0F, 0.0F, true);
        supp1.texOffs(169, 149).addBox(-1.0F, 0.7321F, -7.1719F, 2.0F, 3.0F, 5.0F, 0.0F, false);
        supp1.texOffs(169, 149).addBox(-3.7321F, -1.0F, -7.1719F, 3.0F, 2.0F, 5.0F, 0.0F, false);
        supp1.texOffs(169, 149).addBox(-1.0F, -3.7321F, -7.1719F, 2.0F, 3.0F, 5.0F, 0.0F, true);
        supp1.texOffs(169, 149).addBox(0.7321F, -1.0F, -7.1719F, 3.0F, 2.0F, 5.0F, 0.0F, true);

        supp0 = new ModelRenderer(this);
        supp0.setPos(0.0F, 0.0F, 0.0F);
        suppressor.addChild(supp0);
        setRotationAngle(supp0, 0.0F, 0.0F, 0.5236F);
        supp0.texOffs(169, 149).addBox(-1.0F, -0.2679F, -2.1719F, 2.0F, 4.0F, 20.0F, 0.0F, false);
        supp0.texOffs(169, 149).addBox(0.7321F, -1.0F, -2.1719F, 3.0F, 2.0F, 20.0F, 0.0F, false);
        supp0.texOffs(169, 149).addBox(-3.7321F, -1.0F, -2.1719F, 3.0F, 2.0F, 20.0F, 0.0F, true);
        supp0.texOffs(169, 149).addBox(-1.0F, -3.7321F, -2.1719F, 2.0F, 4.0F, 20.0F, 0.0F, true);
        supp0.texOffs(169, 149).addBox(-1.0F, 0.7321F, -7.1719F, 2.0F, 3.0F, 5.0F, 0.0F, false);
        supp0.texOffs(169, 149).addBox(0.7321F, -1.0F, -7.1719F, 3.0F, 2.0F, 5.0F, 0.0F, false);
        supp0.texOffs(169, 149).addBox(-3.7321F, -1.0F, -7.1719F, 3.0F, 2.0F, 5.0F, 0.0F, true);
        supp0.texOffs(169, 149).addBox(-1.0F, -3.7321F, -7.1719F, 2.0F, 3.0F, 5.0F, 0.0F, true);

        rds = new ModelRenderer(this);
        rds.setPos(0.0F, 24.0F, 0.0F);
        rds.texOffs(140, 140).addBox(-2.0F, -14.5F, -12.0F, 4.0F, 1.0F, 13.0F, 0.0F, false);
        rds.texOffs(140, 140).addBox(-2.0F, -15.5F, -2.0F, 4.0F, 1.0F, 3.0F, 0.0F, false);
        rds.texOffs(140, 140).addBox(-2.0F, -15.5F, -12.0F, 4.0F, 1.0F, 2.0F, 0.0F, false);
        rds.texOffs(140, 140).addBox(-3.0F, -20.5F, -12.0F, 6.0F, 1.0F, 2.0F, 0.0F, false);
        rds.texOffs(140, 140).addBox(2.0F, -19.5F, -12.0F, 1.0F, 3.0F, 2.0F, 0.0F, false);
        rds.texOffs(140, 140).addBox(-3.0F, -19.5F, -12.0F, 1.0F, 3.0F, 2.0F, 0.0F, false);

        a1 = new ModelRenderer(this);
        a1.setPos(-2.15F, 2.4F, 0.0F);
        rds.addChild(a1);
        setRotationAngle(a1, 0.0F, 0.0F, 0.3491F);
        a1.texOffs(140, 140).addBox(-2.6369F, -19.5257F, -12.0F, 1.0F, 3.0F, 2.0F, 0.0F, false);

        a2 = new ModelRenderer(this);
        a2.setPos(-2.15F, 2.4F, 0.0F);
        rds.addChild(a2);
        setRotationAngle(a2, 0.0F, 0.0F, -0.3491F);
        a2.texOffs(140, 140).addBox(5.6756F, -18.0965F, -12.0F, 1.0F, 3.0F, 2.0F, 0.0F, false);
    }

    @Override
    public void renderWeapon(ItemStack stack, PlayerData data, MatrixStack matrix, IVertexBuilder builder, int light, int overlay, float r, float g, float b, float a) {
        receiver.render(matrix, builder, light, overlay);
        grip.render(matrix, builder, light, overlay);
        magazine.render(matrix, builder, light, overlay);
        stock.render(matrix, builder, light, overlay);
        sights.render(matrix, builder, light, overlay);
        barrel.render(matrix, builder, light, overlay);
        PlayerSkills skills = data.getSkills();
        renderWithCondition(suppressor, matrix, builder, light, overlay, skills, s -> s.hasSkill(Skills.SMG_SUPPRESSOR));
        renderWithCondition(rds, matrix, builder, light, overlay, skills, s -> s.hasSkill(Skills.SMG_RED_DOT));
    }
}
