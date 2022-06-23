package dev.toma.gunsrpg.common.quests.quest.filter;

import net.minecraft.entity.Entity;

import java.util.function.Predicate;

public interface IEntityFilter extends Predicate<Entity> {

    EntityFilterType<?> getType();
}
