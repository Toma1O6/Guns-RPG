package dev.toma.gunsrpg.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.container.SmithingTableContainer;
import dev.toma.gunsrpg.common.tileentity.SmithingTableTileEntity;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.SPacketCheckSmithingRecipe;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.object.OptionalObject;
import dev.toma.gunsrpg.util.recipes.SmithingTableRecipes;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;
import java.util.List;

public class GuiSmithingTable extends ContainerScreen<SmithingTableContainer> {

    public static final ResourceLocation TEXTURE = GunsRPG.makeResource("textures/gui/smithing_table.png");
    private final SmithingTableTileEntity smithingTable;
    private List<SmithingTableRecipes.SmithingRecipe> recipeList = new ArrayList<>();
    private int scrollIndex;

    public GuiSmithingTable(SmithingTableContainer container, PlayerInventory inventory, ITextComponent component) {
        super(container, inventory, component);
        this.smithingTable = container.getTileEntity();
        imageWidth = 203;
        imageHeight = 172;
    }

    @Override
    public void init() {
        addButton(new Button(leftPos + 25, topPos + 65, 54, 20, new StringTextComponent("Craft"), this::buttonCraft_Clicked));
        recipeList = SmithingTableRecipes.getAvailableRecipes(minecraft.player);
        for (int i = scrollIndex; i < scrollIndex + 8; i++) {
            if (i >= recipeList.size()) break;
            SmithingTableRecipes.SmithingRecipe recipe = recipeList.get(i);
            int index = i - scrollIndex;
            //RecipeObject object = new RecipeObject(leftPos + 170, topPos + 5 + index * 20, 20, 20, recipe);
            //addButton(object);
        }
    }

    private void buttonCraft_Clicked(Button button) {
        boolean shiftKey = Screen.hasShiftDown();
        NetworkManager.sendServerPacket(new SPacketCheckSmithingRecipe(smithingTable.getBlockPos(), shiftKey, OptionalObject.empty()));
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        int scrollbarX = leftPos + 191;
        int scrollbarWidth = 8;
        int scrollbarY = topPos + 5;
        int scroolbarHeight = 160;
        if(mouseX >= scrollbarX && mouseX <= scrollbarX + scrollbarWidth && mouseY >= scrollbarY && mouseY <= scrollbarY + scroolbarHeight) {
            float step = scroolbarHeight / (float) recipeList.size();
            int barPos = (int)(scrollIndex * step);
            int barHeight = (int)((scrollIndex + 8) * step - barPos);
            int halfHeight = barHeight / 2;
            int delta = (int) (mouseY - scrollbarY - barPos - halfHeight);
            if (delta > halfHeight) {
                int next = scrollIndex + 1;
                if(next < recipeList.size() - 7) {
                    scrollIndex = next;
                    init(minecraft, width, height);
                }
            } else if(delta < -halfHeight) {
                int next = scrollIndex - 1;
                if(next >= 0) {
                    scrollIndex = next;
                    init(minecraft, width, height);
                }
            }
        }
        return false;
    }

    @Override
    protected void renderBg(MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        minecraft.getTextureManager().bind(TEXTURE);
        blit(p_230450_1_, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrix);
        super.render(matrix, mouseX, mouseY, partialTicks);
        renderScrollbar(matrix, leftPos + 191, 8);
        renderTooltip(matrix, mouseX, mouseY);
    }

    public void renderScrollbar(MatrixStack matrix, int x, int width) {
        int barHeight = 160;
        ModUtils.renderColor(x, topPos + 5, x + width, topPos + 5 + barHeight, 0.2F, 0.2F, 0.2F, 1.0F);
        double step = recipeList.size() == 0 ? 1.0 : barHeight / (double) recipeList.size();
        double start = scrollIndex * step;
        double end = recipeList.size() == 0 ? barHeight - 1 : Math.min(barHeight, (scrollIndex + 8) * step) - 1;
        ModUtils.renderColor(x + 1, (int) (topPos + 6 + start), x + width - 1, (int) (topPos + 5 + end), 0.75F, 0.75F, 0.75F, 1.0F);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        int i = (int) (-amount);
        int next = scrollIndex + i;
        if (i != 0 && next >= 0 && next < recipeList.size() - 7) {
            scrollIndex = next;
            init(minecraft, width, height);
            return true;
        }
        return false;
    }
}
