package toma.config.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import org.lwjgl.input.Mouse;
import toma.config.Config;
import toma.config.datatypes.ConfigObject;
import toma.config.ui.widget.ActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ModListScreen extends GuiScreen {

    private GuiScreen parent;
    public int displayedEntryAmount;
    public List<ModContainer> mods;
    public ModContainer[] displayList;
    private ModDisplayInfo compiledDisplayData;
    private ModContainer selected = null;
    private GuiButton config;
    private int modIdx = 0;
    private int infoIdx = 0;

    public ModListScreen(GuiScreen parent) {
        this.parent = parent;
        mods = Loader.instance().getModList();
    }

    private static void drawRect(int x, int y, int x2, int y2, float r, float g, float b, float a) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        builder.pos(x, y2, 0).color(r, g, b, a).endVertex();
        builder.pos(x2, y2, 0).color(r, g, b, a).endVertex();
        builder.pos(x2, y, 0).color(r, g, b, a).endVertex();
        builder.pos(x, y, 0).color(r, g, b, a).endVertex();
        tessellator.draw();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(keyCode == 1) {
            mc.displayGuiScreen(this.parent);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if(button instanceof ActionButton) {
            ((ActionButton) button).executeAction(((ActionButton) button));
        }
    }

    @Override
    public void drawScreen(int p_render_1_, int p_render_2_, float p_render_3_) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawBackground(0);
        drawRect(20, 20, 150, 20 + displayedEntryAmount * 20, 0, 0, 0, 0.5F);
        if (displayList == null) return;
        if (p_render_1_ >= 20 && p_render_1_ <= 150 && p_render_2_ >= 20 && p_render_2_ < 20 + displayedEntryAmount * 20) {
            int entry = (p_render_2_ - 20) / 20;
            if (displayList[entry] != null) {
                drawRect(20, 20 + entry * 20, 150, 20 + (entry + 1) * 20, 1.0F, 1.0F, 1.0F, 0.25F);
            }
        }
        if (selected != null) {
            for (int i = 0; i < displayList.length; i++) {
                if (displayList[i] != null && displayList[i].getModId().equals(selected.getModId())) {
                    drawRect(20, 20 + i * 20, 150, 20 + (i + 1) * 20, 1.0F, 1.0F, 1.0F, 0.4F);
                    break;
                }
            }
        }
        if (mods.size() > displayedEntryAmount) {
            drawRect(140, 20, 150, 20 + displayedEntryAmount * 20, 0, 0, 0, 1.0F);
            int steps = mods.size();
            double stepSize = displayedEntryAmount * 20 / 20.0D;
            drawRect(140, (int) (20 + modIdx * stepSize), 150, (int) (20 + modIdx * stepSize) + (int) (displayedEntryAmount * stepSize), 1.0f, 1.0f, 1.0f, 1.0f);
        }
        for (int i = 0; i < displayList.length; i++) {
            ModContainer info = displayList[i];
            if (info == null) break;
            mc.fontRenderer.drawStringWithShadow(info.getName(), 23, 25 + i * 20, 0xFFFFFFFF);
        }
        drawRect(170, 20, width - 20, 25 + ((displayedEntryAmount - 1) * 20), 0, 0, 0, 0.5F);
        if (compiledDisplayData != null) {
            int y = 25;
            int lines = ((displayedEntryAmount - 1) * 20) / 10;
            for (int i = infoIdx; i < infoIdx + lines; i++) {
                if (i >= compiledDisplayData.list.size()) break;
                ITextComponent line = compiledDisplayData.list.get(i);
                if (line != null) {
                    GlStateManager.enableBlend();
                    mc.fontRenderer.drawStringWithShadow(line.getFormattedText(), 174, y, 0xFFFFFF);
                    GlStateManager.disableAlpha();
                    GlStateManager.disableBlend();
                }
                y += 10;
            }
            int steps = compiledDisplayData.list.size();
            if (steps > lines) {
                drawRect(width - 30, 20, width - 20, 25 + lines * 10, 0, 0, 0, 1.0F);
                double stepSize = lines * 10.0D / steps;
                drawRect(width - 30, 20 + (int) (infoIdx * stepSize), width - 20, 25 + (int) ((infoIdx + lines + 1) * stepSize), 1.0f, 1.0f, 1.0f, 1.0f);
            }
        }
        super.drawScreen(p_render_1_, p_render_2_, p_render_3_);
    }

    @Override
    public void mouseClicked(int p_mouseClicked_1_, int p_mouseClicked_3_, int p_mouseClicked_5_) throws IOException {
        if (p_mouseClicked_5_ == 0) {
            if (p_mouseClicked_1_ >= 20 && p_mouseClicked_1_ <= 150 && p_mouseClicked_3_ >= 20 && p_mouseClicked_3_ < 20 + displayedEntryAmount * 20) {
                int entry = (int) (p_mouseClicked_3_ - 20) / 20;
                ModContainer info = displayList[entry];
                if (info != null) {
                    this.selected = info;
                    this.compiledDisplayData = ModDisplayInfo.compileFrom(info, width - 205);
                    config.enabled = selected != null && (Config.configs().get(selected.getModId()) != null || FMLClientHandler.instance().getGuiFactoryFor(selected) != null);
                    config.displayString = (config.enabled ? TextFormatting.GREEN + "Config" : TextFormatting.RED + "Config");
                }
            }
        }
        super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
    }

    @Override
    public void handleMouseInput() throws IOException {
        int i = Integer.signum(Mouse.getEventDWheel());
        int diff = Integer.compare(0, i);
        int p_mouseScrolled_1_ = Mouse.getX();
        int p_mouseScrolled_3_ = Mouse.getY();
        if (p_mouseScrolled_1_ >= 20 && p_mouseScrolled_3_ >= 20 && p_mouseScrolled_1_ <= 150 && p_mouseScrolled_3_ <= 20 + displayedEntryAmount * 20) {
            // on scroll in mod list
            if ((modIdx > 0 && diff < 0) || (modIdx < mods.size() - displayedEntryAmount && diff > 0)) {
                modIdx += diff;
                initGui();
            }
        } else if (p_mouseScrolled_1_ >= 170 && p_mouseScrolled_3_ >= 20 && p_mouseScrolled_1_ <= width - 20 && p_mouseScrolled_3_ <= 20 + ((displayedEntryAmount - 1) * 20)) {
            // on scroll in mod info
            if (compiledDisplayData == null) return;
            int h = ((displayedEntryAmount - 1) * 20) / mc.fontRenderer.FONT_HEIGHT;
            if ((infoIdx > 0 && diff < 0) || (infoIdx < compiledDisplayData.list.size() - h + 1 && diff > 0)) {
                infoIdx += diff;
            }
        }
        super.handleMouseInput();
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        displayedEntryAmount = (height - 40) / 20;
        updateDisplayList();
        int w = (width - 220) / 3;
        addButton(new ActionButton(0, 170, 10 + displayedEntryAmount * 20, w + 5, 20, "Back", b -> mc.displayGuiScreen(this.parent)));
        addButton(config = new ActionButton(0, 185 + w, 10 + displayedEntryAmount * 20, w + 5, 20, TextFormatting.GREEN + "Config", b -> openConfig()));
        config.enabled = selected != null && (Config.configs().get(selected.getModId()) != null || FMLClientHandler.instance().getGuiFactoryFor(selected) != null);
        config.displayString = (config.enabled ? TextFormatting.GREEN + "Config" : TextFormatting.RED + "Config");
        if (selected != null) {
            compiledDisplayData = ModDisplayInfo.compileFrom(selected, width - 205);
        }
    }

    protected void openConfig() {
        ConfigObject object = Config.configs().get(selected.getModId());
        if (object != null) {
            mc.displayGuiScreen(new ModConfigScreen(object, selected, this));
        } else {
            IModGuiFactory factory = FMLClientHandler.instance().getGuiFactoryFor(selected);
            if(factory != null) {
                mc.displayGuiScreen(factory.createConfigGui(this));
            }
        }
    }

    private void updateDisplayList() {
        displayList = new ModContainer[displayedEntryAmount];
        for (int i = modIdx; i < modIdx + displayedEntryAmount; i++) {
            if (i >= mods.size()) break;
            int index = i - modIdx;
            displayList[index] = mods.get(i);
        }

    }

    private static class ModDisplayInfo {

        private final List<ITextComponent> list;

        private ModDisplayInfo(List<ITextComponent> list) {
            this.list = list;
        }

        private static ModDisplayInfo compileFrom(ModContainer info, int fieldWidth) {
            List<String> lines = new ArrayList<>();
            lines.add(TextFormatting.BOLD + info.getName());
            lines.add("ID: " + info.getModId());
            lines.add("Authors: " + info.getMetadata().getAuthorList());
            if(!info.getMetadata().credits.isEmpty()) lines.add("Credits: " + info.getMetadata().credits);
            lines.add("URL: " + info.getMetadata().url);
            ForgeVersion.CheckResult result = ForgeVersion.getResult(info);
            if (result.status == ForgeVersion.Status.OUTDATED || result.status == ForgeVersion.Status.BETA_OUTDATED) {
                lines.add("Update Available: " + (result.url != null ? result.url : "unknown URL!"));
            }
            lines.add("");
            lines.add(info.getMetadata().description);
            List<ITextComponent> list = new ArrayList<>();
            for (String line : lines) {
                if (line == null) {
                    list.add(null);
                    continue;
                }
                ITextComponent component = ForgeHooks.newChatWithLinks(line, false);
                list.addAll(GuiUtilRenderComponents.splitText(component, fieldWidth, Minecraft.getMinecraft().fontRenderer, false, true));
            }
            return new ModDisplayInfo(list);
        }

        private List<ITextComponent> getList() {
            return list;
        }
    }
}
