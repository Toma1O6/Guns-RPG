package dev.toma.gunsrpg.common.command;

import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.DebuffData;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.config.GRPGConfig;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
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
                case "debuff": return getListOfStringsMatchingLastWord(args, "poison", "infection", "broken_bone", "bleeding");
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
                        this.sendMessage(sender, TextFormatting.RED + "Write the name of debuff you want to toggle, e.g. /grpg debuff poison");
                        return;
                    }
                    switch (args[1]) {
                        case "poison": {
                            toggleDebuff(data, debuffData, 0);
                            sendMessage(sender, TextFormatting.GREEN + "Debuff toggled");
                            break;
                        }
                        case "infection": {
                            toggleDebuff(data, debuffData, 1);
                            sendMessage(sender, TextFormatting.GREEN + "Debuff toggled");
                            break;
                        }
                        case "broken_bone": {
                            toggleDebuff(data, debuffData, 2);
                            sendMessage(sender, TextFormatting.GREEN + "Debuff toggled");
                            break;
                        }
                        case "bleeding": {
                            toggleDebuff(data, debuffData, 3);
                            sendMessage(sender, TextFormatting.GREEN + "Debuff toggled");
                            break;
                        }
                        default: {
                            sendMessage(sender, TextFormatting.RED + "Unknown debuff type! Known types: [poison, infection, broken_bone, bleeding]");
                            break;
                        }
                    }
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
                        levelUp(data, sender, n);
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

    private void toggleDebuff(PlayerData cap, DebuffData data, int i) {
        data.getDebuffs()[i].toggle();
        cap.sync();
    }
}
