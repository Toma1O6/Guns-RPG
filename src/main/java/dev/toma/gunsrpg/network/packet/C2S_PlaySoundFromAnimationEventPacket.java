package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class C2S_PlaySoundFromAnimationEventPacket extends AbstractNetworkPacket<C2S_PlaySoundFromAnimationEventPacket> {

    private final ResourceLocation soundId;
    private final float volume;
    private final float pitch;

    public C2S_PlaySoundFromAnimationEventPacket(ResourceLocation soundId, float volume, float pitch) {
        this.soundId = soundId;
        this.volume = volume;
        this.pitch = pitch;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeResourceLocation(soundId);
        buffer.writeFloat(volume);
        buffer.writeFloat(pitch);
    }

    @Override
    public C2S_PlaySoundFromAnimationEventPacket decode(PacketBuffer buffer) {
        ResourceLocation soundId = buffer.readResourceLocation();
        float volume = buffer.readFloat();
        float pitch = buffer.readFloat();
        return new C2S_PlaySoundFromAnimationEventPacket(soundId, volume, pitch);
    }

    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        ServerPlayerEntity serverPlayer = context.getSender();
        IForgeRegistry<SoundEvent> soundRegistry = ForgeRegistries.SOUND_EVENTS;
        if (!soundRegistry.containsKey(soundId)) {
            return;
        }
        SoundEvent event = soundRegistry.getValue(soundId);
        serverPlayer.level.playSound(serverPlayer, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), event, SoundCategory.PLAYERS, volume, pitch);
    }
}