package dev.toma.gunsrpg.client.screen.quest;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.common.entity.MayorEntity;
import dev.toma.gunsrpg.common.quests.condition.IQuestCondition;
import dev.toma.gunsrpg.common.quests.quest.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collections;
import java.util.List;

public class QuestDetailsWidget extends Widget {

    private static final ITextComponent QUEST_NAME = new TranslationTextComponent("screen.quests.quest_name").withStyle(TextFormatting.BOLD, TextFormatting.YELLOW);
    private static final ITextComponent QUEST_DESCRIPTION = new TranslationTextComponent("screen.quests.quest_detail").withStyle(TextFormatting.BOLD, TextFormatting.YELLOW);
    private static final ITextComponent QUEST_CONDITIONS = new TranslationTextComponent("screen.quests.conditions").withStyle(TextFormatting.BOLD, TextFormatting.YELLOW);
    private static final ITextComponent WRONG_MAYOR = new TranslationTextComponent("screen.quests.has_other_active").withStyle(TextFormatting.RED);

    private final Quest<?> quest;
    private final MayorEntity mayor;
    private final List<ITextComponent> extendedInfo;
    private int textMargin = 5;
    private boolean showExtendedInfo;

    public QuestDetailsWidget(int x, int y, int width, int height, Quest<?> quest, MayorEntity mayor) {
        super(x, y, width, height, StringTextComponent.EMPTY);
        this.quest = quest;
        this.mayor = mayor;
        if (this.quest instanceof AdditionalObjectiveInfo) {
            AdditionalObjectiveInfo info = (AdditionalObjectiveInfo) this.quest;
            this.extendedInfo = info.additionalInfo();
        } else {
            this.extendedInfo = Collections.emptyList();
        }
    }

    public void setTextMargin(int textMargin) {
        this.textMargin = textMargin;
    }

    public void setExtendedInformation(boolean showExtendedInfo) {
        this.showExtendedInfo = showExtendedInfo;
    }

    @Override
    public void renderButton(MatrixStack matrix, int mouseX, int mouseY, float delta) {
        QuestScheme<?> scheme = this.quest.getScheme();
        DisplayInfo displayInfo = scheme.getDisplayInfo();
        ITextComponent questName = displayInfo.getName();
        ITextComponent detail = displayInfo.getInfo(this.quest.getDescriptionArguments());
        FontRenderer font = Minecraft.getInstance().font;
        int left = this.x + this.textMargin;
        // name
        font.drawShadow(matrix, QUEST_NAME, left, this.y + 2, 0xFFFFFF);
        font.drawShadow(matrix, questName, left, this.y + 12, 0xFFFFFF);

        // description
        font.drawShadow(matrix, QUEST_DESCRIPTION, left, this.y + 37, 0xFFFFFF);
        font.drawShadow(matrix, detail, left, this.y + 47, 0xFFFFFF);
        for (int i = 0; i < this.extendedInfo.size(); i++) {
            ITextComponent info = this.extendedInfo.get(i);
            font.drawShadow(matrix, info, left, this.y + 57 + i * 10, 0xFFFFFF);
        }

        // conditions
        IQuestCondition[] conditions = this.quest.getConditions();
        if (conditions.length > 0) {
            font.drawShadow(matrix, QUEST_CONDITIONS, left, this.y + 72 + this.extendedInfo.size() * 10, 0xFFFFFF);
            for (int i = 0; i < conditions.length; i++) {
                IQuestCondition condition = conditions[i];
                String conditionInfo = condition.getDescriptor(false).getString();
                font.drawShadow(matrix, "- " + conditionInfo, left, this.y + 82 + i * 11 + this.extendedInfo.size() * 10, 0xFFFFFF);
            }
        }

        // mayor info for active quests
        if (this.showExtendedInfo && this.quest.getStatus().isActiveOrCompleted()) {
            if (!this.quest.isManageableByMayor(this.mayor)) {
                font.drawShadow(matrix, WRONG_MAYOR, this.x + this.width - this.textMargin - font.width(WRONG_MAYOR), this.y + this.height - 10, 0xFFFFFF);
            }
        }
    }
}
