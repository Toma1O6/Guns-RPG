package dev.toma.gunsrpg.client.gui;

import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.ammo.ItemAmmo;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.SPacketSelectAmmo;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.math.Vec2Di;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class GuiChooseAmmo extends GuiScreen {

    private final ItemAmmo[] items;
    private final List<Pair<Vec2Di, AmmoInformation>> ammoList = new ArrayList<>();
    private Pair<Vec2Di, AmmoInformation> hoveredEntry;

    public GuiChooseAmmo(GunItem item) {
        this.items = ItemAmmo.GUN_TO_ITEM_MAP.get(item);
    }

    @Override
    public void initGui() {
        ammoList.clear();
        int scale = this.width / 6;
        double diff = 1.0D / items.length;
        int y = (int)(height / 3.5);
        for(int i = 0; i < items.length; i++) {
            double angle = Math.toRadians(360.0D * (i * diff));
            double sin = Math.sin(angle);
            double cos = Math.abs(1.0D - Math.cos(angle));
            ammoList.add(Pair.of(new Vec2Di( (int)(scale * sin), (int) (scale * cos) - y), new AmmoInformation(items[i], mc.player) ));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int x = this.width / 2;
        int y = this.height / 2;
        hoveredEntry = null;
        for(Pair<Vec2Di, AmmoInformation> pair : ammoList) {
            Vec2Di pos = pair.getLeft();
            AmmoInformation ammoInformation = pair.getRight();
            boolean f = ammoInformation.canUse && mouseX >= x + pos.x - 16 && mouseX <= x + pos.x + 16 && mouseY >= y + pos.y - 16 && mouseY <= y + pos.y + 16;
            if(f) hoveredEntry = pair;
            ItemAmmo item = ammoInformation.getAmmo();
            ItemStack stack = new ItemStack(item);
            ModUtils.renderColor(x + pos.x - 16, y + pos.y - 16, x + pos.x + 16, y + pos.y + 16, f ? 0.7F : 0.0F, f ? 0.7F : 0.0F, f ? 0.7F : 0.0F, 0.5F);
            mc.getRenderItem().renderItemIntoGUI(stack, x + pos.x - 8, y + pos.y - 8);
            String name = stack.getDisplayName();
            GlStateManager.pushMatrix();
            GlStateManager.translate(0, 0, 101);
            if(!ammoInformation.canUse) {
                renderRedCross(x + pos.x - 16, y + pos.y - 16, x + pos.x + 16, y + pos.y + 16, 4);
            }
            FontRenderer renderer = mc.fontRenderer;
            int width = renderer.getStringWidth(name) / 2;
            int countWidth = renderer.getStringWidth(ammoInformation.totalCount + "") / 2;
            mc.fontRenderer.drawStringWithShadow(ammoInformation.totalCount + "", x + pos.x + 8 - countWidth, y + pos.y + 8, 0xffffff);
            mc.fontRenderer.drawStringWithShadow(item.getMaterial().getColor() + name, x + pos.x - width, y + pos.y + 18, 0xffffff);
            GlStateManager.popMatrix();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(hoveredEntry != null) {
            NetworkManager.toServer(new SPacketSelectAmmo(hoveredEntry.getRight().getAmmo().getMaterial()));
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

    private static class AmmoInformation {

        private final ItemAmmo ammo;
        private int totalCount;
        private final boolean canUse;

        public AmmoInformation(ItemAmmo ammo, EntityPlayer player) {
            this.ammo = ammo;
            InventoryPlayer inventory = player.inventory;
            for(int i = 0; i < inventory.getSizeInventory(); i++) {
                ItemStack stack = inventory.getStackInSlot(i);
                if(stack.getItem() == ammo) {
                    totalCount += stack.getCount();
                }
            }
            this.canUse = PlayerDataFactory.get(player).getSkillData().getAbilityData().hasProperty(ammo.getRequiredProperty());
        }

        public ItemAmmo getAmmo() {
            return ammo;
        }

        public int getTotalCount() {
            return totalCount;
        }
    }
}
