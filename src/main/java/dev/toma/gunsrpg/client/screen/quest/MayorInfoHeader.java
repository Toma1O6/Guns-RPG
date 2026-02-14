package dev.toma.gunsrpg.client.screen.quest;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.common.entity.MayorEntity;
import dev.toma.gunsrpg.common.quests.mayor.ReputationStatus;
import dev.toma.gunsrpg.util.Interval;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.function.Function;

public final class MayorInfoHeader extends Widget {

    private static final Interval.IFormatFactory RESTOCK_TIMER_FORMAT = format -> format.src(Interval.Unit.TICK).out(Interval.Unit.HOUR, Interval.Unit.MINUTE, Interval.Unit.SECOND).compact();
    private static final Function<Integer, String> FORMATTER = (ticks) -> Interval.format(ticks, RESTOCK_TIMER_FORMAT);

    private final MayorEntity mayor;
    private final ReputationStatus status;

    public MayorInfoHeader(int x, int y, int width, int height, MayorEntity mayor, ReputationStatus status) {
        super(x, y, width, height, StringTextComponent.EMPTY);
        this.mayor = mayor;
        this.status = status;
    }

    @Override
    public void renderButton(MatrixStack matrix, int mouseX, int mouseY, float delta) {
        FontRenderer font = Minecraft.getInstance().font;
        float yPos = 1 + this.y + (this.height - font.lineHeight) / 2.0F;

        // left align - name
        ITextComponent name = this.mayor.getDisplayName();
        ITextComponent nameComponent = new TranslationTextComponent("screen.quests.mayor_name", name);
        font.drawShadow(matrix, nameComponent, this.x, yPos, 0xFFFFFF);

        // right align - restock
        int remainingTime = (int) this.mayor.getRemainingRestockTime();
        TextFormatting color = this.getTimerColor(remainingTime);
        ITextComponent formattedTime = new StringTextComponent(FORMATTER.apply(remainingTime)).withStyle(color);
        ITextComponent timeComponent = new TranslationTextComponent("screen.quests.restock_timer", formattedTime);
        font.drawShadow(matrix, timeComponent, this.x + this.width - font.width(timeComponent), yPos, 0xFFFFFF);

        // center align - status
        ITextComponent status = this.status.getStatusDescriptor();
        int center = (this.width - font.width(nameComponent) - font.width(timeComponent)) / 2;
        ITextComponent statusComponent = new TranslationTextComponent("screen.quests.mayor_status", status);
        int statusWidth = font.width(statusComponent);
        font.drawShadow(matrix, statusComponent, this.x + center + statusWidth / 2.0F, yPos, 0xFFFFFF);
    }

    private TextFormatting getTimerColor(int time) {
        if (time <= 12_000) {
            return TextFormatting.RED;
        } else if (time <= 36_000) {
            return TextFormatting.YELLOW;
        }
        return TextFormatting.GREEN;
    }
}
