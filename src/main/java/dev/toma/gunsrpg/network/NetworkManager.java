package dev.toma.gunsrpg.network;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.INetworkPacket;
import dev.toma.gunsrpg.network.packet.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.function.Function;
import java.util.function.Predicate;

public class NetworkManager {

    public static final Marker MARKER = MarkerManager.getMarker("Networking");
    private static final String VERSION = "1.9.2";
    private static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(GunsRPG.makeResource("network"))
            .networkProtocolVersion(() -> VERSION)
            .clientAcceptedVersions(VERSION::equals)
            .serverAcceptedVersions(VERSION::equals)
            .simpleChannel();
    private static byte ID;

    public static void sendServerPacket(INetworkPacket packet) {
        CHANNEL.sendToServer(packet);
    }

    public static void sendWorldPacket(World world, INetworkPacket packet) {
        sendWorldPacket(world, packet, player -> true);
    }

    public static void sendWorldPacket(World world, INetworkPacket packet, Predicate<ServerPlayerEntity> condition) {
        if (!(world instanceof ServerWorld)) {
            throw new UnsupportedOperationException("Cannot send world packet from client!");
        }
        world.players().stream()
                .map(pl -> (ServerPlayerEntity) pl)
                .filter(condition)
                .forEach(serverPlayerEntity -> sendClientPacket(serverPlayerEntity, packet));
    }

    public static void sendToAllTracking(Entity entity, INetworkPacket packet) {
        CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), packet);
    }

    public static void sendClientPacket(ServerPlayerEntity user, INetworkPacket packet) {
        CHANNEL.sendTo(packet, user.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void init() {
        // client packets
        registerNetworkPacket(S2C_UpdateCapabilityPacket.class, S2C_UpdateCapabilityPacket::new);
        registerNetworkPacket(S2C_AnimationPacket.class, S2C_AnimationPacket::new);
        registerNetworkPacket(S2C_NewSkillsPacket.class, S2C_NewSkillsPacket::decode);
        registerNetworkPacket(S2C_SynchBlockEntityPacket.class, S2C_SynchBlockEntityPacket::new);
        registerNetworkPacket(S2C_SynchronizationPayloadPacket.class, S2C_SynchronizationPayloadPacket::decode);
        registerNetworkPacket(S2C_SetTrackedStashPacket.class, S2C_SetTrackedStashPacket::decode);
        registerNetworkPacket(S2C_UseStashDetectorPacket.class, S2C_UseStashDetectorPacket::new);
        registerNetworkPacket(S2C_OpenQuestScreen.class, S2C_OpenQuestScreen::decode);
        registerNetworkPacket(S2C_SendEntityData.class, S2C_SendEntityData::new);
        registerNetworkPacket(S2C_SendQuestingData.class, S2C_SendQuestingData::new);
        registerNetworkPacket(S2C_CloseScreen.class, buf -> S2C_CloseScreen.INSTANCE);
        registerNetworkPacket(S2C_InitiateLockpicking.class, S2C_InitiateLockpicking::new);
        // server packets
        registerNetworkPacket(C2S_ShootPacket.class, C2S_ShootPacket::decode);
        registerNetworkPacket(C2S_RequestDataUpdatePacket.class, C2S_RequestDataUpdatePacket::new);
        registerNetworkPacket(C2S_SelectAmmoPacket.class, C2S_SelectAmmoPacket::decode);
        registerNetworkPacket(C2S_SetAimingPacket.class, C2S_SetAimingPacket::new);
        registerNetworkPacket(C2S_SetReloadingPacket.class, C2S_SetReloadingPacket::new);
        registerNetworkPacket(C2S_ChangeFiremodePacket.class, buf -> C2S_ChangeFiremodePacket.INSTANCE);
        registerNetworkPacket(C2S_UnlockSkillPacket.class, C2S_UnlockSkillPacket::decode);
        registerNetworkPacket(C2S_RequestSkilledCraftPacket.class, C2S_RequestSkilledCraftPacket::new);
        registerNetworkPacket(C2S_SkillClickedPacket.class, C2S_SkillClickedPacket::decode);
        registerNetworkPacket(C2S_PacketSetJamming.class, C2S_PacketSetJamming::decode);
        registerNetworkPacket(C2S_RequestRepairPacket.class, C2S_RequestRepairPacket::new);
        registerNetworkPacket(C2S_RequestBatteryChange.class, buffer -> C2S_RequestBatteryChange.INSTANCE);
        registerNetworkPacket(C2S_RequestStashDetectorStatus.class, C2S_RequestStashDetectorStatus::new);
        registerNetworkPacket(C2S_FusePacket.class, C2S_FusePacket::new);
        registerNetworkPacket(C2S_PurifyPacket.class, C2S_PurifyPacket::new);
        registerNetworkPacket(C2S_RequestExtensionSkillLockPacket.class, C2S_RequestExtensionSkillLockPacket::decode);
        registerNetworkPacket(C2S_TurretSettingsPacket.class, C2S_TurretSettingsPacket::decode);
        registerNetworkPacket(C2S_PlaySoundFromAnimationEventPacket.class, C2S_PlaySoundFromAnimationEventPacket::new);
        registerNetworkPacket(C2S_AmmoBenchEventPacket.class, C2S_AmmoBenchEventPacket::new);
        registerNetworkPacket(C2S_InviteMember.class, C2S_InviteMember::new);
        registerNetworkPacket(C2S_InviteEvent.class, C2S_InviteEvent::new);
        registerNetworkPacket(C2S_RemoveFromGroup.class, C2S_RemoveFromGroup::new);
        registerNetworkPacket(C2S_QuestStartRequest.class, C2S_QuestStartRequest::new);
        registerNetworkPacket(C2S_QuestCancelRequest.class, C2S_QuestCancelRequest::new);
        registerNetworkPacket(C2S_QuestCompleteRequest.class, C2S_QuestCompleteRequest::new);
        registerNetworkPacket(C2S_QuestClaimRequest.class, C2S_QuestClaimRequest::new);
        registerNetworkPacket(C2S_TestPinCombination.class, C2S_TestPinCombination::new);
    }

    private static <P extends INetworkPacket> void registerNetworkPacket(Class<P> packetType, Function<PacketBuffer, P> decoder) {
        CHANNEL.registerMessage(ID++, packetType, INetworkPacket::encode, decoder, INetworkPacket::handle);
    }
}
