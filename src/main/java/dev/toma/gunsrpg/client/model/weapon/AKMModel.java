package dev.toma.gunsrpg.client.model.weapon;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.toma.gunsrpg.client.animation.ModAnimations;
import dev.toma.gunsrpg.common.capability.IPlayerData;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.item.ItemStack;

public class AKMModel extends AbstractWeaponModel {

    private final ModelRenderer bolt;
    private final ModelRenderer bone23;
    private final ModelRenderer bone26;
    private final ModelRenderer bone24;
    private final ModelRenderer bone19;
    private final ModelRenderer akm;
    private final ModelRenderer bone15;
    private final ModelRenderer bone16;
    private final ModelRenderer bone9;
    private final ModelRenderer bone10;
    private final ModelRenderer bone13;
    private final ModelRenderer bone11;
    private final ModelRenderer bone12;
    private final ModelRenderer bone3;
    private final ModelRenderer dust_cover;
    private final ModelRenderer bone29;
    private final ModelRenderer bone28;
    private final ModelRenderer bone17;
    private final ModelRenderer bone22;
    private final ModelRenderer bone21;
    private final ModelRenderer bone46;
    private final ModelRenderer bone48;
    private final ModelRenderer bone47;
    private final ModelRenderer bone45;
    private final ModelRenderer bone43;
    private final ModelRenderer bone44;
    private final ModelRenderer bone39;
    private final ModelRenderer bone40;
    private final ModelRenderer bone41;
    private final ModelRenderer bone42;
    private final ModelRenderer bone36;
    private final ModelRenderer bone37;
    private final ModelRenderer bone34;
    private final ModelRenderer bone35;
    private final ModelRenderer bone33;
    private final ModelRenderer bone14;
    private final ModelRenderer bone18;
    private final ModelRenderer bone20;
    private final ModelRenderer stock;
    private final ModelRenderer bone32;
    private final ModelRenderer bone30;
    private final ModelRenderer bone31;
    private final ModelRenderer bone27;
    private final ModelRenderer bone38;
    private final ModelRenderer bone25;
    private final ModelRenderer bone2;
    private final ModelRenderer magazine;
    private final ModelRenderer bone4;
    private final ModelRenderer bone;
    private final ModelRenderer bone5;
    private final ModelRenderer bone6;
    private final ModelRenderer bone7;
    private final ModelRenderer bone8;
    private final ModelRenderer bullet;
    private final ModelRenderer bullet2;

    @Override
    protected void renderWeapon(ItemStack stack, IPlayerData data, MatrixStack matrix, IVertexBuilder builder, int light, int overlay) {
        akm.render(matrix, builder, light, overlay);
    }

