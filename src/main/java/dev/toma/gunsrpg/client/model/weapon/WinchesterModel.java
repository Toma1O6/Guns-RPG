package dev.toma.gunsrpg.client.model.weapon;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.toma.gunsrpg.client.animation.ModAnimations;
import dev.toma.gunsrpg.common.capability.IPlayerData;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.item.ItemStack;

public class WinchesterModel extends AbstractWeaponModel {

    private final ModelRenderer win94;
    private final ModelRenderer bone2;
    private final ModelRenderer bone4;
    private final ModelRenderer bone5;
    private final ModelRenderer bone6;
    private final ModelRenderer bone7;
    private final ModelRenderer bone8;
    private final ModelRenderer bone9;
    private final ModelRenderer bone10;
    private final ModelRenderer bone11;
    private final ModelRenderer bone12;
    private final ModelRenderer bone13;
    private final ModelRenderer bone17;
    private final ModelRenderer barrel;
    private final ModelRenderer bone18;
    private final ModelRenderer barrel4;
    private final ModelRenderer bone21;
    private final ModelRenderer barrel5;
    private final ModelRenderer bone22;
    private final ModelRenderer barrel2;
    private final ModelRenderer bone19;
    private final ModelRenderer bone20;
    private final ModelRenderer lever;
    private final ModelRenderer bone15;
    private final ModelRenderer bone16;
    private final ModelRenderer bolt;
    private final ModelRenderer bone3;
    private final ModelRenderer bullet;

    @Override
    protected void renderWeapon(ItemStack stack, IPlayerData data, MatrixStack matrix, IVertexBuilder builder, int light, int overlay) {
        win94.render(matrix, builder, light, overlay);
    }

