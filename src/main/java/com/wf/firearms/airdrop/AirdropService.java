package com.wf.firearms.airdrop;



import com.wf.firearms.GunsRpg;

import com.wf.firearms.config.AirdropConfig;

import com.wf.firearms.entity.AirdropEntity;

import net.minecraft.core.BlockPos;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import net.minecraft.world.level.Level;

import net.minecraft.world.level.levelgen.Heightmap;

import net.minecraft.world.level.saveddata.SavedData;



import java.util.Optional;



public final class AirdropService {

    private static final long TICKS_PER_DAY = 24000L;

    private static final int SPAWN_HEIGHT_ABOVE = 100;



    private AirdropService() {}



    public static Optional<BlockPos> spawn(ServerLevel level) {

        if (level.dimension() != Level.OVERWORLD) {

            return Optional.empty();

        }

        BlockPos spawn = level.getSharedSpawnPos();

        int radius = AirdropConfig.spawnRadius();

        var random = level.random;

        for (int attempt = 0; attempt < 32; attempt++) {

            int x = spawn.getX() + random.nextInt(radius * 2 + 1) - radius;

            int z = spawn.getZ() + random.nextInt(radius * 2 + 1) - radius;

            if (x * x + z * z > (long) radius * radius) {

                continue;

            }

            int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);

            BlockPos surface = new BlockPos(x, surfaceY, z);

            if (findPlacePos(level, surface) == null) {

                continue;

            }

            double dropY = surfaceY + SPAWN_HEIGHT_ABOVE;

            if (!level.getBlockState(BlockPos.containing(x + 0.5, dropY, z + 0.5)).canBeReplaced()) {

                continue;

            }

            AirdropEntity entity = new AirdropEntity(level, x + 0.5, dropY, z + 0.5);

            if (!level.addFreshEntity(entity)) {

                continue;

            }

            GunsRpg.LOGGER.info("[gunsrpg] 空投已投放，目标区域 ({}, {})", x, z);
            broadcastIncoming(level);
            return Optional.of(surface);

        }

        GunsRpg.LOGGER.warn("[gunsrpg] 空投生成失败：出生点 {} 周围 {} 格内未找到合适位置", spawn, radius);

        return Optional.empty();

    }



    private static void broadcastIncoming(ServerLevel level) {
        Component msg = Component.translatable("gunsrpg.airdrop.incoming");
        for (ServerPlayer player : level.players()) {
            player.displayClientMessage(msg, true);
            player.sendSystemMessage(msg);
        }
    }

    private static BlockPos findPlacePos(ServerLevel level, BlockPos surface) {

        BlockPos pos = surface;

        if (!level.getBlockState(pos).canBeReplaced()) {

            pos = pos.above();

        }

        if (!level.getBlockState(pos).canBeReplaced()) {

            return null;

        }

        if (!level.getBlockState(pos.below()).isSolidRender(level, pos.below())) {

            return null;

        }

        return pos;

    }



    public static void tick(ServerLevel level) {

        if (level.dimension() != Level.OVERWORLD) {

            return;

        }

        if (level.getGameTime() % 200 != 0) {

            return;

        }

        AirdropSaveData data = getData(level);

        long day = level.getDayTime() / TICKS_PER_DAY;

        long interval = AirdropConfig.activeIntervalDays();

        if (data.lastDropDay < 0) {

            data.lastDropDay = day;

            data.setDirty();

            return;

        }

        if (day - data.lastDropDay >= interval) {

            if (spawn(level).isPresent()) {

                data.lastDropDay = day;

                data.setDirty();

            }

        }

    }



    public static Optional<BlockPos> spawnNearPlayer(ServerLevel level, ServerPlayer player) {
        BlockPos base = player.blockPosition();
        var random = level.random;
        for (int attempt = 0; attempt < 16; attempt++) {
            int x = base.getX() + random.nextInt(33) - 16;
            int z = base.getZ() + random.nextInt(33) - 16;
            int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);
            BlockPos surface = new BlockPos(x, surfaceY, z);
            if (findPlacePos(level, surface) == null) {
                continue;
            }
            double dropY = surfaceY + SPAWN_HEIGHT_ABOVE;
            AirdropEntity entity = new AirdropEntity(level, x + 0.5, dropY, z + 0.5);
            if (!level.addFreshEntity(entity)) {
                continue;
            }
            player.displayClientMessage(Component.translatable("gunsrpg.airdrop.incoming"), true);
            return Optional.of(surface);
        }
        return Optional.empty();
    }

    public static Optional<BlockPos> forceSpawn(ServerLevel level) {

        Optional<BlockPos> placed = spawn(level);

        placed.ifPresent(

                pos -> {

                    AirdropSaveData data = getData(level);

                    data.lastDropDay = level.getDayTime() / TICKS_PER_DAY;

                    data.setDirty();

                });

        return placed;

    }



    private static AirdropSaveData getData(ServerLevel level) {

        return level.getDataStorage().computeIfAbsent(AirdropSaveData::load, AirdropSaveData::new, "gunsrpg_airdrop");

    }



    static final class AirdropSaveData extends SavedData {

        long lastDropDay = -1;



        static AirdropSaveData load(net.minecraft.nbt.CompoundTag tag) {

            AirdropSaveData data = new AirdropSaveData();

            data.lastDropDay = tag.getLong("LastDropDay");

            return data;

        }



        @Override

        public net.minecraft.nbt.CompoundTag save(net.minecraft.nbt.CompoundTag tag) {

            tag.putLong("LastDropDay", lastDropDay);

            return tag;

        }

    }

}


