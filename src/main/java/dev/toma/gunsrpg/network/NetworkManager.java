package dev.toma.gunsrpg.network;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.INetworkPacket;
import dev.toma.gunsrpg.network.packet.*;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.ReportedException;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.function.Predicate;

public class NetworkManager {

    private static final String VERSION = "gunsrpg1";
    private static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(GunsRPG.makeResource("network"))
            .networkProtocolVersion(() -> VERSION)
            .clientAcceptedVersions(VERSION::equals)
            .serverAcceptedVersions(VERSION::equals)
            .simpleChannel();
    private static byte ID;

    public static void sendServerPacket(INetworkPacket<?> packet) {
        CHANNEL.sendToServer(packet);
    }

    public static void sendWorldPacket(World world, INetworkPacket<?> packet) {
        sendWorldPacket(world, packet, player -> true);
    }

    public static void sendWorldPacket(World world, INetworkPacket<?> packet, Predicate<ServerPlayerEntity> condition) {
        if (!(world instanceof ServerWorld)) {
            throw new UnsupportedOperationException("Cannot send world packet from client!");
        }
        world.players().stream()
                .map(pl -> (ServerPlayerEntity) pl)
                .filter(condition)
                .forEach(serverPlayerEntity -> sendClientPacket(serverPlayerEntity, packet));
    }

    public static void sendClientPacket(ServerPlayerEntity user, INetworkPacket<?> packet) {
        CHANNEL.sendTo(packet, user.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void init() {
        // client packets
        registerNetworkPacket(CPacketUpdateCap.class);
        registerNetworkPacket(CPacketSendAnimation.class);
        registerNetworkPacket(CPacketNewSkills.class);
        registerNetworkPacket(CPacketSynchTile.class);
        // server packets
        registerNetworkPacket(SPacketShoot.class);
        registerNetworkPacket(SPacketRequestDataUpdate.class);
        registerNetworkPacket(SPacketSelectAmmo.class);
        registerNetworkPacket(SPacketSetAiming.class);
        registerNetworkPacket(SPacketSetReloading.class);
        registerNetworkPacket(SPacketChangeFiremode.class);
        registerNetworkPacket(SPacketUnlockSkill.class);
        registerNetworkPacket(SPacketRequestSmithingCraft.class);
        registerNetworkPacket(SPacketSkillClicked.class);
    }

    private static <P extends INetworkPacket<P>> void registerNetworkPacket(Class<P> packetType) {
        P packet;
        try {
            packet = packetType.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ReportedException(CrashReport.forThrowable(e, "Couldn't instantiate packet for registration. Make sure you have provided public constructor with no parameters."));
        }
        CHANNEL.registerMessage(ID++, packetType, INetworkPacket::encode, packet::decode, INetworkPacket::handle);
    }
}
