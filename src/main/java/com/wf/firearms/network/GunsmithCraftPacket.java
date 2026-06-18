package com.wf.firearms.network;

import com.wf.firearms.gunsmith.GunsmithTableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record GunsmithCraftPacket(BlockPos pos) {
    public static void encode(GunsmithCraftPacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
    }

    public static GunsmithCraftPacket decode(FriendlyByteBuf buf) {
        return new GunsmithCraftPacket(buf.readBlockPos());
    }

    public static void handle(GunsmithCraftPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) {
                return;
            }
            BlockEntity be = player.level().getBlockEntity(msg.pos);
            if (be instanceof GunsmithTableBlockEntity table && table.stillValid(player)) {
                table.tryCraft(player);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
