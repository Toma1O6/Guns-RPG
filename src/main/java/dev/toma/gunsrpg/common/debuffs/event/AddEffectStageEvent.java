package dev.toma.gunsrpg.common.debuffs.event;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.toma.gunsrpg.common.init.ModDebuffStageEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.registry.Registry;

import java.util.function.Supplier;

public class AddEffectStageEvent implements DebuffStageEvent {

    public static final Codec<AddEffectStageEvent> CODEC = EffectData.CODEC.xmap(AddEffectStageEvent::new, t -> t.effectData)
            .fieldOf("effectData").codec();
    private final EffectData effectData;

    public AddEffectStageEvent(EffectData effectData) {
        this.effectData = effectData;
    }

    @Override
    public void apply(PlayerEntity player) {
        if (player.level.isClientSide || player.level.getGameTime() % 50L != 0) {
            return;
        }
        player.addEffect(this.effectData.get());
    }

    @Override
    public DebuffStageEventType<?> getType() {
        return ModDebuffStageEvents.ADD_EFFECT;
    }

    private static final class EffectData implements Supplier<EffectInstance> { // TODO replace with type from Questing library

        private static final Codec<EffectData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Registry.MOB_EFFECT.fieldOf("effect").forGetter(t -> t.effect),
                Codec.intRange(1, Integer.MAX_VALUE).optionalFieldOf("duration", 600).forGetter(t -> t.duration),
                Codec.intRange(0, 255).optionalFieldOf("amplifier", 0).forGetter(t -> t.amplifier),
                Codec.BOOL.optionalFieldOf("ambient", false).forGetter(t -> t.ambient),
                Codec.BOOL.optionalFieldOf("visible", true).forGetter(t -> t.visible),
                Codec.BOOL.optionalFieldOf("showIcon", true).forGetter(t -> t.showIcon)
        ).apply(instance, EffectData::new));
        private final Effect effect;
        private final int duration;
        private final int amplifier;
        private final boolean ambient;
        private final boolean visible;
        private final boolean showIcon;

        public EffectData(Effect effect, int duration, int amplifier, boolean ambient, boolean visible, boolean showIcon) {
            this.effect = effect;
            this.duration = duration;
            this.amplifier = amplifier;
            this.ambient = ambient;
            this.visible = visible;
            this.showIcon = visible && showIcon;
        }

        @Override
        public EffectInstance get() {
            return new EffectInstance(effect, duration, amplifier, ambient, visible, showIcon);
        }
    }
}
