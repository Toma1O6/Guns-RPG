package com.wf.firearms.network;

import com.wf.firearms.culinary.CulinaryTableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record CulinaryCraftPacket(BlockPos pos) {
    public static void encode(CulinaryCraftPacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
    }

    public static CulinaryCraftPacket decode(FriendlyByteBuf buf) {
        return new CulinaryCraftPacket(buf.readBlockPos());
    }

    public static void handle(CulinaryCraftPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) {
                return;
            }
            BlockEntity be = player.level().getBlockEntity(msg.pos);
            if (be instanceof CulinaryTableBlockEntity table && table.stillValid(player)) {
                table.tryCraft(player);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
