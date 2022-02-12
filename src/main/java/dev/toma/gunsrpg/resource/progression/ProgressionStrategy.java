package dev.toma.gunsrpg.resource.progression;

import com.google.gson.*;
import dev.toma.gunsrpg.api.common.data.IKillData;
import dev.toma.gunsrpg.resource.util.functions.Functions;
import dev.toma.gunsrpg.resource.util.functions.IFunction;
import dev.toma.gunsrpg.resource.util.functions.IFunctionAdapter;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Type;

public class ProgressionStrategy implements IProgressionStrategy {

    private final KillRequirement[] requirements;
    private final Reward[] rewards;

    public ProgressionStrategy(KillRequirement[] requirements, Reward[] rewards) {
        this.requirements = requirements;
        this.rewards = rewards;
    }

    @Override
    public int getRequiredKillCount(int level) {
        for (KillRequirement requirement : requirements) {
            if (requirement.function.canApplyFor(level)) {
                return requirement.amount;
            }
        }
        return Integer.MAX_VALUE;
    }

    @Override
    public void applyRewards(PlayerEntity player, IKillData data, int level) {
        for (Reward reward : rewards) {
            if (reward.function.canApplyFor(level)) {
                for (ILevelReward levelReward : reward.rewards) {
                    levelReward.applyTo(player, data);
                }
                break;
            }
        }
    }

    public static class Adapter implements JsonDeserializer<IProgressionStrategy> {

        @Override
        public IProgressionStrategy deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject object = JsonHelper.asJsonObject(json);
            JsonArray requirementArray = JSONUtils.getAsJsonArray(object, "killRequirements");
            KillRequirement[] requirements = JsonHelper.deserializeInto(requirementArray, KillRequirement[]::new, KillRequirement::fromJson);
            JsonArray rewardArray = JSONUtils.getAsJsonArray(object, "rewards");
            Reward[] rewards = JsonHelper.deserializeInto(rewardArray, Reward[]::new, Reward::fromJson);
            return new ProgressionStrategy(requirements, rewards);
        }
    }

    private static class Reward {

        private final IFunction function;
        private final ILevelReward[] rewards;

        private Reward(IFunction function, ILevelReward[] rewards) {
            this.function = function;
            this.rewards = rewards;
        }

        static Reward fromJson(JsonElement element) {
            JsonObject object = JsonHelper.asJsonObject(element);
            String functionId = JSONUtils.getAsString(object, "function");
            IFunctionAdapter<?> adapter = Functions.getRegistry().getAdapter(functionId);
            if (adapter == null) {
                throw new JsonSyntaxException("Unknown function " + functionId);
            }
            IFunction function = adapter.resolveFromJson(object);
            JsonArray rewardJson = JSONUtils.getAsJsonArray(object, "rewards");
            ILevelReward[] rewards = JsonHelper.deserializeInto(rewardJson, ILevelReward[]::new, Reward::resolveReward);
            return new Reward(function, rewards);
        }

        static ILevelReward resolveReward(JsonElement element) {
            JsonObject object = JsonHelper.asJsonObject(element);
            String adapterId = JSONUtils.getAsString(object, "type");
            ILevelRewardAdapter<?> adapter = LevelRewards.getAdapter(new ResourceLocation(adapterId));
            if (adapter == null) {
                throw new JsonSyntaxException("Unknown reward type " + adapterId);
            }
            return adapter.resolveFromJson(object);
        }
    }

    private static class KillRequirement {

        private final IFunction function;
        private final int amount;

        public KillRequirement(IFunction function, int amount) {
            this.function = function;
            this.amount = amount;
        }

        static KillRequirement fromJson(JsonElement element) {
            JsonObject object = JsonHelper.asJsonObject(element);
            String functionId = JSONUtils.getAsString(object, "function");
            IFunctionAdapter<?> adapter = Functions.getRegistry().getAdapter(functionId);
            if (adapter == null) {
                throw new JsonSyntaxException("Unknown function " + functionId);
            }
            IFunction function = adapter.resolveFromJson(object);
            int required = JSONUtils.getAsInt(object, "require");
            return new KillRequirement(function, required);
        }
    }
}
