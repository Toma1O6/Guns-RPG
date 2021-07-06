package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.common.skills.core.SkillType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;

public class WarMachineSkill extends BasicSkill {

    public WarMachineSkill(SkillType<?> type) {
        super(type);
    }

    @Override
    public void onPurchase(PlayerEntity player) {
        player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(40.0D);
    }

    @Override
    public void onDeactivate(PlayerEntity player) {
        player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0D);
    }
}
