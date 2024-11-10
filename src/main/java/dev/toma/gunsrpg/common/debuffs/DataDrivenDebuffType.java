package dev.toma.gunsrpg.common.debuffs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.toma.gunsrpg.api.common.attribute.IAttribute;
import dev.toma.gunsrpg.api.common.attribute.IAttributeId;
import dev.toma.gunsrpg.api.common.attribute.IAttributeModifier;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.attribute.ExpiringModifier;
import dev.toma.gunsrpg.common.debuffs.event.DebuffStageEvent;
import dev.toma.gunsrpg.common.debuffs.event.DebuffStageEventType;
import dev.toma.gunsrpg.common.debuffs.sources.DebuffSource;
import dev.toma.gunsrpg.common.debuffs.sources.DebuffSourceType;
import dev.toma.gunsrpg.util.function.FloatSupplier;
import net.minecraft.entity.player.PlayerEntity;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.BooleanSupplier;

public final class DataDrivenDebuffType<D extends IStagedDebuff> extends DebuffType<D> implements DynamicDebuff<DataDrivenDebuffType.LoadedData> {

    public static final Codec<LoadedData> DATA_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            DebuffAttributes.CODEC.fieldOf("attributes").forGetter(d -> d.attributes),
            DebuffStage.CODEC.listOf().fieldOf("stages").forGetter(d -> d.stages),
            DebuffSourceType.SOURCE_CODEC.listOf().fieldOf("sources").forGetter(d -> d.sources)
    ).apply(instance, LoadedData::new));
    private final FloatSupplier globalChanceMultiplier;
    private DebuffAttributes attributes;
    private List<DebuffStage> stages;
    private List<DebuffSource> sources;

    public DataDrivenDebuffType(Builder<D> builder) {
        super(builder);
        this.globalChanceMultiplier = builder.chanceMultiplier;
    }

    @Override
    public D onTrigger(IDebuffContext context, Random random, @Nullable Object data) {
        IPlayerData playerData = context.getData();
        IAttributeProvider provider = playerData.getAttributes();
        float resistChance = attributes != null && attributes.getResistanceAttribute() != null ? provider.getAttribute(attributes.getResistanceAttribute()).floatValue() : 0.0F;
        if (isDisabledByAttributes(provider) || (random.nextFloat() < resistChance && resistChance > 0)) {
            return null;
        }
        if (sources == null)
            return null;
        float chanceMultiplier = globalChanceMultiplier.getFloat();
        for (DebuffSource source : sources) {
            float chance = source.getChance(context) * chanceMultiplier;
            if (chance > 0.0F && random.nextFloat() < chance) {
                return this.createRaw();
            }
        }
        return null;
    }

    @Override
    public int getFlags() {
        return TriggerFlags.HURT.getValue();
    }

    @Override
    public Codec<LoadedData> getDataCodec() {
        return DATA_CODEC;
    }

    @Override
    public void onDataLoaded(LoadedData data) {
        this.attributes = data.attributes;
        this.stages = data.stages;
        this.sources = data.sources;
    }

    public boolean isDisabledByAttributes(IAttributeProvider provider) {
        return attributes != null && attributes.blockAttribute != null && provider.getAttribute(attributes.blockAttribute).intValue() > 0;
    }

    public DebuffAttributes getAttributes() {
        return attributes;
    }

    public List<DebuffStage> getStages() {
        return stages;
    }

    public List<DebuffSource> getSources() {
        return sources;
    }

    public IAttributeId getBlockingAttribute() {
        return attributes.blockAttribute;
    }

    public DebuffStage getStage(int index) {
        return stages != null && index >= 0 && index < stages.size() ? stages.get(index) : DebuffStage.NONE;
    }

    public int getDelay(IPlayerData data) {
        IAttributeProvider provider = data.getAttributes();
        return attributes != null && attributes.delayAttribute != null ? provider.getAttribute(attributes.delayAttribute).intValue() : Integer.MAX_VALUE;
    }

    public static float getBuffedProgress(IAttributeProvider provider, IAttributeId blockingAttribute) {
        IAttribute attribute = provider.getAttribute(blockingAttribute);
        List<IAttributeModifier> list = attribute.listModifiers();
        if (list.isEmpty()) {
            return 0.0F;
        }
        IAttributeModifier modifier = list.get(0);
        if (modifier instanceof ExpiringModifier) {
            ExpiringModifier expiringModifier = (ExpiringModifier) modifier;
            int total = expiringModifier.getInitialTime();
            int left = expiringModifier.getTimeLeft();
            return (float) left / total;
        }
        return 0.0F;
    }

    public static final class DebuffStage {

        public static final Codec<DebuffStage> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.intRange(0, 100).fieldOf("limit").forGetter(t -> t.maxApplicableProgress),
                DebuffStageEventType.EVENT_CODEC.listOf().fieldOf("events").forGetter(t -> t.events)
        ).apply(instance, DebuffStage::new));
        public static final DebuffStage NONE = new DebuffStage(Integer.MAX_VALUE, Collections.emptyList());
        private final int maxApplicableProgress;
        private final List<DebuffStageEvent> events;

        public DebuffStage(int maxApplicableProgress, List<DebuffStageEvent> events) {
            this.maxApplicableProgress = maxApplicableProgress;
            this.events = events;
        }

        public boolean isApplicable(int progress) {
            return progress <= maxApplicableProgress;
        }

        public void apply(PlayerEntity player) {
            this.events.forEach(event -> event.apply(player));
        }
    }

    public static final class DebuffAttributes {

        public static final Codec<DebuffAttributes> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Attribs.LOOKUP_CODEC.fieldOf("delay").forGetter(DebuffAttributes::getDelayAttribute),
                Attribs.LOOKUP_CODEC.fieldOf("resist").forGetter(DebuffAttributes::getResistanceAttribute),
                Attribs.LOOKUP_CODEC.fieldOf("block").forGetter(DebuffAttributes::getBlockingAttribute)
        ).apply(instance, DebuffAttributes::new));
        private final IAttributeId delayAttribute;
        private final IAttributeId resistAttribute;
        private final IAttributeId blockAttribute;

        public DebuffAttributes(IAttributeId delayAttribute, IAttributeId resistAttribute, IAttributeId blockAttribute) {
            this.delayAttribute = delayAttribute;
            this.resistAttribute = resistAttribute;
            this.blockAttribute = blockAttribute;
        }

        public IAttributeId getDelayAttribute() {
            return delayAttribute;
        }

        public IAttributeId getResistanceAttribute() {
            return resistAttribute;
        }

        public IAttributeId getBlockingAttribute() {
            return blockAttribute;
        }
    }

    public static final class LoadedData {

        private final DebuffAttributes attributes;
        private final List<DebuffStage> stages;
        private final List<DebuffSource> sources;

        public LoadedData(DebuffAttributes attributes, List<DebuffStage> stages, List<DebuffSource> sources) {
            this.attributes = attributes;
            this.stages = stages;
            this.sources = sources;
        }
    }

    public static final class Builder<D extends IStagedDebuff> implements IDebuffBuilder<D> {

        private IFactory<D> factory;
        private BooleanSupplier isConfigDisabled;
        private FloatSupplier chanceMultiplier;

        public Builder<D> factory(IFactory<D> factory) {
            this.factory = factory;
            return this;
        }

        public Builder<D> config(BooleanSupplier supplier, FloatSupplier chanceMultiplier) {
            this.isConfigDisabled = supplier;
            this.chanceMultiplier = chanceMultiplier;
            return this;
        }

        public DataDrivenDebuffType<D> build() {
            return new DataDrivenDebuffType<>(this);
        }

        @Override
        public IFactory<D> getFactory() {
            return factory;
        }

        @Override
        public BooleanSupplier disabledStatusSupplier() {
            return isConfigDisabled;
        }
    }
}
