package dev.toma.gunsrpg.resource.progression;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.toma.gunsrpg.api.common.data.IKillData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.JSONUtils;

public class SkillPointReward implements ILevelReward {

    private final int amount;

    public SkillPointReward(int amount) {
        this.amount = amount;
    }

    @Override
    public void applyTo(PlayerEntity player, IKillData data) {
        data.addPoints(amount);
    }

    public static class Adapter implements ILevelRewardAdapter<SkillPointReward> {

        @Override
        public SkillPointReward resolveFromJson(JsonObject object) throws JsonParseException {
            int amount = JSONUtils.getAsInt(object, "amount");
            return new SkillPointReward(amount);
        }
    }
}
