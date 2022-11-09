package dev.toma.gunsrpg.client.render.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.config.client.IHeldLayerSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.vector.Vector3f;

public class CustomHeldLayer<E extends LivingEntity, M extends EntityModel<E> & IHasArm> extends LayerRenderer<E, M> {

    private final IHeldLayerSettings config;

    public CustomHeldLayer(IEntityRenderer<E, M> renderer, IHeldLayerSettings config) {
        super(renderer);
        this.config = config;
    }

    @Override
    public void render(MatrixStack matrix, IRenderTypeBuffer buffer, int light, E entity, float f0, float f1, float f2, float f3, float f4, float f5) {
        IHeldLayerSettings.Mode mode = config.getRenderingMode();
        if (mode == IHeldLayerSettings.Mode.NONE) return;
        boolean rightHanded = entity.getMainArm() == HandSide.RIGHT;
        boolean useStatic = mode == IHeldLayerSettings.Mode.STATIC;
        ItemStack mainHandItem = useStatic ? config.getRenderItem() : rightHanded ? entity.getMainHandItem() : entity.getOffhandItem();
        ItemStack offHandItem = useStatic ? ItemStack.EMPTY : rightHanded ? entity.getOffhandItem() : entity.getMainHandItem();
        if (!mainHandItem.isEmpty() || !offHandItem.isEmpty()) {
            matrix.pushPose();
            M model = this.getParentModel();
            if (model.young) {
                float scale = 0.5f;
                matrix.translate(0.0, 0.75, 0.0);
                matrix.scale(scale, scale, scale);
            }
            renderItemInArm(entity, mainHandItem, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, HandSide.RIGHT, matrix, buffer, light);
            renderItemInArm(entity, offHandItem, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, HandSide.LEFT, matrix, buffer, light);
            matrix.popPose();
        }
    }

    private void renderItemInArm(E entity, ItemStack item, ItemCameraTransforms.TransformType transform, HandSide side, MatrixStack matrix, IRenderTypeBuffer typeBuffer, int light) {
        if (item.isEmpty()) return;
        matrix.pushPose();
        IHasArm iHasArm = this.getParentModel();
        iHasArm.translateToHand(side, matrix);
        matrix.mulPose(Vector3f.XP.rotationDegrees(-90.0F));
        matrix.mulPose(Vector3f.YP.rotationDegrees(180.0F));
        boolean leftHanded = side == HandSide.LEFT;
        matrix.translate((leftHanded ? -1 : 1) / 16.0F, 0.125, -0.625);
        Minecraft.getInstance().getItemInHandRenderer().renderItem(entity, item, transform, leftHanded, matrix, typeBuffer, light);
        matrix.popPose();
    }
}
