package dev.toma.gunsrpg.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.ISkillProvider;
import dev.toma.gunsrpg.client.animation.ModAnimations;
import dev.toma.gunsrpg.common.LootStashDetectorHandler;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.skills.TreasureHunterSkill;
import dev.toma.gunsrpg.util.SkillUtil;
import lib.toma.animations.AnimationEngine;
import lib.toma.animations.api.IAnimationPipeline;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.player.PlayerEntity;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

public class StashDetectorModel extends Model {

    private static final int DIODE_LIGHT = LightTexture.pack(15, 15);
    private static final int DISPLAY_LIGHT = LightTexture.pack(13, 13);

    private final ModelRenderer red_light;
    private final ModelRenderer bone;
    private final ModelRenderer yellow_light;
    private final ModelRenderer bone5;
    private final ModelRenderer green_light;
    private final ModelRenderer bone6;
    private final ModelRenderer light_support;
    private final ModelRenderer bone2;
    private final ModelRenderer light_support2;
    private final ModelRenderer bone3;
    private final ModelRenderer light_support3;
    private final ModelRenderer bone4;
    private final ModelRenderer display;
    private final ModelRenderer cube_r1;
    private final ModelRenderer base;
    private final ModelRenderer bone11;
    private final ModelRenderer bone12;
    private final ModelRenderer bone13;
    private final ModelRenderer cube_r2;
    private final ModelRenderer bone14;
    private final ModelRenderer cube_r3;
    private final ModelRenderer keypad;
    private final ModelRenderer battery;
    private final ModelRenderer bone7;
    private final ModelRenderer battery_cover;
    private final ModelRenderer bone8;
    private final ModelRenderer bone9;
    private final ModelRenderer bone10;
    private final Map<LootStashDetectorHandler.Status, ModelRenderer> statusMap = new EnumMap<>(LootStashDetectorHandler.Status.class);

    private void renderLights(MatrixStack stack, IVertexBuilder builder, int light, int overlay) {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        UUID uuid = player.getUUID();
        if (LootStashDetectorHandler.isUsing(uuid)) {
            keypad.render(stack, builder, DISPLAY_LIGHT, overlay);
            display.render(stack, builder, DISPLAY_LIGHT, overlay);
            LootStashDetectorHandler.DetectionData data = LootStashDetectorHandler.getData(uuid);
            LootStashDetectorHandler.Status status = data.getStatus();
            for (Map.Entry<LootStashDetectorHandler.Status, ModelRenderer> entry : statusMap.entrySet()) {
                if (entry.getKey() == status) {
                    int lightValue = DIODE_LIGHT;
                    if (status == LootStashDetectorHandler.Status.NEARBY) {
                        lightValue = data.isActive() ? DIODE_LIGHT : light;
                    }
                    entry.getValue().render(stack, builder, lightValue, overlay);
                } else {
                    entry.getValue().render(stack, builder, light, overlay);
                }
            }
        } else {
            statusMap.values().forEach(renderer -> renderer.render(stack, builder, light, overlay));
            keypad.render(stack, builder, light, overlay);
            display.render(stack, builder, light, overlay);
        }
    }

    public void renderStashDetector(MatrixStack stack, IRenderTypeBuffer typeBuffer, IVertexBuilder builder, int light, int overlay, ItemCameraTransforms.TransformType type) {
        light_support.render(stack, builder, light, overlay);
        light_support2.render(stack, builder, light, overlay);
        light_support3.render(stack, builder, light, overlay);
        base.render(stack, builder, light, overlay);

        renderLights(stack, builder, light, overlay);
        renderAnimatable(stack, typeBuffer, builder, light, overlay, type.firstPerson());
    }