    public AKMModel() {
        texWidth = 512;
        texHeight = 512;

        bolt = new ModelRenderer(this);
        bolt.setPos(-5.2266F, -37.9844F, -3.6563F);
        setRotationAngle(bolt, 0.0F, 0.0F, 0.2443F);
        bolt.texOffs(13, 148).addBox(1.9339F, 1.7672F, -13.0F, 2.0F, 4.0F, 18.0F, 0.0F, false);

        bone23 = new ModelRenderer(this);
        bone23.setPos(1.6874F, 3.7672F, -9.191F);
        bolt.addChild(bone23);
        setRotationAngle(bone23, 0.0F, -0.2618F, 0.0F);
        bone23.texOffs(13, 148).addBox(-0.4688F, -1.0F, -2.5F, 2.0F, 2.0F, 4.0F, 0.0F, false);
        bone23.texOffs(13, 148).addBox(-2.4688F, -1.0F, -0.5F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        bone26 = new ModelRenderer(this);
        bone26.setPos(1.6874F, 3.7672F, -9.191F);
        bolt.addChild(bone26);
        setRotationAngle(bone26, 0.0F, -0.6981F, 0.0F);
        bone26.texOffs(13, 148).addBox(-2.6035F, -1.0F, 0.4028F, 1.0F, 2.0F, 2.0F, 0.0F, false);

        bone24 = new ModelRenderer(this);
        bone24.setPos(1.6874F, 3.7672F, -9.191F);
        bolt.addChild(bone24);
        setRotationAngle(bone24, 0.0F, -0.5236F, 0.0F);


        bone19 = new ModelRenderer(this);
        bone19.setPos(2.8245F, 3.7672F, -9.2656F);
        bolt.addChild(bone19);
        setRotationAngle(bone19, 0.0F, 0.0698F, 0.0F);
        bone19.texOffs(13, 148).addBox(-1.2093F, -1.0F, 0.9927F, 2.0F, 2.0F, 4.0F, 0.0F, false);

        akm = new ModelRenderer(this);
        akm.setPos(0.0F, 24.0F, 0.0F);
        akm.texOffs(67, 78).addBox(-5.0F, -50.0F, 10.0F, 10.0F, 2.0F, 14.0F, 0.0F, false);
        akm.texOffs(88, 83).addBox(-0.1094F, -48.5078F, 15.2344F, 4.0F, 2.0F, 8.0F, 0.0F, false);
        akm.texOffs(83, 86).addBox(-3.8906F, -48.5078F, 15.2344F, 4.0F, 2.0F, 8.0F, 0.0F, true);
        akm.texOffs(86, 97).addBox(-5.0F, -54.0F, -12.0F, 2.0F, 4.0F, 23.0F, 0.0F, false);
        akm.texOffs(67, 78).addBox(-5.0F, -54.0F, 11.0F, 2.0F, 4.0F, 13.0F, 0.0F, false);
        akm.texOffs(83, 87).addBox(-2.0F, -48.4525F, 4.9637F, 4.0F, 6.0F, 2.0F, 0.0F, false);
        akm.texOffs(83, 87).addBox(-4.0F, -49.6478F, 1.0731F, 8.0F, 6.0F, 4.0F, 0.0F, false);
        akm.texOffs(83, 87).addBox(-3.2766F, -43.6478F, 1.0731F, 2.0F, 2.0F, 4.0F, 0.0F, false);
        akm.texOffs(83, 87).addBox(1.2766F, -43.6478F, 1.0731F, 2.0F, 2.0F, 4.0F, 0.0F, true);
        akm.texOffs(68, 76).addBox(-5.0F, -58.0F, 0.0F, 2.0F, 4.0F, 26.0F, 0.0F, false);
        akm.texOffs(68, 76).addBox(3.0F, -58.0F, 0.0F, 2.0F, 4.0F, 26.0F, 0.0F, true);
        akm.texOffs(36, 73).addBox(-0.7573F, -62.2158F, -18.0F, 2.0F, 2.0F, 40.0F, 0.0F, true);
        akm.texOffs(67, 78).addBox(-0.7573F, -62.7158F, 16.0F, 2.0F, 2.0F, 2.0F, 0.0F, true);
        akm.texOffs(67, 78).addBox(-0.7573F, -62.7158F, 1.0F, 2.0F, 2.0F, 2.0F, 0.0F, true);
        akm.texOffs(67, 78).addBox(-0.7573F, -62.7158F, -14.0F, 2.0F, 2.0F, 2.0F, 0.0F, true);
        akm.texOffs(67, 78).addBox(4.0F, -58.5F, 16.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);
        akm.texOffs(67, 78).addBox(4.0F, -58.5F, 1.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);
        akm.texOffs(67, 78).addBox(4.0F, -58.5F, -14.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);
        akm.texOffs(67, 78).addBox(-5.0F, -58.5F, 16.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        akm.texOffs(86, 97).addBox(-5.0F, -58.5F, 1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        akm.texOffs(22, 98).addBox(-0.7573F, -62.5127F, -29.0F, 2.0F, 1.0F, 1.0F, 0.0F, true);
        akm.texOffs(89, 88).addBox(-2.0F, -61.9346F, 20.4453F, 4.0F, 2.0F, 2.0F, 0.0F, true);
        akm.texOffs(36, 73).addBox(-1.2427F, -62.2158F, -18.0F, 2.0F, 2.0F, 40.0F, 0.0F, false);
        akm.texOffs(67, 78).addBox(-1.2427F, -62.7158F, 16.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        akm.texOffs(67, 78).addBox(-1.2427F, -62.7158F, 1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        akm.texOffs(67, 78).addBox(-1.2427F, -62.7158F, -14.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        akm.texOffs(22, 98).addBox(-1.2427F, -62.5127F, -29.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        akm.texOffs(86, 97).addBox(-5.0F, -58.0F, -24.0F, 10.0F, 6.0F, 8.0F, 0.0F, false);
        akm.texOffs(137, 148).addBox(-5.5F, -57.0F, -33.0F, 11.0F, 2.0F, 9.0F, 0.0F, false);
        akm.texOffs(140, 152).addBox(-5.5F, -59.0977F, -33.0F, 11.0F, 2.0F, 4.0F, 0.0F, false);
        akm.texOffs(140, 152).addBox(-5.5F, -59.0977F, -44.0F, 11.0F, 2.0F, 4.0F, 0.0F, false);
        akm.texOffs(140, 152).addBox(-5.5F, -59.0977F, -55.0F, 11.0F, 2.0F, 4.0F, 0.0F, false);
        akm.texOffs(140, 152).addBox(-5.5F, -59.0977F, -40.0F, 11.0F, 1.0F, 7.0F, 0.0F, false);
        akm.texOffs(140, 152).addBox(-5.5F, -58.3977F, -40.0F, 11.0F, 1.0F, 7.0F, 0.0F, false);
        akm.texOffs(137, 148).addBox(-5.5F, -56.6977F, -40.0F, 11.0F, 1.0F, 7.0F, 0.0F, false);
        akm.texOffs(140, 152).addBox(-5.5F, -59.0977F, -51.0F, 11.0F, 1.0F, 7.0F, 0.0F, false);
        akm.texOffs(140, 152).addBox(-5.5F, -58.3977F, -51.0F, 11.0F, 1.0F, 7.0F, 0.0F, false);
        akm.texOffs(140, 152).addBox(-5.5F, -56.6977F, -51.0F, 11.0F, 1.0F, 7.0F, 0.0F, false);
        akm.texOffs(140, 152).addBox(-5.5F, -55.0F, -27.0F, 11.0F, 2.0F, 3.0F, 0.0F, false);
        akm.texOffs(140, 152).addBox(-5.5F, -56.0F, -40.0F, 11.0F, 1.0F, 7.0F, 0.0F, false);
        akm.texOffs(140, 152).addBox(-5.5F, -56.0F, -51.0F, 11.0F, 1.0F, 7.0F, 0.0F, false);
        akm.texOffs(140, 152).addBox(-5.5F, -57.0F, -56.0F, 11.0F, 2.0F, 1.0F, 0.0F, false);
        akm.texOffs(19, 81).addBox(-2.0F, -56.4023F, -56.7148F, 4.0F, 4.0F, 1.0F, 0.0F, false);
        akm.texOffs(83, 27).addBox(-1.5F, -55.9023F, -87.7148F, 3.0F, 3.0F, 12.0F, 0.0F, false);
        akm.texOffs(82, 22).addBox(-1.5F, -55.9023F, -69.7148F, 3.0F, 3.0F, 13.0F, 0.0F, false);
        akm.texOffs(19, 81).addBox(-2.0F, -56.4023F, -76.2148F, 4.0F, 4.0F, 7.0F, 0.0F, false);
        akm.texOffs(19, 81).addBox(-2.0F, -56.4023F, -94.6953F, 4.0F, 4.0F, 7.0F, 0.0F, false);
        akm.texOffs(19, 81).addBox(-1.5F, -64.4023F, -94.1367F, 3.0F, 6.0F, 1.0F, 0.0F, false);
        akm.texOffs(19, 81).addBox(-1.5F, -58.4023F, -94.1367F, 3.0F, 2.0F, 6.0F, 0.0F, false);
        akm.texOffs(19, 81).addBox(-1.5F, -64.0405F, -93.1888F, 3.0F, 1.0F, 3.0F, 0.0F, false);
        akm.texOffs(19, 81).addBox(-1.5F, -64.9802F, -93.5309F, 3.0F, 1.0F, 3.0F, 0.0F, false);
        akm.texOffs(19, 81).addBox(-2.7321F, -67.7122F, -93.5309F, 1.0F, 2.0F, 3.0F, 0.0F, false);
        akm.texOffs(19, 81).addBox(1.7321F, -67.7122F, -93.5309F, 1.0F, 2.0F, 3.0F, 0.0F, true);
        akm.texOffs(19, 81).addBox(-1.0F, -52.4023F, -73.2148F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        akm.texOffs(19, 81).addBox(-1.0F, -52.4023F, -76.0148F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        akm.texOffs(19, 81).addBox(-1.0F, -52.4023F, -92.4328F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        akm.texOffs(19, 81).addBox(-1.0F, -52.4023F, -91.4328F, 2.0F, 1.0F, 2.0F, 0.0F, false);
        akm.texOffs(19, 81).addBox(-1.0F, -52.4023F, -70.4148F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        akm.texOffs(78, 24).addBox(-0.5F, -52.0586F, -93.2148F, 1.0F, 1.0F, 16.0F, 0.0F, false);
        akm.texOffs(75, 18).addBox(-0.5F, -52.0586F, -77.2148F, 1.0F, 1.0F, 22.0F, 0.0F, false);
        akm.texOffs(19, 81).addBox(-2.0F, -62.0898F, -56.7148F, 4.0F, 4.0F, 1.0F, 0.0F, false);
        akm.texOffs(86, 25).addBox(-1.5F, -61.5898F, -69.7148F, 3.0F, 3.0F, 13.0F, 0.0F, false);
        akm.texOffs(140, 152).addBox(-5.5F, -59.0977F, -56.0F, 11.0F, 2.0F, 1.0F, 0.0F, false);
        akm.texOffs(140, 152).addBox(-4.5F, -60.0977F, -56.0F, 9.0F, 1.0F, 1.0F, 0.0F, false);
        akm.texOffs(140, 152).addBox(-4.0F, -61.0977F, -56.0F, 8.0F, 1.0F, 1.0F, 0.0F, false);
        akm.texOffs(140, 152).addBox(-3.0F, -62.0977F, -56.0F, 6.0F, 1.0F, 1.0F, 0.0F, false);
        akm.texOffs(140, 152).addBox(-1.5F, -63.0977F, -56.0F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        akm.texOffs(140, 152).addBox(-1.4019F, -63.1957F, -56.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        akm.texOffs(140, 152).addBox(-1.4019F, -63.1957F, -55.0F, 2.0F, 1.0F, 15.0F, 0.0F, false);
        akm.texOffs(140, 152).addBox(-1.4019F, -63.1957F, -42.0F, 2.0F, 1.0F, 13.0F, 0.0F, false);
        akm.texOffs(140, 152).addBox(0.4019F, -63.1957F, -56.0F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        akm.texOffs(140, 152).addBox(0.4019F, -63.1957F, -55.0F, 1.0F, 1.0F, 15.0F, 0.0F, true);
        akm.texOffs(140, 152).addBox(0.4019F, -63.1957F, -42.0F, 1.0F, 1.0F, 13.0F, 0.0F, true);
        akm.texOffs(140, 152).addBox(-4.5F, -55.0F, -56.0F, 9.0F, 1.0F, 1.0F, 0.0F, false);
        akm.texOffs(140, 152).addBox(-4.0F, -54.0F, -56.0F, 8.0F, 1.0F, 1.0F, 0.0F, false);
        akm.texOffs(140, 152).addBox(-3.5F, -53.2852F, -56.0F, 7.0F, 1.0F, 1.0F, 0.0F, false);
        akm.texOffs(140, 152).addBox(-2.0F, -52.2852F, -56.0F, 4.0F, 1.0F, 1.0F, 0.0F, false);
        akm.texOffs(140, 152).addBox(-1.4019F, -51.9019F, -56.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        akm.texOffs(140, 152).addBox(-1.4019F, -49.9019F, -27.0F, 2.0F, 1.0F, 3.0F, 0.0F, false);
        akm.texOffs(140, 152).addBox(-3.0F, -51.9019F, -24.99F, 6.0F, 2.0F, 1.0F, 0.0F, false);
        akm.texOffs(140, 152).addBox(-0.5981F, -49.9019F, -27.0F, 2.0F, 1.0F, 3.0F, 0.0F, true);
        akm.texOffs(140, 152).addBox(0.4019F, -51.9019F, -56.0F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        akm.texOffs(137, 148).addBox(-5.5F, -57.0F, -44.0F, 11.0F, 2.0F, 4.0F, 0.0F, false);
        akm.texOffs(140, 152).addBox(-5.5F, -57.0F, -55.0F, 11.0F, 2.0F, 4.0F, 0.0F, false);
        akm.texOffs(76, 84).addBox(-5.0F, -61.1406F, -28.0F, 10.0F, 4.0F, 10.0F, 0.0F, false);
        akm.texOffs(76, 84).addBox(-3.5F, -63.1406F, -28.0F, 7.0F, 2.0F, 10.0F, 0.0F, false);
        akm.texOffs(76, 84).addBox(1.5F, -65.1406F, -28.0F, 2.0F, 2.0F, 4.0F, 0.0F, false);
        akm.texOffs(23, 100).addBox(-2.0F, -65.3785F, -20.3797F, 4.0F, 1.0F, 2.0F, 0.0F, false);
        akm.texOffs(23, 100).addBox(1.4226F, -66.2848F, -20.3797F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        akm.texOffs(23, 100).addBox(-2.4226F, -66.2848F, -20.3797F, 1.0F, 1.0F, 2.0F, 0.0F, true);
        akm.texOffs(23, 100).addBox(1.4226F, -66.8137F, -20.3797F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        akm.texOffs(23, 100).addBox(-2.4226F, -66.8137F, -20.3797F, 1.0F, 1.0F, 2.0F, 0.0F, true);
        akm.texOffs(76, 84).addBox(-3.5F, -65.1406F, -28.0F, 2.0F, 2.0F, 4.0F, 0.0F, false);
        akm.texOffs(86, 97).addBox(-5.0F, -56.0F, -16.0F, 2.0F, 2.0F, 16.0F, 0.0F, false);
        akm.texOffs(67, 78).addBox(3.0F, -58.0F, -16.0F, 2.0F, 4.0F, 16.0F, 0.0F, true);
        akm.texOffs(67, 78).addBox(-5.0F, -54.7734F, 24.0F, 10.0F, 2.0F, 2.0F, 0.0F, false);
        akm.texOffs(86, 97).addBox(-5.0F, -54.0F, -14.0F, 10.0F, 2.0F, 2.0F, 0.0F, false);
        akm.texOffs(67, 78).addBox(3.0F, -54.0F, -12.0F, 2.0F, 4.0F, 36.0F, 0.0F, true);
        akm.texOffs(29, 93).addBox(-5.1758F, -50.1953F, 19.8789F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        akm.texOffs(29, 93).addBox(4.1758F, -50.1953F, 19.8789F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        akm.texOffs(29, 93).addBox(4.1758F, -49.7656F, 12.1094F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        akm.texOffs(29, 93).addBox(4.1758F, -51.8789F, -16.0117F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        akm.texOffs(29, 93).addBox(-5.1758F, -51.8789F, -16.0117F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        akm.texOffs(29, 93).addBox(-5.1758F, -49.7656F, 12.1094F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        akm.texOffs(0, 3).addBox(2.5313F, -43.0859F, 3.7148F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        akm.texOffs(0, 3).addBox(-3.5313F, -43.0859F, 3.7148F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        bone15 = new ModelRenderer(this);
        bone15.setPos(0.0F, -41.8125F, 17.0F);
        akm.addChild(bone15);
        setRotationAngle(bone15, 0.6981F, 0.0F, 0.0F);
        bone15.texOffs(83, 87).addBox(-2.0F, -2.5702F, -3.1521F, 4.0F, 2.0F, 4.0F, 0.0F, false);
        bone15.texOffs(83, 87).addBox(-2.0F, -8.227F, -8.809F, 4.0F, 2.0F, 2.0F, 0.0F, false);

        bone16 = new ModelRenderer(this);
        bone16.setPos(2.0F, 41.8125F, -17.0F);
        bone15.addChild(bone16);
        setRotationAngle(bone16, -0.7854F, 0.0F, 0.0F);
        bone16.texOffs(83, 87).addBox(-4.0F, -41.761F, -28.1772F, 4.0F, 2.0F, 8.0F, 0.0F, false);

        bone9 = new ModelRenderer(this);
        bone9.setPos(0.0F, -1.7109F, -1.2344F);
        akm.addChild(bone9);
        setRotationAngle(bone9, 0.1222F, 0.0F, 0.0F);
        bone9.texOffs(83, 86).addBox(-4.0F, -42.5602F, 20.6427F, 4.0F, 2.0F, 10.0F, 0.0F, false);
        bone9.texOffs(83, 86).addBox(0.0F, -42.5602F, 20.6427F, 4.0F, 2.0F, 10.0F, 0.0F, false);

        bone10 = new ModelRenderer(this);
        bone10.setPos(-0.2734F, 0.0313F, 0.5078F);
        bone9.addChild(bone10);
        setRotationAngle(bone10, 0.1745F, 0.0F, 0.0F);
        bone10.texOffs(83, 86).addBox(-3.7266F, -32.8803F, 27.8519F, 8.0F, 13.0F, 8.0F, 0.0F, false);
        bone10.texOffs(83, 86).addBox(-3.7266F, -20.466F, 29.2661F, 8.0F, 2.0F, 4.0F, 0.0F, false);
        bone10.texOffs(83, 86).addBox(-3.7266F, -20.466F, 32.4377F, 8.0F, 2.0F, 2.0F, 0.0F, false);

        bone13 = new ModelRenderer(this);
        bone13.setPos(0.2734F, 24.0224F, 18.818F);
        bone10.addChild(bone13);
        setRotationAngle(bone13, -0.7854F, 0.0F, 0.0F);
        bone13.texOffs(83, 86).addBox(-4.0F, -43.0886F, -19.9991F, 8.0F, 2.0F, 1.0F, 0.0F, false);
        bone13.texOffs(83, 86).addBox(-4.0F, -38.4317F, -24.656F, 8.0F, 1.0F, 2.0F, 0.0F, false);

        bone11 = new ModelRenderer(this);
        bone11.setPos(-0.2734F, 0.0313F, 0.5078F);
        bone9.addChild(bone11);
        setRotationAngle(bone11, -0.0873F, 0.0F, 0.0F);
        bone11.texOffs(83, 86).addBox(-3.7266F, -43.0634F, 18.4824F, 8.0F, 4.0F, 8.0F, 0.0F, false);

        bone12 = new ModelRenderer(this);
        bone12.setPos(-0.2734F, 7.1875F, -2.9375F);
        bone9.addChild(bone12);
        setRotationAngle(bone12, 0.4363F, 0.0F, 0.0F);
        bone12.texOffs(83, 86).addBox(-3.7266F, -33.3087F, 41.55F, 8.0F, 4.0F, 2.0F, 0.0F, false);

        bone3 = new ModelRenderer(this);
        bone3.setPos(0.0F, 0.0F, 0.0F);
        akm.addChild(bone3);
        setRotationAngle(bone3, -0.0873F, 0.0F, 0.0F);
        bone3.texOffs(86, 97).addBox(-5.001F, -52.6889F, -28.2215F, 10.0F, 4.0F, 9.0F, 0.0F, false);
        bone3.texOffs(86, 97).addBox(-4.99F, -52.6889F, -19.2215F, 2.0F, 4.0F, 9.0F, 0.0F, false);
        bone3.texOffs(67, 78).addBox(2.999F, -52.6889F, -19.2215F, 2.0F, 4.0F, 9.0F, 0.0F, false);
        bone3.texOffs(67, 78).addBox(-5.001F, -52.6889F, -10.2215F, 10.0F, 4.0F, 16.0F, 0.0F, false);

        dust_cover = new ModelRenderer(this);
        dust_cover.setPos(20.0F, -31.0F, -4.0F);
        akm.addChild(dust_cover);
        dust_cover.texOffs(30, 102).addBox(-26.0F, -22.0F, 22.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        dust_cover.texOffs(30, 102).addBox(-26.1563F, -22.0F, 20.6172F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        dust_cover.texOffs(30, 102).addBox(-26.0F, -23.4142F, 18.5858F, 1.0F, 2.0F, 4.0F, 0.0F, false);
        dust_cover.texOffs(30, 102).addBox(-26.0F, -25.5355F, 6.4645F, 1.0F, 3.0F, 10.0F, 0.0F, false);
        dust_cover.texOffs(30, 102).addBox(-26.3633F, -25.7777F, 5.4645F, 2.0F, 1.0F, 3.0F, 0.0F, false);
        dust_cover.texOffs(30, 102).addBox(-26.0F, -20.5858F, 20.5858F, 1.0F, 2.0F, 2.0F, 0.0F, false);

        bone29 = new ModelRenderer(this);
        bone29.setPos(0.0F, 0.0F, 0.0F);
        dust_cover.addChild(bone29);
        setRotationAngle(bone29, -0.2618F, 0.0F, 0.0F);
        bone29.texOffs(30, 102).addBox(-26.0F, -26.2805F, 0.074F, 1.0F, 3.0F, 15.0F, 0.0F, false);

        bone28 = new ModelRenderer(this);
        bone28.setPos(-25.0F, -21.0F, 23.0F);
        dust_cover.addChild(bone28);
        setRotationAngle(bone28, -0.7854F, 0.0F, 0.0F);
        bone28.texOffs(30, 102).addBox(-1.0F, -1.4142F, -2.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        bone28.texOffs(30, 102).addBox(-1.0F, 1.4142F, -7.8284F, 1.0F, 3.0F, 5.0F, 0.0F, false);
        bone28.texOffs(30, 102).addBox(-1.0F, 0.0F, -0.5858F, 1.0F, 2.0F, 2.0F, 0.0F, false);

        bone17 = new ModelRenderer(this);
        bone17.setPos(0.0F, -40.9837F, 3.0215F);
        akm.addChild(bone17);
        setRotationAngle(bone17, -0.192F, 0.0F, 0.0F);
        bone17.texOffs(0, 3).addBox(-2.0F, -3.0F, -1.0F, 4.0F, 6.0F, 2.0F, 0.0F, true);

        bone22 = new ModelRenderer(this);
        bone22.setPos(4.0F, -66.0F, 13.0F);
        akm.addChild(bone22);
        setRotationAngle(bone22, 0.0F, 0.0F, 1.2217F);
        bone22.texOffs(38, 77).addBox(1.7628F, 6.2208F, -31.0F, 2.0F, 2.0F, 40.0F, 0.0F, true);
        bone22.texOffs(67, 78).addBox(1.293F, 6.0498F, 3.0F, 2.0F, 2.0F, 2.0F, 0.0F, true);
        bone22.texOffs(67, 78).addBox(1.293F, 6.0498F, -12.0F, 2.0F, 2.0F, 2.0F, 0.0F, true);
        bone22.texOffs(67, 78).addBox(1.293F, 6.0498F, -27.0F, 2.0F, 2.0F, 2.0F, 0.0F, true);
        bone22.texOffs(22, 98).addBox(1.4839F, 6.1193F, -42.0F, 1.0F, 2.0F, 1.0F, 0.0F, true);

        bone21 = new ModelRenderer(this);
        bone21.setPos(-4.0F, -66.0F, 13.0F);
        akm.addChild(bone21);
        setRotationAngle(bone21, 0.0F, 0.0F, -1.2217F);
        bone21.texOffs(38, 77).addBox(-3.7628F, 6.2208F, -31.0F, 2.0F, 2.0F, 40.0F, 0.0F, false);
        bone21.texOffs(67, 78).addBox(-3.293F, 6.0498F, 3.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        bone21.texOffs(67, 78).addBox(-3.293F, 6.0498F, -12.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        bone21.texOffs(67, 78).addBox(-3.293F, 6.0498F, -27.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        bone21.texOffs(22, 98).addBox(-2.4839F, 6.1193F, -42.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        bone46 = new ModelRenderer(this);
        bone46.setPos(0.0F, -58.4023F, -88.1367F);
        akm.addChild(bone46);
        setRotationAngle(bone46, 0.3491F, 0.0F, 0.0F);
        bone46.texOffs(19, 81).addBox(-1.5F, -7.0F, -1.0F, 3.0F, 7.0F, 1.0F, 0.0F, false);

        bone48 = new ModelRenderer(this);
        bone48.setPos(3.5F, -64.4802F, -92.0309F);
        akm.addChild(bone48);
        setRotationAngle(bone48, 0.0F, 0.0F, 0.5236F);
        bone48.texOffs(19, 81).addBox(-5.1471F, 1.549F, -1.5F, 2.0F, 1.0F, 3.0F, 0.0F, true);
        bone48.texOffs(19, 81).addBox(-2.4151F, -1.183F, -1.5F, 1.0F, 2.0F, 3.0F, 0.0F, true);
        bone48.texOffs(19, 81).addBox(-6.8792F, -1.183F, -1.5F, 1.0F, 2.0F, 3.0F, 0.0F, true);

        bone47 = new ModelRenderer(this);
        bone47.setPos(-3.5F, -64.4802F, -92.0309F);
        akm.addChild(bone47);
        setRotationAngle(bone47, 0.0F, 0.0F, -0.5236F);
        bone47.texOffs(19, 81).addBox(3.1471F, 1.549F, -1.5F, 2.0F, 1.0F, 3.0F, 0.0F, false);
        bone47.texOffs(19, 81).addBox(1.4151F, -1.183F, -1.5F, 1.0F, 2.0F, 3.0F, 0.0F, false);
        bone47.texOffs(19, 81).addBox(5.8792F, -1.183F, -1.5F, 1.0F, 2.0F, 3.0F, 0.0F, false);

        bone45 = new ModelRenderer(this);
        bone45.setPos(0.0F, -61.5898F, -69.7148F);
        akm.addChild(bone45);
        setRotationAngle(bone45, 0.8203F, 0.0F, 0.0F);
        bone45.texOffs(93, 33).addBox(-1.501F, 0.0F, -8.0F, 3.0F, 3.0F, 8.0F, 0.0F, false);

        bone43 = new ModelRenderer(this);
        bone43.setPos(-3.5F, -49.5F, -55.5F);
        akm.addChild(bone43);
        setRotationAngle(bone43, 0.0F, 0.0F, -0.5236F);
        bone43.texOffs(140, 152).addBox(1.0179F, -5.7631F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone43.texOffs(140, 152).addBox(0.0179F, -4.0311F, 28.5F, 1.0F, 3.0F, 3.0F, 0.0F, false);
        bone43.texOffs(140, 152).addBox(11.5931F, -6.8118F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        bone43.texOffs(140, 152).addBox(5.6649F, -10.8118F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        bone43.texOffs(140, 152).addBox(11.5931F, -6.8118F, 0.5F, 1.0F, 3.0F, 13.0F, 0.0F, false);
        bone43.texOffs(140, 152).addBox(5.6649F, -10.8118F, 0.5F, 3.0F, 1.0F, 13.0F, 0.0F, false);
        bone43.texOffs(140, 152).addBox(11.5931F, -6.8118F, 13.5F, 1.0F, 3.0F, 13.0F, 0.0F, false);
        bone43.texOffs(140, 152).addBox(5.6649F, -10.8118F, 13.5F, 3.0F, 1.0F, 13.0F, 0.0F, false);
        bone43.texOffs(140, 152).addBox(4.9462F, 0.2369F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        bone43.texOffs(140, 152).addBox(3.9462F, 1.9689F, 28.5F, 3.0F, 1.0F, 3.0F, 0.0F, false);

        bone44 = new ModelRenderer(this);
        bone44.setPos(3.5F, -49.5F, -55.5F);
        akm.addChild(bone44);
        setRotationAngle(bone44, 0.0F, 0.0F, 0.5236F);
        bone44.texOffs(140, 152).addBox(-2.0179F, -5.7631F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);
        bone44.texOffs(140, 152).addBox(-1.0179F, -4.0311F, 28.5F, 1.0F, 3.0F, 3.0F, 0.0F, true);
        bone44.texOffs(140, 152).addBox(-12.5931F, -6.8118F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, true);
        bone44.texOffs(140, 152).addBox(-8.6649F, -10.8118F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, true);
        bone44.texOffs(140, 152).addBox(-12.5931F, -6.8118F, 0.5F, 1.0F, 3.0F, 14.0F, 0.0F, true);
        bone44.texOffs(140, 152).addBox(-8.6649F, -10.8118F, 0.5F, 3.0F, 1.0F, 14.0F, 0.0F, true);
        bone44.texOffs(140, 152).addBox(-12.5931F, -6.8118F, 14.5F, 1.0F, 3.0F, 12.0F, 0.0F, true);
        bone44.texOffs(140, 152).addBox(-8.6649F, -10.8118F, 14.5F, 3.0F, 1.0F, 12.0F, 0.0F, true);
        bone44.texOffs(140, 152).addBox(-7.9462F, 0.2369F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, true);
        bone44.texOffs(140, 152).addBox(-6.9462F, 1.9689F, 28.5F, 3.0F, 1.0F, 3.0F, 0.0F, true);

        bone39 = new ModelRenderer(this);
        bone39.setPos(0.0F, -47.5F, -50.5F);
        akm.addChild(bone39);
        setRotationAngle(bone39, -0.0698F, 0.0F, 0.0F);
        bone39.texOffs(140, 152).addBox(-5.5F, -8.1678F, -5.0122F, 11.0F, 1.0F, 5.0F, 0.0F, false);
        bone39.texOffs(140, 151).addBox(-1.4019F, -4.0698F, -5.0122F, 2.0F, 1.0F, 16.0F, 0.0F, false);
        bone39.texOffs(140, 152).addBox(-1.4019F, -4.0698F, 10.9878F, 2.0F, 1.0F, 14.0F, 0.0F, false);
        bone39.texOffs(140, 151).addBox(0.4019F, -4.0698F, -5.0122F, 1.0F, 1.0F, 16.0F, 0.0F, true);
        bone39.texOffs(140, 152).addBox(0.4019F, -4.0698F, 10.9878F, 1.0F, 1.0F, 14.0F, 0.0F, true);
        bone39.texOffs(140, 152).addBox(-5.5F, -8.1678F, -0.0122F, 11.0F, 1.0F, 5.0F, 0.0F, false);
        bone39.texOffs(140, 152).addBox(-5.5F, -9.1678F, 4.9878F, 11.0F, 2.0F, 5.0F, 0.0F, false);
        bone39.texOffs(137, 148).addBox(-5.5F, -9.1678F, 9.9878F, 11.0F, 2.0F, 5.0F, 0.0F, false);
        bone39.texOffs(140, 152).addBox(-5.5F, -9.1678F, 14.9878F, 11.0F, 2.0F, 5.0F, 0.0F, false);
        bone39.texOffs(140, 152).addBox(-5.5F, -9.1678F, 19.9878F, 11.0F, 2.0F, 4.0F, 0.0F, false);

        bone40 = new ModelRenderer(this);
        bone40.setPos(-5.0F, -2.1703F, 12.5575F);
        bone39.addChild(bone40);
        setRotationAngle(bone40, 0.0F, 0.0F, -0.5236F);
        bone40.texOffs(140, 152).addBox(2.0658F, -4.578F, -17.5698F, 1.0F, 3.0F, 15.0F, 0.0F, false);
        bone40.texOffs(140, 152).addBox(5.994F, 1.422F, -17.5698F, 3.0F, 1.0F, 15.0F, 0.0F, false);
        bone40.texOffs(140, 152).addBox(2.0658F, -4.578F, -2.5698F, 1.0F, 3.0F, 15.0F, 0.0F, false);
        bone40.texOffs(140, 152).addBox(5.994F, 1.422F, -2.5698F, 3.0F, 1.0F, 15.0F, 0.0F, false);

        bone41 = new ModelRenderer(this);
        bone41.setPos(5.0F, -2.1703F, 12.5575F);
        bone39.addChild(bone41);
        setRotationAngle(bone41, 0.0F, 0.0F, 0.5236F);
        bone41.texOffs(140, 152).addBox(-3.0658F, -4.578F, -17.5698F, 1.0F, 3.0F, 15.0F, 0.0F, true);
        bone41.texOffs(140, 152).addBox(-3.0658F, -4.578F, -2.5698F, 1.0F, 3.0F, 15.0F, 0.0F, true);

        bone42 = new ModelRenderer(this);
        bone42.setPos(5.0F, -2.1703F, 12.5575F);
        bone39.addChild(bone42);
        setRotationAngle(bone42, 0.0F, 0.0F, 0.5236F);
        bone42.texOffs(140, 152).addBox(-8.994F, 1.422F, -17.5698F, 3.0F, 1.0F, 15.0F, 0.0F, true);
        bone42.texOffs(140, 152).addBox(-8.994F, 1.422F, -2.5698F, 3.0F, 1.0F, 15.0F, 0.0F, true);

        bone36 = new ModelRenderer(this);
        bone36.setPos(0.9226F, -66.7848F, -19.3797F);
        akm.addChild(bone36);
        setRotationAngle(bone36, 0.0F, 0.0F, 0.5236F);
        bone36.texOffs(23, 100).addBox(0.5686F, -0.0152F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        bone36.texOffs(23, 100).addBox(-2.0841F, 1.8127F, -1.0F, 2.0F, 1.0F, 2.0F, 0.0F, false);

        bone37 = new ModelRenderer(this);
        bone37.setPos(-0.9226F, -66.7848F, -19.3797F);
        akm.addChild(bone37);
        setRotationAngle(bone37, 0.0F, 0.0F, -0.5236F);
        bone37.texOffs(23, 100).addBox(-1.5686F, -0.0152F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);
        bone37.texOffs(23, 100).addBox(0.0841F, 1.8127F, -1.0F, 2.0F, 1.0F, 2.0F, 0.0F, true);

        bone34 = new ModelRenderer(this);
        bone34.setPos(0.0F, -68.3785F, -19.3797F);
        akm.addChild(bone34);
        setRotationAngle(bone34, 0.0F, 0.0F, 0.4363F);
        bone34.texOffs(23, 100).addBox(2.5031F, 1.78F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        bone35 = new ModelRenderer(this);
        bone35.setPos(0.0F, -68.3785F, -19.3797F);
        akm.addChild(bone35);
        setRotationAngle(bone35, 0.0F, 0.0F, -0.4363F);
        bone35.texOffs(23, 100).addBox(-3.5031F, 1.78F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, true);

        bone33 = new ModelRenderer(this);
        bone33.setPos(0.0F, -64.6406F, -23.5F);
        akm.addChild(bone33);
        setRotationAngle(bone33, 0.1047F, 0.0F, 0.0F);
        bone33.texOffs(23, 100).addBox(-1.5F, -0.3086F, -2.9297F, 3.0F, 1.0F, 7.0F, 0.0F, false);
        bone33.texOffs(23, 100).addBox(2.6836F, -0.3086F, -2.9297F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone33.texOffs(23, 100).addBox(-3.6836F, -0.3086F, -2.9297F, 1.0F, 1.0F, 1.0F, 0.0F, true);

        bone14 = new ModelRenderer(this);
        bone14.setPos(0.0F, -64.1406F, -22.0F);
        akm.addChild(bone14);
        setRotationAngle(bone14, -0.5236F, 0.0F, 0.0F);
        bone14.texOffs(76, 84).addBox(1.5F, 0.134F, -2.2321F, 2.0F, 2.0F, 4.0F, 0.0F, false);
        bone14.texOffs(76, 84).addBox(-3.5F, 0.134F, -2.2321F, 2.0F, 2.0F, 4.0F, 0.0F, false);

        bone18 = new ModelRenderer(this);
        bone18.setPos(-4.0F, -64.0F, 13.0F);
        akm.addChild(bone18);
        setRotationAngle(bone18, 0.0F, 0.0F, 0.4887F);
        bone18.texOffs(70, 82).addBox(1.9339F, 1.7672F, -13.0F, 2.0F, 4.0F, 22.0F, 0.0F, false);
        bone18.texOffs(67, 78).addBox(1.6991F, 1.3257F, 3.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);
        bone18.texOffs(86, 97).addBox(1.6991F, 1.3257F, -12.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);
        bone18.texOffs(67, 78).addBox(1.6991F, 1.3257F, -27.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        bone18.texOffs(67, 78).addBox(1.9339F, 3.7672F, 9.0F, 4.0F, 2.0F, 2.0F, 0.0F, false);
        bone18.texOffs(86, 97).addBox(1.9339F, 1.7672F, -31.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);
        bone18.texOffs(22, 98).addBox(1.7945F, 1.505F, -42.0F, 5.0F, 4.0F, 1.0F, 0.0F, false);
        bone18.texOffs(86, 97).addBox(1.9339F, 1.7672F, -29.0F, 2.0F, 2.0F, 16.0F, 0.0F, false);

        bone20 = new ModelRenderer(this);
        bone20.setPos(4.0F, -64.0F, 13.0F);
        akm.addChild(bone20);
        setRotationAngle(bone20, 0.0F, 0.0F, -0.4887F);
        bone20.texOffs(67, 78).addBox(-3.9339F, 1.7672F, -13.0F, 2.0F, 4.0F, 22.0F, 0.0F, true);
        bone20.texOffs(67, 78).addBox(-3.6991F, 1.3257F, 3.0F, 2.0F, 4.0F, 2.0F, 0.0F, true);
        bone20.texOffs(67, 78).addBox(-3.6991F, 1.3257F, -12.0F, 2.0F, 4.0F, 2.0F, 0.0F, true);
        bone20.texOffs(67, 78).addBox(-3.6991F, 1.3257F, -27.0F, 2.0F, 4.0F, 2.0F, 0.0F, true);
        bone20.texOffs(89, 88).addBox(-5.9339F, 3.7672F, 9.0F, 4.0F, 2.0F, 2.0F, 0.0F, true);
        bone20.texOffs(86, 97).addBox(-3.9339F, 1.7672F, -31.0F, 2.0F, 4.0F, 2.0F, 0.0F, true);
        bone20.texOffs(22, 98).addBox(-6.7945F, 1.505F, -42.0F, 5.0F, 4.0F, 1.0F, 0.0F, true);
        bone20.texOffs(67, 78).addBox(-3.9339F, 1.7672F, -29.0F, 2.0F, 4.0F, 16.0F, 0.0F, true);

        stock = new ModelRenderer(this);
        stock.setPos(1.0F, 0.5F, 2.6719F);
        akm.addChild(stock);
        setRotationAngle(stock, -0.1222F, 0.0F, 0.0F);
        stock.texOffs(146, 170).addBox(-3.0F, -59.7173F, 16.4474F, 4.0F, 1.0F, 10.0F, 0.0F, false);
        stock.texOffs(39, 98).addBox(-1.5F, -59.936F, 20.924F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        stock.texOffs(39, 98).addBox(-1.5F, -59.936F, 17.8732F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        stock.texOffs(150, 151).addBox(0.6F, -60.1977F, 16.4474F, 1.0F, 1.0F, 9.0F, 0.0F, false);
        stock.texOffs(0, 3).addBox(-3.0F, -59.9455F, 22.6921F, 4.0F, 1.0F, 1.0F, 0.0F, false);
        stock.texOffs(146, 151).addBox(1.3071F, -59.4906F, 16.4474F, 1.0F, 5.0F, 14.0F, 0.0F, false);
        stock.texOffs(146, 151).addBox(1.3071F, -59.4906F, 30.4474F, 1.0F, 5.0F, 14.0F, 0.0F, false);
        stock.texOffs(146, 154).addBox(-4.0F, -61.3383F, 58.7052F, 6.0F, 14.0F, 1.0F, 0.0F, false);
        stock.texOffs(146, 154).addBox(-4.3071F, -59.5414F, 44.4474F, 1.0F, 12.0F, 15.0F, 0.0F, true);
        stock.texOffs(146, 154).addBox(1.3071F, -59.5414F, 44.4474F, 1.0F, 12.0F, 15.0F, 0.0F, false);
        stock.texOffs(146, 151).addBox(-4.3071F, -59.4906F, 16.4474F, 1.0F, 5.0F, 14.0F, 0.0F, true);
        stock.texOffs(146, 151).addBox(-4.3071F, -59.4906F, 30.4474F, 1.0F, 5.0F, 14.0F, 0.0F, true);
        stock.texOffs(146, 152).addBox(-4.3071F, -54.4906F, 26.4474F, 1.0F, 2.0F, 18.0F, 0.0F, true);
        stock.texOffs(146, 152).addBox(1.3071F, -54.4906F, 26.4474F, 1.0F, 2.0F, 18.0F, 0.0F, false);
        stock.texOffs(153, 150).addBox(-4.3071F, -52.4906F, 34.4474F, 1.0F, 3.0F, 10.0F, 0.0F, true);
        stock.texOffs(153, 150).addBox(1.3071F, -52.4906F, 34.4474F, 1.0F, 3.0F, 10.0F, 0.0F, false);
        stock.texOffs(146, 152).addBox(-4.3071F, -61.4906F, 38.4474F, 1.0F, 2.0F, 11.0F, 0.0F, true);
        stock.texOffs(146, 153).addBox(-4.3071F, -61.4906F, 49.4474F, 1.0F, 2.0F, 10.0F, 0.0F, true);
        stock.texOffs(146, 152).addBox(-4.3071F, -61.4906F, 27.4474F, 1.0F, 2.0F, 11.0F, 0.0F, true);
        stock.texOffs(146, 152).addBox(1.3071F, -61.4906F, 38.4474F, 1.0F, 2.0F, 11.0F, 0.0F, false);
        stock.texOffs(146, 153).addBox(1.3071F, -61.4906F, 49.4474F, 1.0F, 2.0F, 10.0F, 0.0F, false);
        stock.texOffs(155, 145).addBox(-1.4F, -62.1977F, 38.4474F, 3.0F, 1.0F, 11.0F, 0.0F, false);
        stock.texOffs(155, 145).addBox(-1.4F, -62.1977F, 49.4474F, 3.0F, 1.0F, 10.0F, 0.0F, false);
        stock.texOffs(155, 145).addBox(-1.4F, -62.1977F, 27.4474F, 3.0F, 1.0F, 11.0F, 0.0F, false);
        stock.texOffs(155, 145).addBox(-3.6F, -62.1977F, 38.4474F, 3.0F, 1.0F, 11.0F, 0.0F, true);
        stock.texOffs(155, 145).addBox(-3.6F, -62.1977F, 49.4474F, 3.0F, 1.0F, 10.0F, 0.0F, true);
        stock.texOffs(155, 145).addBox(-3.6F, -62.1977F, 27.4474F, 3.0F, 1.0F, 11.0F, 0.0F, true);
        stock.texOffs(146, 152).addBox(1.3071F, -61.4906F, 27.4474F, 1.0F, 2.0F, 11.0F, 0.0F, false);
        stock.texOffs(150, 151).addBox(-3.6F, -60.1977F, 16.4474F, 1.0F, 1.0F, 9.0F, 0.0F, true);

        bone32 = new ModelRenderer(this);
        bone32.setPos(-0.6929F, -63.5656F, 32.054F);
        stock.addChild(bone32);
        setRotationAngle(bone32, 0.3142F, 0.0F, 0.0F);
        bone32.texOffs(150, 151).addBox(0.0F, 0.5499F, -10.0223F, 3.0F, 2.0F, 5.0F, 0.0F, true);
        bone32.texOffs(150, 151).addBox(2.0F, 0.5499F, -12.0223F, 1.0F, 1.0F, 2.0F, 0.0F, true);
        bone32.texOffs(150, 151).addBox(-3.6142F, 0.5499F, -12.0223F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        bone32.texOffs(150, 151).addBox(-3.6142F, 0.5499F, -10.0223F, 4.0F, 2.0F, 5.0F, 0.0F, false);

        bone30 = new ModelRenderer(this);
        bone30.setPos(-3.5F, -53.9906F, 23.4474F);
        stock.addChild(bone30);
        setRotationAngle(bone30, -0.2443F, 0.0F, 0.0F);
        bone30.texOffs(146, 141).addBox(-0.807F, -1.3699F, -7.3882F, 1.0F, 3.0F, 15.0F, 0.0F, true);
        bone30.texOffs(146, 141).addBox(4.807F, -1.3699F, -7.3882F, 1.0F, 3.0F, 15.0F, 0.0F, false);
        bone30.texOffs(146, 141).addBox(-0.807F, -1.3699F, 7.6118F, 1.0F, 3.0F, 15.0F, 0.0F, true);
        bone30.texOffs(146, 141).addBox(4.807F, -1.3699F, 7.6118F, 1.0F, 3.0F, 15.0F, 0.0F, false);
        bone30.texOffs(146, 141).addBox(-0.807F, -2.8308F, 31.3813F, 1.0F, 2.0F, 5.0F, 0.0F, true);
        bone30.texOffs(146, 141).addBox(-0.807F, -1.3699F, 22.6118F, 1.0F, 3.0F, 14.0F, 0.0F, true);
        bone30.texOffs(146, 153).addBox(-0.5F, -2.3387F, 35.7493F, 6.0F, 4.0F, 1.0F, 0.0F, true);
        bone30.texOffs(146, 141).addBox(4.807F, -2.8308F, 31.3813F, 1.0F, 2.0F, 5.0F, 0.0F, false);
        bone30.texOffs(146, 141).addBox(4.807F, -1.3699F, 22.6118F, 1.0F, 3.0F, 14.0F, 0.0F, false);
        bone30.texOffs(150, 138).addBox(-0.1F, 1.3372F, 13.6118F, 3.0F, 1.0F, 12.0F, 0.0F, true);
        bone30.texOffs(150, 139).addBox(-0.1F, 1.3372F, 25.6118F, 3.0F, 1.0F, 11.0F, 0.0F, true);
        bone30.texOffs(150, 138).addBox(-0.1F, 1.3372F, 1.6118F, 3.0F, 1.0F, 12.0F, 0.0F, true);
        bone30.texOffs(150, 138).addBox(-0.1F, 1.3372F, -7.3882F, 3.0F, 1.0F, 9.0F, 0.0F, true);
        bone30.texOffs(150, 138).addBox(2.1F, 1.3372F, 13.6118F, 3.0F, 1.0F, 12.0F, 0.0F, false);
        bone30.texOffs(150, 139).addBox(2.1F, 1.3372F, 25.6118F, 3.0F, 1.0F, 11.0F, 0.0F, false);
        bone30.texOffs(150, 138).addBox(2.1F, 1.3372F, 1.6118F, 3.0F, 1.0F, 12.0F, 0.0F, false);
        bone30.texOffs(150, 138).addBox(2.1F, 1.3372F, -7.3882F, 3.0F, 1.0F, 9.0F, 0.0F, false);

        bone31 = new ModelRenderer(this);
        bone31.setPos(-0.3071F, 2.1301F, 22.6118F);
        bone30.addChild(bone31);
        setRotationAngle(bone31, 0.0F, 0.0F, -0.7854F);
        bone31.texOffs(150, 138).addBox(0.0F, -0.7071F, -12.0F, 1.0F, 1.0F, 15.0F, 0.0F, true);
        bone31.texOffs(150, 139).addBox(0.0F, -0.7071F, 3.0F, 1.0F, 1.0F, 11.0F, 0.0F, true);
        bone31.texOffs(150, 138).addBox(3.677F, 2.9698F, -12.0F, 1.0F, 1.0F, 15.0F, 0.0F, true);
        bone31.texOffs(150, 139).addBox(3.677F, 2.9698F, 3.0F, 1.0F, 1.0F, 11.0F, 0.0F, true);
        bone31.texOffs(150, 138).addBox(0.0F, -0.7071F, -30.0F, 1.0F, 1.0F, 18.0F, 0.0F, true);
        bone31.texOffs(150, 138).addBox(3.677F, 2.9698F, -30.0F, 1.0F, 1.0F, 18.0F, 0.0F, true);

        bone27 = new ModelRenderer(this);
        bone27.setPos(7.0F, -59.6977F, 23.4474F);
        stock.addChild(bone27);
        setRotationAngle(bone27, 0.0F, 0.0F, 0.7854F);
        bone27.texOffs(150, 151).addBox(-4.1719F, 3.4648F, -7.0F, 1.0F, 1.0F, 9.0F, 0.0F, false);
        bone27.texOffs(146, 152).addBox(-5.5861F, 2.0506F, 4.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone27.texOffs(146, 152).addBox(-9.2631F, 5.7276F, 4.0F, 1.0F, 1.0F, 11.0F, 0.0F, true);
        bone27.texOffs(146, 152).addBox(-5.5861F, 2.0506F, 15.0F, 1.0F, 1.0F, 11.0F, 0.0F, false);
        bone27.texOffs(146, 152).addBox(-5.5861F, 2.0506F, 26.0F, 1.0F, 1.0F, 10.0F, 0.0F, false);
        bone27.texOffs(146, 152).addBox(-9.2631F, 5.7276F, 15.0F, 1.0F, 1.0F, 11.0F, 0.0F, true);
        bone27.texOffs(146, 152).addBox(-9.2631F, 5.7276F, 26.0F, 1.0F, 1.0F, 10.0F, 0.0F, true);
        bone27.texOffs(155, 151).addBox(-7.8489F, 7.1418F, -7.0F, 1.0F, 1.0F, 9.0F, 0.0F, false);

        bone38 = new ModelRenderer(this);
        bone38.setPos(1.0F, 0.0F, 0.0F);
        akm.addChild(bone38);
        setRotationAngle(bone38, -0.1745F, 0.0F, 0.0F);
        bone38.texOffs(86, 97).addBox(-6.001F, -61.0766F, 14.4474F, 10.0F, 8.0F, 2.0F, 0.0F, false);

        bone25 = new ModelRenderer(this);
        bone25.setPos(0.0F, -57.0766F, 15.4474F);
        akm.addChild(bone25);
        setRotationAngle(bone25, 0.7854F, 0.0F, 0.0F);
        bone25.texOffs(82, 86).addBox(-5.001F, 5.8771F, 6.1827F, 10.0F, 2.0F, 2.0F, 0.0F, false);
        bone25.texOffs(89, 88).addBox(-4.0F, 3.8771F, 6.1827F, 8.0F, 2.0F, 2.0F, 0.0F, false);
        bone25.texOffs(89, 88).addBox(-2.0F, 1.3693F, 6.4561F, 4.0F, 6.0F, 2.0F, 0.0F, false);
        bone25.texOffs(28, 108).addBox(-1.0F, 2.6662F, 6.5811F, 2.0F, 3.0F, 2.0F, 0.0F, false);
        bone25.texOffs(89, 88).addBox(-3.0F, 1.4709F, 5.8155F, 6.0F, 4.0F, 2.0F, 0.0F, false);

        bone2 = new ModelRenderer(this);
        bone2.setPos(0.0F, -3.0F, 32.0F);
        akm.addChild(bone2);
        setRotationAngle(bone2, 0.7854F, 0.0F, 0.0F);
        bone2.texOffs(86, 97).addBox(-5.001F, -39.4767F, 26.163F, 10.0F, 2.0F, 2.0F, 0.0F, false);

        magazine = new ModelRenderer(this);
        magazine.setPos(-41.0F, 26.0F, -0.4336F);
        setRotationAngle(magazine, -0.0698F, 0.0F, 0.0F);


        bone4 = new ModelRenderer(this);
        bone4.setPos(0.0F, 0.0F, -0.4336F);
        magazine.addChild(bone4);
        bone4.texOffs(86, 38).addBox(38.001F, -42.4831F, -8.9147F, 6.0F, 2.0F, 8.0F, 0.0F, false);
        bone4.texOffs(86, 38).addBox(38.001F, -42.4831F, -12.9147F, 6.0F, 2.0F, 4.0F, 0.0F, false);
        bone4.texOffs(86, 38).addBox(38.0F, -50.4831F, -8.9147F, 6.0F, 2.0F, 8.0F, 0.0F, false);
        bone4.texOffs(86, 38).addBox(38.0F, -50.4831F, -12.9147F, 6.0F, 2.0F, 4.0F, 0.0F, false);
        bone4.texOffs(86, 38).addBox(38.0F, -48.4831F, -12.9147F, 6.0F, 6.0F, 4.0F, 0.0F, false);
        bone4.texOffs(86, 38).addBox(41.8F, -48.4831F, -8.9147F, 2.0F, 6.0F, 6.0F, 0.0F, false);
        bone4.texOffs(86, 38).addBox(38.2F, -48.4831F, -8.9147F, 2.0F, 6.0F, 6.0F, 0.0F, true);
        bone4.texOffs(86, 38).addBox(39.001F, -50.4831F, -16.9147F, 4.0F, 10.0F, 4.0F, 0.0F, false);
        bone4.texOffs(86, 38).addBox(40.0F, -53.4831F, -16.9147F, 2.0F, 3.0F, 1.0F, 0.0F, false);
        bone4.texOffs(86, 38).addBox(39.95F, -52.4831F, -20.9147F, 2.0F, 2.0F, 4.0F, 0.0F, false);
        bone4.texOffs(86, 38).addBox(39.0F, -54.4831F, -16.9147F, 1.0F, 4.0F, 4.0F, 0.0F, false);
        bone4.texOffs(86, 38).addBox(42.0F, -54.4831F, -16.9147F, 1.0F, 4.0F, 4.0F, 0.0F, false);
        bone4.texOffs(86, 38).addBox(43.0F, -54.4831F, -12.9147F, 1.0F, 4.0F, 11.0F, 0.0F, false);
        bone4.texOffs(86, 38).addBox(38.0F, -54.4831F, -1.9147F, 6.0F, 4.0F, 1.0F, 0.0F, false);
        bone4.texOffs(86, 38).addBox(38.0F, -54.4831F, -12.9147F, 1.0F, 4.0F, 11.0F, 0.0F, false);
        bone4.texOffs(86, 38).addBox(38.0F, -48.4831F, -2.9147F, 6.0F, 6.0F, 2.0F, 0.0F, false);

        bone = new ModelRenderer(this);
        bone.setPos(41.1F, -51.2219F, -19.1358F);
        bone4.addChild(bone);
        setRotationAngle(bone, -0.0873F, 0.0F, 0.0F);
        bone.texOffs(86, 38).addBox(-1.05F, 0.0F, -1.5F, 2.0F, 1.0F, 4.0F, 0.0F, false);

        bone5 = new ModelRenderer(this);
        bone5.setPos(0.0F, 0.0F, -0.4336F);
        magazine.addChild(bone5);
        setRotationAngle(bone5, -0.2618F, 0.0F, 0.0F);
        bone5.texOffs(86, 38).addBox(38.0F, -30.7547F, -19.3466F, 6.0F, 2.0F, 8.0F, 0.0F, false);
        bone5.texOffs(86, 38).addBox(38.0F, -30.7547F, -23.3466F, 6.0F, 2.0F, 4.0F, 0.0F, false);
        bone5.texOffs(86, 38).addBox(38.0F, -38.7547F, -19.3466F, 6.0F, 2.0F, 8.0F, 0.0F, false);
        bone5.texOffs(86, 38).addBox(38.0F, -38.7547F, -23.3466F, 6.0F, 8.0F, 4.0F, 0.0F, false);
        bone5.texOffs(86, 38).addBox(41.8F, -36.7547F, -19.3466F, 2.0F, 6.0F, 6.0F, 0.0F, false);
        bone5.texOffs(86, 38).addBox(38.2F, -36.7547F, -19.3466F, 2.0F, 6.0F, 6.0F, 0.0F, true);
        bone5.texOffs(86, 38).addBox(39.0F, -38.7547F, -27.3466F, 4.0F, 10.0F, 4.0F, 0.0F, false);
        bone5.texOffs(86, 38).addBox(38.0F, -36.7547F, -13.3466F, 6.0F, 6.0F, 2.0F, 0.0F, false);

        bone6 = new ModelRenderer(this);
        bone6.setPos(0.0F, 0.0F, -0.4336F);
        magazine.addChild(bone6);
        setRotationAngle(bone6, -0.5236F, 0.0F, 0.0F);
        bone6.texOffs(86, 38).addBox(38.001F, -16.726F, -26.3874F, 6.0F, 2.0F, 8.0F, 0.0F, false);
        bone6.texOffs(86, 38).addBox(38.001F, -24.726F, -26.3874F, 6.0F, 2.0F, 8.0F, 0.0F, false);
        bone6.texOffs(86, 38).addBox(38.001F, -24.726F, -30.3874F, 6.0F, 10.0F, 4.0F, 0.0F, false);
        bone6.texOffs(86, 38).addBox(41.8F, -22.726F, -26.3874F, 2.0F, 6.0F, 6.0F, 0.0F, false);
        bone6.texOffs(86, 38).addBox(38.2F, -22.726F, -26.3874F, 2.0F, 6.0F, 6.0F, 0.0F, true);
        bone6.texOffs(86, 38).addBox(39.001F, -24.726F, -34.3874F, 4.0F, 10.0F, 4.0F, 0.0F, false);
        bone6.texOffs(86, 38).addBox(38.001F, -22.726F, -20.3874F, 6.0F, 6.0F, 2.0F, 0.0F, false);

        bone7 = new ModelRenderer(this);
        bone7.setPos(0.0F, 0.0F, -0.4336F);
        magazine.addChild(bone7);
        setRotationAngle(bone7, -0.7854F, 0.0F, 0.0F);
        bone7.texOffs(86, 38).addBox(38.0F, -1.3529F, -29.5575F, 6.0F, 2.0F, 8.0F, 0.0F, false);
        bone7.texOffs(86, 38).addBox(38.0F, -1.3529F, -33.5575F, 6.0F, 2.0F, 4.0F, 0.0F, false);
        bone7.texOffs(86, 38).addBox(38.0F, -9.3529F, -29.5575F, 6.0F, 2.0F, 8.0F, 0.0F, false);
        bone7.texOffs(86, 38).addBox(38.0F, -9.3529F, -33.5575F, 6.0F, 2.0F, 4.0F, 0.0F, false);
        bone7.texOffs(86, 38).addBox(38.0F, -7.3529F, -33.5575F, 6.0F, 6.0F, 4.0F, 0.0F, false);
        bone7.texOffs(86, 38).addBox(41.8F, -7.3529F, -29.5575F, 2.0F, 6.0F, 6.0F, 0.0F, false);
        bone7.texOffs(86, 38).addBox(38.2F, -7.3529F, -29.5575F, 2.0F, 6.0F, 6.0F, 0.0F, true);
        bone7.texOffs(86, 38).addBox(39.0F, -9.3529F, -37.5575F, 4.0F, 10.0F, 4.0F, 0.0F, false);
        bone7.texOffs(86, 38).addBox(38.0F, -7.3529F, -23.5575F, 6.0F, 6.0F, 2.0F, 0.0F, false);

        bone8 = new ModelRenderer(this);
        bone8.setPos(0.0F, 33.1379F, 5.235F);
        bone7.addChild(bone8);
        setRotationAngle(bone8, -0.0873F, 0.0F, 0.0F);
        bone8.texOffs(86, 38).addBox(39.0F, -30.4137F, -42.1992F, 4.0F, 2.0F, 12.0F, 0.0F, false);

        bullet = new ModelRenderer(this);
        bullet.setPos(-41.0F, 26.0F, -0.4336F);
        setRotationAngle(bullet, -0.0698F, 0.0F, 0.0F);

        bullet2 = new ModelRenderer(this);
        bullet2.setPos(41.0F, -2.0F, 0.4336F);
        bullet.addChild(bullet2);
        bullet2.texOffs(0, 491).addBox(-1.0F, -52.4758F, -15.2842F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        bullet2.texOffs(0, 491).addBox(-0.5F, -51.9758F, -16.1318F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bullet2.texOffs(0, 491).addBox(-1.5F, -52.9758F, -13.2842F, 3.0F, 3.0F, 2.0F, 0.0F, false);
        bullet2.texOffs(1, 492).addBox(-2.0F, -53.4758F, -11.2842F, 4.0F, 4.0F, 7.0F, 0.0F, false);
        bullet2.texOffs(0, 491).addBox(-1.5F, -52.9758F, -4.9912F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        bullet2.texOffs(0, 491).addBox(-2.0F, -53.4758F, -3.9912F, 4.0F, 4.0F, 1.0F, 0.0F, false);

        setSpecialRenderer(ModAnimations.MAGAZINE, magazine);
        setSpecialRenderer(ModAnimations.BOLT, bolt);
        setBulletRenderer(setSpecialRenderer(ModAnimations.BULLET, bullet));
    }
}
