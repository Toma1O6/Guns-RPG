package com.wf.firearms.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.wf.firearms.combat.FirearmStackState;
import com.wf.firearms.item.FirearmItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/** 瞄准姿态：委托 {@link FirstPersonHandTransforms}。 */
public final class AimRenderTransforms {
    private AimRenderTransforms() {}

    public static boolean isAiming(ItemStack gunStack) {
        if (gunStack.isEmpty() || !(gunStack.getItem() instanceof FirearmItem)) {
            return false;
        }
        return FirearmStackState.isAiming(gunStack);
    }

    public static void applyAim(PoseStack pose, String weaponKey, ItemDisplayContext ctx) {
        if (ctx != ItemDisplayContext.FIRST_PERSON_RIGHT_HAND) {
            return;
        }
        FirstPersonHandTransforms.applyAim(pose, weaponKey);
    }
}
