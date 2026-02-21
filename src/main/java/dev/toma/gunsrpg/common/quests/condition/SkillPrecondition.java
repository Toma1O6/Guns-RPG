package dev.toma.gunsrpg.common.quests.condition;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import dev.toma.gunsrpg.api.common.data.ISkillProvider;
import dev.toma.gunsrpg.common.init.ModRegistries;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ITagCollection;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeTagHandler;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public final class SkillPrecondition implements Predicate<ISkillProvider> {

    private final List<SkillType<?>> skills;
    private final boolean requireAll;

    private SkillPrecondition(List<SkillType<?>> skills, boolean requireAll) {
        this.skills = skills;
        this.requireAll = requireAll;
    }

    public static SkillPrecondition fromNbt(ListNBT nbt, boolean requireAll) {
        ImmutableList.Builder<SkillType<?>> builder = ImmutableList.builder();
        for (int i = 0; i < nbt.size(); i++) {
            SkillType<?> type = ModRegistries.SKILLS.getValue(new ResourceLocation(nbt.getString(i)));
            if (type != null)
                builder.add(type);
        }
        return new SkillPrecondition(builder.build(), requireAll);
    }

    public ListNBT toNbt() {
        ListNBT nbt = new ListNBT();
        for (SkillType<?> skill : this.skills) {
            nbt.add(StringNBT.valueOf(skill.getRegistryName().toString()));
        }
        return nbt;
    }

    @Override
    public boolean test(ISkillProvider provider) {
        if (this.skills.isEmpty())
            return true;
        if (this.requireAll) {
            return this.skills.stream()
                    .allMatch(provider::hasSkill);
        } else {
            return this.skills.stream()
                    .anyMatch(provider::hasSkill);
        }
    }

    @SuppressWarnings("unchecked")
    public static SkillPrecondition parse(JsonArray array, boolean requireAll) throws JsonParseException {
        Set<SkillType<?>> set = new HashSet<>();
        for (JsonElement element : array) {
            String id = element.getAsString();
            if (id.startsWith("#")) {
                // resolve tag
                ResourceLocation tagId = ResourceLocation.tryParse(id.substring(1));
                ITagCollection<SkillType<?>> collection = (ITagCollection<SkillType<?>>) ForgeTagHandler.getCustomTagTypes().get(ModRegistries.SKILLS.getRegistryName());
                ITag<SkillType<?>> tag = collection.getTag(tagId);
                if (tag == null)
                    throw new JsonParseException("Unknown skill tag: " + id);
                set.addAll(tag.getValues());
            } else {
                ResourceLocation skillId = ResourceLocation.tryParse(id);
                SkillType<?> type = ModRegistries.SKILLS.getValue(skillId);
                if (type == null)
                    throw new JsonParseException("Unknown skill: " + id);
                set.add(type);
            }
        }
        return new SkillPrecondition(
                ImmutableList.copyOf(set),
                requireAll
        );
    }
}
