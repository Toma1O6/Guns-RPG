package dev.toma.gunsrpg.common.quests.quest.area;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RemoveSpecificTasksProcessor implements IMobSpawnProcessor {

    private final MobSpawnProcessorType<? extends RemoveSpecificTasksProcessor> type;
    private final String[] goalSelectorClasses;
    private final String[] targetSelectorClasses;

    public RemoveSpecificTasksProcessor(MobSpawnProcessorType<? extends RemoveSpecificTasksProcessor> type, String[] goalSelectorClasses, String[] targetSelectorClasses) {
        this.type = type;
        this.goalSelectorClasses = goalSelectorClasses;
        this.targetSelectorClasses = targetSelectorClasses;
    }

    @Override
    public MobSpawnProcessorType<?> getType() {
        return type;
    }

    @Override
    public void processMobSpawn(LivingEntity entity, IMobTargettingContext targettingContext) {
        if (!(entity instanceof MobEntity)) return;
        MobEntity mob = (MobEntity) entity;
        List<Goal> goalsToRemove = findGoalsByClasses(mob.goalSelector, goalSelectorClasses);
        List<Goal> targetGoalsToRemove = findGoalsByClasses(mob.targetSelector, targetSelectorClasses);
        goalsToRemove.forEach(mob.goalSelector::removeGoal);
        targetGoalsToRemove.forEach(mob.targetSelector::removeGoal);
    }

    private List<Goal> findGoalsByClasses(GoalSelector selector, String[] classes) {
        List<Goal> results = new ArrayList<>();
        for (PrioritizedGoal prioritizedGoal : selector.availableGoals) {
            Goal goal = prioritizedGoal.getGoal();
            String name = goal.getClass().getName();
            for (String classpath : classes) {
                if (name.equals(classpath)) {
                    results.add(goal);
                    break;
                }
            }
        }
        return results;
    }

    public static final class Serializer implements IMobSpawnProcessorSerializer<RemoveSpecificTasksProcessor> {

        @Override
        public RemoveSpecificTasksProcessor deserialize(MobSpawnProcessorType<RemoveSpecificTasksProcessor> type, JsonElement element) throws JsonParseException {
            JsonObject object = JsonHelper.asJsonObject(element);
            String[] goalClasses = resolveArray(object, "goals");
            String[] targetGoalClasses = resolveArray(object, "targetGoals");
            return new RemoveSpecificTasksProcessor(type, goalClasses, targetGoalClasses);
        }

        @Override
        public void toNbt(RemoveSpecificTasksProcessor processor, CompoundNBT nbt) {
            ListNBT goals = new ListNBT();
            Arrays.stream(processor.goalSelectorClasses).map(StringNBT::valueOf).forEach(goals::add);
            ListNBT targetGoals = new ListNBT();
            Arrays.stream(processor.targetSelectorClasses).map(StringNBT::valueOf).forEach(targetGoals::add);
            nbt.put("goals", goals);
            nbt.put("targetGoals", targetGoals);
        }

        @Override
        public RemoveSpecificTasksProcessor fromNbt(MobSpawnProcessorType<RemoveSpecificTasksProcessor> type, CompoundNBT nbt) {
            ListNBT goals = nbt.getList("goals", Constants.NBT.TAG_STRING);
            ListNBT targetGoals = nbt.getList("targetGoals", Constants.NBT.TAG_STRING);
            return new RemoveSpecificTasksProcessor(
                    type,
                    goals.stream().map(INBT::getAsString).toArray(String[]::new),
                    targetGoals.stream().map(INBT::getAsString).toArray(String[]::new)
            );
        }

        private static String[] resolveArray(JsonObject object, String key) {
            String[] array = new String[0];
            if (object.has(key)) {
                JsonArray jsonArray = JSONUtils.getAsJsonArray(object, key);
                array = JsonHelper.deserializeInto(jsonArray, String[]::new, JsonElement::getAsString);
            }
            return array;
        }
    }
}
