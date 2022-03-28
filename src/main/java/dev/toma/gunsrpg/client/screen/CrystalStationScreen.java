package dev.toma.gunsrpg.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.data.IPerkProvider;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.container.CrystalStationContainer;
import dev.toma.gunsrpg.common.init.ModItems;
import dev.toma.gunsrpg.common.init.Skills;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class CrystalStationScreen extends ContainerScreen<CrystalStationContainer> {

    private static final ResourceLocation[] TEXTURES = {
            GunsRPG.makeResource("textures/screen/crystal_station1.png"),
            GunsRPG.makeResource("textures/screen/crystal_station2.png")
    };

    private final IPlayerData data;
    private final ResourceLocation background;
    private final ItemStack perkBook;

    public CrystalStationScreen(CrystalStationContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
        data = PlayerData.getUnsafe(inventory.player);
        background = TEXTURES[data.getSkillProvider().hasSkill(Skills.CRYSTALIZED) ? 1 : 0];
        imageHeight = 156;
        inventoryLabelY = 63;
        perkBook = new ItemStack(ModItems.PERKPOINT_BOOK);
    }

    @Override
    protected void renderBg(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        minecraft.getTextureManager().bind(background);
        blit(stack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        IPerkProvider perkProvider = data.getPerkProvider();
        String text = String.valueOf(perkProvider.getPoints());
        int textWidth = font.width(text);

        font.draw(stack, text, leftPos + imageWidth - 26 - textWidth, topPos + 60F, 0xAAAA);
        minecraft.getItemRenderer().renderGuiItem(perkBook, leftPos + imageWidth - 24, topPos + 56);
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        renderTooltip(stack, mouseX, mouseY);
    }
}
