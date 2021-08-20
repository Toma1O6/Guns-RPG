package dev.toma.gunsrpg.client.model.weapon;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.toma.gunsrpg.client.animation.ModAnimations;
import dev.toma.gunsrpg.client.model.weapon.AbstractWeaponModel;
import dev.toma.gunsrpg.common.capability.IPlayerData;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.item.ItemStack;

public class Kar98kModel extends AbstractWeaponModel {

    private final ModelRenderer kar98k;
    private final ModelRenderer bone13;
    private final ModelRenderer bone16;
    private final ModelRenderer bone20;
    private final ModelRenderer bone17;
    private final ModelRenderer bone19;
    private final ModelRenderer bone18;
    private final ModelRenderer bone14;
    private final ModelRenderer bone15;
    private final ModelRenderer bone12;
    private final ModelRenderer bone10;
    private final ModelRenderer bone11;
    private final ModelRenderer bone9;
    private final ModelRenderer bone7;
    private final ModelRenderer bone8;
    private final ModelRenderer bolt;
    private final ModelRenderer bone3;
    private final ModelRenderer bone6;
    private final ModelRenderer bone5;
    private final ModelRenderer bone4;
    private final ModelRenderer boltcarrier;
    private final ModelRenderer bone2;
    private final ModelRenderer stock;
    private final ModelRenderer bone21;
    private final ModelRenderer bone22;
    private final ModelRenderer bone23;
    private final ModelRenderer bone24;
    private final ModelRenderer bone25;
    private final ModelRenderer bone26;
    private final ModelRenderer bone27;
    private final ModelRenderer bone28;
    private final ModelRenderer bone29;
    private final ModelRenderer bone30;
    private final ModelRenderer bone31;
    private final ModelRenderer bone32;
    private final ModelRenderer bone33;
    private final ModelRenderer bone34;
    private final ModelRenderer bone35;
    private final ModelRenderer bone36;
    private final ModelRenderer bone37;
    private final ModelRenderer bone38;
    private final ModelRenderer bone39;
    private final ModelRenderer bone40;
    private final ModelRenderer bone;
    private final ModelRenderer bone41;
    private final ModelRenderer bone42;
    private final ModelRenderer bullets;
    private final ModelRenderer bullet;

