package dev.toma.gunsrpg.client.render.infobar;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;

public interface IDataModel {

    void addElement(IDataElement element);

    void prepare(MatrixStack matrix, FontRenderer font);

    void renderModel(MatrixStack matrix, FontRenderer font, int x, int y, boolean renderBackground);

    int getModelWidth();

    int getModelHeight();
}
