package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.common.entity.TurretEntity;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.BiConsumer;

public class C2S_TurretSettingsPacket extends AbstractNetworkPacket<C2S_TurretSettingsPacket> {

    private final int turretId;
    private final SettingsType settingsType;
    private final TurretEntity.TargettingMode targettingMode;
    private final WhitelistOperation whitelistOperation;
    private final UUID playerId;

    public C2S_TurretSettingsPacket() {
        this(0, null, null, null, null);
    }

    private C2S_TurretSettingsPacket(int turretId, SettingsType settingsType, TurretEntity.TargettingMode targettingMode, WhitelistOperation whitelistOperation, UUID playerId) {
        this.turretId = turretId;
        this.settingsType = settingsType;
        this.targettingMode = targettingMode;
        this.whitelistOperation = whitelistOperation;
        this.playerId = playerId;
    }

    public static C2S_TurretSettingsPacket setTargettingMode(TurretEntity turret, TurretEntity.TargettingMode targettingMode) {
        return new C2S_TurretSettingsPacket(turret.getId(), SettingsType.TARGETTING_MODE, targettingMode, null, null);
    }

    public static C2S_TurretSettingsPacket whitelistPacket(TurretEntity turret, WhitelistOperation operation, UUID playerId) {
        return new C2S_TurretSettingsPacket(turret.getId(), SettingsType.WHITELIST, null, operation, playerId);
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeInt(turretId);
        buffer.writeEnum(settingsType);
        settingsType.encoder.accept(this, buffer);
    }

    @Override
    public C2S_TurretSettingsPacket decode(PacketBuffer buffer) {
        int turretId = buffer.readInt();
        SettingsType settingsType = buffer.readEnum(SettingsType.class);
        return settingsType.decoder.decode(turretId, settingsType, buffer);
    }

    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        ServerPlayerEntity sender = context.getSender();
        ServerWorld world = sender.getLevel();
        Entity entity = world.getEntity(turretId);
        if (entity instanceof TurretEntity) {
            TurretEntity turret = (TurretEntity) entity;
            if (turret.isAuthorized(sender)) {
                settingsType.handler.accept(this, turret);
            }
        }
    }

    private TurretEntity.TargettingMode getTargettingMode() {
        return targettingMode;
    }

    private WhitelistOperation getWhitelistOperation() {
        return whitelistOperation;
    }

    private UUID getPlayerId() {
        return playerId;
    }

    private enum SettingsType {

        TARGETTING_MODE((pkt, buf) -> {
            TurretEntity.TargettingMode mode = pkt.getTargettingMode();
            buf.writeEnum(mode);
        }, (id, type, buf) -> {
            TurretEntity.TargettingMode mode = buf.readEnum(TurretEntity.TargettingMode.class);
            return new C2S_TurretSettingsPacket(id, type, mode, null, null);
        }, (pkt, turret) -> {
            TurretEntity.TargettingMode targettingMode = pkt.getTargettingMode();
            turret.setTargettingMode(targettingMode);
        }),
        WHITELIST((pkt, buf) -> {
            WhitelistOperation operation = pkt.getWhitelistOperation();
            UUID playerId = pkt.getPlayerId();
            buf.writeEnum(operation);
            buf.writeUUID(playerId);
        }, (id, type, buf) -> {
            WhitelistOperation operation = buf.readEnum(WhitelistOperation.class);
            UUID playerId = buf.readUUID();
            return new C2S_TurretSettingsPacket(id, type, null, operation, playerId);
        }, (pkt, turret) -> {
            WhitelistOperation operation = pkt.getWhitelistOperation();
            UUID playerId = pkt.getPlayerId();
            BiConsumer<TurretEntity, UUID> op = operation == WhitelistOperation.ADD ? TurretEntity::addToWhitelist : TurretEntity::removeFromWhitelist;
            op.accept(turret, playerId);
        });

        private final BiConsumer<C2S_TurretSettingsPacket, PacketBuffer> encoder;
        private final Decoder decoder;
        private final BiConsumer<C2S_TurretSettingsPacket, TurretEntity> handler;

        SettingsType(BiConsumer<C2S_TurretSettingsPacket, PacketBuffer> encoder, Decoder decoder, BiConsumer<C2S_TurretSettingsPacket, TurretEntity> handler) {
            this.encoder = encoder;
            this.decoder = decoder;
            this.handler = handler;
        }

        @FunctionalInterface
        private interface Decoder {
            C2S_TurretSettingsPacket decode(int turretId, SettingsType settingsType, PacketBuffer buffer);
        }
    }

    public enum WhitelistOperation {

        ADD,
        REMOVE
    }
}
