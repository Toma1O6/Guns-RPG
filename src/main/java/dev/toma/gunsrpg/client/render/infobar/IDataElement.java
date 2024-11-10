package dev.toma.gunsrpg.client.render.infobar;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.util.math.IDimensions;
import net.minecraft.client.gui.FontRenderer;

public interface IDataElement extends IDimensions {

    void recalculate(FontRenderer font, int width, int height);

    void draw(MatrixStack matrix, FontRenderer font, int x, int y, int width, int height);
}
