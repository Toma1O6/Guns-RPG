package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import net.minecraft.entity.player.EntityPlayer;

import java.util.function.Consumer;

public class DataChangeSkill extends BasicSkill {

    private final Consumer<PlayerSkills> activationConsumer;

    public DataChangeSkill(SkillType<?> type, Consumer<PlayerSkills> consumer) {
        super(type);
        this.activationConsumer = consumer;
    }

    @Override
    public void onPurchase(EntityPlayer player) {
        activationConsumer.accept(PlayerDataFactory.get(player).getSkills());
    }
}
