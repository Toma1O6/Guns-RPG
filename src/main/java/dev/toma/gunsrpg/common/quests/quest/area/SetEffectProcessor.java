package dev.toma.gunsrpg.common.quests.quest.area;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class SetEffectProcessor implements IMobSpawnProcessor {

    private final MobSpawnProcessorType<SetEffectProcessor> type;
    private final Effect effect;
    private final int duration;
    private final int amplifier;
    private final boolean ambient;
    private final boolean visible;

    public SetEffectProcessor(MobSpawnProcessorType<SetEffectProcessor> type, Effect effect, int duration, int amplifier, boolean ambient, boolean visible) {
        this.type = type;
        this.effect = effect;
        this.duration = duration;
        this.amplifier = amplifier;
        this.ambient = ambient;
        this.visible = visible;
    }

    @Override
    public MobSpawnProcessorType<?> getType() {
        return type;
    }

    @Override
    public void processMobSpawn(LivingEntity entity, IMobTargettingContext targettingContext) {
        entity.addEffect(new EffectInstance(effect, duration, amplifier, ambient, visible));
    }

    public static final class Serializer implements IMobSpawnProcessorSerializer<SetEffectProcessor> {

        @Override
        public SetEffectProcessor deserialize(MobSpawnProcessorType<SetEffectProcessor> type, JsonElement element) throws JsonParseException {
            JsonObject object = JsonHelper.asJsonObject(element);
            ResourceLocation effectId = new ResourceLocation(JSONUtils.getAsString(object, "effect"));
            Effect effect = ForgeRegistries.POTIONS.getValue(effectId);
            if (effect == null) {
                throw new JsonSyntaxException("Unknown effect: " + effectId);
            }
            int duration = JSONUtils.getAsInt(object, "duration", 600);
            int amplifier = JSONUtils.getAsInt(object, "amplifier", 0);
            boolean ambient = JSONUtils.getAsBoolean(object, "ambient", false);
            boolean visible = JSONUtils.getAsBoolean(object, "visible", true);
            return new SetEffectProcessor(type, effect, duration, amplifier, ambient, visible);
        }

        @Override
        public void toNbt(SetEffectProcessor processor, CompoundNBT nbt) {
            nbt.putString("effect", processor.effect.getRegistryName().toString());
            nbt.putInt("duration", processor.duration);
            nbt.putInt("amplifier", processor.amplifier);
            nbt.putBoolean("ambient", processor.ambient);
            nbt.putBoolean("visible", processor.visible);
        }

        @Override
        public SetEffectProcessor fromNbt(MobSpawnProcessorType<SetEffectProcessor> type, CompoundNBT nbt) {
            Effect effect = ForgeRegistries.POTIONS.getValue(new ResourceLocation(nbt.getString("effect")));
            int duration = nbt.getInt("duration");
            int amplifier = nbt.getInt("amplifier");
            boolean ambient = nbt.getBoolean("ambient");
            boolean visible = nbt.getBoolean("visible");
            return new SetEffectProcessor(type, effect, duration, amplifier, ambient, visible);
        }
    }
}
