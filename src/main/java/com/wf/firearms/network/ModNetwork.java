package com.wf.firearms.network;

import com.wf.firearms.GunsRpg;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public final class ModNetwork {
    private static final String PROTOCOL = "1";
    public static final SimpleChannel CHANNEL =
            NetworkRegistry.newSimpleChannel(
                    ResourceLocation.fromNamespaceAndPath(GunsRpg.MOD_ID, "main"),
                    () -> PROTOCOL,
                    PROTOCOL::equals,
                    PROTOCOL::equals);

    private static int nextId = 0;

    private ModNetwork() {}

    public static void register() {
        CHANNEL.registerMessage(
                nextId++,
                UnlockSkillPacket.class,
                UnlockSkillPacket::encode,
                UnlockSkillPacket::decode,
                UnlockSkillPacket::handle);
        CHANNEL.registerMessage(
                nextId++,
                AdjustPerkPacket.class,
                AdjustPerkPacket::encode,
                AdjustPerkPacket::decode,
                AdjustPerkPacket::handle);
        CHANNEL.registerMessage(
                nextId++,
                GunsmithCraftPacket.class,
                GunsmithCraftPacket::encode,
                GunsmithCraftPacket::decode,
                GunsmithCraftPacket::handle);
        CHANNEL.registerMessage(
                nextId++,
                RepairStationRepairPacket.class,
                RepairStationRepairPacket::encode,
                RepairStationRepairPacket::decode,
                RepairStationRepairPacket::handle);
        CHANNEL.registerMessage(
                nextId++,
                FirearmsProgressSyncPacket.class,
                FirearmsProgressSyncPacket::encode,
                FirearmsProgressSyncPacket::decode,
                FirearmsProgressSyncPacket::handle);
        CHANNEL.registerMessage(
                nextId++,
                UnlockSkillResultPacket.class,
                UnlockSkillResultPacket::encode,
                UnlockSkillResultPacket::decode,
                UnlockSkillResultPacket::handle);
        CHANNEL.registerMessage(
                nextId++,
                FirearmActionPacket.class,
                FirearmActionPacket::encode,
                FirearmActionPacket::decode,
                FirearmActionPacket::handle);
        CHANNEL.registerMessage(
                nextId++,
                FirearmStateSyncPacket.class,
                FirearmStateSyncPacket::encode,
                FirearmStateSyncPacket::decode,
                FirearmStateSyncPacket::handle);
        CHANNEL.registerMessage(
                nextId++,
                ReloadSoundPacket.class,
                ReloadSoundPacket::encode,
                ReloadSoundPacket::decode,
                ReloadSoundPacket::handle);
        CHANNEL.registerMessage(
                nextId++,
                TaczUnjamSyncPacket.class,
                TaczUnjamSyncPacket::encode,
                TaczUnjamSyncPacket::decode,
                TaczUnjamSyncPacket::handle);
        CHANNEL.registerMessage(
                nextId++,
                ActiveSkillPacket.class,
                ActiveSkillPacket::encode,
                ActiveSkillPacket::decode,
                ActiveSkillPacket::handle);
        CHANNEL.registerMessage(
                nextId++,
                BloodmoonSyncPacket.class,
                BloodmoonSyncPacket::encode,
                BloodmoonSyncPacket::decode,
                BloodmoonSyncPacket::handle);
        CHANNEL.registerMessage(
                nextId++,
                CulinaryCraftPacket.class,
                CulinaryCraftPacket::encode,
                CulinaryCraftPacket::decode,
                CulinaryCraftPacket::handle);
        CHANNEL.registerMessage(
                nextId++,
                DebuffSyncPacket.class,
                DebuffSyncPacket::encode,
                DebuffSyncPacket::decode,
                DebuffSyncPacket::handle);
        CHANNEL.registerMessage(
                nextId++,
                MedicalStationCraftPacket.class,
                MedicalStationCraftPacket::encode,
                MedicalStationCraftPacket::decode,
                MedicalStationCraftPacket::handle);
    }

    public static void sendActiveSkill(String skillId) {
        if (skillId == null || skillId.isEmpty()) {
            return;
        }
        CHANNEL.sendToServer(new ActiveSkillPacket(skillId));
    }

    public static void sendFirearmAction(FirearmActionPacket packet) {
        CHANNEL.sendToServer(packet);
    }

    public static void sendToServer(UnlockSkillPacket packet) {
        CHANNEL.sendToServer(packet);
    }

    public static void sendToServer(AdjustPerkPacket packet) {
        CHANNEL.sendToServer(packet);
    }

    public static void sendToServer(GunsmithCraftPacket packet) {
        CHANNEL.sendToServer(packet);
    }

    public static void sendToServer(RepairStationRepairPacket packet) {
        CHANNEL.sendToServer(packet);
    }

    public static void sendToServer(CulinaryCraftPacket packet) {
        CHANNEL.sendToServer(packet);
    }

    public static void sendToServer(MedicalStationCraftPacket packet) {
        CHANNEL.sendToServer(packet);
    }
}
