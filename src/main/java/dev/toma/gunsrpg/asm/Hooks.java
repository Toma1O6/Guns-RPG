package dev.toma.gunsrpg.asm;

import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.skills.AdrenalineRushSkill;
import dev.toma.gunsrpg.config.GRPGConfig;
import dev.toma.gunsrpg.util.SkillUtil;
import dev.toma.gunsrpg.world.cap.WorldDataFactory;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class Hooks {

    public static double modifyAttackDelay(PlayerEntity player) {
        double value = player.getAttributeValue(Attributes.ATTACK_SPEED);
        PlayerData data = PlayerDataFactory.get(player).orElseThrow(NullPointerException::new);
        PlayerSkills skills = data.getSkills();
        AdrenalineRushSkill adrenaline = SkillUtil.getBestSkillFromOverrides(skills.getSkill(Skills.ADRENALINE_RUSH_I), player);
        if (adrenaline != null) {
            value *= adrenaline.getAttackSpeedBoost();
        }
        return value;
    }

    public static double modifyFollowDistance(MobEntity mob) {
        double attributeValue = mob.getAttributeValue(Attributes.FOLLOW_RANGE);
        World world = mob.level;
        if (WorldDataFactory.isBloodMoon(world)) {
            return Math.max(attributeValue, GRPGConfig.worldConfig.bloodMoonMobAgroRange.get());
        }
        return attributeValue;
    }

    public static int getCreeperMaxFuse(CreeperEntity creeper) {
        if (WorldDataFactory.isBloodMoon(creeper.level))
            return 10;
        return 30;
    }
}
