package dev.toma.gunsrpg.client.render.infobar;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.common.quests.condition.IQuestCondition;
import dev.toma.gunsrpg.common.quests.condition.NoConditionProvider;
import dev.toma.gunsrpg.common.quests.quest.Quest;
import dev.toma.gunsrpg.util.RenderUtils;
import dev.toma.gunsrpg.util.math.IDimensions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.*;
import java.util.function.Function;

public class QuestDisplayDataModel implements IDataModel {

    private static final ITextComponent CONDITIONS_HEADER = new TranslationTextComponent("screen.quests.conditions").withStyle(TextFormatting.BOLD);

    private final List<IDataElement> list = new ArrayList<>();
    private final UUID clientId;
    private boolean resized;
    private int width;
    private int height;

    public QuestDisplayDataModel(UUID clientId) {
        this.clientId = clientId;
    }

    public void addQuestHeaderWithObjective(Quest<?> quest, Object... args) {
        addQuestHeader(quest, true, args);
    }

    public void addQuestHeader(Quest<?> quest, boolean objective, Object... args) {
        addElement(new QuestInfoElement(quest, objective, args));
    }

    public <Q extends Quest<?>> void addInformationRow(Q quest, Function<Q, ITextComponent> title, Function<Q, ITextComponent> provider) {
        addElement(new DataRowElement<>(quest, title, provider));
    }

    @OnlyIn(Dist.CLIENT)
    public void addConditionDisplay(Quest<?> quest) {
        IQuestCondition[] conditions = quest.getConditions();
        if (conditions.length > 0)
            addElement(new TextElement(CONDITIONS_HEADER));
        Arrays.stream(conditions)
                .filter(condition -> condition != NoConditionProvider.NO_CONDITION)
                .sorted(Comparator.comparing(condition -> condition.getProviderType().getType().getId()))
                .forEach(condition -> addElement(new DataTextElement<>(quest, q -> {
                    IFormattableTextComponent descriptor = condition.getDescriptor(true).copy();
                    Style style = descriptor.getStyle();
                    PlayerEntity player = Minecraft.getInstance().player;
                    Boolean conditionState = condition.isValidInClientContext(q, player);
                    style = style.withItalic(true).withColor(conditionState != null ? conditionState ? TextFormatting.GREEN : TextFormatting.RED : TextFormatting.GRAY);
                    return new StringTextComponent("- ").append(descriptor.setStyle(style));
                })));
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
            matrix.pushPose();
            matrix.scale(0, 0, 0); // workaround for reposition "twitching"
            for (IDataElement element : list) {
                element.draw(matrix, font, x, y, width, height);
            }
            matrix.popPose();
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
