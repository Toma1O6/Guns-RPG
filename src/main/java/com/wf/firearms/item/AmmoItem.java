package com.wf.firearms.item;

import com.wf.firearms.combat.AmmoCaliber;
import com.wf.firearms.combat.AmmoMaterial;
import com.wf.firearms.combat.AmmoStatProfile;
import com.wf.firearms.combat.AmmoStatsRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;

/** 弹药物品：tooltip 显示口径与 Guns RPG 式属性。 */
public class AmmoItem extends Item {
    public AmmoItem(Properties properties) {
        super(properties);
    }

    public AmmoMaterial getMaterial() {
        return AmmoMaterial.fromItem(this);
    }

    public AmmoCaliber getCaliber() {
        return AmmoCaliber.fromAmmoItem(this);
    }

    @Override
    public void appendHoverText(
            ItemStack stack,
            @Nullable net.minecraft.world.level.Level level,
            List<Component> lines,
            TooltipFlag flag) {
        AmmoCaliber caliber = getCaliber();
        AmmoMaterial material = getMaterial();
        lines.add(
                Component.translatable(
                                "gunsrpg.ammo.caliber_line",
                                Component.translatable("gunsrpg.caliber." + caliber.suffix()))
                        .withStyle(ChatFormatting.GRAY));
        lines.add(Component.translatable("gunsrpg.ammo.stat.attributes").withStyle(ChatFormatting.GRAY));

        AmmoStatProfile stats = AmmoStatsRegistry.profile(caliber, material);
        appendStat(lines, "gunsrpg.ammo.stat.recoil", stats.recoilPercent());
        appendStat(lines, "gunsrpg.ammo.stat.durability", stats.durabilityPercent());
        appendStat(lines, "gunsrpg.ammo.stat.jamming", stats.jamPercent());

        int dmgPct = AmmoStatsRegistry.damagePercent(material, caliber);
        lines.add(
                Component.translatable("gunsrpg.ammo.stat.damage", dmgPct)
                        .withStyle(ChatFormatting.DARK_GREEN));
    }

    private static void appendStat(List<Component> lines, String key, int percent) {
        ChatFormatting color;
        if (key.contains("recoil")) {
            color = percent > 0 ? ChatFormatting.RED : (percent < 0 ? ChatFormatting.GREEN : ChatFormatting.GRAY);
        } else if (key.contains("jamming")) {
            color = percent < 0 ? ChatFormatting.GREEN : (percent > 0 ? ChatFormatting.RED : ChatFormatting.GRAY);
        } else {
            color = percent > 0 ? ChatFormatting.GREEN : (percent < 0 ? ChatFormatting.RED : ChatFormatting.GRAY);
        }
        String sign = percent > 0 ? "+" : "";
        lines.add(Component.translatable(key, sign + percent).withStyle(color));
    }

    public static String itemId(Item item) {
        var key = ForgeRegistries.ITEMS.getKey(item);
        return key != null ? key.toString() : "";
    }
}
