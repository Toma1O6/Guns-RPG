package dev.toma.gunsrpg.client.render.infobar;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.common.quests.quest.DisplayInfo;
import dev.toma.gunsrpg.common.quests.quest.Quest;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public class QuestInfoElement extends DataSourcedElement<Quest<?>> {

    private final boolean showObjective;
    private final ITextComponent title;
    private final ITextComponent objective;
    private int width;
    private int height;

    public QuestInfoElement(Quest<?> quest, boolean showObjective) {
        super(quest);
        this.showObjective = showObjective;

        DisplayInfo info = quest.getScheme().getDisplayInfo();
        this.title = info.getName().withStyle(TextFormatting.YELLOW, TextFormatting.BOLD);
        this.objective = info.getInfo();
    }

    @Override
    public void draw(MatrixStack matrix, FontRenderer font, int x, int y, int width, int height) {
        calculateWidthAndHeight(font);
        font.draw(matrix, title, x, y, 0xFFFFFF);
        if (showObjective) {
            font.draw(matrix, objective, x, y + 10, 0xFFFFFF);
        }
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    private void calculateWidthAndHeight(FontRenderer font) {
        int width = font.width(title);
        int height = 10;
        if (showObjective) {
            width = Math.max(width, font.width(objective));
            height *= 2;
        }
        this.width = width;
        this.height = height;
    }
}
