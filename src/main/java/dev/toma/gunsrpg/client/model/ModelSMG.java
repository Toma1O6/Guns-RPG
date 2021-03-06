package dev.toma.gunsrpg.client.model;

import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.init.Skills;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.player.EntityPlayer;
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
        textureWidth = 256;
        textureHeight = 256;

        receiver = new ModelRenderer(this);
        receiver.setRotationPoint(0.0F, 24.0F, 0.0F);


        bone = new ModelRenderer(this);
        bone.setRotationPoint(0.0F, 0.0F, 0.0F);
        receiver.addChild(bone);
        bone.cubeList.add(new ModelBox(bone, 69, 10, -2.0F, -9.5F, -13.0F, 4, 2, 23, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 69, 10, -1.5F, -8.0F, -4.0F, 3, 4, 9, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 69, 10, -1.5F, -7.5F, 7.0F, 3, 1, 3, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 69, 10, -2.0F, -12.5F, -24.0F, 4, 4, 11, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 69, 10, -1.5F, -13.0F, -24.0F, 3, 5, 11, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 69, 10, -2.0F, -11.5F, -13.0F, 4, 2, 7, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 69, 10, -2.0F, -11.5F, 1.0F, 4, 2, 9, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 69, 10, -1.0F, -11.5F, -6.0F, 3, 2, 7, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 19, 20, -1.85F, -11.5F, -5.8F, 1, 2, 7, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 69, 10, -2.0F, -13.5F, -14.0F, 4, 2, 24, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 69, 10, -1.5F, -14.0F, -13.75F, 3, 1, 22, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 4, 89, -1.7F, -11.7F, -12.6F, 4, 4, 22, 0.0F, false));
        bone.cubeList.add(new ModelBox(bone, 4, 89, -2.3F, -8.7F, -12.6F, 4, 1, 22, 0.0F, true));
        bone.cubeList.add(new ModelBox(bone, 4, 89, -2.3F, -11.7F, 1.4F, 4, 3, 8, 0.0F, true));
        bone.cubeList.add(new ModelBox(bone, 4, 89, -2.3F, -11.7F, -12.6F, 4, 3, 6, 0.0F, true));

        bone2 = new ModelRenderer(this);
        bone2.setRotationPoint(0.0F, 0.0F, -7.0F);
        receiver.addChild(bone2);
        setRotationAngle(bone2, -0.1745F, 0.0F, 0.0F);
        bone2.cubeList.add(new ModelBox(bone2, 69, 10, -2.0F, -8.3755F, -3.3077F, 4, 6, 6, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 69, 10, -1.5F, -5.3755F, 2.6923F, 3, 3, 3, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 36, 146, -1.0F, -2.3755F, 2.4923F, 2, 2, 1, 0.0F, false));

        bone3 = new ModelRenderer(this);
        bone3.setRotationPoint(0.0F, 0.0F, 0.0F);
        receiver.addChild(bone3);
        setRotationAngle(bone3, 0.6109F, 0.0F, 0.0F);
        bone3.cubeList.add(new ModelBox(bone3, 69, 10, -1.5F, -3.4087F, 6.3901F, 3, 3, 5, 0.0F, false));

        grip = new ModelRenderer(this);
        grip.setRotationPoint(0.0F, 24.0F, 0.0F);


        bone4 = new ModelRenderer(this);
        bone4.setRotationPoint(0.0F, -1.0F, 0.75F);
        grip.addChild(bone4);
        setRotationAngle(bone4, 0.4363F, 0.0F, 0.0F);
        bone4.cubeList.add(new ModelBox(bone4, 105, 48, -1.0F, -2.2289F, 2.8445F, 2, 7, 4, 0.0F, false));
        bone4.cubeList.add(new ModelBox(bone4, 105, 48, -0.5F, -0.4789F, 0.8445F, 1, 1, 2, 0.0F, false));

        bone5 = new ModelRenderer(this);
        bone5.setRotationPoint(0.0F, -1.0F, 0.75F);
        grip.addChild(bone5);
        setRotationAngle(bone5, -0.0873F, 0.0F, 0.0F);
        bone5.cubeList.add(new ModelBox(bone5, 105, 48, -0.5F, -0.9709F, -2.0081F, 1, 1, 3, 0.0F, false));

        bone6 = new ModelRenderer(this);
        bone6.setRotationPoint(0.0F, -1.0F, 0.75F);
        grip.addChild(bone6);
        setRotationAngle(bone6, -0.6109F, 0.0F, 0.0F);
        bone6.cubeList.add(new ModelBox(bone6, 105, 48, -0.5F, 0.0292F, -2.7245F, 1, 1, 1, 0.0F, false));

        magazine = new ModelRenderer(this);
        magazine.setRotationPoint(0.0F, 24.0F, -7.0F);
        setRotationAngle(magazine, -0.1745F, 0.0F, 0.0F);
        magazine.cubeList.add(new ModelBox(magazine, 89, 94, -1.5F, -9.3755F, -2.8077F, 3, 20, 5, 0.0F, false));
        magazine.cubeList.add(new ModelBox(magazine, 89, 94, -1.5F, 9.6245F, -3.8077F, 3, 1, 1, 0.0F, false));

        stock = new ModelRenderer(this);
        stock.setRotationPoint(0.0F, 23.0F, 0.0F);


        bone11 = new ModelRenderer(this);
        bone11.setRotationPoint(-9.8F, 0.8F, 6.7F);
        stock.addChild(bone11);
        setRotationAngle(bone11, 0.0F, 0.7854F, 0.0F);
        bone11.cubeList.add(new ModelBox(bone11, 25, 93, 4.5F, -11.2F, 10.0F, 1, 3, 1, 0.0F, false));

        bone7 = new ModelRenderer(this);
        bone7.setRotationPoint(0.0F, 0.0F, 0.0F);
        stock.addChild(bone7);
        bone7.cubeList.add(new ModelBox(bone7, 25, 93, -1.5F, -11.2F, 10.0F, 3, 4, 1, 0.0F, false));
        bone7.cubeList.add(new ModelBox(bone7, 25, 93, -1.0F, -8.4F, 13.1641F, 2, 2, 1, 0.0F, false));
        bone7.cubeList.add(new ModelBox(bone7, 25, 93, -1.0F, -8.4F, 14.1641F, 2, 3, 1, 0.0F, false));
        bone7.cubeList.add(new ModelBox(bone7, 25, 93, -1.5F, -9.4F, 15.1641F, 3, 1, 8, 0.0F, false));
        bone7.cubeList.add(new ModelBox(bone7, 25, 93, -1.0F, -8.4F, 18.1641F, 2, 5, 1, 0.0F, false));
        bone7.cubeList.add(new ModelBox(bone7, 25, 93, -1.5F, -9.4F, 23.1641F, 3, 8, 1, 0.0F, false));
        bone7.cubeList.add(new ModelBox(bone7, 25, 93, -1.5F, -2.7417F, 21.6743F, 3, 1, 2, 0.0F, false));
        bone7.cubeList.add(new ModelBox(bone7, 25, 93, -1.5F, -9.4F, 13.1641F, 3, 1, 2, 0.0F, false));

        bone8 = new ModelRenderer(this);
        bone8.setRotationPoint(0.0F, -4.2F, 13.5F);
        stock.addChild(bone8);
        setRotationAngle(bone8, 1.0472F, 0.0F, 0.0F);
        bone8.cubeList.add(new ModelBox(bone8, 25, 93, -1.5F, -5.6651F, 2.8122F, 3, 4, 2, 0.0F, false));
        bone8.cubeList.add(new ModelBox(bone8, 25, 93, -1.5F, -4.6651F, 1.3481F, 3, 3, 2, 0.0F, false));
        bone8.cubeList.add(new ModelBox(bone8, 25, 93, -1.5F, -1.6651F, 1.3481F, 3, 3, 1, 0.0F, false));

        bone9 = new ModelRenderer(this);
        bone9.setRotationPoint(0.0F, -4.2F, 13.5F);
        stock.addChild(bone9);
        setRotationAngle(bone9, 1.1345F, 0.0F, 0.0F);
        bone9.cubeList.add(new ModelBox(bone9, 25, 93, -1.5F, 1.4473F, 1.2266F, 3, 7, 1, 0.0F, false));
        bone9.cubeList.add(new ModelBox(bone9, 25, 93, -1.0F, -4.4304F, -0.0694F, 2, 1, 2, 0.0F, false));

        sights = new ModelRenderer(this);
        sights.setRotationPoint(0.0F, 24.0F, 0.0F);
        sights.cubeList.add(new ModelBox(sights, 87, 41, -2.0F, -14.5F, 7.6F, 4, 1, 2, 0.0F, false));
        sights.cubeList.add(new ModelBox(sights, 87, 41, 0.9F, -15.5F, 7.6F, 1, 1, 2, 0.0F, false));
        sights.cubeList.add(new ModelBox(sights, 87, 41, 0.3F, -15.2F, 8.2F, 1, 1, 1, 0.0F, false));
        sights.cubeList.add(new ModelBox(sights, 87, 41, -1.3F, -15.2F, 8.2F, 1, 1, 1, 0.0F, false));
        sights.cubeList.add(new ModelBox(sights, 87, 41, -1.9F, -15.5F, 7.6F, 1, 1, 2, 0.0F, false));
        sights.cubeList.add(new ModelBox(sights, 87, 41, -0.5F, -15.5962F, -22.8128F, 1, 2, 1, 0.0F, false));

        bone10 = new ModelRenderer(this);
        bone10.setRotationPoint(1.4F, -10.0F, 8.6F);
        sights.addChild(bone10);
        setRotationAngle(bone10, -1.309F, 0.0F, 0.0F);
        bone10.cubeList.add(new ModelBox(bone10, 87, 41, -0.5F, -2.3894F, -5.0538F, 1, 1, 2, 0.0F, false));
        bone10.cubeList.add(new ModelBox(bone10, 87, 41, -3.3F, -2.3894F, -5.0538F, 1, 1, 2, 0.0F, false));

        bone12 = new ModelRenderer(this);
        bone12.setRotationPoint(0.0F, -9.0F, -22.9F);
        sights.addChild(bone12);
        setRotationAngle(bone12, 1.1345F, 0.0F, 0.0F);
        bone12.cubeList.add(new ModelBox(bone12, 87, 41, -0.5F, -1.4604F, 4.4979F, 1, 1, 1, 0.0F, false));

        bone13 = new ModelRenderer(this);
        bone13.setRotationPoint(0.0F, -9.0F, -22.9F);
        sights.addChild(bone13);
        setRotationAngle(bone13, 0.5236F, 0.0F, 0.0F);
        bone13.cubeList.add(new ModelBox(bone13, 87, 41, -0.5F, -3.5306F, 2.2396F, 1, 3, 2, 0.0F, false));

        bone14 = new ModelRenderer(this);
        bone14.setRotationPoint(0.0F, -9.0F, -22.4F);
        sights.addChild(bone14);
        setRotationAngle(bone14, -0.0873F, 0.0F, 0.0F);
        bone14.cubeList.add(new ModelBox(bone14, 87, 41, -0.5F, -6.5351F, -0.9862F, 1, 4, 1, 0.0F, false));

        barrel = new ModelRenderer(this);
        barrel.setRotationPoint(0.0F, 24.0F, 0.0F);
        barrel.cubeList.add(new ModelBox(barrel, 30, 139, -1.5F, -11.0F, -31.0F, 2, 2, 7, 0.0F, false));
        barrel.cubeList.add(new ModelBox(barrel, 30, 139, -2.0F, -11.5F, -26.5F, 3, 3, 2, 0.0F, false));

        suppressor = new ModelRenderer(this);
        suppressor.setRotationPoint(0.0F, 14.0F, -42.8F);
        suppressor.cubeList.add(new ModelBox(suppressor, 169, 149, -1.0F, 1.7321F, -2.1719F, 2, 2, 20, 0.0F, false));
        suppressor.cubeList.add(new ModelBox(suppressor, 169, 149, -3.7321F, -1.0F, -2.1719F, 3, 2, 20, 0.0F, false));
        suppressor.cubeList.add(new ModelBox(suppressor, 169, 149, 0.7321F, -1.0F, -2.1719F, 3, 2, 20, 0.0F, true));
        suppressor.cubeList.add(new ModelBox(suppressor, 169, 149, -1.0F, -3.7321F, -2.1719F, 2, 2, 20, 0.0F, false));
        suppressor.cubeList.add(new ModelBox(suppressor, 169, 149, -1.0F, 0.7321F, -7.1719F, 2, 3, 5, 0.0F, false));
        suppressor.cubeList.add(new ModelBox(suppressor, 169, 149, -3.7321F, -1.0F, -7.1719F, 3, 2, 5, 0.0F, false));
        suppressor.cubeList.add(new ModelBox(suppressor, 169, 149, 0.7321F, -1.0F, -7.1719F, 3, 2, 5, 0.0F, true));
        suppressor.cubeList.add(new ModelBox(suppressor, 169, 149, -1.0F, -3.7321F, -7.1719F, 2, 3, 5, 0.0F, false));

        supp1 = new ModelRenderer(this);
        supp1.setRotationPoint(0.0F, 0.0F, 0.0F);
        suppressor.addChild(supp1);
        setRotationAngle(supp1, 0.0F, 0.0F, -0.5236F);
        supp1.cubeList.add(new ModelBox(supp1, 169, 149, -1.0F, -0.2679F, -2.1719F, 2, 4, 20, 0.0F, false));
        supp1.cubeList.add(new ModelBox(supp1, 169, 149, -3.7321F, -1.0F, -2.1719F, 3, 2, 20, 0.0F, false));
        supp1.cubeList.add(new ModelBox(supp1, 169, 149, -1.0F, -3.7321F, -2.1719F, 2, 4, 20, 0.0F, true));
        supp1.cubeList.add(new ModelBox(supp1, 169, 149, 0.7321F, -1.0F, -2.1719F, 3, 2, 20, 0.0F, true));
        supp1.cubeList.add(new ModelBox(supp1, 169, 149, -1.0F, 0.7321F, -7.1719F, 2, 3, 5, 0.0F, false));
        supp1.cubeList.add(new ModelBox(supp1, 169, 149, -3.7321F, -1.0F, -7.1719F, 3, 2, 5, 0.0F, false));
        supp1.cubeList.add(new ModelBox(supp1, 169, 149, -1.0F, -3.7321F, -7.1719F, 2, 3, 5, 0.0F, true));
        supp1.cubeList.add(new ModelBox(supp1, 169, 149, 0.7321F, -1.0F, -7.1719F, 3, 2, 5, 0.0F, true));

        supp0 = new ModelRenderer(this);
        supp0.setRotationPoint(0.0F, 0.0F, 0.0F);
        suppressor.addChild(supp0);
        setRotationAngle(supp0, 0.0F, 0.0F, 0.5236F);
        supp0.cubeList.add(new ModelBox(supp0, 169, 149, -1.0F, -0.2679F, -2.1719F, 2, 4, 20, 0.0F, false));
        supp0.cubeList.add(new ModelBox(supp0, 169, 149, 0.7321F, -1.0F, -2.1719F, 3, 2, 20, 0.0F, false));
        supp0.cubeList.add(new ModelBox(supp0, 169, 149, -3.7321F, -1.0F, -2.1719F, 3, 2, 20, 0.0F, true));
        supp0.cubeList.add(new ModelBox(supp0, 169, 149, -1.0F, -3.7321F, -2.1719F, 2, 4, 20, 0.0F, true));
        supp0.cubeList.add(new ModelBox(supp0, 169, 149, -1.0F, 0.7321F, -7.1719F, 2, 3, 5, 0.0F, false));
        supp0.cubeList.add(new ModelBox(supp0, 169, 149, 0.7321F, -1.0F, -7.1719F, 3, 2, 5, 0.0F, false));
        supp0.cubeList.add(new ModelBox(supp0, 169, 149, -3.7321F, -1.0F, -7.1719F, 3, 2, 5, 0.0F, true));
        supp0.cubeList.add(new ModelBox(supp0, 169, 149, -1.0F, -3.7321F, -7.1719F, 2, 3, 5, 0.0F, true));

        rds = new ModelRenderer(this);
        rds.setRotationPoint(0.0F, 24.0F, 0.0F);
        rds.cubeList.add(new ModelBox(rds, 140, 140, -2.0F, -14.5F, -12.0F, 4, 1, 13, 0.0F, false));
        rds.cubeList.add(new ModelBox(rds, 140, 140, -2.0F, -15.5F, -2.0F, 4, 1, 3, 0.0F, false));
        rds.cubeList.add(new ModelBox(rds, 140, 140, -2.0F, -15.5F, -12.0F, 4, 1, 2, 0.0F, false));
        rds.cubeList.add(new ModelBox(rds, 140, 140, -3.0F, -20.5F, -12.0F, 6, 1, 2, 0.0F, false));
        rds.cubeList.add(new ModelBox(rds, 140, 140, 2.0F, -19.5F, -12.0F, 1, 3, 2, 0.0F, false));
        rds.cubeList.add(new ModelBox(rds, 140, 140, -3.0F, -19.5F, -12.0F, 1, 3, 2, 0.0F, false));

        a1 = new ModelRenderer(this);
        a1.setRotationPoint(-2.15F, 2.4F, 0.0F);
        rds.addChild(a1);
        setRotationAngle(a1, 0.0F, 0.0F, 0.3491F);
        a1.cubeList.add(new ModelBox(a1, 140, 140, -2.6369F, -19.5257F, -12.0F, 1, 3, 2, 0.0F, false));

        a2 = new ModelRenderer(this);
        a2.setRotationPoint(-2.15F, 2.4F, 0.0F);
        rds.addChild(a2);
        setRotationAngle(a2, 0.0F, 0.0F, -0.3491F);
        a2.cubeList.add(new ModelBox(a2, 140, 140, 5.6756F, -18.0965F, -12.0F, 1, 3, 2, 0.0F, false));
    }

    @Override
    public void doRender(ItemStack stack) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        receiver.render(1f);
        grip.render(1f);
        magazine.render(1f);
        stock.render(1f);
        sights.render(1f);
        barrel.render(1f);
        if(PlayerDataFactory.hasActiveSkill(player, Skills.SMG_SUPPRESSOR)) suppressor.render(1f);
        if(PlayerDataFactory.hasActiveSkill(player, Skills.SMG_RED_DOT)) rds.render(1f);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
