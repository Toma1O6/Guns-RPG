package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.common.ModRegistry;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;

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
        ((EntityPlayerMP) player).connection.sendPacket(new SPacketSoundEffect(ModRegistry.GRPGSounds.USE_WELL_FED, SoundCategory.MASTER, player.posX, player.posY, player.posZ, 1.0F, 1.0F));
    }
}
