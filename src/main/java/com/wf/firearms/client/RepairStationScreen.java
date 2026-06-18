package com.wf.firearms.client;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.combat.WeaponRepairService;
import com.wf.firearms.combat.WeaponWearState;
import com.wf.firearms.network.ModNetwork;
import com.wf.firearms.network.RepairStationRepairPacket;
import com.wf.firearms.repair.RepairStationBlockEntity;
import com.wf.firearms.repair.RepairStationMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** 维修站 GUI：耐久条预览 + 修理后上限递减（对标 Guns RPG RepairStationScreen）。 */
public class RepairStationScreen extends AbstractContainerScreen<RepairStationMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(GunsRpg.MOD_ID, "textures/screen/repair_station.png");

    private static final int BAR_LEFT = 7;
    private static final int BAR_TOP = 68;
    private static final int BAR_WIDTH = 161;
    private static final int BAR_HEIGHT = 9;

    private Button repairButton;

    public RepairStationScreen(RepairStationMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        imageWidth = 176;
        imageHeight = 164;
    }

    @Override
    protected void init() {
        super.init();
        inventoryLabelX = imageWidth - 8 - font.width(playerInventoryTitle);
        repairButton = Button.builder(Component.translatable("screen.button.repair"), b -> onRepair())
                .bounds(leftPos + 116, topPos + 45, 52, 20)
                .build();
        addRenderableWidget(repairButton);
        refreshRepairButton();
    }

    private void onRepair() {
        if (minecraft != null && minecraft.player != null && menu.getBlockEntity().canRepair(minecraft.player)) {
            ModNetwork.sendToServer(new RepairStationRepairPacket(menu.getBlockEntity().getBlockPos()));
        }
    }

    private void refreshRepairButton() {
        if (repairButton != null && minecraft != null && minecraft.player != null) {
            repairButton.active = menu.getBlockEntity().canRepair(minecraft.player);
        }
    }

    @Override
    protected void renderBg(GuiGraphics gfx, float partialTick, int mouseX, int mouseY) {
        gfx.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);
        ItemStack weapon = menu.getBlockEntity().getItems().getStackInSlot(RepairStationBlockEntity.SLOT_WEAPON);
        if (!weapon.isEmpty()) {
            WeaponRepairService.weaponKey(weapon).ifPresent(key -> renderWearBars(gfx, weapon, key, mouseX, mouseY));
        }
    }

    private void renderWearBars(GuiGraphics gfx, ItemStack gun, String weaponKey, int mouseX, int mouseY) {
        float currentLimit = WeaponWearState.getWearLimit(gun);
        float currentCondition = WeaponWearState.conditionRatio(gun, weaponKey);
        float afterLimit = WeaponRepairService.projectedLimitRatioAfterRepair(minecraft.player, gun, weaponKey);

        List<BarLayer> layers = new ArrayList<>();
        layers.add(new BarLayer(1.0f, 0xFFAA0000, "gunsrpg.repair.bar.total_limit"));
        layers.add(new BarLayer(currentLimit, 0xFF8B8B8B, "gunsrpg.repair.bar.current_limit"));
        layers.add(new BarLayer(currentCondition, 0xFF0094FF, "gunsrpg.repair.bar.current"));
        layers.add(new BarLayer(afterLimit, 0xFFFFA300, "gunsrpg.repair.bar.after_repair"));
        layers.sort(Comparator.comparingDouble((BarLayer b) -> b.value).reversed());

        int x0 = leftPos + BAR_LEFT;
        int y0 = topPos + BAR_TOP;
        for (BarLayer layer : layers) {
            int w = Math.max(1, Math.round(BAR_WIDTH * layer.value));
            gfx.fill(x0, y0, x0 + w, y0 + BAR_HEIGHT, layer.color);
        }

        int relX = mouseX - x0;
        int relY = mouseY - y0;
        if (relX >= 0 && relX <= BAR_WIDTH && relY >= 0 && relY <= BAR_HEIGHT) {
            for (BarLayer layer : layers) {
                int w = Math.round(BAR_WIDTH * layer.value);
                if (relX <= w) {
                    gfx.renderTooltip(font, Component.translatable(layer.tooltipKey), mouseX, mouseY);
                    break;
                }
            }
        }
    }

    @Override
    public void containerTick() {
        super.containerTick();
        refreshRepairButton();
    }

    @Override
    protected void renderLabels(GuiGraphics gfx, int mouseX, int mouseY) {
        gfx.drawString(font, title, titleLabelX, titleLabelY, 0x404040, false);
    }

    @Override
    public void render(GuiGraphics gfx, int mouseX, int mouseY, float partialTick) {
        renderBackground(gfx);
        super.render(gfx, mouseX, mouseY, partialTick);
        renderTooltip(gfx, mouseX, mouseY);
    }

    private record BarLayer(float value, int color, String tooltipKey) {}
}
