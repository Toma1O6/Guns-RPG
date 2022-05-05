package dev.toma.gunsrpg.common.quests.quest;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.item.ItemStack;

public class ItemHandoverData implements IQuestData {

    private final ItemStack[] items;

    public ItemHandoverData(ItemStack[] items) {
        this.items = items;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <Q extends IQuestData> Q copy() {
        return (Q) new ItemHandoverData(items);
    }

    public static final class Serializer implements QuestType.IQuestDataResolver<ItemHandoverData> {

        @Override
        public ItemHandoverData resolve(JsonElement element) throws JsonParseException {
            return null;
        }
    }
}
