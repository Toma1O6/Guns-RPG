package dev.toma.gunsrpg.client.animation;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.client.model.weapon.AbstractWeaponModel;
import dev.toma.gunsrpg.client.render.item.AbstractWeaponRenderer;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import lib.toma.animations.api.Animation;
import lib.toma.animations.api.AnimationStage;
import lib.toma.animations.api.IKeyframeProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class BulletEjectAnimation extends Animation {

    private final AbstractWeaponModel.IRenderCallback bulletRenderer;

    public BulletEjectAnimation(IKeyframeProvider provider) {
        super(provider, 5);
        PlayerEntity player = Minecraft.getInstance().player;
        ItemStack stack = player.getMainHandItem();
        if (stack.getItem() instanceof GunItem) {
            AbstractWeaponRenderer renderer = (AbstractWeaponRenderer) stack.getItem().getItemStackTileEntityRenderer();
            AbstractWeaponModel model = renderer.getWeaponModel();
            this.bulletRenderer = model.getBulletRenderer();
        } else {
            this.bulletRenderer = null;
        }
    }

    @Override
    public void onAnimated(AnimationStage stage, MatrixStack stack, IRenderTypeBuffer typeBuffer, int light, int overlay) {
        if (bulletRenderer != null) {
            bulletRenderer.renderSpecial(null, stack, typeBuffer.getBuffer(RenderType.entitySolid(AbstractWeaponRenderer.WEAPON)), light, overlay);
        }
    }
}
