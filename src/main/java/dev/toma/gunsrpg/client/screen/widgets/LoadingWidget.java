package dev.toma.gunsrpg.client.screen.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.configuration.api.client.widget.ITickable;
import dev.toma.gunsrpg.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.settings.GraphicsFanciness;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class LoadingWidget extends Widget implements ITickable {

    private final boolean animated;
    private final ITextComponent text;
    private int animationCounter;
    private int dotCount;

    public LoadingWidget(int x, int y, int width, ITextComponent text) {
        this(x, y, width, text, RenderUtils.hasGraphicsMode(GraphicsFanciness.FANCY));
    }

    public LoadingWidget(int x, int y, int width, ITextComponent text, boolean animated) {
        super(x, y, width, animated ? 30 : 12, text);
        this.animated = animated;
        this.text = text;
        setMessage(text);
    }

    @Override
    public void tick() {
        if (animated && ++animationCounter % 10 == 0) {
            dotCount = ++dotCount % 4;
            updateText();
        }
    }

    @Override
    public void renderButton(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        FontRenderer fontRenderer = Minecraft.getInstance().font;
        fontRenderer.draw(matrix, getMessage(), x, y + height - 12, 0xFFFFFF);
    }

    private void updateText() {
        String title = text.getString();
        StringBuilder builder = new StringBuilder();
        builder.append(title);
        for (int i = 0; i < dotCount; i++) {
            builder.append(".");
        }
        setMessage(new StringTextComponent(builder.toString()));
    }
}
