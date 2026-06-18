package com.wf.firearms.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

/** 技能树顶栏视图切换按钮（独立类，避免内嵌类在 reobf/热加载下 ClassNotFound）。 */
final class SkillTreeBookTabButton extends Button {
    private final ItemStack icon;
    private final boolean selected;

    SkillTreeBookTabButton(
            int x,
            int y,
            ItemStack icon,
            boolean selected,
            OnPress onPress,
            Component tooltip) {
        super(x, y, 22, 22, Component.empty(), onPress, DEFAULT_NARRATION);
        this.icon = icon;
        this.selected = selected;
        setTooltip(Tooltip.create(tooltip));
    }

    @Override
    protected void renderWidget(GuiGraphics gfx, int mouseX, int mouseY, float partialTick) {
        int bg = selected ? 0xCC8B6914 : (isHoveredOrFocused() ? 0x88FFFFFF : 0x44000000);
        gfx.fill(getX(), getY(), getX() + width, getY() + height, bg);
        gfx.renderItem(icon, getX() + 3, getY() + 3);
        if (selected) {
            gfx.fill(getX(), getY() + height - 2, getX() + width, getY() + height, 0xFFFFD21E);
        }
    }
}
