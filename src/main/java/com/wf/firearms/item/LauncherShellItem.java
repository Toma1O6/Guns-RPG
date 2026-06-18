package com.wf.firearms.item;

import com.wf.firearms.launcher.LauncherShellStats;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;

/** 榴弹/火箭发射器弹药：tooltip 显示爆炸参数与用途说明。 */
public class LauncherShellItem extends Item {
    public LauncherShellItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(
            ItemStack stack, @Nullable Level level, List<Component> lines, TooltipFlag flag) {
        LauncherShellStats stats = LauncherShellStats.forItem(this);
        lines.add(
                Component.translatable(
                                "gunsrpg.launcher_shell.tooltip.damage", (int) stats.explosionDamage())
                        .withStyle(ChatFormatting.DARK_GRAY));
        lines.add(
                Component.translatable(
                                "gunsrpg.launcher_shell.tooltip.blast",
                                String.format("%.1f", stats.blastRadius()))
                        .withStyle(ChatFormatting.DARK_GRAY));
        if (stats.rocket()) {
            lines.add(
                    Component.translatable("gunsrpg.launcher_shell.tooltip.rocket")
                            .withStyle(ChatFormatting.GRAY));
        } else if (stats.explodeOnImpact()) {
            lines.add(
                    Component.translatable("gunsrpg.launcher_shell.tooltip.impact")
                            .withStyle(ChatFormatting.GRAY));
        } else if (stats.fuseTicks() > 0) {
            lines.add(
                    Component.translatable(
                                    "gunsrpg.launcher_shell.tooltip.fuse",
                                    String.format("%.1f", stats.fuseTicks() / 20f))
                            .withStyle(ChatFormatting.GRAY));
        }
        if (stats.incendiary()) {
            lines.add(
                    Component.translatable("gunsrpg.launcher_shell.tooltip.incendiary")
                            .withStyle(ChatFormatting.GOLD));
        }
        if (stats.toxic()) {
            lines.add(
                    Component.translatable("gunsrpg.launcher_shell.tooltip.toxic")
                            .withStyle(ChatFormatting.DARK_GREEN));
        }
        String descKey = descriptionKeyFor(ForgeRegistries.ITEMS.getKey(this).getPath());
        if (descKey != null) {
            lines.add(Component.translatable(descKey).withStyle(ChatFormatting.DARK_AQUA));
        }
    }

    @Nullable
    private static String descriptionKeyFor(String path) {
        return switch (path) {
            case "grenade_launcher_shell" -> "gunsrpg.launcher_shell.desc.standard";
            case "impact_grenade_launcher_shell" -> "gunsrpg.launcher_shell.desc.impact";
            case "high_explosive_grenade_launcher_shell" -> "gunsrpg.launcher_shell.desc.high_explosive";
            case "explosive_grenade_launcher_shell" -> "gunsrpg.launcher_shell.desc.explosive";
            case "sticky_grenade_launcher_shell" -> "gunsrpg.launcher_shell.desc.sticky";
            case "tear_gas_grenade_launcher_shell" -> "gunsrpg.launcher_shell.desc.tear_gas";
            case "rocket_shell", "rocket_launcher_shell" -> "gunsrpg.launcher_shell.desc.rocket";
            case "incendiary_rocket_shell" -> "gunsrpg.launcher_shell.desc.incendiary_rocket";
            default -> null;
        };
    }
}
