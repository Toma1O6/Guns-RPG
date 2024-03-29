package dev.toma.gunsrpg.client.model.weapon;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.client.animation.ModAnimations;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.item.ItemStack;

public class S1897Model extends AbstractWeaponModel {

    private final ModelRenderer charging_handle;
    private final ModelRenderer bone7;
    private final ModelRenderer bone6;
    private final ModelRenderer bone;
    private final ModelRenderer gun;
    private final ModelRenderer bone2;
    private final ModelRenderer bone13;
    private final ModelRenderer bone8;
    private final ModelRenderer bone10;
    private final ModelRenderer bone9;
    private final ModelRenderer bone11;
    private final ModelRenderer bone12;
    private final ModelRenderer bone14;
    private final ModelRenderer bone15;
    private final ModelRenderer bone16;
    private final ModelRenderer bone18;
    private final ModelRenderer bone20;
    private final ModelRenderer bone21;
    private final ModelRenderer bone23;
    private final ModelRenderer bone24;
    private final ModelRenderer bone17;
    private final ModelRenderer bone19;
    private final ModelRenderer bone4;
    private final ModelRenderer bone5;
    private final ModelRenderer bone3;
    private final ModelRenderer bullet;

    public S1897Model() {
        texWidth = 512;
        texHeight = 512;

        charging_handle = new ModelRenderer(this);
        charging_handle.setPos(1.5F, 24.0F, 0.0F);
        charging_handle.texOffs(145, 152).addBox(0.3927F, -4.1927F, -40.4F, 1.0F, 3.0F, 10.0F, 0.0F, true);
        charging_handle.texOffs(145, 152).addBox(0.3927F, -4.1927F, -30.4F, 1.0F, 3.0F, 10.0F, 0.0F, true);
        charging_handle.texOffs(145, 152).addBox(-4.3927F, -4.1927F, -30.4F, 1.0F, 3.0F, 10.0F, 0.0F, false);
        charging_handle.texOffs(145, 152).addBox(-4.3927F, -4.1927F, -40.4F, 1.0F, 3.0F, 10.0F, 0.0F, false);
        charging_handle.texOffs(145, 152).addBox(-3.0F, -0.8F, -30.4F, 3.0F, 1.0F, 10.0F, 0.0F, true);
        charging_handle.texOffs(145, 152).addBox(-3.0F, -0.8F, -40.4F, 3.0F, 1.0F, 10.0F, 0.0F, true);

        bone7 = new ModelRenderer(this);
        bone7.setPos(-3.0F, 0.0F, 0.0F);
        charging_handle.addChild(bone7);
        setRotationAngle(bone7, 0.0F, 0.0F, 0.6109F);
        bone7.texOffs(145, 152).addBox(-0.8853F, -0.8362F, -40.4F, 1.0F, 1.0F, 10.0F, 0.0F, false);
        bone7.texOffs(145, 152).addBox(-0.8853F, -0.8362F, -30.4F, 1.0F, 1.0F, 10.0F, 0.0F, false);
        bone7.texOffs(145, 152).addBox(1.9142F, -3.4966F, -40.4F, 1.0F, 1.0F, 10.0F, 0.0F, false);
        bone7.texOffs(145, 152).addBox(1.9142F, -3.4966F, -30.4F, 1.0F, 1.0F, 10.0F, 0.0F, false);

        bone6 = new ModelRenderer(this);
        bone6.setPos(0.0F, 0.0F, 0.0F);
        charging_handle.addChild(bone6);
        setRotationAngle(bone6, 0.0F, 0.0F, -0.6109F);
        bone6.texOffs(145, 152).addBox(-0.1147F, -0.8362F, -40.4F, 1.0F, 1.0F, 10.0F, 0.0F, true);
        bone6.texOffs(145, 152).addBox(-0.1147F, -0.8362F, -30.4F, 1.0F, 1.0F, 10.0F, 0.0F, true);
        bone6.texOffs(145, 152).addBox(-2.9142F, -3.4966F, -40.4F, 1.0F, 1.0F, 10.0F, 0.0F, true);
        bone6.texOffs(145, 152).addBox(-2.9142F, -3.4966F, -30.4F, 1.0F, 1.0F, 10.0F, 0.0F, true);

        bone = new ModelRenderer(this);
        bone.setPos(-1.5F, 0.0F, 0.0F);
        charging_handle.addChild(bone);
        bone.texOffs(4, 7).addBox(-2.8071F, -8.7071F, -1.0F, 1.0F, 4.0F, 12.0F, 0.0F, true);
        bone.texOffs(4, 7).addBox(-3.0071F, -7.7071F, -0.7F, 1.0F, 2.0F, 2.0F, 0.0F, true);

        gun = new ModelRenderer(this);
        gun.setPos(0.0F, 24.0F, 0.0F);
        gun.texOffs(0, 81).addBox(-2.5F, -1.0F, 1.0F, 1.0F, 1.0F, 9.0F, 0.0F, false);
        gun.texOffs(0, 81).addBox(1.5F, -1.0F, 1.0F, 1.0F, 1.0F, 9.0F, 0.0F, true);
        gun.texOffs(0, 81).addBox(-2.5F, -1.0F, 10.0F, 5.0F, 1.0F, 3.0F, 0.0F, true);
        gun.texOffs(0, 81).addBox(-2.5F, -10.4142F, -11.0F, 5.0F, 1.0F, 17.0F, 0.0F, true);
        gun.texOffs(50, 16).addBox(-1.5F, -11.0142F, -9.984F, 3.0F, 1.0F, 1.0F, 0.0F, true);
        gun.texOffs(54, 17).addBox(-1.5F, -11.0142F, 19.016F, 3.0F, 1.0F, 1.0F, 0.0F, true);
        gun.texOffs(20, 154).addBox(-1.4329F, -12.233F, 19.5F, 1.0F, 2.0F, 1.0F, 0.0F, true);
        gun.texOffs(20, 154).addBox(0.4329F, -12.233F, 19.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        gun.texOffs(20, 154).addBox(-0.5F, -11.653F, 19.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        gun.texOffs(50, 16).addBox(-1.5F, -11.0142F, -9.016F, 3.0F, 1.0F, 1.0F, 0.0F, true);
        gun.texOffs(46, 16).addBox(-1.5F, -11.0142F, 19.984F, 3.0F, 1.0F, 1.0F, 0.0F, true);
        gun.texOffs(20, 154).addBox(-0.5F, -11.8142F, -9.8F, 1.0F, 2.0F, 2.0F, 0.0F, true);
        gun.texOffs(20, 154).addBox(-0.5F, -11.615F, -8.6703F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        gun.texOffs(20, 154).addBox(-0.5F, -12.5213F, -9.0929F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        gun.texOffs(0, 81).addBox(-2.5F, -10.4142F, 6.0F, 5.0F, 1.0F, 17.0F, 0.0F, true);
        gun.texOffs(0, 81).addBox(-2.5F, -10.3142F, 22.2F, 5.0F, 1.0F, 1.0F, 0.0F, true);
        gun.texOffs(0, 81).addBox(-2.5F, -3.0F, 16.0F, 5.0F, 1.0F, 7.0F, 0.0F, true);
        gun.texOffs(0, 81).addBox(2.2071F, -9.7071F, 6.0F, 1.0F, 9.0F, 7.0F, 0.0F, true);
        gun.texOffs(0, 81).addBox(2.2071F, -9.7071F, 13.0F, 1.0F, 7.0F, 10.0F, 0.0F, true);
        gun.texOffs(0, 81).addBox(2.2071F, -9.7071F, -11.0F, 1.0F, 9.0F, 17.0F, 0.0F, true);
        gun.texOffs(0, 81).addBox(-3.2071F, -4.7071F, -1.0F, 1.0F, 4.0F, 14.0F, 0.0F, true);
        gun.texOffs(0, 81).addBox(-3.2071F, -9.7071F, 11.0F, 1.0F, 7.0F, 12.0F, 0.0F, true);
        gun.texOffs(0, 81).addBox(-3.2071F, -9.7071F, -1.0F, 1.0F, 1.0F, 12.0F, 0.0F, true);
        gun.texOffs(0, 81).addBox(-3.2071F, -9.7071F, -11.0F, 1.0F, 9.0F, 10.0F, 0.0F, true);
        gun.texOffs(0, 81).addBox(-2.5F, -10.0F, -11.0F, 5.0F, 10.0F, 12.0F, 0.0F, true);
        gun.texOffs(0, 146).addBox(-1.0F, -6.5F, -33.0F, 2.0F, 1.0F, 22.0F, 0.0F, true);
        gun.texOffs(0, 142).addBox(-1.0F, -6.5F, -53.0F, 2.0F, 1.0F, 20.0F, 0.0F, true);
        gun.texOffs(0, 81).addBox(-1.5F, -9.5F, -14.6F, 3.0F, 3.0F, 1.0F, 0.0F, true);
        gun.texOffs(0, 146).addBox(-1.0F, -1.3F, -30.0F, 2.0F, 1.0F, 19.0F, 0.0F, true);
        gun.texOffs(0, 146).addBox(-1.0F, -1.3F, -41.0F, 2.0F, 1.0F, 11.0F, 0.0F, true);
        gun.texOffs(0, 146).addBox(-1.0F, -2.3F, -42.6F, 2.0F, 2.0F, 1.0F, 0.0F, true);
        gun.texOffs(0, 146).addBox(-1.0F, -10.2321F, -33.0F, 2.0F, 1.0F, 22.0F, 0.0F, true);
        gun.texOffs(0, 142).addBox(-1.0F, -10.2321F, -53.0F, 2.0F, 1.0F, 20.0F, 0.0F, true);
        gun.texOffs(0, 146).addBox(-1.0F, -5.0321F, -30.0F, 2.0F, 1.0F, 19.0F, 0.0F, true);
        gun.texOffs(0, 146).addBox(-1.0F, -5.0321F, -41.0F, 2.0F, 1.0F, 11.0F, 0.0F, true);
        gun.texOffs(0, 146).addBox(-1.0F, -5.0321F, -42.6F, 2.0F, 3.0F, 1.0F, 0.0F, true);
        gun.texOffs(0, 146).addBox(-1.5F, -6.2321F, -20.3F, 3.0F, 2.0F, 2.0F, 0.0F, true);
        gun.texOffs(0, 146).addBox(-1.5F, -6.2321F, -35.3F, 3.0F, 2.0F, 2.0F, 0.0F, true);
        gun.texOffs(0, 146).addBox(1.366F, -8.866F, -33.0F, 1.0F, 2.0F, 22.0F, 0.0F, true);
        gun.texOffs(0, 142).addBox(1.366F, -8.866F, -53.0F, 1.0F, 2.0F, 20.0F, 0.0F, true);
        gun.texOffs(0, 146).addBox(1.366F, -3.666F, -30.0F, 1.0F, 2.0F, 19.0F, 0.0F, true);
        gun.texOffs(0, 146).addBox(1.366F, -3.666F, -41.0F, 1.0F, 2.0F, 11.0F, 0.0F, true);
        gun.texOffs(0, 146).addBox(0.366F, -3.666F, -42.6F, 2.0F, 2.0F, 1.0F, 0.0F, true);
        gun.texOffs(0, 146).addBox(-1.5F, -4.666F, -41.8F, 3.0F, 4.0F, 1.0F, 0.0F, true);
        gun.texOffs(0, 133).addBox(-2.366F, -8.866F, -33.0F, 1.0F, 2.0F, 22.0F, 0.0F, false);
        gun.texOffs(0, 142).addBox(-2.366F, -8.866F, -53.0F, 1.0F, 2.0F, 20.0F, 0.0F, false);
        gun.texOffs(0, 146).addBox(-2.366F, -3.666F, -30.0F, 1.0F, 2.0F, 19.0F, 0.0F, false);
        gun.texOffs(0, 146).addBox(-2.366F, -3.666F, -41.0F, 1.0F, 2.0F, 11.0F, 0.0F, false);
        gun.texOffs(0, 146).addBox(-2.366F, -3.666F, -42.6F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        gun.texOffs(0, 146).addBox(-1.0F, -3.666F, -42.9F, 2.0F, 2.0F, 1.0F, 0.0F, false);

        bone2 = new ModelRenderer(this);
        bone2.setPos(-0.5F, 0.2F, 0.1F);
        gun.addChild(bone2);
        setRotationAngle(bone2, -0.0349F, 0.0F, 0.0F);
        bone2.texOffs(0, 81).addBox(-1.0F, -1.7698F, -0.0012F, 3.0F, 1.0F, 10.0F, 0.0F, false);

        bone13 = new ModelRenderer(this);
        bone13.setPos(0.5F, 6.7F, 0.0F);
        gun.addChild(bone13);
        setRotationAngle(bone13, 0.5236F, 0.0F, 0.0F);
        bone13.texOffs(0, 81).addBox(-3.0F, -0.3024F, 14.6083F, 5.0F, 1.0F, 6.0F, 0.0F, true);
        bone13.texOffs(0, 81).addBox(-3.5F, -3.9086F, 14.9583F, 6.0F, 4.0F, 6.0F, 0.0F, true);

        bone8 = new ModelRenderer(this);
        bone8.setPos(0.3F, -11.5142F, -9.0F);
        gun.addChild(bone8);
        setRotationAngle(bone8, 0.0F, 0.0F, 0.3491F);
        bone8.texOffs(48, 13).addBox(0.7744F, -0.5999F, -1.0F, 1.0F, 2.0F, 2.0F, 0.0F, true);
        bone8.texOffs(48, 13).addBox(0.7744F, -0.5999F, 28.0F, 1.0F, 2.0F, 2.0F, 0.0F, true);

        bone10 = new ModelRenderer(this);
        bone10.setPos(-0.3F, -11.5142F, -9.0F);
        gun.addChild(bone10);
        setRotationAngle(bone10, 0.0F, 0.0F, -0.3491F);
        bone10.texOffs(48, 13).addBox(-1.7744F, -0.5999F, -1.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        bone10.texOffs(48, 13).addBox(-1.7744F, -0.5999F, 28.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);

        bone9 = new ModelRenderer(this);
        bone9.setPos(-0.3F, -11.5142F, -9.0F);
        gun.addChild(bone9);
        setRotationAngle(bone9, 0.0F, 0.0F, -0.3491F);


        bone11 = new ModelRenderer(this);
        bone11.setPos(0.0F, -12.3142F, -8.3F);
        gun.addChild(bone11);
        setRotationAngle(bone11, 0.7854F, 0.0F, 0.0F);
        bone11.texOffs(20, 154).addBox(-0.5F, -0.7071F, -1.4142F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone11.texOffs(20, 154).addBox(-0.5F, 0.6468F, -1.7562F, 1.0F, 1.0F, 1.0F, 0.0F, true);

        bone12 = new ModelRenderer(this);
        bone12.setPos(0.0F, -12.3142F, -8.3F);
        gun.addChild(bone12);
        setRotationAngle(bone12, 0.4363F, 0.0F, 0.0F);
        bone12.texOffs(20, 154).addBox(-0.5F, -0.1002F, -0.7248F, 1.0F, 1.0F, 1.0F, 0.0F, true);

        bone14 = new ModelRenderer(this);
        bone14.setPos(0.5F, 0.0F, 0.0F);
        gun.addChild(bone14);
        setRotationAngle(bone14, -0.2618F, 0.0F, 0.0F);
        bone14.texOffs(0, 81).addBox(-3.7071F, -15.3292F, 19.7039F, 1.0F, 7.0F, 7.0F, 0.0F, true);
        bone14.texOffs(0, 81).addBox(-3.0F, -16.0363F, 19.7039F, 5.0F, 1.0F, 7.0F, 0.0F, true);
        bone14.texOffs(97, 149).addBox(-2.0F, -16.7383F, 21.6193F, 3.0F, 1.0F, 5.0F, 0.0F, true);
        bone14.texOffs(97, 149).addBox(-2.0F, -17.0194F, 20.1775F, 3.0F, 2.0F, 2.0F, 0.0F, true);
        bone14.texOffs(97, 149).addBox(0.5F, -16.8194F, 20.6775F, 1.0F, 1.0F, 2.0F, 0.0F, true);
        bone14.texOffs(97, 149).addBox(-2.5F, -16.8194F, 20.3775F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        bone14.texOffs(97, 149).addBox(-1.5F, -17.0194F, 22.1775F, 2.0F, 1.0F, 2.0F, 0.0F, true);
        bone14.texOffs(97, 149).addBox(-1.0F, -17.0194F, 24.1775F, 1.0F, 1.0F, 2.0F, 0.0F, true);
        bone14.texOffs(0, 81).addBox(-3.0F, -8.6221F, 19.7039F, 5.0F, 1.0F, 7.0F, 0.0F, true);
        bone14.texOffs(0, 81).addBox(1.7071F, -15.3292F, 19.7039F, 1.0F, 7.0F, 7.0F, 0.0F, true);

        bone15 = new ModelRenderer(this);
        bone15.setPos(0.0F, 0.0F, 0.0F);
        bone14.addChild(bone15);
        setRotationAngle(bone15, 0.0F, 0.0F, -0.7854F);
        bone15.texOffs(0, 81).addBox(8.218F, -13.4607F, 19.7039F, 1.0F, 1.0F, 7.0F, 0.0F, true);
        bone15.texOffs(0, 81).addBox(11.7536F, -9.9252F, 19.7039F, 1.0F, 1.0F, 7.0F, 0.0F, true);

        bone16 = new ModelRenderer(this);
        bone16.setPos(0.0F, 7.0F, 0.0F);
        bone14.addChild(bone16);
        setRotationAngle(bone16, 0.0F, 0.0F, -0.7854F);
        bone16.texOffs(0, 81).addBox(8.218F, -13.4607F, 19.7039F, 1.0F, 1.0F, 7.0F, 0.0F, true);
        bone16.texOffs(0, 81).addBox(11.7536F, -9.9252F, 19.7039F, 1.0F, 1.0F, 7.0F, 0.0F, true);

        bone18 = new ModelRenderer(this);
        bone18.setPos(0.5F, 0.0F, 16.0F);
        gun.addChild(bone18);
        setRotationAngle(bone18, -0.5236F, 0.0F, 0.0F);
        bone18.texOffs(0, 81).addBox(-3.7071F, -13.7183F, 7.9701F, 1.0F, 7.0F, 8.0F, 0.0F, true);
        bone18.texOffs(0, 81).addBox(-3.0F, -14.4254F, 7.9701F, 5.0F, 1.0F, 8.0F, 0.0F, true);
        bone18.texOffs(0, 81).addBox(-3.0F, -7.0112F, 7.9701F, 5.0F, 1.0F, 8.0F, 0.0F, true);
        bone18.texOffs(0, 81).addBox(1.7071F, -13.7183F, 7.9701F, 1.0F, 7.0F, 8.0F, 0.0F, true);

        bone20 = new ModelRenderer(this);
        bone20.setPos(0.0F, 0.0F, 0.0F);
        bone18.addChild(bone20);
        setRotationAngle(bone20, 0.0F, 0.0F, -0.7854F);
        bone20.texOffs(0, 81).addBox(7.079F, -12.3216F, 7.9701F, 1.0F, 1.0F, 8.0F, 0.0F, true);
        bone20.texOffs(0, 81).addBox(10.6145F, -8.7861F, 7.9701F, 1.0F, 1.0F, 8.0F, 0.0F, true);

        bone21 = new ModelRenderer(this);
        bone21.setPos(0.0F, 7.0F, 0.0F);
        bone18.addChild(bone21);
        setRotationAngle(bone21, 0.0F, 0.0F, -0.7854F);
        bone21.texOffs(0, 81).addBox(7.079F, -12.3216F, 7.9701F, 1.0F, 1.0F, 8.0F, 0.0F, true);
        bone21.texOffs(0, 81).addBox(10.6145F, -8.7861F, 7.9701F, 1.0F, 1.0F, 8.0F, 0.0F, true);

        bone23 = new ModelRenderer(this);
        bone23.setPos(0.5F, 7.5F, 10.2F);
        gun.addChild(bone23);
        bone23.texOffs(0, 81).addBox(-3.7071F, -12.8821F, 22.6764F, 1.0F, 5.0F, 20.0F, 0.0F, true);
        bone23.texOffs(0, 81).addBox(-3.7071F, -7.8821F, 39.6764F, 1.0F, 4.0F, 3.0F, 0.0F, true);
        bone23.texOffs(0, 81).addBox(-3.0F, -13.5892F, 22.6764F, 5.0F, 1.0F, 20.0F, 0.0F, true);
        bone23.texOffs(0, 81).addBox(-3.0F, -12.6892F, 40.6964F, 5.0F, 9.0F, 2.0F, 0.0F, true);
        bone23.texOffs(0, 81).addBox(1.7071F, -12.8821F, 22.6764F, 1.0F, 5.0F, 20.0F, 0.0F, true);
        bone23.texOffs(0, 81).addBox(1.7071F, -7.8821F, 39.6764F, 1.0F, 4.0F, 3.0F, 0.0F, true);

        bone24 = new ModelRenderer(this);
        bone24.setPos(0.0F, 0.0F, 0.0F);
        bone23.addChild(bone24);
        setRotationAngle(bone24, 0.0F, 0.0F, -0.7854F);
        bone24.texOffs(0, 81).addBox(6.4877F, -11.7303F, 22.6764F, 1.0F, 1.0F, 20.0F, 0.0F, true);
        bone24.texOffs(0, 81).addBox(10.0232F, -8.1948F, 22.6764F, 1.0F, 1.0F, 20.0F, 0.0F, true);

        bone17 = new ModelRenderer(this);
        bone17.setPos(0.5F, -1.3F, -11.9F);
        gun.addChild(bone17);
        setRotationAngle(bone17, -0.0873F, 0.0F, 0.0F);
        bone17.texOffs(0, 81).addBox(-3.7071F, -5.0396F, 44.8886F, 1.0F, 4.0F, 19.0F, 0.0F, true);
        bone17.texOffs(0, 81).addBox(-3.0F, -1.3324F, 44.8886F, 5.0F, 1.0F, 19.0F, 0.0F, true);
        bone17.texOffs(0, 81).addBox(1.7071F, -5.0396F, 44.8886F, 1.0F, 4.0F, 19.0F, 0.0F, true);

        bone19 = new ModelRenderer(this);
        bone19.setPos(0.0F, 7.0F, 0.0F);
        bone17.addChild(bone19);
        setRotationAngle(bone19, 0.0F, 0.0F, -0.7854F);
        bone19.texOffs(0, 81).addBox(3.0635F, -8.3061F, 44.8886F, 1.0F, 1.0F, 19.0F, 0.0F, true);
        bone19.texOffs(0, 81).addBox(6.599F, -4.7706F, 44.8886F, 1.0F, 1.0F, 19.0F, 0.0F, true);

        bone4 = new ModelRenderer(this);
        bone4.setPos(1.0F, 0.0F, 0.0F);
        gun.addChild(bone4);
        setRotationAngle(bone4, 0.0F, 0.0F, 0.5236F);
        bone4.texOffs(0, 142).addBox(-5.4821F, -4.7631F, -33.0F, 1.0F, 1.0F, 22.0F, 0.0F, true);
        bone4.texOffs(0, 142).addBox(-5.4821F, -4.7631F, -53.0F, 1.0F, 1.0F, 20.0F, 0.0F, true);
        bone4.texOffs(0, 142).addBox(-2.8821F, -0.2598F, -30.0F, 1.0F, 1.0F, 19.0F, 0.0F, true);
        bone4.texOffs(0, 142).addBox(-2.8821F, -0.2598F, -41.0F, 1.0F, 1.0F, 11.0F, 0.0F, true);
        bone4.texOffs(0, 142).addBox(-2.8821F, -0.2598F, -42.6F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone4.texOffs(0, 142).addBox(-3.25F, -6.6292F, -33.0F, 1.0F, 1.0F, 22.0F, 0.0F, true);
        bone4.texOffs(0, 142).addBox(-3.25F, -6.6292F, -53.0F, 1.0F, 1.0F, 20.0F, 0.0F, true);
        bone4.texOffs(0, 142).addBox(-0.65F, -2.1258F, -30.0F, 1.0F, 1.0F, 19.0F, 0.0F, true);
        bone4.texOffs(0, 142).addBox(-0.65F, -2.1258F, -41.0F, 1.0F, 1.0F, 11.0F, 0.0F, true);
        bone4.texOffs(0, 142).addBox(-1.65F, -2.1258F, -42.6F, 2.0F, 1.0F, 1.0F, 0.0F, true);
        bone4.texOffs(0, 142).addBox(-7.3481F, -6.9952F, -33.0F, 1.0F, 1.0F, 22.0F, 0.0F, true);
        bone4.texOffs(0, 142).addBox(-7.3481F, -6.9952F, -53.0F, 1.0F, 1.0F, 20.0F, 0.0F, true);
        bone4.texOffs(0, 142).addBox(-4.7481F, -2.4919F, -30.0F, 1.0F, 1.0F, 19.0F, 0.0F, true);
        bone4.texOffs(0, 142).addBox(-4.7481F, -2.4919F, -41.0F, 1.0F, 1.0F, 11.0F, 0.0F, true);
        bone4.texOffs(0, 142).addBox(-4.7481F, -2.4919F, -42.6F, 2.0F, 1.0F, 1.0F, 0.0F, true);
        bone4.texOffs(0, 142).addBox(-5.116F, -8.8612F, -33.0F, 1.0F, 1.0F, 22.0F, 0.0F, true);
        bone4.texOffs(0, 142).addBox(-5.116F, -8.8612F, -53.0F, 1.0F, 1.0F, 20.0F, 0.0F, true);
        bone4.texOffs(0, 142).addBox(-2.516F, -4.3579F, -30.0F, 1.0F, 1.0F, 19.0F, 0.0F, true);
        bone4.texOffs(0, 142).addBox(-2.516F, -4.3579F, -41.0F, 1.0F, 1.0F, 11.0F, 0.0F, true);
        bone4.texOffs(0, 142).addBox(-2.516F, -4.3579F, -42.6F, 1.0F, 1.0F, 1.0F, 0.0F, true);

        bone5 = new ModelRenderer(this);
        bone5.setPos(-1.0F, 0.0F, 0.0F);
        gun.addChild(bone5);
        setRotationAngle(bone5, 0.0F, 0.0F, -0.5236F);
        bone5.texOffs(0, 142).addBox(4.4821F, -4.7631F, -33.0F, 1.0F, 1.0F, 22.0F, 0.0F, false);
        bone5.texOffs(0, 142).addBox(4.4821F, -4.7631F, -53.0F, 1.0F, 1.0F, 20.0F, 0.0F, false);
        bone5.texOffs(0, 142).addBox(1.8821F, -0.2598F, -30.0F, 1.0F, 1.0F, 19.0F, 0.0F, false);
        bone5.texOffs(0, 142).addBox(1.8821F, -0.2598F, -41.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone5.texOffs(0, 142).addBox(1.8821F, -0.2598F, -42.6F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone5.texOffs(0, 142).addBox(2.25F, -6.6292F, -33.0F, 1.0F, 1.0F, 22.0F, 0.0F, false);
        bone5.texOffs(0, 142).addBox(2.25F, -6.6292F, -53.0F, 1.0F, 1.0F, 20.0F, 0.0F, false);
        bone5.texOffs(0, 142).addBox(-0.35F, -2.1258F, -30.0F, 1.0F, 1.0F, 19.0F, 0.0F, false);
        bone5.texOffs(0, 142).addBox(-0.35F, -2.1258F, -41.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone5.texOffs(0, 142).addBox(-0.35F, -2.1258F, -42.6F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        bone5.texOffs(0, 142).addBox(6.3481F, -6.9952F, -33.0F, 1.0F, 1.0F, 22.0F, 0.0F, false);
        bone5.texOffs(0, 142).addBox(6.3481F, -6.9952F, -53.0F, 1.0F, 1.0F, 20.0F, 0.0F, false);
        bone5.texOffs(0, 142).addBox(3.7481F, -2.4919F, -30.0F, 1.0F, 1.0F, 19.0F, 0.0F, false);
        bone5.texOffs(0, 142).addBox(3.7481F, -2.4919F, -41.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone5.texOffs(0, 142).addBox(2.7481F, -2.4919F, -42.6F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        bone5.texOffs(0, 142).addBox(4.116F, -8.8612F, -33.0F, 1.0F, 1.0F, 22.0F, 0.0F, false);
        bone5.texOffs(0, 142).addBox(4.116F, -8.8612F, -53.0F, 1.0F, 1.0F, 20.0F, 0.0F, false);
        bone5.texOffs(0, 142).addBox(1.516F, -4.3579F, -30.0F, 1.0F, 1.0F, 19.0F, 0.0F, false);
        bone5.texOffs(0, 142).addBox(1.516F, -4.3579F, -41.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone5.texOffs(0, 142).addBox(1.516F, -4.3579F, -42.6F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        bone3 = new ModelRenderer(this);
        bone3.setPos(0.5F, 0.0F, 0.0F);
        gun.addChild(bone3);
        setRotationAngle(bone3, 0.0F, 0.0F, 0.7854F);
        bone3.texOffs(0, 81).addBox(-3.1213F, 1.1213F, -11.0F, 1.0F, 1.0F, 17.0F, 0.0F, true);
        bone3.texOffs(0, 81).addBox(0.4142F, -2.4142F, -11.0F, 1.0F, 1.0F, 17.0F, 0.0F, true);
        bone3.texOffs(0, 81).addBox(-5.9497F, -8.7782F, -11.0F, 1.0F, 1.0F, 17.0F, 0.0F, true);
        bone3.texOffs(0, 81).addBox(-5.9497F, -8.7782F, 6.0F, 1.0F, 1.0F, 17.0F, 0.0F, true);
        bone3.texOffs(0, 81).addBox(-1.0F, -3.8284F, 13.0F, 1.0F, 1.0F, 10.0F, 0.0F, true);
        bone3.texOffs(0, 81).addBox(-9.4853F, -5.2426F, -11.0F, 1.0F, 1.0F, 17.0F, 0.0F, true);
        bone3.texOffs(0, 81).addBox(-9.4853F, -5.2426F, 6.0F, 1.0F, 1.0F, 17.0F, 0.0F, true);
        bone3.texOffs(0, 81).addBox(-4.5355F, -0.2929F, 13.0F, 1.0F, 1.0F, 10.0F, 0.0F, true);
        bone3.texOffs(0, 81).addBox(-3.1213F, 1.1213F, 6.0F, 1.0F, 1.0F, 7.0F, 0.0F, true);
        bone3.texOffs(0, 81).addBox(0.4142F, -2.4142F, 6.0F, 1.0F, 1.0F, 7.0F, 0.0F, true);

        bullet = new ModelRenderer(this);
        bullet.setPos(-1.0F, -2.52F, 4.224F);
        bullet.texOffs(11, 467).addBox(-1.0F, 18.154F, -4.8959F, 3.0F, 3.0F, 8.0F, 0.0F, false);
        bullet.texOffs(11, 494).addBox(-1.0F, 18.154F, 3.4041F, 3.0F, 3.0F, 2.0F, 0.0F, false);
        bullet.texOffs(26, 499).addBox(-0.1F, 18.254F, 2.4041F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        bullet.texOffs(26, 499).addBox(-0.1F, 19.054F, 2.4041F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        bullet.texOffs(26, 499).addBox(-0.9F, 18.254F, 2.4041F, 2.0F, 2.0F, 1.0F, 0.0F, true);
        bullet.texOffs(26, 499).addBox(-0.9F, 19.054F, 2.4041F, 2.0F, 2.0F, 1.0F, 0.0F, true);

        setSpecialRenderer(ModAnimations.CHARGING_HANDLE, charging_handle);
        setSpecialRenderer(ModAnimations.BULLET, bullet);
    }

    @Override
    public void renderWeapon(ItemStack stack, IPlayerData data, MatrixStack matrix, IVertexBuilder builder, int light, int overlay) {
        gun.render(matrix, builder, light, overlay);
    }
}
