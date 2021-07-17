package dev.toma.gunsrpg.util;

import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.skills.AdrenalineRushSkill;
import dev.toma.gunsrpg.common.skills.CraftingSkill;
import dev.toma.gunsrpg.common.skills.core.ISkill;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.PickaxeItem;

@SuppressWarnings("unchecked")
public class SkillUtil {

    public static <S extends ISkill> S getBestSkillFromOverrides(S skill, PlayerEntity player) {
        PlayerSkills skills = PlayerDataFactory.get(player).orElseThrow(NullPointerException::new).getSkills();
        if (skill == null)
            return null;
        while (skill.getType().isOverriden() && skills.hasSkill(skill.getType().getOverride())) {
            skill = (S) skills.getSkill(skill.getType().getOverride());
        }
        return skill;
    }

    public static float getReloadTimeMultiplier(PlayerEntity player) {
        PlayerSkills skills = PlayerDataFactory.getUnsafe(player).getSkills();
        if (skills.hasSkill(Skills.ADRENALINE_RUSH_I)) {
            AdrenalineRushSkill ars = getBestSkillFromOverrides(skills.getSkill(Skills.ADRENALINE_RUSH_I), player);
            return ars.apply(player) ? 1.0F - ars.getReloadMultiplier() : 1.0F;
        }
        return 1.0F;
    }

    public static int getGunpowderCraftAmount(PlayerEntity player) {
        return getCraftingAmount(PlayerDataFactory.getUnsafe(player).getSkills(), Skills.GUNPOWDER_MASTER, Skills.GUNPOWDER_EXPERT, Skills.GUNPOWDER_NOVICE);
    }

    public static int getBonemealCraftAmount(PlayerEntity player) {
        return getCraftingAmount(PlayerDataFactory.getUnsafe(player).getSkills(), Skills.BONE_GRINDER_III, Skills.BONE_GRINDER_II, Skills.BONE_GRINDER_I);
    }

    public static int getBlazepowderCraftAmount(PlayerEntity player) {
        return getCraftingAmount(PlayerDataFactory.getUnsafe(player).getSkills(), Skills.BLAZE_POWDER_III, Skills.BLAZE_POWDER_II, Skills.BLAZE_POWDER_I);
    }

    public static int getAmmoAmount(PlayerEntity player) {
        return PlayerDataFactory.hasActiveSkill(player, Skills.AMMO_SMITHING_MASTERY) ? 2 : 1;
    }

    public static int getCrossbowBoltAmount(PlayerEntity player) {
        return PlayerDataFactory.hasActiveSkill(player, Skills.AMMO_SMITHING_MASTERY) ? 3 : 2;
    }

    public static float getAxeSpeedModifier(float input, AxeItem axe, PlayerEntity player) {
        return input;
    }

    public static float getPickaxeSpeedModifier(float input, PickaxeItem pickaxe, PlayerEntity player) {
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
        if (skills.hasSkill(type)) {
            return skills.getSkill(type).getOutputAmount();
        }
        return -1;
    }
}
