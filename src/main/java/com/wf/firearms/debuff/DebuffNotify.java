package com.wf.firearms.debuff;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

final class DebuffNotify {
    private DebuffNotify() {}

    static void onApplied(ServerPlayer player, DebuffType type, DebuffCause cause, int stage, int prevStage) {
        if (cause == DebuffCause.COMMAND) {
            return;
        }
        if (prevStage <= 0) {
            player.displayClientMessage(firstApply(type, cause), true);
        } else if (stage > prevStage) {
            player.displayClientMessage(
                    Component.translatable("gunsrpg.debuff.msg.worsen", type.displayName(), stage),
                    true);
        }
    }

    static void onFractureApplied(ServerPlayer player, DebuffCause cause) {
        if (cause == DebuffCause.COMMAND) {
            return;
        }
        player.displayClientMessage(
                Component.translatable(
                        cause == DebuffCause.FALL
                                ? "gunsrpg.debuff.msg.fracture.fall"
                                : "gunsrpg.debuff.msg.fracture.hurt"),
                true);
    }

    static void onFractureBleedEscalation(ServerPlayer player, int newStage) {
        player.displayClientMessage(
                Component.translatable("gunsrpg.debuff.msg.bleed.fracture_escalate", newStage),
                true);
    }

    static void onStageTimerWorsen(ServerPlayer player, DebuffType type, int stage) {
        player.displayClientMessage(
                Component.translatable("gunsrpg.debuff.msg.worsen", type.displayName(), stage),
                true);
    }

    private static Component firstApply(DebuffType type, DebuffCause cause) {
        return switch (type) {
            case BLEED -> Component.translatable(
                    cause == DebuffCause.GUNSHOT
                            ? "gunsrpg.debuff.msg.bleed.gun"
                            : "gunsrpg.debuff.msg.bleed.hurt");
            case POISON -> Component.translatable(
                    cause == DebuffCause.GUNSHOT
                            ? "gunsrpg.debuff.msg.poison.gun"
                            : "gunsrpg.debuff.msg.poison.hurt");
            case INFECTION -> Component.translatable("gunsrpg.debuff.msg.infection.spread");
            case FRACTURE -> Component.translatable("gunsrpg.debuff.msg.fracture.hurt");
        };
    }
}