    private void renderAnimatable(MatrixStack stack, IRenderTypeBuffer typeBuffer, IVertexBuilder builder, int light, int overlay, boolean shouldAnimate) {
        if (shouldAnimate) {
            IAnimationPipeline pipeline = AnimationEngine.get().pipeline();
            stack.pushPose();
            pipeline.animateStage(ModAnimations.BATTERY, stack, typeBuffer, light, overlay);
            battery.render(stack, builder, light, overlay);
            stack.popPose();
            stack.pushPose();
            pipeline.animateStage(ModAnimations.BATTERY_COVER, stack, typeBuffer, light, overlay);
            battery_cover.render(stack, builder, light, overlay);
            stack.popPose();
        } else {
            battery.render(stack, builder, light, overlay);
            battery_cover.render(stack, builder, light, overlay);
        }
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder iVertexBuilder, int light, int overlay, float red, float green, float blue, float alpha) {
        throw new UnsupportedOperationException();
    }

    public StashDetectorModel() {
        super(RenderType::entitySolid);
        texWidth = 512;
        texHeight = 512;

        red_light = new ModelRenderer(this);
        red_light.setPos(-0.3F, 12.9F, 0.5F);
        red_light.texOffs(427, 174).addBox(-6.2F, -2.9858F, 6.6F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        red_light.texOffs(427, 174).addBox(-6.2F, -3.6929F, 7.3071F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        red_light.texOffs(427, 174).addBox(-6.2F, -3.6929F, 5.8929F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        red_light.texOffs(427, 174).addBox(-6.2F, -4.4F, 6.6F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        bone = new ModelRenderer(this);
        bone.setPos(-0.5F, -74.5F, -23.5F);
        red_light.addChild(bone);
        setRotationAngle(bone, -0.7854F, 0.0F, 0.0F);
        bone.texOffs(427, 174).addBox(-5.7F, 28.2843F, 72.2663F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone.texOffs(427, 174).addBox(-5.7F, 28.2843F, 70.8521F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone.texOffs(427, 174).addBox(-5.7F, 28.9914F, 71.5592F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone.texOffs(427, 174).addBox(-5.7F, 27.5772F, 71.5592F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        yellow_light = new ModelRenderer(this);
        yellow_light.setPos(-0.3F, 12.9F, -7.925F);
        yellow_light.texOffs(357, 241).addBox(-6.2F, -2.9858F, 7.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        yellow_light.texOffs(357, 241).addBox(-6.2F, -3.6929F, 8.2071F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        yellow_light.texOffs(357, 241).addBox(-6.2F, -3.6929F, 6.7929F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        yellow_light.texOffs(357, 241).addBox(-6.2F, -4.4F, 7.5F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        bone5 = new ModelRenderer(this);
        bone5.setPos(-0.5F, -74.5F, -23.5F);
        yellow_light.addChild(bone5);
        setRotationAngle(bone5, -0.7854F, 0.0F, 0.0F);
        bone5.texOffs(357, 241).addBox(-5.7F, 27.6479F, 72.9027F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone5.texOffs(357, 241).addBox(-5.7F, 27.6479F, 71.4885F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone5.texOffs(357, 241).addBox(-5.7F, 28.355F, 72.1956F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone5.texOffs(357, 241).addBox(-5.7F, 26.9408F, 72.1956F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        green_light = new ModelRenderer(this);
        green_light.setPos(-0.3F, 12.9F, -16.1F);
        green_light.texOffs(237, 215).addBox(-6.2F, -2.9858F, 8.1F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        green_light.texOffs(237, 215).addBox(-6.2F, -3.6929F, 8.8071F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        green_light.texOffs(237, 215).addBox(-6.2F, -3.6929F, 7.3929F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        green_light.texOffs(237, 215).addBox(-6.2F, -4.4F, 8.1F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        bone6 = new ModelRenderer(this);
        bone6.setPos(-0.5F, -74.5F, -23.5F);
        green_light.addChild(bone6);
        setRotationAngle(bone6, -0.7854F, 0.0F, 0.0F);
        bone6.texOffs(237, 215).addBox(-5.7F, 27.2236F, 73.327F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone6.texOffs(237, 215).addBox(-5.7F, 27.2236F, 71.9128F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone6.texOffs(237, 215).addBox(-5.7F, 27.9307F, 72.6199F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone6.texOffs(237, 215).addBox(-5.7F, 26.5165F, 72.6199F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        light_support = new ModelRenderer(this);
        light_support.setPos(0.0F, 17.3F, 4.2F);
        light_support.texOffs(342, 168).addBox(-6.0F, -8.6426F, 3.7716F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        light_support.texOffs(342, 168).addBox(-6.0F, -10.0569F, 2.3574F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        light_support.texOffs(342, 168).addBox(-6.0F, -8.6426F, 0.9431F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        light_support.texOffs(342, 168).addBox(-6.0F, -7.2284F, 2.3574F, 1.0F, 2.0F, 2.0F, 0.0F, false);

        bone2 = new ModelRenderer(this);
        bone2.setPos(-0.5F, -74.5F, -23.5F);
        light_support.addChild(bone2);
        setRotationAngle(bone2, -0.7854F, 0.0F, 0.0F);
        bone2.texOffs(342, 168).addBox(-5.5F, 27.2843F, 66.6805F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        bone2.texOffs(342, 168).addBox(-5.5F, 27.2843F, 63.8521F, 1.0F, 2.0F, 3.0F, 0.0F, false);
        bone2.texOffs(342, 168).addBox(-5.5F, 28.6985F, 65.2663F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        bone2.texOffs(342, 168).addBox(-5.5F, 25.8701F, 65.2663F, 1.0F, 2.0F, 2.0F, 0.0F, false);

        light_support2 = new ModelRenderer(this);
        light_support2.setPos(0.0F, 17.3F, -12.4F);
        light_support2.texOffs(342, 168).addBox(-6.0F, -8.6426F, 5.2716F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        light_support2.texOffs(342, 168).addBox(-6.0F, -10.0569F, 3.8574F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        light_support2.texOffs(342, 168).addBox(-6.0F, -8.6426F, 2.4431F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        light_support2.texOffs(342, 168).addBox(-6.0F, -7.2284F, 3.8574F, 1.0F, 2.0F, 2.0F, 0.0F, false);

        bone3 = new ModelRenderer(this);
        bone3.setPos(-0.5F, -74.5F, -23.5F);
        light_support2.addChild(bone3);
        setRotationAngle(bone3, -0.7854F, 0.0F, 0.0F);
        bone3.texOffs(342, 168).addBox(-5.5F, 26.2236F, 67.7412F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        bone3.texOffs(342, 168).addBox(-5.5F, 26.2236F, 64.9128F, 1.0F, 2.0F, 3.0F, 0.0F, false);
        bone3.texOffs(342, 168).addBox(-5.5F, 27.6378F, 66.327F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        bone3.texOffs(342, 168).addBox(-5.5F, 24.8094F, 66.327F, 1.0F, 2.0F, 2.0F, 0.0F, false);

        light_support3 = new ModelRenderer(this);
        light_support3.setPos(0.0F, 17.3F, -4.2F);
        light_support3.texOffs(342, 168).addBox(-6.0F, -8.6426F, 4.6716F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        light_support3.texOffs(342, 168).addBox(-6.0F, -10.0569F, 3.2574F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        light_support3.texOffs(342, 168).addBox(-6.0F, -8.6426F, 1.8431F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        light_support3.texOffs(342, 168).addBox(-6.0F, -7.2284F, 3.2574F, 1.0F, 2.0F, 2.0F, 0.0F, false);

        bone4 = new ModelRenderer(this);
        bone4.setPos(-0.5F, -74.5F, -23.5F);
        light_support3.addChild(bone4);
        setRotationAngle(bone4, -0.7854F, 0.0F, 0.0F);
        bone4.texOffs(342, 168).addBox(-5.5F, 26.6479F, 67.3169F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        bone4.texOffs(342, 168).addBox(-5.5F, 26.6479F, 64.4885F, 1.0F, 2.0F, 3.0F, 0.0F, false);
        bone4.texOffs(342, 168).addBox(-5.5F, 28.0621F, 65.9027F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        bone4.texOffs(342, 168).addBox(-5.5F, 25.2337F, 65.9027F, 1.0F, 2.0F, 2.0F, 0.0F, false);

        display = new ModelRenderer(this);
        display.setPos(0.0F, 24.0F, 0.0F);


        cube_r1 = new ModelRenderer(this);
        cube_r1.setPos(-5.4F, -21.5F, 0.0F);
        display.addChild(cube_r1);
        setRotationAngle(cube_r1, 1.5708F, 0.0F, 0.0F);
        cube_r1.texOffs(360, 141).addBox(-0.5F, -9.5F, -3.0F, 1.0F, 19.0F, 6.0F, 0.0F, false);

        base = new ModelRenderer(this);
        base.setPos(0.0F, 24.0F, 0.0F);
        base.texOffs(401, 133).addBox(-5.8F, -26.0F, 0.0F, 11.0F, 19.0F, 12.0F, 0.0F, false);
        base.texOffs(401, 133).addBox(-3.7F, -14.6F, 11.5F, 6.0F, 11.0F, 1.0F, 0.0F, false);
        base.texOffs(401, 135).addBox(-5.8F, -7.0F, 0.0F, 11.0F, 14.0F, 12.0F, 0.0F, false);
        base.texOffs(401, 135).addBox(-5.8F, -7.0F, -12.0F, 11.0F, 14.0F, 12.0F, 0.0F, false);
        base.texOffs(401, 133).addBox(-5.8F, -26.0F, -12.0F, 11.0F, 19.0F, 12.0F, 0.0F, false);
        base.texOffs(401, 133).addBox(-2.6F, -26.8F, -11.0F, 4.0F, 1.0F, 5.0F, 0.0F, false);
        base.texOffs(401, 133).addBox(-2.6F, -26.8F, -1.0F, 4.0F, 1.0F, 11.0F, 0.0F, false);
        base.texOffs(406, 135).addBox(-6.0F, -10.4F, -10.0F, 1.0F, 16.0F, 1.0F, 0.0F, false);
        base.texOffs(406, 135).addBox(-6.0F, -9.4F, 2.8F, 1.0F, 14.0F, 1.0F, 0.0F, false);
        base.texOffs(406, 135).addBox(-6.0F, -9.4F, -3.7F, 1.0F, 14.0F, 1.0F, 0.0F, false);
        base.texOffs(406, 135).addBox(-6.0F, -10.4F, -9.0F, 1.0F, 1.0F, 18.0F, 0.0F, false);
        base.texOffs(157, 18).addBox(-6.1F, -25.0F, -9.0F, 1.0F, 1.0F, 18.0F, 0.0F, false);
        base.texOffs(157, 18).addBox(-6.1F, -19.0F, -9.0F, 1.0F, 1.0F, 18.0F, 0.0F, false);
        base.texOffs(406, 135).addBox(-6.0F, -5.4F, -9.0F, 1.0F, 1.0F, 18.0F, 0.0F, false);
        base.texOffs(406, 135).addBox(-6.1F, -0.4F, -9.0F, 1.0F, 1.0F, 18.0F, 0.0F, false);
        base.texOffs(406, 135).addBox(-6.1F, 4.6F, -9.0F, 1.0F, 1.0F, 18.0F, 0.0F, false);
        base.texOffs(406, 135).addBox(-6.0F, -10.4F, 9.0F, 1.0F, 16.0F, 1.0F, 0.0F, false);
        base.texOffs(157, 18).addBox(-6.1F, -25.0F, -10.0F, 1.0F, 7.0F, 1.0F, 0.0F, false);
        base.texOffs(157, 18).addBox(-6.1F, -25.0F, 9.0F, 1.0F, 7.0F, 1.0F, 0.0F, false);
        base.texOffs(407, 143).addBox(-5.8F, 7.0F, 10.0F, 10.0F, 13.0F, 2.0F, 0.0F, false);
        base.texOffs(415, 144).addBox(-5.8F, 7.0F, -12.0F, 10.0F, 13.0F, 2.0F, 0.0F, false);
        base.texOffs(395, 138).addBox(-4.8F, 17.0F, -10.0F, 9.0F, 3.0F, 20.0F, 0.0F, false);
        base.texOffs(399, 129).addBox(4.2F, 7.0F, -12.0F, 1.0F, 13.0F, 24.0F, 0.0F, false);

        bone11 = new ModelRenderer(this);
        bone11.setPos(0.0F, 0.0F, 0.0F);
        base.addChild(bone11);
        bone11.texOffs(156, 235).addBox(-1.2071F, -45.0F, -8.2929F, 1.0F, 19.0F, 1.0F, 0.0F, false);
        bone11.texOffs(157, 235).addBox(-1.2071F, -45.0F, -9.7071F, 1.0F, 19.0F, 1.0F, 0.0F, false);
        bone11.texOffs(156, 235).addBox(-0.5F, -45.0F, -9.0F, 1.0F, 19.0F, 1.0F, 0.0F, false);
        bone11.texOffs(156, 235).addBox(-1.9142F, -45.0F, -9.0F, 1.0F, 19.0F, 1.0F, 0.0F, false);
        bone11.texOffs(156, 235).addBox(-1.2071F, -38.0F, 1.7071F, 1.0F, 12.0F, 1.0F, 0.0F, false);
        bone11.texOffs(157, 235).addBox(-1.2071F, -38.0F, 0.2929F, 1.0F, 12.0F, 1.0F, 0.0F, false);
        bone11.texOffs(156, 235).addBox(-0.5F, -38.0F, 1.0F, 1.0F, 12.0F, 1.0F, 0.0F, false);
        bone11.texOffs(156, 235).addBox(-1.9142F, -38.0F, 1.0F, 1.0F, 12.0F, 1.0F, 0.0F, false);
        bone11.texOffs(156, 235).addBox(-1.2071F, -36.0F, 7.7071F, 1.0F, 10.0F, 1.0F, 0.0F, false);
        bone11.texOffs(157, 235).addBox(-1.2071F, -36.0F, 6.2929F, 1.0F, 10.0F, 1.0F, 0.0F, false);
        bone11.texOffs(156, 235).addBox(-0.5F, -36.0F, 7.0F, 1.0F, 10.0F, 1.0F, 0.0F, false);
        bone11.texOffs(156, 235).addBox(-1.9142F, -36.0F, 7.0F, 1.0F, 10.0F, 1.0F, 0.0F, false);

        bone12 = new ModelRenderer(this);
        bone12.setPos(8.5F, -108.5F, -0.25F);
        base.addChild(bone12);
        setRotationAngle(bone12, 0.0F, 0.7854F, 0.0F);
        bone12.texOffs(156, 235).addBox(-1.1768F, 63.5F, -12.1369F, 1.0F, 19.0F, 1.0F, 0.0F, false);
        bone12.texOffs(156, 235).addBox(-1.1768F, 63.5F, -13.5511F, 1.0F, 19.0F, 2.0F, 0.0F, false);
        bone12.texOffs(156, 235).addBox(-0.4697F, 63.5F, -12.844F, 1.0F, 19.0F, 1.0F, 0.0F, false);
        bone12.texOffs(156, 235).addBox(-1.8839F, 63.5F, -12.844F, 1.0F, 19.0F, 1.0F, 0.0F, false);
        bone12.texOffs(156, 235).addBox(-8.2478F, 70.5F, -5.0659F, 1.0F, 12.0F, 1.0F, 0.0F, false);
        bone12.texOffs(156, 235).addBox(-8.2478F, 70.5F, -6.4801F, 1.0F, 12.0F, 2.0F, 0.0F, false);
        bone12.texOffs(156, 235).addBox(-7.5407F, 70.5F, -5.773F, 1.0F, 12.0F, 1.0F, 0.0F, false);
        bone12.texOffs(156, 235).addBox(-8.955F, 70.5F, -5.773F, 1.0F, 12.0F, 1.0F, 0.0F, false);
        bone12.texOffs(156, 235).addBox(-12.4905F, 72.5F, -0.8232F, 1.0F, 10.0F, 1.0F, 0.0F, false);
        bone12.texOffs(156, 235).addBox(-12.4905F, 72.5F, -2.2374F, 1.0F, 10.0F, 2.0F, 0.0F, false);
        bone12.texOffs(156, 235).addBox(-11.7834F, 72.5F, -1.5303F, 1.0F, 10.0F, 1.0F, 0.0F, false);
        bone12.texOffs(156, 235).addBox(-13.1976F, 72.5F, -1.5303F, 1.0F, 10.0F, 1.0F, 0.0F, false);

        bone13 = new ModelRenderer(this);
        bone13.setPos(0.0F, 0.0F, 0.0F);
        base.addChild(bone13);


        cube_r2 = new ModelRenderer(this);
        cube_r2.setPos(-0.1F, -11.5F, 13.0F);
        bone13.addChild(cube_r2);
        setRotationAngle(cube_r2, -0.2182F, 0.0F, 0.0F);
        cube_r2.texOffs(181, 237).addBox(-2.5F, -1.5F, -1.5F, 4.0F, 5.0F, 2.0F, 0.0F, false);

        bone14 = new ModelRenderer(this);
        bone14.setPos(0.0F, 0.0F, 0.0F);
        base.addChild(bone14);


        cube_r3 = new ModelRenderer(this);
        cube_r3.setPos(-0.1F, -6.5F, 13.0F);
        bone14.addChild(cube_r3);
        setRotationAngle(cube_r3, 0.2182F, 0.0F, 0.0F);
        cube_r3.texOffs(181, 237).addBox(-2.5F, -3.7F, -1.4F, 4.0F, 5.0F, 2.0F, 0.0F, false);

        keypad = new ModelRenderer(this);
        keypad.setPos(0.0F, 24.0F, 0.0F);
        keypad.texOffs(370, 160).addBox(-6.3F, -9.4F, -9.7F, 1.0F, 4.0F, 6.0F, 0.0F, false);
        keypad.texOffs(370, 160).addBox(-6.3F, -9.4F, -3.25F, 1.0F, 4.0F, 6.0F, 0.0F, false);
        keypad.texOffs(370, 160).addBox(-6.3F, -9.4F, 3.1F, 1.0F, 4.0F, 6.0F, 0.0F, false);
        keypad.texOffs(370, 160).addBox(-6.3F, -4.4F, 3.1F, 1.0F, 4.0F, 6.0F, 0.0F, false);
        keypad.texOffs(370, 160).addBox(-6.3F, -4.4F, -3.25F, 1.0F, 4.0F, 6.0F, 0.0F, false);
        keypad.texOffs(370, 160).addBox(-6.3F, -4.4F, -9.7F, 1.0F, 4.0F, 6.0F, 0.0F, false);
        keypad.texOffs(370, 160).addBox(-6.3F, 0.6F, -9.7F, 1.0F, 4.0F, 6.0F, 0.0F, false);
        keypad.texOffs(370, 160).addBox(-6.3F, 0.6F, -3.25F, 1.0F, 4.0F, 6.0F, 0.0F, false);
        keypad.texOffs(370, 160).addBox(-6.3F, 0.6F, 3.1F, 1.0F, 4.0F, 6.0F, 0.0F, false);

        battery = new ModelRenderer(this);
        battery.setPos(0.0F, 2.2F, 0.0F);
        battery.texOffs(143, 18).addBox(-4.9213F, 32.1213F, -9.5F, 3.0F, 3.0F, 14.0F, 0.0F, false);
        battery.texOffs(143, 18).addBox(-0.6787F, 32.1213F, -9.5F, 3.0F, 3.0F, 14.0F, 0.0F, false);
        battery.texOffs(143, 18).addBox(-2.8F, 30.0F, -9.5F, 3.0F, 4.0F, 14.0F, 0.0F, false);
        battery.texOffs(143, 18).addBox(-2.8F, 33.2426F, -9.5F, 3.0F, 4.0F, 14.0F, 0.0F, false);
        battery.texOffs(436, 246).addBox(-0.6787F, 32.1213F, 4.5F, 3.0F, 3.0F, 5.0F, 0.0F, false);
        battery.texOffs(436, 246).addBox(-2.8F, 30.0F, 4.5F, 3.0F, 3.0F, 5.0F, 0.0F, false);
        battery.texOffs(436, 246).addBox(-2.8F, 34.2426F, 4.5F, 3.0F, 3.0F, 5.0F, 0.0F, false);
        battery.texOffs(436, 246).addBox(-4.9213F, 32.1213F, 4.5F, 3.0F, 3.0F, 5.0F, 0.0F, false);

        bone7 = new ModelRenderer(this);
        bone7.setPos(3.75F, -35.25F, 6.5F);
        battery.addChild(bone7);
        setRotationAngle(bone7, 0.0F, 0.0F, 0.7854F);
        bone7.texOffs(143, 18).addBox(41.5072F, 50.7703F, -16.0F, 3.0F, 3.0F, 14.0F, 0.0F, false);
        bone7.texOffs(143, 18).addBox(45.7498F, 50.7703F, -16.0F, 3.0F, 3.0F, 14.0F, 0.0F, false);
        bone7.texOffs(143, 18).addBox(43.6285F, 48.6489F, -16.0F, 3.0F, 3.0F, 14.0F, 0.0F, false);
        bone7.texOffs(143, 18).addBox(43.6285F, 52.8916F, -16.0F, 3.0F, 3.0F, 14.0F, 0.0F, false);
        bone7.texOffs(436, 246).addBox(43.6285F, 48.6489F, -2.0F, 3.0F, 3.0F, 5.0F, 0.0F, false);
        bone7.texOffs(436, 246).addBox(41.5072F, 50.7703F, -2.0F, 4.0F, 3.0F, 5.0F, 0.0F, false);
        bone7.texOffs(436, 246).addBox(44.7498F, 50.7703F, -2.0F, 4.0F, 3.0F, 5.0F, 0.0F, false);
        bone7.texOffs(436, 246).addBox(43.6285F, 52.8916F, -2.0F, 3.0F, 3.0F, 5.0F, 0.0F, false);

        battery_cover = new ModelRenderer(this);
        battery_cover.setPos(40.3F, 24.0F, 0.0F);
        battery_cover.texOffs(400, 130).addBox(-45.6F, 7.0F, -10.0F, 1.0F, 13.0F, 20.0F, 0.0F, false);

        bone8 = new ModelRenderer(this);
        bone8.setPos(-40.3F, -56.5F, 8.0F);
        battery_cover.addChild(bone8);
        setRotationAngle(bone8, 0.0F, 0.0F, -0.7854F);
        bone8.texOffs(359, 0).addBox(-50.5632F, 42.7749F, -13.0F, 1.0F, 1.0F, 10.0F, 0.0F, false);

        bone9 = new ModelRenderer(this);
        bone9.setPos(-40.3F, -57.5F, 8.0F);
        battery_cover.addChild(bone9);
        setRotationAngle(bone9, 0.0F, 0.0F, -0.7854F);
        bone9.texOffs(359, 0).addBox(-50.5632F, 42.7749F, -13.0F, 1.0F, 1.0F, 10.0F, 0.0F, false);

        bone10 = new ModelRenderer(this);
        bone10.setPos(-40.3F, -55.5F, 8.0F);
        battery_cover.addChild(bone10);
        setRotationAngle(bone10, 0.0F, 0.0F, -0.7854F);
        bone10.texOffs(359, 0).addBox(-50.5632F, 42.7749F, -13.0F, 1.0F, 1.0F, 10.0F, 0.0F, false);

        statusMap.put(LootStashDetectorHandler.Status.UNDETECTED, red_light);
        statusMap.put(LootStashDetectorHandler.Status.NEARBY, yellow_light);
        statusMap.put(LootStashDetectorHandler.Status.LOCATED, green_light);
    }

    private static void setRotationAngle(ModelRenderer renderer, float x, float y, float z) {
        renderer.xRot = x;
        renderer.yRot = y;
        renderer.zRot = z;
    }
}
