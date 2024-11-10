package dev.toma.gunsrpg.util.helper;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.data.ITraderStatus;
import dev.toma.gunsrpg.common.init.ModItems;
import dev.toma.gunsrpg.common.quests.QuestSystem;
import dev.toma.gunsrpg.common.quests.quest.Quest;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.function.Function;

public final class ReputationHelper {

    public static final float MIN_REPUTATION =  0.0F;
    public static final float MAX_REPUTATION = 50.0F;

    public static void awardReputationForCompletedQuest(ITraderStatus status, Quest<?> quest) {
        float reputationAward = GunsRPG.config.quests.reputationAwardPerTier;
        addReputation(status, quest, tier -> tier * reputationAward);
    }

    public static void takeReputationForFailedQuest(ITraderStatus status, Quest<?> quest) {
        float reputationLoss = GunsRPG.config.quests.reputationLossPerTier;
        addReputation(status, quest, tier -> tier * -reputationLoss);
    }

    public static float clampWithinReputationLimits(float in) {
        return MathHelper.clamp(in, MIN_REPUTATION, MAX_REPUTATION);
    }

    public static boolean isMaxedOut(float reputation) {
        return reputation >= MAX_REPUTATION;
    }

    public static void awardPlayerForReputation(PlayerEntity player) {
        if (player.level.isClientSide) return;
        ItemStack stack = new ItemStack(ModItems.GOLD_EGG_SHARD, 2);
        ModUtils.addItem(player, stack);
        GunsRPG.log.info(QuestSystem.MARKER, "Added reputation award to {}", player.getName().getString());
        player.sendMessage(new TranslationTextComponent("quest.reputation.award_given"), Util.NIL_UUID);
    }

    private static void addReputation(ITraderStatus status, Quest<?> quest, Function<Integer, Float> calculator) {
        int tier = quest.getRewardTier();
        float reputation = calculator.apply(tier);
        status.addReputation(reputation);
    }

    private ReputationHelper() {}
}
