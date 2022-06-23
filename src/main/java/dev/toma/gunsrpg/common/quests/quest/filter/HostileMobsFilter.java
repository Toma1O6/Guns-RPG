package dev.toma.gunsrpg.common.quests.quest.filter;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.nbt.CompoundNBT;

public class HostileMobsFilter implements IEntityFilter {

    private static final HostileMobsFilter HOSTILE_ONLY = new HostileMobsFilter();

    public static IEntityFilter hostileMobsOnly() {
        return HOSTILE_ONLY;
    }

    @Override
    public EntityFilterType<?> getType() {
        return EntityFilterType.HOSTILE;
    }

    @Override
    public boolean test(Entity entity) {
        return entity instanceof MonsterEntity;
    }

    @Override
    public String toString() {
        return "All hostile mobs";
    }

    public static final class Serializer implements EntityFilterType.Serializer<HostileMobsFilter> {

        @Override
        public HostileMobsFilter resolveFile(EntityFilterType<HostileMobsFilter> type, JsonElement json) throws JsonParseException {
            return HOSTILE_ONLY;
        }

        @Override
        public void toNbt(HostileMobsFilter filter, CompoundNBT nbt) {
        }

        @Override
        public HostileMobsFilter fromNbt(EntityFilterType<HostileMobsFilter> filterType, CompoundNBT nbt) {
            return HOSTILE_ONLY;
        }
    }
}
