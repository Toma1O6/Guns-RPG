package dev.toma.gunsrpg.util.helper;

import dev.toma.gunsrpg.api.common.data.ITraderStatus;
import dev.toma.gunsrpg.common.init.ModItems;
import dev.toma.gunsrpg.common.quests.quest.Quest;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

import java.util.function.Function;

public final class ReputationHelper {

    public static final float MIN_REPUTATION =  0.0F;
    public static final float MAX_REPUTATION = 50.0F;
    public static final float COMPLETION_SCALE = 0.2F;
    public static final float FAILURE_SCALE = -0.3F;

    public static void awardReputationForCompletedQuest(ITraderStatus status, Quest<?> quest) {
        addReputation(status, quest, tier -> tier * COMPLETION_SCALE);
    }

    public static void takeReputationForFailedQuest(ITraderStatus status, Quest<?> quest) {
        addReputation(status, quest, tier -> tier * FAILURE_SCALE);
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
        player.addItem(stack);
    }

    private static void addReputation(ITraderStatus status, Quest<?> quest, Function<Integer, Float> calculator) {
        int tier = quest.getRewardTier();
        float reputation = calculator.apply(tier);
        status.addReputation(reputation);
    }

    private ReputationHelper() {}
}
