package com.wf.firearms.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.wf.firearms.GunsRpg;
import com.wf.firearms.airdrop.AirdropService;
import com.wf.firearms.bloodmoon.BloodmoonService;
import com.wf.firearms.config.AirdropConfig;
import com.wf.firearms.config.BloodmoonConfig;
import com.wf.firearms.config.GunshotAlertConfig;
import com.wf.firearms.combat.FirearmRegistry;
import com.wf.firearms.data.PlayerFirearmsData;
import com.wf.firearms.data.WeaponMapping;
import com.wf.firearms.data.SkillDatabase;
import com.wf.firearms.debuff.DebuffCause;
import com.wf.firearms.debuff.DebuffConfig;
import com.wf.firearms.debuff.DebuffCureRegistry;
import com.wf.firearms.debuff.DebuffService;
import com.wf.firearms.debuff.DebuffType;
import com.wf.firearms.gameplay.SkillUnlockService;
import com.wf.firearms.gameplay.StarterKitService;
import com.wf.firearms.network.FirearmsProgressSyncPacket;
import com.wf.firearms.registry.ModItems;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.item.Item;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public final class GunsRpgCommands {
    private GunsRpgCommands() {}

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("gunsrpg")
                        .requires(src -> src.hasPermission(0))
                        .then(
                                Commands.literal("reload")
                                        .requires(src -> src.hasPermission(2))
                                        .executes(ctx -> {
                                            SkillDatabase.reload();
                                            AirdropConfig.reload();
                                            BloodmoonConfig.reload();
                                            GunshotAlertConfig.reload();
                                            int nodes = SkillDatabase.getAllNodes().size();
                                            ctx.getSource()
                                                    .sendSuccess(
                                                            () -> Component.literal(
                                                                    "[gunsrpg] 重载完成，节点数="
                                                                            + nodes
                                                                            + (SkillDatabase.isLoaded()
                                                                                    ? ""
                                                                                    : "，错误："
                                                                                            + SkillDatabase
                                                                                                    .getLoadError())),
                                                            true);
                                            return 1;
                                        }))
                        .then(
                                Commands.literal("status")
                                        .executes(
                                                ctx -> {
                                                    ctx.getSource()
                                                            .sendSuccess(
                                                                    () -> Component.literal(
                                                                            "[gunsrpg] loaded="
                                                                                    + SkillDatabase.isLoaded()
                                                                                    + " nodes="
                                                                                    + SkillDatabase.getAllNodes()
                                                                                            .size()
                                                                                    + " perks="
                                                                                    + SkillDatabase.getAllPerks()
                                                                                            .size()),
                                                                    false);
                                                    return 1;
                                                }))
                        .then(
                                Commands.literal("level")
                                        .requires(src -> src.hasPermission(2))
                                        .then(
                                                Commands.argument("value", IntegerArgumentType.integer(0, 999))
                                                        .executes(ctx -> {
                                                            ServerPlayer player = ctx.getSource().getPlayer();
                                                            if (player == null) {
                                                                return 0;
                                                            }
                                                            int level =
                                                                    IntegerArgumentType.getInteger(ctx, "value");
                                                            PlayerFirearmsData.setPlayerLevel(player, level);
                                                            FirearmsProgressSyncPacket.sendTo(player);
                                                            ctx.getSource()
                                                                    .sendSuccess(
                                                                            () -> Component.literal(
                                                                                    "火器等级已设为 " + level),
                                                                            true);
                                                            return 1;
                                                        })))
                        .then(
                                Commands.literal("points")
                                        .requires(src -> src.hasPermission(2))
                                        .then(
                                                Commands.argument("amount", IntegerArgumentType.integer(1, 999))
                                                        .executes(
                                                                ctx -> {
                                                                    ServerPlayer player = ctx.getSource().getPlayer();
                                                                    if (player == null) {
                                                                        return 0;
                                                                    }
                                                                    int n = IntegerArgumentType.getInteger(ctx, "amount");
                                                                    PlayerFirearmsData.addSkillPoints(player, n);
                                                                    FirearmsProgressSyncPacket.sendTo(player);
                                                                    ctx.getSource()
                                                                            .sendSuccess(
                                                                                    () -> Component.literal("已添加 " + n + " 技能点"),
                                                                                    true);
                                                                    return 1;
                                                                })))
                        .then(
                                Commands.literal("unlock")
                                        .requires(src -> src.hasPermission(2))
                                        .then(
                                                Commands.argument("skill", StringArgumentType.string())
                                                        .executes(
                                                                ctx -> {
                                                                    ServerPlayer player = ctx.getSource().getPlayer();
                                                                    if (player == null) {
                                                                        return 0;
                                                                    }
                                                                    String id = StringArgumentType.getString(ctx, "skill");
                                                                    var result = SkillUnlockService.tryUnlock(player, id);
                                                                    ctx.getSource()
                                                                            .sendSuccess(
                                                                                    () -> Component.literal(result.message()),
                                                                                    true);
                                                                    return result.success() ? 1 : 0;
                                                                })))
                        .then(
                                Commands.literal("give")
                                        .requires(src -> src.hasPermission(2))
                                        .then(
                                                Commands.literal("gun")
                                                        .then(
                                                                Commands.argument(
                                                                                "weapon",
                                                                                StringArgumentType.string())
                                                                        .executes(ctx -> {
                                                                            ServerPlayer player =
                                                                                    ctx.getSource().getPlayer();
                                                                            if (player == null) {
                                                                                return 0;
                                                                            }
                                                                            String key =
                                                                                    StringArgumentType.getString(
                                                                                            ctx, "weapon");
                                                                            if (FirearmRegistry.get(key).isEmpty()) {
                                                                                ctx.getSource()
                                                                                        .sendFailure(
                                                                                                Component.literal(
                                                                                                        "未知枪械 key："
                                                                                                                + key));
                                                                                return 0;
                                                                            }
                                                                            ItemStack stack = WeaponMapping.stackForWeapon(key);
                                                                            if (stack.isEmpty()
                                                                                    || !player.getInventory().add(stack.copy())) {
                                                                                player.drop(stack.copy(), false);
                                                                            }
                                                                            ctx.getSource()
                                                                                    .sendSuccess(
                                                                                            () -> Component.literal(
                                                                                                    "已发放 "
                                                                                                            + WeaponMapping
                                                                                                                    .displayNameForWeaponKey(
                                                                                                                            key)),
                                                                                            true);
                                                                            return 1;
                                                                        })))
                                        .then(
                                                Commands.literal("spawn_egg")
                                                        .then(
                                                                Commands.argument(
                                                                                "mob",
                                                                                StringArgumentType.string())
                                                                        .executes(ctx -> {
                                                                            ServerPlayer player =
                                                                                    ctx.getSource().getPlayer();
                                                                            if (player == null) {
                                                                                return 0;
                                                                            }
                                                                            String mob =
                                                                                    StringArgumentType.getString(
                                                                                            ctx, "mob");
                                                                            Item egg =
                                                                                    switch (mob) {
                                                                                        case "bloodmoon_golem",
                                                                                                "golem" ->
                                                                                                ModItems
                                                                                                        .BLOODMOON_GOLEM_SPAWN_EGG
                                                                                                        .get();
                                                                                        case "rocket_angel",
                                                                                                "angel" ->
                                                                                                ModItems
                                                                                                        .ROCKET_ANGEL_SPAWN_EGG
                                                                                                        .get();
                                                                                        case "zombie_gunner",
                                                                                                "gunner" ->
                                                                                                ModItems
                                                                                                        .ZOMBIE_GUNNER_SPAWN_EGG
                                                                                                        .get();
                                                                                        case "explosive_skeleton",
                                                                                                "grenadier" ->
                                                                                                ModItems
                                                                                                        .EXPLOSIVE_SKELETON_SPAWN_EGG
                                                                                                        .get();
                                                                                        default -> null;
                                                                                    };
                                                                            if (egg == null) {
                                                                                ctx.getSource()
                                                                                        .sendFailure(
                                                                                                Component.literal(
                                                                                                        "可用: bloodmoon_golem, rocket_angel, zombie_gunner, explosive_skeleton"));
                                                                                return 0;
                                                                            }
                                                                            ItemStack stack = new ItemStack(egg);
                                                                            if (!player.getInventory().add(stack)) {
                                                                                player.drop(stack, false);
                                                                            }
                                                                            ctx.getSource()
                                                                                    .sendSuccess(
                                                                                            () -> Component.literal(
                                                                                                    "已发放刷怪蛋: "
                                                                                                            + mob),
                                                                                            true);
                                                                            return 1;
                                                                        }))))
                        .then(
                                Commands.literal("bootstrap")
                                        .requires(src -> src.hasPermission(2))
                                        .executes(ctx -> {
                                            ServerPlayer player = ctx.getSource().getPlayer();
                                            if (player == null) {
                                                return 0;
                                            }
                                            StarterKitService.reapply(player);
                                            ctx.getSource()
                                                    .sendSuccess(
                                                            () -> Component.literal("已重新发放新手火器礼包"),
                                                            true);
                                            return 1;
                                        }))
                        .then(
                                Commands.literal("airdrop")
                                        .requires(src -> src.hasPermission(2))
                                        .then(
                                                Commands.literal("test")
                                                        .executes(GunsRpgCommands::executeAirdropSpawn))
                                        .then(
                                                Commands.literal("spawn")
                                                        .executes(GunsRpgCommands::executeAirdropSpawn)))
                        .then(buildBloodmoonCommands())
                        .then(buildDebuffCommands()));
    }

    private static int executeAirdropSpawn(CommandContext<CommandSourceStack> ctx) {
        ServerLevel level = ctx.getSource().getLevel();
        if (level.dimension() != net.minecraft.world.level.Level.OVERWORLD) {
            ctx.getSource().sendFailure(Component.literal("空投仅能在主世界生成"));
            return 0;
        }
        return AirdropService.forceSpawn(level)
                .map(
                        pos -> {
                            ctx.getSource()
                                    .sendSuccess(() -> Component.literal("空投已投放"), true);
                            return 1;
                        })
                .orElseGet(
                        () -> {
                            ctx.getSource().sendFailure(Component.literal("空投生成失败"));
                            return 0;
                        });
    }

    private static com.mojang.brigadier.tree.LiteralCommandNode<CommandSourceStack> buildBloodmoonCommands() {
        return Commands.literal("bloodmoon")
                .requires(src -> src.hasPermission(2))
                .then(
                        Commands.literal("start")
                                .executes(ctx -> {
                                    ServerLevel level = ctx.getSource().getLevel();
                                    if (level.dimension() != net.minecraft.world.level.Level.OVERWORLD) {
                                        ctx.getSource().sendFailure(Component.literal("血月仅能在主世界触发"));
                                        return 0;
                                    }
                                    if (BloodmoonService.forceStart(level)) {
                                        ctx.getSource()
                                                .sendSuccess(
                                                        () -> Component.literal("血月已强制开启（/gunsrpg bloodmoon stop 可结束）"),
                                                        true);
                                        return 1;
                                    }
                                    return 0;
                                }))
                .then(
                        Commands.literal("stop")
                                .executes(ctx -> {
                                    ServerLevel level = ctx.getSource().getLevel();
                                    BloodmoonService.forceStop(level);
                                    ctx.getSource()
                                            .sendSuccess(() -> Component.literal("血月已结束"), true);
                                    return 1;
                                }))
                .then(
                        Commands.literal("status")
                                .executes(ctx -> {
                                    ServerLevel level = ctx.getSource().getLevel();
                                    String line = BloodmoonService.statusLine(level);
                                    ctx.getSource()
                                            .sendSuccess(() -> Component.literal("[血月] " + line), false);
                                    return 1;
                                }))
                .build();
    }

    private static com.mojang.brigadier.tree.LiteralCommandNode<CommandSourceStack> buildDebuffCommands() {
        var root = Commands.literal("debuff").requires(src -> src.hasPermission(2));
        root.then(
                        Commands.literal("help")
                                .executes(ctx -> {
                                    sendDebuffHelp(ctx.getSource());
                                    return 1;
                                }))
                .then(
                        Commands.literal("clear")
                                .executes(ctx -> executeDebuffClear(ctx.getSource())))
                .then(
                        Commands.literal("status")
                                .executes(ctx -> executeDebuffStatus(ctx.getSource())))
                .then(
                        Commands.literal("all")
                                .executes(ctx -> executeDebuffAll(ctx.getSource(), 2))
                                .then(
                                        Commands.argument("stage", IntegerArgumentType.integer(1, 4))
                                                .executes(
                                                        ctx -> executeDebuffAll(
                                                                ctx.getSource(),
                                                                IntegerArgumentType.getInteger(ctx, "stage")))))
                .then(debuffTypeCommand("bleed", DebuffType.BLEED))
                .then(debuffTypeCommand("fracture", DebuffType.FRACTURE))
                .then(debuffTypeCommand("poison", DebuffType.POISON))
                .then(debuffTypeCommand("infection", DebuffType.INFECTION))
                .then(
                        Commands.literal("meds")
                                .executes(ctx -> executeGiveMeds(ctx.getSource(), null))
                                .then(
                                        Commands.argument("item", StringArgumentType.string())
                                                .executes(
                                                        ctx -> executeGiveMeds(
                                                                ctx.getSource(),
                                                                StringArgumentType.getString(ctx, "item")))))
                .then(
                        Commands.literal("apply")
                                .then(
                                        Commands.argument("type", StringArgumentType.string())
                                                .executes(
                                                        ctx -> executeDebuffApply(
                                                                ctx.getSource(),
                                                                StringArgumentType.getString(ctx, "type"),
                                                                2))
                                                .then(
                                                        Commands.argument(
                                                                        "stage",
                                                                        IntegerArgumentType.integer(1, 4))
                                                                .executes(
                                                                        ctx -> executeDebuffApply(
                                                                                ctx.getSource(),
                                                                                StringArgumentType.getString(
                                                                                        ctx, "type"),
                                                                                IntegerArgumentType.getInteger(
                                                                                        ctx, "stage"))))));
        return root.build();
    }

    private static com.mojang.brigadier.builder.LiteralArgumentBuilder<CommandSourceStack> debuffTypeCommand(
            String literal, DebuffType type) {
        if (type == DebuffType.FRACTURE) {
            return Commands.literal(literal).executes(ctx -> executeDebuffApply(ctx.getSource(), type.getId(), 1));
        }
        int max = maxStage(type);
        return Commands.literal(literal)
                .executes(ctx -> executeDebuffApply(ctx.getSource(), type.getId(), Math.min(2, max)))
                .then(
                        Commands.argument("stage", IntegerArgumentType.integer(1, max))
                                .executes(
                                        ctx -> executeDebuffApply(
                                                ctx.getSource(),
                                                type.getId(),
                                                IntegerArgumentType.getInteger(ctx, "stage"))));
    }

    private static int executeDebuffClear(CommandSourceStack source) {
        ServerPlayer player = source.getPlayer();
        if (player == null) {
            return 0;
        }
        DebuffService.clearAll(player);
        source.sendSuccess(() -> Component.literal("§a已清除全部 Debuff"), true);
        return 1;
    }

    private static int executeDebuffStatus(CommandSourceStack source) {
        ServerPlayer player = source.getPlayer();
        if (player == null) {
            return 0;
        }
        String line = DebuffService.statusLine(player);
        source.sendSuccess(() -> Component.literal("Debuff: " + line), false);
        return 1;
    }

    private static int executeDebuffAll(CommandSourceStack source, int stage) {
        ServerPlayer player = source.getPlayer();
        if (player == null) {
            return 0;
        }
        DebuffService.applyStage(
                player, DebuffType.BLEED, clampStage(DebuffType.BLEED, stage), DebuffCause.COMMAND, true);
        DebuffService.applyStage(
                player, DebuffType.POISON, clampStage(DebuffType.POISON, stage), DebuffCause.COMMAND, true);
        DebuffService.applyStage(
                player, DebuffType.INFECTION, clampStage(DebuffType.INFECTION, stage), DebuffCause.COMMAND, true);
        DebuffService.applyFracture(player, DebuffCause.COMMAND, true);
        source.sendSuccess(
                () -> Component.literal(
                        "§c已施加全部 Debuff（阶段 "
                                + stage
                                + " + 骨折）。用 §e/gunsrpg debuff meds§c 领取测试药品。"),
                true);
        return 1;
    }

    private static int executeDebuffApply(CommandSourceStack source, String rawType, int stage) {
        ServerPlayer player = source.getPlayer();
        if (player == null) {
            return 0;
        }
        DebuffType type = DebuffType.fromId(rawType.toLowerCase(Locale.ROOT));
        if (type == null) {
            source.sendFailure(Component.literal("未知类型，可用: bleed, fracture, poison, infection"));
            return 0;
        }
        if (type == DebuffType.FRACTURE) {
            DebuffService.applyFracture(player, DebuffCause.COMMAND, true);
            source.sendSuccess(
                    () -> Component.literal("§c已施加骨折。测试清除: §egunsrpg:plaster_cast§r（石膏）"),
                    true);
            return 1;
        }
        int applied = clampStage(type, stage);
        DebuffService.applyStage(player, type, applied, DebuffCause.COMMAND, true);
        source.sendSuccess(
                () -> Component.literal(
                        "§c已施加 "
                                + type.displayName().getString()
                                + " 阶段 "
                                + applied
                                + "。建议药品: "
                                + medHintFor(type)),
                true);
        return 1;
    }

    private static int executeGiveMeds(CommandSourceStack source, String rawItem) {
        ServerPlayer player = source.getPlayer();
        if (player == null) {
            return 0;
        }
        if (rawItem == null || rawItem.isBlank()) {
            int count = 0;
            for (Item item : testMedItems().values()) {
                giveItem(player, item);
                count++;
            }
            int given = count;
            source.sendSuccess(
                    () -> Component.literal("§a已发放 " + given + " 种 Debuff 测试药品（各 8 个）"), true);
            return 1;
        }
        Optional<Item> item = resolveTestMed(rawItem);
        if (item.isEmpty()) {
            source.sendFailure(
                    Component.literal(
                            "未知药品。可用: "
                                    + String.join(", ", testMedItems().keySet())
                                    + "；或不带参数一次领取全部"));
            return 0;
        }
        giveItem(player, item.get());
        source.sendSuccess(
                () -> Component.literal("§a已发放 8× " + rawItem.toLowerCase(Locale.ROOT)), true);
        return 1;
    }

    private static void giveItem(ServerPlayer player, Item item) {
        ItemStack stack = new ItemStack(item, 8);
        if (!player.getInventory().add(stack)) {
            player.drop(stack, false);
        }
    }

    private static void sendDebuffHelp(CommandSourceStack source) {
        source.sendSuccess(
                () -> Component.literal(
                        """
                                §6[gunsrpg debuff 测试]
                                §e/gunsrpg debuff bleed [1-4]§r — 出血
                                §e/gunsrpg debuff fracture§r — 骨折
                                §e/gunsrpg debuff poison [1-3]§r — 中毒
                                §e/gunsrpg debuff infection [1-3]§r — 感染
                                §e/gunsrpg debuff all [阶段]§r — 一次施加全部
                                §e/gunsrpg debuff clear§r — 清除
                                §e/gunsrpg debuff status§r — 当前状态
                                §e/gunsrpg debuff meds§r — 发放全部测试药品
                                §e/gunsrpg debuff meds <名>§r — 发放指定药品
                                §7读条完成后生效；止血剂仅延缓出血恶化（HUD 显示 RESIST）"""),
                false);
        source.sendSuccess(
                () -> Component.literal(
                        "§7bandage→出血-1 | plaster→清骨折 | antidotum→清中毒 | vaccine→清感染 | hemostat→止血延缓"),
                false);
    }

    private static Map<String, Item> testMedItems() {
        Map<String, Item> map = new LinkedHashMap<>();
        map.put("bandage", ModItems.BANDAGE.get());
        map.put("hemostat", ModItems.HEMOSTAT.get());
        map.put("plaster", ModItems.PLASTER_CAST.get());
        map.put("plaster_cast", ModItems.PLASTER_CAST.get());
        map.put("antidotum", ModItems.ANTIDOTUM_PILLS.get());
        map.put("antidotum_pills", ModItems.ANTIDOTUM_PILLS.get());
        map.put("vaccine", ModItems.VACCINE.get());
        for (var entry : DebuffCureRegistry.allCures().entrySet()) {
            Item item = ForgeRegistries.ITEMS.getValue(entry.getKey());
            if (item != null && item != net.minecraft.world.item.Items.AIR) {
                String key = entry.getKey().getPath();
                map.putIfAbsent(key, item);
                map.putIfAbsent(entry.getKey().toString(), item);
            }
        }
        return map;
    }

    private static Optional<Item> resolveTestMed(String raw) {
        String key = raw.toLowerCase(Locale.ROOT).trim();
        Map<String, Item> meds = testMedItems();
        if (meds.containsKey(key)) {
            return Optional.of(meds.get(key));
        }
        ResourceLocation id = ResourceLocation.tryParse(key);
        if (id == null) {
            id = ResourceLocation.fromNamespaceAndPath(GunsRpg.MOD_ID, key);
        }
        Item item = ForgeRegistries.ITEMS.getValue(id);
        return item != null && item != net.minecraft.world.item.Items.AIR
                ? Optional.of(item)
                : Optional.empty();
    }

    private static String medHintFor(DebuffType type) {
        return switch (type) {
            case BLEED -> "bandage(降1级), hemostat(RESIST)";
            case FRACTURE -> "plaster_cast";
            case POISON -> "antidotum_pills";
            case INFECTION -> "vaccine";
        };
    }

    private static int maxStage(DebuffType type) {
        return switch (type) {
            case BLEED -> DebuffConfig.bleed().maxStage();
            case POISON -> DebuffConfig.poison().maxStage();
            case INFECTION -> DebuffConfig.infection().maxStage();
            case FRACTURE -> 1;
        };
    }

    private static int clampStage(DebuffType type, int stage) {
        return Math.max(1, Math.min(maxStage(type), stage));
    }
}
