package dev.toma.gunsrpg.api.common.skill;

import com.google.gson.JsonElement;
import dev.toma.gunsrpg.api.common.data.IKillData;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.ITransactionProcessor;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import net.minecraft.util.ResourceLocation;

public interface ITransactionValidator {

    ResourceLocation getId();

    JsonElement getData();

    boolean canUnlock(IPlayerData data, SkillType<?> skillType);

    void onUnlocked(ITransactionProcessor transactionProcessor, SkillType<?> skillType);

    IKillData getData(IPlayerData data);
}
