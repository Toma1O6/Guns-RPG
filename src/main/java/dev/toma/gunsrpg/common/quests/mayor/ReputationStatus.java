package dev.toma.gunsrpg.common.quests.mayor;

import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Locale;

public enum ReputationStatus {

    STRANGER(0.0F, TextFormatting.RED),
    NEUTRAL(3.0F, TextFormatting.YELLOW),
    FRIENDLY(10.0F, TextFormatting.GREEN),
    LOYAL(25.0F, TextFormatting.BLUE),
    HERO(50.0F, TextFormatting.DARK_PURPLE);

    private final int tierLevel;
    private final float sinceReputation;
    private final IFormattableTextComponent statusDescriptor;
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

    public static boolean is(ReputationStatus status, float reputation) {
        return reputation >= status.sinceReputation;
    }

    public int getBaseTier() {
        return tierLevel;
    }

    public IFormattableTextComponent getStatusDescriptor() {
        return statusDescriptor;
    }

    private IFormattableTextComponent generateLocalizedDescriptor() {
        String key = name().toLowerCase(Locale.ROOT);
        String prefix = "quest.reputation.status.";
        IFormattableTextComponent textComponent = new TranslationTextComponent(prefix + key);
        textComponent.withStyle(color, TextFormatting.BOLD);
        return textComponent;
    }
}
