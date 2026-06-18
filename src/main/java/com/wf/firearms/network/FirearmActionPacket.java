package com.wf.firearms.network;

import com.wf.firearms.compat.tacz.TaczBridge;
import com.wf.firearms.compat.tacz.TaczGunsrpgAmmoBridge;
import com.wf.firearms.compat.tacz.TaczUnjamBridge;
import com.wf.firearms.config.TaczBackendConfig;
import com.wf.firearms.combat.FirearmCombat;
import com.wf.firearms.combat.FirearmRegistry;
import com.wf.firearms.combat.FirearmShooter;
import com.wf.firearms.combat.FirearmSpec;
import com.wf.firearms.combat.FirearmStackState;
import com.wf.firearms.item.FirearmItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record FirearmActionPacket(Action action, boolean aiming) {
    public enum Action {
        FIRE,
        RELOAD,
        UNJAM,
        FIREMODE,
        AIM_START,
        AIM_STOP
    }

    public static void encode(FirearmActionPacket msg, FriendlyByteBuf buf) {
        buf.writeEnum(msg.action);
        buf.writeBoolean(msg.aiming);
    }

    public static FirearmActionPacket decode(FriendlyByteBuf buf) {
        return new FirearmActionPacket(buf.readEnum(Action.class), buf.readBoolean());
    }

    public static void handle(FirearmActionPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) {
                return;
            }
            ItemStack taczHand = player.getMainHandItem();
            if (TaczBackendConfig.useTaczShooting()
                    && TaczBridge.isTaczGun(taczHand)
                    && msg.action == Action.UNJAM) {
                TaczUnjamBridge.startUnjam(player, taczHand);
                return;
            }
            ItemStack gun = FirearmCombat.getHeldGun(player);
            if (gun.isEmpty() || !(gun.getItem() instanceof FirearmItem firearm)) {
                return;
            }
            FirearmSpec spec = firearm.getSpec();
            switch (msg.action) {
                case FIRE -> FirearmShooter.tryFire(player, gun, spec, msg.aiming);
                case RELOAD -> FirearmCombat.startReload(player, gun);
                case UNJAM -> FirearmCombat.startUnjam(player, gun);
                case FIREMODE -> {
                    ItemStack main = player.getMainHandItem();
                    if (TaczBackendConfig.useTaczShooting()
                            && TaczBridge.isTaczGun(main)
                            && TaczGunsrpgAmmoBridge.usesGunsrpgMaterialAmmo(main)) {
                        TaczGunsrpgAmmoBridge.resolveWeaponKey(main)
                                .map(FirearmRegistry::getOrDefault)
                                .filter(FirearmSpec::supportsFireModeSwitch)
                                .ifPresent(
                                        taczSpec -> {
                                            if (FirearmCombat.cycleFireMode(player, main, taczSpec)) {
                                                TaczBridge.applyFireModeFromSpec(main, taczSpec);
                                            }
                                        });
                    } else {
                        FirearmCombat.cycleFireMode(player, gun);
                    }
                }
                case AIM_START -> FirearmStackState.setAiming(gun, true);
                case AIM_STOP -> FirearmStackState.setAiming(gun, false);
                default -> {}
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
