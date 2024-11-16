package dev.toma.gunsrpg.api.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.util.math.IVec2i;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.PlayerEntity;

public interface IProgressionDetailProvider {

    void draw(MatrixStack matrix, FontRenderer font, IVec2i position, int xOffset, int yOffset, int width, int height, PlayerEntity player, IPlayerData data);

    int getWidth(FontRenderer font, PlayerEntity player, IPlayerData data);

    int getHeight(FontRenderer font, PlayerEntity player, IPlayerData data);
}