    public Kar98kModel() {
        texWidth = 512;
        texHeight = 512;

        kar98k = new ModelRenderer(this);
        kar98k.setPos(0.0F, 24.0F, 0.0F);
        kar98k.texOffs(142, 156).addBox(-2.6188F, 0.7071F, -2.0F, 5.0F, 7.0F, 10.0F, 0.0F, false);
        kar98k.texOffs(142, 156).addBox(-2.6188F, 0.7071F, 8.0F, 5.0F, 7.0F, 11.0F, 0.0F, false);
        kar98k.texOffs(142, 156).addBox(-2.6188F, 3.3048F, 25.8984F, 5.0F, 3.0F, 5.0F, 0.0F, false);
        kar98k.texOffs(142, 156).addBox(-1.6188F, 2.3048F, 25.8984F, 3.0F, 1.0F, 5.0F, 0.0F, false);
        kar98k.texOffs(142, 156).addBox(-2.6188F, 1.6186F, 34.3138F, 5.0F, 3.0F, 2.0F, 0.0F, false);
        kar98k.texOffs(142, 156).addBox(-1.6188F, 0.6186F, 34.3138F, 3.0F, 1.0F, 2.0F, 0.0F, false);
        kar98k.texOffs(142, 156).addBox(-2.6188F, 8.0561F, 28.3138F, 5.0F, 6.0F, 11.0F, 0.0F, false);
        kar98k.texOffs(142, 156).addBox(-2.6188F, 13.0561F, 39.3138F, 5.0F, 1.0F, 5.0F, 0.0F, false);
        kar98k.texOffs(142, 156).addBox(-2.6188F, 9.6186F, 64.0559F, 5.0F, 18.0F, 8.0F, 0.0F, false);
        kar98k.texOffs(142, 156).addBox(-1.6188F, 8.6186F, 64.0559F, 3.0F, 1.0F, 8.0F, 0.0F, false);
        kar98k.texOffs(142, 156).addBox(-2.6188F, 0.7071F, -14.0F, 5.0F, 7.0F, 1.0F, 0.0F, false);
        kar98k.texOffs(142, 156).addBox(-2.6188F, 0.7071F, -13.0F, 1.0F, 7.0F, 11.0F, 0.0F, false);
        kar98k.texOffs(142, 156).addBox(1.3812F, 0.7071F, -13.0F, 1.0F, 7.0F, 11.0F, 0.0F, true);
        kar98k.texOffs(5, 158).addBox(1.0F, -3.0312F, -14.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);
        kar98k.texOffs(5, 158).addBox(-1.0F, -3.0312F, -14.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        kar98k.texOffs(5, 158).addBox(-1.0F, -0.0312F, -14.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        kar98k.texOffs(5, 158).addBox(-2.0F, -3.0312F, -14.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);
        kar98k.texOffs(12, 162).addBox(-1.6188F, -4.0312F, -23.0F, 3.0F, 1.0F, 8.0F, 0.0F, false);
        kar98k.texOffs(12, 162).addBox(-2.1188F, -5.0312F, -17.0F, 4.0F, 1.0F, 1.0F, 0.0F, false);
        kar98k.texOffs(0, 93).addBox(1.0F, -3.0312F, -2.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);
        kar98k.texOffs(0, 93).addBox(-2.2376F, -3.0312F, -2.0F, 1.0F, 4.0F, 2.0F, 0.0F, true);
        kar98k.texOffs(0, 93).addBox(1.0F, -3.0312F, 10.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);
        kar98k.texOffs(0, 93).addBox(-2.2376F, -3.0312F, 10.0F, 1.0F, 4.0F, 2.0F, 0.0F, true);
        kar98k.texOffs(0, 93).addBox(-1.6188F, -4.0312F, -2.0F, 3.0F, 1.0F, 14.0F, 0.0F, false);
        kar98k.texOffs(0, 93).addBox(-1.9118F, -4.1952F, -2.0F, 1.0F, 1.0F, 14.0F, 0.0F, false);
        kar98k.texOffs(0, 93).addBox(0.6742F, -4.1952F, -2.0F, 1.0F, 1.0F, 14.0F, 0.0F, true);
        kar98k.texOffs(0, 93).addBox(-2.0F, 0.0469F, 14.0F, 1.0F, 1.0F, 5.0F, 0.0F, false);
        kar98k.texOffs(0, 93).addBox(1.0F, 0.0469F, 14.0F, 1.0F, 1.0F, 5.0F, 0.0F, false);
        kar98k.texOffs(142, 156).addBox(-2.6188F, -2.2929F, -25.0F, 5.0F, 10.0F, 11.0F, 0.0F, false);
        kar98k.texOffs(142, 156).addBox(-2.6188F, -2.2929F, -36.0F, 5.0F, 4.0F, 11.0F, 0.0F, false);
        kar98k.texOffs(142, 156).addBox(-2.6188F, -2.2929F, -47.0F, 5.0F, 4.0F, 11.0F, 0.0F, false);
        kar98k.texOffs(142, 156).addBox(-2.6188F, -2.2929F, -58.0F, 5.0F, 4.0F, 11.0F, 0.0F, false);
        kar98k.texOffs(142, 156).addBox(-2.6188F, -2.2929F, -69.0F, 5.0F, 4.0F, 11.0F, 0.0F, false);
        kar98k.texOffs(142, 156).addBox(-2.6188F, 1.7071F, -69.0F, 5.0F, 2.0F, 6.0F, 0.0F, false);
        kar98k.texOffs(142, 156).addBox(-2.1188F, -1.7929F, -82.0F, 1.0F, 4.0F, 11.0F, 0.0F, false);
        kar98k.texOffs(142, 156).addBox(0.8812F, -1.7929F, -82.0F, 1.0F, 4.0F, 11.0F, 0.0F, false);
        kar98k.texOffs(142, 156).addBox(-2.6188F, -2.2929F, -71.0F, 5.0F, 6.0F, 2.0F, 0.0F, false);
        kar98k.texOffs(142, 156).addBox(-1.6188F, -3.2929F, -25.0F, 3.0F, 1.0F, 11.0F, 0.0F, false);
        kar98k.texOffs(142, 156).addBox(-1.6188F, -3.2929F, -36.0F, 3.0F, 1.0F, 11.0F, 0.0F, false);
        kar98k.texOffs(142, 156).addBox(-1.6188F, -3.2929F, -47.0F, 3.0F, 1.0F, 11.0F, 0.0F, false);
        kar98k.texOffs(142, 156).addBox(-1.6188F, -3.2929F, -58.0F, 3.0F, 1.0F, 11.0F, 0.0F, false);
        kar98k.texOffs(142, 156).addBox(-1.6188F, -3.2929F, -69.0F, 3.0F, 1.0F, 11.0F, 0.0F, false);
        kar98k.texOffs(142, 156).addBox(-1.6188F, 3.7071F, -69.0F, 3.0F, 1.0F, 7.0F, 0.0F, false);
        kar98k.texOffs(142, 156).addBox(-1.1188F, -2.7929F, -82.0F, 2.0F, 1.0F, 11.0F, 0.0F, false);
        kar98k.texOffs(0, 12).addBox(-1.1188F, -1.9062F, -108.0F, 2.0F, 2.0F, 29.0F, 0.0F, false);
        kar98k.texOffs(12, 162).addBox(-1.6188F, -1.9062F, -107.0F, 3.0F, 2.0F, 3.0F, 0.0F, false);
        kar98k.texOffs(12, 162).addBox(-1.1188F, -2.4062F, -107.0F, 2.0F, 3.0F, 3.0F, 0.0F, false);
        kar98k.texOffs(12, 162).addBox(-0.6188F, -5.4062F, -106.5F, 1.0F, 3.0F, 2.0F, 0.0F, false);
        kar98k.texOffs(0, 12).addBox(-1.1188F, 0.2852F, -90.0F, 2.0F, 2.0F, 11.0F, 0.0F, false);
        kar98k.texOffs(142, 156).addBox(-1.1188F, 2.2071F, -82.0F, 2.0F, 1.0F, 11.0F, 0.0F, false);
        kar98k.texOffs(142, 156).addBox(-1.6188F, -3.2929F, -71.0F, 3.0F, 1.0F, 2.0F, 0.0F, false);
        kar98k.texOffs(142, 156).addBox(-1.6188F, 3.7071F, -71.0F, 3.0F, 1.0F, 2.0F, 0.0F, false);
        kar98k.texOffs(142, 156).addBox(-1.6188F, 7.7071F, -25.0F, 3.0F, 1.0F, 11.0F, 0.0F, false);
        kar98k.texOffs(142, 156).addBox(-1.6188F, 7.7071F, -14.0F, 3.0F, 1.0F, 11.0F, 0.0F, false);
        kar98k.texOffs(142, 156).addBox(-1.6188F, 7.7071F, -3.0F, 3.0F, 1.0F, 11.0F, 0.0F, false);
        kar98k.texOffs(142, 156).addBox(-1.6188F, 7.7071F, 8.0F, 3.0F, 1.0F, 13.0F, 0.0F, false);
        kar98k.texOffs(17, 154).addBox(-1.1188F, 7.9871F, -0.5F, 2.0F, 1.0F, 12.0F, 0.0F, false);
        kar98k.texOffs(142, 156).addBox(-1.6188F, 0.7071F, 19.0F, 3.0F, 2.0F, 4.0F, 0.0F, false);

        bone13 = new ModelRenderer(this);
        bone13.setPos(5.3812F, 0.0F, 0.0F);
        kar98k.addChild(bone13);
        setRotationAngle(bone13, -0.3491F, 0.0F, 0.0F);
        bone13.texOffs(142, 156).addBox(-8.0F, -5.834F, 18.0962F, 5.0F, 7.0F, 9.0F, 0.0F, false);
        bone13.texOffs(142, 156).addBox(-7.0F, -6.834F, 18.0962F, 3.0F, 1.0F, 9.0F, 0.0F, false);

        bone16 = new ModelRenderer(this);
        bone16.setPos(5.3812F, 14.832F, 17.1758F);
        kar98k.addChild(bone16);
        setRotationAngle(bone16, 0.4363F, 0.0F, 0.0F);
        bone16.texOffs(142, 156).addBox(-8.0F, -4.7325F, 15.1168F, 5.0F, 7.0F, 6.0F, 0.0F, false);
        bone16.texOffs(142, 156).addBox(-7.0F, -5.7325F, 17.1168F, 3.0F, 1.0F, 4.0F, 0.0F, false);

        bone20 = new ModelRenderer(this);
        bone20.setPos(-5.3812F, -12.2176F, 22.3737F);
        bone16.addChild(bone20);
        setRotationAngle(bone20, 0.0F, 0.0F, 0.7854F);
        bone20.texOffs(142, 156).addBox(5.5623F, 3.609F, -5.2569F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        bone20.texOffs(142, 156).addBox(3.441F, 5.7303F, -5.2569F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        bone20.texOffs(142, 156).addBox(3.441F, 6.1445F, -5.2569F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        bone20.texOffs(142, 156).addBox(5.9765F, 3.609F, -5.2569F, 1.0F, 1.0F, 4.0F, 0.0F, false);

        bone17 = new ModelRenderer(this);
        bone17.setPos(5.3812F, 14.832F, 26.1758F);
        kar98k.addChild(bone17);
        setRotationAngle(bone17, -0.2618F, 0.0F, 0.0F);
        bone17.texOffs(142, 156).addBox(-8.0F, -15.387F, 6.3727F, 5.0F, 7.0F, 11.0F, 0.0F, false);
        bone17.texOffs(142, 156).addBox(-7.0F, -16.387F, 6.3727F, 3.0F, 1.0F, 11.0F, 0.0F, false);
        bone17.texOffs(142, 156).addBox(-8.0F, -15.387F, 17.3727F, 5.0F, 7.0F, 11.0F, 0.0F, false);
        bone17.texOffs(142, 156).addBox(-7.0F, -16.387F, 17.3727F, 3.0F, 1.0F, 11.0F, 0.0F, false);
        bone17.texOffs(142, 156).addBox(-8.0F, -15.387F, 28.3727F, 5.0F, 9.0F, 11.0F, 0.0F, false);
        bone17.texOffs(142, 156).addBox(-7.0F, -16.387F, 28.3727F, 3.0F, 1.0F, 11.0F, 0.0F, false);

        bone19 = new ModelRenderer(this);
        bone19.setPos(0.0F, 0.0F, 0.0F);
        bone17.addChild(bone19);
        setRotationAngle(bone19, 0.0F, 0.0F, 0.7854F);
        bone19.texOffs(142, 156).addBox(-14.4158F, -8.7589F, 6.3727F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone19.texOffs(142, 156).addBox(-14.4158F, -8.7589F, 17.3727F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone19.texOffs(142, 156).addBox(-14.4158F, -8.7589F, 28.3727F, 1.0F, 1.0F, 9.0F, 0.0F, false);
        bone19.texOffs(142, 156).addBox(-16.5371F, -6.6376F, 28.3727F, 1.0F, 1.0F, 9.0F, 0.0F, false);
        bone19.texOffs(142, 156).addBox(-16.5371F, -6.6376F, 17.3727F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone19.texOffs(142, 156).addBox(-16.5371F, -6.6376F, 6.3727F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone19.texOffs(142, 156).addBox(-14.0015F, -8.7589F, 6.3727F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone19.texOffs(142, 156).addBox(-14.0015F, -8.7589F, 17.3727F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone19.texOffs(142, 156).addBox(-14.0015F, -8.7589F, 28.3727F, 1.0F, 1.0F, 9.0F, 0.0F, false);
        bone19.texOffs(142, 156).addBox(-16.5371F, -6.2234F, 28.3727F, 1.0F, 1.0F, 9.0F, 0.0F, false);
        bone19.texOffs(142, 156).addBox(-16.5371F, -6.2234F, 17.3727F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone19.texOffs(142, 156).addBox(-16.5371F, -6.2234F, 6.3727F, 1.0F, 1.0F, 11.0F, 0.0F, false);

        bone18 = new ModelRenderer(this);
        bone18.setPos(5.3812F, 14.832F, 26.1758F);
        kar98k.addChild(bone18);
        setRotationAngle(bone18, -0.5236F, 0.0F, 0.0F);
        bone18.texOffs(142, 156).addBox(-8.0F, -14.8665F, 6.1985F, 5.0F, 7.0F, 11.0F, 0.0F, false);
        bone18.texOffs(142, 156).addBox(-8.0F, -15.8665F, 17.1985F, 5.0F, 8.0F, 11.0F, 0.0F, false);
        bone18.texOffs(142, 156).addBox(-8.0F, -15.8665F, 28.1985F, 5.0F, 8.0F, 11.0F, 0.0F, false);

        bone14 = new ModelRenderer(this);
        bone14.setPos(5.3812F, -7.918F, 0.0F);
        kar98k.addChild(bone14);
        setRotationAngle(bone14, -1.0472F, 0.0F, 0.0F);
        bone14.texOffs(142, 156).addBox(-8.0F, -19.0157F, 25.0721F, 5.0F, 7.0F, 10.0F, 0.0F, false);
        bone14.texOffs(142, 156).addBox(-7.0F, -19.0157F, 35.0721F, 3.0F, 7.0F, 1.0F, 0.0F, false);

        bone15 = new ModelRenderer(this);
        bone15.setPos(0.0F, 0.0F, 0.0F);
        bone14.addChild(bone15);
        setRotationAngle(bone15, 0.0F, 0.7854F, 0.0F);
        bone15.texOffs(142, 156).addBox(-27.921F, -19.0157F, 21.6785F, 1.0F, 7.0F, 1.0F, 0.0F, false);
        bone15.texOffs(142, 156).addBox(-28.3352F, -19.0157F, 21.6785F, 1.0F, 7.0F, 1.0F, 0.0F, false);
        bone15.texOffs(142, 156).addBox(-30.4565F, -19.0157F, 19.5571F, 1.0F, 7.0F, 1.0F, 0.0F, false);
        bone15.texOffs(142, 156).addBox(-30.4565F, -19.0157F, 19.1429F, 1.0F, 7.0F, 1.0F, 0.0F, false);

        bone12 = new ModelRenderer(this);
        bone12.setPos(1.0F, -12.2383F, -15.0F);
        kar98k.addChild(bone12);
        setRotationAngle(bone12, 0.1745F, 0.0F, 0.0F);
        bone12.texOffs(12, 162).addBox(-0.6188F, 6.6934F, -9.3035F, 1.0F, 1.0F, 8.0F, 0.0F, false);
        bone12.texOffs(12, 162).addBox(-2.6188F, 6.6934F, -9.3035F, 1.0F, 1.0F, 8.0F, 0.0F, true);

        bone10 = new ModelRenderer(this);
        bone10.setPos(0.0F, -3.0F, -30.5F);
        kar98k.addChild(bone10);
        setRotationAngle(bone10, -0.1047F, 0.0F, 0.0F);
        bone10.texOffs(142, 156).addBox(-2.6198F, 4.0736F, -4.411F, 5.0F, 6.0F, 11.0F, 0.0F, false);
        bone10.texOffs(142, 156).addBox(-2.6198F, 5.0736F, -15.411F, 5.0F, 5.0F, 11.0F, 0.0F, false);
        bone10.texOffs(142, 156).addBox(-2.6198F, 6.0736F, -26.411F, 5.0F, 4.0F, 11.0F, 0.0F, false);
        bone10.texOffs(142, 156).addBox(-2.6198F, 7.0736F, -32.411F, 5.0F, 3.0F, 6.0F, 0.0F, false);
        bone10.texOffs(142, 156).addBox(-1.6188F, 10.0736F, -32.411F, 3.0F, 1.0F, 13.0F, 0.0F, false);
        bone10.texOffs(142, 156).addBox(-1.6188F, 10.0736F, -19.411F, 3.0F, 1.0F, 13.0F, 0.0F, false);
        bone10.texOffs(142, 156).addBox(-1.6188F, 10.0736F, -6.411F, 3.0F, 1.0F, 13.0F, 0.0F, false);

        bone11 = new ModelRenderer(this);
        bone11.setPos(6.501F, 3.0F, 30.5F);
        bone10.addChild(bone11);
        setRotationAngle(bone11, 0.0F, 0.0F, -0.7854F);
        bone11.texOffs(142, 156).addBox(-8.9149F, 1.0886F, -36.911F, 1.0F, 1.0F, 13.0F, 0.0F, false);
        bone11.texOffs(142, 156).addBox(-11.4511F, -1.4476F, -36.911F, 1.0F, 1.0F, 13.0F, 0.0F, false);
        bone11.texOffs(142, 156).addBox(-8.9149F, 1.0886F, -49.911F, 1.0F, 1.0F, 13.0F, 0.0F, false);
        bone11.texOffs(142, 156).addBox(-11.4511F, -1.4476F, -49.911F, 1.0F, 1.0F, 13.0F, 0.0F, false);
        bone11.texOffs(142, 156).addBox(-8.9149F, 1.0886F, -62.911F, 1.0F, 1.0F, 13.0F, 0.0F, false);
        bone11.texOffs(142, 156).addBox(-11.4511F, -1.4476F, -62.911F, 1.0F, 1.0F, 13.0F, 0.0F, false);
        bone11.texOffs(142, 156).addBox(-9.3291F, 1.0886F, -36.911F, 1.0F, 1.0F, 13.0F, 0.0F, false);
        bone11.texOffs(142, 156).addBox(-11.4504F, -1.0327F, -36.911F, 1.0F, 1.0F, 13.0F, 0.0F, false);
        bone11.texOffs(142, 156).addBox(-9.3291F, 1.0886F, -49.911F, 1.0F, 1.0F, 13.0F, 0.0F, false);
        bone11.texOffs(142, 156).addBox(-11.4504F, -1.0327F, -49.911F, 1.0F, 1.0F, 13.0F, 0.0F, false);
        bone11.texOffs(142, 156).addBox(-9.3291F, 1.0886F, -62.911F, 1.0F, 1.0F, 13.0F, 0.0F, false);
        bone11.texOffs(142, 156).addBox(-11.4504F, -1.0327F, -62.911F, 1.0F, 1.0F, 13.0F, 0.0F, false);

        bone9 = new ModelRenderer(this);
        bone9.setPos(5.3812F, 0.0F, 0.0F);
        kar98k.addChild(bone9);
        setRotationAngle(bone9, 0.0F, 0.0F, 0.7854F);
        bone9.texOffs(142, 156).addBox(-7.2781F, 2.6213F, -25.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-7.2781F, 2.6213F, -36.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-7.2781F, 2.6213F, -47.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-7.2781F, 2.6213F, -58.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-7.2781F, 2.6213F, -69.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-7.2781F, 2.6213F, -71.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-2.6213F, 7.2781F, -71.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-5.1568F, 0.4999F, -36.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-5.1568F, 0.4999F, -47.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-5.1568F, 0.4999F, -58.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-5.1568F, 0.4999F, -69.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-0.4999F, 4.7426F, -69.0F, 1.0F, 1.0F, 7.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-5.1568F, 0.4999F, -71.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-0.4999F, 5.1568F, -71.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-4.7426F, 0.4999F, -36.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-4.7426F, 0.4999F, -47.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-4.7426F, 0.4999F, -58.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-4.7426F, 0.4999F, -69.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-0.4999F, 5.1568F, -69.0F, 1.0F, 1.0F, 7.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-2.6213F, 7.2781F, -69.0F, 1.0F, 1.0F, 7.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-3.0355F, 7.2781F, -69.0F, 1.0F, 1.0F, 7.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-4.7426F, 1.207F, -82.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-6.571F, 3.0355F, -82.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-6.571F, 2.6213F, -82.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-5.1568F, 1.207F, -82.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-1.9142F, 4.4497F, -82.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-3.3284F, 5.8639F, -82.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-1.9142F, 4.0355F, -82.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-3.7426F, 5.8639F, -82.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-4.7426F, 0.4999F, -71.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-0.4999F, 4.7426F, -71.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-4.7426F, 0.4999F, -25.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(2.3285F, 7.571F, -25.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(2.3285F, 7.571F, -14.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(2.3285F, 7.571F, -3.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(3.2659F, 8.9227F, 62.0559F, 1.0F, 1.0F, 10.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-2.3909F, 3.2658F, 34.3138F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-1.1987F, 4.4581F, 24.8984F, 1.0F, 1.0F, 6.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-3.32F, 6.5794F, 24.8984F, 1.0F, 1.0F, 6.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-4.5122F, 5.3871F, 34.3138F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-4.5122F, 5.8014F, 34.3138F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(1.1446F, 11.044F, 62.0559F, 1.0F, 1.0F, 10.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(3.6802F, 8.9227F, 65.0559F, 1.0F, 1.0F, 7.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-1.9767F, 3.2658F, 34.3138F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-0.7844F, 4.4581F, 24.8984F, 1.0F, 1.0F, 6.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-3.32F, 6.9936F, 24.8984F, 1.0F, 1.0F, 6.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(1.1446F, 11.4582F, 65.0559F, 1.0F, 1.0F, 7.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(2.3285F, 7.571F, 8.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(2.3285F, 7.9852F, -25.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(2.3285F, 7.9852F, -14.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(2.3285F, 7.9852F, -3.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(2.3285F, 7.9852F, 8.0F, 1.0F, 1.0F, 13.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(0.2072F, 10.1065F, -25.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(0.2072F, 10.1065F, -14.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(0.2072F, 10.1065F, -3.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(0.2072F, 10.1065F, 8.0F, 1.0F, 1.0F, 13.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-0.207F, 10.1065F, -25.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-0.207F, 10.1065F, -14.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-0.207F, 10.1065F, -3.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-0.207F, 10.1065F, 8.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-5.1568F, 0.4999F, -25.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-7.2781F, 3.0355F, -25.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-7.2781F, 3.0355F, -36.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-7.2781F, 3.0355F, -47.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-7.2781F, 3.0355F, -58.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-7.2781F, 3.0355F, -69.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-7.2781F, 3.0355F, -71.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        bone9.texOffs(142, 156).addBox(-3.0355F, 7.2781F, -71.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        bone7 = new ModelRenderer(this);
        bone7.setPos(5.9883F, 0.0F, 0.0F);
        kar98k.addChild(bone7);
        setRotationAngle(bone7, 0.0F, 0.2618F, 0.0F);
        bone7.texOffs(142, 156).addBox(-13.2314F, 0.7071F, 16.125F, 1.0F, 2.0F, 4.0F, 0.0F, false);

        bone8 = new ModelRenderer(this);
        bone8.setPos(-6.0F, 0.0F, 0.0F);
        kar98k.addChild(bone8);
        setRotationAngle(bone8, 0.0F, -0.2618F, 0.0F);
        bone8.texOffs(142, 156).addBox(12.0131F, 0.7071F, 16.1833F, 1.0F, 2.0F, 4.0F, 0.0F, false);

        bolt = new ModelRenderer(this);
        bolt.setPos(2.0F, 25.0F, -1.0F);
        setRotationAngle(bolt, 0.0F, 0.0F, -0.3491F);
        bolt.texOffs(84, 17).addBox(-5.8707F, -4.0195F, 13.0F, 4.0F, 2.0F, 2.0F, 0.0F, false);

        bone3 = new ModelRenderer(this);
        bone3.setPos(0.0F, 0.0F, 0.0F);
        bolt.addChild(bone3);
        setRotationAngle(bone3, 0.0F, 0.0F, -0.8727F);
        bone3.texOffs(84, 17).addBox(-3.6946F, -7.0813F, 13.0F, 3.0F, 2.0F, 2.0F, 0.0F, false);
        bone3.texOffs(84, 17).addBox(-6.1088F, -7.0813F, 13.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        bone3.texOffs(84, 17).addBox(-5.4017F, -7.0813F, 14.7071F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        bone3.texOffs(84, 17).addBox(-5.4017F, -7.0813F, 12.2929F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        bone3.texOffs(84, 17).addBox(-5.4017F, -7.7884F, 13.0F, 2.0F, 1.0F, 2.0F, 0.0F, false);
        bone3.texOffs(84, 17).addBox(-5.4017F, -5.3742F, 13.0F, 2.0F, 1.0F, 2.0F, 0.0F, false);

        bone6 = new ModelRenderer(this);
        bone6.setPos(0.0F, 0.0F, 0.0F);
        bone3.addChild(bone6);
        setRotationAngle(bone6, 0.0F, -0.7854F, 0.0F);
        bone6.texOffs(84, 17).addBox(6.287F, -7.0813F, 13.9262F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        bone6.texOffs(84, 17).addBox(7.7012F, -7.0813F, 12.512F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        bone6.texOffs(84, 17).addBox(4.8728F, -7.0813F, 12.512F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        bone6.texOffs(84, 17).addBox(6.287F, -7.0813F, 11.0978F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        bone5 = new ModelRenderer(this);
        bone5.setPos(0.0F, 0.0F, 0.0F);
        bone3.addChild(bone5);
        setRotationAngle(bone5, 0.7854F, 0.0F, 0.0F);
        bone5.texOffs(84, 17).addBox(-5.4017F, 6.5136F, 13.6996F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        bone5.texOffs(84, 17).addBox(-5.4017F, 5.0994F, 12.2854F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        bone5.texOffs(84, 17).addBox(-5.4017F, 5.0994F, 15.1138F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        bone5.texOffs(84, 17).addBox(-5.4017F, 3.6852F, 13.6996F, 2.0F, 1.0F, 1.0F, 0.0F, false);

        bone4 = new ModelRenderer(this);
        bone4.setPos(0.0F, 0.0F, 0.0F);
        bone3.addChild(bone4);
        setRotationAngle(bone4, 0.0F, 0.0F, -0.7854F);
        bone4.texOffs(84, 17).addBox(2.1019F, -7.9127F, 13.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        bone4.texOffs(84, 17).addBox(0.6876F, -9.3269F, 13.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        bone4.texOffs(84, 17).addBox(-0.7266F, -7.9127F, 13.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        bone4.texOffs(84, 17).addBox(0.6876F, -6.4985F, 13.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        boltcarrier = new ModelRenderer(this);
        boltcarrier.setPos(0.0F, 15.3F, 21.0F);
        boltcarrier.texOffs(84, 17).addBox(-1.0F, 6.4071F, -28.0F, 2.0F, 3.0F, 14.0F, 0.0F, false);
        boltcarrier.texOffs(84, 17).addBox(-1.0F, 7.4071F, -14.0F, 2.0F, 2.0F, 14.0F, 0.0F, false);
        boltcarrier.texOffs(84, 17).addBox(-0.2929F, 6.7F, -28.0F, 2.0F, 2.0F, 14.0F, 0.0F, false);
        boltcarrier.texOffs(84, 17).addBox(-0.2929F, 6.7F, -42.0F, 2.0F, 2.0F, 14.0F, 0.0F, false);
        boltcarrier.texOffs(84, 17).addBox(-0.2929F, 6.7F, -14.0F, 2.0F, 2.0F, 14.0F, 0.0F, false);
        boltcarrier.texOffs(84, 17).addBox(-1.7071F, 6.7F, -28.0F, 2.0F, 2.0F, 14.0F, 0.0F, false);
        boltcarrier.texOffs(84, 17).addBox(-1.7071F, 6.7F, -42.0F, 2.0F, 2.0F, 14.0F, 0.0F, false);
        boltcarrier.texOffs(84, 17).addBox(-1.7071F, 6.7F, -14.0F, 2.0F, 2.0F, 14.0F, 0.0F, false);
        boltcarrier.texOffs(84, 17).addBox(-1.0F, 5.9929F, -28.0F, 2.0F, 2.0F, 14.0F, 0.0F, false);
        boltcarrier.texOffs(84, 17).addBox(-1.0F, 5.9929F, -42.0F, 2.0F, 2.0F, 14.0F, 0.0F, false);
        boltcarrier.texOffs(84, 17).addBox(-1.0F, 7.4071F, -42.0F, 2.0F, 2.0F, 14.0F, 0.0F, false);
        boltcarrier.texOffs(84, 17).addBox(-1.0F, 5.9929F, -14.0F, 2.0F, 2.0F, 14.0F, 0.0F, false);

        bone2 = new ModelRenderer(this);
        bone2.setPos(-2.0F, 9.7F, -22.0F);
        boltcarrier.addChild(bone2);
        setRotationAngle(bone2, 0.0F, 0.0F, -0.7854F);
        bone2.texOffs(84, 17).addBox(2.3284F, -0.0858F, -6.0F, 1.0F, 2.0F, 14.0F, 0.0F, false);
        bone2.texOffs(84, 17).addBox(2.3284F, -0.0858F, -20.0F, 1.0F, 2.0F, 14.0F, 0.0F, false);
        bone2.texOffs(84, 17).addBox(2.3284F, -0.0858F, 8.0F, 1.0F, 2.0F, 14.0F, 0.0F, false);
        bone2.texOffs(84, 17).addBox(0.9142F, -0.5F, -6.0F, 2.0F, 1.0F, 14.0F, 0.0F, false);
        bone2.texOffs(84, 17).addBox(0.9142F, -0.5F, -20.0F, 2.0F, 1.0F, 14.0F, 0.0F, false);
        bone2.texOffs(84, 17).addBox(0.9142F, -0.5F, 8.0F, 2.0F, 1.0F, 14.0F, 0.0F, false);
        bone2.texOffs(84, 17).addBox(2.7426F, -0.5F, -6.0F, 2.0F, 1.0F, 14.0F, 0.0F, false);
        bone2.texOffs(84, 17).addBox(2.7426F, -0.5F, -20.0F, 2.0F, 1.0F, 14.0F, 0.0F, false);
        bone2.texOffs(84, 17).addBox(2.7426F, -0.5F, 8.0F, 2.0F, 1.0F, 14.0F, 0.0F, false);
        bone2.texOffs(84, 17).addBox(2.3284F, -1.9143F, -6.0F, 1.0F, 2.0F, 14.0F, 0.0F, false);
        bone2.texOffs(84, 17).addBox(2.3284F, -1.9143F, -20.0F, 1.0F, 2.0F, 14.0F, 0.0F, false);
        bone2.texOffs(84, 17).addBox(2.3284F, -1.9143F, 8.0F, 1.0F, 2.0F, 14.0F, 0.0F, false);

        stock = new ModelRenderer(this);
        stock.setPos(0.0F, 24.0F, -2.0F);


        bone21 = new ModelRenderer(this);
        bone21.setPos(-0.5F, -2.5F, 53.5F);
        stock.addChild(bone21);
        setRotationAngle(bone21, -0.2618F, -0.1745F, 0.0698F);
        bone21.texOffs(212, 153).addBox(-0.4639F, 6.7346F, 7.4025F, 5.0F, 1.0F, 2.0F, 0.0F, false);

        bone22 = new ModelRenderer(this);
        bone22.setPos(-0.5F, -2.75F, 52.66F);
        stock.addChild(bone22);
        setRotationAngle(bone22, -0.3142F, -0.1047F, 0.0F);
        bone22.texOffs(212, 153).addBox(-1.5547F, 6.2918F, 7.3565F, 5.0F, 1.0F, 2.0F, 0.0F, false);

        bone23 = new ModelRenderer(this);
        bone23.setPos(-4.0066F, -2.2342F, 59.8428F);
        stock.addChild(bone23);
        setRotationAngle(bone23, -0.2705F, 0.1134F, 0.6545F);
        bone23.texOffs(212, 153).addBox(6.068F, 5.3228F, 1.1545F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        bone24 = new ModelRenderer(this);
        bone24.setPos(-4.1366F, -2.4342F, 58.1528F);
        stock.addChild(bone24);
        setRotationAngle(bone24, -0.2705F, 0.0262F, 0.5672F);
        bone24.texOffs(212, 153).addBox(5.5392F, 5.7971F, 0.856F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        bone25 = new ModelRenderer(this);
        bone25.setPos(1.0434F, -2.7542F, 59.0128F);
        stock.addChild(bone25);
        setRotationAngle(bone25, -0.2705F, 0.2705F, 0.829F);
        bone25.texOffs(212, 153).addBox(6.8982F, 4.2021F, 1.8572F, 1.0F, 1.0F, 4.0F, 0.0F, false);

        bone26 = new ModelRenderer(this);
        bone26.setPos(-1.43F, -2.2F, 56.46F);
        stock.addChild(bone26);
        setRotationAngle(bone26, -0.576F, 0.0698F, 0.0F);
        bone26.texOffs(212, 153).addBox(3.0015F, 5.5629F, 7.61F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        bone27 = new ModelRenderer(this);
        bone27.setPos(-1.02F, -2.22F, 54.54F);
        stock.addChild(bone27);
        setRotationAngle(bone27, -0.4451F, -0.0611F, 0.0F);
        bone27.texOffs(212, 153).addBox(3.1127F, 6.2706F, 6.0481F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        bone28 = new ModelRenderer(this);
        bone28.setPos(-0.96F, 2.29F, 53.7F);
        stock.addChild(bone28);
        setRotationAngle(bone28, -0.4538F, -0.0611F, 0.0524F);
        bone28.texOffs(212, 153).addBox(3.5158F, 6.2483F, 6.0843F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        bone29 = new ModelRenderer(this);
        bone29.setPos(2.7713F, 4.9076F, 55.3132F);
        stock.addChild(bone29);
        setRotationAngle(bone29, -0.5847F, -0.0611F, -0.0349F);
        bone29.texOffs(212, 153).addBox(-0.8869F, 3.9064F, 3.2688F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        bone30 = new ModelRenderer(this);
        bone30.setPos(2.7713F, 7.5776F, 53.7032F);
        stock.addChild(bone30);
        setRotationAngle(bone30, -0.5847F, 0.096F, -0.0349F);
        bone30.texOffs(212, 153).addBox(-1.167F, 5.1301F, 3.4327F, 1.0F, 6.0F, 3.0F, 0.0F, false);

        bone31 = new ModelRenderer(this);
        bone31.setPos(2.9934F, 11.7658F, 52.8428F);
        stock.addChild(bone31);
        setRotationAngle(bone31, -0.2705F, 0.5498F, 1.3526F);
        bone31.texOffs(212, 153).addBox(6.1664F, 1.2285F, 1.9902F, 1.0F, 2.0F, 2.0F, 0.0F, false);

        bone32 = new ModelRenderer(this);
        bone32.setPos(2.6362F, 12.0719F, 52.1305F);
        stock.addChild(bone32);
        setRotationAngle(bone32, -0.0524F, 0.5498F, 1.3526F);
        bone32.texOffs(212, 153).addBox(5.8936F, 1.0766F, 3.0062F, 1.0F, 2.0F, 2.0F, 0.0F, false);

        bone33 = new ModelRenderer(this);
        bone33.setPos(2.8413F, 16.0276F, 55.3132F);
        stock.addChild(bone33);
        setRotationAngle(bone33, -0.5847F, -0.0611F, 0.096F);
        bone33.texOffs(212, 153).addBox(-5.7106F, 4.5333F, -1.3702F, 5.0F, 1.0F, 2.0F, 0.0F, false);

        bone34 = new ModelRenderer(this);
        bone34.setPos(-0.9886F, 11.3433F, 51.1138F);
        stock.addChild(bone34);
        setRotationAngle(bone34, -0.672F, 0.0262F, 0.096F);
        bone34.texOffs(212, 153).addBox(-1.8797F, 5.5024F, 3.7957F, 5.0F, 1.0F, 2.0F, 0.0F, false);

        bone35 = new ModelRenderer(this);
        bone35.setPos(-0.9886F, 10.7833F, 51.1138F);
        stock.addChild(bone35);
        setRotationAngle(bone35, -0.4538F, -0.0611F, 0.096F);
        bone35.texOffs(212, 153).addBox(-1.6936F, 4.3789F, 4.2845F, 1.0F, 3.0F, 2.0F, 0.0F, false);

        bone36 = new ModelRenderer(this);
        bone36.setPos(-0.9886F, 10.7833F, 51.1138F);
        stock.addChild(bone36);
        setRotationAngle(bone36, -0.5847F, 0.0262F, 0.0087F);
        bone36.texOffs(212, 153).addBox(-2.5517F, 3.9282F, 3.2531F, 1.0F, 3.0F, 2.0F, 0.0F, false);

        bone37 = new ModelRenderer(this);
        bone37.setPos(-0.7386F, 8.9533F, 54.0038F);
        stock.addChild(bone37);
        setRotationAngle(bone37, -0.5847F, 0.0262F, 0.0087F);
        bone37.texOffs(212, 153).addBox(-2.5517F, 0.9282F, 3.2531F, 1.0F, 6.0F, 2.0F, 0.0F, false);

        bone38 = new ModelRenderer(this);
        bone38.setPos(-2.8486F, 6.4533F, 54.0038F);
        stock.addChild(bone38);
        setRotationAngle(bone38, -0.5847F, -0.1047F, 0.0087F);
        bone38.texOffs(212, 153).addBox(-0.5515F, 3.4245F, 3.2589F, 1.0F, 6.0F, 2.0F, 0.0F, false);

        bone39 = new ModelRenderer(this);
        bone39.setPos(-2.8486F, 5.6233F, 53.0238F);
        stock.addChild(bone39);
        setRotationAngle(bone39, -0.4538F, -0.1047F, 0.0087F);
        bone39.texOffs(212, 153).addBox(0.2104F, -1.895F, 5.2889F, 1.0F, 6.0F, 2.0F, 0.0F, false);

        bone40 = new ModelRenderer(this);
        bone40.setPos(-2.7068F, 1.383F, 57.0389F);
        stock.addChild(bone40);
        setRotationAngle(bone40, -0.4538F, -0.3665F, 0.1833F);
        bone40.texOffs(212, 153).addBox(0.8296F, 4.3033F, 2.196F, 1.0F, 6.0F, 1.0F, 0.0F, false);

        bone = new ModelRenderer(this);
        bone.setPos(2.8F, 6.5F, 60.5F);
        stock.addChild(bone);
        setRotationAngle(bone, -0.5236F, 0.0F, 0.0F);
        bone.texOffs(18, 86).addBox(-0.5F, 6.4282F, -11.5F, 1.0F, 3.0F, 21.0F, 0.0F, false);
        bone.texOffs(18, 86).addBox(0.366F, 6.9282F, -11.5F, 1.0F, 2.0F, 21.0F, 0.0F, false);
        bone.texOffs(105, 181).addBox(0.866F, 7.4282F, -10.7F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone.texOffs(105, 181).addBox(0.866F, 7.4282F, -6.7F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone.texOffs(105, 181).addBox(0.866F, 7.4282F, -2.7F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone.texOffs(105, 181).addBox(0.866F, 7.4282F, 1.3F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone.texOffs(105, 181).addBox(0.866F, 7.4282F, 5.3F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone.texOffs(105, 181).addBox(0.866F, 7.4282F, -8.3F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone.texOffs(105, 181).addBox(0.866F, 7.4282F, -4.3F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone.texOffs(105, 181).addBox(0.866F, 7.4282F, -0.3F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone.texOffs(105, 181).addBox(0.866F, 7.4282F, 3.7F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone.texOffs(105, 181).addBox(0.866F, 7.4282F, 7.7F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        bone41 = new ModelRenderer(this);
        bone41.setPos(0.0F, 1.0F, -5.0F);
        bone.addChild(bone41);
        setRotationAngle(bone41, 0.0F, 0.0F, 0.5236F);
        bone41.texOffs(18, 86).addBox(3.1471F, 4.451F, -6.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone41.texOffs(18, 86).addBox(3.1471F, 4.451F, -2.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone41.texOffs(18, 86).addBox(3.1471F, 4.451F, 1.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone41.texOffs(18, 86).addBox(3.1471F, 4.451F, 5.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone41.texOffs(18, 86).addBox(3.1471F, 4.451F, 9.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone41.texOffs(18, 86).addBox(3.1471F, 4.451F, 13.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        bone42 = new ModelRenderer(this);
        bone42.setPos(0.0F, -14.0F, -5.0F);
        bone.addChild(bone42);
        setRotationAngle(bone42, 0.0F, 0.0F, -0.5236F);
        bone42.texOffs(18, 86).addBox(-11.2811F, 19.5394F, -6.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone42.texOffs(18, 86).addBox(-11.2811F, 19.5394F, -2.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone42.texOffs(18, 86).addBox(-11.2811F, 19.5394F, 1.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone42.texOffs(18, 86).addBox(-11.2811F, 19.5394F, 5.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone42.texOffs(18, 86).addBox(-11.2811F, 19.5394F, 9.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone42.texOffs(18, 86).addBox(-11.2811F, 19.5394F, 13.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        bullets = new ModelRenderer(this);
        bullets.setPos(2.2F, -5.4F, -14.0F);
        bone.addChild(bullets);
        bullets.texOffs(9, 500).addBox(-1.6188F, 9.9282F, 11.5F, 3.0F, 1.0F, 3.0F, 0.0F, false);
        bullets.texOffs(9, 500).addBox(-1.6188F, 9.9282F, 7.5F, 3.0F, 1.0F, 3.0F, 0.0F, false);
        bullets.texOffs(9, 500).addBox(-1.6188F, 9.9282F, 15.5F, 3.0F, 1.0F, 3.0F, 0.0F, false);
        bullets.texOffs(9, 500).addBox(-1.6188F, 9.9282F, 19.5F, 3.0F, 1.0F, 3.0F, 0.0F, false);
        bullets.texOffs(9, 500).addBox(-1.6188F, 9.9282F, 3.5F, 3.0F, 1.0F, 3.0F, 0.0F, false);
        bullets.texOffs(9, 500).addBox(-1.6188F, 11.3282F, 11.5F, 3.0F, 4.0F, 3.0F, 0.0F, false);
        bullets.texOffs(9, 500).addBox(-1.6188F, 11.3282F, 7.5F, 3.0F, 4.0F, 3.0F, 0.0F, false);
        bullets.texOffs(9, 500).addBox(-1.6188F, 11.3282F, 15.5F, 3.0F, 4.0F, 3.0F, 0.0F, false);
        bullets.texOffs(9, 500).addBox(-1.6188F, 11.3282F, 19.5F, 3.0F, 4.0F, 3.0F, 0.0F, false);
        bullets.texOffs(9, 500).addBox(-1.6188F, 11.3282F, 3.5F, 3.0F, 4.0F, 3.0F, 0.0F, false);
        bullets.texOffs(9, 500).addBox(-1.1188F, 17.5282F, 12.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        bullets.texOffs(9, 500).addBox(-1.1188F, 17.5282F, 8.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        bullets.texOffs(9, 500).addBox(-1.1188F, 17.5282F, 16.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        bullets.texOffs(9, 500).addBox(-1.1188F, 17.5282F, 20.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        bullets.texOffs(9, 500).addBox(-1.1188F, 17.5282F, 4.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        bullets.texOffs(9, 500).addBox(-0.8188F, 10.9282F, 12.3F, 2.0F, 7.0F, 2.0F, 0.0F, false);
        bullets.texOffs(9, 500).addBox(-0.8188F, 10.9282F, 8.3F, 2.0F, 7.0F, 2.0F, 0.0F, false);
        bullets.texOffs(9, 500).addBox(-0.8188F, 10.9282F, 16.3F, 2.0F, 7.0F, 2.0F, 0.0F, false);
        bullets.texOffs(9, 500).addBox(-0.8188F, 10.9282F, 20.3F, 2.0F, 7.0F, 2.0F, 0.0F, false);
        bullets.texOffs(9, 500).addBox(-0.8188F, 10.9282F, 4.3F, 2.0F, 7.0F, 2.0F, 0.0F, false);
        bullets.texOffs(9, 500).addBox(-0.8188F, 10.9282F, 11.7F, 2.0F, 7.0F, 2.0F, 0.0F, false);
        bullets.texOffs(9, 500).addBox(-0.8188F, 10.9282F, 7.7F, 2.0F, 7.0F, 2.0F, 0.0F, false);
        bullets.texOffs(9, 500).addBox(-0.8188F, 10.9282F, 15.7F, 2.0F, 7.0F, 2.0F, 0.0F, false);
        bullets.texOffs(9, 500).addBox(-0.8188F, 10.9282F, 19.7F, 2.0F, 7.0F, 2.0F, 0.0F, false);
        bullets.texOffs(9, 500).addBox(-0.8188F, 10.9282F, 3.7F, 2.0F, 7.0F, 2.0F, 0.0F, false);
        bullets.texOffs(9, 500).addBox(-1.4188F, 10.9282F, 12.3F, 2.0F, 7.0F, 2.0F, 0.0F, true);
        bullets.texOffs(9, 500).addBox(-1.4188F, 10.9282F, 8.3F, 2.0F, 7.0F, 2.0F, 0.0F, true);
        bullets.texOffs(9, 500).addBox(-1.4188F, 10.9282F, 16.3F, 2.0F, 7.0F, 2.0F, 0.0F, true);
        bullets.texOffs(9, 500).addBox(-1.4188F, 10.9282F, 20.3F, 2.0F, 7.0F, 2.0F, 0.0F, true);
        bullets.texOffs(9, 500).addBox(-1.4188F, 10.9282F, 4.3F, 2.0F, 7.0F, 2.0F, 0.0F, true);
        bullets.texOffs(9, 500).addBox(-1.4188F, 10.9282F, 11.7F, 2.0F, 7.0F, 2.0F, 0.0F, true);
        bullets.texOffs(9, 500).addBox(-1.4188F, 10.9282F, 7.7F, 2.0F, 7.0F, 2.0F, 0.0F, true);
        bullets.texOffs(9, 500).addBox(-1.4188F, 10.9282F, 15.7F, 2.0F, 7.0F, 2.0F, 0.0F, true);
        bullets.texOffs(9, 500).addBox(-1.4188F, 10.9282F, 19.7F, 2.0F, 7.0F, 2.0F, 0.0F, true);
        bullets.texOffs(9, 500).addBox(-1.4188F, 10.9282F, 3.7F, 2.0F, 7.0F, 2.0F, 0.0F, true);

        bullet = new ModelRenderer(this);
        bullet.setPos(0.0F, 32.0F, 0.0F);
        bullet.texOffs(8, 496).addBox(-1.6188F, -2.5F, -4.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        bullet.texOffs(8, 496).addBox(-1.6188F, -2.5F, -8.4F, 3.0F, 3.0F, 4.0F, 0.0F, false);
        bullet.texOffs(8, 496).addBox(-1.1188F, -2.0F, -12.6F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        bullet.texOffs(8, 496).addBox(-0.8188F, -2.3F, -11.0F, 2.0F, 2.0F, 7.0F, 0.0F, false);
        bullet.texOffs(8, 496).addBox(-0.8188F, -1.7F, -11.0F, 2.0F, 2.0F, 7.0F, 0.0F, false);
        bullet.texOffs(8, 496).addBox(-1.4188F, -2.3F, -11.0F, 2.0F, 2.0F, 7.0F, 0.0F, true);
        bullet.texOffs(8, 496).addBox(-1.4188F, -1.7F, -11.0F, 2.0F, 2.0F, 7.0F, 0.0F, true);

        addSingleAnimated(ModAnimations.BOLT, bolt);
        addSingleAnimated(ModAnimations.BOLT_CARRIER, boltcarrier);
        addSingleAnimated(ModAnimations.BULLET, bullet);
    }

    @Override
    public void renderWeapon(ItemStack stack, IPlayerData data, MatrixStack matrix, IVertexBuilder builder, int light, int overlay) {
        kar98k.render(matrix, builder, light, overlay);
        stock.render(matrix, builder, light, overlay);
    }
}
