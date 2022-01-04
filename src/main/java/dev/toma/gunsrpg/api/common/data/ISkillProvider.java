package dev.toma.gunsrpg.api.common.data;

import dev.toma.gunsrpg.api.common.skill.IDisplayableSkill;
import dev.toma.gunsrpg.api.common.skill.ISkill;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Collection;
import java.util.List;

public interface ISkillProvider extends ILockStateChangeable {

    void tick(PlayerEntity player);

    void unlock(SkillType<?> type);

    void unlock(SkillType<?> type, boolean shouldSyncClient);

    boolean hasSkill(SkillType<?> type);

    <S extends ISkill> S getSkill(SkillType<S> type);

    Collection<ISkill> getUnlockedSkills();

    List<IDisplayableSkill> getDisplayableSkills();

    void onLevelUp(int level, PlayerEntity player);
}
