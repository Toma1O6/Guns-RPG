package dev.toma.gunsrpg.client.model.weapon;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.toma.gunsrpg.client.animation.ModAnimations;
import dev.toma.gunsrpg.common.capability.IPlayerData;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.item.ItemStack;

public class ThompsonModel extends AbstractWeaponModel {

    private final ModelRenderer thompson;
    private final ModelRenderer bone15;
    private final ModelRenderer bone19;
    private final ModelRenderer bone16;
    private final ModelRenderer bone17;
    private final ModelRenderer bone18;
    private final ModelRenderer bone24;
    private final ModelRenderer bone25;
    private final ModelRenderer bone23;
    private final ModelRenderer bone22;
    private final ModelRenderer bone27;
    private final ModelRenderer bone20;
    private final ModelRenderer bone21;
    private final ModelRenderer bone7;
    private final ModelRenderer bone14;
    private final ModelRenderer bone12;
    private final ModelRenderer bone26;
    private final ModelRenderer bone13;
    private final ModelRenderer bone35;
    private final ModelRenderer bone5;
    private final ModelRenderer bone6;
    private final ModelRenderer bone3;
    private final ModelRenderer bone4;
    private final ModelRenderer magazine1;
    private final ModelRenderer bone;
    private final ModelRenderer bone2;
    private final ModelRenderer magazine2;
    private final ModelRenderer bone28;
    private final ModelRenderer bone29;
    private final ModelRenderer bone30;
    private final ModelRenderer bone31;
    private final ModelRenderer bone32;
    private final ModelRenderer bone33;
    private final ModelRenderer bone34;
    private final ModelRenderer charging_handle;
    private final ModelRenderer bone11;
    private final ModelRenderer bone10;
    private final ModelRenderer bone9;
    private final ModelRenderer bone8;
    private final ModelRenderer bullet;

    @Override
    protected void renderWeapon(ItemStack stack, IPlayerData data, MatrixStack matrix, IVertexBuilder builder, int light, int overlay) {
        thompson.render(matrix, builder, light, overlay);
    }

