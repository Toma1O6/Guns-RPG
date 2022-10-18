package dev.toma.gunsrpg.world.cap.events;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.data.IWorldEventHandler;
import dev.toma.gunsrpg.api.common.data.IWorldEventSpec;
import dev.toma.gunsrpg.common.init.ModSounds;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SPlaySoundEffectPacket;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class BloodmoonEventHandler implements IWorldEventHandler {

    private static final ITextComponent TEXT_BLOODMOON_FALL = new TranslationTextComponent("event.bloodmoon.end").withStyle(TextFormatting.GREEN);

    @Override
    public void eventStarted(World world) {
        GunsRPG.log.debug(WorldEventSpec.MARKER, "Sending bloodmoon start notification");
        world.players().stream().map(player -> (ServerPlayerEntity) player).forEach(this::notifyBloodmoonStart);
    }

    @Override
    public void eventFinished(World world) {
        GunsRPG.log.debug(WorldEventSpec.MARKER, "Sending bloodmoon end notification");
        world.players().stream().map(player -> (ServerPlayerEntity) player).forEach(this::notifyBloodmoonEnd);
    }

    @Override
    public boolean canTriggerEvent(World world) {
        return (world.getDayTime() % 24000L) >= 12500;
    }

    private void notifyBloodmoonStart(ServerPlayerEntity player) {
        player.connection.send(new SPlaySoundEffectPacket(SoundEvents.END_PORTAL_SPAWN, SoundCategory.NEUTRAL, player.getX(), player.getY(), player.getZ(), 1.0F, 1.0F));
    }

    private void notifyBloodmoonEnd(ServerPlayerEntity player) {
        player.sendMessage(TEXT_BLOODMOON_FALL, Util.NIL_UUID);
        player.connection.send(new SPlaySoundEffectPacket(ModSounds.RELAXED_2, SoundCategory.NEUTRAL, player.getX(), player.getY(), player.getZ(), 1.0F, 1.0F));
    }
}
