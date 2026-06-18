package com.wf.firearms.debuff;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.debuff.DebuffSyncService;
import com.wf.firearms.gameplay.MedicalEffectService;
import com.wf.firearms.gameplay.PerkEffectService;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

/**
 * 物品读条完成后治愈 debuff；止血剂单独处理（延缓出血，不降级）。
 */
@Mod.EventBusSubscriber(modid = GunsRpg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class DebuffMedicalHandler {
    private static final String LAST_CURE_TICK = "gunsrpg_last_cure_tick";

    private DebuffMedicalHandler() {}

    @SubscribeEvent
    public static void onUseItemFinish(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        ItemStack stack = event.getItem();
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (itemId == null) {
            return;
        }
        if (GunsRpg.MOD_ID.equals(itemId.getNamespace()) && "hemostat".equals(itemId.getPath())) {
            applyFinishThrottle(player);
            MedicalEffectService.applyHemostat(player);
            MedicalEffectService.applyAfterMedUse(
                    player,
                    MedicalEffectService.flatHealForDebuffItem("hemostat"),
                    0,
                    "hemostat_effect",
                    null);
            DebuffSyncService.syncFull(player);
            return;
        }
        applyCureFromItem(player, stack);
    }

    private static void applyFinishThrottle(ServerPlayer player) {
        long now = player.level().getGameTime();
        long last = player.getPersistentData().getLong(LAST_CURE_TICK);
        if (now - last < 5) {
            return;
        }
        player.getPersistentData().putLong(LAST_CURE_TICK, now);
    }

    private static void applyCureFromItem(ServerPlayer player, ItemStack stack) {
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(stack.getItem());
        DebuffCureRegistry.CureEntry entry = DebuffCureRegistry.cureEntryFor(itemId);
        if (entry == null) {
            return;
        }
        applyFinishThrottle(player);
        List<DebuffType> cured = new ArrayList<>();
        String effectPerk = effectPerkForItem(itemId);
        for (DebuffType type : entry.types()) {
            boolean ok = entry.mode() == DebuffCureRegistry.CureMode.REDUCE_ONE
                    ? cureReduce(player, type, effectPerk)
                    : DebuffService.tryCure(player, type);
            if (ok) {
                cured.add(type);
            }
        }
        if (!cured.isEmpty()) {
            Component names = Component.literal(String.join(
                    "、",
                    cured.stream().map(t -> t.displayName().getString()).toList()));
            player.displayClientMessage(
                    Component.translatable("gunsrpg.debuff.cured_by_item", names), true);
        }
        MedicalEffectService.applyAfterMedUse(
                player,
                MedicalEffectService.flatHealForDebuffItem(itemId.getPath()),
                0,
                effectPerk,
                null);
    }

    private static boolean cureReduce(ServerPlayer player, DebuffType type, String effectPerk) {
        if (!DebuffService.tryReduce(player, type)) {
            return false;
        }
        int extra = PerkEffectService.extraCureLayers(player, effectPerk);
        for (int i = 0; i < extra; i++) {
            DebuffService.tryReduce(player, type);
        }
        return true;
    }

    private static String effectPerkForItem(ResourceLocation itemId) {
        String path = itemId.getPath();
        if (path.contains("bandage")) {
            return "bandage_effect";
        }
        if (path.contains("hemostat") || path.contains("tourniquet")) {
            return "hemostat_effect";
        }
        if (path.contains("morphine")) {
            return "morphine_effect";
        }
        if (path.contains("calcium") || path.contains("antidote") || path.contains("antidotum")) {
            return "calcium_shot_effect";
        }
        if (path.contains("propital")) {
            return "propital_effect";
        }
        if (path.contains("plaster") || path.contains("splint")) {
            return "splint_effect";
        }
        if (path.contains("vaccine")) {
            return "vaccine_effect";
        }
        if (path.contains("vitamin")) {
            return "vitamins_effect";
        }
        if (path.contains("steroid")) {
            return "steroids_effect";
        }
        if (path.contains("adrenaline")) {
            return "adrenaline_effect";
        }
        return "bandage_effect";
    }
}
