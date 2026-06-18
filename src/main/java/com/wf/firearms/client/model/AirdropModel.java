package com.wf.firearms.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.wf.firearms.client.model.legacy.LegacyModelRenderer;

public class AirdropModel extends AbstractSolidEntityModel {

    private final LegacyModelRenderer drop;
    private final LegacyModelRenderer box;
    private final LegacyModelRenderer detail;
    private final LegacyModelRenderer cloth;
    private final LegacyModelRenderer detail2;
    private final LegacyModelRenderer chute;
    private final LegacyModelRenderer strings;
    private final LegacyModelRenderer a0;
    private final LegacyModelRenderer a1;
    private final LegacyModelRenderer a2;
    private final LegacyModelRenderer a3;
    private final LegacyModelRenderer a4;
    private final LegacyModelRenderer a5;
    private final LegacyModelRenderer a6;
    private final LegacyModelRenderer a7;
    private final LegacyModelRenderer base;
    private final LegacyModelRenderer b0;
    private final LegacyModelRenderer b1;
    private final LegacyModelRenderer b2;
    private final LegacyModelRenderer b3;
    private final LegacyModelRenderer b4;
    private final LegacyModelRenderer b5;
    private final LegacyModelRenderer b6;
    private final LegacyModelRenderer b7;

    public AirdropModel() {
        texWidth = 128;
        texHeight = 128;

        drop = new LegacyModelRenderer(this);
        drop.setPos(0.0F, 24.0F, 0.0F);


        box = new LegacyModelRenderer(this);
        box.setPos(0.0F, 0.0F, 0.0F);
        drop.addChild(box);
        box.texOffs(0, 0).addBox(-7.5F, -0.9F, -7.5F, 15.0F, 1.0F, 15.0F, 0.0F, false);
        box.texOffs(82, 0).addBox(-7.0F, -12.0F, 6.0F, 14.0F, 12.0F, 1.0F, 0.0F, false);
        box.texOffs(85, 0).addBox(-7.0F, -12.0F, -7.0F, 14.0F, 12.0F, 1.0F, 0.0F, false);
        box.texOffs(82, 0).addBox(-7.0F, -12.0F, -6.0F, 1.0F, 12.0F, 12.0F, 0.0F, false);
        box.texOffs(82, 0).addBox(6.0F, -12.0F, -6.0F, 1.0F, 12.0F, 12.0F, 0.0F, false);

        detail = new LegacyModelRenderer(this);
        detail.setPos(0.0F, 0.0F, 0.0F);
        box.addChild(detail);
        detail.texOffs(0, 0).addBox(-7.5F, -12.0F, -7.5F, 2.0F, 12.0F, 2.0F, 0.0F, false);
        detail.texOffs(0, 0).addBox(5.5F, -12.0F, -7.5F, 2.0F, 12.0F, 2.0F, 0.0F, false);
        detail.texOffs(0, 0).addBox(5.5F, -12.0F, 5.5F, 2.0F, 12.0F, 2.0F, 0.0F, false);
        detail.texOffs(0, 0).addBox(-7.5F, -12.0F, 5.5F, 2.0F, 12.0F, 2.0F, 0.0F, false);
        detail.texOffs(0, 0).addBox(-3.25F, -12.0F, -7.5F, 2.0F, 12.0F, 1.0F, 0.0F, false);
        detail.texOffs(0, 0).addBox(1.25F, -12.0F, -7.5F, 2.0F, 12.0F, 1.0F, 0.0F, false);
        detail.texOffs(0, 0).addBox(-3.25F, -12.0F, 6.5F, 2.0F, 12.0F, 1.0F, 0.0F, false);
        detail.texOffs(0, 0).addBox(1.25F, -12.0F, 6.5F, 2.0F, 12.0F, 1.0F, 0.0F, false);
        detail.texOffs(0, 0).addBox(-7.5F, -12.0F, 1.25F, 1.0F, 12.0F, 2.0F, 0.0F, false);
        detail.texOffs(0, 0).addBox(-7.5F, -12.0F, -3.25F, 1.0F, 12.0F, 2.0F, 0.0F, false);
        detail.texOffs(0, 0).addBox(6.5F, -12.0F, 1.25F, 1.0F, 12.0F, 2.0F, 0.0F, false);
        detail.texOffs(0, 0).addBox(6.5F, -12.0F, -3.25F, 1.0F, 12.0F, 2.0F, 0.0F, false);

        cloth = new LegacyModelRenderer(this);
        cloth.setPos(0.0F, 0.0F, 0.0F);
        drop.addChild(cloth);
        cloth.texOffs(82, 71).addBox(-8.0F, -13.0F, -8.0F, 16.0F, 1.0F, 7.0F, 0.0F, false);
        cloth.texOffs(82, 71).addBox(-8.0F, -13.0F, 1.0F, 16.0F, 1.0F, 7.0F, 0.0F, false);
        cloth.texOffs(82, 71).addBox(-8.0F, -13.0F, -1.0F, 16.0F, 1.0F, 2.0F, 0.0F, false);
        cloth.texOffs(82, 71).addBox(-8.0F, -12.0F, 7.0F, 16.0F, 2.0F, 1.0F, 0.0F, false);
        cloth.texOffs(82, 71).addBox(-8.0F, -12.0F, -8.0F, 16.0F, 2.0F, 1.0F, 0.0F, false);
        cloth.texOffs(82, 71).addBox(-8.0F, -12.0F, -7.0F, 1.0F, 2.0F, 14.0F, 0.0F, false);
        cloth.texOffs(82, 71).addBox(7.0F, -12.0F, -7.0F, 1.0F, 2.0F, 14.0F, 0.0F, false);

        detail2 = new LegacyModelRenderer(this);
        detail2.setPos(0.0F, 0.0F, 0.0F);
        cloth.addChild(detail2);
        detail2.texOffs(82, 114).addBox(7.1F, -13.1F, 3.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        detail2.texOffs(82, 114).addBox(7.1F, -13.1F, -4.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        detail2.texOffs(82, 114).addBox(-8.1F, -13.1F, 3.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        detail2.texOffs(82, 114).addBox(-8.1F, -13.1F, -4.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        detail2.texOffs(82, 114).addBox(3.0F, -13.1F, -8.1F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        detail2.texOffs(82, 114).addBox(-4.0F, -13.1F, -8.1F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        detail2.texOffs(82, 114).addBox(-4.0F, -13.1F, 7.1F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        detail2.texOffs(82, 114).addBox(3.0F, -13.1F, 7.1F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        detail2.texOffs(82, 114).addBox(-7.4F, -13.1F, -4.0F, 15.0F, 1.0F, 1.0F, 0.0F, false);
        detail2.texOffs(82, 114).addBox(-7.4F, -13.1F, 3.0F, 15.0F, 1.0F, 1.0F, 0.0F, false);
        detail2.texOffs(82, 114).addBox(-4.0F, -13.1F, -5.7F, 1.0F, 1.0F, 13.0F, 0.0F, false);
        detail2.texOffs(82, 114).addBox(3.0F, -13.1F, -5.7F, 1.0F, 1.0F, 13.0F, 0.0F, false);
        detail2.texOffs(82, 114).addBox(3.0F, -13.1F, -7.7F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        detail2.texOffs(82, 114).addBox(-4.0F, -13.1F, -7.7F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        chute = new LegacyModelRenderer(this);
        chute.setPos(0.0F, 0.0F, 0.0F);
        drop.addChild(chute);


        strings = new LegacyModelRenderer(this);
        strings.setPos(0.0F, 0.0F, 0.0F);
        chute.addChild(strings);
        strings.texOffs(0, 70).addBox(-1.0F, -13.2F, -1.0F, 2.0F, 1.0F, 2.0F, 0.0F, false);

        a0 = new LegacyModelRenderer(this);
        a0.setPos(0.0F, 0.0F, 0.0F);
        strings.addChild(a0);
        setRotationAngle(a0, -0.3491F, 0.0F, 0.0F);
        a0.texOffs(0, 70).addBox(-0.5F, -53.0F, -4.0F, 1.0F, 41.0F, 1.0F, 0.0F, false);

        a1 = new LegacyModelRenderer(this);
        a1.setPos(0.0F, 0.0F, 0.0F);
        strings.addChild(a1);
        setRotationAngle(a1, -0.3491F, 0.0F, -0.3491F);
        a1.texOffs(0, 70).addBox(3.25F, -52.0F, -4.0F, 1.0F, 40.0F, 1.0F, 0.0F, false);

        a2 = new LegacyModelRenderer(this);
        a2.setPos(0.0F, 0.0F, 0.0F);
        strings.addChild(a2);
        setRotationAngle(a2, 0.0F, 0.0F, -0.3491F);
        a2.texOffs(0, 70).addBox(3.0F, -52.0F, -0.5F, 1.0F, 40.0F, 1.0F, 0.0F, false);

        a3 = new LegacyModelRenderer(this);
        a3.setPos(0.0F, 0.0F, 0.0F);
        strings.addChild(a3);
        setRotationAngle(a3, 0.3491F, 0.0F, -0.3491F);
        a3.texOffs(0, 70).addBox(3.0F, -51.0F, 3.0F, 1.0F, 39.0F, 1.0F, 0.0F, false);

        a4 = new LegacyModelRenderer(this);
        a4.setPos(0.0F, 0.0F, 0.0F);
        strings.addChild(a4);
        setRotationAngle(a4, 0.3491F, 0.0F, 0.0F);
        a4.texOffs(0, 70).addBox(-0.5F, -53.0F, 3.0F, 1.0F, 41.0F, 1.0F, 0.0F, false);

        a5 = new LegacyModelRenderer(this);
        a5.setPos(0.0F, 0.0F, 0.0F);
        strings.addChild(a5);
        setRotationAngle(a5, 0.3491F, 0.0F, 0.3491F);
        a5.texOffs(0, 70).addBox(-4.0F, -52.0F, 3.0F, 1.0F, 40.0F, 1.0F, 0.0F, false);

        a6 = new LegacyModelRenderer(this);
        a6.setPos(0.0F, 0.0F, 0.0F);
        strings.addChild(a6);
        setRotationAngle(a6, 0.0F, 0.0F, 0.3491F);
        a6.texOffs(0, 70).addBox(-4.0F, -53.0F, -0.5F, 1.0F, 41.0F, 1.0F, 0.0F, false);

        a7 = new LegacyModelRenderer(this);
        a7.setPos(0.0F, 0.0F, 0.0F);
        strings.addChild(a7);
        setRotationAngle(a7, -0.3491F, 0.0F, 0.3491F);
        a7.texOffs(0, 70).addBox(-4.0F, -52.0F, -4.0F, 1.0F, 40.0F, 1.0F, 0.0F, false);

        base = new LegacyModelRenderer(this);
        base.setPos(0.0F, 0.0F, 0.0F);
        chute.addChild(base);
        base.texOffs(0, 70).addBox(-10.0F, -54.0F, 3.0F, 20.0F, 1.0F, 7.0F, 0.0F, false);
        base.texOffs(0, 70).addBox(-10.0F, -54.0F, -10.0F, 20.0F, 1.0F, 7.0F, 0.0F, false);
        base.texOffs(0, 70).addBox(3.0F, -54.0F, -3.0F, 7.0F, 1.0F, 6.0F, 0.0F, false);
        base.texOffs(0, 70).addBox(-10.0F, -54.0F, -3.0F, 7.0F, 1.0F, 6.0F, 0.0F, false);

        b0 = new LegacyModelRenderer(this);
        b0.setPos(0.0F, 0.0F, 0.0F);
        base.addChild(b0);
        setRotationAngle(b0, 0.5236F, 0.0F, 0.0F);
        b0.texOffs(0, 70).addBox(-10.0F, -51.7F, -1.6F, 20.0F, 1.0F, 20.0F, 0.0F, false);

        b1 = new LegacyModelRenderer(this);
        b1.setPos(0.0F, 0.0F, 0.0F);
        base.addChild(b1);
        setRotationAngle(b1, -0.5236F, 0.0F, 0.0F);
        b1.texOffs(0, 70).addBox(-10.0F, -51.8F, -18.3F, 20.0F, 1.0F, 20.0F, 0.0F, false);

        b2 = new LegacyModelRenderer(this);
        b2.setPos(0.0F, 0.0F, 0.0F);
        base.addChild(b2);
        setRotationAngle(b2, 0.0F, 0.0F, -0.5236F);
        b2.texOffs(0, 70).addBox(-1.7F, -51.7F, -10.0F, 20.0F, 1.0F, 20.0F, 0.0F, false);

        b3 = new LegacyModelRenderer(this);
        b3.setPos(0.0F, 0.0F, 0.0F);
        base.addChild(b3);
        setRotationAngle(b3, 0.0F, 0.0F, 0.5236F);
        b3.texOffs(0, 70).addBox(-18.3F, -51.8F, -10.0F, 20.0F, 1.0F, 20.0F, 0.0F, false);

        b4 = new LegacyModelRenderer(this);
        b4.setPos(0.0F, 0.0F, 0.0F);
        base.addChild(b4);
        setRotationAngle(b4, -0.5236F, -0.0873F, -0.5236F);
        b4.texOffs(0, 70).addBox(0.0F, -49.1F, -18.6F, 19.0F, 1.0F, 19.0F, 0.0F, false);

        b5 = new LegacyModelRenderer(this);
        b5.setPos(0.0F, 0.0F, 0.0F);
        base.addChild(b5);
        setRotationAngle(b5, 0.5236F, 0.1571F, -0.5236F);
        b5.texOffs(0, 70).addBox(0.6F, -48.5F, 0.7F, 19.0F, 1.0F, 19.0F, 0.0F, false);

        b6 = new LegacyModelRenderer(this);
        b6.setPos(0.0F, 0.0F, 0.0F);
        base.addChild(b6);
        setRotationAngle(b6, 0.5236F, -0.1222F, 0.5236F);
        b6.texOffs(0, 70).addBox(-19.4F, -48.9F, 0.1F, 19.0F, 1.0F, 19.0F, 0.0F, false);

        b7 = new LegacyModelRenderer(this);
        b7.setPos(0.0F, 0.0F, 0.0F);
        base.addChild(b7);
        setRotationAngle(b7, -0.5236F, 0.1745F, 0.5236F);
        b7.texOffs(0, 70).addBox(-19.7F, -48.5F, -19.9F, 19.0F, 1.0F, 19.0F, 0.0F, false);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        drop.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}

