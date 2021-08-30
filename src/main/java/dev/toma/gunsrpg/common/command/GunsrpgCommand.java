package dev.toma.gunsrpg.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.object.DebuffData;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.common.debuffs.DebuffType;
import dev.toma.gunsrpg.common.init.ModRegistries;
import dev.toma.gunsrpg.config.ModConfig;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.ResourceLocationArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.storage.IServerWorldInfo;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class GunsrpgCommand {

    private static final SuggestionProvider<CommandSource> DEBUFF_SUGGESTION = (context, builder) -> ISuggestionProvider.suggestResource(ModRegistries.DEBUFFS.getKeys(), builder);
    private static final SimpleCommandExceptionType MISSING_KEY_EXCEPTION = new SimpleCommandExceptionType(new LiteralMessage("Undefined key!"));
    private static final SimpleCommandExceptionType NO_ARGUMENTS_EXCEPTION = new SimpleCommandExceptionType(new LiteralMessage("Not enough arguments!"));
    private static final DynamicCommandExceptionType UNKNOWN_KEY_EXCEPTION = new DynamicCommandExceptionType(o -> new LiteralMessage("Unknown key " + o.toString() + "!"));

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                Commands.literal("gunsrpg")
                        .requires(src -> src.hasPermission(2) && src.getEntity() instanceof PlayerEntity)
                        .then(
                                Commands.literal("debuff")
                                        .then(
                                                Commands.argument("registryKey", ResourceLocationArgument.id())
                                                        .suggests(DEBUFF_SUGGESTION)
                                                        .executes(context -> toggleDebuff(context, ResourceLocationArgument.getId(context, "registryKey")))
                                        )
                        )
                        .then(
                                Commands.literal("bloodmoon")
                                        .executes(GunsrpgCommand::forceBloodmoon)
                        )
                        .then(
                                Commands.literal("skilltree")
                                        .then(
                                                Commands.literal("lock")
                                                        .executes(ctx -> modifySkillTree(ctx, ModifyAction.LOCK))
                                        )
                                        .then(
                                                Commands.literal("unlock")
                                                        .executes(ctx -> modifySkillTree(ctx, ModifyAction.UNLOCK))
                                        )
                                        .executes(ctx -> modifySkillTree(ctx, null))
                        )
                        .then(
                                Commands.literal("levelup")
                                        .then(
                                                Commands.argument("amount", IntegerArgumentType.integer(1, 100))
                                                        .executes(ctx -> addLevels(ctx, IntegerArgumentType.getInteger(ctx, "amount")))
                                        )
                                        .executes(ctx -> addLevels(ctx, 1))
                        )
                        .executes(GunsrpgCommand::noArgsProvided)
        );
    }

    private static int toggleDebuff(CommandContext<CommandSource> ctx, ResourceLocation registryKey) throws CommandSyntaxException {
        if (registryKey == null) {
            throw MISSING_KEY_EXCEPTION.create();
        }
        DebuffType<?> type = ModRegistries.DEBUFFS.getValue(registryKey);
        if (type == null) throw UNKNOWN_KEY_EXCEPTION.create(registryKey);
        PlayerEntity player = getPlayer(ctx);
        LazyOptional<IPlayerData> optional = PlayerData.get(player);
        optional.ifPresent(data -> {
            DebuffData debuffs = data.getDebuffData();
            debuffs.toggle(type);
            data.sync();
            ctx.getSource().sendSuccess(new TranslationTextComponent("gunsrpg.command.toggledebuff", type.getRegistryName().toString()), false);
        });
        return 0;
    }

    private static int forceBloodmoon(CommandContext<CommandSource> ctx) {
        CommandSource src = ctx.getSource();
        MinecraftServer server = src.getServer();
        IServerWorldInfo worldInfo = server.getWorldData().overworldData();
        long newGameTime = ModConfig.worldConfig.bloodmoonCycle.get() * 24000L + 13000L;
        worldInfo.setGameTime(newGameTime);
        src.getLevel().setDayTime(newGameTime);
        src.sendSuccess(new TranslationTextComponent("gunsrpg.command.setbloodmoon"), false);
        return 0;
    }

    private static int modifySkillTree(CommandContext<CommandSource> ctx, @Nullable ModifyAction action) {
        CommandSource src = ctx.getSource();
        if (action == null) {
            src.sendFailure(new TranslationTextComponent("gunsrpg.command.editskills.fail"));
            return -1;
        }
        PlayerEntity player = getPlayer(ctx);
        LazyOptional<IPlayerData> optional = PlayerData.get(player);
        optional.ifPresent(data -> {
            PlayerSkills skills = data.getSkills();
            action.apply(skills);
            String translationKey = "gunsrpg.command.editskills." + (skills.getUnlockedSkills().isEmpty() ? "lock" : "unlock");
            src.sendSuccess(new TranslationTextComponent(translationKey), false);
        });
        return 0;
    }

    private static int addLevels(CommandContext<CommandSource> ctx, int levelCount) {
        PlayerEntity player = getPlayer(ctx);
        LazyOptional<IPlayerData> optional = PlayerData.get(player);
        optional.ifPresent(data -> {
            PlayerSkills skills = data.getSkills();
            int currentLevel = skills.getLevel();
            int delta = 100 - currentLevel;
            int target = Math.min(levelCount, delta);
            for (int i = 0; i < target; i++) {
                skills.nextLevel(false);
            }
            data.sync();
            ctx.getSource().sendSuccess(new TranslationTextComponent("gunsrpg.command." + (target == 1 ? "addlevel" : "addlevels"), target), false);
        });
        return 0;
    }

    private static int noArgsProvided(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        throw NO_ARGUMENTS_EXCEPTION.create();
    }

    private static PlayerEntity getPlayer(CommandContext<CommandSource> ctx) {
        return (PlayerEntity) ctx.getSource().getEntity();
    }

    private enum ModifyAction {
        LOCK(PlayerSkills::revertToDefault),
        UNLOCK(PlayerSkills::unlockAll);

        final Consumer<PlayerSkills> action;

        ModifyAction(Consumer<PlayerSkills> action) {
            this.action = action;
        }

        void apply(PlayerSkills skills) {
            action.accept(skills);
        }
    }
}
