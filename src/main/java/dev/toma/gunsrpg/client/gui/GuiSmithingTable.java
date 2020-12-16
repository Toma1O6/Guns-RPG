package dev.toma.gunsrpg.client.gui;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.common.container.ContainerSmithingTable;
import dev.toma.gunsrpg.common.init.GRPGItems;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.common.tileentity.TileEntitySmithingTable;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.SPacketCheckSmithingRecipe;
import dev.toma.gunsrpg.network.packet.SPacketQuickInsertRecipe;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.object.LazyLoader;
import dev.toma.gunsrpg.util.object.OptionalObject;
import dev.toma.gunsrpg.util.recipes.SmithingTableRecipes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiSmithingTable extends GuiContainer {

    private static final LazyLoader<Item[]> references = new LazyLoader<>(() -> new Item[] {Items.GUNPOWDER, GRPGItems.WOODEN_AMMO_9MM, GRPGItems.WOODEN_AMMO_CROSSBOW_BOLT, GRPGItems.WOODEN_AMMO_12G, GRPGItems.WOODEN_AMMO_762MM, GRPGItems.WOODEN_HAMMER, GRPGItems.BANDAGE, GRPGItems.GUN_PARTS});
    public static final ResourceLocation TEXTURE = GunsRPG.makeResource("textures/gui/smithing_table.png");
    private final TileEntitySmithingTable smithingTable;
    private final OptionalObject<SmithingTableRecipes.SmithingRecipe> clicked = OptionalObject.empty();
    private List<SmithingTableRecipes.SmithingRecipe> recipeList = new ArrayList<>();
    private int scrollIndex;

    public GuiSmithingTable(EntityPlayer player, TileEntitySmithingTable tileEntity) {
        super(new ContainerSmithingTable(player, tileEntity));
        this.smithingTable = tileEntity;
        xSize = 203;
        ySize = 172;
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();
        addButton(new GuiButton(0, guiLeft + 25, guiTop + 65, 54, 20, "Craft"));
        addButton(new GuiButton(1, guiLeft + 82, guiTop + 65, 20, 20, "+"));
        recipeList = SmithingTableRecipes.getAvailableRecipes(mc.player);
        for (int i = scrollIndex; i < scrollIndex + 8; i++) {
            if (i >= recipeList.size()) break;
            SmithingTableRecipes.SmithingRecipe recipe = recipeList.get(i);
            int index = i - scrollIndex;
            RecipeObject object = new RecipeObject(guiLeft + 170, guiTop + 5 + index * 20, 20, 20, recipe);
            addButton(object);
        }
        Item[] items = references.get();
        for(int i = 0; i < items.length; i++) {
            int x = i / 4 < 1 ? guiLeft + 115 : guiLeft + 140;
            int y = guiTop + i % 4 * 21 + 3;
            addButton(new QuickScroll(x, y, 20, 20, items[i]));
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        int scrollbarX = guiLeft + 191;
        int scrollbarWidth = 8;
        int scrollbarY = guiTop + 5;
        int scroolbarHeight = 160;
        if(mouseX >= scrollbarX && mouseX <= scrollbarX + scrollbarWidth && mouseY >= scrollbarY && mouseY <= scrollbarY + scroolbarHeight) {
            float step = scroolbarHeight / (float) recipeList.size();
            int barPos = (int)(scrollIndex * step);
            int barHeight = (int)((scrollIndex + 8) * step - barPos);
            int halfHeight = barHeight / 2;
            int delta = mouseY - scrollbarY - barPos - halfHeight;
            if (delta > halfHeight) {
                int next = scrollIndex + 1;
                if(next < recipeList.size() - 7) {
                    scrollIndex = next;
                    initGui();
                }
            } else if(delta < -halfHeight) {
                int next = scrollIndex - 1;
                if(next >= 0) {
                    scrollIndex = next;
                    initGui();
                }
            }
        }
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            boolean shiftKey = GuiScreen.isShiftKeyDown();
            NetworkManager.toServer(new SPacketCheckSmithingRecipe(smithingTable.getPos(), shiftKey, clicked.isPresent() ? OptionalObject.of(clicked.get()) : OptionalObject.empty()));
        } else if(button.id == 1) {
            if(clicked.isPresent()) {
                SmithingTableRecipes.SmithingRecipe recipe = clicked.get();
                do {
                    if(!hasSpace(recipe) || !canCraft(recipe)) {
                        break;
                    }
                    for(SmithingTableRecipes.SmithingIngredient ingredient : recipe.getIngredients()) {
                        int index = ingredient.getIndex();
                        ItemStack stack = ingredient.getFirstItem().copy();
                        int i = mc.player.inventory.clearMatchingItems(stack.getItem(), stack.getItemDamage(), 1, null);
                        insert(index, stack);
                    }
                } while (isShiftKeyDown());
                NetworkManager.toServer(new SPacketQuickInsertRecipe(recipe, smithingTable.getPos(), isShiftKeyDown()));
            }
        }
    }

    void insert(int slot, ItemStack stack) {
        ItemStack st = smithingTable.getStackInSlot(slot);
        if(st.isEmpty()) {
            smithingTable.setInventorySlotContents(slot, stack);
        } else {
            if(st.getItem() != stack.getItem()) {
                ItemStack remove = st.copy();
                smithingTable.removeStackFromSlot(slot);
                mc.player.addItemStackToInventory(remove);
            }
            int count = smithingTable.getStackInSlot(slot).getCount();
            smithingTable.setInventorySlotContents(slot, new ItemStack(stack.getItem(), count + stack.getCount(), stack.getItemDamage()));
        }
    }

    boolean hasSpace(SmithingTableRecipes.SmithingRecipe recipe) {
        for(SmithingTableRecipes.SmithingIngredient ingredient : recipe.getIngredients()) {
            ItemStack stack = smithingTable.getStackInSlot(ingredient.getIndex());
            if(stack.getCount() >= 64) {
                return false;
            }
        }
        return true;
    }

    boolean canCraft(SmithingTableRecipes.SmithingRecipe recipe) {
        for(SmithingTableRecipes.SmithingIngredient ingredient : recipe.getIngredients()) {
            Item item = ingredient.getFirstItem().getItem();
            int ingredientCount = ingredientsInRecipe(recipe, ingredient);
            int inInventory = getFromInventory(item);
            if(inInventory < ingredientCount) {
                return false;
            }
        }
        return true;
    }

    int ingredientsInRecipe(SmithingTableRecipes.SmithingRecipe recipe, SmithingTableRecipes.SmithingIngredient ingredient) {
        int i = 0;
        for(SmithingTableRecipes.SmithingIngredient ingredient1 : recipe.getIngredients()) {
            if(ingredient.getFirstItem().getItem() == ingredient1.getFirstItem().getItem()) {
                ++i;
            }
        }
        return i;
    }

    int getFromInventory(Item item) {
        InventoryPlayer player = mc.player.inventory;
        int count = 0;
        for(int i = 0; i < player.getSizeInventory(); i++) {
            ItemStack stack = player.getStackInSlot(i);
            if(stack.getItem() == item) {
                count += stack.getCount();
            }
        }
        return count;
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
        clicked.ifPresent(recipe -> {
            int px = guiLeft + xSize;
            int py = guiTop;
            RenderHelper.enableGUIStandardItemLighting();
            List<Slot> slots = inventorySlots.inventorySlots;
            for (SmithingTableRecipes.SmithingIngredient ingredient : recipe.getIngredients()) {
                int position = ingredient.getIndex();
                int ingredientX = guiLeft + 26 + (position % 3) * 18;
                int ingredientY = py + 8 + (position / 3) * 18;
                Slot slot = slots.get(position);
                if(!slot.getHasStack()) {
                    mc.getRenderItem().renderItemIntoGUI(ingredient.getFirstItem(), ingredientX, ingredientY);
                }
                if(!slot.getStack().isItemEqual(ingredient.getFirstItem())) {
                    ModUtils.renderColor(ingredientX, ingredientY, ingredientX + 16, ingredientY + 16, 1.0F, 0.0F, 0.0F, 0.5F);
                }
            }
            GlStateManager.enableLighting();
        });
        renderScrollbar(guiLeft + 191, 8);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    public void renderScrollbar(int x, int width) {
        int barHeight = 160;
        ModUtils.renderColor(x, guiTop + 5, x + width, guiTop + 5 + barHeight, 0.2F, 0.2F, 0.2F, 1.0F);
        double step = recipeList.size() == 0 ? 1.0 : barHeight / (double) recipeList.size();
        double start = scrollIndex * step;
        double end = recipeList.size() == 0 ? barHeight - 1 : Math.min(barHeight, (scrollIndex + 8) * step) - 1;
        ModUtils.renderColor(x + 1, (int) (guiTop + 6 + start), x + width - 1, (int) (guiTop + 5 + end), 0.75F, 0.75F, 0.75F, 1.0F);
    }

    @Override
    public void handleMouseInput() throws IOException {
        int i = -Integer.signum(Mouse.getEventDWheel());
        int next = scrollIndex + i;
        if (next >= 0 && next < recipeList.size() - 7) {
            if (i != 0) {
                scrollIndex = next;
                initGui();
            }
        }
        super.handleMouseInput();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private void renderItemIntoGui(ItemStack stack, int x, int y) {
        GlStateManager.pushMatrix();
        IBakedModel bakedmodel = mc.getRenderItem().getItemModelWithOverrides(stack, null, null);
        mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getRenderItem().setupGuiTransform(x, y, bakedmodel.isGui3d());
        bakedmodel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(bakedmodel, ItemCameraTransforms.TransformType.GUI, false);
        this.renderItem(stack, bakedmodel);
        GlStateManager.disableAlpha();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
        GlStateManager.popMatrix();
    }

    public void renderItem(ItemStack stack, IBakedModel model) {
        if (!stack.isEmpty()) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(-0.5F, -0.5F, -0.5F);
            if (model.isBuiltInRenderer()) {
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.enableRescaleNormal();
                stack.getItem().getTileEntityItemStackRenderer().renderByItem(stack);
            } else {
                mc.getRenderItem().renderModel(model, stack);
            }
            GlStateManager.popMatrix();
        }
    }

    public void scrollTo(int index) {
        index = recipeList.size() < 8 ? 0 : Math.min(recipeList.size() - 8, Math.max(0, index));
        this.scrollIndex = index;
        initGui();
    }

    class QuickScroll extends GuiButton {

        private final SmithingTableRecipes.SmithingRecipe recipe;
        private final ItemStack display;

        public QuickScroll(int x, int y, int w, int h, Item reference) {
            super(-1, x, y, w, h, "");
            if(reference == GRPGItems.GUN_PARTS) {
                this.recipe = SmithingTableRecipes.getRecipeByOutput(reference);
                SmithingTableRecipes.SmithingRecipe recipe = SmithingTableRecipes.getRecipeByOutput(GRPGItems.PISTOL);
                this.display = recipe.getOutputForDisplay();
                this.enabled = hasAllSkills(this.recipe.getRequiredTypes(), Minecraft.getMinecraft().player);
            } else {
                this.recipe = SmithingTableRecipes.getRecipeByOutput(reference);
                this.display = recipe.getOutputForDisplay();
                this.enabled = hasAllSkills(recipe.getRequiredTypes(), Minecraft.getMinecraft().player);
            }
        }

        boolean hasAllSkills(SkillType<?>[] types, EntityPlayer player) {
            if (types != null && types.length != 0) {
                PlayerSkills skills = PlayerDataFactory.get(player).getSkills();
                for (SkillType<?> type : types) {
                    if (!skills.hasSkill(type)) {
                        return false;
                    }
                }
            }
            return true;
        }

        @Override
        public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
            boolean flag = super.mousePressed(mc, mouseX, mouseY);
            if(flag) {
                GuiSmithingTable.this.scrollTo(GuiSmithingTable.this.recipeList.indexOf(recipe));
                GuiSmithingTable.this.clicked.map(recipe);
            }
            return flag;
        }

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            if (this.visible) {
                FontRenderer fontrenderer = mc.fontRenderer;
                mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
                if(this.enabled) {
                    GlStateManager.color(0.0F, 1.0F, 0.35F, 1.0F);
                } else {
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                }
                this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
                int i = this.getHoverState(this.hovered);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                this.drawTexturedModalRect(this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
                this.drawTexturedModalRect(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                mc.getRenderItem().renderItemIntoGUI(display, x + 2, y + 2);
            }
        }
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
            if (flag) {
                OptionalObject<SmithingTableRecipes.SmithingRecipe> optional = GuiSmithingTable.this.clicked;
                if(optional.isPresent()) {
                    if(optional.get() == this.recipe) {
                        optional.clear();
                    } else optional.map(recipe);
                } else optional.map(recipe);
            }
            return flag;
        }

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            if (this.visible) {
                FontRenderer fontrenderer = mc.fontRenderer;
                mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
                int i = this.getHoverState(this.hovered);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                this.drawTexturedModalRect(this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
                this.drawTexturedModalRect(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
                mc.getRenderItem().renderItemIntoGUI(output, x + 2, y + 2);
            }
        }
    }
}
