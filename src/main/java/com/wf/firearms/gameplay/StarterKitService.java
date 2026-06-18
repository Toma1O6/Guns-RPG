package com.wf.firearms.gameplay;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wf.firearms.GunsRpg;
import com.wf.firearms.compat.tacz.TaczGiveGun;
import com.wf.firearms.config.TaczBackendConfig;
import com.wf.firearms.data.PlayerFirearmsData;
import com.wf.firearms.data.PortPaths;
import com.wf.firearms.network.FirearmsProgressSyncPacket;
import com.wf.firearms.registry.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * 首次进世界可选礼包（默认关闭，由 {@code config/gunsrpg/starter_kit.json} 控制）。
 * 整合包新手引导请走 FTB 任务；调试可用 {@code /gunsrpg starter_kit reapply}。
 */
public final class StarterKitService {
    private static final String FLAG = "starter_kit_applied";

    private static boolean enabled;
    private static int skillPoints;
    private static final List<String> autoUnlock = new ArrayList<>();
    private static final List<ItemStack> starterItems = new ArrayList<>();
    private static final List<ItemStack> optionalItems = new ArrayList<>();
    private static String welcome = "";

    private StarterKitService() {}

    public static void reload() {
        enabled = false;
        autoUnlock.clear();
        starterItems.clear();
        optionalItems.clear();
        skillPoints = 0;
        welcome = "";

        Path file = PortPaths.configRoot().resolve("starter_kit.json");
        if (!Files.isRegularFile(file)) {
            GunsRpg.LOGGER.info("[gunsrpg] 未找到 starter_kit.json，新手礼包默认关闭（由 FTB 等外部引导）");
            return;
        }
        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            if (root.has("enabled")) {
                enabled = root.get("enabled").getAsBoolean();
            }
            if (root.has("skill_points")) {
                skillPoints = Math.max(0, root.get("skill_points").getAsInt());
            }
            if (root.has("welcome")) {
                welcome = root.get("welcome").getAsString();
            }
            readStringArray(root, "auto_unlock", autoUnlock);
            readItems(root, "starter_items", starterItems);
            readItems(root, "optional_items", optionalItems);
            if (enabled && TaczBackendConfig.useTaczShooting()) {
                replaceCgmStarterWithTacz();
            }
            if (enabled) {
                GunsRpg.LOGGER.info(
                        "[gunsrpg] 新手礼包已启用：{} 技能点，{} 项自动解锁，物品 {}+{}",
                        skillPoints,
                        autoUnlock.size(),
                        starterItems.size(),
                        optionalItems.size());
            } else {
                GunsRpg.LOGGER.info("[gunsrpg] 新手礼包已关闭（starter_kit.enabled=false）");
            }
        } catch (Exception e) {
            GunsRpg.LOGGER.warn("[gunsrpg] 读取 starter_kit.json 失败，新手礼包保持关闭", e);
            enabled = false;
        }
    }

    /** starter_kit.json 仍写 CGM 物品时，TaCZ 模式下替换为 TaCZ 枪弹（仅 enabled 时）。 */
    private static void replaceCgmStarterWithTacz() {
        boolean hasCgm = starterItems.stream().anyMatch(StarterKitService::isCgmStack);
        if (!hasCgm) {
            return;
        }
        starterItems.removeIf(StarterKitService::isCgmStack);
        applyTaczStarterDefaults();
    }

    private static boolean isCgmStack(ItemStack stack) {
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(stack.getItem());
        return id != null && "cgm".equals(id.getNamespace());
    }

    private static void applyTaczStarterDefaults() {
        ItemStack gun = TaczGiveGun.create("m1911", true);
        if (!gun.isEmpty()) {
            starterItems.add(gun);
        }
        starterItems.add(new ItemStack(ModItems.WOODEN_9MM.get(), 32));
        starterItems.add(new ItemStack(ModItems.WOODEN_45ACP.get(), 16));
    }

    public static void tryApply(ServerPlayer player) {
        if (PlayerFirearmsData.root(player).getBoolean(FLAG)) {
            return;
        }
        PlayerFirearmsData.migrateLegacyKills(player);

        if (!enabled) {
            PlayerFirearmsData.root(player).putBoolean(FLAG, true);
            return;
        }

        if (skillPoints > 0) {
            PlayerFirearmsData.addSkillPoints(player, skillPoints);
        }
        for (String skillId : autoUnlock) {
            PlayerFirearmsData.unlock(player, skillId);
        }
        for (ItemStack stack : starterItems) {
            give(player, stack);
        }
        for (ItemStack stack : optionalItems) {
            give(player, stack);
        }

        PlayerFirearmsData.root(player).putBoolean(FLAG, true);
        if (welcome != null && !welcome.isBlank()) {
            player.sendSystemMessage(Component.literal(welcome), false);
        }
        FirearmsProgressSyncPacket.sendTo(player);
    }

    /** 调试用：重置并重新发放 */
    public static void reapply(ServerPlayer player) {
        PlayerFirearmsData.root(player).remove(FLAG);
        tryApply(player);
    }

    private static void give(ServerPlayer player, ItemStack stack) {
        if (stack.isEmpty()) {
            return;
        }
        if (!player.getInventory().add(stack.copy())) {
            ItemEntity drop = player.drop(stack.copy(), false);
            if (drop != null) {
                drop.setNoPickUpDelay();
                drop.setTarget(player.getUUID());
            }
        }
    }

    private static ItemStack stack(String id, int count) {
        ResourceLocation loc = ResourceLocation.tryParse(id);
        if (loc == null) {
            return ItemStack.EMPTY;
        }
        Item item = ForgeRegistries.ITEMS.getValue(loc);
        if (item == null) {
            GunsRpg.LOGGER.warn("[gunsrpg] 新手礼包未知物品: {}", id);
            return ItemStack.EMPTY;
        }
        return new ItemStack(item, count);
    }

    private static void readStringArray(JsonObject root, String key, List<String> out) {
        if (!root.has(key)) {
            return;
        }
        JsonArray arr = root.getAsJsonArray(key);
        for (JsonElement el : arr) {
            out.add(el.getAsString());
        }
    }

    private static void readItems(JsonObject root, String key, List<ItemStack> out) {
        if (!root.has(key)) {
            return;
        }
        for (JsonElement el : root.getAsJsonArray(key)) {
            JsonObject o = el.getAsJsonObject();
            String id = o.get("id").getAsString();
            int count = o.has("count") ? o.get("count").getAsInt() : 1;
            ItemStack s = stack(id, count);
            if (!s.isEmpty()) {
                out.add(s);
            }
        }
    }
}