    public ThompsonModel() {
        texWidth = 512;
        texHeight = 512;

        thompson = new ModelRenderer(this);
        thompson.setPos(0.0F, 24.0F, 0.0F);
        thompson.texOffs(8, 70).addBox(-0.082F, -24.5391F, -20.0F, 2.0F, 1.0F, 7.0F, 0.0F, false);
        thompson.texOffs(8, 70).addBox(-1.918F, -24.5391F, -20.0F, 2.0F, 1.0F, 7.0F, 0.0F, true);
        thompson.texOffs(85, 37).addBox(-2.5F, -26.5508F, -20.0F, 5.0F, 3.0F, 7.0F, 0.0F, false);
        thompson.texOffs(85, 37).addBox(-2.5F, -26.5508F, -26.0F, 5.0F, 4.0F, 6.0F, 0.0F, false);
        thompson.texOffs(64, 25).addBox(-1.0F, -26.5352F, -42.0F, 2.0F, 2.0F, 13.0F, 0.0F, false);
        thompson.texOffs(67, 15).addBox(-2.5F, -26.5508F, -13.0F, 5.0F, 5.0F, 22.0F, 0.0F, false);
        thompson.texOffs(67, 15).addBox(-2.5F, -26.5508F, 9.0F, 5.0F, 5.0F, 12.0F, 0.0F, false);
        thompson.texOffs(165, 156).addBox(-2.0F, -21.5508F, 16.0F, 4.0F, 4.0F, 7.0F, 0.0F, false);
        thompson.texOffs(165, 156).addBox(-2.0F, -17.8433F, 39.9118F, 4.0F, 11.0F, 2.0F, 0.0F, false);
        thompson.texOffs(165, 156).addBox(-2.0F, -21.5508F, 9.0F, 4.0F, 4.0F, 7.0F, 0.0F, false);
        thompson.texOffs(165, 156).addBox(-2.0F, -18.1366F, 10.4142F, 4.0F, 2.0F, 10.0F, 0.0F, false);
        thompson.texOffs(67, 15).addBox(-1.5F, -26.9648F, 21.0F, 3.0F, 6.0F, 1.0F, 0.0F, false);
        thompson.texOffs(5, 17).addBox(1.0F, -26.5508F, 20.5352F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        thompson.texOffs(5, 17).addBox(-2.0F, -26.5508F, 20.5352F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        thompson.texOffs(67, 15).addBox(0.7929F, -27.2579F, -17.0F, 1.0F, 1.0F, 26.0F, 0.0F, false);
        thompson.texOffs(67, 15).addBox(0.9648F, -29.961F, 3.4258F, 1.0F, 3.0F, 5.0F, 0.0F, false);
        thompson.texOffs(67, 15).addBox(-1.9648F, -29.961F, 3.4258F, 1.0F, 3.0F, 5.0F, 0.0F, true);
        thompson.texOffs(67, 15).addBox(-1.0F, -28.6641F, 6.2852F, 2.0F, 2.0F, 2.0F, 0.0F, true);
        thompson.texOffs(67, 15).addBox(-1.7929F, -27.2579F, -17.0F, 1.0F, 1.0F, 26.0F, 0.0F, true);
        thompson.texOffs(67, 15).addBox(-1.0F, -27.2579F, 3.0F, 2.0F, 1.0F, 6.0F, 0.0F, true);
        thompson.texOffs(67, 15).addBox(-1.7929F, -27.2579F, -26.0F, 2.0F, 1.0F, 9.0F, 0.0F, true);
        thompson.texOffs(67, 15).addBox(-1.7929F, -27.2579F, 9.0F, 2.0F, 1.0F, 12.0F, 0.0F, true);
        thompson.texOffs(67, 15).addBox(-1.7929F, -22.8437F, -26.0F, 2.0F, 1.0F, 6.0F, 0.0F, true);
        thompson.texOffs(67, 15).addBox(-0.2071F, -27.2579F, -26.0F, 2.0F, 1.0F, 9.0F, 0.0F, false);
        thompson.texOffs(67, 15).addBox(-0.2071F, -27.2579F, 9.0F, 2.0F, 1.0F, 12.0F, 0.0F, false);
        thompson.texOffs(67, 15).addBox(-0.2071F, -22.8437F, -26.0F, 2.0F, 1.0F, 6.0F, 0.0F, false);
        thompson.texOffs(152, 152).addBox(-1.5F, -24.8437F, -38.0F, 3.0F, 3.0F, 11.0F, 0.0F, false);
        thompson.texOffs(152, 152).addBox(-1.5F, -24.8437F, -49.0F, 3.0F, 3.0F, 11.0F, 0.0F, false);
        thompson.texOffs(152, 152).addBox(-0.2071F, -22.1366F, -49.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        thompson.texOffs(152, 152).addBox(-0.2071F, -22.1366F, -38.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        thompson.texOffs(152, 152).addBox(-0.7929F, -22.1366F, -49.0F, 1.0F, 1.0F, 11.0F, 0.0F, true);
        thompson.texOffs(152, 152).addBox(-0.7929F, -22.1366F, -38.0F, 1.0F, 1.0F, 11.0F, 0.0F, true);
        thompson.texOffs(90, 77).addBox(-1.0F, -26.7384F, -7.0F, 2.0F, 1.0F, 10.0F, 0.0F, true);
        thompson.texOffs(90, 77).addBox(-1.0F, -26.7384F, -17.0F, 2.0F, 1.0F, 10.0F, 0.0F, true);
        thompson.texOffs(67, 15).addBox(-2.0F, -21.5508F, -13.0F, 4.0F, 5.0F, 3.0F, 0.0F, false);
        thompson.texOffs(67, 15).addBox(-2.0F, -21.5508F, -10.0F, 4.0F, 1.0F, 9.0F, 0.0F, false);
        thompson.texOffs(67, 15).addBox(-2.0F, -21.5508F, -1.0F, 4.0F, 2.0F, 6.0F, 0.0F, false);
        thompson.texOffs(67, 15).addBox(-1.0F, -20.5508F, -3.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        thompson.texOffs(67, 15).addBox(-1.0F, -21.2109F, -4.0F, 2.0F, 3.0F, 1.0F, 0.0F, false);
        thompson.texOffs(165, 156).addBox(-2.0F, -19.5508F, -1.0F, 4.0F, 5.0F, 7.0F, 0.0F, false);
        thompson.texOffs(67, 15).addBox(-2.0F, -15.5508F, -11.0F, 4.0F, 1.0F, 3.0F, 0.0F, false);
        thompson.texOffs(67, 15).addBox(-1.5F, -15.5508F, -8.0F, 3.0F, 1.0F, 7.0F, 0.0F, false);

        bone15 = new ModelRenderer(this);
        bone15.setPos(0.0F, -25.5352F, -35.4333F);
        thompson.addChild(bone15);
        bone15.texOffs(64, 25).addBox(-0.5F, -1.5F, 6.4333F, 1.0F, 3.0F, 3.0F, 0.0F, false);
        bone15.texOffs(64, 25).addBox(-0.5F, -1.5F, 5.2333F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone15.texOffs(64, 25).addBox(-0.5F, -1.5F, 4.0333F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone15.texOffs(64, 25).addBox(-0.5F, -1.5F, 2.8333F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone15.texOffs(64, 25).addBox(-0.5F, -1.5F, 1.6333F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone15.texOffs(64, 25).addBox(-0.5F, -1.5F, 0.4333F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone15.texOffs(64, 25).addBox(-0.5F, -1.5F, -0.7667F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone15.texOffs(64, 25).addBox(-0.5F, -1.5F, -1.9667F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone15.texOffs(64, 25).addBox(-0.5F, -1.5F, -4.3667F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone15.texOffs(64, 25).addBox(-0.5F, -1.5F, -3.1667F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone15.texOffs(64, 25).addBox(-0.5F, -1.5F, -5.5667F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone15.texOffs(64, 25).addBox(-0.5F, -1.5F, -16.7667F, 1.0F, 3.0F, 11.0F, 0.0F, false);
        bone15.texOffs(64, 25).addBox(-0.5F, -2.5F, -16.1729F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        bone15.texOffs(64, 25).addBox(-0.5F, -4.4319F, -15.6553F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        bone19 = new ModelRenderer(this);
        bone19.setPos(0.0F, -3.0F, -15.6729F);
        bone15.addChild(bone19);
        setRotationAngle(bone19, -0.2618F, 0.0F, 0.0F);
        bone19.texOffs(64, 25).addBox(-0.5F, -1.3876F, -0.3536F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        bone16 = new ModelRenderer(this);
        bone16.setPos(0.0F, -25.5352F, -35.4333F);
        thompson.addChild(bone16);
        setRotationAngle(bone16, 0.0F, 0.0F, -0.7854F);
        bone16.texOffs(64, 25).addBox(-0.5F, -1.5F, 6.4333F, 1.0F, 3.0F, 3.0F, 0.0F, false);
        bone16.texOffs(64, 25).addBox(-0.5F, -1.5F, 5.2333F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone16.texOffs(64, 25).addBox(-0.5F, -1.5F, 4.0333F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone16.texOffs(64, 25).addBox(-0.5F, -1.5F, 2.8333F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone16.texOffs(64, 25).addBox(-0.5F, -1.5F, 1.6333F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone16.texOffs(64, 25).addBox(-0.5F, -1.5F, 0.4333F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone16.texOffs(64, 25).addBox(-0.5F, -1.5F, -0.7667F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone16.texOffs(64, 25).addBox(-0.5F, -1.5F, -1.9667F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone16.texOffs(64, 25).addBox(-0.5F, -1.5F, -4.3667F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone16.texOffs(64, 25).addBox(-0.5F, -1.5F, -3.1667F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone16.texOffs(64, 25).addBox(-0.5F, -1.5F, -5.5667F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone16.texOffs(64, 25).addBox(-0.5F, -1.5F, -16.7667F, 1.0F, 3.0F, 11.0F, 0.0F, false);

        bone17 = new ModelRenderer(this);
        bone17.setPos(0.0F, -25.5352F, -35.4333F);
        thompson.addChild(bone17);
        setRotationAngle(bone17, 0.0F, 0.0F, -1.5708F);
        bone17.texOffs(64, 25).addBox(-0.5F, -1.5F, 6.4333F, 1.0F, 3.0F, 3.0F, 0.0F, false);
        bone17.texOffs(64, 25).addBox(-0.5F, -1.5F, 5.2333F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone17.texOffs(64, 25).addBox(-0.5F, -1.5F, 4.0333F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone17.texOffs(64, 25).addBox(-0.5F, -1.5F, 2.8333F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone17.texOffs(64, 25).addBox(-0.5F, -1.5F, 1.6333F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone17.texOffs(64, 25).addBox(-0.5F, -1.5F, 0.4333F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone17.texOffs(64, 25).addBox(-0.5F, -1.5F, -0.7667F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone17.texOffs(64, 25).addBox(-0.5F, -1.5F, -1.9667F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone17.texOffs(64, 25).addBox(-0.5F, -1.5F, -4.3667F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone17.texOffs(64, 25).addBox(-0.5F, -1.5F, -3.1667F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone17.texOffs(64, 25).addBox(-0.5F, -1.5F, -5.5667F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone17.texOffs(64, 25).addBox(-0.5F, -1.5F, -16.7667F, 1.0F, 3.0F, 11.0F, 0.0F, false);

        bone18 = new ModelRenderer(this);
        bone18.setPos(0.0F, -25.5352F, -35.4333F);
        thompson.addChild(bone18);
        setRotationAngle(bone18, 0.0F, 0.0F, -2.3562F);
        bone18.texOffs(64, 25).addBox(-0.5F, -1.5F, 6.4333F, 1.0F, 3.0F, 3.0F, 0.0F, false);
        bone18.texOffs(64, 25).addBox(-0.5F, -1.5F, 5.2333F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone18.texOffs(64, 25).addBox(-0.5F, -1.5F, 4.0333F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone18.texOffs(64, 25).addBox(-0.5F, -1.5F, 2.8333F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone18.texOffs(64, 25).addBox(-0.5F, -1.5F, 1.6333F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone18.texOffs(64, 25).addBox(-0.5F, -1.5F, 0.4333F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone18.texOffs(64, 25).addBox(-0.5F, -1.5F, -0.7667F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone18.texOffs(64, 25).addBox(-0.5F, -1.5F, -1.9667F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone18.texOffs(64, 25).addBox(-0.5F, -1.5F, -4.3667F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone18.texOffs(64, 25).addBox(-0.5F, -1.5F, -3.1667F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone18.texOffs(64, 25).addBox(-0.5F, -1.5F, -5.5667F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone18.texOffs(64, 25).addBox(-0.5F, -1.5F, -16.7667F, 1.0F, 3.0F, 11.0F, 0.0F, false);

        bone24 = new ModelRenderer(this);
        bone24.setPos(0.0F, -10.9722F, 40.0212F);
        thompson.addChild(bone24);
        setRotationAngle(bone24, -0.0873F, 0.0F, 0.0F);
        bone24.texOffs(165, 156).addBox(-2.0F, -3.8773F, -4.7491F, 4.0F, 8.0F, 5.0F, 0.0F, false);

        bone25 = new ModelRenderer(this);
        bone25.setPos(0.0F, -10.9722F, 34.0212F);
        thompson.addChild(bone25);
        setRotationAngle(bone25, -0.5236F, 0.0F, 0.0F);
        bone25.texOffs(165, 156).addBox(-2.0F, -6.2565F, -8.3657F, 4.0F, 9.0F, 11.0F, 0.0F, false);
        bone25.texOffs(165, 156).addBox(-2.0F, -3.2565F, -12.3657F, 4.0F, 6.0F, 4.0F, 0.0F, false);

        bone23 = new ModelRenderer(this);
        bone23.setPos(0.0F, -39.5449F, 7.1166F);
        thompson.addChild(bone23);
        setRotationAngle(bone23, -1.0472F, 0.0F, 0.0F);
        bone23.texOffs(165, 156).addBox(-2.0F, -9.3251F, 25.3284F, 4.0F, 4.0F, 3.0F, 0.0F, false);

        bone22 = new ModelRenderer(this);
        bone22.setPos(-1.0F, 0.0F, 0.0F);
        thompson.addChild(bone22);
        setRotationAngle(bone22, -0.2618F, 0.0F, 0.0F);
        bone22.texOffs(165, 156).addBox(-1.0F, -26.7693F, 16.6385F, 4.0F, 2.0F, 3.0F, 0.0F, false);
        bone22.texOffs(165, 156).addBox(-1.0F, -24.9021F, 11.6385F, 4.0F, 5.0F, 8.0F, 0.0F, false);
        bone22.texOffs(165, 156).addBox(-1.0F, -28.7233F, 21.1428F, 4.0F, 4.0F, 11.0F, 0.0F, false);
        bone22.texOffs(165, 156).addBox(-1.0F, -28.7233F, 32.1428F, 4.0F, 5.0F, 4.0F, 0.0F, false);

        bone27 = new ModelRenderer(this);
        bone27.setPos(-1.0F, 0.0F, 0.0F);
        thompson.addChild(bone27);
        setRotationAngle(bone27, -0.7854F, 0.0F, 0.0F);
        bone27.texOffs(165, 156).addBox(-1.0F, -20.7742F, -6.0463F, 4.0F, 2.0F, 2.0F, 0.0F, false);

        bone20 = new ModelRenderer(this);
        bone20.setPos(1.4648F, -30.9571F, 6.0F);
        thompson.addChild(bone20);
        setRotationAngle(bone20, 0.1745F, 0.0F, 0.0F);
        bone20.texOffs(67, 15).addBox(-0.5F, 1.4022F, 1.216F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone20.texOffs(67, 15).addBox(-3.4295F, 1.4022F, 1.216F, 1.0F, 3.0F, 1.0F, 0.0F, true);

        bone21 = new ModelRenderer(this);
        bone21.setPos(1.4648F, -30.9571F, 4.375F);
        thompson.addChild(bone21);
        setRotationAngle(bone21, -0.1745F, 0.0F, 0.0F);
        bone21.texOffs(67, 15).addBox(-0.5F, 1.1458F, -0.7618F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone21.texOffs(67, 15).addBox(-3.4295F, 1.1458F, -0.7618F, 1.0F, 3.0F, 1.0F, 0.0F, true);

        bone7 = new ModelRenderer(this);
        bone7.setPos(2.0F, -27.0508F, -2.0F);
        thompson.addChild(bone7);
        setRotationAngle(bone7, 0.0F, 0.0F, -0.7854F);
        bone7.texOffs(65, 14).addBox(-1.0F, -0.2929F, -18.0F, 1.0F, 1.0F, 29.0F, 0.0F, false);
        bone7.texOffs(65, 14).addBox(-1.0F, -0.2929F, 11.0F, 1.0F, 1.0F, 12.0F, 0.0F, false);
        bone7.texOffs(65, 14).addBox(-1.0F, -0.2929F, -24.0F, 1.0F, 1.0F, 6.0F, 0.0F, false);
        bone7.texOffs(65, 14).addBox(-3.8284F, 2.5355F, -24.0F, 1.0F, 1.0F, 6.0F, 0.0F, false);
        bone7.texOffs(65, 14).addBox(-3.5355F, -2.8284F, -18.0F, 1.0F, 1.0F, 29.0F, 0.0F, false);
        bone7.texOffs(65, 14).addBox(-3.5355F, -2.8284F, 11.0F, 1.0F, 1.0F, 12.0F, 0.0F, false);
        bone7.texOffs(65, 14).addBox(-3.5355F, -2.8284F, -24.0F, 1.0F, 1.0F, 6.0F, 0.0F, false);
        bone7.texOffs(65, 14).addBox(-6.364F, 0.0F, -24.0F, 1.0F, 1.0F, 6.0F, 0.0F, false);
        bone7.texOffs(152, 152).addBox(-6.1569F, 1.2071F, -47.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone7.texOffs(152, 152).addBox(-5.0355F, 2.3284F, -47.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone7.texOffs(152, 152).addBox(-6.1569F, 1.2071F, -36.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone7.texOffs(152, 152).addBox(-5.0355F, 2.3284F, -36.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);

        bone14 = new ModelRenderer(this);
        bone14.setPos(0.0F, -16.3359F, -4.5F);
        thompson.addChild(bone14);
        setRotationAngle(bone14, -0.3491F, 0.0F, 0.0F);
        bone14.texOffs(67, 15).addBox(-1.0F, -2.275F, -0.2317F, 2.0F, 2.0F, 1.0F, 0.0F, false);

        bone12 = new ModelRenderer(this);
        bone12.setPos(-1.5F, 2.9023F, 8.8242F);
        thompson.addChild(bone12);
        setRotationAngle(bone12, 0.3491F, 0.0F, 0.0F);
        bone12.texOffs(165, 156).addBox(-0.5F, -19.5508F, -1.0F, 4.0F, 8.0F, 5.0F, 0.0F, false);

        bone26 = new ModelRenderer(this);
        bone26.setPos(-0.5F, 0.0F, 0.0F);
        bone12.addChild(bone26);
        setRotationAngle(bone26, 0.3491F, 0.0F, 0.0F);
        bone26.texOffs(165, 156).addBox(0.0F, -11.4861F, 2.7094F, 4.0F, 2.0F, 5.0F, 0.0F, false);
        bone26.texOffs(165, 156).addBox(0.0F, -9.4861F, 3.7094F, 4.0F, 1.0F, 3.0F, 0.0F, false);

        bone13 = new ModelRenderer(this);
        bone13.setPos(-1.5F, -7.0313F, 13.2969F);
        thompson.addChild(bone13);
        setRotationAngle(bone13, 0.9599F, 0.0F, 0.0F);
        bone13.texOffs(165, 156).addBox(-0.5F, -14.5508F, -1.0F, 4.0F, 1.0F, 1.0F, 0.0F, false);

        bone35 = new ModelRenderer(this);
        bone35.setPos(-1.0F, 0.0F, 0.0F);
        thompson.addChild(bone35);
        setRotationAngle(bone35, -0.7854F, 0.0F, 0.0F);
        bone35.texOffs(67, 15).addBox(-1.0F, -4.5108F, -20.0671F, 4.0F, 2.0F, 2.0F, 0.0F, false);
        bone35.texOffs(67, 15).addBox(-1.0F, -3.5108F, -20.8956F, 4.0F, 1.0F, 1.0F, 0.0F, false);

        bone5 = new ModelRenderer(this);
        bone5.setPos(0.0F, -16.0508F, -9.5F);
        thompson.addChild(bone5);
        setRotationAngle(bone5, -0.5236F, 0.0F, 0.0F);
        bone5.texOffs(67, 15).addBox(-2.0F, -0.317F, -0.451F, 4.0F, 1.0F, 2.0F, 0.0F, false);
        bone5.texOffs(67, 15).addBox(-2.0F, -4.1124F, -2.9084F, 4.0F, 2.0F, 1.0F, 0.0F, false);
        bone5.texOffs(67, 15).addBox(-2.0F, -8.4151F, 2.1112F, 4.0F, 2.0F, 4.0F, 0.0F, false);

        bone6 = new ModelRenderer(this);
        bone6.setPos(0.0F, -16.0508F, -9.5F);
        thompson.addChild(bone6);
        setRotationAngle(bone6, -1.0472F, 0.0F, 0.0F);
        bone6.texOffs(67, 15).addBox(-2.0F, -0.2945F, -1.857F, 4.0F, 1.0F, 2.0F, 0.0F, false);
        bone6.texOffs(67, 15).addBox(-2.0F, -3.6232F, -4.3826F, 4.0F, 2.0F, 1.0F, 0.0F, false);

        bone3 = new ModelRenderer(this);
        bone3.setPos(0.0F, -15.0508F, -5.0F);
        thompson.addChild(bone3);
        setRotationAngle(bone3, 0.0F, -0.3491F, 0.0F);
        bone3.texOffs(67, 15).addBox(-0.1467F, -0.5F, -3.5031F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        bone4 = new ModelRenderer(this);
        bone4.setPos(0.0F, -15.0508F, -5.0F);
        thompson.addChild(bone4);
        setRotationAngle(bone4, 0.0F, 0.3491F, 0.0F);
        bone4.texOffs(67, 15).addBox(-0.8533F, -0.5F, -3.5031F, 1.0F, 1.0F, 2.0F, 0.0F, true);

        magazine1 = new ModelRenderer(this);
        magazine1.setPos(0.0F, 21.0742F, 0.0F);
        magazine1.texOffs(20, 146).addBox(-2.0F, -1.0F, -20.0F, 4.0F, 2.0F, 7.0F, 0.0F, false);
        magazine1.texOffs(20, 146).addBox(1.0F, -24.0F, -20.0F, 1.0F, 3.0F, 7.0F, 0.0F, false);
        magazine1.texOffs(20, 146).addBox(-2.0F, -24.0F, -20.0F, 1.0F, 3.0F, 7.0F, 0.0F, false);
        magazine1.texOffs(20, 146).addBox(-1.0F, -23.0F, -14.0F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        magazine1.texOffs(20, 146).addBox(-1.0F, -23.0F, -20.0F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        magazine1.texOffs(20, 146).addBox(-2.0F, -21.0F, -14.0F, 4.0F, 20.0F, 1.0F, 0.0F, false);
        magazine1.texOffs(20, 146).addBox(-1.5F, -21.0F, -15.0F, 3.0F, 20.0F, 1.0F, 0.0F, false);
        magazine1.texOffs(20, 146).addBox(-2.0F, -21.0F, -20.0F, 4.0F, 20.0F, 5.0F, 0.0F, false);

        bone = new ModelRenderer(this);
        bone.setPos(0.0F, 0.0F, 0.0F);
        magazine1.addChild(bone);
        setRotationAngle(bone, 0.0F, 0.0F, 0.2618F);
        bone.texOffs(20, 146).addBox(-4.5033F, -20.8021F, -15.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        bone2 = new ModelRenderer(this);
        bone2.setPos(0.0F, 0.0F, 0.0F);
        magazine1.addChild(bone2);
        setRotationAngle(bone2, 0.0F, 0.0F, -0.2618F);
        bone2.texOffs(20, 146).addBox(3.5033F, -20.8021F, -15.0F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        magazine2 = new ModelRenderer(this);
        magazine2.setPos(0.0F, 21.5938F, 0.0F);
        magazine2.texOffs(29, 169).addBox(1.0F, -24.0F, -20.0F, 1.0F, 3.0F, 7.0F, 0.0F, false);
        magazine2.texOffs(29, 169).addBox(-2.0F, -24.0F, -20.0F, 1.0F, 3.0F, 7.0F, 0.0F, false);
        magazine2.texOffs(29, 169).addBox(-1.0F, -23.0F, -14.0F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        magazine2.texOffs(29, 169).addBox(-2.0F, -21.0F, -20.0F, 4.0F, 8.0F, 7.0F, 0.0F, false);
        magazine2.texOffs(29, 169).addBox(-8.3374F, -10.5158F, -20.0F, 2.0F, 1.0F, 7.0F, 0.0F, false);
        magazine2.texOffs(29, 169).addBox(6.3374F, -10.5158F, -20.0F, 2.0F, 1.0F, 7.0F, 0.0F, true);
        magazine2.texOffs(29, 169).addBox(-1.0F, -23.0F, -20.0F, 2.0F, 2.0F, 1.0F, 0.0F, false);

        bone28 = new ModelRenderer(this);
        bone28.setPos(0.0F, 0.0F, 0.0F);
        magazine2.addChild(bone28);
        setRotationAngle(bone28, 0.0F, 0.0F, 0.2618F);


        bone29 = new ModelRenderer(this);
        bone29.setPos(0.0F, 0.0F, 0.0F);
        magazine2.addChild(bone29);
        setRotationAngle(bone29, 0.0F, 0.0F, -0.2618F);


        bone30 = new ModelRenderer(this);
        bone30.setPos(0.0F, 0.0F, 0.0F);
        magazine2.addChild(bone30);
        bone30.texOffs(29, 169).addBox(-2.5F, -18.0F, -20.0F, 5.0F, 16.0F, 7.0F, 0.0F, false);
        bone30.texOffs(78, 24).addBox(-3.0F, -13.0F, -20.4414F, 6.0F, 6.0F, 8.0F, 0.0F, false);
        bone30.texOffs(78, 24).addBox(-2.0F, -12.0F, -21.4414F, 4.0F, 4.0F, 10.0F, 0.0F, false);

        bone31 = new ModelRenderer(this);
        bone31.setPos(0.0F, -10.0F, -16.5F);
        magazine2.addChild(bone31);
        setRotationAngle(bone31, 0.0F, 0.0F, 0.6109F);
        bone31.texOffs(29, 169).addBox(-2.5F, -8.0F, -3.5F, 5.0F, 16.0F, 7.0F, 0.0F, false);

        bone32 = new ModelRenderer(this);
        bone32.setPos(0.0F, -10.0F, -16.5F);
        magazine2.addChild(bone32);
        setRotationAngle(bone32, 0.0F, 0.0F, -0.6109F);
        bone32.texOffs(29, 169).addBox(-2.5F, -8.0F, -3.5F, 5.0F, 16.0F, 7.0F, 0.0F, false);

        bone33 = new ModelRenderer(this);
        bone33.setPos(0.0F, -10.0F, -16.5F);
        magazine2.addChild(bone33);
        setRotationAngle(bone33, 0.0F, 0.0F, -1.2217F);
        bone33.texOffs(29, 169).addBox(-2.5F, -8.0F, -3.5F, 5.0F, 16.0F, 7.0F, 0.0F, false);

        bone34 = new ModelRenderer(this);
        bone34.setPos(0.0F, -10.0F, -16.5F);
        magazine2.addChild(bone34);
        setRotationAngle(bone34, 0.0F, 0.0F, 1.2217F);
        bone34.texOffs(29, 169).addBox(-2.5F, -8.0F, -3.5F, 5.0F, 16.0F, 7.0F, 0.0F, false);

        charging_handle = new ModelRenderer(this);
        charging_handle.setPos(0.7929F, 24.0F, 0.0F);
        charging_handle.texOffs(49, 45).addBox(-0.8929F, -28.7462F, -14.9547F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        charging_handle.texOffs(49, 45).addBox(-1.6929F, -28.7462F, -14.9547F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        charging_handle.texOffs(49, 45).addBox(-1.6929F, -28.7462F, -14.1547F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        charging_handle.texOffs(49, 45).addBox(-0.8929F, -28.7462F, -14.1547F, 1.0F, 1.0F, 1.0F, 0.0F, true);

        bone11 = new ModelRenderer(this);
        bone11.setPos(-0.7929F, -27.4845F, -14.0547F);
        charging_handle.addChild(bone11);
        setRotationAngle(bone11, 0.0F, 0.0F, -0.1745F);
        bone11.texOffs(51, 48).addBox(-0.3339F, -0.898F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        bone10 = new ModelRenderer(this);
        bone10.setPos(-0.7929F, -27.4845F, -14.0547F);
        charging_handle.addChild(bone10);
        setRotationAngle(bone10, 0.0F, 0.0F, 0.1745F);
        bone10.texOffs(51, 48).addBox(-0.6661F, -0.898F, -0.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        bone9 = new ModelRenderer(this);
        bone9.setPos(-0.7929F, -27.4845F, -14.0547F);
        charging_handle.addChild(bone9);
        setRotationAngle(bone9, -0.1745F, 0.0F, 0.0F);
        bone9.texOffs(51, 48).addBox(-0.5F, -0.898F, -0.6661F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        bone8 = new ModelRenderer(this);
        bone8.setPos(-0.7929F, -27.4845F, -14.0547F);
        charging_handle.addChild(bone8);
        setRotationAngle(bone8, 0.1745F, 0.0F, 0.0F);
        bone8.texOffs(51, 48).addBox(-0.5F, -0.898F, -0.3339F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        bullet = new ModelRenderer(this);
        bullet.setPos(0.0F, 21.0742F, 0.0F);
        bullet.texOffs(6, 505).addBox(-1.0F, -23.418F, -15.0F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        bullet.texOffs(6, 505).addBox(-1.0F, -23.418F, -17.3828F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        bullet.texOffs(6, 505).addBox(-0.9F, -23.318F, -18.2836F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bullet.texOffs(6, 505).addBox(-0.7F, -22.718F, -18.8437F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bullet.texOffs(6, 505).addBox(-0.7F, -23.118F, -18.8437F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bullet.texOffs(6, 505).addBox(-0.3F, -22.718F, -18.8437F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bullet.texOffs(6, 505).addBox(-0.3F, -23.118F, -18.8437F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bullet.texOffs(0, 512).addBox(-0.9F, -22.518F, -18.2836F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bullet.texOffs(6, 505).addBox(-0.1F, -23.318F, -18.2836F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bullet.texOffs(0, 512).addBox(-0.1F, -22.518F, -18.2836F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bullet.texOffs(6, 505).addBox(-0.8F, -23.218F, -15.7305F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bullet.texOffs(6, 505).addBox(-0.8F, -22.618F, -15.7305F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bullet.texOffs(6, 505).addBox(-0.2F, -23.218F, -15.7305F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bullet.texOffs(6, 505).addBox(-0.2F, -22.618F, -15.7305F, 1.0F, 1.0F, 1.0F, 0.0F, true);

        // TODO extended mag
        setSpecialRenderer(ModAnimations.MAGAZINE, magazine1);
        setSpecialRenderer(ModAnimations.CHARGING_HANDLE, charging_handle);
        setBulletRenderer(setSpecialRenderer(ModAnimations.BULLET, bullet));
    }
}
