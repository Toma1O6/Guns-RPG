package dev.toma.gunsrpg.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.data.IPerkProvider;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.container.CrystalFusionStationContainer;
import dev.toma.gunsrpg.common.init.ModItems;
import dev.toma.gunsrpg.common.item.perk.Crystal;
import dev.toma.gunsrpg.common.item.perk.CrystalItem;
import dev.toma.gunsrpg.common.item.perk.PerkVariant;
import dev.toma.gunsrpg.common.tileentity.CrystalFusionStationTileEntity;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.C2S_FusePacket;
import dev.toma.gunsrpg.resource.perks.FusionConfiguration;
import dev.toma.gunsrpg.resource.perks.PerkConfiguration;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.IItemHandler;

import java.util.Arrays;

public class CrystalFuseStationScreen extends ContainerScreen<CrystalFusionStationContainer> {

    private static final ResourceLocation BG = GunsRPG.makeResource("textures/screen/fusion_station.png");
    private static final ITextComponent BUTTON_FUSE = new TranslationTextComponent("screen.button.fuse_crystals");
    private final ItemStack renderItem = new ItemStack(ModItems.PERKPOINT_BOOK);
    private IPlayerData data;
    private Button fuseButton;

    public CrystalFuseStationScreen(CrystalFusionStationContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
        imageHeight = 164;
    }

    @Override
    protected void init() {
        super.init();
        data = PlayerData.getUnsafe(minecraft.player);
        fuseButton = addButton(new Button(leftPos + 61, topPos + 55, 54, 20, BUTTON_FUSE, this::fuseButtonClicked));
        updateButtonState();
    }

    @Override
    protected void slotClicked(Slot slot, int mouseX, int mouseY, ClickType type) {
        super.slotClicked(slot, mouseX, mouseY, type);
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

        CrystalFusionStationTileEntity tileEntity = menu.getTileEntity();
        IItemHandler itemHandler = tileEntity.getItemHandler();
        int orbCount = (int) Arrays.stream(CrystalFusionStationTileEntity.ORBS).mapToObj(itemHandler::getStackInSlot).filter(stack -> !stack.isEmpty()).count();
        int price = this.getPrice(orbCount);
        PerkConfiguration perkConfig = GunsRPG.getModLifecycle().getPerkManager().configLoader.getConfiguration();
        FusionConfiguration fusionConfig = perkConfig.getFusionConfig();
        FusionConfiguration.Swap swapConfig = fusionConfig.getSwaps().getSwapStat(orbCount);
        if (price > 0) {
            font.draw(matrix, "Cost: " + price, leftPos + 6, topPos + 68, 0x404040);
        }
        ItemStack item1 = itemHandler.getStackInSlot(1);
        ItemStack item2 = itemHandler.getStackInSlot(2);
        if (item1.isEmpty() || item2.isEmpty()) {
            return;
        }

        Crystal crystal1 = CrystalItem.getCrystal(item1);
        Crystal crystal2 = CrystalItem.getCrystal(item2);
        if (crystal1 != null && crystal2 != null) {
            int targetLevel = crystal1.getLevel() + crystal2.getLevel();
            FusionConfiguration.Upgrade upgrade = fusionConfig.getUpgrades().getUpgradeStat(targetLevel);
            float breakChance = upgrade.getBreakChance();
            String breakChanceText = "Break: " + Math.round(breakChance * 100) + "%";
            font.draw(matrix, breakChanceText, leftPos + 6, topPos + 58, 0x404040);
        }

        PerkVariant variant1 = tileEntity.getItemVariant(item1);
        PerkVariant variant2 = tileEntity.getItemVariant(item2);
        PerkVariant targetVariant = tileEntity.getTargetedOrbType();
        float chance1, chance2;
        if (variant1 == variant2) {
            return;
        }
        if (targetVariant == null) {
            chance1 = 0.5f;
            chance2 = 0.5f;
        } else {
            float swapChance = swapConfig.getChance();
            float chance = 1.0F - swapChance;
            chance1 = variant1 == targetVariant ? swapChance : chance;
            chance2 = variant2 == targetVariant ? swapChance : chance;
        }
        String text1 = Math.round(chance1 * 100) + "%";
        String text2 = Math.round(chance2 * 100) + "%";
        font.draw(matrix, text1, leftPos + 38 + (font.width(text1) - 18) / 2.0F, topPos + 30, variant1.getRgb());
        font.draw(matrix, text2, leftPos + 122 + (font.width(text2) - 18) / 2.0F, topPos + 30, variant2.getRgb());
    }

    @Override
    protected void renderLabels(MatrixStack matrix, int mouseX, int mouseY) {
    }

    @Override
    public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrix);
        super.render(matrix, mouseX, mouseY, partialTicks);
        renderTooltip(matrix, mouseX, mouseY);
    }

    private void fuseButtonClicked(Button button) {
        NetworkManager.sendServerPacket(new C2S_FusePacket(menu.getTileEntity()));
    }

    private void updateButtonState() {
        fuseButton.active = menu.getTileEntity().canFuse(minecraft.player);
    }

    private int getPrice(int orbCount) {
        PerkConfiguration perkConfig = GunsRPG.getModLifecycle().getPerkManager().configLoader.getConfiguration();
        FusionConfiguration fusionConfig = perkConfig.getFusionConfig();
        CrystalFusionStationTileEntity tileEntity = menu.getTileEntity();
        IItemHandler itemHandler = tileEntity.getItemHandler();
        FusionConfiguration.Swap swapConfig = fusionConfig.getSwaps().getSwapStat(orbCount);
        Crystal crystal1 = CrystalItem.getCrystal(itemHandler.getStackInSlot(CrystalFusionStationTileEntity.INPUTS[0]));
        Crystal crystal2 = CrystalItem.getCrystal(itemHandler.getStackInSlot(CrystalFusionStationTileEntity.INPUTS[1]));
        if (crystal1 == null || crystal2 == null) {
            return 0;
        }
        int targetLevel = crystal1.getLevel() + crystal2.getLevel();
        FusionConfiguration.Upgrade upgrade = fusionConfig.getUpgrades().getUpgradeStat(targetLevel);
        if (upgrade == null) {
            return 0;
        }
        return upgrade.getPrice() + swapConfig.getPrice();
    }
}
