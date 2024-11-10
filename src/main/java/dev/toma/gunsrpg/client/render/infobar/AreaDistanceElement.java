package dev.toma.gunsrpg.client.render.infobar;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.toma.gunsrpg.common.quests.quest.AbstractAreaBasedQuest;
import dev.toma.gunsrpg.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;

import java.util.function.Function;

public class AreaDistanceElement<T extends AbstractAreaBasedQuest<?>> extends DataSourcedElement<T> {

    private final ITextComponent title;
    private final Function<T, ITextComponent> distanceFunction;
    private final Function<T, BlockPos> centerGetter;
    private int width;

    public AreaDistanceElement(T dataSource, ITextComponent title, Function<T, ITextComponent> distanceFunction, Function<T, BlockPos> centerGetter) {
        super(dataSource);
        this.title = title;
        this.distanceFunction = distanceFunction;
        this.centerGetter = centerGetter;
    }

    @Override
    public void draw(MatrixStack matrix, FontRenderer font, int x, int y, int width, int height) {
        calculateDimensions(font);
        font.draw(matrix, title, x, y, 0xFFFFFF);
        T src = this.getDataSource();
        ITextComponent col2 = distanceFunction.apply(src);
        int col2Width = font.width(col2);
        font.draw(matrix, col2, x + width - col2Width - 16, y, 0xFFFFFF);
        int compassLeft = x + width - 13;
        Matrix4f pose = matrix.last().pose();
        RenderUtils.drawSolid(pose, compassLeft, y - 1, compassLeft + 10, y + 9, 0x66 << 24);
        PlayerEntity player = Minecraft.getInstance().player;
        BlockPos center = centerGetter.apply(src);
        double orientation = this.getAngleTo(Vector3d.atCenterOf(center), player) / (Math.PI * 2.0);
        double playerOrientation = MathHelper.positiveModulo(player.yRot / 360.0, 1.0);
        float result = (MathHelper.positiveModulo((float) (0.5 - (playerOrientation - 0.25 - orientation)), 1.0f));
        float rotation = (float) (result * 2.0F * Math.PI);
        float sin = MathHelper.sin(rotation);
        float cos = MathHelper.cos(rotation);
        RenderSystem.lineWidth(1.0F);
        RenderUtils.drawLine(pose, compassLeft + 5, y, compassLeft + 5, y + 8, 0xFF888888);
        RenderUtils.drawLine(pose, compassLeft + 1, y + 4, compassLeft + 9, y + 4, 0xFF888888);
        RenderSystem.lineWidth(3.0F);
        RenderUtils.drawLine(pose, compassLeft + 5.0F, y + 4.0F, compassLeft + 5.0F + (3.0F * sin), y + 4.0F + (3.0F * -cos), 0xFFFF0000, 0xFFFF0000);
        RenderUtils.drawLine(pose, compassLeft + 5.0F, y + 4.0F, compassLeft + 5.0F + (3.0F * -sin), y + 4.0F + (3.0F * cos), 0xFF3388FF, 0xFF3388FF);
    }

    @Override
    public void recalculate(FontRenderer font, int width, int height) {
        this.calculateDimensions(font);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return 10;
    }

    private void calculateDimensions(FontRenderer font) {
        T source = this.getDataSource();
        ITextComponent column2 = distanceFunction.apply(source);
        int leftWidth = font.width(title);
        int spacing = 28;
        int rightWidth = font.width(column2);
        this.width = leftWidth + spacing + rightWidth;
    }

    private double getAngleTo(Vector3d vector, Entity entity) {
        return Math.atan2(vector.z() - entity.getZ(), vector.x() - entity.getX());
    }
}
