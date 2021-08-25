package dev.toma.gunsrpg.client.model.weapon;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.toma.gunsrpg.client.animation.ModAnimations;
import dev.toma.gunsrpg.common.capability.IPlayerData;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.item.ItemStack;

public class DesertEagleModel extends AbstractWeaponModel {

    private final ModelRenderer slide;
    private final ModelRenderer bone13;
    private final ModelRenderer bone16;
    private final ModelRenderer bone31;
    private final ModelRenderer bone9;
    private final ModelRenderer bone23;
    private final ModelRenderer bone;
    private final ModelRenderer gun;
    private final ModelRenderer bone27;
    private final ModelRenderer bone5;
    private final ModelRenderer bone28;
    private final ModelRenderer bone8;
    private final ModelRenderer bone7;
    private final ModelRenderer bone10;
    private final ModelRenderer bone15;
    private final ModelRenderer bone14;
    private final ModelRenderer bone2;
    private final ModelRenderer bone21;
    private final ModelRenderer bone22;
    private final ModelRenderer bone4;
    private final ModelRenderer bone3;
    private final ModelRenderer bone30;
    private final ModelRenderer bone6;
    private final ModelRenderer bone17;
    private final ModelRenderer bone18;
    private final ModelRenderer rail;
    private final ModelRenderer bone25;
    private final ModelRenderer bone24;
    private final ModelRenderer bone26;
    private final ModelRenderer bone19;
    private final ModelRenderer bone20;
    private final ModelRenderer magazine;
    private final ModelRenderer bullet;
    private final ModelRenderer bullet2;

    @Override
    protected void renderWeapon(ItemStack stack, IPlayerData data, MatrixStack matrix, IVertexBuilder builder, int light, int overlay) {
        gun.render(matrix, builder, light, overlay);
        // TODO render rail for red dot
    }

