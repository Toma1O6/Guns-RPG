package dev.toma.gunsrpg.client.screen.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.configuration.api.client.widget.ITickable;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.StringTextComponent;

import java.util.Objects;

import static dev.toma.gunsrpg.util.RenderUtils.red_i;
import static dev.toma.gunsrpg.util.RenderUtils.green_i;
import static dev.toma.gunsrpg.util.RenderUtils.blue_i;

public class ViewSwitchWidget extends Widget implements ITickable {

    private static final ResourceLocation BACKGROUND = GunsRPG.makeResource("textures/screen/switch_button.png");
    private final ResourceLocation icon;
    private Runnable clickEvent = () -> {};
    private int hoverTimer;

    private final int primary = 0x49A1FF;
    private final int red;
    private final int green;
    private final int blue;

    public ViewSwitchWidget(int x, int y, int width, int height, ResourceLocation icon) {
        super(x, y, width, height, StringTextComponent.EMPTY);
        this.icon = icon;
        int secondary = 0x49D8FF;
        this.red = red_i(secondary) - red_i(primary);
        this.green = green_i(secondary) - green_i(primary);
        this.blue = blue_i(secondary) - blue_i(primary);
    }

    public void setClickEvent(Runnable event) {
        this.clickEvent = Objects.requireNonNull(event);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        clickEvent.run();
    }

    @Override
    public void tick() {
        if (hoverTimer > 0) {
            --hoverTimer;
        }
    }

    @Override
    public void renderButton(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        int timer = 8;
        int scale = 2;
        int cropSize = 8;
        if (isHovered) {
            hoverTimer = timer;
            scale -= 2;
            cropSize -= 1;
        }
        float hoverProgress = hoverTimer / (float) timer;
        int red = (int) (this.red * hoverProgress);
        int green = (int) (this.green * hoverProgress);
        int blue = (int) (this.blue * hoverProgress);
        int baseRed = red_i(primary);
        int baseGreen = green_i(primary);
        int baseBlue = blue_i(primary);
        int color = (0xFF << 24) | ((baseRed + red) << 16) | ((baseGreen + green) << 8) | (baseBlue + blue);
        TextureManager manager = Minecraft.getInstance().getTextureManager();
        Matrix4f pose = matrix.last().pose();
        manager.bind(BACKGROUND);
        RenderUtils.drawColoredTex(pose, x + scale, y + scale, x + width - scale, y + height - scale, color);
        manager.bind(icon);
        RenderUtils.drawTex(pose, x + cropSize, y + cropSize, x + width - cropSize, y + height - cropSize);
    }
}
