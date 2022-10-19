package dev.toma.gunsrpg.common.quests.quest.filter;

import com.google.gson.*;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.quests.QuestSystem;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.Objects;

public class SpecificEntityFilter implements IEntityFilter {

    private final EntityType<?>[] types;

    public SpecificEntityFilter(EntityType<?>[] types) {
        this.types = types;
    }

    @Override
    public EntityFilterType<?> getType() {
        return EntityFilterType.SPECIFIC;
    }

    @Override
    public boolean test(Entity entity) {
        return ModUtils.contains(entity.getType(), types);
    }

    @Override
    public String toString() {
        return String.format("[%s]", String.join(",", Arrays.stream(types).map(type -> type.getRegistryName().toString()).toArray(String[]::new)));
    }

    public static final class Serializer implements EntityFilterType.Serializer<SpecificEntityFilter> {

        @Override
        public SpecificEntityFilter resolveFile(EntityFilterType<SpecificEntityFilter> type, JsonElement json) throws JsonParseException {
            JsonObject object = JsonHelper.asJsonObject(json);
            JsonArray array = JSONUtils.getAsJsonArray(object, "filter");
            EntityType<?>[] types = JsonHelper.deserializeInto(array, EntityType[]::new, el -> {
                ResourceLocation resource = new ResourceLocation(el.getAsString());
                EntityType<?> entityType = ForgeRegistries.ENTITIES.getValue(resource);
                if (entityType == null) {
                    GunsRPG.log.warn(QuestSystem.MARKER, "Skipping unknown entity filter: {}", resource);
                    return null;
                }
                return entityType;
            });
            EntityType<?>[] validTypes = Arrays.stream(types).filter(Objects::nonNull).toArray(EntityType[]::new);
            if (validTypes.length == 0) {
                throw new JsonSyntaxException("Invalid entity filter! Atleast one entity must be defined");
            }
            return new SpecificEntityFilter(validTypes);
        }

        @Override
        public void toNbt(SpecificEntityFilter filter, CompoundNBT nbt) {
            ListNBT list = new ListNBT();
            Arrays.stream(filter.types).map(type -> StringNBT.valueOf(type.getRegistryName().toString())).forEach(list::add);
            nbt.put("filter", list);
        }

        @Override
        public SpecificEntityFilter fromNbt(EntityFilterType<SpecificEntityFilter> filterType, CompoundNBT nbt) {
            ListNBT list = nbt.getList("filter", Constants.NBT.TAG_STRING);
            EntityType<?>[] validTypes = list.stream().map(inbt -> {
                ResourceLocation resourceLocation = new ResourceLocation(inbt.getAsString());
                return ForgeRegistries.ENTITIES.getValue(resourceLocation);
            }).toArray(EntityType[]::new);
            return new SpecificEntityFilter(validTypes);
        }
    }
}
