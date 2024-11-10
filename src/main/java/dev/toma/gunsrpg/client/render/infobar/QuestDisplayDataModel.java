package dev.toma.gunsrpg.client.render.infobar;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.common.quests.condition.NoConditionProvider;
import dev.toma.gunsrpg.common.quests.quest.Quest;
import dev.toma.gunsrpg.util.RenderUtils;
import dev.toma.gunsrpg.util.math.IDimensions;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.ITextComponent;

import java.util.*;
import java.util.function.Function;

public class QuestDisplayDataModel implements IDataModel {

    private final List<IDataElement> list = new ArrayList<>();
    private final UUID clientId;
    private boolean resized;
    private int width;
    private int height;

    public QuestDisplayDataModel(UUID clientId) {
        this.clientId = clientId;
    }

    public void addQuestHeader(Quest<?> quest) {
        addQuestHeader(quest, true);
    }

    public void addQuestHeader(Quest<?> quest, boolean objective) {
        addElement(new QuestInfoElement(quest, objective));
    }

    public <Q extends Quest<?>> void addInformationRow(ITextComponent title, Q quest, Function<Q, ITextComponent> provider) {
        addElement(new DataRowElement<>(quest, title, provider));
    }

    public void addConditionDisplay(Quest<?> quest) {
        Arrays.stream(quest.getConditions())
                .filter(condition -> condition != NoConditionProvider.NO_CONDITION)
                .sorted(Comparator.comparing(condition -> condition.getProviderType().getType().getId()))
                .forEach(condition -> addElement(new TextElement(condition.getDescriptor(true))));
    }

    public UUID getClientId() {
        return clientId;
    }

    @Override
    public void addElement(IDataElement element) {
        list.add(element);
        resized = true;
    }

    @Override
    public void renderModel(MatrixStack matrix, FontRenderer font, int x, int y, boolean rightSided, boolean renderBackground) {
        if (resized) {
            // redraw so all dimension properties are recalculated before proper draw call
            for (IDataElement element : list) {
                element.draw(matrix, font, x, y, width, height);
            }
            width = getWidth();
            height = getHeight();
            resized = false;
        }
        if (rightSided) {
            x -= width + 3;
        }
        int heightOffset = 0;
        if (renderBackground)
            RenderUtils.drawSolid(matrix.last().pose(), x - 3, y - 3, x + width + 3, y + height + 3, 0x66 << 24);
        for (IDataElement element : list) {
            element.draw(matrix, font, x, y + heightOffset, width, height);
            heightOffset += element.getHeight();
        }
    }

    private int getWidth() {
        return list.stream().mapToInt(IDimensions::getWidth).max().orElse(0);
    }

    private int getHeight() {
        return list.stream().mapToInt(IDimensions::getHeight).reduce(Integer::sum).orElse(0);
    }
}
