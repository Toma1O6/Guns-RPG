package dev.toma.gunsrpg.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.data.IPerkProvider;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.container.CrystalPurificationStationContainer;
import dev.toma.gunsrpg.common.init.ModItems;
import dev.toma.gunsrpg.common.tileentity.CrystalPurificationStationTileEntity;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.C2S_PurifyPacket;
import dev.toma.gunsrpg.resource.perks.PurificationConfiguration;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class CrystalPurificationStationScreen extends ContainerScreen<CrystalPurificationStationContainer> {

    private static final ResourceLocation BG = GunsRPG.makeResource("textures/screen/purification_station.png");
    private final ItemStack renderItem = new ItemStack(ModItems.PERKPOINT_BOOK);
    private IPlayerData data;
    private Button purifyButton;

    public CrystalPurificationStationScreen(CrystalPurificationStationContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
        imageHeight = 164;
    }

    @Override
    protected void init() {
        super.init();
        data = PlayerData.getUnsafe(minecraft.player);
        purifyButton = addButton(new Button(leftPos + 26, topPos + 29, 37, 20, new StringTextComponent("Purify"), this::purifyButtonClicked));
        updateButtonState();
    }

    @Override
    protected void renderBg(MatrixStack matrix, float partialRenderTicks, int mouseX, int mouseY) {
        minecraft.getTextureManager().bind(BG);
        blit(matrix, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        minecraft.getItemRenderer().renderGuiItem(renderItem, leftPos + imageWidth - 22, topPos + 63);
        IPerkProvider provider = data.getPerkProvider();
        String text = String.valueOf(provider.getPoints());
        int fundsWidth = font.width(text);
        font.draw(matrix, text, leftPos + imageWidth - 24 - fundsWidth, topPos + 68, 0x00AAFF);

        CrystalPurificationStationTileEntity tile = menu.getTileEntity();
        int orbs = tile.getOrbCount();
        int price = tile.getPrice(orbs);
        if (price > -1) {
            String priceText = "Cost: " + price;
            font.draw(matrix, priceText, leftPos + 8, topPos + 68, 0x404040);
        }

        PurificationConfiguration.Entry entry = GunsRPG.getModLifecycle().getPerkManager().configLoader.getConfiguration().getPurificationConfig().getValueForAmount(orbs);
        if (entry != null && !tile.getItemHandler().getStackInSlot(1).isEmpty()) {
            float successChance = entry.getSuccessChance();
            String chance = "Success:";
            font.draw(matrix, chance, leftPos + 125, topPos + 15, 0xaa << 8);
            int chanceWidth = font.width(chance);
            String chanceAmount = Math.round(successChance * 100) + "%";
            int chanceAmountWidth = font.width(chanceAmount);
            font.draw(matrix, chanceAmount, leftPos + 125 + (chanceWidth - chanceAmountWidth) / 2.0F, topPos + 25, 0xaa << 8);
            String breakChance = "Break:";
            int breakChanceWidth = font.width(breakChance);
            String breakChanceAmount = Math.round(entry.getBreakChance() * 100) + "%";
            int breakChanceAmountWidth = font.width(breakChanceAmount);
            font.draw(matrix, breakChance, leftPos + 125 + (chanceWidth - breakChanceWidth) / 2.0F, topPos + 40, 0x404040);
            font.draw(matrix, breakChanceAmount, leftPos + 125 + (chanceWidth - breakChanceAmountWidth) / 2.0f, topPos + 50, 0x404040);
        }
    }

    @Override
    public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrix);
        super.render(matrix, mouseX, mouseY, partialTicks);
        renderTooltip(matrix, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
    }

    @Override
    protected void slotClicked(Slot slot, int mouseX, int mouseY, ClickType clickType) {
        super.slotClicked(slot, mouseX, mouseY, clickType);
        updateButtonState();
    }

    private void purifyButtonClicked(Button button) {
        NetworkManager.sendServerPacket(new C2S_PurifyPacket(menu.getTileEntity().getBlockPos()));
        updateButtonState();
    }

    private void updateButtonState() {
        purifyButton.active = menu.getTileEntity().canPurify(data.getPerkProvider());
    }
}
