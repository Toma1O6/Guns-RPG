package dev.toma.gunsrpg.client.gui;

import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.ammo.ItemAmmo;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.SPacketSelectAmmo;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class GuiChooseAmmo extends GuiScreen {

    private final ItemAmmo[] items;

    public GuiChooseAmmo(GunItem item) {
        this.items = ItemAmmo.GUN_TO_ITEM_MAP.get(item);
    }

    @Override
    public void initGui() {
        int scale = this.width / 6;
        double diff = 1.0D / items.length;
        int x = width / 2;
        int y = height / 2;
        int py = (int)(height / 3.5);
        for(int i = 0; i < items.length; i++) {
            double angle = Math.toRadians(360.0D * (i * diff));
            double sin = Math.sin(angle);
            double cos = Math.abs(1.0D - Math.cos(angle));
            addButton(new AmmoButton(x + (int) (scale * sin) - 16, y + (int) (scale * cos) - py - 16, items[i]));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ModUtils.renderColor(0, 0, width, height, 0.0F, 0.0F, 0.0F, 0.4F);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(mouseButton != 0) return;
        for(GuiButton button : buttonList) {
            if(button instanceof AmmoButton) {
                AmmoButton ammoButton = (AmmoButton) button;
                if(ammoButton.isMouseOver(mouseX, mouseY) && ammoButton.mouseClicked()) {
                    ammoButton.playPressSound(mc.getSoundHandler());
                    break;
                }
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        mc.displayGuiScreen(null);
    }

    private static void renderRedCross(int x, int y, int x2, int y2, int width) {
        ModUtils.renderLine(x, y, x2, y2, 1.0F, 0.0F, 0.0F, 1.0F, width);
        ModUtils.renderLine(x, y2, x2, y, 1.0F, 0.0F, 0.0F, 1.0F, width);
    }

    private static class AmmoButton extends GuiButton {

        private final ItemAmmo ammo;
        private final int count;
        private final int requiredLevel;
        private final ItemStack stack;

        public AmmoButton(int x, int y, ItemAmmo ammo) {
            super(-1, x, y, 32, 32, "");
            this.ammo = ammo;
            EntityPlayer player = Minecraft.getMinecraft().player;
            ItemStack stack = player.getHeldItemMainhand();
            this.count = ModUtils.getItemCountInInventory(ammo, player.inventory);
            this.requiredLevel = ammo.getMaterial().ordinal() + 1;
            this.enabled = stack.getItem() instanceof GunItem && PlayerDataFactory.get(player).getSkills().getGunData((GunItem) stack.getItem()).getLevel() >= requiredLevel;
            this.stack = new ItemStack(ammo);
        }

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            FontRenderer fontrenderer = mc.fontRenderer;
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            ModUtils.renderColor(x, y, x + width, y + height, 0.0F, 0.0F, 0.0F, 1.0F);
            if(enabled) {
                ModUtils.renderColor(x + 1, y + 1, x + width - 1, y + height - 1, 0.2F, 0.7F, 0.2F, 1.0F);
            } else {
                ModUtils.renderColor(x + 1, y + 1, x + width - 1, y + height - 1, 0.3F, 0.3F, 0.3F, 1.0F);
            }
            mc.getRenderItem().renderItemIntoGUI(stack, x + 8, y + 8);
            int countWidth = fontrenderer.getStringWidth(count + "") / 2;
            String name = ammo.getMaterial().name();
            int nameWidth = fontrenderer.getStringWidth(name) / 2;
            fontrenderer.drawStringWithShadow(ammo.getMaterial().getColor() + name, x + 16 - nameWidth, y + height + 1, 0xffffff);
            fontrenderer.drawStringWithShadow(count + "", x + 24 - countWidth, y + height - 8, 0xffffff);
            if(hovered && !enabled) {
                String text = String.format("Requires your weapon on level %d", requiredLevel);
                fontrenderer.drawStringWithShadow(text, x + (width - fontrenderer.getStringWidth(text)) / 2.0F, y + height + 10, 0xdd0000);
            }
        }

        public boolean isMouseOver(int mouseX, int mouseY) {
            return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
        }

        public boolean mouseClicked() {
            if(!enabled) {
                return false;
            }
            NetworkManager.toServer(new SPacketSelectAmmo(ammo.getMaterial()));
            return true;
        }
    }
}
