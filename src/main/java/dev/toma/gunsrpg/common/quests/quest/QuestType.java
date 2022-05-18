package dev.toma.gunsrpg.common.quests.quest;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

public final class QuestType<D extends IQuestData> {

    private final ResourceLocation id;
    private final IQuestDataResolver<D> resolver;

    public QuestType(ResourceLocation id, IQuestDataResolver<D> resolver) {
        this.id = id;
        this.resolver = resolver;
    }

    public ResourceLocation getId() {
        return id;
    }

    public CompoundNBT writeData(D data) {
        return resolver.serialize(data);
    }

    public D readData(CompoundNBT nbt) {
        return resolver.deserialize(nbt);
    }

    public D resolveJson(JsonElement element) throws JsonParseException {
        return resolver.resolve(element);
    }

    // TODO
    public interface IQuestDataResolver<D> {

        D resolve(JsonElement element) throws JsonParseException;

        default CompoundNBT serialize(D data) {
            return new CompoundNBT();
        }

        default D deserialize(CompoundNBT nbt) {
            return null;
        }
    }
}
