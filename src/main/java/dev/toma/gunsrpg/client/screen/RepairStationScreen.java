package dev.toma.gunsrpg.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.container.RepairStationContainer;
import dev.toma.gunsrpg.common.container.SimpleContainerChangeListener;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.tileentity.RepairStationTileEntity;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.C2S_RequestRepairPacket;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.RenderUtils;
import lib.toma.animations.QuickSort;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RepairStationScreen extends ContainerScreen<RepairStationContainer> {

    private static final ITextComponent TEXT_REPAIR = new TranslationTextComponent("screen.button.repair");
    private static final ResourceLocation TEXTURE = GunsRPG.makeResource("textures/screen/repair_station.png");
    private final Map<DamageType, DamageHolder> damageDataMap = Arrays.stream(DamageType.values()).map(DamageHolder::new).collect(Collectors.toMap(DamageHolder::getType, Function.identity()));
    private final IContainerListener listener;

    private Button repairButton;
    private IPlayerData data;

    public RepairStationScreen(RepairStationContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
        imageHeight = 164;
        listener = new SimpleContainerChangeListener(this::slotChanged);
        menu.addSlotListener(listener);
    }

    @Override
    public void removed() {
        menu.removeSlotListener(listener);
        super.removed();
    }

    @Override
    protected void init() {
        super.init();
        ITextComponent inventoryText = inventory.getDisplayName();
        int labelWidth = font.width(inventoryText);
        inventoryLabelX = imageWidth - 8 - labelWidth;
        repairButton = addButton(new Button(leftPos + 116, topPos + 45, 52, 20, TEXT_REPAIR, this::repair));
        slotChanged(0, ItemStack.EMPTY);
        data = PlayerData.getUnsafe(minecraft.player);
    }

    @Override
    protected void renderBg(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        minecraft.getTextureManager().bind(TEXTURE);
        blit(stack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        ItemStack itemStack = menu.getTileEntity().getItemHandler().getStackInSlot(RepairStationTileEntity.SLOT_INPUT);
        if (!itemStack.isEmpty()) {
            renderDamageModel(stack, itemStack, mouseX, mouseY);
        }
    }

    @Override
    protected void renderLabels(MatrixStack stack, int mouseX, int mouseY) {
        this.font.draw(stack, this.title, this.titleLabelX, this.titleLabelY, 0x404040);
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        renderTooltip(stack, mouseX, mouseY);
    }

    private void slotChanged(int slot, ItemStack stack) {
        if (repairButton == null) return;
        RepairStationTileEntity tile = menu.getTileEntity();
        repairButton.active = tile.canRepair();
    }

    private void repair(Button button) {
        if (menu.getTileEntity().canRepair()) {
            NetworkManager.sendServerPacket(new C2S_RequestRepairPacket(menu.getTileEntity().getBlockPos()));
        }
    }

    private void renderDamageModel(MatrixStack matrixStack, ItemStack stack, int mouseX, int mouseY) {
        float currentLimit = ((GunItem) stack.getItem()).getDurabilityLimit(stack);
        float currentDurability = 1.0F - (stack.getDamageValue() / (float) stack.getMaxDamage());
        DamageHolder[] holders = this.getSortedHolders(currentLimit, currentDurability);
        Matrix4f pose = matrixStack.last().pose();
        for (DamageHolder holder : holders) {
            renderDamageHolder(pose, holder);
        }
        renderHovered(matrixStack, holders, mouseX, mouseY);
    }

    private void renderHovered(MatrixStack stack, DamageHolder[] holders, int mouseX, int mouseY) {
        ModUtils.inverse(holders);
        for (DamageHolder holder : holders) {
            int xDiff = (int) (161 * holder.getValue());
            int left = leftPos + 7;
            int top = topPos + 68;
            int right = left + xDiff;
            int bottom = topPos + 77;
            boolean hovered = ModUtils.isWithinPoints(mouseX, mouseY, left, top, right, bottom);
            if (hovered) {
                ITextComponent desc = new StringTextComponent(holder.type.description);
                renderTooltip(stack, desc, mouseX, mouseY);
                break;
            }
        }
    }

    private void renderDamageHolder(Matrix4f pose, DamageHolder holder) {
        DamageType type = holder.getType();
        int primaryRgb = type.colorPrimary;
        int secondaryRgb = type.colorSecondary;
        int xDiff = (int) (161 * holder.getValue());
        RenderUtils.drawGradient(pose, leftPos + 7, topPos + 68, leftPos + 7 + xDiff, topPos + 77, primaryRgb, secondaryRgb);
    }

    private DamageHolder[] getSortedHolders(float limit, float current) {
        float after = limit * data.getAttributes().getAttribute(Attribs.REPAIR_PENALTY).floatValue();
        damageDataMap.get(DamageType.TOTAL_LIMIT).setValue(1.0F);
        damageDataMap.get(DamageType.CURRENT_LIMIT).setValue(limit);
        damageDataMap.get(DamageType.CURRENT).setValue(current);
        damageDataMap.get(DamageType.AFTER_REPAIR).setValue(after);
        DamageHolder[] holders = damageDataMap.values().toArray(new DamageHolder[0]);
        QuickSort.sort(holders, Comparator.comparingDouble(DamageHolder::getValue).reversed().thenComparing(Comparator.comparing(DamageHolder::getType).reversed()));
        return holders;
    }

    private enum DamageType {

        CURRENT(0x0094FF, "Current durability"),
        CURRENT_LIMIT(0x8B8B8B, "Current max durability"),
        TOTAL_LIMIT(0xAA0000, 0, "Total weapon durability"),
        AFTER_REPAIR(0xFFA300, "Durability after repair");

        final int colorPrimary;
        final int colorSecondary;
        final String description;

        DamageType(int colorPrimary, int colorSecondary, String description) {
            this.colorPrimary = colorPrimary | 0xFF << 24;
            this.colorSecondary = colorSecondary | 0xFF << 24;
            this.description = description;
        }

        DamageType(int colorPrimary, String description) {
            this.colorPrimary = 0xFF << 24 | colorPrimary;
            this.colorSecondary = 0xFF << 24 | RenderUtils.darken(this.colorPrimary, 0.5F);
            this.description = description;
        }
    }

    private static class DamageHolder {

        private final DamageType type;
        private float value;

        public DamageHolder(DamageType type) {
            this.type = type;
        }

        public DamageType getType() {
            return type;
        }

        public void setValue(float value) {
            this.value = value;
        }

        public float getValue() {
            return value;
        }
    }
}
