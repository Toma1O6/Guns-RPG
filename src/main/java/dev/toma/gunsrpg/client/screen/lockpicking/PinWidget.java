package dev.toma.gunsrpg.client.screen.lockpicking;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class PinWidget extends AbstractButton {

    private static final ResourceLocation SHEET = GunsRPG.makeResource("textures/screen/lockpicking.png");

    private final int index;
    private final PinPressHandler handler;

    public PinWidget(int x, int y, int width, int height, int index, PinPressHandler handler) {
        super(x, y, width, height, new StringTextComponent(String.valueOf(index)));
        this.index = index;
        this.handler = handler;
    }

    @Override
    public void renderButton(MatrixStack matrix, int mouseX, int mouseY, float renderDelta) {
        int textureIndex = this.active ? this.isHovered ? 2 : 1 : 0;
        float texStep = 1.0F / 3;
        float v1 = textureIndex * texStep;
        float v2 = v1 + texStep;
        Minecraft client = Minecraft.getInstance();
        FontRenderer font = client.font;
        ITextComponent title = this.getMessage();
        TextureManager manager = client.textureManager;
        manager.bind(SHEET);
        RenderUtils.drawTex(matrix.last().pose(), this.x, this.y, this.x + this.width, this.y + this.height, 0.0F, v1, 1.0F, v2);
        font.drawShadow(matrix, title, this.x + (this.width - font.width(title)) / 2.0F, 1 + this.y + (this.height - font.lineHeight) / 2.0F, 0xFFFFFF);
    }

    @Override
    public void onPress() {
        this.handler.onPress(this.index);
    }

    @FunctionalInterface
    public interface PinPressHandler {
        void onPress(int index);
    }
}
