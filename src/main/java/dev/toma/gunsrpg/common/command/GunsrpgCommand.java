package dev.toma.gunsrpg.common.command;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import dev.toma.gunsrpg.api.common.attribute.IAttribute;
import dev.toma.gunsrpg.api.common.attribute.IAttributeId;
import dev.toma.gunsrpg.api.common.attribute.IAttributeModifier;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.data.*;
import dev.toma.gunsrpg.api.common.skill.ISkillHierarchy;
import dev.toma.gunsrpg.api.common.skill.ITransactionValidator;
import dev.toma.gunsrpg.api.common.skill.ITransactionValidatorFactory;
import dev.toma.gunsrpg.common.attribute.*;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.debuffs.IDebuffType;
import dev.toma.gunsrpg.common.entity.AirdropEntity;
import dev.toma.gunsrpg.common.init.ModRegistries;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.common.skills.core.TransactionValidatorRegistry;
import dev.toma.gunsrpg.config.ModConfig;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.ResourceLocationArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.IServerWorldInfo;
import net.minecraftforge.common.util.LazyOptional;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class GunsrpgCommand {

    private static final SuggestionProvider<CommandSource> DEBUFF_SUGGESTION = (context, builder) -> ISuggestionProvider.suggestResource(ModRegistries.DEBUFFS.getKeys(), builder);
    private static final SuggestionProvider<CommandSource> TX_VALIDATOR_SUGGESTION = (context, builder) -> ISuggestionProvider.suggestResource(TransactionValidatorRegistry.getRegisteredValidatorTypes(), builder);
    private static final SuggestionProvider<CommandSource> SKILL_SUGGESTION = (context, builder) -> ISuggestionProvider.suggestResource(ModRegistries.SKILLS.getKeys(), builder);
    private static final SuggestionProvider<CommandSource> ATTRIBUTE_SUGGESTION = (context, builder) -> ISuggestionProvider.suggestResource(Attribs.listKeys(), builder);
    private static final SimpleCommandExceptionType NO_DATA = new SimpleCommandExceptionType(new TranslationTextComponent("command.gunsrpg.exception.no_data"));
    private static final SimpleCommandExceptionType MISSING_KEY_EXCEPTION = new SimpleCommandExceptionType(new TranslationTextComponent("command.gunsrpg.exception.missing_key"));
    private static final DynamicCommandExceptionType MISSING_ARGUMENTS = new DynamicCommandExceptionType(o -> new TranslationTextComponent("command.gunsrpg.exception.missing_args", o));
    private static final SimpleCommandExceptionType LOCATION_OBSTRUCTED = new SimpleCommandExceptionType(new TranslationTextComponent("command.gunsrpg.exception.location_obstructed"));
    private static final DynamicCommandExceptionType UNKNOWN_KEY_EXCEPTION = new DynamicCommandExceptionType(data -> new TranslationTextComponent("command.gunsrpg.exception.unknown_key", data));
    private static final SimpleCommandExceptionType INVALID_TX_DATA = new SimpleCommandExceptionType(new TranslationTextComponent("command.gunsrpg.exception.invalid_tx_data"));
    private static final DynamicCommandExceptionType REQUIRED_SKILL = new DynamicCommandExceptionType(id -> new TranslationTextComponent("command.gunsrpg.exception.missing_skill", id));
    private static final DynamicCommandExceptionType DEPENDENT_SKILL_ACTIVE = new DynamicCommandExceptionType(id -> new TranslationTextComponent("command.gunsrpg.exception.active_skill", id));

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
                                                        .executes(ctx -> noArgsProvided(ctx, "level", "point", "progress"))
                                        )
                                        .executes(ctx -> noArgsProvided(ctx, "reset", "unlockAll"))
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
                                                        .executes(ctx -> noArgsProvided(ctx, "unlock", "lock"))
                                        )
                                        .executes(ctx -> {
                                            throw MISSING_KEY_EXCEPTION.create();
                                        })
                        )
                        .then(
                                Commands.literal("attribute")
                                        .executes(ctx -> noArgsProvided(ctx, "id"))
                                        .then(
                                                Commands.argument("id", ResourceLocationArgument.id())
                                                        .suggests(ATTRIBUTE_SUGGESTION)
                                                        .executes(GunsrpgCommand::listAttributeInfo)
                                        )
                        )
                        .executes(ctx -> noArgsProvided(ctx, "debuff", "event", "progression", "skill"))
        );
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
        if (type == null) throw UNKNOWN_KEY_EXCEPTION.create(registryKey);
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
        long newGameTime = ModConfig.worldConfig.bloodmoonCycle.get() * 24000L + 13000L;
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
            ISkillProvider provider = data.getSkillProvider();
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
                if (children.length > 0) {
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

    private static int noArgsProvided(CommandContext<CommandSource> ctx, String... args) throws CommandSyntaxException {
        TranslationTextComponent argList = new TranslationTextComponent("command.gunsrpg.exception.arg_list", args.length > 0 ? String.join(",", args) : "");
        throw MISSING_ARGUMENTS.create(argList.getString());
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
