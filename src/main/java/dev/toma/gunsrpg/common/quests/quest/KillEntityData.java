package dev.toma.gunsrpg.common.quests.quest;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.quests.quest.filter.EntityFilterType;
import dev.toma.gunsrpg.common.quests.quest.filter.IEntityFilter;
import dev.toma.gunsrpg.common.quests.sharing.QuestingGroup;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.math.MathHelper;

public class KillEntityData implements IQuestData {

    protected final IEntityFilter filter;
    protected final int kills;
    protected final float memberScaling;

    public KillEntityData(IEntityFilter filter, int kills, float memberScaling) {
        this.filter = filter;
        this.kills = kills;
        this.memberScaling = memberScaling;
    }

    protected IEntityFilter getEntityFilter() {
        return filter;
    }

    protected int getKillTarget(QuestingGroup group, boolean multiplier) {
        return multiplier ? MathHelper.ceil(this.kills * (1.0F + (this.memberScaling - 1.0F) * (group.getMemberCount() - 1.0F))) : this.kills;
    }

    protected int getKillTargetUnmodified() {
        return this.kills;
    }

    public static String formatKillRequirement(int value, Quest<? extends KillEntityData> quest) {
        return value > 0 ? String.valueOf(value) : String.valueOf(quest.getScheme().getData().getKillTargetUnmodified());
    }

    @Override
    public String toString() {
        return String.format("KillEntities - Filter: %s, Count: %d", filter.toString(), kills);
    }

    public static final class Serializer implements QuestType.IQuestDataResolver<KillEntityData> {

        @Override
        public KillEntityData resolve(JsonElement element) throws JsonParseException {
            JsonObject object = JsonHelper.asJsonObject(element);
            JsonElement filterElement = object.get("entities");
            IEntityFilter entityFilter = EntityFilterType.resolveJsonFile(filterElement);
            int count = JSONUtils.getAsInt(object, "count", 1);
            float scale = JSONUtils.getAsFloat(object, "memberScale", GunsRPG.config.quests.defaultKillMemberMultiplier);
            return new KillEntityData(entityFilter, count, scale);
        }

        @Override
        public CompoundNBT serialize(KillEntityData data) {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putInt("kills", data.kills);
            nbt.put("filter", EntityFilterType.serializeNbt(data.filter));
            nbt.putFloat("memberScale", data.memberScaling);
            return nbt;
        }

        @Override
        public KillEntityData deserialize(CompoundNBT nbt) {
            return new KillEntityData(
                    EntityFilterType.deserializeNbt(nbt.getCompound("filter")),
                    nbt.getInt("kills"),
                    nbt.getFloat("memberScale")
            );
        }
    }
}
