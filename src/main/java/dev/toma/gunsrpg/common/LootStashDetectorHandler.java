package dev.toma.gunsrpg.common;

import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.ISkillProvider;
import dev.toma.gunsrpg.client.sound.ContinuousSound;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.ModItems;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.skills.TreasureHunterSkill;
import dev.toma.gunsrpg.util.Interval;
import dev.toma.gunsrpg.util.SkillUtil;
import dev.toma.gunsrpg.world.LootStashes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.*;

public class LootStashDetectorHandler {

    private static final Map<UUID, DetectionData> DATA = new HashMap<>();
    private static int cacheClearScheduler;

    public static void initiateUsing(PlayerEntity player) {
        DATA.put(player.getUUID(), new DetectionData());
        if (!player.level.isClientSide) {
            LootStashes.addTracker((ServerPlayerEntity) player);
        }
    }

    public static boolean isUsing(UUID uuid) {
        return DATA.containsKey(uuid);
    }

    public static void stopUsing(UUID uuid) {
        DATA.remove(uuid);
        LootStashes.clearTracker(uuid);
    }

    public static DetectionData getData(UUID uuid) {
        return DATA.computeIfAbsent(uuid, k -> new DetectionData());
    }

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
        data.tickClient(player);
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
        private Status lastStatus;
        private Status status = Status.UNDETECTED;
        private BlockPos trackedLocation;
        private int soundTimer;
        private int lightSwitchTimerHalf;
        private float diodeIntesity;
        @OnlyIn(Dist.CLIENT)
        private ContinuousSound sound;

        public void setTrackedLocation(BlockPos trackedLocation) {
            this.trackedLocation = trackedLocation;
        }

        public Status getStatus() {
            return status;
        }

        public float getDiodeIntesity() {
            return diodeIntesity;
        }

        public void resetTimer() {
            updateTimer = 0;
        }

        public boolean increaseTimer() {
            return ++updateTimer > 20;
        }

        public void tickClient(PlayerEntity player) {
            lastStatus = status;
            if (trackedLocation == null) {
                status = Status.UNDETECTED;
            } else {
                double distance = getDistance(player);
                IPlayerData data = PlayerData.getUnsafe(player);
                ISkillProvider provider = data.getSkillProvider();
                TreasureHunterSkill skill = SkillUtil.getTopHierarchySkill(Skills.TREASURE_HUNTER_I, provider);
                if (skill == null) {
                    status = Status.UNDETECTED;
                    return;
                }
                TreasureHunterSkill.DetectionRadius radius = skill.getRadius();
                status = radius.getStatusByDistance(distance);
                float soundDelay = radius.getSoundIntensity(distance);
                int soundScheduler = soundDelay == 1.0F ? -1 : 2 + (int) (soundDelay * 28);
                if (soundScheduler > 0) {
                    if (--soundTimer <= 0) {
                        soundTimer = soundScheduler;
                        lightSwitchTimerHalf = soundScheduler;
                        player.playSound(ModSounds.DETECTOR_BEEP, 1.0F, 1.0F);
                    }
                    diodeIntesity = soundTimer / (float) lightSwitchTimerHalf;
                }
            }
            if (lastStatus != status) {
                onStatusChanged();
            }
        }

        @OnlyIn(Dist.CLIENT)
        private void onStatusChanged() {
            boolean startSound = status == Status.LOCATED;
            Minecraft mc = Minecraft.getInstance();
            SoundHandler handler = mc.getSoundManager();
            if (sound != null) {
                handler.stop(sound);
            }
            if (startSound) {
                sound = new ContinuousSound(ModSounds.DETECTOR_BEEP_LONG, SoundCategory.MASTER, player -> isUsing(player.getUUID()));
                handler.play(sound);
            }
        }

        private double getDistance(PlayerEntity player) {
            double x = player.getX() - trackedLocation.getX();
            double z = player.getZ() - trackedLocation.getZ();
            return Math.sqrt(x * x + z * z);
        }
    }

    public enum Status {
        LOCATED,
        NEARBY,
        UNDETECTED
    }
}
