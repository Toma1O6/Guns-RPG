package dev.toma.gunsrpg.world;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.init.ModBlockEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ChunkHolder;
import net.minecraft.world.server.ChunkManager;
import net.minecraft.world.server.ServerWorld;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class LootStashes {

    private static final ForkJoinPool ASYNC_POOL = new ForkJoinPool(2);
    private static final Set<UUID> TRACKING = Collections.synchronizedSet(new HashSet<>());
    private static Set<BlockPos> cache = Collections.emptySet();
    private static int updateScheduler;

    public static void addTracker(ServerPlayerEntity player) {
        TRACKING.add(player.getUUID());
        updateClosestAsync(player);
    }

    public static void clearTracker(UUID uuid) {
        TRACKING.remove(uuid);
    }

    public static void tick(ServerWorld world) {
        if (TRACKING.isEmpty()) {
            return;
        }
        --updateScheduler;
        if (updateScheduler <= 0) {
            updateScheduler = 100;
            initiateRefreshAsync(world);
        }
    }

    private static void initiateRefreshAsync(ServerWorld world) {
        CompletableFuture.supplyAsync(() -> getLoaded(world), ASYNC_POOL).exceptionally(err -> {
            GunsRPG.log.error("Couldn't refresh loot stashes, error occurred: {}", err.toString());
            return Collections.emptySet();
        }).thenAccept(set -> {
            cache = set;
            Iterator<UUID> iterator = TRACKING.iterator();
            while (iterator.hasNext()) {
                UUID uuid = iterator.next();
                PlayerEntity player = world.getPlayerByUUID(uuid);
                if (player == null) {
                    iterator.remove();
                    continue;
                }
                updateClosestAsync(player);
            }
        });
    }

    private static void updateClosestAsync(PlayerEntity player) {
        CompletableFuture.supplyAsync(() -> {
            if (cache.isEmpty()) {
                return null;
            }
            BlockPos closest = null;
            double dist = Double.MAX_VALUE;
            for (BlockPos pos : cache) {
                double blockDist = getDistanceTo(pos, player);
                if (blockDist < dist) {
                    closest = pos;
                    dist = blockDist;
                }
            }
            return closest;
        }, ASYNC_POOL).thenAccept(pos -> {
            if (pos != null) {

            }
            // TODO send packet
        });
    }

    private static Set<BlockPos> getLoaded(ServerWorld world) {
        ChunkManager manager = world.getChunkSource().chunkMap;
        Iterable<ChunkHolder> iterable = manager.getChunks();
        return StreamSupport.stream(iterable.spliterator(), true)
                .filter(holder -> holder.getTickingChunk() != null)
                .map(holder -> {
                    Chunk chunk = holder.getTickingChunk();
                    Set<BlockPos> positions = new HashSet<>();
                    for (Map.Entry<BlockPos, TileEntity> entry : chunk.getBlockEntities().entrySet()) {
                        TileEntity entity = entry.getValue();
                        if (entity.getType() == ModBlockEntities.MILITARY_CRATE.get()) {
                            positions.add(entry.getKey());
                        }
                    }
                    return positions;
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private static double getDistanceTo(BlockPos pos, Entity entity) {
        double x = Math.abs(pos.getX() - entity.getX());
        double z = Math.abs(pos.getZ() - entity.getZ());
        return x * x + z * z;
    }
}
