package dev.toma.gunsrpg.network;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.INetworkPacket;
import dev.toma.gunsrpg.network.packet.*;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.ReportedException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.function.Predicate;

public class NetworkManager {

    public static final Marker MARKER = MarkerManager.getMarker("Networking");
    private static final String VERSION = "1.5.5";
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

    public static void sendToAllTracking(Entity entity, INetworkPacket<?> packet) {
        CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), packet);
    }

    public static void sendClientPacket(ServerPlayerEntity user, INetworkPacket<?> packet) {
        CHANNEL.sendTo(packet, user.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void init() {
        // client packets
        registerNetworkPacket(S2C_UpdateCapabilityPacket.class);
        registerNetworkPacket(S2C_AnimationPacket.class);
        registerNetworkPacket(S2C_NewSkillsPacket.class);
        registerNetworkPacket(S2C_SynchBlockEntityPacket.class);
        registerNetworkPacket(S2C_SynchronizationPayloadPacket.class);
        registerNetworkPacket(S2C_SetTrackedStashPacket.class);
        registerNetworkPacket(S2C_UseStashDetectorPacket.class);
        registerNetworkPacket(S2C_OpenQuestScreen.class);
        registerNetworkPacket(S2C_SendEntityData.class);
        registerNetworkPacket(S2C_SendQuestingData.class);
        // server packets
        registerNetworkPacket(C2S_ShootPacket.class);
        registerNetworkPacket(C2S_RequestDataUpdatePacket.class);
        registerNetworkPacket(C2S_SelectAmmoPacket.class);
        registerNetworkPacket(C2S_SetAimingPacket.class);
        registerNetworkPacket(C2S_SetReloadingPacket.class);
        registerNetworkPacket(C2S_ChangeFiremodePacket.class);
        registerNetworkPacket(C2S_UnlockSkillPacket.class);
        registerNetworkPacket(C2S_RequestSkilledCraftPacket.class);
        registerNetworkPacket(C2S_SkillClickedPacket.class);
        registerNetworkPacket(C2S_PacketSetJamming.class);
        registerNetworkPacket(C2S_RequestRepairPacket.class);
        registerNetworkPacket(C2S_RequestBatteryChange.class);
        registerNetworkPacket(C2S_RequestStashDetectorStatus.class);
        registerNetworkPacket(C2S_FusePacket.class);
        registerNetworkPacket(C2S_PurifyPacket.class);
        registerNetworkPacket(C2S_RequestExtensionSkillLockPacket.class);
        registerNetworkPacket(C2S_QuestActionPacket.class);
        registerNetworkPacket(C2S_TurretSettingsPacket.class);
        registerNetworkPacket(C2S_PlaySoundFromAnimationEventPacket.class);
        registerNetworkPacket(C2S_AmmoBenchEventPacket.class);
        registerNetworkPacket(C2S_InviteMember.class);
        registerNetworkPacket(C2S_InviteEvent.class);
        registerNetworkPacket(C2S_RemoveFromGroup.class);
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
