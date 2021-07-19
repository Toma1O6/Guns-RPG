package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SPlaySoundEffectPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
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

    public void applyEffects(PlayerEntity player) {
        Random random = new Random();
        if (player.level.isClientSide || random.nextFloat() >= chance) return;
        int amplifier = level - 1;
        int duration = 1200 + amplifier * 600;
        player.addEffect(new EffectInstance(Effects.ABSORPTION, duration, amplifier));
        ((ServerPlayerEntity) player).connection.send(new SPlaySoundEffectPacket(ModSounds.USE_WELL_FED, SoundCategory.MASTER, player.getX(), player.getY(), player.getZ(), 1.0F, 1.0F));
    }
}
