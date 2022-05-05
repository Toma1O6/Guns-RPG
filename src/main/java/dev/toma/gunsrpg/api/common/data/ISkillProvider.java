package dev.toma.gunsrpg.api.common.data;

import dev.toma.gunsrpg.api.common.skill.ISkill;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import net.minecraft.entity.player.PlayerEntity;

public interface ISkillProvider extends ILockStateChangeable {

    void tick(PlayerEntity player);

    void unlock(SkillType<?> type);

    void unlock(SkillType<?> type, boolean shouldSyncClient);

    void lock(SkillType<?> type);

    boolean hasSkill(SkillType<?> type);

    <S extends ISkill> S getSkill(SkillType<S> type);

    void onLevelUp(int level, PlayerEntity player);
}
