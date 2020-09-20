package dev.toma.gunsrpg.common.command;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.ModRegistry;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.DebuffData;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.common.debuffs.DebuffType;
import dev.toma.gunsrpg.config.GRPGConfig;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandGRPG extends CommandBase {

    @Override
    public String getName() {
        return "grpg";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if(args.length == 2) {
            switch (args[0]) {
                case "debuff": return getListOfStringsMatchingLastWord(args, ModRegistry.DEBUFFS.getKeys());
                case "skillTree": return getListOfStringsMatchingLastWord(args, "lockAll", "unlockAll");
                default: return Collections.emptyList();
            }
        }
        return getListOfStringsMatchingLastWord(args, "debuff", "bloodmoon", "skillTree", "levelup");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if(args.length == 0) {
            this.sendMessage(sender, TextFormatting.GREEN + "Subcommands:");
            this.sendMessage(sender, TextFormatting.GREEN + "debuff - manage your debuffs");
            this.sendMessage(sender, TextFormatting.GREEN + "bloodmoon - starts bloodmoon");
            this.sendMessage(sender, TextFormatting.GREEN + "skillTree - manage your skills");
            this.sendMessage(sender, TextFormatting.GREEN + "levelup [n] - level up n times");
        } else {
            World world = sender.getEntityWorld();
            if(!(sender instanceof EntityPlayerMP)) return;
            EntityPlayerMP player = (EntityPlayerMP) sender;
            PlayerData data = PlayerDataFactory.get(player);
            switch (args[0]) {
                case "debuff": {
                    DebuffData debuffData = data.getDebuffData();
                    if(args.length < 2) {
                        this.sendMessage(sender, TextFormatting.RED + "Write the name of debuff you want to toggle, e.g. /grpg debuff gunsrpg:poison");
                        return;
                    }
                    boolean flag = args[1].contains(":");
                    ResourceLocation location = flag ? new ResourceLocation(args[1]) : GunsRPG.makeResource(args[1]);
                    DebuffType type = ModRegistry.DEBUFFS.getValue(location);
                    if(type == null) {
                        sendMessage(sender, TextFormatting.RED + "No debuff exists with id " + location.toString());
                        return;
                    }
                    toggleDebuff(data, debuffData, type);
                    sendMessage(sender, TextFormatting.GREEN + "Debuff " + location.toString() + " has been toggled");
                    break;
                }
                case "bloodmoon": {
                    world.setWorldTime(GRPGConfig.worldConfig.bloodmoonCycle * 24000L + 13000L);
                    sendMessage(sender, TextFormatting.GREEN + "Enjoy the bloodmoon...");
                    break;
                }
                case "skillTree": {
                    if(args.length < 2) {
                        this.sendMessage(sender, TextFormatting.RED + "Unknown argument! Valid arguments: [lockAll, unlockAll]");
                        return;
                    }
                    boolean reset = args[1].equals("lockAll");
                    PlayerSkills skills = data.getSkills();
                    if(args[1].equals("lockAll")) {
                        skills.revertToDefault();
                        sendMessage(sender, TextFormatting.GREEN + "All your progress has been deleted");
                    } else if("unlockAll".equals(args[1])) {
                        skills.unlockAll();
                        sendMessage(sender, TextFormatting.GREEN + "You have unlocked all skills!");
                    } else {
                        this.sendMessage(sender, TextFormatting.RED + "Unknown argument! Valid arguments: [lockAll, unlockAll]");
                    }
                    break;
                }
                case "levelup": {
                    if(args.length < 2) {
                        levelUp(data, sender, 1);
                    } else {
                        int n = 1;
                        try {
                            n = Integer.parseInt(args[1]);
                        } catch (NumberFormatException ex) {
                            // don't care, use 1 instead of n
                        }
                        levelUp(data, sender, Math.abs(n));
                    }
                    break;
                }
                default: {
                    sendMessage(sender, TextFormatting.RED + "Unknown argument");
                    break;
                }
            }
            data.sync();
        }
    }

    private void levelUp(PlayerData data, ICommandSender sender, int n) {
        PlayerSkills skills = data.getSkills();
        for(int i = 0; i < n; i++) {
            if(skills.isMaxLevel()) break;
            skills.nextLevel(false);
        }
        sendMessage(sender, "Obtained " + n + " levels");
    }

    private void sendMessage(ICommandSender sender, String message) {
        sender.sendMessage(new TextComponentString(message));
    }

    private void toggleDebuff(PlayerData cap, DebuffData data, DebuffType type) {
        data.toggle(type);
        cap.sync();
    }
}
