package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.tileentity.ILockable;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

public class C2S_TestPinCombination extends AbstractNetworkPacket {

    private final BlockPos pos;
    private final int[] pins;

    public C2S_TestPinCombination(BlockPos position, IntList pins) {
        this.pos = position;
        this.pins = pins.toIntArray();
    }

    public C2S_TestPinCombination(PacketBuffer buffer) {
        this.pos = buffer.readBlockPos();
        int pinCount = buffer.readInt();
        this.pins = new int[pinCount];
        for (int i = 0; i < pinCount; i++) {
            this.pins[i] = buffer.readInt();
        }
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeBlockPos(this.pos);
        buffer.writeInt(this.pins.length);
        for (int pin : this.pins) {
            buffer.writeInt(pin);
        }
    }

    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        ServerPlayerEntity player = context.getSender();
        ServerWorld world = player.getLevel();
        if (!world.isLoaded(this.pos)) {
            return;
        }
        TileEntity entity = world.getBlockEntity(this.pos);
        if (!(entity instanceof ILockable)) {
            return;
        }
        ILockable lockable = (ILockable) entity;
        IntList list = new IntArrayList(this.pins);
        ILockable.VerificationResult result = lockable.verifyLockCombination(list);
        GunsRPG.log.debug("Unlock attempt at {} ({}) by {} ended with result {}", this.pos, lockable.getClass().getSimpleName(), player, result);
        switch (result) {
            case FAILED:
                lockable.onUnlockFailed(player, list);
                world.playSound(null, this.pos, ModSounds.LOCKPICK_FAILED, SoundCategory.MASTER, 0.5F, 1.0F);
                break;
            case UNLOCK:
                lockable.unlock(player);
                world.playSound(null, this.pos, ModSounds.LOCKPICK_COMPLETED, SoundCategory.MASTER, 0.5F, 1.0F);
                break;
            case PARTIAL_SUCCESS:
                world.playSound(null, this.pos, ModSounds.LOCKPICK_SUCCESS, SoundCategory.MASTER, 0.5F, 1.0F);
                break;
        }
    }
}
