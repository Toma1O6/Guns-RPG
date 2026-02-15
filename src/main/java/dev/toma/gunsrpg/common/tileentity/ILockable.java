package dev.toma.gunsrpg.common.tileentity;

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Hand;

import javax.annotation.Nullable;

public interface ILockable {

    boolean isLocked();

    boolean canLockpick(PlayerEntity player, Hand hand);

    void unlock(ServerPlayerEntity player);

    void onUnlockFailed(ServerPlayerEntity player, IntList testedCombination);

    void assignLockConfiguration(@Nullable IntList configuration);

    VerificationResult verifyLockCombination(IntList combination);

    @Nullable
    IntList getLockConfiguration();

    enum VerificationResult {
        UNLOCK,
        PARTIAL_SUCCESS,
        FAILED,
        ALREADY_UNLOCKED;
    }
}
