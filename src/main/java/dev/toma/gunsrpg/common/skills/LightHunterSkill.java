package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.api.common.skill.ITickableSkill;
import dev.toma.gunsrpg.common.attribute.*;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Items;

import java.util.UUID;

public class LightHunterSkill extends BasicSkill implements ITickableSkill {

    private static final IAttributeModifier MOVEMENT_SPEED_MODIFIER = AttributeModifierFactory.modifier(UUID.fromString("9942EE39-CCD1-4F37-88E4-98711C2CF3EB"), AttributeOps.SUM, 0.015);
    private boolean validityState;

    public LightHunterSkill(SkillType<?> type) {
        super(type);
    }

    @Override
    public boolean canApply(PlayerEntity user) {
        return true;
    }

    @Override
    public void onUpdate(PlayerEntity player) {
        PlayerData.get(player).ifPresent(data -> {
            IAttributeProvider provider = data.getAttributes();
            boolean lastState = validityState;
            validityState = hasArmor(player);
            if (lastState != validityState) {
                IAttribute attribute = provider.getAttribute(Attribs.MOVEMENT_SPEED);
                if (validityState)
                    attribute.addModifier(MOVEMENT_SPEED_MODIFIER);
                else
                    attribute.removeModifier(MOVEMENT_SPEED_MODIFIER);
            }
        });
    }

    public boolean hasArmor(PlayerEntity player) {
        return player.getItemBySlot(EquipmentSlotType.HEAD).getItem() == Items.LEATHER_HELMET && player.getItemBySlot(EquipmentSlotType.CHEST).getItem() == Items.LEATHER_CHESTPLATE && player.getItemBySlot(EquipmentSlotType.LEGS).getItem() == Items.LEATHER_LEGGINGS && player.getItemBySlot(EquipmentSlotType.FEET).getItem() == Items.LEATHER_BOOTS;
    }
}
