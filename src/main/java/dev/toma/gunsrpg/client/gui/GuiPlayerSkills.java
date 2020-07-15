package dev.toma.gunsrpg.client.gui;

import dev.toma.gunsrpg.common.skills.core.SkillCategory;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

public class GuiPlayerSkills extends GuiScreen {

    private final List<Component> uiComponents = new ArrayList<>();
    private SkillCategory displayedCategory = SkillCategory.GUN;
    private String headerText;
    private int headerWidth;

    @Override
    public void initGui() {
        uiComponents.clear();
        headerText = String.format("%s's skill tree", mc.player.getName());
        headerWidth = mc.fontRenderer.getStringWidth(headerText);
        this.placeCategoryButtons();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.color(1F, 1F, 1F, 1F);
        drawBackground(0);
        FontRenderer renderer = mc.fontRenderer;

        // HEADER
        ModUtils.renderColor(0, 0, width, 25, 0, 0, 0, 0.4f);
        renderer.drawStringWithShadow(TextFormatting.UNDERLINE + headerText, (width - headerWidth) / 2f, 8, 0xFFFFFFFF);
        for(Component component : uiComponents) {
            component.draw(mc, mouseX, mouseY, partialTicks);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(mouseButton != 0) return;
        for(Component component : uiComponents) {
            if(component.isMouseOver(mouseX, mouseY) && component.onClick()) {
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                initGui();
                break;
            }
        }
    }

    private void placeCategoryButtons() {
        SkillCategory[] categories = SkillCategory.mainCategories();
        int btnWidth = width / categories.length;
        int missedPixels = width - ((categories.length) * btnWidth);
        for (int i = 0; i < categories.length; i++) {
            int px = i * btnWidth;
            boolean last = i == categories.length - 1;
            addComponent(new CategoryComponent(px, 25, last ? btnWidth + missedPixels : btnWidth, 20, categories[i]));
        }
    }

    private void addComponent(Component component) {
        this.uiComponents.add(component);
    }

    private static class Component {
        protected final int x, y, w, h;
        protected boolean hovered;

        public Component(int x, int y, int w, int h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

        public void draw(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            hovered = isMouseOver(mouseX, mouseY);
            ModUtils.renderColor(x, y, x + w, y + h, 0.0F, 0.0F, 0.0F, 0.4F);
            if (hovered) ModUtils.renderColor(x, y, x + w, y + h, 1.0F, 1.0F, 1.0F, 0.4F);
        }

        public boolean isMouseOver(int mouseX, int mouseY) {
            return mouseX >= this.x && mouseX <= this.x + this.w && mouseY >= this.y && mouseY <= this.y + this.h;
        }

        public boolean onClick() {
            return false;
        }

        public void drawCenteredString(FontRenderer renderer, String text, int x, int y, int color) {
            renderer.drawStringWithShadow(text, (float) (x - renderer.getStringWidth(text) / 2), (float) y, color);
        }
    }

    private class CategoryComponent extends Component {
        private final SkillCategory category;
        private final boolean selected;

        public CategoryComponent(int x, int y, int w, int h, SkillCategory category) {
            super(x, y, w, h);
            this.category = category;
            this.selected = GuiPlayerSkills.this.displayedCategory == category;
        }

        @Override
        public void draw(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            super.draw(mc, mouseX, mouseY, partialTicks);
            if(selected) {
                ModUtils.renderColor(x, y, x + 2, y + h, 1.0F, 1.0F, 1.0F, 1.0F);
                ModUtils.renderColor(x, y + h - 2, x + w, y + h, 1.0F, 1.0F, 1.0F, 1.0F);
                ModUtils.renderColor(x + w - 2, y + h, x + w, y, 1.0F, 1.0F, 1.0F, 1.0F);
            }
            this.drawCenteredString(mc.fontRenderer, this.category.name(), this.x + this.w / 2, this.y + (this.h - 8) / 2, hovered ? 0xffff88 : 0xffffff);
        }

        @Override
        public boolean onClick() {
            if(GuiPlayerSkills.this.displayedCategory != this.category) {
                GuiPlayerSkills.this.displayedCategory = category;
                return true;
            }
            return false;
        }
    }
}
