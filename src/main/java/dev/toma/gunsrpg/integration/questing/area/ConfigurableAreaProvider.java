package dev.toma.gunsrpg.integration.questing.area;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.config.quest.AreaConfig;
import dev.toma.questing.area.AreaInteractionMode;
import dev.toma.questing.area.AreaType;
import dev.toma.questing.area.LandBasedAreaProvider;
import dev.toma.questing.area.spawner.SpawnerProvider;
import dev.toma.questing.area.spawner.SpawnerType;
import net.minecraft.util.JSONUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class ConfigurableAreaProvider extends LandBasedAreaProvider {

    public ConfigurableAreaProvider(AreaLevel level, AreaInteractionMode interactionMode, List<SpawnerProvider<?>> spawners) {
        super(level.configProvider.get().minDistance, level.configProvider.get().maxDistance, level.configProvider.get().areaSize, interactionMode, spawners);
    }

    @Override
    public AreaType<?> getAreaType() {
        return super.getAreaType();
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

    public static final class Serializer implements AreaType.ProviderSerializer<ConfigurableAreaProvider> {

        @Override
        public ConfigurableAreaProvider providerFromJson(JsonObject data) {
            String levelId = JSONUtils.getAsString(data, "areaLevel", AreaLevel.AREA_LEVEL_1.name());
            AreaLevel level;
            try {
                level = AreaLevel.valueOf(levelId);
            } catch (IllegalArgumentException e) {
                throw new JsonSyntaxException("Unknown area level: " + levelId);
            }
            String interactionId = JSONUtils.getAsString(data, "interactionMode", AreaInteractionMode.NO_INTERACTION.name());
            AreaInteractionMode interactionMode;
            try {
                interactionMode = AreaInteractionMode.valueOf(interactionId);
            } catch (IllegalArgumentException e) {
                throw new JsonSyntaxException("Unknown interaction mode: " + interactionId);
            }
            JsonArray array = JSONUtils.getAsJsonArray(data, "spawners", new JsonArray());
            List<SpawnerProvider<?>> providers = new ArrayList<>();
            array.forEach(element -> {
                SpawnerProvider<?> provider = SpawnerType.fromJson(element);
                providers.add(provider);
            });
            return new ConfigurableAreaProvider(level, interactionMode, providers);
        }
    }
}
