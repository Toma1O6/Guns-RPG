package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.common.skills.core.SkillType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;

public class WarMachineSkill extends BasicSkill {

    public WarMachineSkill(SkillType<?> type) {
        super(type);
    }

    @Override
    public void onPurchase(EntityPlayer player) {
        player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
    }

    @Override
    public void onDeactivate(EntityPlayer player) {
        player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
    }
}
