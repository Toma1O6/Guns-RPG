package lib.toma.animations.pipeline.event;

import com.google.gson.*;
import lib.toma.animations.pipeline.IAnimation;
import lib.toma.animations.serialization.IAnimationEventSerializer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class SoundAnimationEvent extends AbstractAnimationEvent {

    private final SoundEvent sound;
    private final float volume;
    private final float pitch;

    protected SoundAnimationEvent(AnimationEventType<? extends SoundAnimationEvent> type, float target, SoundEvent sound, float volume, float pitch) {
        super(type, target);
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    public SoundAnimationEvent(float target, SoundEvent sound, float volume, float pitch) {
        this(AnimationEventType.SOUND, target, sound, volume, pitch);
    }

    @Override
    public void dispatchEvent(Minecraft client, IAnimation fromAnimation) {
        client.player.playSound(sound, volume, pitch);
    }

    public static final class Serializer implements IAnimationEventSerializer<SoundAnimationEvent> {

        @Override
        public JsonElement serialize(SoundAnimationEvent event, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.addProperty("sound", event.sound.getRegistryName().toString());
            object.addProperty("volume", event.volume);
            object.addProperty("pitch", event.pitch);
            return object;
        }

        @Override
        public SoundAnimationEvent deserialize(float target, JsonElement src, JsonDeserializationContext context) throws JsonParseException {
            if (!src.isJsonObject())
                throw new JsonSyntaxException("Not a Json object!");
            JsonObject object = src.getAsJsonObject();
            ResourceLocation location = new ResourceLocation(JSONUtils.getAsString(object, "sound"));
            SoundEvent event = ForgeRegistries.SOUND_EVENTS.getValue(location);
            if (event == null)
                throw new JsonSyntaxException("Unknown sound: " + location);
            float volume = JSONUtils.getAsFloat(object, "volume", 1.0F);
            float pitch = JSONUtils.getAsFloat(object, "pitch", 1.0F);
            return new SoundAnimationEvent(target, event, volume, pitch);
        }
    }
}