    public DesertEagleModel() {
        texWidth = 512;
        texHeight = 512;

        slide = new ModelRenderer(this);
        slide.setPos(0.0F, 24.0F, 0.0F);
        slide.texOffs(67, 143).addBox(3.0F, -20.0F, -26.0F, 1.0F, 2.0F, 21.0F, 0.0F, false);
        slide.texOffs(67, 143).addBox(-4.0F, -20.0F, -26.0F, 1.0F, 2.0F, 21.0F, 0.0F, false);
        slide.texOffs(82, 149).addBox(-4.0F, -23.44F, -11.0F, 2.0F, 4.0F, 14.0F, 0.0F, false);
        slide.texOffs(82, 149).addBox(2.0F, -23.44F, -11.0F, 2.0F, 4.0F, 14.0F, 0.0F, false);
        slide.texOffs(82, 149).addBox(-4.0F, -19.44F, 1.0F, 8.0F, 1.0F, 3.0F, 0.0F, false);
        slide.texOffs(82, 149).addBox(-2.5F, -19.44F, 3.616F, 5.0F, 1.0F, 1.0F, 0.0F, false);
        slide.texOffs(82, 149).addBox(-1.5505F, -25.8895F, -11.0F, 2.0F, 2.0F, 14.0F, 0.0F, true);
        slide.texOffs(82, 149).addBox(-0.4495F, -25.8895F, -11.0F, 2.0F, 2.0F, 14.0F, 0.0F, false);
        slide.texOffs(17, 82).addBox(-1.0F, -26.416F, 0.6F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        slide.texOffs(10, 80).addBox(-3.0F, -26.304F, -1.1321F, 6.0F, 1.0F, 2.0F, 0.0F, true);
        slide.texOffs(17, 82).addBox(1.0F, -27.304F, 0.6F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        slide.texOffs(17, 82).addBox(1.452F, -26.3594F, 1.1823F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        slide.texOffs(17, 82).addBox(-3.0F, -27.304F, 0.6F, 2.0F, 2.0F, 1.0F, 0.0F, true);
        slide.texOffs(17, 82).addBox(-2.548F, -26.3594F, 1.1823F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        bone13 = new ModelRenderer(this);
        bone13.setPos(0.0F, -23.0F, -29.0F);
        slide.addChild(bone13);
        setRotationAngle(bone13, 0.0F, 0.0F, 0.2618F);
        bone13.texOffs(82, 149).addBox(0.7498F, -3.1923F, 18.0F, 2.0F, 2.0F, 14.0F, 0.0F, true);
        bone13.texOffs(82, 149).addBox(-3.9776F, -1.3897F, 18.0F, 2.0F, 2.0F, 14.0F, 0.0F, true);

        bone16 = new ModelRenderer(this);
        bone16.setPos(0.0F, -23.0F, -29.0F);
        slide.addChild(bone16);
        setRotationAngle(bone16, 0.0F, 0.0F, -0.2618F);
        bone16.texOffs(82, 149).addBox(-2.7498F, -3.1923F, 18.0F, 2.0F, 2.0F, 14.0F, 0.0F, false);
        bone16.texOffs(82, 149).addBox(1.9776F, -1.3897F, 18.0F, 2.0F, 2.0F, 14.0F, 0.0F, false);

        bone31 = new ModelRenderer(this);
        bone31.setPos(0.0F, -27.16F, -28.68F);
        slide.addChild(bone31);
        setRotationAngle(bone31, -1.0472F, 0.0F, 0.0F);
        bone31.texOffs(17, 82).addBox(1.452F, -26.3272F, 15.1245F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone31.texOffs(41, 489).addBox(1.452F, -26.3411F, 15.1325F, 1.0F, 0.0F, 1.0F, 0.0F, false);
        bone31.texOffs(17, 82).addBox(-2.548F, -26.3272F, 15.1245F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone31.texOffs(41, 489).addBox(-2.548F, -26.3411F, 15.1325F, 1.0F, 0.0F, 1.0F, 0.0F, false);
        bone31.texOffs(10, 80).addBox(2.0F, -25.4292F, 14.5153F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        bone31.texOffs(17, 82).addBox(-3.0F, -25.4292F, 14.5153F, 1.0F, 2.0F, 2.0F, 0.0F, false);

        bone9 = new ModelRenderer(this);
        bone9.setPos(2.0F, 0.896F, -5.912F);
        slide.addChild(bone9);
        setRotationAngle(bone9, 0.1745F, 0.0F, 0.0F);
        bone9.texOffs(82, 149).addBox(-6.0F, -22.4039F, 12.2048F, 8.0F, 4.0F, 1.0F, 0.0F, false);
        bone9.texOffs(82, 149).addBox(-4.5F, -24.2927F, 12.8351F, 5.0F, 6.0F, 1.0F, 0.0F, true);

        bone23 = new ModelRenderer(this);
        bone23.setPos(0.0F, -18.94F, 2.5F);
        slide.addChild(bone23);
        setRotationAngle(bone23, -0.2618F, 0.0F, 0.0F);
        bone23.texOffs(82, 149).addBox(3.0F, -0.1288F, -4.3195F, 1.0F, 1.0F, 3.0F, 0.0F, false);
        bone23.texOffs(82, 149).addBox(-4.0F, -0.1288F, -4.3195F, 1.0F, 1.0F, 3.0F, 0.0F, false);

        bone = new ModelRenderer(this);
        bone.setPos(2.0F, 0.0F, -9.0F);
        slide.addChild(bone);
        setRotationAngle(bone, -0.2618F, 0.0F, 0.0F);
        bone.texOffs(82, 149).addBox(1.0F, -20.4219F, -1.795F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        bone.texOffs(82, 149).addBox(-6.0F, -20.4219F, -1.795F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        gun = new ModelRenderer(this);
        gun.setPos(0.0F, 24.0F, -9.0F);
        gun.texOffs(76, 26).addBox(2.0F, -20.0F, -6.0F, 1.0F, 4.0F, 17.0F, 0.0F, false);
        gun.texOffs(76, 26).addBox(-3.0F, -20.0F, -6.0F, 1.0F, 4.0F, 17.0F, 0.0F, true);
        gun.texOffs(69, 16).addBox(-3.0F, -20.0F, -8.0F, 6.0F, 4.0F, 2.0F, 0.0F, false);
        gun.texOffs(84, 36).addBox(-0.5F, -17.744F, -26.864F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        gun.texOffs(101, 30).addBox(-2.0F, -19.776F, 10.36F, 4.0F, 2.0F, 3.0F, 0.0F, false);
        gun.texOffs(101, 30).addBox(-3.0F, -20.032F, 10.36F, 6.0F, 2.0F, 2.0F, 0.0F, false);
        gun.texOffs(84, 25).addBox(-1.0F, -24.0924F, -28.264F, 2.0F, 1.0F, 16.0F, 0.0F, true);
        gun.texOffs(84, 25).addBox(1.4142F, -22.6782F, -28.264F, 1.0F, 2.0F, 16.0F, 0.0F, true);
        gun.texOffs(84, 25).addBox(-2.4142F, -22.6782F, -28.264F, 1.0F, 2.0F, 16.0F, 0.0F, false);
        gun.texOffs(84, 25).addBox(-1.0F, -20.264F, -28.264F, 2.0F, 1.0F, 16.0F, 0.0F, false);
        gun.texOffs(20, 79).addBox(-1.5F, -23.264F, -26.096F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        gun.texOffs(73, 28).addBox(-3.7321F, -15.6F, 4.0F, 4.0F, 1.0F, 6.0F, 0.0F, true);
        gun.texOffs(99, 38).addBox(-3.7321F, -17.6F, 10.0F, 4.0F, 3.0F, 2.0F, 0.0F, true);
        gun.texOffs(93, 25).addBox(-3.7321F, -17.6F, -4.0F, 4.0F, 3.0F, 8.0F, 0.0F, true);
        gun.texOffs(80, 24).addBox(-3.7321F, -17.6F, -8.0F, 2.0F, 1.0F, 4.0F, 0.0F, true);
        gun.texOffs(73, 28).addBox(-3.1881F, -16.08F, 4.0F, 2.0F, 1.0F, 6.0F, 0.0F, true);
        gun.texOffs(90, 27).addBox(1.1881F, -16.08F, 4.0F, 2.0F, 1.0F, 6.0F, 0.0F, false);
        gun.texOffs(99, 38).addBox(-0.2679F, -15.6F, 4.0F, 4.0F, 1.0F, 6.0F, 0.0F, false);
        gun.texOffs(99, 38).addBox(-0.2679F, -17.6F, 10.0F, 4.0F, 3.0F, 2.0F, 0.0F, false);
        gun.texOffs(81, 24).addBox(-0.2679F, -17.6F, -4.0F, 4.0F, 3.0F, 8.0F, 0.0F, false);
        gun.texOffs(44, 174).addBox(2.8321F, -17.5F, -5.1F, 1.0F, 1.0F, 9.0F, 0.0F, false);
        gun.texOffs(44, 174).addBox(-3.8321F, -17.5F, -5.1F, 1.0F, 1.0F, 9.0F, 0.0F, true);
        gun.texOffs(44, 174).addBox(2.8001F, -16.6F, 0.08F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        gun.texOffs(44, 174).addBox(-3.8001F, -16.6F, 0.08F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        gun.texOffs(44, 174).addBox(2.8001F, -16.552F, 2.848F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        gun.texOffs(44, 174).addBox(-3.8001F, -16.552F, 2.848F, 1.0F, 2.0F, 1.0F, 0.0F, true);
        gun.texOffs(44, 174).addBox(2.8001F, -17.624F, -3.88F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        gun.texOffs(44, 174).addBox(-3.8001F, -17.624F, -3.88F, 1.0F, 2.0F, 1.0F, 0.0F, true);
        gun.texOffs(44, 174).addBox(2.8001F, -16.6F, -1.084F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        gun.texOffs(44, 174).addBox(-3.8001F, -16.6F, -1.084F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        gun.texOffs(44, 174).addBox(2.8321F, -15.7F, -3.9F, 1.0F, 1.0F, 7.0F, 0.0F, false);
        gun.texOffs(44, 174).addBox(-3.8321F, -15.7F, -3.9F, 1.0F, 1.0F, 7.0F, 0.0F, true);
        gun.texOffs(81, 24).addBox(-2.5F, -15.8679F, -7.0F, 5.0F, 1.0F, 3.0F, 0.0F, false);
        gun.texOffs(81, 24).addBox(-1.5F, -14.8679F, -7.0F, 3.0F, 4.0F, 1.0F, 0.0F, false);
        gun.texOffs(81, 24).addBox(-1.0F, -10.8419F, -4.1809F, 2.0F, 1.0F, 6.0F, 0.0F, false);
        gun.texOffs(80, 25).addBox(1.7321F, -17.6F, -8.0F, 2.0F, 1.0F, 4.0F, 0.0F, false);
        gun.texOffs(81, 27).addBox(-3.232F, -20.0F, 2.0F, 1.0F, 2.0F, 9.0F, 0.0F, true);
        gun.texOffs(81, 27).addBox(2.232F, -20.0F, 2.0F, 1.0F, 2.0F, 9.0F, 0.0F, false);
        gun.texOffs(105, 25).addBox(-1.0F, -15.8679F, -8.0F, 2.0F, 2.0F, 1.0F, 0.0F, true);

        bone27 = new ModelRenderer(this);
        bone27.setPos(0.16F, -25.4F, -20.0F);
        gun.addChild(bone27);
        setRotationAngle(bone27, 0.0F, 0.0F, 0.5236F);
        bone27.texOffs(81, 22).addBox(5.4935F, 5.835F, 12.0F, 2.0F, 2.0F, 4.0F, 0.0F, false);
        bone27.texOffs(100, 23).addBox(2.7614F, 8.567F, 12.0F, 2.0F, 2.0F, 1.0F, 0.0F, false);

        bone5 = new ModelRenderer(this);
        bone5.setPos(-0.16F, -25.4F, -20.0F);
        gun.addChild(bone5);
        setRotationAngle(bone5, 0.0F, 0.0F, -0.5236F);
        bone5.texOffs(80, 24).addBox(-7.4935F, 5.835F, 12.0F, 2.0F, 2.0F, 4.0F, 0.0F, true);
        bone5.texOffs(105, 25).addBox(-4.7614F, 8.567F, 12.0F, 2.0F, 2.0F, 1.0F, 0.0F, true);

        bone28 = new ModelRenderer(this);
        bone28.setPos(0.0F, -22.028F, 12.604F);
        gun.addChild(bone28);
        setRotationAngle(bone28, -0.2618F, 0.0F, 0.0F);
        bone28.texOffs(99, 38).addBox(-2.0F, 7.4998F, -1.0525F, 4.0F, 3.0F, 2.0F, 0.0F, false);

        bone8 = new ModelRenderer(this);
        bone8.setPos(0.0F, -21.264F, -22.264F);
        gun.addChild(bone8);
        setRotationAngle(bone8, 0.0F, 0.0F, -0.7854F);
        bone8.texOffs(84, 25).addBox(-2.1213F, -1.2929F, -6.0F, 1.0F, 2.0F, 16.0F, 0.0F, false);
        bone8.texOffs(84, 25).addBox(-0.7071F, -2.7071F, -6.0F, 2.0F, 1.0F, 16.0F, 0.0F, false);
        bone8.texOffs(84, 25).addBox(-0.7071F, 1.1213F, -6.0F, 2.0F, 1.0F, 16.0F, 0.0F, false);
        bone8.texOffs(84, 25).addBox(1.7071F, -1.2929F, -6.0F, 1.0F, 2.0F, 16.0F, 0.0F, false);

        bone7 = new ModelRenderer(this);
        bone7.setPos(-1.2679F, 0.0F, 0.0F);
        gun.addChild(bone7);
        setRotationAngle(bone7, 0.2618F, 0.0F, 0.0F);
        bone7.texOffs(93, 32).addBox(-1.7321F, -14.0732F, 4.4856F, 6.0F, 14.0F, 9.0F, 0.0F, true);

        bone10 = new ModelRenderer(this);
        bone10.setPos(0.0F, -17.408F, 8.168F);
        gun.addChild(bone10);
        setRotationAngle(bone10, -0.0524F, 0.0F, 0.0F);
        bone10.texOffs(106, 97).addBox(-4.2F, -0.336F, -0.56F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        bone10.texOffs(13, 85).addBox(2.2F, -0.336F, -0.56F, 2.0F, 2.0F, 2.0F, 0.0F, true);
        bone10.texOffs(106, 97).addBox(-4.3F, 0.164F, -0.06F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone10.texOffs(111, 41).addBox(3.3F, 0.164F, -0.06F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone10.texOffs(106, 97).addBox(-4.2F, -0.336F, -3.46F, 1.0F, 1.0F, 3.0F, 0.0F, false);
        bone10.texOffs(13, 85).addBox(3.2F, -0.336F, -3.46F, 1.0F, 1.0F, 3.0F, 0.0F, true);

        bone15 = new ModelRenderer(this);
        bone15.setPos(0.0F, -13.3679F, -6.5F);
        gun.addChild(bone15);
        setRotationAngle(bone15, 1.2217F, 0.0F, 0.0F);
        bone15.texOffs(81, 24).addBox(-1.5F, 0.3852F, -2.5202F, 3.0F, 2.0F, 1.0F, 0.0F, false);
        bone15.texOffs(81, 24).addBox(-1.0F, 2.3852F, -2.5202F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        bone15.texOffs(81, 24).addBox(-1.0F, 8.0234F, -0.4681F, 2.0F, 1.0F, 2.0F, 0.0F, false);

        bone14 = new ModelRenderer(this);
        bone14.setPos(0.5F, 0.0F, 0.0F);
        gun.addChild(bone14);
        bone14.texOffs(82, 149).addBox(-3.5F, -20.0F, -26.0F, 6.0F, 4.0F, 18.0F, 0.0F, false);
        bone14.texOffs(82, 149).addBox(-0.9142F, -16.5858F, -26.0F, 2.0F, 2.0F, 18.0F, 0.0F, true);
        bone14.texOffs(82, 149).addBox(-2.0858F, -16.5858F, -26.0F, 2.0F, 2.0F, 18.0F, 0.0F, true);
        bone14.texOffs(82, 149).addBox(-4.5F, -20.0F, -26.0F, 8.0F, 2.0F, 9.0F, 0.0F, false);
        bone14.texOffs(74, 144).addBox(-1.5F, -24.976F, -26.0F, 2.0F, 2.0F, 24.0F, 0.0F, false);
        bone14.texOffs(8, 83).addBox(-2.0F, -25.592F, -25.0F, 3.0F, 1.0F, 5.0F, 0.0F, false);
        bone14.texOffs(12, 75).addBox(-1.5F, -26.592F, -24.68F, 2.0F, 1.0F, 3.0F, 0.0F, false);
        bone14.texOffs(23, 77).addBox(-1.5F, -27.592F, -24.28F, 2.0F, 1.0F, 2.0F, 0.0F, false);
        bone14.texOffs(23, 77).addBox(-1.0F, -28.16F, -23.68F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone14.texOffs(23, 77).addBox(-1.0F, -28.08F, -24.6F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        bone14.texOffs(23, 77).addBox(-1.0F, -26.8714F, -22.2737F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        bone2 = new ModelRenderer(this);
        bone2.setPos(-0.5F, 0.0F, 0.0F);
        bone14.addChild(bone2);
        setRotationAngle(bone2, 0.0F, 0.0F, 0.7854F);
        bone2.texOffs(82, 149).addBox(-13.435F, -11.1924F, -26.0F, 2.0F, 2.0F, 18.0F, 0.0F, false);
        bone2.texOffs(82, 149).addBox(-15.5563F, -11.8995F, -26.0F, 2.0F, 2.0F, 9.0F, 0.0F, false);
        bone2.texOffs(82, 149).addBox(-11.8995F, -15.5563F, -26.0F, 2.0F, 2.0F, 9.0F, 0.0F, false);
        bone2.texOffs(82, 149).addBox(-11.1924F, -13.435F, -26.0F, 2.0F, 2.0F, 18.0F, 0.0F, false);

        bone21 = new ModelRenderer(this);
        bone21.setPos(-14.5563F, -10.8995F, -22.0F);
        bone2.addChild(bone21);
        setRotationAngle(bone21, 0.4363F, 0.0F, 0.0F);


        bone22 = new ModelRenderer(this);
        bone22.setPos(-10.8995F, -14.5563F, -22.0F);
        bone2.addChild(bone22);
        setRotationAngle(bone22, 0.0F, -0.4363F, 0.0F);


        bone4 = new ModelRenderer(this);
        bone4.setPos(-0.5F, -23.0F, -20.0F);
        bone14.addChild(bone4);
        setRotationAngle(bone4, 0.0F, 0.0F, 0.2618F);
        bone4.texOffs(82, 149).addBox(-2.8916F, -0.2356F, 6.0F, 2.0F, 4.0F, 12.0F, 0.0F, true);
        bone4.texOffs(82, 149).addBox(-2.8916F, -0.2356F, -6.0F, 2.0F, 4.0F, 12.0F, 0.0F, true);
        bone4.texOffs(30, 100).addBox(-3.0621F, 1.2407F, 6.544F, 2.0F, 2.0F, 1.0F, 0.0F, true);
        bone4.texOffs(30, 100).addBox(-3.0621F, 1.2407F, -3.056F, 2.0F, 2.0F, 1.0F, 0.0F, true);
        bone4.texOffs(30, 100).addBox(-3.0621F, 1.2407F, 4.144F, 2.0F, 2.0F, 1.0F, 0.0F, true);
        bone4.texOffs(30, 100).addBox(-3.0621F, 1.2407F, -5.456F, 2.0F, 2.0F, 1.0F, 0.0F, true);
        bone4.texOffs(30, 100).addBox(-3.0621F, 1.2407F, 8.944F, 2.0F, 2.0F, 1.0F, 0.0F, true);
        bone4.texOffs(30, 100).addBox(-3.0621F, 1.2407F, -0.656F, 2.0F, 2.0F, 1.0F, 0.0F, true);
        bone4.texOffs(30, 100).addBox(-3.0621F, 1.2407F, 13.744F, 2.0F, 2.0F, 1.0F, 0.0F, true);
        bone4.texOffs(30, 100).addBox(-3.0621F, 1.2407F, 11.344F, 2.0F, 2.0F, 1.0F, 0.0F, true);
        bone4.texOffs(30, 100).addBox(-3.0621F, 1.2407F, 1.744F, 2.0F, 2.0F, 1.0F, 0.0F, true);
        bone4.texOffs(30, 100).addBox(-3.0621F, 1.2407F, 16.144F, 2.0F, 2.0F, 1.0F, 0.0F, true);

        bone3 = new ModelRenderer(this);
        bone3.setPos(-0.5F, -23.0F, -20.0F);
        bone14.addChild(bone3);
        setRotationAngle(bone3, 0.0F, 0.0F, -0.2618F);
        bone3.texOffs(82, 149).addBox(0.8916F, -0.2356F, 6.0F, 2.0F, 4.0F, 12.0F, 0.0F, false);
        bone3.texOffs(82, 149).addBox(0.8916F, -0.2356F, -6.0F, 2.0F, 4.0F, 12.0F, 0.0F, false);
        bone3.texOffs(30, 100).addBox(1.0621F, 1.2407F, 6.544F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        bone3.texOffs(30, 100).addBox(1.0621F, 1.2407F, -3.056F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        bone3.texOffs(30, 100).addBox(1.0621F, 1.2407F, 4.144F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        bone3.texOffs(30, 100).addBox(1.0621F, 1.2407F, -5.456F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        bone3.texOffs(30, 100).addBox(1.0621F, 1.2407F, 8.944F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        bone3.texOffs(30, 100).addBox(1.0621F, 1.2407F, -0.656F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        bone3.texOffs(30, 100).addBox(1.0621F, 1.2407F, 13.744F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        bone3.texOffs(30, 100).addBox(1.0621F, 1.2407F, 11.344F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        bone3.texOffs(30, 100).addBox(1.0621F, 1.2407F, 1.744F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        bone3.texOffs(30, 100).addBox(1.0621F, 1.2407F, 16.144F, 2.0F, 2.0F, 1.0F, 0.0F, false);

        bone30 = new ModelRenderer(this);
        bone30.setPos(-0.66F, -25.4F, -20.0F);
        bone14.addChild(bone30);
        setRotationAngle(bone30, 0.0F, 0.0F, -0.5236F);
        bone30.texOffs(73, 142).addBox(-2.9395F, -0.0528F, -6.0F, 2.0F, 2.0F, 24.0F, 0.0F, true);
        bone30.texOffs(82, 149).addBox(-6.0255F, 2.7565F, -6.0F, 2.0F, 2.0F, 9.0F, 0.0F, true);

        bone6 = new ModelRenderer(this);
        bone6.setPos(-0.34F, -25.4F, -20.0F);
        bone14.addChild(bone6);
        setRotationAngle(bone6, 0.0F, 0.0F, 0.5236F);
        bone6.texOffs(71, 139).addBox(0.9395F, -0.0528F, -6.0F, 2.0F, 2.0F, 24.0F, 0.0F, false);
        bone6.texOffs(82, 149).addBox(4.0255F, 2.7565F, -6.0F, 2.0F, 2.0F, 9.0F, 0.0F, false);

        bone17 = new ModelRenderer(this);
        bone17.setPos(-0.5F, -27.16F, -19.68F);
        bone14.addChild(bone17);
        setRotationAngle(bone17, -0.4363F, 0.0F, 0.0F);
        bone17.texOffs(23, 77).addBox(-0.5F, 0.3615F, -3.1415F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        bone18 = new ModelRenderer(this);
        bone18.setPos(-0.5F, -27.16F, -19.68F);
        bone14.addChild(bone18);
        setRotationAngle(bone18, -1.0472F, 0.0F, 0.0F);
        bone18.texOffs(23, 77).addBox(-0.5F, 1.5245F, -1.5469F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone18.texOffs(41, 489).addBox(-0.5F, 1.5072F, -1.5369F, 1.0F, 0.0F, 1.0F, 0.0F, false);

        rail = new ModelRenderer(this);
        rail.setPos(-1.0F, 23.872F, 6.0F);
        rail.texOffs(0, 68).addBox(0.0F, -28.416F, -10.4F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        rail.texOffs(0, 68).addBox(0.0F, -28.416F, -18.4F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        rail.texOffs(0, 68).addBox(0.0F, -28.416F, -14.4F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        rail.texOffs(0, 68).addBox(0.0F, -28.416F, -26.4F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        rail.texOffs(0, 68).addBox(0.0F, -28.416F, -22.4F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        rail.texOffs(0, 68).addBox(0.0F, -28.416F, -12.4F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        rail.texOffs(0, 68).addBox(0.0F, -28.416F, -20.4F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        rail.texOffs(0, 68).addBox(0.0F, -28.416F, -16.4F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        rail.texOffs(0, 68).addBox(0.0F, -28.416F, -28.4F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        rail.texOffs(0, 68).addBox(0.0F, -28.416F, -24.4F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        rail.texOffs(0, 68).addBox(-0.8669F, -27.9155F, -11.4F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        rail.texOffs(0, 68).addBox(-0.8669F, -27.9155F, -19.4F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        rail.texOffs(0, 68).addBox(-0.8669F, -27.9155F, -15.4F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        rail.texOffs(0, 68).addBox(-0.8669F, -27.9155F, -27.4F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        rail.texOffs(0, 68).addBox(-0.8669F, -27.9155F, -23.4F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        rail.texOffs(0, 68).addBox(-0.8669F, -27.9155F, -13.4F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        rail.texOffs(0, 68).addBox(-0.8669F, -27.9155F, -25.4F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        rail.texOffs(0, 68).addBox(-0.8669F, -27.9155F, -21.4F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        rail.texOffs(0, 68).addBox(-0.8669F, -27.9155F, -17.4F, 2.0F, 0.0F, 1.0F, 0.0F, false);
        rail.texOffs(0, 68).addBox(0.8669F, -27.9155F, -11.4F, 2.0F, 0.0F, 1.0F, 0.0F, true);
        rail.texOffs(0, 68).addBox(0.8669F, -27.9155F, -19.4F, 2.0F, 0.0F, 1.0F, 0.0F, true);
        rail.texOffs(0, 68).addBox(0.8669F, -27.9155F, -15.4F, 2.0F, 0.0F, 1.0F, 0.0F, true);
        rail.texOffs(0, 68).addBox(0.8669F, -27.9155F, -27.4F, 2.0F, 0.0F, 1.0F, 0.0F, true);
        rail.texOffs(0, 68).addBox(0.8669F, -27.9155F, -23.4F, 2.0F, 0.0F, 1.0F, 0.0F, true);
        rail.texOffs(0, 68).addBox(0.8669F, -27.9155F, -13.4F, 2.0F, 0.0F, 1.0F, 0.0F, true);
        rail.texOffs(0, 68).addBox(0.8669F, -27.9155F, -25.4F, 2.0F, 0.0F, 1.0F, 0.0F, true);
        rail.texOffs(0, 68).addBox(0.8669F, -27.9155F, -21.4F, 2.0F, 0.0F, 1.0F, 0.0F, true);
        rail.texOffs(0, 68).addBox(0.8669F, -27.9155F, -17.4F, 2.0F, 0.0F, 1.0F, 0.0F, true);
        rail.texOffs(0, 68).addBox(-0.5F, -27.704F, -28.4F, 3.0F, 1.0F, 10.0F, 0.0F, false);
        rail.texOffs(0, 68).addBox(-0.5F, -27.704F, -18.4F, 3.0F, 1.0F, 9.0F, 0.0F, false);
        rail.texOffs(67, 105).addBox(-0.5F, -26.704F, -23.2F, 3.0F, 1.0F, 5.0F, 0.0F, false);
        rail.texOffs(67, 105).addBox(-1.0F, -26.428F, -24.2F, 4.0F, 1.0F, 7.0F, 0.0F, false);
        rail.texOffs(68, 108).addBox(-1.0F, -26.028F, -24.2F, 4.0F, 1.0F, 7.0F, 0.0F, false);
        rail.texOffs(0, 68).addBox(-2.0F, -26.228F, -24.0F, 6.0F, 1.0F, 1.0F, 0.0F, false);
        rail.texOffs(0, 68).addBox(-2.0F, -26.228F, -18.4F, 6.0F, 1.0F, 1.0F, 0.0F, false);
        rail.texOffs(0, 68).addBox(5.1213F, -24.1067F, -18.4F, 1.0F, 2.0F, 1.0F, 0.0F, true);
        rail.texOffs(0, 68).addBox(-4.1213F, -19.4727F, -12.8378F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        rail.texOffs(0, 68).addBox(-2.8213F, -16.7047F, -12.8378F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        rail.texOffs(0, 68).addBox(3.8213F, -16.7047F, -12.8378F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        rail.texOffs(0, 68).addBox(5.1213F, -19.4727F, -12.8378F, 1.0F, 2.0F, 1.0F, 0.0F, true);
        rail.texOffs(0, 68).addBox(5.2493F, -18.8247F, -18.0218F, 1.0F, 1.0F, 6.0F, 0.0F, true);
        rail.texOffs(0, 68).addBox(-4.2493F, -18.8247F, -18.0218F, 1.0F, 1.0F, 6.0F, 0.0F, false);
        rail.texOffs(0, 68).addBox(-4.2493F, -23.8647F, -23.8618F, 1.0F, 1.0F, 6.0F, 0.0F, false);
        rail.texOffs(0, 68).addBox(5.2493F, -23.8647F, -23.8618F, 1.0F, 1.0F, 6.0F, 0.0F, true);
        rail.texOffs(0, 68).addBox(3.0253F, -26.3447F, -23.8618F, 1.0F, 1.0F, 6.0F, 0.0F, true);
        rail.texOffs(0, 68).addBox(-2.0253F, -26.3447F, -23.8618F, 1.0F, 1.0F, 6.0F, 0.0F, false);
        rail.texOffs(0, 68).addBox(-4.1213F, -19.4727F, -18.4378F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        rail.texOffs(0, 68).addBox(-2.8213F, -16.7047F, -18.4378F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        rail.texOffs(0, 68).addBox(3.8213F, -16.7047F, -18.4378F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        rail.texOffs(0, 68).addBox(5.1213F, -19.4727F, -18.4378F, 1.0F, 2.0F, 1.0F, 0.0F, true);
        rail.texOffs(0, 68).addBox(-4.1213F, -24.1067F, -18.4F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        rail.texOffs(0, 68).addBox(5.1213F, -24.1067F, -24.0F, 1.0F, 2.0F, 1.0F, 0.0F, true);
        rail.texOffs(0, 68).addBox(-4.1213F, -24.1067F, -24.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        bone25 = new ModelRenderer(this);
        bone25.setPos(1.0F, -25.728F, -26.7F);
        rail.addChild(bone25);
        setRotationAngle(bone25, 0.0F, 0.0F, 0.7854F);
        bone25.texOffs(0, 68).addBox(1.7678F, -2.4749F, 2.7F, 3.0F, 1.0F, 1.0F, 0.0F, true);
        bone25.texOffs(0, 68).addBox(8.4587F, 2.2161F, 8.2622F, 1.0F, 2.0F, 1.0F, 0.0F, true);
        bone25.texOffs(0, 68).addBox(1.7678F, -2.4749F, 8.3F, 3.0F, 1.0F, 1.0F, 0.0F, true);
        bone25.texOffs(0, 68).addBox(8.4587F, 2.2161F, 13.8622F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        bone24 = new ModelRenderer(this);
        bone24.setPos(1.0F, -25.728F, -26.7F);
        rail.addChild(bone24);
        setRotationAngle(bone24, 0.0F, 0.0F, -0.7854F);
        bone24.texOffs(0, 68).addBox(-4.7678F, -2.4749F, 2.7F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        bone24.texOffs(0, 68).addBox(-9.4587F, 2.2161F, 8.2622F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        bone24.texOffs(0, 68).addBox(-4.7678F, -2.4749F, 8.3F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        bone24.texOffs(0, 68).addBox(-9.4587F, 2.2161F, 13.8622F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        bone26 = new ModelRenderer(this);
        bone26.setPos(5.6213F, -23.6067F, -21.1F);
        rail.addChild(bone26);
        setRotationAngle(bone26, -0.5236F, 0.0F, 0.0F);
        bone26.texOffs(0, 68).addBox(-0.5F, -1.051F, 3.0883F, 1.0F, 1.0F, 7.0F, 0.0F, true);
        bone26.texOffs(0, 68).addBox(-9.7426F, -1.051F, 3.0883F, 1.0F, 1.0F, 7.0F, 0.0F, false);
        bone26.texOffs(0, 68).addBox(-0.5F, 1.749F, -1.7615F, 1.0F, 1.0F, 7.0F, 0.0F, true);
        bone26.texOffs(0, 68).addBox(-9.7426F, 1.749F, -1.7615F, 1.0F, 1.0F, 7.0F, 0.0F, false);

        bone19 = new ModelRenderer(this);
        bone19.setPos(1.0F, -27.916F, -9.9F);
        rail.addChild(bone19);
        setRotationAngle(bone19, 0.0F, 0.0F, -0.5236F);
        bone19.texOffs(0, 68).addBox(-1.616F, -0.933F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone19.texOffs(0, 68).addBox(-1.616F, -0.933F, -8.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone19.texOffs(0, 68).addBox(-1.616F, -0.933F, -4.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone19.texOffs(0, 68).addBox(-1.616F, -0.933F, -16.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone19.texOffs(0, 68).addBox(-1.616F, -0.933F, -12.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone19.texOffs(0, 68).addBox(-1.616F, -0.933F, -2.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone19.texOffs(0, 68).addBox(-1.616F, -0.933F, -10.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone19.texOffs(0, 68).addBox(-1.616F, -0.933F, -6.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone19.texOffs(0, 68).addBox(-1.616F, -0.933F, -18.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone19.texOffs(0, 68).addBox(-1.616F, -0.933F, -14.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone19.texOffs(0, 68).addBox(-1.616F, -0.933F, -1.5F, 0.0F, 1.0F, 1.0F, 0.0F, false);
        bone19.texOffs(0, 68).addBox(-1.616F, -0.933F, -9.5F, 0.0F, 1.0F, 1.0F, 0.0F, false);
        bone19.texOffs(0, 68).addBox(-1.616F, -0.933F, -5.5F, 0.0F, 1.0F, 1.0F, 0.0F, false);
        bone19.texOffs(0, 68).addBox(-1.616F, -0.933F, -17.5F, 0.0F, 1.0F, 1.0F, 0.0F, false);
        bone19.texOffs(0, 68).addBox(-1.616F, -0.933F, -13.5F, 0.0F, 1.0F, 1.0F, 0.0F, false);
        bone19.texOffs(0, 68).addBox(-1.616F, -0.933F, -3.5F, 0.0F, 1.0F, 1.0F, 0.0F, false);
        bone19.texOffs(0, 68).addBox(-1.616F, -0.933F, -15.5F, 0.0F, 1.0F, 1.0F, 0.0F, false);
        bone19.texOffs(0, 68).addBox(-1.616F, -0.933F, -11.5F, 0.0F, 1.0F, 1.0F, 0.0F, false);
        bone19.texOffs(0, 68).addBox(-1.616F, -0.933F, -7.5F, 0.0F, 1.0F, 1.0F, 0.0F, false);

        bone20 = new ModelRenderer(this);
        bone20.setPos(1.0F, -27.916F, -9.9F);
        rail.addChild(bone20);
        setRotationAngle(bone20, 0.0F, 0.0F, 0.5236F);
        bone20.texOffs(0, 68).addBox(0.616F, -0.933F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone20.texOffs(0, 68).addBox(0.616F, -0.933F, -8.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone20.texOffs(0, 68).addBox(0.616F, -0.933F, -4.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone20.texOffs(0, 68).addBox(0.616F, -0.933F, -16.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone20.texOffs(0, 68).addBox(0.616F, -0.933F, -12.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone20.texOffs(0, 68).addBox(0.616F, -0.933F, -2.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone20.texOffs(0, 68).addBox(0.616F, -0.933F, -10.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone20.texOffs(0, 68).addBox(0.616F, -0.933F, -6.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone20.texOffs(0, 68).addBox(0.616F, -0.933F, -18.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone20.texOffs(0, 68).addBox(0.616F, -0.933F, -14.5F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        bone20.texOffs(0, 68).addBox(1.616F, -0.933F, -1.5F, 0.0F, 1.0F, 1.0F, 0.0F, true);
        bone20.texOffs(0, 68).addBox(1.616F, -0.933F, -9.5F, 0.0F, 1.0F, 1.0F, 0.0F, true);
        bone20.texOffs(0, 68).addBox(1.616F, -0.933F, -5.5F, 0.0F, 1.0F, 1.0F, 0.0F, true);
        bone20.texOffs(0, 68).addBox(1.616F, -0.933F, -17.5F, 0.0F, 1.0F, 1.0F, 0.0F, true);
        bone20.texOffs(0, 68).addBox(1.616F, -0.933F, -13.5F, 0.0F, 1.0F, 1.0F, 0.0F, true);
        bone20.texOffs(0, 68).addBox(1.616F, -0.933F, -3.5F, 0.0F, 1.0F, 1.0F, 0.0F, true);
        bone20.texOffs(0, 68).addBox(1.616F, -0.933F, -15.5F, 0.0F, 1.0F, 1.0F, 0.0F, true);
        bone20.texOffs(0, 68).addBox(1.616F, -0.933F, -11.5F, 0.0F, 1.0F, 1.0F, 0.0F, true);
        bone20.texOffs(0, 68).addBox(1.616F, -0.933F, -7.5F, 0.0F, 1.0F, 1.0F, 0.0F, true);

        magazine = new ModelRenderer(this);
        magazine.setPos(-1.2679F, 24.0F, -9.0F);
        setRotationAngle(magazine, 0.2618F, 0.0F, 0.0F);
        magazine.texOffs(80, 90).addBox(-1.2321F, -1.4732F, 3.8696F, 5.0F, 2.0F, 9.0F, 0.0F, true);
        magazine.texOffs(80, 90).addBox(-1.2321F, -15.4732F, 4.8696F, 5.0F, 14.0F, 8.0F, 0.0F, true);
        magazine.texOffs(80, 90).addBox(-1.2321F, -18.4732F, 4.8696F, 1.0F, 3.0F, 8.0F, 0.0F, true);
        magazine.texOffs(80, 90).addBox(2.7679F, -18.4732F, 4.8696F, 1.0F, 3.0F, 8.0F, 0.0F, true);
        magazine.texOffs(80, 90).addBox(-0.2321F, -16.4732F, 4.8696F, 3.0F, 1.0F, 1.0F, 0.0F, true);
        magazine.texOffs(80, 90).addBox(-0.2321F, -17.4732F, 11.8696F, 3.0F, 2.0F, 1.0F, 0.0F, true);

        bullet = new ModelRenderer(this);
        bullet.setPos(-1.2679F, 24.0F, -9.0F);
        setRotationAngle(bullet, 0.2618F, 0.0F, 0.0F);


        bullet2 = new ModelRenderer(this);
        bullet2.setPos(1.2679F, 0.0F, 9.0F);
        bullet.addChild(bullet2);
        bullet2.texOffs(8, 497).addBox(-1.0F, -18.5852F, -3.5424F, 2.0F, 2.0F, 6.0F, 0.0F, true);
        bullet2.texOffs(8, 497).addBox(-1.5F, -19.0852F, -2.3024F, 3.0F, 3.0F, 4.0F, 0.0F, true);
        bullet2.texOffs(8, 497).addBox(-1.5F, -19.0852F, 2.2656F, 3.0F, 3.0F, 1.0F, 0.0F, true);

        setSpecialRenderer(ModAnimations.MAGAZINE, magazine);
        setSpecialRenderer(ModAnimations.SLIDE, slide);
        setBulletRenderer(setSpecialRenderer(ModAnimations.BULLET, bullet));
    }
}
