package dev.toma.gunsrpg.common.quests.mayor;

import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Locale;

public enum ReputationStatus {

    STRANGER(0.0F, TextFormatting.RED),
    NEUTRAL(5.0F, TextFormatting.GRAY),
    FRIENDLY(10.0F, TextFormatting.GREEN),
    LOYAL(20.0F, TextFormatting.YELLOW),
    HERO(40.0F, TextFormatting.DARK_PURPLE);

    private final int tierLevel;
    private final float sinceReputation;
    private final ITextComponent statusDescriptor;
    private final TextFormatting color;

    ReputationStatus(float sinceReputation, TextFormatting color) {
        this.tierLevel = ordinal() + 1;
        this.sinceReputation = sinceReputation;
        this.color = color;
        this.statusDescriptor = this.generateLocalizedDescriptor();
    }

    public static ReputationStatus getStatus(float reputation) {
        ReputationStatus status = STRANGER;
        ReputationStatus[] values = values();
        for (int i = 1; i < values.length; i++) {
            ReputationStatus reputationStatus = values[i];
            if (reputation >= reputationStatus.sinceReputation) {
                status = reputationStatus;
            } else {
                break;
            }
        }
        return status;
    }

    public int getBaseTier() {
        return tierLevel;
    }

    public ITextComponent getStatusDescriptor() {
        return statusDescriptor;
    }

    private ITextComponent generateLocalizedDescriptor() {
        String key = name().toLowerCase(Locale.ROOT);
        String prefix = "quest.reputation.status.";
        IFormattableTextComponent component = new TranslationTextComponent(prefix + key);
        component.withStyle(color);
        return component;
    }
}
