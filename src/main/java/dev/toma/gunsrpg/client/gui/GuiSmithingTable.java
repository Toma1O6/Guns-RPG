package dev.toma.gunsrpg.client.gui;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.container.ContainerSmithingTable;
import dev.toma.gunsrpg.common.tileentity.TileEntitySmithingTable;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.SPacketCheckSmithingRecipe;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.object.OptionalObject;
import dev.toma.gunsrpg.util.recipes.SmithingTableRecipes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GuiSmithingTable extends GuiContainer {

    private static final ResourceLocation TEXTURE = GunsRPG.makeResource("textures/gui/smithing_table.png");
    private final TileEntitySmithingTable smithingTable;
    private List<SmithingTableRecipes.SmithingRecipe> recipeList = new ArrayList<>();
    private int scrollIndex;
    private final OptionalObject<RecipeObject> clicked = OptionalObject.empty();

    public GuiSmithingTable(EntityPlayer player, TileEntitySmithingTable tileEntity) {
        super(new ContainerSmithingTable(player, tileEntity));
        this.smithingTable = tileEntity;
        ySize = 172;
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();
        addButton(new GuiButton(0, guiLeft + 115, guiTop + 65, 54, 20, "Craft"));
        recipeList = SmithingTableRecipes.getAvailableRecipes(mc.player);
        recipeList.sort(Comparator.comparing(rec -> rec.getOutput(mc.player).getDisplayName()));
        for(int i = scrollIndex; i < scrollIndex + 4; i++) {
            if(i >= recipeList.size()) break;
            SmithingTableRecipes.SmithingRecipe recipe = recipeList.get(i);
            int index = i - scrollIndex;
            addButton(new RecipeObject(guiLeft + 7, guiTop + 7 + index * 20, 90, 20, recipe));
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if(button.id == 0) {
            NetworkManager.toServer(new SPacketCheckSmithingRecipe(smithingTable.getPos()));
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        mc.getTextureManager().bindTexture(TEXTURE);
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        if(recipeList.isEmpty()) {
            String text = "No known recipes";
            mc.fontRenderer.drawString(text, guiLeft + 6 + (96 - fontRenderer.getStringWidth(text)) / 2, guiTop + 25, 0xff0000);
        }
        clicked.ifPresent(button -> {
            int px = guiLeft + xSize;
            int py = guiTop;
            GlStateManager.disableLighting();
            ModUtils.renderTexture(px, py, px + 68, py + 68, 176 / 255.0D, 0, 243 / 255.0D, 68 / 255.0D, TEXTURE);
            RenderHelper.enableGUIStandardItemLighting();
            for(SmithingTableRecipes.SmithingIngredient ingredient : button.recipe.getIngredients()) {
                int position = ingredient.getIndex();
                int ingredientX = px + 7 + (position % 3) * 18;
                int ingredientY = py + 8 + (position / 3) * 18;
                mc.getRenderItem().renderItemIntoGUI(ingredient.getFirstItem(), ingredientX, ingredientY);
            }
            GlStateManager.enableLighting();
        });
        renderScrollbar(guiLeft + 98, 8);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    public void renderScrollbar(int x, int width) {
        int barHeight = 80;
        ModUtils.renderColor(x, guiTop + 7, x + width, guiTop + 7 + barHeight, 0.2F, 0.2F, 0.2F, 1.0F);
        double step = recipeList.size() == 0 ? 1.0 : barHeight / (double) recipeList.size();
        double start = scrollIndex * step;
        double end = recipeList.size() == 0 ? barHeight - 1 : Math.min(barHeight, (scrollIndex + 4) * step) - 1;
        ModUtils.renderColor(x + 1, (int)(guiTop + 8 + start), x + width - 1, (int)(guiTop + 7 + end), 0.75F, 0.75F, 0.75F, 1.0F);
    }

    @Override
    public void handleMouseInput() throws IOException {
        int i = -Integer.signum(Mouse.getEventDWheel());
        int next = scrollIndex + i;
        if(next >= 0 && next < recipeList.size() - 3) {
            if(i != 0) {
                scrollIndex = next;
                initGui();
            }
        }
        super.handleMouseInput();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        clicked.clear();
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public class RecipeObject extends GuiButton {

        private final SmithingTableRecipes.SmithingRecipe recipe;
        private final ItemStack output;

        public RecipeObject(int x, int y, int w, int h, SmithingTableRecipes.SmithingRecipe recipe) {
            super(-1, x, y, w, h, "");
            this.recipe = recipe;
            this.output = recipe.getOutput(Minecraft.getMinecraft().player);
            this.displayString = output.getDisplayName();
        }

        @Override
        public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
            boolean flag = super.mousePressed(mc, mouseX, mouseY);
            if(flag) GuiSmithingTable.this.clicked.map(this);
            return flag;
        }

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            super.drawButton(mc, mouseX, mouseY, partialTicks);
        }
    }
}
