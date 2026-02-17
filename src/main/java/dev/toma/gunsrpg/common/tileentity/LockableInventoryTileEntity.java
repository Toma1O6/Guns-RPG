package dev.toma.gunsrpg.common.tileentity;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.init.ModTags;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.S2C_CloseScreen;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Random;

public abstract class LockableInventoryTileEntity extends InventoryTileEntity implements ILockable {

    private IntList lockCombination;

    public LockableInventoryTileEntity(TileEntityType<? extends LockableInventoryTileEntity> tileEntityType) {
        super(tileEntityType);
    }

    public abstract int getLockPinSize();

    public boolean isLockable() {
        return this.getLockPinSize() > 0;
    }

    public void generateDefaultLockCombination() {
        int pins = this.getLockPinSize();
        IntList combination = new IntArrayList(pins);
        for (int i = 0; i < pins; i++) {
            combination.add(i);
        }
        Collections.shuffle(combination);
        this.assignLockConfiguration(combination);
    }

    @Override
    public VerificationResult verifyLockCombination(IntList combination) {
        if (this.lockCombination == null)
            return VerificationResult.ALREADY_UNLOCKED;
        if (combination.size() >= this.lockCombination.size()) {
            return this.lockCombination.equals(combination) ? VerificationResult.UNLOCK : VerificationResult.FAILED;
        }
        int snapshotSize = Math.min(this.lockCombination.size(), combination.size());
        for (int i = 0; i < snapshotSize; i++) {
            if (this.lockCombination.getInt(i) != combination.getInt(i)) {
                return VerificationResult.FAILED;
            }
        }
        return VerificationResult.PARTIAL_SUCCESS;
    }

    @Override
    public boolean isLocked() {
        return this.isLockable() && this.lockCombination != null;
    }

    @Override
    public boolean canLockpick(PlayerEntity player) {
        ItemStack itemStack = player.getMainHandItem();
        return itemStack.getItem().is(ModTags.Items.LOCKPICKS);
    }

    @Override
    public void unlock(ServerPlayerEntity player) {
        this.assignLockConfiguration(null);
        NetworkManager.sendClientPacket(player, S2C_CloseScreen.INSTANCE);
    }

    @Override
    public void onUnlockFailed(ServerPlayerEntity player, IntList testedCombination) {
        ItemStack lockpick = player.getMainHandItem();
        Random random = player.getRandom();
        if (!player.isCreative() && !lockpick.isEmpty() && lockpick.getItem().is(ModTags.Items.LOCKPICKS) && random.nextFloat() < 0.4F) {
            lockpick.shrink(1);
            ServerWorld level = player.getLevel();
            level.playSound(null, player.blockPosition(), SoundEvents.ITEM_BREAK, player.getSoundSource(), 1.0F, 1.0F);
        }
        NetworkManager.sendClientPacket(player, S2C_CloseScreen.INSTANCE);
    }

    @Override
    public void assignLockConfiguration(@Nullable IntList configuration) {
        this.lockCombination = configuration;
        GunsRPG.log.debug("Setting lock configuration for {} to {}", this, configuration);
        this.setChanged();
    }

    @Nullable
    @Override
    public IntList getLockConfiguration() {
        return this.lockCombination;
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        super.save(nbt);
        int[] pins = this.lockCombination != null ? this.lockCombination.toIntArray() : null;
        if (pins != null) {
            nbt.putIntArray("lock_pins", pins);
        }
        return nbt;
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        int[] pins = nbt.getIntArray("lock_pins");
        if (pins.length == 0) {
            this.lockCombination = null;
        } else {
            this.lockCombination = new IntArrayList(pins);
        }
    }
}
