package dev.toma.gunsrpg.common.quests.condition;

import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.entity.player.PlayerEntity;

public interface IQuestCondition {

    boolean isValid(PlayerEntity player, IPropertyReader reader);
}
