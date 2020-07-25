package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.common.ModRegistry;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.common.skills.interfaces.Cooldown;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;

public class SecondChanceSkill extends BasicSkill implements Cooldown {

    private final int maxCooldown;
    private final int healAmount;
    private int cooldown;

    public SecondChanceSkill(SkillType<?> type, int maxCooldown, int healAmount) {
        super(type);
        this.maxCooldown = maxCooldown;
        this.healAmount = healAmount;
    }

    @Override
    public void onUpdate(EntityPlayer player) {
        if(cooldown > 0) --cooldown;
    }

    @Override
    public boolean apply(EntityPlayer user) {
        return getCooldown() == 0;
    }

    @Override
    public int getCooldown() {
        return cooldown;
    }

    @Override
    public void setOnCooldown() {
        this.cooldown = getMaxCooldown();
    }

    @Override
    public int getMaxCooldown() {
        return maxCooldown;
    }

    @Override
    public void onUse(EntityPlayer player) {
        player.setHealth(this.healAmount);
        player.world.playSound(null, player.posX, player.posY, player.posZ, ModRegistry.GRPGSounds.SECOND_CHANCE_USE, SoundCategory.MASTER, 1.0F, 1.0F);
    }

    @Override
    public void writeExtra(NBTTagCompound nbt) {
        nbt.setInteger("cooldown", cooldown);
    }

    @Override
    public void readExtra(NBTTagCompound nbt) {
        cooldown = nbt.getInteger("cooldown");
    }
}
