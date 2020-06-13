package dev.toma.gunsrpg.client.animation.builder.component;

import net.minecraft.client.gui.FontRenderer;

public class PlainTextComponent extends UIComponent {

    public PlainTextComponent(int x, int y, String text) {
        super(x, y, 0, 0, text);
    }

    @Override
    public void draw(FontRenderer renderer) {
        renderer.drawStringWithShadow(name.or("???"), x, y + 6, 0xffffff);
    }

    @Override
    public boolean isMouseOver(int mouseX, int mouseY) {
        return false;
    }

    public void setName(String text) {
        this.name.map(text);
    }
}
