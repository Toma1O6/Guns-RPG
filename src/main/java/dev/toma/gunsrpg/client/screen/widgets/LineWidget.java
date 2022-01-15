package dev.toma.gunsrpg.client.screen.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.toma.gunsrpg.client.render.IOrderedRender;
import dev.toma.gunsrpg.util.RenderUtils;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.StringTextComponent;

public class LineWidget extends Widget implements IOrderedRender {

    private final int initialX;
    private final int initialY;
    private final int x2;
    private final int y2;

    public LineWidget(int x1, int y1, int x2, int y2) {
        super(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2), StringTextComponent.EMPTY);
        this.initialX = x;
        this.initialY = y;
        this.x2 = Math.max(x1, x2);
        this.y2 = Math.max(y1, y2);
    }

    @Override
    public void renderButton(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        int diffX = x - initialX;
        int diffY = y - initialY;
        RenderSystem.lineWidth(3);
        RenderUtils.drawLine(matrix.last().pose(), x, y, x2 + diffX, y2 + diffY, 0xFF777777);
        RenderSystem.lineWidth(1);
    }

    @Override
    public int getRenderIndex() {
        return -1;
    }
}
