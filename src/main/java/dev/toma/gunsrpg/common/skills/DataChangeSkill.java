package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import net.minecraft.entity.player.PlayerEntity;

import java.util.function.Consumer;

public class DataChangeSkill extends BasicSkill {

    private final Consumer<PlayerSkills> activationConsumer;

    public DataChangeSkill(SkillType<?> type, Consumer<PlayerSkills> consumer) {
        super(type);
        this.activationConsumer = consumer;
    }

    @Override
    public void onPurchase(PlayerEntity player) {
        PlayerDataFactory.get(player).ifPresent(data -> activationConsumer.accept(data.getSkills()));
    }
}