    public WinchesterModel() {
        texWidth = 512;
        texHeight = 512;

        win94 = new ModelRenderer(this);
        win94.setPos(0.0F, 24.0F, 0.0F);
        win94.texOffs(96, 38).addBox(-2.0F, -12.0F, -1.0F, 4.0F, 2.0F, 3.0F, 0.0F, false);
        win94.texOffs(96, 38).addBox(1.0F, -12.0F, 2.0F, 1.0F, 2.0F, 9.0F, 0.0F, false);
        win94.texOffs(96, 38).addBox(-2.0F, -12.0F, 2.0F, 1.0F, 2.0F, 9.0F, 0.0F, false);
        win94.texOffs(96, 38).addBox(2.0F, -14.0F, -1.0F, 1.0F, 3.0F, 12.0F, 0.0F, false);
        win94.texOffs(86, 39).addBox(-3.0F, -13.0F, -1.0F, 1.0F, 2.0F, 9.0F, 0.0F, false);
        win94.texOffs(86, 39).addBox(-3.0F, -17.0F, 8.0F, 1.0F, 6.0F, 7.0F, 0.0F, false);
        win94.texOffs(88, 36).addBox(-3.0F, -16.0F, -1.0F, 1.0F, 3.0F, 2.0F, 0.0F, false);
        win94.texOffs(96, 38).addBox(2.0F, -17.0F, -1.0F, 1.0F, 3.0F, 12.0F, 0.0F, false);
        win94.texOffs(86, 39).addBox(-3.0F, -17.0F, -1.0F, 1.0F, 1.0F, 9.0F, 0.0F, false);
        win94.texOffs(88, 36).addBox(-0.4142F, -18.4142F, -1.0F, 2.0F, 1.0F, 12.0F, 0.0F, false);
        win94.texOffs(88, 43).addBox(-1.5858F, -18.4142F, -1.0F, 2.0F, 1.0F, 12.0F, 0.0F, true);
        win94.texOffs(88, 36).addBox(-2.0F, -14.0F, 11.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);
        win94.texOffs(104, 38).addBox(2.0F, -14.0F, 11.0F, 1.0F, 3.0F, 4.0F, 0.0F, false);
        win94.texOffs(93, 38).addBox(1.0F, -17.0F, 11.0F, 2.0F, 3.0F, 4.0F, 0.0F, false);
        win94.texOffs(86, 39).addBox(-0.4142F, -18.4142F, 11.0F, 2.0F, 1.0F, 4.0F, 0.0F, false);
        win94.texOffs(86, 39).addBox(-1.5858F, -18.4142F, 11.0F, 2.0F, 1.0F, 4.0F, 0.0F, true);
        win94.texOffs(92, 38).addBox(-3.0F, -17.0F, -5.0F, 1.0F, 5.0F, 4.0F, 0.0F, false);
        win94.texOffs(96, 38).addBox(2.0F, -17.0F, -5.0F, 1.0F, 5.0F, 4.0F, 0.0F, false);
        win94.texOffs(96, 38).addBox(-0.4142F, -18.4142F, -5.0F, 2.0F, 1.0F, 4.0F, 0.0F, false);
        win94.texOffs(93, 42).addBox(-1.5858F, -18.4142F, -5.0F, 2.0F, 1.0F, 4.0F, 0.0F, true);
        win94.texOffs(96, 38).addBox(2.0F, -12.5F, -5.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        win94.texOffs(88, 36).addBox(-3.0F, -12.5F, -5.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        win94.texOffs(96, 38).addBox(-2.0F, -11.5F, -5.0F, 4.0F, 1.0F, 4.0F, 0.0F, false);
        win94.texOffs(136, 158).addBox(-2.5F, -17.2969F, -16.0F, 1.0F, 6.0F, 11.0F, 0.0F, false);
        win94.texOffs(136, 158).addBox(1.5F, -17.2969F, -16.0F, 1.0F, 6.0F, 11.0F, 0.0F, false);
        win94.texOffs(136, 158).addBox(-1.5F, -18.2969F, -16.0F, 3.0F, 1.0F, 11.0F, 0.0F, false);
        win94.texOffs(3, 73).addBox(-1.0F, -11.1563F, 2.0F, 2.0F, 1.0F, 9.0F, 0.0F, false);
        win94.texOffs(82, 25).addBox(-1.0F, -18.633F, -3.5F, 2.0F, 1.0F, 18.0F, 0.0F, false);
        win94.texOffs(136, 158).addBox(-1.5F, -11.2969F, -16.0F, 3.0F, 1.0F, 11.0F, 0.0F, false);
        win94.texOffs(136, 158).addBox(-1.5F, -11.2969F, -27.0F, 3.0F, 1.0F, 11.0F, 0.0F, false);
        win94.texOffs(136, 158).addBox(1.5F, -17.2969F, -27.0F, 1.0F, 6.0F, 11.0F, 0.0F, false);
        win94.texOffs(136, 158).addBox(-1.5F, -18.2969F, -27.0F, 3.0F, 1.0F, 11.0F, 0.0F, false);
        win94.texOffs(136, 158).addBox(-2.5F, -17.2969F, -27.0F, 1.0F, 6.0F, 11.0F, 0.0F, false);
        win94.texOffs(136, 158).addBox(-1.5F, -11.2969F, -39.0F, 3.0F, 1.0F, 11.0F, 0.0F, false);
        win94.texOffs(136, 158).addBox(1.5F, -17.2969F, -39.0F, 1.0F, 6.0F, 11.0F, 0.0F, false);
        win94.texOffs(136, 158).addBox(-1.5F, -18.2969F, -39.0F, 3.0F, 1.0F, 11.0F, 0.0F, false);
        win94.texOffs(136, 158).addBox(-2.5F, -17.2969F, -39.0F, 1.0F, 6.0F, 11.0F, 0.0F, false);
        win94.texOffs(27, 79).addBox(-1.5F, -11.2969F, -28.0F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        win94.texOffs(27, 79).addBox(1.5F, -17.2969F, -28.0F, 1.0F, 6.0F, 1.0F, 0.0F, false);
        win94.texOffs(27, 79).addBox(-1.5F, -18.2969F, -28.0F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        win94.texOffs(27, 79).addBox(-2.5F, -17.2969F, -28.0F, 1.0F, 6.0F, 1.0F, 0.0F, false);
        win94.texOffs(13, 73).addBox(-1.5F, -17.2969F, -33.3125F, 3.0F, 6.0F, 1.0F, 0.0F, false);
        win94.texOffs(24, 94).addBox(-1.5F, -19.258F, 5.0313F, 3.0F, 1.0F, 4.0F, 0.0F, false);
        win94.texOffs(82, 25).addBox(-1.0F, -18.633F, -21.5F, 2.0F, 1.0F, 18.0F, 0.0F, false);
        win94.texOffs(82, 25).addBox(-1.0F, -18.633F, -28.5F, 2.0F, 1.0F, 7.0F, 0.0F, false);
        win94.texOffs(92, 40).addBox(-1.0781F, -18.9611F, -1.75F, 2.0F, 1.0F, 7.0F, 0.0F, true);
        win94.texOffs(92, 40).addBox(-0.9219F, -18.9611F, -1.75F, 2.0F, 1.0F, 7.0F, 0.0F, true);
        win94.texOffs(84, 45).addBox(-1.5F, -18.5392F, 3.6875F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        win94.texOffs(84, 45).addBox(-1.5F, -18.5392F, 0.6875F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        win94.texOffs(84, 45).addBox(-1.5F, -18.5392F, -2.3125F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        win94.texOffs(24, 94).addBox(-1.0F, -19.7423F, 7.0313F, 2.0F, 1.0F, 1.0F, 0.0F, false);

        bone2 = new ModelRenderer(this);
        bone2.setPos(-3.5F, -12.5F, 5.0F);
        win94.addChild(bone2);
        setRotationAngle(bone2, 0.0F, 0.0F, -0.7854F);
        bone2.texOffs(86, 39).addBox(-0.7071F, 1.8284F, -6.0F, 1.0F, 1.0F, 12.0F, 0.0F, false);
        bone2.texOffs(86, 39).addBox(-0.7071F, 1.4142F, -6.0F, 1.0F, 1.0F, 12.0F, 0.0F, false);
        bone2.texOffs(96, 38).addBox(2.5355F, 4.6569F, -6.0F, 1.0F, 1.0F, 12.0F, 0.0F, false);
        bone2.texOffs(96, 38).addBox(2.1213F, 4.6569F, -6.0F, 1.0F, 1.0F, 12.0F, 0.0F, false);
        bone2.texOffs(92, 41).addBox(2.1213F, 4.6569F, 6.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        bone2.texOffs(88, 36).addBox(-0.7071F, 1.8284F, 6.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        bone2.texOffs(86, 39).addBox(-0.7071F, 1.4142F, 6.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        bone2.texOffs(101, 41).addBox(2.5355F, 4.6569F, 6.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        bone2.texOffs(96, 38).addBox(-0.3535F, 1.4749F, -10.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        bone2.texOffs(96, 38).addBox(2.4749F, 4.3033F, -10.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        bone2.texOffs(96, 38).addBox(2.8891F, 4.3033F, -10.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        bone2.texOffs(96, 38).addBox(-0.3535F, 1.0607F, -10.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);

        bone4 = new ModelRenderer(this);
        bone4.setPos(-3.5F, -19.0F, 5.0F);
        win94.addChild(bone4);
        setRotationAngle(bone4, 0.0F, 0.0F, 0.7854F);
        bone4.texOffs(86, 39).addBox(1.7678F, -0.9393F, -6.0F, 1.0F, 2.0F, 12.0F, 0.0F, false);
        bone4.texOffs(88, 34).addBox(4.0104F, -3.182F, -6.0F, 2.0F, 1.0F, 12.0F, 0.0F, false);
        bone4.texOffs(88, 36).addBox(4.0104F, -3.182F, 6.0F, 2.0F, 2.0F, 4.0F, 0.0F, false);
        bone4.texOffs(86, 39).addBox(1.7678F, -0.9393F, 6.0F, 2.0F, 2.0F, 4.0F, 0.0F, false);
        bone4.texOffs(88, 36).addBox(1.7678F, -0.9393F, -10.0F, 1.0F, 2.0F, 4.0F, 0.0F, false);
        bone4.texOffs(90, 42).addBox(4.0104F, -3.182F, -10.0F, 2.0F, 1.0F, 4.0F, 0.0F, false);
        bone4.texOffs(136, 158).addBox(4.4469F, -3.0384F, -21.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone4.texOffs(136, 158).addBox(1.9114F, -0.5028F, -21.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone4.texOffs(136, 158).addBox(4.0327F, -3.0384F, -21.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone4.texOffs(136, 158).addBox(1.9114F, -0.917F, -21.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone4.texOffs(136, 158).addBox(6.154F, 3.7398F, -21.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone4.texOffs(136, 158).addBox(6.5682F, 3.7398F, -21.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone4.texOffs(136, 158).addBox(8.6896F, 1.6185F, -21.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone4.texOffs(136, 158).addBox(8.6896F, 1.2043F, -21.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone4.texOffs(136, 158).addBox(6.154F, 3.7398F, -32.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone4.texOffs(136, 158).addBox(6.5682F, 3.7398F, -32.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone4.texOffs(136, 158).addBox(8.6896F, 1.6185F, -32.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone4.texOffs(136, 158).addBox(8.6896F, 1.2043F, -32.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone4.texOffs(136, 158).addBox(4.4469F, -3.0384F, -32.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone4.texOffs(136, 158).addBox(4.0327F, -3.0384F, -32.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone4.texOffs(136, 158).addBox(1.9114F, -0.917F, -32.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone4.texOffs(136, 158).addBox(1.9114F, -0.5028F, -32.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone4.texOffs(136, 158).addBox(6.154F, 3.7398F, -44.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone4.texOffs(136, 158).addBox(6.5682F, 3.7398F, -44.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone4.texOffs(136, 158).addBox(8.6896F, 1.6185F, -44.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone4.texOffs(136, 158).addBox(8.6896F, 1.2043F, -44.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone4.texOffs(136, 158).addBox(4.4469F, -3.0384F, -44.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone4.texOffs(136, 158).addBox(4.0327F, -3.0384F, -44.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone4.texOffs(136, 158).addBox(1.9114F, -0.917F, -44.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone4.texOffs(136, 158).addBox(1.9114F, -0.5028F, -44.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone4.texOffs(27, 79).addBox(6.154F, 3.7398F, -33.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone4.texOffs(27, 79).addBox(6.5682F, 3.7398F, -33.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone4.texOffs(27, 79).addBox(8.6896F, 1.6185F, -33.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone4.texOffs(27, 79).addBox(8.6896F, 1.2043F, -33.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone4.texOffs(27, 79).addBox(4.4469F, -3.0384F, -33.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone4.texOffs(27, 79).addBox(4.0327F, -3.0384F, -33.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone4.texOffs(27, 79).addBox(1.9114F, -0.917F, -33.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone4.texOffs(27, 79).addBox(1.9114F, -0.5028F, -33.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        bone5 = new ModelRenderer(this);
        bone5.setPos(0.0F, -17.6017F, 17.0F);
        win94.addChild(bone5);
        setRotationAngle(bone5, -0.7854F, 0.0F, 0.0F);
        bone5.texOffs(142, 142).addBox(-1.5F, 1.2706F, -1.5578F, 3.0F, 1.0F, 4.0F, 0.0F, true);
        bone5.texOffs(142, 142).addBox(1.2079F, 1.9777F, 0.1453F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone5.texOffs(142, 142).addBox(-2.2079F, 1.9777F, 0.1453F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone5.texOffs(37, 170).addBox(-0.5F, 1.0496F, -1.7007F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone5.texOffs(37, 170).addBox(-0.5F, 1.0496F, 0.2993F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone5.texOffs(37, 170).addBox(-1.0F, 0.9715F, -1.2007F, 2.0F, 1.0F, 2.0F, 0.0F, true);
        bone5.texOffs(99, 95).addBox(-0.5F, 0.3934F, -0.4038F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone5.texOffs(99, 95).addBox(-0.5F, -0.3566F, -0.7788F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone5.texOffs(99, 95).addBox(-0.5F, 0.284F, -0.8726F, 1.0F, 1.0F, 1.0F, 0.0F, true);

        bone6 = new ModelRenderer(this);
        bone6.setPos(0.0F, 0.0F, 0.0F);
        bone5.addChild(bone6);
        setRotationAngle(bone6, 0.0F, 0.0F, 0.7854F);
        bone6.texOffs(142, 142).addBox(-0.1622F, 1.9591F, -1.5578F, 1.0F, 1.0F, 4.0F, 0.0F, true);
        bone6.texOffs(142, 142).addBox(1.9591F, -0.1622F, -1.5578F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        bone6.texOffs(142, 142).addBox(2.7872F, -0.1622F, -1.761F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        bone6.texOffs(142, 142).addBox(-0.1622F, 2.7872F, -1.761F, 1.0F, 1.0F, 2.0F, 0.0F, true);

        bone7 = new ModelRenderer(this);
        bone7.setPos(0.0F, 0.0F, 0.0F);
        win94.addChild(bone7);
        setRotationAngle(bone7, 0.0F, -0.5236F, 0.0F);
        bone7.texOffs(152, 139).addBox(9.0981F, -16.0F, 11.4904F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        bone8 = new ModelRenderer(this);
        bone8.setPos(0.0F, 0.0F, 0.0F);
        win94.addChild(bone8);
        setRotationAngle(bone8, 0.0F, 0.5236F, 0.0F);
        bone8.texOffs(152, 139).addBox(-10.0981F, -16.0F, 11.4904F, 1.0F, 4.0F, 1.0F, 0.0F, true);

        bone9 = new ModelRenderer(this);
        bone9.setPos(0.0F, -15.5469F, 16.4219F);
        win94.addChild(bone9);
        setRotationAngle(bone9, -0.4363F, 0.0F, 0.0F);
        bone9.texOffs(145, 150).addBox(-1.5F, -0.5625F, -1.9688F, 3.0F, 5.0F, 12.0F, 0.0F, false);
        bone9.texOffs(145, 150).addBox(1.2071F, 0.1446F, -1.9688F, 1.0F, 2.0F, 12.0F, 0.0F, false);
        bone9.texOffs(145, 150).addBox(-2.2071F, 0.1446F, -1.9688F, 1.0F, 2.0F, 12.0F, 0.0F, true);
        bone9.texOffs(145, 150).addBox(-2.207F, 1.7304F, -1.9688F, 1.0F, 2.0F, 12.0F, 0.0F, true);
        bone9.texOffs(145, 150).addBox(1.207F, 1.7304F, -1.9688F, 1.0F, 2.0F, 12.0F, 0.0F, false);
        bone9.texOffs(145, 150).addBox(-1.5F, 3.4375F, 10.0313F, 3.0F, 1.0F, 10.0F, 0.0F, false);
        bone9.texOffs(145, 150).addBox(-1.5F, 3.4375F, 20.0313F, 3.0F, 1.0F, 5.0F, 0.0F, false);
        bone9.texOffs(145, 150).addBox(1.207F, -0.2696F, 10.0313F, 1.0F, 4.0F, 15.0F, 0.0F, false);
        bone9.texOffs(145, 150).addBox(-2.207F, -0.2696F, 10.0313F, 1.0F, 4.0F, 15.0F, 0.0F, true);
        bone9.texOffs(145, 150).addBox(-2.207F, -1.2696F, 11.0313F, 1.0F, 1.0F, 14.0F, 0.0F, true);
        bone9.texOffs(145, 150).addBox(-1.5F, -3.5625F, 24.0313F, 3.0F, 7.0F, 1.0F, 0.0F, false);
        bone9.texOffs(145, 150).addBox(1.207F, -1.2696F, 11.0313F, 1.0F, 1.0F, 14.0F, 0.0F, false);
        bone9.texOffs(145, 150).addBox(-2.207F, -2.2696F, 12.0313F, 1.0F, 1.0F, 13.0F, 0.0F, true);
        bone9.texOffs(145, 150).addBox(1.207F, -2.2696F, 12.0313F, 1.0F, 1.0F, 13.0F, 0.0F, false);
        bone9.texOffs(145, 150).addBox(-2.207F, -4.2696F, 19.0313F, 1.0F, 2.0F, 6.0F, 0.0F, true);
        bone9.texOffs(145, 150).addBox(1.207F, -4.2696F, 19.0313F, 1.0F, 2.0F, 6.0F, 0.0F, false);
        bone9.texOffs(145, 150).addBox(-1.5001F, -0.9767F, 10.0313F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        bone9.texOffs(145, 150).addBox(-1.5001F, -1.9767F, 11.0313F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        bone9.texOffs(145, 150).addBox(-1.5F, -2.2696F, 12.0313F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        bone9.texOffs(145, 150).addBox(-1.4999F, -2.9767F, 12.0313F, 3.0F, 1.0F, 2.0F, 0.0F, false);
        bone9.texOffs(145, 150).addBox(-1.5F, -4.5625F, 24.0313F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        bone9.texOffs(145, 150).addBox(-1.5F, -4.1094F, 24.3438F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        bone9.texOffs(145, 150).addBox(-1.5F, -1.4063F, 24.6094F, 3.0F, 2.0F, 1.0F, 0.0F, false);
        bone9.texOffs(145, 150).addBox(-2.0F, 0.8906F, 24.6094F, 4.0F, 2.0F, 1.0F, 0.0F, false);
        bone9.texOffs(145, 150).addBox(-2.0F, -1.1094F, 24.3438F, 4.0F, 2.0F, 1.0F, 0.0F, false);
        bone9.texOffs(145, 150).addBox(-1.5F, 0.5822F, 24.7515F, 3.0F, 2.0F, 1.0F, 0.0F, false);

        bone10 = new ModelRenderer(this);
        bone10.setPos(0.0F, 0.0F, 0.0F);
        bone9.addChild(bone10);
        setRotationAngle(bone10, 0.0F, 0.0F, 0.7854F);
        bone10.texOffs(145, 150).addBox(0.6629F, -1.4584F, -1.9688F, 1.0F, 1.0F, 12.0F, 0.0F, false);
        bone10.texOffs(145, 150).addBox(-1.4584F, 0.6629F, -1.9688F, 1.0F, 1.0F, 12.0F, 0.0F, false);
        bone10.texOffs(145, 150).addBox(3.1984F, 1.0771F, -1.9688F, 1.0F, 1.0F, 12.0F, 0.0F, false);
        bone10.texOffs(145, 150).addBox(1.0771F, 3.1984F, -1.9688F, 1.0F, 1.0F, 12.0F, 0.0F, false);
        bone10.texOffs(145, 150).addBox(3.1984F, 1.0771F, 10.0313F, 1.0F, 1.0F, 15.0F, 0.0F, false);
        bone10.texOffs(145, 150).addBox(1.0771F, 3.1984F, 10.0313F, 1.0F, 1.0F, 15.0F, 0.0F, false);
        bone10.texOffs(145, 150).addBox(0.3699F, -1.7512F, 10.0313F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone10.texOffs(145, 150).addBox(-1.7514F, 0.3701F, 10.0313F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone10.texOffs(145, 150).addBox(-0.3372F, -2.4583F, 11.0313F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone10.texOffs(145, 150).addBox(-2.4585F, -0.337F, 11.0313F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone10.texOffs(145, 150).addBox(-3.1654F, -1.0443F, 12.0313F, 1.0F, 1.0F, 3.0F, 0.0F, false);
        bone10.texOffs(145, 150).addBox(-1.0441F, -3.1656F, 12.0313F, 1.0F, 1.0F, 3.0F, 0.0F, false);

        bone11 = new ModelRenderer(this);
        bone11.setPos(1.707F, -4.2696F, 19.0313F);
        bone9.addChild(bone11);
        setRotationAngle(bone11, 0.2967F, 0.0F, 0.0F);
        bone11.texOffs(145, 150).addBox(-0.5F, 0.0F, -7.0F, 1.0F, 2.0F, 13.0F, 0.0F, false);
        bone11.texOffs(145, 150).addBox(-3.207F, 0.0F, 5.0313F, 3.0F, 2.0F, 1.0F, 0.0F, true);

        bone12 = new ModelRenderer(this);
        bone12.setPos(-1.707F, -4.2696F, 19.0313F);
        bone9.addChild(bone12);
        setRotationAngle(bone12, 0.2967F, 0.0F, 0.0F);
        bone12.texOffs(145, 150).addBox(-0.5F, 0.0F, -7.0F, 1.0F, 2.0F, 13.0F, 0.0F, true);

        bone13 = new ModelRenderer(this);
        bone13.setPos(-1.707F, -4.2696F, 19.0313F);
        bone9.addChild(bone13);
        setRotationAngle(bone13, 0.2967F, 0.0F, 0.0F);
        bone13.texOffs(145, 150).addBox(0.207F, -0.2801F, -6.9144F, 3.0F, 1.0F, 13.0F, 0.0F, true);

        bone17 = new ModelRenderer(this);
        bone17.setPos(0.0F, 3.9375F, 22.5313F);
        bone9.addChild(bone17);
        setRotationAngle(bone17, -0.3491F, 0.0F, 0.0F);
        bone17.texOffs(145, 150).addBox(-2.0F, -2.375F, -0.4375F, 4.0F, 2.0F, 3.0F, 0.0F, false);

        barrel = new ModelRenderer(this);
        barrel.setPos(0.0F, -0.7031F, 0.0F);
        win94.addChild(barrel);
        barrel.texOffs(11, 155).addBox(-0.5F, -14.9531F, -48.3125F, 1.0F, 1.0F, 15.0F, 0.0F, true);
        barrel.texOffs(11, 155).addBox(-1.2071F, -15.6602F, -48.3125F, 1.0F, 1.0F, 15.0F, 0.0F, true);
        barrel.texOffs(11, 155).addBox(-0.5F, -16.3673F, -48.3125F, 1.0F, 1.0F, 15.0F, 0.0F, true);
        barrel.texOffs(11, 155).addBox(0.2071F, -15.6602F, -48.3125F, 1.0F, 1.0F, 15.0F, 0.0F, true);

        bone18 = new ModelRenderer(this);
        bone18.setPos(0.0F, -14.4531F, -48.8125F);
        barrel.addChild(bone18);
        setRotationAngle(bone18, 0.0F, 0.0F, -0.7854F);
        bone18.texOffs(11, 155).addBox(-0.7071F, -1.0F, 0.5F, 1.0F, 1.0F, 15.0F, 0.0F, true);
        bone18.texOffs(11, 155).addBox(0.0F, -0.2929F, 0.5F, 1.0F, 1.0F, 15.0F, 0.0F, true);
        bone18.texOffs(11, 155).addBox(0.0F, -1.7071F, 0.5F, 1.0F, 1.0F, 15.0F, 0.0F, true);
        bone18.texOffs(11, 155).addBox(0.7071F, -1.0F, 0.5F, 1.0F, 1.0F, 15.0F, 0.0F, true);

        barrel4 = new ModelRenderer(this);
        barrel4.setPos(0.0F, 2.0F, 0.0F);
        win94.addChild(barrel4);
        barrel4.texOffs(11, 155).addBox(-0.5F, -14.9531F, -48.3125F, 1.0F, 1.0F, 15.0F, 0.0F, true);
        barrel4.texOffs(11, 155).addBox(-1.2071F, -15.6602F, -48.3125F, 1.0F, 1.0F, 15.0F, 0.0F, true);
        barrel4.texOffs(11, 155).addBox(-0.5F, -16.3673F, -48.3125F, 1.0F, 1.0F, 15.0F, 0.0F, true);
        barrel4.texOffs(11, 155).addBox(0.2071F, -15.6602F, -48.3125F, 1.0F, 1.0F, 15.0F, 0.0F, true);

        bone21 = new ModelRenderer(this);
        bone21.setPos(0.0F, -14.4531F, -48.8125F);
        barrel4.addChild(bone21);
        setRotationAngle(bone21, 0.0F, 0.0F, -0.7854F);
        bone21.texOffs(11, 155).addBox(-0.7071F, -1.0F, 0.5F, 1.0F, 1.0F, 15.0F, 0.0F, true);
        bone21.texOffs(11, 155).addBox(0.0F, -0.2929F, 0.5F, 1.0F, 1.0F, 15.0F, 0.0F, true);
        bone21.texOffs(11, 155).addBox(0.0F, -1.7071F, 0.5F, 1.0F, 1.0F, 15.0F, 0.0F, true);
        bone21.texOffs(11, 155).addBox(0.7071F, -1.0F, 0.5F, 1.0F, 1.0F, 15.0F, 0.0F, true);

        barrel5 = new ModelRenderer(this);
        barrel5.setPos(0.0F, 2.0F, -15.0F);
        win94.addChild(barrel5);
        barrel5.texOffs(11, 155).addBox(-0.5F, -14.9531F, -48.3125F, 1.0F, 1.0F, 15.0F, 0.0F, true);
        barrel5.texOffs(11, 155).addBox(-1.2071F, -15.6602F, -48.3125F, 1.0F, 1.0F, 15.0F, 0.0F, true);
        barrel5.texOffs(11, 155).addBox(-0.5F, -16.3673F, -48.3125F, 1.0F, 1.0F, 15.0F, 0.0F, true);
        barrel5.texOffs(11, 155).addBox(0.2071F, -15.6602F, -48.3125F, 1.0F, 1.0F, 15.0F, 0.0F, true);

        bone22 = new ModelRenderer(this);
        bone22.setPos(0.0F, -14.4531F, -48.8125F);
        barrel5.addChild(bone22);
        setRotationAngle(bone22, 0.0F, 0.0F, -0.7854F);
        bone22.texOffs(11, 155).addBox(-0.7071F, -1.0F, 0.5F, 1.0F, 1.0F, 15.0F, 0.0F, true);
        bone22.texOffs(11, 155).addBox(0.0F, -0.2929F, 0.5F, 1.0F, 1.0F, 15.0F, 0.0F, true);
        bone22.texOffs(11, 155).addBox(0.0F, -1.7071F, 0.5F, 1.0F, 1.0F, 15.0F, 0.0F, true);
        bone22.texOffs(11, 155).addBox(0.7071F, -1.0F, 0.5F, 1.0F, 1.0F, 15.0F, 0.0F, true);

        barrel2 = new ModelRenderer(this);
        barrel2.setPos(0.0F, -0.7031F, -15.0F);
        win94.addChild(barrel2);
        barrel2.texOffs(11, 155).addBox(-0.5F, -14.9531F, -48.3125F, 1.0F, 1.0F, 15.0F, 0.0F, true);
        barrel2.texOffs(11, 155).addBox(-1.2071F, -15.6602F, -48.3125F, 1.0F, 1.0F, 15.0F, 0.0F, true);
        barrel2.texOffs(11, 155).addBox(-0.5F, -16.3673F, -48.3125F, 1.0F, 1.0F, 15.0F, 0.0F, true);
        barrel2.texOffs(11, 155).addBox(0.2071F, -15.6602F, -48.3125F, 1.0F, 1.0F, 15.0F, 0.0F, true);
        barrel2.texOffs(11, 155).addBox(-1.0F, -14.6602F, -42.3125F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        barrel2.texOffs(24, 94).addBox(-1.0F, -17.8048F, -47.1875F, 2.0F, 2.0F, 2.0F, 0.0F, true);
        barrel2.texOffs(24, 94).addBox(-0.5F, -18.6017F, -47.25F, 1.0F, 3.0F, 1.0F, 0.0F, true);
        barrel2.texOffs(24, 94).addBox(-1.7563F, -19.4048F, -47.1875F, 1.0F, 2.0F, 2.0F, 0.0F, true);
        barrel2.texOffs(24, 94).addBox(0.7563F, -19.4048F, -47.1875F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        barrel2.texOffs(24, 94).addBox(-1.5F, -18.1017F, -47.1875F, 3.0F, 1.0F, 2.0F, 0.0F, true);

        bone19 = new ModelRenderer(this);
        bone19.setPos(0.0F, -14.4531F, -48.8125F);
        barrel2.addChild(bone19);
        setRotationAngle(bone19, 0.0F, 0.0F, -0.7854F);
        bone19.texOffs(11, 155).addBox(-0.7071F, -1.0F, 0.5F, 1.0F, 1.0F, 15.0F, 0.0F, true);
        bone19.texOffs(11, 155).addBox(0.0F, -0.2929F, 0.5F, 1.0F, 1.0F, 15.0F, 0.0F, true);
        bone19.texOffs(11, 155).addBox(0.0F, -1.7071F, 0.5F, 1.0F, 1.0F, 15.0F, 0.0F, true);
        bone19.texOffs(11, 155).addBox(0.7071F, -1.0F, 0.5F, 1.0F, 1.0F, 15.0F, 0.0F, true);

        bone20 = new ModelRenderer(this);
        bone20.setPos(0.0F, 0.0F, 0.0F);
        win94.addChild(bone20);
        setRotationAngle(bone20, 0.0F, 0.0F, -0.7854F);
        bone20.texOffs(24, 94).addBox(12.9599F, -14.9599F, 7.0313F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone20.texOffs(24, 94).addBox(13.9599F, -13.9599F, 7.0313F, 1.0F, 1.0F, 1.0F, 0.0F, true);

        lever = new ModelRenderer(this);
        lever.setPos(0.0F, 12.3549F, 8.215F);
        setRotationAngle(lever, -0.5236F, 0.0F, 0.0F);
        lever.texOffs(19, 160).addBox(-1.0F, -1.8605F, 7.5025F, 2.0F, 1.0F, 11.0F, 0.0F, false);
        lever.texOffs(19, 160).addBox(-1.0F, 0.8715F, 20.2346F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        lever.texOffs(19, 160).addBox(-1.0F, 3.6036F, 7.5025F, 2.0F, 1.0F, 11.0F, 0.0F, false);
        lever.texOffs(19, 160).addBox(-1.0F, -0.17F, 5.8773F, 2.0F, 4.0F, 1.0F, 0.0F, false);
        lever.texOffs(19, 160).addBox(-1.0F, 2.83F, 4.8773F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        lever.texOffs(19, 160).addBox(-1.0F, -1.7265F, 6.2705F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        lever.texOffs(19, 160).addBox(-1.0F, 3.696F, 1.6452F, 2.0F, 1.0F, 2.0F, 0.0F, false);
        lever.texOffs(19, 160).addBox(-1.0F, -0.7267F, -1.33F, 2.0F, 2.0F, 1.0F, 0.0F, false);

        bone15 = new ModelRenderer(this);
        bone15.setPos(0.0F, 8.0145F, 2.5025F);
        lever.addChild(bone15);
        setRotationAngle(bone15, -0.5236F, 0.0F, 0.0F);
        bone15.texOffs(19, 160).addBox(-1.0F, -16.552F, 8.9189F, 2.0F, 1.0F, 2.0F, 0.0F, false);
        bone15.texOffs(19, 160).addBox(-1.0F, -14.686F, 12.151F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        bone15.texOffs(19, 160).addBox(-1.0F, -6.4539F, -0.3753F, 2.0F, 1.0F, 3.0F, 0.0F, false);
        bone15.texOffs(19, 160).addBox(-1.0F, -11.186F, -1.1074F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        bone15.texOffs(19, 160).addBox(-1.0F, -7.4539F, -0.3753F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        bone15.texOffs(19, 160).addBox(-1.0F, -10.7754F, -1.1696F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        bone15.texOffs(19, 160).addBox(-1.0F, -3.4453F, -4.4017F, 2.0F, 1.0F, 2.0F, 0.0F, false);

        bone16 = new ModelRenderer(this);
        bone16.setPos(0.0F, 8.0145F, 2.5025F);
        lever.addChild(bone16);
        setRotationAngle(bone16, -1.0472F, 0.0F, 0.0F);
        bone16.texOffs(19, 160).addBox(-1.0F, -19.7939F, 1.18F, 2.0F, 1.0F, 2.0F, 0.0F, false);
        bone16.texOffs(19, 160).addBox(-1.0F, -17.5619F, 4.0461F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        bone16.texOffs(19, 160).addBox(-1.0F, -9.1336F, -6.552F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        bone16.texOffs(19, 160).addBox(-1.0F, -9.1428F, -5.9395F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        bone16.texOffs(19, 160).addBox(-1.0F, -6.4515F, -2.5989F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        bone16.texOffs(19, 160).addBox(-1.0F, -5.6489F, -3.3026F, 2.0F, 3.0F, 1.0F, 0.0F, false);
        bone16.texOffs(19, 160).addBox(-1.0F, -0.9168F, -9.0346F, 2.0F, 1.0F, 4.0F, 0.0F, false);

        bolt = new ModelRenderer(this);
        bolt.setPos(0.0F, 24.0F, 0.0F);
        bolt.texOffs(15, 15).addBox(-2.9375F, -15.5F, 1.0F, 1.0F, 2.0F, 7.0F, 0.0F, false);
        bolt.texOffs(38, 42).addBox(-2.8906F, -16.0F, 0.5F, 1.0F, 3.0F, 8.0F, 0.0F, false);

        bone3 = new ModelRenderer(this);
        bone3.setPos(-3.4375F, -14.5F, 2.5F);
        bolt.addChild(bone3);
        setRotationAngle(bone3, 0.0F, 0.3491F, 0.0F);
        bone3.texOffs(15, 15).addBox(0.4258F, -0.5F, -0.8902F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        bullet = new ModelRenderer(this);
        bullet.setPos(4.5F, 24.5F, 6.0F);
        bullet.texOffs(0, 497).addBox(-7.0F, -16.0F, 0.0F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        bullet.texOffs(0, 497).addBox(-7.0F, -16.0F, -3.3F, 2.0F, 2.0F, 3.0F, 0.0F, false);
        bullet.texOffs(0, 497).addBox(-6.1F, -15.9F, -4.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        bullet.texOffs(0, 497).addBox(-6.1F, -15.1F, -4.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        bullet.texOffs(0, 497).addBox(-6.9F, -15.9F, -4.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        bullet.texOffs(0, 497).addBox(-6.9F, -15.1F, -4.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);

        setSpecialRenderer(ModAnimations.CHARGING_HANDLE, lever);
        setSpecialRenderer(ModAnimations.BOLT, bolt);
        setSpecialRenderer(ModAnimations.BULLET, bullet);
    }
}
