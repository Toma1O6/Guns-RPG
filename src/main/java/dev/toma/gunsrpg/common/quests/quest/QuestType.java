package dev.toma.gunsrpg.common.quests.quest;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.UUID;

public final class QuestType<D extends IQuestData, Q extends Quest<D>> {

    private final ResourceLocation id;
    private final IQuestDataResolver<D> resolver;
    private final IQuestFactory<D, Q> factory;

    public QuestType(ResourceLocation id, IQuestDataResolver<D> resolver, IQuestFactory<D, Q> factory) {
        this.id = id;
        this.resolver = resolver;
        this.factory = factory;
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

    public Q newQuestInstance(World world, QuestScheme<D> scheme, UUID traderId) {
        return factory.makeQuestInstance(world, scheme, traderId);
    }

    public Q fromContext(QuestDeserializationContext<D> context) {
        return factory.questFromContext(context);
    }

    public interface IQuestDataResolver<D> {

        D resolve(JsonElement element) throws JsonParseException;

        CompoundNBT serialize(D data);

        D deserialize(CompoundNBT nbt);
    }
}
