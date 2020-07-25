package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.common.skills.interfaces.TickableSkill;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;

public class LightHunterSkill extends BasicSkill implements TickableSkill {

    public LightHunterSkill(SkillType<?> type) {
        super(type);
    }

    @Override
    public boolean apply(EntityPlayer user) {
        return true;
    }

    @Override
    public void onUpdate(EntityPlayer player) {
        PlayerSkills skills = PlayerDataFactory.get(player).getSkills();
        if(hasArmor(player)) {
            skills.lightHunterMovementSpeed = 0.015F;
        } else {
            skills.lightHunterMovementSpeed = 0.0F;
        }
    }

    public boolean hasArmor(EntityPlayer player) {
        return player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == Items.LEATHER_HELMET && player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == Items.LEATHER_CHESTPLATE && player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() == Items.LEATHER_LEGGINGS && player.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() == Items.LEATHER_BOOTS;
    }
}
