package dev.toma.gunsrpg.common.command;

import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.AbilityData;
import dev.toma.gunsrpg.common.capability.object.DebuffData;
import dev.toma.gunsrpg.common.capability.object.SkillData;
import dev.toma.gunsrpg.common.skilltree.PlayerSkillTree;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

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
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length == 0) {
            this.sendMessage(sender, TextFormatting.GREEN + "Subcommands:");
            this.sendMessage(sender, TextFormatting.GREEN + "debuff - manage your debuffs");
            this.sendMessage(sender, TextFormatting.GREEN + "bloodmoon - starts bloodmoon");
            this.sendMessage(sender, TextFormatting.GREEN + "resetSkillTree - manage your skills");
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
                        case "broken_leg": {
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
                            sendMessage(sender, TextFormatting.RED + "Unknown debuff type! Known types: [poison, infection, broken_leg, bleeding]");
                            break;
                        }
                    }
                    break;
                }
                case "bloodmoon": {
                    world.setWorldTime(181000);
                    sendMessage(sender, TextFormatting.GREEN + "Enjoy the bloodmoon...");
                    break;
                }
                case "resetSkillTree": {
                    SkillData skillData = data.getSkillData();
                    PlayerSkillTree skillTree = skillData.getSkillTree();
                    AbilityData abilityData = skillData.getAbilityData();
                    skillTree.setDefault();
                    abilityData.reset();
                    skillData.killCount.clear();
                    sendMessage(sender, TextFormatting.GREEN + "All your progress has been deleted");
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

    private void sendMessage(ICommandSender sender, String message) {
        sender.sendMessage(new TextComponentString(message));
    }

    private void toggleDebuff(PlayerData cap, DebuffData data, int i) {
        data.getDebuffs()[i].toggle();
        cap.sync();
    }
}
