package com.wf.firearms.client;

import com.wf.firearms.combat.GunsRpgWeaponItem;
import com.wf.firearms.combat.WeaponWearState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

/** 热键栏当前选中格为 wf / TaCZ 枪时，在格子正下方显示射击损耗条。 */
public final class WeaponWearHotbarOverlay {
    public static final IGuiOverlay OVERLAY = WeaponWearHotbarOverlay::render;

    private static final int BAR_W = 16;
    private static final int BAR_H = 2;
    private static final int HOTBAR_LEFT_OFFSET = 91;
    private static final int HOTBAR_TOP_OFFSET = 22;
    private static final int SLOT_SIZE = 20;

    private WeaponWearHotbarOverlay() {}

    private static void render(ForgeGui gui, GuiGraphics g, float partialTick, int width, int height) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui) {
            return;
        }

        int selected = mc.player.getInventory().selected;
        ItemStack stack = mc.player.getInventory().getItem(selected);
        var keyOpt = GunsRpgWeaponItem.weaponKey(stack);
        if (keyOpt.isEmpty()) {
            return;
        }

        String weaponKey = keyOpt.get();
        float ratio = WeaponWearState.conditionRatio(stack, weaponKey);
        boolean destroyed = WeaponWearState.isDestroyed(stack, weaponKey);
        int pct = Math.round(ratio * 100f);
        int max = WeaponWearState.getEffectiveMaxWear(stack, weaponKey);
        int remain = WeaponWearState.remainingWear(stack, weaponKey);

        int hotbarLeft = width / 2 - HOTBAR_LEFT_OFFSET;
        int hotbarTop = height - HOTBAR_TOP_OFFSET;
        int slotX = hotbarLeft + selected * SLOT_SIZE + 2;
        int barY = hotbarTop + 17;

        int filled = destroyed ? 0 : Math.max(1, Math.round(BAR_W * ratio));
        g.fill(slotX, barY, slotX + filled, barY + BAR_H, WeaponWearState.barColor(stack, weaponKey));
        if (filled < BAR_W) {
            g.fill(slotX + filled, barY, slotX + BAR_W, barY + BAR_H, 0xFF2A2A2A);
        }

        Component label =
                destroyed
                        ? Component.translatable("gunsrpg.gun.wear_destroyed")
                        : Component.translatable("gunsrpg.gun.wear", pct, remain, max);
        int labelColor = destroyed ? 0xFFFF5555 : 0xFFAAAAAA;
        int labelW = mc.font.width(label);
        int labelX = slotX + (BAR_W - labelW) / 2;
        int labelY = barY - 9;
        if (labelY >= 0) {
            g.drawString(mc.font, label, labelX, labelY, labelColor, true);
        }
    }
}
