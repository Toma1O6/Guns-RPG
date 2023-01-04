package dev.toma.gunsrpg.integration.questing.area.instance;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.toma.gunsrpg.integration.questing.area.provider.ConfiguredAreaProvider;
import dev.toma.questing.common.component.area.instance.SimpleArea;
import dev.toma.questing.common.component.area.spawner.Spawner;
import dev.toma.questing.common.component.area.spawner.SpawnerType;
import dev.toma.questing.utils.Codecs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

import java.util.List;

public class ConfiguredArea extends SimpleArea<ConfiguredAreaProvider> {

    public static final Codec<ConfiguredArea> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ConfiguredAreaProvider.CODEC.fieldOf("provider").forGetter(SimpleArea::getAreaProvider),
            BlockPos.CODEC.fieldOf("center").forGetter(SimpleArea::getCenter),
            Codecs.VECTOR3D.fieldOf("a").forGetter(SimpleArea::getLeftCorner),
            Codecs.VECTOR3D.fieldOf("b").forGetter(SimpleArea::getRightCorner),
            SpawnerType.CODEC.listOf().fieldOf("spawners").forGetter(SimpleArea::getSpawnerList),
            Codec.BOOL.fieldOf("active").forGetter(SimpleArea::isActive))
    .apply(instance, ConfiguredArea::new));

    public ConfiguredArea(ConfiguredAreaProvider provider, BlockPos center, List<Spawner> spawnerList) {
        super(provider, center, spawnerList);
    }

    public ConfiguredArea(ConfiguredAreaProvider provider, BlockPos center, Vector3d a, Vector3d b, List<Spawner> spawnerList, boolean active) {
        super(provider, center, a, b, spawnerList, active);
    }

    @Override
    public boolean hasBeenAbandonded() {
        return false;
    }
}
