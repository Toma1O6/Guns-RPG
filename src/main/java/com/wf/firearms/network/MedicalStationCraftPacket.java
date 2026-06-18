package com.wf.firearms.network;

import com.wf.firearms.medical.MedicalStationTableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record MedicalStationCraftPacket(BlockPos pos) {
    public static void encode(MedicalStationCraftPacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
    }

    public static MedicalStationCraftPacket decode(FriendlyByteBuf buf) {
        return new MedicalStationCraftPacket(buf.readBlockPos());
    }

    public static void handle(MedicalStationCraftPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) {
                return;
            }
            BlockEntity be = player.level().getBlockEntity(msg.pos);
            if (be instanceof MedicalStationTableBlockEntity table && table.stillValid(player)) {
                table.tryCraft(player);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
