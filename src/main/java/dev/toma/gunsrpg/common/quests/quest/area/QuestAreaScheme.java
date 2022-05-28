package dev.toma.gunsrpg.common.quests.quest.area;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.quests.QuestSystem;
import dev.toma.gunsrpg.common.quests.adapters.MobSpawnerAdapter;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public final class QuestAreaScheme {

    private final int size;
    private final int distance;
    private final int spawnInterval;
    private final List<IMobSpawner> mobSpawnerList;

    public QuestAreaScheme(int size, int distance, int spawnInterval, List<IMobSpawner> mobSpawnerList) {
        this.size = size;
        this.distance = distance;
        this.spawnInterval = spawnInterval;
        this.mobSpawnerList = mobSpawnerList;
    }

    public QuestArea getArea(World world, int positionX, int positionZ) {
        GunsRPG.log.debug(QuestSystem.MARKER, "Generating new quest area");
        int minDist = this.distance;
        int maxDist = this.distance * 6;
        Random random = world.random;
        int x = this.getRandomDistance(minDist, maxDist, random);
        int z = this.getRandomDistance(minDist, maxDist, random);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        mutable.setX(positionX + x);
        mutable.setZ(positionZ + z);
        Biome biome = world.getBiome(mutable);
        int attemptIndex = 0;
        while (biome.getBiomeCategory() == Biome.Category.RIVER || biome.getBiomeCategory() == Biome.Category.OCEAN) {
            x = this.getRandomDistance(minDist, maxDist, random);
            z = this.getRandomDistance(minDist, maxDist, random);
            mutable.setX(positionX + x);
            mutable.setZ(positionZ + z);
            biome = world.getBiome(mutable);
            ++attemptIndex;
            if (attemptIndex == 100) break;
        }
        GunsRPG.log.debug(QuestSystem.MARKER, "Generated area at [{},{}] after {} attempts", mutable.getX(), mutable.getZ(), attemptIndex + 1);
        return new QuestArea(this, mutable.immutable());
    }

    public int getSize() {
        return size;
    }

    public int getDistance() {
        return distance;
    }

    public int getSpawnInterval() {
        return spawnInterval;
    }

    public List<IMobSpawner> getMobSpawnerList() {
        return mobSpawnerList;
    }

    @Override
    public String toString() {
        return String.format("Size: %d, MinDistance: %d, SpawnerCount: %d", size, distance, mobSpawnerList.size());
    }

    public CompoundNBT toNbt() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("size", size);
        nbt.putInt("distance", distance);
        nbt.putInt("spawnInterval", spawnInterval);
        ListNBT spawners = new ListNBT();
        mobSpawnerList.stream().map(IMobSpawner::toNbt).forEach(spawners::add);
        nbt.put("spawners", spawners);
        return nbt;
    }

    private int getRandomDistance(int minDist, int maxDist, Random random) {
        return random.nextBoolean() ? minDist + random.nextInt(maxDist) : -minDist - random.nextInt(maxDist);
    }

    public static QuestAreaScheme fromNbt(CompoundNBT nbt) {
        int size = nbt.getInt("size");
        int distance = nbt.getInt("distance");
        int spawnInterval = nbt.getInt("spawnInterval");
        ListNBT spawnersNbt = nbt.getList("spawners", Constants.NBT.TAG_COMPOUND);
        List<IMobSpawner> list = spawnersNbt.stream().map(inbt -> MobSpawner.fromNbt((CompoundNBT) inbt)).collect(Collectors.toList());
        return new QuestAreaScheme(size, distance, spawnInterval, list);
    }

    public static QuestAreaScheme fromJson(JsonObject object) throws JsonParseException {
        int size = JSONUtils.getAsInt(object, "size");
        int distance = JSONUtils.getAsInt(object, "distance");
        int spawnInterval = JSONUtils.getAsInt(object, "spawnInterval", 20);
        List<IMobSpawner> list;
        if (object.has("spawners")) {
            JsonArray array = JSONUtils.getAsJsonArray(object, "spawners");
            MobSpawnerAdapter adapter = new MobSpawnerAdapter();
            list = JsonHelper.<List<IMobSpawner>, IMobSpawner>deserialize(array, arr -> new ArrayList<>(arr.size()), adapter::deserialize, List::add);
        } else {
            list = Collections.emptyList();
        }
        return new QuestAreaScheme(size, distance, spawnInterval, list);
    }
}
