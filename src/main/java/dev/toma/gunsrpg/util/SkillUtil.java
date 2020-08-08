package dev.toma.gunsrpg.util;

import dev.toma.gunsrpg.common.ModRegistry;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.common.skills.AdrenalineRushSkill;
import dev.toma.gunsrpg.common.skills.CraftingSkill;
import dev.toma.gunsrpg.common.skills.core.ISkill;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;

@SuppressWarnings("unchecked")
public class SkillUtil {

    public static <S extends ISkill> S getBestSkillFromOverrides(S skill, EntityPlayer player) {
        PlayerSkills skills = PlayerDataFactory.get(player).getSkills();
        while (skill.getType().isOverriden() && skills.hasSkill(skill.getType().getOverride())) {
            skill = (S) skills.getSkill(skill.getType().getOverride());
        }
        return skill;
    }

    public static float getReloadTimeMultiplier(EntityPlayer player) {
        PlayerSkills skills = PlayerDataFactory.get(player).getSkills();
        if(skills.hasSkill(ModRegistry.Skills.ADRENALINE_RUSH_I)) {
            AdrenalineRushSkill ars = getBestSkillFromOverrides(skills.getSkill(ModRegistry.Skills.ADRENALINE_RUSH_I), player);
            return ars.apply(player) ? 1.0F - ars.getReloadMultiplier() : 1.0F;
        }
        return 1.0F;
    }

    public static int getGunpowderCraftAmount(EntityPlayer player) {
        return getCraftingAmount(PlayerDataFactory.get(player).getSkills(), ModRegistry.Skills.GUNPOWDER_MASTER, ModRegistry.Skills.GUNPOWDER_EXPERT, ModRegistry.Skills.GUNPOWDER_NOVICE);
    }

    public static int getBonemealCraftAmount(EntityPlayer player) {
        return getCraftingAmount(PlayerDataFactory.get(player).getSkills(), ModRegistry.Skills.BONE_GRINDER_III, ModRegistry.Skills.BONE_GRINDER_II, ModRegistry.Skills.BONE_GRINDER_I);
    }

    public static int getAmmoAmount(EntityPlayer player) {
        return PlayerDataFactory.hasActiveSkill(player, ModRegistry.Skills.AMMO_SMITHING_MASTERY) ? 2 : 1;
    }

    public static int getCrossbowBoltAmount(EntityPlayer player) {
        return PlayerDataFactory.hasActiveSkill(player, ModRegistry.Skills.AMMO_SMITHING_MASTERY) ? 3 : 2;
    }

    public static float getAxeSpeedModifier(float input, ItemAxe axe, EntityPlayer player) {
        return input;
    }

    public static float getPickaxeSpeedModifier(float input, ItemPickaxe pickaxe, EntityPlayer player) {
        return input;
    }

    private static int getCraftingAmount(PlayerSkills skills, SkillType<? extends CraftingSkill>... array) {
        for (SkillType<? extends CraftingSkill> skillType : array) {
            int count = getOutputAmount(skills, skillType);
            if (count > 0) {
                return count;
            }
        }
        return 0;
    }

    private static int getOutputAmount(PlayerSkills skills, SkillType<? extends CraftingSkill> type) {
        if(skills.hasSkill(type)) {
            return skills.getSkill(type).getOutputAmount();
        }
        return -1;
    }
}
