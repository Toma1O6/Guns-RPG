package dev.toma.gunsrpg.world.cap.events;

import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.config.ModConfig;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SPlaySoundEffectPacket;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class BloodmoonEventHandler implements IWorldEventHandler {

    @Override
    public void eventStarted(World world) {
        world.players().stream().map(player -> (ServerPlayerEntity) player).forEach(this::notifyBloodmoonStart);
    }

    @Override
    public void eventFinished(World world) {
        world.players().stream().map(player -> (ServerPlayerEntity) player).forEach(this::notifyBloodmoonEnd);
    }

    @Override
    public boolean canTriggerEvent(World world) {
        return world.getDayTime() >= 12500;
    }

    private void notifyBloodmoonStart(ServerPlayerEntity player) {
        player.connection.send(new SPlaySoundEffectPacket(SoundEvents.END_PORTAL_SPAWN, SoundCategory.NEUTRAL, player.getX(), player.getY(), player.getZ(), 1.0F, 1.0F));
    }

    private void notifyBloodmoonEnd(ServerPlayerEntity player) {
        player.sendMessage(new StringTextComponent(TextFormatting.GREEN + "Bloodmoon falls"), Util.NIL_UUID);
        player.connection.send(new SPlaySoundEffectPacket(ModSounds.RELAXED_2, SoundCategory.NEUTRAL, player.getX(), player.getY(), player.getZ(), 1.0F, 1.0F));
    }
}
