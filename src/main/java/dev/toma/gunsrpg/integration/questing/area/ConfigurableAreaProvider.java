package dev.toma.gunsrpg.integration.questing.area;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.init.QuestRegistry;
import dev.toma.gunsrpg.config.quest.AreaConfig;
import dev.toma.questing.area.AreaInteractionMode;
import dev.toma.questing.area.AreaType;
import dev.toma.questing.area.LandBasedAreaProvider;
import dev.toma.questing.area.SimpleAreaProvider;
import dev.toma.questing.area.spawner.Spawner;
import dev.toma.questing.area.spawner.SpawnerType;
import dev.toma.questing.utils.Codecs;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public final class ConfigurableAreaProvider extends LandBasedAreaProvider {

    public static final Codec<ConfigurableAreaProvider> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codecs.enumCodec(AreaLevel.class).optionalFieldOf("areaLevel", AreaLevel.AREA_LEVEL_1).forGetter(t -> t.level),
            Codecs.enumCodec(AreaInteractionMode.class).optionalFieldOf("interactionMode", AreaInteractionMode.NO_INTERACTION).forGetter(SimpleAreaProvider::getInteractionMode),
            SpawnerType.CODEC.listOf().optionalFieldOf("spawners", Collections.emptyList()).forGetter(SimpleAreaProvider::getMobSpawners)
    ).apply(instance, ConfigurableAreaProvider::new));
    private final AreaLevel level;

    public ConfigurableAreaProvider(AreaLevel level, AreaInteractionMode interactionMode, List<Spawner> spawners) {
        super(level.configProvider.get().minDistance, level.configProvider.get().maxDistance, level.configProvider.get().areaSize, interactionMode, spawners);
        this.level = level;
    }

    @Override
    public AreaType<?> getAreaType() {
        return QuestRegistry.CONFIGURED_AREA;
    }

    public enum AreaLevel {

        AREA_LEVEL_1(() -> GunsRPG.config.quest.area1),
        AREA_LEVEL_2(() -> GunsRPG.config.quest.area2),
        AREA_LEVEL_3(() -> GunsRPG.config.quest.area3);

        private final Supplier<AreaConfig> configProvider;

        AreaLevel(Supplier<AreaConfig> configProvider) {
            this.configProvider = configProvider;
        }
    }
}
