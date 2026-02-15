package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.client.screen.lockpicking.LockpickingScreen;
import dev.toma.gunsrpg.common.init.ModTags;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

public class S2C_InitiateLockpicking extends AbstractNetworkPacket {

    private final BlockPos position;
    private final int pinCount;

    public S2C_InitiateLockpicking(BlockPos position, int pinCount) {
        this.position = position;
        this.pinCount = pinCount;
    }

    public S2C_InitiateLockpicking(PacketBuffer buffer) {
        this(buffer.readBlockPos(), buffer.readInt());
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeBlockPos(position);
        buffer.writeInt(pinCount);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        Minecraft client = Minecraft.getInstance();
        ItemStack itemStack = client.player.getMainHandItem();
        if (itemStack.getItem().is(ModTags.Items.LOCKPICKS)) {
            client.setScreen(new LockpickingScreen(this.position, this.pinCount));
        }
    }
}
