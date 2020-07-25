package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.common.skills.core.SkillType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

import java.util.Random;

public class WellFedSkill extends BasicSkill {

    private final int level;
    private final float chance;

    public WellFedSkill(SkillType<?> type, int level, float chance) {
        super(type);
        this.level = level;
        this.chance = chance;
    }

    public void applyEffects(EntityPlayer player) {
        Random random = new Random();
        if(player.world.isRemote || random.nextFloat() >= chance) return;
        int amplifier = level - 1;
        int duration = 1200 + amplifier * 600;
        player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, duration, amplifier));
    }
}
