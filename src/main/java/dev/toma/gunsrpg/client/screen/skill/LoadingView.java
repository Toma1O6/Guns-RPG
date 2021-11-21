package dev.toma.gunsrpg.client.screen.skill;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.client.screen.widgets.LoadingWidget;
import net.minecraft.client.MainWindow;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class LoadingView extends View {

    private final ITextComponent text;

    public LoadingView(MainWindow window) {
        super(window.getGuiScaledWidth(), window.getGuiScaledHeight(), null);
        this.text = new StringTextComponent("Loading data");
    }

    @Override
    protected void init() {
        int centerX = width / 2;
        int centerY = height / 2 - 20;
        int titleWidth = font.width(text);
        addWidget(new LoadingWidget(centerX - titleWidth / 2, centerY, titleWidth, text));
    }

    @Override
    protected void renderView(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {

    }
}
