package dev.toma.gunsrpg.common;

import dev.toma.gunsrpg.common.init.ModItems;
import dev.toma.gunsrpg.util.Interval;
import dev.toma.gunsrpg.world.LootStashes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.*;

public class LootStashDetectorHandler {

    private static final Map<UUID, DetectionData> DATA = new HashMap<>();
    private static int cacheClearScheduler;

    @SubscribeEvent
    public void tickPlayers(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START)
            return;
        PlayerEntity player = event.player;
        UUID uuid = player.getUUID();
        DetectionData data = DATA.get(uuid);
        if (data == null) return;
        tickPlayer(player, data);
    }

    @SubscribeEvent
    public void tickServer(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END || --cacheClearScheduler > 0) {
            return;
        }
        cacheClearScheduler = Interval.minutes(5).valueIn(Interval.Unit.TICK);
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        PlayerList playerList = server.getPlayerList();
        Set<UUID> uuids = Collections.unmodifiableSet(DATA.keySet());
        for (UUID uuid : uuids) {
            PlayerEntity player = playerList.getPlayer(uuid);
            if (player == null) {
                remove(uuid);
            }
        }
    }

    private void tickPlayer(PlayerEntity player, DetectionData data) {
        ItemStack stack = getStashDetector(player);
        if (stack.isEmpty() || stack.getDamageValue() >= stack.getMaxDamage()) {
            remove(player.getUUID());
            data.setTrackedLocation(null);
        }
        if (player.level.isClientSide) {
            tickPlayerClient(player, data);
        } else {
            tickPlayerServer(player, data, stack);
        }
    }

    private void tickPlayerClient(PlayerEntity player, DetectionData data) {
        // TODO update status and distance
    }

    private void tickPlayerServer(PlayerEntity player, DetectionData data, ItemStack stack) {
        if (data.increaseTimer()) {
            data.resetTimer();
            stack.hurt(1, player.getRandom(), (ServerPlayerEntity) player);
            if (stack.getDamageValue() == stack.getMaxDamage()) {
                remove(player.getUUID());
            }
        }
    }

    private void remove(UUID uuid) {
        DATA.remove(uuid);
        LootStashes.clearTracker(uuid);
    }

    private ItemStack getStashDetector(PlayerEntity player) {
        ItemStack stack = player.getMainHandItem();
        if (isStashDetector(stack)) {
            return stack;
        }
        stack = player.getOffhandItem();
        if (isStashDetector(stack)) {
            return stack;
        }
        return ItemStack.EMPTY;
    }

    private boolean isStashDetector(ItemStack stack) {
        return stack.getItem() == ModItems.STASH_DETECTOR;
    }

    public static class DetectionData {

        private int updateTimer;
        private Status status;
        private BlockPos trackedLocation;

        public void setTrackedLocation(BlockPos trackedLocation) {
            this.trackedLocation = trackedLocation;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public Status getStatus() {
            return status;
        }

        public void resetTimer() {
            updateTimer = 0;
        }

        public boolean increaseTimer() {
            return ++updateTimer > 20;
        }
    }

    public enum Status {
        LOCATED,
        NEARBY,
        UNDETECTED,
        OFF
    }
}
