package dev.toma.gunsrpg.client.animation.builder.component;

import dev.toma.gunsrpg.client.animation.builder.BuilderData;
import dev.toma.gunsrpg.client.animation.builder.GuiAnimationBuilder;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.client.gui.FontRenderer;

public class InputComponent extends UIComponent {

    private final GuiAnimationBuilder parent;
    private final int index;

    public InputComponent(GuiAnimationBuilder parent, int index, int x, int y, int w, int h, int entryValue) {
        super(x, y, w, h, entryValue + "");
        this.parent = parent;
        this.index = index;
    }

    @Override
    public void draw(FontRenderer renderer) {
        boolean selected = parent.selectedInput.get() == this;
        if(selected) {
            ModUtils.renderColor(x, y, x + w, y + h, 0.0F, 0.7F, 0.0F, 1.0F);
        } else {
            ModUtils.renderColor(x, y, x + w, y + h, 1.0F, 1.0F, 1.0F, 1.0F);
        }
        ModUtils.renderColor(x + 1, y + 1, x + w - 1, y + h - 1, 0.0F, 0.0F, 0.0F, 1.0F);
        String s = name.get();
        int tx = renderer.getStringWidth(s) / 2;
        renderer.drawStringWithShadow(s, x + w / 2f - tx, y + 6, 0xffffff);
    }

    public void keyPressed(char character, int code) {
        if(Character.isDigit(character) && name.get().length() < 3) {
            name.map(name.get() + character);
        } else if(character == '\b') {
            String t = name.get();
            if(!t.isEmpty()) t = t.substring(0, t.length() - 1);
            name.map(t);
        }
        try {
            String text = name.get();
            int value = text.isEmpty() ? 1 : Integer.parseInt(name.get());
            BuilderData.steps.get(this.index).length = value / 100.0F;
        } catch (NumberFormatException ex) {
            // do nothing as we don't really care
        }
    }
}
