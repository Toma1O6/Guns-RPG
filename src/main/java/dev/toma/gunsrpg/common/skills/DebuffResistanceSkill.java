package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import net.minecraft.entity.player.EntityPlayer;

import java.util.function.Consumer;

public class DebuffResistanceSkill extends BasicSkill {

    private final Consumer<PlayerSkills> consumer;

    public DebuffResistanceSkill(SkillType<?> type, Consumer<PlayerSkills> consumer) {
        super(type);
        this.consumer = consumer;
    }

    @Override
    public void onPurchase(EntityPlayer player) {
        consumer.accept(PlayerDataFactory.get(player).getSkills());
    }
}
