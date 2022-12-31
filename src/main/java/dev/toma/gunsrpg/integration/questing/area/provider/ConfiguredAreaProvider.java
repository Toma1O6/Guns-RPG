package dev.toma.gunsrpg.integration.questing.area.provider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.init.QuestRegistry;
import dev.toma.gunsrpg.config.quest.AreaConfig;
import dev.toma.gunsrpg.integration.questing.area.instance.ConfiguredArea;
import dev.toma.questing.common.component.area.AreaInteractionMode;
import dev.toma.questing.common.component.area.AreaType;
import dev.toma.questing.common.component.area.provider.SimpleAreaProvider;
import dev.toma.questing.common.component.area.spawner.Spawner;
import dev.toma.questing.common.component.area.spawner.SpawnerType;
import dev.toma.questing.common.quest.Quest;
import dev.toma.questing.utils.Codecs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public final class ConfiguredAreaProvider extends SimpleAreaProvider<ConfiguredArea> {

    public static final Codec<ConfiguredAreaProvider> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codecs.enumCodec(AreaLevel.class, String::toUpperCase).optionalFieldOf("areaLevel", AreaLevel.AREA_LEVEL_1).forGetter(t -> t.level),
            Codecs.enumCodec(AreaInteractionMode.class, String::toUpperCase).optionalFieldOf("interactionMode", AreaInteractionMode.NO_INTERACTION).forGetter(SimpleAreaProvider::getInteractionMode),
            SpawnerType.CODEC.listOf().optionalFieldOf("spawners", Collections.emptyList()).forGetter(SimpleAreaProvider::getMobSpawners)
    ).apply(instance, ConfiguredAreaProvider::new));
    private final AreaLevel level;

    public ConfiguredAreaProvider(AreaLevel level, AreaInteractionMode interactionMode, List<Spawner> spawners) {
        super(level.configProvider.get().minDistance, level.configProvider.get().maxDistance, level.configProvider.get().areaSize, interactionMode, spawners);
        this.level = level;
    }

    @Override
    public AreaType<ConfiguredArea, ?> getAreaType() {
        return QuestRegistry.CONFIGURED_AREA;
    }

    @Override
    public ConfiguredArea createAreaInstance(BlockPos blockPos, List<Spawner> list) {
        return new ConfiguredArea(this, blockPos, list);
    }

    @Override
    public boolean isValidLocationForArea(World world, Quest quest, BlockPos blockPos) {
        Biome biome = world.getBiome(blockPos);
        Biome.Category category = biome.getBiomeCategory();
        return category != Biome.Category.RIVER && category != Biome.Category.OCEAN;
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
