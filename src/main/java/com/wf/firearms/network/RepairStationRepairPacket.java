package com.wf.firearms.network;

import com.wf.firearms.repair.RepairStationBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record RepairStationRepairPacket(BlockPos pos) {
    public static void encode(RepairStationRepairPacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
    }

    public static RepairStationRepairPacket decode(FriendlyByteBuf buf) {
        return new RepairStationRepairPacket(buf.readBlockPos());
    }

    public static void handle(RepairStationRepairPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) {
                return;
            }
            BlockEntity be = player.level().getBlockEntity(msg.pos);
            if (be instanceof RepairStationBlockEntity station && station.stillValid(player)) {
                station.tryRepair(player);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
