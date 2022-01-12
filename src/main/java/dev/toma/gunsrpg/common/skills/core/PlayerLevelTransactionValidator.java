package dev.toma.gunsrpg.common.skills.core;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.skill.ISkillProperties;
import dev.toma.gunsrpg.api.common.skill.ITransactionValidator;
import dev.toma.gunsrpg.api.common.data.IKillData;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.ITransactionProcessor;
import dev.toma.gunsrpg.common.skills.transaction.SkillPointTransaction;
import net.minecraft.util.ResourceLocation;

public class PlayerLevelTransactionValidator implements ITransactionValidator {

    public static final ResourceLocation ID = GunsRPG.makeResource("level");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public JsonElement getData() {
        return new JsonObject();
    }

    @Override
    public boolean canUnlock(IPlayerData data, SkillType<?> skillType) {
        IKillData killData = data.getProgressData();
        ISkillProperties properties = skillType.getProperties();
        return killData.getLevel() >= properties.getRequiredLevel() && killData.getPoints() >= properties.getPrice();
    }

    @Override
    public void onUnlocked(ITransactionProcessor processor, SkillType<?> skillType) {
        processor.processTransaction(new SkillPointTransaction(skillType));
    }

    @Override
    public IKillData getData(IPlayerData data) {
        return data.getProgressData();
    }
}
