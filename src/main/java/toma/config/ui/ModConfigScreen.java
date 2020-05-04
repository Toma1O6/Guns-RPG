package toma.config.ui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.common.ModContainer;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import toma.config.datatypes.ConfigObject;
import toma.config.datatypes.IConfigType;
import toma.config.ui.widget.ActionButton;
import toma.config.ui.widget.ConfigWidget;
import toma.config.util.ConfigUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModConfigScreen extends GuiScreen {

    private ConfigObject object;
    private Map<String, IConfigType<?>> valueMap;
    private ModContainer info;
    private final GuiScreen parent;

    private long lastTime;
    private int animationTick;

    private List<IConfigType<?>> typeList = new ArrayList<>();
    private List<ConfigWidget<?>> renderList = new ArrayList<>();
    private int index;
    private int entryCount;

    public ModConfigScreen(Map<String, IConfigType<?>> valueMap, GuiScreen parent) {
        this.valueMap = valueMap;
        this.parent = parent;
    }

    public ModConfigScreen(ConfigObject object, ModContainer modInfo, GuiScreen parent) {
        this(object.value(), parent);
        this.object = object;
        this.info = modInfo;
    }

    @Override
    public void onGuiClosed() {
    }

    @Override
    public void initGui() {
        this.typeList.clear();
        this.renderList.clear();
        entryCount = (height - 20) / 30 + 1;
        typeList.addAll(valueMap.values());
        for(int i = index; i < index + entryCount; i++) {
            if(i >= typeList.size()) break;
            ConfigWidget<?> widget = typeList.get(i).create(30, 10 + (i - index) * 25, width - 60, 20, this);
            renderList.add(widget);
            widget.init();
        }
        addButton(new ActionButton(0, 30, height - 30, 100, 20, "Back", btn -> {
            if(this.object != null) {
                this.object.set(valueMap);
                if(!(parent instanceof ModConfigScreen)) {
                    ConfigUtils.c_saveConfig(info.getModId(), this.object);
                }
            }
            mc.displayGuiScreen(this.parent);
        }));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if(button instanceof ActionButton) {
            ActionButton actionButton = (ActionButton) button;
            actionButton.executeAction(actionButton);
        }
    }

    @Override
    public void mouseClicked(int p_mouseClicked_1_, int p_mouseClicked_3_, int p_mouseClicked_5_) throws IOException {
        for(ConfigWidget<?> widget : renderList) {
            if(widget.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_)) {
                widget.playPressSound(mc.getSoundHandler());
                break;
            }
        }
        for(GuiButton button : buttonList) {
            if(button.mousePressed(mc, p_mouseClicked_1_, p_mouseClicked_3_)) {
                button.playPressSound(mc.getSoundHandler());
                actionPerformed(button);
                break;
            }
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        int diff = Integer.signum(Mouse.getEventDWheel());
        int pos = Integer.compare(0, diff);
        int prev = index;
        int next = index + pos;
        if(next >= 0 && next <= typeList.size() - entryCount) {
            if (prev != next) {
                index = next;
                initGui();
            }
        }
        super.handleMouseInput();
    }

    @Override
    public void keyTyped(char character, int code) {
        renderList.forEach(w -> w.onKeyPress(character, code));
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        renderList.forEach(e -> e.mouseDragged(mc, mouseX, mouseY));
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    public void drawScreen(int p_render_1_, int p_render_2_, float p_render_3_) {
        drawBackground(0);
        for(ConfigWidget<?> widget : this.renderList) {
            widget.drawButton(mc, p_render_1_, p_render_2_, p_render_3_);
        }
        long time = System.currentTimeMillis();
        float diff = 1200.0F;
        long l = time - lastTime;
        if(l > diff) lastTime = time;
        float f = (float) Math.cos(Math.toRadians(-90 + 180 * (l / diff)));
        if(index < typeList.size() - entryCount) drawArrow(width - 18, entryCount * 25 - 12 + 7 * f, 10, 10, false);
        if(typeList.size() > entryCount) {
            int height = 25 * (entryCount - 2) - 5;
            float perStep = height / (float) typeList.size();
            float start = 35 + index * perStep;
            float end = entryCount * perStep;
            drawColorShape(width - 23, 35, 16, height, 0f, 0f, 0f, 1.0F);
            drawColorShape(width - 23, start, 16, end, 1.0F, 1.0F, 1.0F, 1.0F);
        }
        if(index > 0) drawArrow(width - 18, 20 + 7 * -f, 10, 10, true);
        super.drawScreen(p_render_1_, p_render_2_, p_render_3_);
    }

    private void drawArrow(int x, float y, int width, int height, boolean invert) {
        Tessellator tessellator = Tessellator.getInstance();
        GlStateManager.color(1f, 1f, 1f);
        double d = width / 3d;
        BufferBuilder builder = tessellator.getBuffer();
        GlStateManager.disableTexture2D();
        builder.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
        GlStateManager.glLineWidth(5);
        if(invert) {
            builder.pos(x, y + height, 0).color(1f, 1f, 1f, 1f).endVertex();
            builder.pos(d + x, y, 0).color(1f, 1f, 1f, 1f).endVertex();
            builder.pos(d + x, y, 0).color(1f, 1f, 1f, 1f).endVertex();
            builder.pos(2*d + x, y + height, 0).color(1f, 1f, 1f, 1f).endVertex();
        } else {
            builder.pos(x, y, 0).color(1f, 1f, 1f, 1f).endVertex();
            builder.pos(d + x, y + height, 0).color(1f, 1f, 1f, 1f).endVertex();
            builder.pos(d + x, y + height, 0).color(1f, 1f, 1f, 1f).endVertex();
            builder.pos(2*d + x, y, 0).color(1f, 1f, 1f, 1f).endVertex();
        }
        tessellator.draw();
        GlStateManager.glLineWidth(1);
        GlStateManager.enableTexture2D();
    }

    private void drawColorShape(int x, float y, int w, float h, float r, float g, float b, float a) {
        GlStateManager.color(1f, 1f, 1f);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        builder.pos(x, y+h, 0).color(r, g, b, a).endVertex();
        builder.pos(x+w, y+h, 0).color(r, g, b, a).endVertex();
        builder.pos(x+w, y, 0).color(r, g, b, a).endVertex();
        builder.pos(x, y, 0).color(r, g, b, a).endVertex();
        tessellator.draw();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
    }
}
