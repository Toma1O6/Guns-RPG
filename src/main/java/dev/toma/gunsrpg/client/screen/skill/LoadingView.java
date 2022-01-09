package dev.toma.gunsrpg.client.screen.skill;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.client.screen.widgets.LoadingWidget;
import net.minecraft.client.MainWindow;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.util.function.BooleanSupplier;

public class LoadingView extends View {

    private final ITextComponent text;
    private final BooleanSupplier loadingCheck;
    private final Runnable correctionEvent;
    private int timeBeforeNextCheck = 50;

    public LoadingView(MainWindow window, BooleanSupplier loadingCheck, Runnable correctionEvent) {
        super(window.getGuiScaledWidth(), window.getGuiScaledHeight(), null);
        this.text = new StringTextComponent("Loading data");
        this.loadingCheck = loadingCheck;
        this.correctionEvent = correctionEvent;
    }

    @Override
    public void tick() {
        super.tick();
        if (--timeBeforeNextCheck <= 0) {
            timeBeforeNextCheck = 50;
            if (loadingCheck.getAsBoolean()) {
                correctionEvent.run();
            }
        }
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
