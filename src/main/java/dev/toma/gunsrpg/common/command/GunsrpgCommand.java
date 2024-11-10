package dev.toma.gunsrpg.common.command;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.attribute.IAttribute;
import dev.toma.gunsrpg.api.common.attribute.IAttributeId;
import dev.toma.gunsrpg.api.common.attribute.IAttributeModifier;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.data.*;
import dev.toma.gunsrpg.api.common.skill.ISkillHierarchy;
import dev.toma.gunsrpg.api.common.skill.ITransactionValidator;
import dev.toma.gunsrpg.api.common.skill.ITransactionValidatorFactory;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.debuffs.IDebuffType;
import dev.toma.gunsrpg.common.entity.AirdropEntity;
import dev.toma.gunsrpg.common.entity.MayorEntity;
import dev.toma.gunsrpg.common.init.ModEntities;
import dev.toma.gunsrpg.common.init.ModRegistries;
import dev.toma.gunsrpg.common.item.guns.setup.AbstractGun;
import dev.toma.gunsrpg.common.item.perk.Crystal;
import dev.toma.gunsrpg.common.item.perk.CrystalAttribute;
import dev.toma.gunsrpg.common.item.perk.CrystalItem;
import dev.toma.gunsrpg.common.perk.Perk;
import dev.toma.gunsrpg.common.perk.PerkRegistry;
import dev.toma.gunsrpg.common.perk.PerkType;
import dev.toma.gunsrpg.common.quests.QuestSystem;
import dev.toma.gunsrpg.common.quests.condition.IQuestCondition;
import dev.toma.gunsrpg.common.quests.condition.IQuestConditionProvider;
import dev.toma.gunsrpg.common.quests.condition.list.WeightedConditionList;
import dev.toma.gunsrpg.common.quests.quest.*;
import dev.toma.gunsrpg.common.quests.reward.IQuestItemProvider;
import dev.toma.gunsrpg.common.quests.reward.QuestReward;
import dev.toma.gunsrpg.common.quests.reward.QuestRewardList;
import dev.toma.gunsrpg.common.quests.reward.QuestRewardManager;
import dev.toma.gunsrpg.common.quests.sharing.GroupInvite;
import dev.toma.gunsrpg.common.quests.sharing.QuestingGroup;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.common.skills.core.TransactionValidatorRegistry;
import dev.toma.gunsrpg.util.helper.CommandHelper;
import dev.toma.gunsrpg.util.object.Interaction;
import dev.toma.gunsrpg.world.cap.QuestingDataProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.ResourceLocationArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.IServerWorldInfo;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.server.command.EnumArgument;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GunsrpgCommand {

    private static final SuggestionProvider<CommandSource> DEBUFF_SUGGESTION = (context, builder) -> ISuggestionProvider.suggestResource(ModRegistries.DEBUFFS.getValues().stream()
            .filter(IDebuffType::isToggleable)
            .map(ForgeRegistryEntry::getRegistryName)
            .collect(Collectors.toSet()), builder);
    private static final SuggestionProvider<CommandSource> TX_VALIDATOR_SUGGESTION = (context, builder) -> ISuggestionProvider.suggestResource(TransactionValidatorRegistry.getRegisteredValidatorTypes(), builder);
    private static final SuggestionProvider<CommandSource> SKILL_SUGGESTION = (context, builder) -> ISuggestionProvider.suggestResource(ModRegistries.SKILLS.getKeys(), builder);
    private static final SuggestionProvider<CommandSource> ATTRIBUTE_SUGGESTION = (context, builder) -> ISuggestionProvider.suggestResource(Attribs.listKeys(), builder);
    private static final SuggestionProvider<CommandSource> PERK_SUGGESTION = (context, builder) -> ISuggestionProvider.suggestResource(PerkRegistry.getRegistry().getPerkIds(), builder);
    private static final SuggestionProvider<CommandSource> QUEST_SUGGESTION = (context, builder) -> ISuggestionProvider.suggestResource(GunsRPG.getModLifecycle().quests().getQuestManager().getQuestIds(), builder);
    private static final SimpleCommandExceptionType NO_DATA = new SimpleCommandExceptionType(new TranslationTextComponent("command.gunsrpg.exception.no_data"));
    private static final SimpleCommandExceptionType MISSING_KEY_EXCEPTION = new SimpleCommandExceptionType(new TranslationTextComponent("command.gunsrpg.exception.missing_key"));
    private static final DynamicCommandExceptionType MISSING_ARGUMENTS = new DynamicCommandExceptionType(o -> new TranslationTextComponent("command.gunsrpg.exception.missing_args", o));
    private static final SimpleCommandExceptionType LOCATION_OBSTRUCTED = new SimpleCommandExceptionType(new TranslationTextComponent("command.gunsrpg.exception.location_obstructed"));
    private static final DynamicCommandExceptionType UNKNOWN_KEY_EXCEPTION = new DynamicCommandExceptionType(data -> new TranslationTextComponent("command.gunsrpg.exception.unknown_key", data));
    private static final SimpleCommandExceptionType INVALID_TX_DATA = new SimpleCommandExceptionType(new TranslationTextComponent("command.gunsrpg.exception.invalid_tx_data"));
    private static final DynamicCommandExceptionType REQUIRED_SKILL = new DynamicCommandExceptionType(id -> new TranslationTextComponent("command.gunsrpg.exception.missing_skill", id));
    private static final DynamicCommandExceptionType DEPENDENT_SKILL_ACTIVE = new DynamicCommandExceptionType(id -> new TranslationTextComponent("command.gunsrpg.exception.active_skill", id));
    private static final SimpleCommandExceptionType NOT_HOLDING_CRYSTAL = new SimpleCommandExceptionType(new TranslationTextComponent("command.gunsrpg.exception.not_holding_crystal"));
    private static final SimpleCommandExceptionType NO_ACTIVE_QUEST = new SimpleCommandExceptionType(new TranslationTextComponent("command.gunsrpg.exception.no_quest"));
    private static final SimpleCommandExceptionType NOT_HOLDING_GUN = new SimpleCommandExceptionType(new TranslationTextComponent("command.gunsrpg.exception.not_holding_weapon"));
    private static final SimpleCommandExceptionType NOT_A_GROUP_LEADER = new SimpleCommandExceptionType(new TranslationTextComponent("command.gunsrpg.exception.not_group_leader"));
    private static final DynamicCommandExceptionType GROUP_INVITE_FAILED = new DynamicCommandExceptionType(msg -> new TranslationTextComponent("command.gunsrpg.exception.group_invite_failed", msg));
    private static final SimpleCommandExceptionType INVITE_NOT_FOUND = new SimpleCommandExceptionType(new TranslationTextComponent("command.gunsrpg.exception.invite_not_found"));
    private static final DynamicCommandExceptionType INVITE_ACCEPT_FAILED = new DynamicCommandExceptionType(msg -> new TranslationTextComponent("command.gunsrpg.exception.invite_accept_failed", msg));
    private static final DynamicCommandExceptionType INVITE_REJECT_FAILED = new DynamicCommandExceptionType(msg -> new TranslationTextComponent("command.gunsrpg.exception.invite_reject_failed", msg));
    private static final DynamicCommandExceptionType INVITE_CANCEL_FAILED = new DynamicCommandExceptionType(msg -> new TranslationTextComponent("command.gunsrpg.exception.invite_cancel_failed", msg));

    public static void registerCommandTree(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                Commands.literal("gunsrpg")
                        .requires(src -> src.hasPermission(2) && src.getEntity() instanceof PlayerEntity)
                        .then(
                                Commands.literal("debuff")
                                        .then(
                                                Commands.argument("registryKey", ResourceLocationArgument.id())
                                                        .suggests(DEBUFF_SUGGESTION)
                                                        .executes(GunsrpgCommand::toggleDebuff)
                                        )
                        )
                        .then(
                                Commands.literal("event")
                                        .then(
                                                Commands.literal("bloodmoon")
                                                        .executes(GunsrpgCommand::forceBloodmoon)
                                        )
                                        .then(
                                                Commands.literal("airdrop")
                                                        .executes(GunsrpgCommand::callAirdrop)
                                        )
                        )
                        .then(
                                Commands.literal("progression")
                                        .then(
                                                Commands.literal("reset")
                                                        .executes(ctx -> forceProgression(ctx, ModifyAction.LOCK))
                                        )
                                        .then(
                                                Commands.literal("unlockAll")
                                                        .executes(ctx -> forceProgression(ctx, ModifyAction.UNLOCK))
                                        )
                                        .then(
                                                Commands.argument("txType", ResourceLocationArgument.id())
                                                        .suggests(TX_VALIDATOR_SUGGESTION)
                                                        .then(
                                                                Commands.literal("level")
                                                                        .executes(GunsrpgCommand::addLevel)
                                                                        .then(
                                                                                Commands.argument("amount", IntegerArgumentType.integer(1, 100))
                                                                                        .executes(GunsrpgCommand::addLevels)
                                                                                        .then(
                                                                                                Commands.argument("txData", JsonArgumentType.json())
                                                                                                        .executes(GunsrpgCommand::addLevelsSpecific)
                                                                                        )
                                                                        )
                                                        )
                                                        .then(
                                                                Commands.literal("point")
                                                                        .executes(GunsrpgCommand::addPoint)
                                                                        .then(
                                                                                Commands.argument("amount", IntegerArgumentType.integer(1))
                                                                                        .executes(GunsrpgCommand::addPoints)
                                                                                        .then(
                                                                                                Commands.argument("txData", JsonArgumentType.json())
                                                                                                        .executes(GunsrpgCommand::addPointsSpecific)
                                                                                        )
                                                                        )
                                                        )
                                                        .then(
                                                                Commands.literal("progress")
                                                                        .executes(ctx -> progressAdd(ctx, 1, null))
                                                                        .then(
                                                                                Commands.argument("amount", IntegerArgumentType.integer(1))
                                                                                        .executes(ctx -> progressAdd(ctx, IntegerArgumentType.getInteger(ctx, "amount"), null))
                                                                                        .then(
                                                                                                Commands.argument("txData", JsonArgumentType.json())
                                                                                                        .executes(ctx -> progressAdd(ctx, IntegerArgumentType.getInteger(ctx, "amount"), JsonArgumentType.getJson(ctx, "txData")))
                                                                                        )
                                                                        )
                                                        )
                                                        .executes(ctx -> noArgsProvided("level", "point", "progress"))
                                        )
                                        .executes(ctx -> noArgsProvided("reset", "unlockAll"))
                        )
                        .then(
                                Commands.literal("skill")
                                        .then(
                                                Commands.argument("skillId", ResourceLocationArgument.id())
                                                        .suggests(SKILL_SUGGESTION)
                                                        .then(
                                                                Commands.literal("unlock")
                                                                        .executes(GunsrpgCommand::unlockSkill)
                                                        )
                                                        .then(
                                                                Commands.literal("lock")
                                                                        .executes(GunsrpgCommand::lockSkill)
                                                        )
                                                        .executes(ctx -> noArgsProvided("unlock", "lock"))
                                        )
                                        .executes(ctx -> {
                                            throw MISSING_KEY_EXCEPTION.create();
                                        })
                        )
                        .then(
                                Commands.literal("attribute")
                                        .executes(ctx -> noArgsProvided("id"))
                                        .then(
                                                Commands.argument("id", ResourceLocationArgument.id())
                                                        .suggests(ATTRIBUTE_SUGGESTION)
                                                        .executes(GunsrpgCommand::listAttributeInfo)
                                        )
                        )
                        .then(
                                Commands.literal("crystal")
                                        .executes(ctx -> noArgsProvided("id", "level", "generate"))
                                        .then(
                                                Commands.argument("perk", ResourceLocationArgument.id())
                                                        .suggests(PERK_SUGGESTION)
                                                        .executes(ctx -> noArgsProvided("add", "remove"))
                                                        .then(
                                                                Commands.literal("add")
                                                                        .executes(ctx -> editCrystal(ctx, ModifyAction.UNLOCK, 1))
                                                                        .then(
                                                                                Commands.argument("level", IntegerArgumentType.integer())
                                                                                        .executes(ctx -> editCrystal(ctx, ModifyAction.UNLOCK, IntegerArgumentType.getInteger(ctx, "level")))
                                                                        )
                                                        )
                                                        .then(
                                                                Commands.literal("remove")
                                                                        .executes(ctx -> editCrystal(ctx, ModifyAction.LOCK, 0))
                                                        )
                                        )
                                        .then(
                                                Commands.argument("level", IntegerArgumentType.integer(1))
                                                        .executes(GunsrpgCommand::editCrystalLevel)
                                        )
                                        .then(
                                                Commands.literal("generate")
                                                        .executes(GunsrpgCommand::generateCrystalFromConfig)
                                                        .then(
                                                                Commands.argument("level", IntegerArgumentType.integer(1))
                                                                        .executes(GunsrpgCommand::generateCrystalFromCustomConfig)
                                                                        .then(
                                                                                Commands.argument("buffs", IntegerArgumentType.integer(0))
                                                                                        .executes(GunsrpgCommand::generateCrystalFromCustomConfig)
                                                                                        .then(
                                                                                                Commands.argument("debuffs", IntegerArgumentType.integer(0))
                                                                                                        .executes(GunsrpgCommand::generateCrystalFromCustomConfig)
                                                                                        )
                                                                        )
                                                        )
                                        )
                                        .then(
                                                Commands.literal("cooldown")
                                                        .executes(ctx -> noArgsProvided("cooldownValue"))
                                                        .then(
                                                                Commands.argument("cooldownValue", IntegerArgumentType.integer(0))
                                                                        .executes(GunsrpgCommand::setCrystalUsageCooldown)
                                                        )
                                        )
                        )
                        .then(
                                Commands.literal("quest")
                                        .executes(ctx -> noArgsProvided("reward", "cancel", "changeStatus", "group", "refresh"))
                                        .then(
                                                Commands.literal("reward")
                                                        .executes(ctx -> addRandomReward(ctx, 1, 1, true))
                                                        .then(
                                                                Commands.argument("tier", IntegerArgumentType.integer(1, 8))
                                                                        .executes(ctx -> addRandomReward(ctx, IntegerArgumentType.getInteger(ctx, "tier"), 1, true))
                                                                        .then(
                                                                                Commands.argument("itemCount", IntegerArgumentType.integer(1, 64))
                                                                                        .executes(ctx -> addRandomReward(ctx, IntegerArgumentType.getInteger(ctx, "tier"), IntegerArgumentType.getInteger(ctx, "itemCount"), true))
                                                                                        .then(
                                                                                                Commands.argument("setUnique", BoolArgumentType.bool())
                                                                                                        .executes(ctx -> addRandomReward(ctx, IntegerArgumentType.getInteger(ctx, "tier"), IntegerArgumentType.getInteger(ctx, "itemCount"), BoolArgumentType.getBool(ctx, "setUnique")))
                                                                                        )
                                                                        )
                                                                        .then(
                                                                                Commands.literal("list")
                                                                                        .executes(GunsrpgCommand::listTierRewardPool)
                                                                        )
                                                        )
                                        )
                                        .then(
                                                Commands.argument("questId", ResourceLocationArgument.id())
                                                        .suggests(QUEST_SUGGESTION)
                                                        .executes(GunsrpgCommand::listQuestInfo)
                                                        .then(
                                                                Commands.literal("start")
                                                                        .executes(GunsrpgCommand::startNewQuest)
                                                        )
                                        )
                                        .then(
                                                Commands.literal("cancel")
                                                        .executes(GunsrpgCommand::cancelCurrectQuest)
                                        )
                                        .then(
                                                Commands.literal("changeStatus")
                                                        .executes(ctx -> noArgsProvided("status"))
                                                        .then(
                                                                Commands.argument("status", EnumArgument.enumArgument(QuestStatus.class))
                                                                        .executes(GunsrpgCommand::updateQuestStatus)
                                                        )
                                        )
                                        .then(
                                                Commands.literal("refresh")
                                                        .executes(GunsrpgCommand::refreshMayorQuests)
                                        )
                                        .then(
                                                Commands.literal("group")
                                                        .executes(ctx -> noArgsProvided("invite", "leave", "accept", "reject", "cancel"))
                                                        .then(
                                                                Commands.literal("invite")
                                                                        .executes(ctx -> noArgsProvided("player"))
                                                                        .then(
                                                                                Commands.argument("player", EntityArgument.player())
                                                                                        .executes(GunsrpgCommand::invitePlayerToGroup)
                                                                        )
                                                        )
                                                        .then(
                                                                Commands.literal("leave")
                                                                        .executes(GunsrpgCommand::leaveCurrentGroup)
                                                        )
                                                        .then(
                                                                Commands.literal("accept")
                                                                        .executes(ctx -> noArgsProvided("player"))
                                                                        .then(
                                                                                Commands.argument("player", EntityArgument.player())
                                                                                        .executes(GunsrpgCommand::acceptGroupInvite)
                                                                        )
                                                        )
                                                        .then(
                                                                Commands.literal("reject")
                                                                        .executes(ctx -> noArgsProvided("player"))
                                                                        .then(
                                                                                Commands.argument("player", EntityArgument.player())
                                                                                        .executes(GunsrpgCommand::rejectGroupInvite)
                                                                        )
                                                        )
                                                        .then(
                                                                Commands.literal("cancel")
                                                                        .executes(ctx -> noArgsProvided("player"))
                                                                        .then(
                                                                                Commands.argument("player", EntityArgument.player())
                                                                                        .executes(GunsrpgCommand::cancelGroupInvite)
                                                                        )
                                                        )
                                        )
                        )
                        .then(
                                Commands.literal("weapon")
                                        .executes(ctx -> noArgsProvided("force_jam"))
                                        .then(
                                                Commands.literal("force_jam")
                                                        .executes(GunsrpgCommand::jamWeapon)
                                        )
                        )
                        .executes(ctx -> noArgsProvided("debuff", "event", "progression", "skill"))
        );
    }

    private static int leaveCurrentGroup(CommandContext<CommandSource> context) {
        Entity executor = context.getSource().getEntity();
        if (!(executor instanceof PlayerEntity)) {
            return -1;
        }
        PlayerEntity player = (PlayerEntity) executor;
        World level = player.level;
        IQuestingData questing = QuestingDataProvider.getData(level).orElse(null);
        QuestingGroup group = questing.getOrCreateGroup(player);
        if (group.getMemberCount() == 1)
            return 0;
        questing.removeFromGroup(player.getUUID());
        questing.sendData();
        return 0;
    }

    private static int invitePlayerToGroup(CommandContext<CommandSource> context) throws CommandSyntaxException {
        Entity executor = context.getSource().getEntity();
        if (!(executor instanceof PlayerEntity)) {
            return -1;
        }
        PlayerEntity player = (PlayerEntity) executor;
        World level = player.level;
        IQuestingData questing = QuestingDataProvider.getData(level).orElse(null);
        QuestingGroup group = questing.getOrCreateGroup(player);
        if (!group.isLeader(player.getUUID())) {
            throw NOT_A_GROUP_LEADER.create();
        }
        ServerPlayerEntity invitee = EntityArgument.getPlayer(context, "player");
        Interaction<GroupInvite> interaction = group.invite(invitee);
        interaction.validate(GROUP_INVITE_FAILED::create);
        questing.sendData();
        return 0;
    }

    private static int acceptGroupInvite(CommandContext<CommandSource> context) throws CommandSyntaxException {
        Entity executor = context.getSource().getEntity();
        if (!(executor instanceof PlayerEntity)) {
            return -1;
        }
        PlayerEntity player = (PlayerEntity) executor;
        World level = player.level;
        IQuestingData questing = QuestingDataProvider.getData(level).orElse(null);
        PlayerEntity leader = EntityArgument.getPlayer(context, "player");
        QuestingGroup targetGroup = questing.getOrCreateGroup(leader);
        GroupInvite invite = targetGroup.getInvite(player.getUUID());
        if (invite == null) {
            throw INVITE_NOT_FOUND.create();
        }
        Interaction<?> interaction = targetGroup.onInviteAccepted(invite, player);
        interaction.validate(INVITE_ACCEPT_FAILED::create);
        questing.sendData();
        return 0;
    }

    private static int rejectGroupInvite(CommandContext<CommandSource> context) throws CommandSyntaxException {
        Entity executor = context.getSource().getEntity();
        if (!(executor instanceof PlayerEntity)) {
            return -1;
        }
        PlayerEntity player = (PlayerEntity) executor;
        World level = player.level;
        IQuestingData questing = QuestingDataProvider.getData(level).orElse(null);
        PlayerEntity leader = EntityArgument.getPlayer(context, "player");
        QuestingGroup targetGroup = questing.getOrCreateGroup(leader);
        GroupInvite invite = targetGroup.getInvite(player.getUUID());
        if (invite == null) {
            throw INVITE_NOT_FOUND.create();
        }
        Interaction<?> interaction = targetGroup.onInviteRejected(invite);
        interaction.validate(INVITE_REJECT_FAILED::create);
        questing.sendData();
        return 0;
    }

    private static int cancelGroupInvite(CommandContext<CommandSource> context) throws CommandSyntaxException {
        Entity executor = context.getSource().getEntity();
        if (!(executor instanceof PlayerEntity)) {
            return -1;
        }
        PlayerEntity player = (PlayerEntity) executor;
        World level = player.level;
        IQuestingData questing = QuestingDataProvider.getData(level).orElse(null);
        QuestingGroup currentGroup = questing.getOrCreateGroup(player);
        if (!currentGroup.isLeader(player.getUUID())) {
            throw NOT_A_GROUP_LEADER.create();
        }
        PlayerEntity invitee = EntityArgument.getPlayer(context, "player");
        GroupInvite invite = currentGroup.getInvite(invitee.getUUID());
        if (invite == null) {
            throw INVITE_NOT_FOUND.create();
        }
        Interaction<?> interaction = currentGroup.onInviteCancelled(invite);
        interaction.validate(INVITE_CANCEL_FAILED::create);
        questing.sendData();
        return 0;
    }

    private static int jamWeapon(CommandContext<CommandSource> context) throws CommandSyntaxException {
        Entity executor = context.getSource().getEntity();
        if (!(executor instanceof PlayerEntity)) {
            return -1;
        }
        PlayerEntity player = (PlayerEntity) executor;
        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof AbstractGun)) {
            throw NOT_HOLDING_GUN.create();
        }
        AbstractGun gun = (AbstractGun) stack.getItem();
        gun.setJammedState(stack, true);
        return 0;
    }

    private static int setCrystalUsageCooldown(CommandContext<CommandSource> context) {
        int value = IntegerArgumentType.getInteger(context, "cooldownValue");
        Entity executor = context.getSource().getEntity();
        if (!(executor instanceof PlayerEntity)) {
            return -1;
        }
        PlayerEntity player = (PlayerEntity) executor;
        IPlayerData data = PlayerData.getUnsafe(player);
        IPerkProvider provider = data.getPerkProvider();
        provider.setCooldown(value);
        return 0;
    }

    private static int startNewQuest(CommandContext<CommandSource> context) throws CommandSyntaxException {
        Entity executor = context.getSource().getEntity();
        if (!(executor instanceof PlayerEntity)) {
            return -1;
        }
        PlayerEntity player = (PlayerEntity) executor;
        IQuestingData questing = QuestingDataProvider.getData(player.level).orElse(null);
        ResourceLocation location = ResourceLocationArgument.getId(context, "questId");
        QuestSystem system = GunsRPG.getModLifecycle().quests();
        QuestManager manager = system.getQuestManager();
        QuestScheme<?> scheme = manager.getScheme(location);
        if (scheme == null) {
            throw UNKNOWN_KEY_EXCEPTION.create(location);
        }
        QuestingGroup group = questing.getOrCreateGroup(player);
        if (!group.isLeader(player.getUUID())) {
            throw NOT_A_GROUP_LEADER.create();
        }
        Quest<?> quest = newQuest(scheme, player.level);
        quest.setStatus(QuestStatus.ACTIVE);
        questing.assignQuest(quest, group);
        questing.sendData();
        return 0;
    }

    private static int refreshMayorQuests(CommandContext<CommandSource> context) {
        Entity executor = context.getSource().getEntity();
        if (!(executor instanceof PlayerEntity)) {
            return -1;
        }
        PlayerEntity player = (PlayerEntity) executor;
        World level = player.level;
        List<MayorEntity> mayorEntities = level.getEntities(ModEntities.MAYOR.get(), player.getBoundingBox().inflate(16), LivingEntity::isAlive);
        mayorEntities.forEach(mayor -> mayor.setRefreshTimer(0L));
        player.sendMessage(new StringTextComponent("Quests have been refreshed by " + mayorEntities.size() + " mayors"), Util.NIL_UUID);
        return 0;
    }

    @SuppressWarnings("unchecked")
    private static <D extends IQuestData, Q extends Quest<D>> Quest<D> newQuest(QuestScheme<D> scheme, World world) {
        QuestType<D, Q> type = (QuestType<D, Q>) scheme.getQuestType();
        return type.newQuestInstance(world, scheme, Util.NIL_UUID);
    }

    private static int updateQuestStatus(CommandContext<CommandSource> context) throws CommandSyntaxException {
        Entity executor = context.getSource().getEntity();
        if (!(executor instanceof PlayerEntity)) {
            return -1;
        }
        PlayerEntity player = (PlayerEntity) executor;
        IQuestingData questing = QuestingDataProvider.getData(player.level).orElse(null);
        Quest<?> quest = questing.getActiveQuestForPlayer(player);
        if (quest == null) {
            throw NO_ACTIVE_QUEST.create();
        }
        QuestStatus status = context.getArgument("status", QuestStatus.class);
        quest.setStatus(status);
        questing.sendData();
        return 0;
    }

    private static int cancelCurrectQuest(CommandContext<CommandSource> context) throws CommandSyntaxException {
        Entity executor = context.getSource().getEntity();
        if (!(executor instanceof PlayerEntity)) {
            return -1;
        }
        PlayerEntity player = (PlayerEntity) executor;
        IQuestingData questing = QuestingDataProvider.getData(player.level).orElse(null);
        Quest<?> quest = questing.getActiveQuestForPlayer(player);
        if (quest == null) {
            throw NO_ACTIVE_QUEST.create();
        }
        QuestingGroup group = questing.getOrCreateGroup(player);
        questing.unassignQuest(group);
        questing.sendData();
        return 0;
    }

    private static int listQuestInfo(CommandContext<CommandSource> context) throws CommandSyntaxException {
        Entity executor = context.getSource().getEntity();
        if (!(executor instanceof PlayerEntity)) {
            return -1;
        }
        PlayerEntity player = (PlayerEntity) executor;
        ResourceLocation questId = ResourceLocationArgument.getId(context, "questId");
        QuestSystem system = GunsRPG.getModLifecycle().quests();
        QuestScheme<?> scheme = system.getQuestManager().getScheme(questId);
        if (scheme == null) {
            throw UNKNOWN_KEY_EXCEPTION.create(questId);
        }
        QuestType<?, ?> questType = scheme.getQuestType();
        IQuestData data = scheme.getData();
        DisplayInfo info = scheme.getDisplayInfo();
        QuestConditionTierScheme tierScheme = scheme.getConditionTierScheme();
        player.sendMessage(new StringTextComponent(TextFormatting.YELLOW.toString() + TextFormatting.BOLD + "Quest: " + TextFormatting.RESET + TextFormatting.YELLOW + scheme.getQuestId()), Util.NIL_UUID);
        player.sendMessage(new StringTextComponent(TextFormatting.GREEN + "Type of quest: " + TextFormatting.AQUA + questType.getId()), Util.NIL_UUID);
        player.sendMessage(new StringTextComponent(TextFormatting.GREEN + "Tier: " + TextFormatting.AQUA + scheme.getTier()), Util.NIL_UUID);
        player.sendMessage(new StringTextComponent(TextFormatting.GREEN + "Data: " + TextFormatting.AQUA + data.toString()), Util.NIL_UUID);
        player.sendMessage(new StringTextComponent(TextFormatting.YELLOW.toString() + TextFormatting.BOLD + "Display info"), Util.NIL_UUID);
        player.sendMessage(new StringTextComponent(TextFormatting.GREEN + "Name: " + TextFormatting.AQUA + info.getName().getString()), Util.NIL_UUID);
        player.sendMessage(new StringTextComponent(TextFormatting.GREEN + "Info: " + TextFormatting.AQUA + info.getInfo().getString()), Util.NIL_UUID);
        IQuestConditionProvider<?>[] questConditions = scheme.getQuestConditions();
        if (questConditions.length > 0) {
            player.sendMessage(new StringTextComponent(TextFormatting.YELLOW.toString() + TextFormatting.BOLD + "Conditions"), Util.NIL_UUID);
            for (IQuestConditionProvider<?> provider : questConditions) {
                IQuestCondition condition = provider.makeConditionInstance();
                ITextComponent text = condition.getDescriptor(false);
                player.sendMessage(new StringTextComponent(TextFormatting.GREEN + "- " + TextFormatting.AQUA + text.getString()), Util.NIL_UUID);
            }
        }
        QuestConditionTierScheme.TieredList[] tieredLists = tierScheme.getListProviders();
        if (tieredLists.length > 0) {
            player.sendMessage(new StringTextComponent(TextFormatting.YELLOW.toString() + TextFormatting.BOLD + "Tiered conditions"), Util.NIL_UUID);
            for (QuestConditionTierScheme.TieredList list : tieredLists) {
                WeightedConditionList rawList = list.getListRaw();
                IQuestConditionProvider<?>[] providers = rawList.getProviders();
                int tierModifier = list.getTier();
                player.sendMessage(new StringTextComponent(TextFormatting.GREEN + "Added tier: " + TextFormatting.AQUA + tierModifier), Util.NIL_UUID);
                for (IQuestConditionProvider<?> provider : providers) {
                    IQuestCondition condition = provider.makeConditionInstance();
                    ITextComponent textComponent = condition.getDescriptor(false);
                    player.sendMessage(new StringTextComponent(TextFormatting.GREEN + "- " + TextFormatting.AQUA + textComponent.getString()), Util.NIL_UUID);
                }
                player.sendMessage(new StringTextComponent(TextFormatting.GREEN + "-------------------------"), Util.NIL_UUID);
            }
        }
        return 0;
    }

    private static int listTierRewardPool(CommandContext<CommandSource> context) {
        Entity executor = context.getSource().getEntity();
        if (!(executor instanceof PlayerEntity)) {
            return -1;
        }
        PlayerEntity player = (PlayerEntity) executor;
        int tier = CommandHelper.getArgumentOptional(context, "tier", IntegerArgumentType::getInteger).orElse(1);
        QuestSystem system = GunsRPG.getModLifecycle().quests();
        QuestRewardManager manager = system.getRewardManager();
        QuestRewardList list = manager.getTieredRewards(tier);
        IQuestItemProvider[] providers = list.listProviders();
        player.sendMessage(new StringTextComponent(TextFormatting.YELLOW.toString() + TextFormatting.BOLD + "=========[ Tier " + tier + " items ]=========="), Util.NIL_UUID);
        player.sendMessage(new StringTextComponent(TextFormatting.GREEN + "Entries: " + TextFormatting.AQUA + providers.length), Util.NIL_UUID);
        player.sendMessage(new StringTextComponent(TextFormatting.GREEN + "Items:"), Util.NIL_UUID);
        for (IQuestItemProvider provider : providers) {
            IQuestItemProvider.Impl implementation = (IQuestItemProvider.Impl) provider;
            player.sendMessage(new StringTextComponent(String.format(
                    "%sItem: %s%s%s, Count: %s%d%s, Weight: %s%d",
                    TextFormatting.GREEN,
                    TextFormatting.AQUA,
                    implementation.getItem().getRegistryName().getPath(),
                    TextFormatting.GREEN,
                    TextFormatting.AQUA,
                    implementation.getCount(),
                    TextFormatting.GREEN,
                    TextFormatting.AQUA,
                    implementation.getWeight()
            )), Util.NIL_UUID);
        }
        return 0;
    }

    private static int addRandomReward(CommandContext<CommandSource> context, int tier, int itemCount, boolean unique) {
        Entity executor = context.getSource().getEntity();
        if (!(executor instanceof PlayerEntity)) {
            return -1;
        }
        PlayerEntity player = (PlayerEntity) executor;
        QuestSystem questSystem = GunsRPG.getModLifecycle().quests();
        QuestRewardManager manager = questSystem.getRewardManager();
        QuestRewardList list = manager.getTieredRewards(tier);
        QuestReward.Options options = new QuestReward.Options().items(itemCount);
        if (unique) {
            options = options.setUnique();
        }
        QuestReward reward = QuestReward.generate(list, options, player);
        for (QuestReward.Choice choice : reward.getChoices()) {
            choice.distributeToInventory(player);
        }
        return 0;
    }

    private static int generateCrystalFromConfig(CommandContext<CommandSource> context) throws CommandSyntaxException {
        Entity executor = context.getSource().getEntity();
        if (!(executor instanceof PlayerEntity)) {
            return -1;
        }
        PlayerEntity player = (PlayerEntity) executor;
        ItemStack crystalStack = player.getMainHandItem();
        if (!(crystalStack.getItem() instanceof CrystalItem)) {
            throw NOT_HOLDING_CRYSTAL.create();
        }
        Crystal crystal = Crystal.generate();
        CrystalItem.addCrystal(crystalStack, crystal);
        return 0;
    }

    private static int generateCrystalFromCustomConfig(CommandContext<CommandSource> context) throws CommandSyntaxException {
        Entity executor = context.getSource().getEntity();
        if (!(executor instanceof PlayerEntity)) {
            return -1;
        }
        PlayerEntity player = (PlayerEntity) executor;
        ItemStack crystalStack = player.getMainHandItem();
        if (!(crystalStack.getItem() instanceof CrystalItem)) {
            throw NOT_HOLDING_CRYSTAL.create();
        }
        int level = IntegerArgumentType.getInteger(context, "level");
        int buffs = CommandHelper.getArgumentOptional(context, "buffs", IntegerArgumentType::getInteger).orElse(1);
        int debuffs = CommandHelper.getArgumentOptional(context, "debuffs", IntegerArgumentType::getInteger).orElse(1);
        Crystal crystal = Crystal.generate(level, buffs, debuffs);
        CrystalItem.addCrystal(crystalStack, crystal);
        return 0;
    }

    private static int editCrystalLevel(CommandContext<CommandSource> context) throws CommandSyntaxException {
        Entity executor = context.getSource().getEntity();
        if (!(executor instanceof PlayerEntity)) {
            return -1;
        }
        PlayerEntity player = (PlayerEntity) executor;
        ItemStack crystalStack = player.getMainHandItem();
        if (!(crystalStack.getItem() instanceof CrystalItem)) {
            throw NOT_HOLDING_CRYSTAL.create();
        }
        int level = IntegerArgumentType.getInteger(context, "level");
        CompoundNBT stackNbt = crystalStack.getTag();
        if (stackNbt == null) {
            Crystal crystal = new Crystal(level, Collections.emptyList());
            CompoundNBT data = crystal.toNbt();
            CompoundNBT stackData = new CompoundNBT();
            stackData.put("crystal", data);
            crystalStack.setTag(stackData);
        } else {
            Crystal crystal = Crystal.fromNbt(stackNbt.getCompound("crystal"));
            Crystal newCrystal = new Crystal(level, crystal.listAttributes());
            stackNbt.put("crystal", newCrystal.toNbt());
        }
        return 0;
    }

    private static int editCrystal(CommandContext<CommandSource> context, ModifyAction action, int level) throws CommandSyntaxException {
        ResourceLocation location = ResourceLocationArgument.getId(context, "perk");
        Perk perk = PerkRegistry.getRegistry().getPerkById(location);
        if (perk == null) {
            throw UNKNOWN_KEY_EXCEPTION.create(location);
        }
        Entity executor = context.getSource().getEntity();
        if (!(executor instanceof PlayerEntity)) {
            return -1;
        }
        PlayerEntity player = (PlayerEntity) executor;
        ItemStack crystal = player.getMainHandItem();
        if (!(crystal.getItem() instanceof CrystalItem)) {
            throw NOT_HOLDING_CRYSTAL.create();
        }
        switch (action) {
            case UNLOCK:
                return addCrystalAttribute(perk, level, crystal);
            case LOCK:
                return removeCrystalAttribute(perk, crystal);
        }
        return -1;
    }

    private static int addCrystalAttribute(Perk perk, int level, ItemStack stack) {
        CompoundNBT stackNbt = stack.getTag();
        if (stackNbt == null) {
            stackNbt = new CompoundNBT();
        }
        Crystal crystal = Crystal.fromNbt(stackNbt.getCompound("crystal"));
        PerkType type = level < 0 ? PerkType.DEBUFF : PerkType.BUFF;
        CrystalAttribute attribute = new CrystalAttribute(perk, type, Math.max(1, Math.abs(level)));
        Crystal modified = crystal.append(attribute);
        stackNbt.put("crystal", modified.toNbt());
        stack.setTag(stackNbt);
        return 0;
    }

    private static int removeCrystalAttribute(Perk perk, ItemStack stack) {
        CompoundNBT stackNbt = stack.getTag();
        if (stackNbt == null) {
            return 0;
        }
        Crystal crystal = Crystal.fromNbt(stackNbt.getCompound("crystal"));
        CrystalAttribute attribute = new CrystalAttribute(perk, PerkType.BUFF, 1);
        Crystal modified = crystal.remove(attribute);
        stackNbt.put("crystal", modified.toNbt());
        return 0;
    }

    private static int listAttributeInfo(CommandContext<CommandSource> context) throws CommandSyntaxException {
        CommandSource source = context.getSource();
        Entity executor = source.getEntity();
        if (!(executor instanceof PlayerEntity)) {
            return -1;
        }
        PlayerEntity player = (PlayerEntity) executor;
        IPlayerData data = PlayerData.get(player).orElseThrow(NO_DATA::create);
        IAttributeProvider provider = data.getAttributes();
        ResourceLocation id = ResourceLocationArgument.getId(context, "id");
        IAttributeId attributeId = Attribs.find(id);
        if (attributeId == null) {
            throw UNKNOWN_KEY_EXCEPTION.create(id);
        }
        IAttribute attribute = provider.getAttribute(attributeId);
        player.sendMessage(new StringTextComponent(TextFormatting.YELLOW.toString() + TextFormatting.BOLD + "=========[ Attribute Info ]=========="), Util.NIL_UUID);
        player.sendMessage(new StringTextComponent(TextFormatting.GREEN + "Attribute: " + TextFormatting.AQUA + id), Util.NIL_UUID);
        player.sendMessage(new StringTextComponent(TextFormatting.GREEN + "Base value: " + TextFormatting.AQUA + attribute.getBaseValue()), Util.NIL_UUID);
        player.sendMessage(new StringTextComponent(TextFormatting.GREEN + "Value: " + TextFormatting.AQUA + attribute.value()), Util.NIL_UUID);
        Collection<IAttributeModifier> modifiers = attribute.listModifiers();
        int modifierCount = modifiers.size();
        player.sendMessage(new StringTextComponent(TextFormatting.GREEN + "Modifier count: " + TextFormatting.AQUA + modifierCount), Util.NIL_UUID);
        if (modifierCount > 0) {
            player.sendMessage(new StringTextComponent(TextFormatting.GREEN + "Modifiers:"), Util.NIL_UUID);
            for (IAttributeModifier modifier : modifiers) {
                player.sendMessage(new StringTextComponent(TextFormatting.GREEN + "-" + TextFormatting.AQUA + modifier.toString()), Util.NIL_UUID);
            }
        }
        return 0;
    }

    private static int progressAdd(CommandContext<CommandSource> ctx, int amount, JsonElement data) throws CommandSyntaxException {
        return modifyProgression(ctx, amount, data, (killData, count) -> {
            for (int i = 0; i < count; i++) {
                killData.advanceLevel(false);
            }
        });
    }

    private static int toggleDebuff(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        ResourceLocation registryKey = ResourceLocationArgument.getId(ctx, "registryKey");
        if (registryKey == null) {
            throw MISSING_KEY_EXCEPTION.create();
        }
        IDebuffType<?> type = ModRegistries.DEBUFFS.getValue(registryKey);
        if (type == null || !type.isToggleable()) throw UNKNOWN_KEY_EXCEPTION.create(registryKey);
        PlayerEntity player = getPlayer(ctx);
        LazyOptional<IPlayerData> optional = PlayerData.get(player);
        optional.ifPresent(data -> {
            IDebuffs debuffs = data.getDebuffControl();
            debuffs.toggle(type);
            ctx.getSource().sendSuccess(new TranslationTextComponent("command.gunsrpg.toggle_debuff", type.getRegistryName().toString()), false);
        });
        return 0;
    }

    private static int callAirdrop(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        CommandSource source = ctx.getSource();
        ServerWorld world = source.getLevel();
        Entity entity = source.getEntity();
        if (entity == null) {
            return -1;
        }
        BlockPos pos = new BlockPos(entity.getX(), entity.getY() + 10, entity.getZ());
        if (!world.isEmptyBlock(pos)) {
            throw LOCATION_OBSTRUCTED.create();
        }
        AirdropEntity airdrop = new AirdropEntity(world);
        airdrop.setPos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        world.addFreshEntity(airdrop);
        source.sendSuccess(new TranslationTextComponent("command.gunsrpg.airdrop"), false);
        return 0;
    }

    private static int forceBloodmoon(CommandContext<CommandSource> ctx) {
        CommandSource src = ctx.getSource();
        MinecraftServer server = src.getServer();
        IServerWorldInfo worldInfo = server.getWorldData().overworldData();
        long newGameTime = GunsRPG.config.world.bloodmoonCycle * 24000L + 13000L;
        worldInfo.setGameTime(newGameTime);
        src.getLevel().setDayTime(newGameTime);
        src.sendSuccess(new TranslationTextComponent("command.gunsrpg.set_bloodmoon"), false);
        return 0;
    }

    private static int forceProgression(CommandContext<CommandSource> ctx, ModifyAction action) {
        CommandSource src = ctx.getSource();
        PlayerEntity player = getPlayer(ctx);
        LazyOptional<IPlayerData> optional = PlayerData.get(player);
        optional.ifPresent(data -> {
            data.getSaveEntries().stream()
                    .filter(entry -> entry instanceof ILockStateChangeable)
                    .map(entry -> (ILockStateChangeable) entry)
                    .forEach(action::apply);
            String translationKey = "command.gunsrpg.progression." + (action == ModifyAction.LOCK ? "reset" : "unlock");
            src.sendSuccess(new TranslationTextComponent(translationKey), false);
            data.sync(DataFlags.WILDCARD);
        });
        return 0;
    }

    private static int addLevel(CommandContext<CommandSource> context) throws CommandSyntaxException {
        return modifyProgression(context, 1, JsonNull.INSTANCE, IKillData::addLevels);
    }

    private static int addLevels(CommandContext<CommandSource> context) throws CommandSyntaxException {
        int amount = IntegerArgumentType.getInteger(context, "amount");
        return modifyProgression(context, amount, JsonNull.INSTANCE, IKillData::addLevels);
    }

    private static int addLevelsSpecific(CommandContext<CommandSource> context) throws CommandSyntaxException {
        int amount = IntegerArgumentType.getInteger(context, "amount");
        JsonElement data = JsonArgumentType.getJson(context, "txData");
        return modifyProgression(context, amount, data, IKillData::addLevels);
    }

    private static int addPoint(CommandContext<CommandSource> context) throws CommandSyntaxException {
        return modifyProgression(context, 1, JsonNull.INSTANCE, IPointProvider::awardPoints);
    }

    private static int addPoints(CommandContext<CommandSource> context) throws CommandSyntaxException {
        int amount = IntegerArgumentType.getInteger(context, "amount");
        return modifyProgression(context, amount, JsonNull.INSTANCE, IPointProvider::awardPoints);
    }

    private static int addPointsSpecific(CommandContext<CommandSource> context) throws CommandSyntaxException {
        int amount = IntegerArgumentType.getInteger(context, "amount");
        JsonElement data = JsonArgumentType.getJson(context, "txData");
        return modifyProgression(context, amount, data, IPointProvider::awardPoints);
    }

    private static int modifyProgression(CommandContext<CommandSource> context, int amount, JsonElement jsonData, BiConsumer<IKillData, Integer> action) throws CommandSyntaxException {
        CommandSource source = context.getSource();
        Entity executor = source.getEntity();
        if (executor instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) executor;
            LazyOptional<IPlayerData> optional = PlayerData.get(player);
            ITransactionValidator validator = getTxValidator(context, jsonData);
            optional.ifPresent(data -> {
                IKillData killData = validator.getData(data);
                action.accept(killData, amount);
                source.sendSuccess(new TranslationTextComponent("command.gunsrpg.progression.modify"), false);
                data.sync(DataFlags.WILDCARD);
            });
            return 0;
        }
        return -1;
    }

    private static int unlockSkill(CommandContext<CommandSource> context) throws CommandSyntaxException {
        return modifySkill(context, ModifyAction.UNLOCK);
    }

    private static int lockSkill(CommandContext<CommandSource> context) throws CommandSyntaxException {
        return modifySkill(context, ModifyAction.LOCK);
    }

    private static int modifySkill(CommandContext<CommandSource> context, ModifyAction action) throws CommandSyntaxException {
        ResourceLocation id = ResourceLocationArgument.getId(context, "skillId");
        if (id == null) {
            throw MISSING_KEY_EXCEPTION.create();
        }
        SkillType<?> type = ModRegistries.SKILLS.getValue(id);
        if (type == null) {
            throw UNKNOWN_KEY_EXCEPTION.create(id);
        }
        boolean isUnlockOp = action == ModifyAction.UNLOCK;
        Entity executor = context.getSource().getEntity();
        if (executor instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) executor;
            IPlayerData data = PlayerData.getUnsafe(player);
            ISkillProvider provider = data.getSkillProvider();
            ISkillHierarchy<?> hierarchy = type.getHierarchy();
            if (isUnlockOp) {
                SkillType<?> parent = hierarchy.getParent();
                if (parent != null && !provider.hasSkill(parent)) {
                    throw REQUIRED_SKILL.create(parent.getRegistryName());
                }
                provider.unlock(type);
            } else {
                SkillType<?>[] children = hierarchy.getChildren();
                if (children != null && children.length > 0) {
                    for (SkillType<?> child : children) {
                        if (provider.hasSkill(child)) {
                            throw DEPENDENT_SKILL_ACTIVE.create(child);
                        }
                    }
                }
                provider.lock(type);
            }
            context.getSource().sendSuccess(new TranslationTextComponent("command.gunsrpg.skill." + (action == ModifyAction.UNLOCK ? "unlock" : "lock"), id), false);
            return 0;
        }
        return -1;
    }

    private static ITransactionValidator getTxValidator(CommandContext<CommandSource> context, JsonElement data) throws CommandSyntaxException {
        ResourceLocation id = ResourceLocationArgument.getId(context, "txType");
        ITransactionValidatorFactory<?, ?> factory = TransactionValidatorRegistry.getValidatorFactory(id);
        if (factory == null) {
            throw UNKNOWN_KEY_EXCEPTION.create(id);
        }
        ITransactionValidator validator;
        try {
            validator = TransactionValidatorRegistry.getTransactionValidator(factory, data);
        } catch (JsonParseException exception) {
            throw INVALID_TX_DATA.create();
        }
        if (validator == null) {
            throw INVALID_TX_DATA.create();
        }
        return validator;
    }

    private static int noArgsProvided(String... args) throws CommandSyntaxException {
        throw MISSING_ARGUMENTS.create(args.length > 0 ? String.join(",", args) : "");
    }

    private static PlayerEntity getPlayer(CommandContext<CommandSource> ctx) {
        return (PlayerEntity) ctx.getSource().getEntity();
    }

    private enum ModifyAction {
        LOCK(ILockStateChangeable::doLock),
        UNLOCK(ILockStateChangeable::doUnlock);

        final Consumer<ILockStateChangeable> action;

        ModifyAction(Consumer<ILockStateChangeable> action) {
            this.action = action;
        }

        void apply(ILockStateChangeable skills) {
            action.accept(skills);
        }
    }
}
