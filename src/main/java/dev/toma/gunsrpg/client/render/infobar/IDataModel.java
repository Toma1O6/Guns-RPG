package dev.toma.gunsrpg.client.render.infobar;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;

public interface IDataModel {

    void addElement(IDataElement element);

    void renderModel(MatrixStack matrix, FontRenderer font, int x, int y, boolean rightSided);
}
