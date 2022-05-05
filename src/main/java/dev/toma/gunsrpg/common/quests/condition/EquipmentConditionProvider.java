package dev.toma.gunsrpg.common.quests.condition;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.EnumMap;
import java.util.Map;

public class EquipmentConditionProvider extends AbstractQuestConditionProvider implements IQuestCondition {

    private final Map<EquipmentSlotType, Item> map;

    public EquipmentConditionProvider(QuestConditionProviderType<?> type, Map<EquipmentSlotType, Item> map) {
        super(type);
        this.map = map;
    }

    @Override
    public boolean isValid(PlayerEntity player, IPropertyReader reader) {
        for (Map.Entry<EquipmentSlotType, Item> entry : map.entrySet()) {
            EquipmentSlotType slotType = entry.getKey();
            Item item = entry.getValue();
            ItemStack equipped = player.getItemBySlot(slotType);
            if (equipped.getItem() != item) {
                return false;
            }
        }
        return true;
    }

    @Override
    public IQuestCondition getCondition() {
        return this;
    }

    public static final class Serializer implements IQuestConditionProviderSerializer<EquipmentConditionProvider> {

        @Override
        public EquipmentConditionProvider deserialize(QuestConditionProviderType<EquipmentConditionProvider> conditionType, JsonElement data) {
            JsonObject object = JsonHelper.asJsonObject(data);
            JsonObject slots = JSONUtils.getAsJsonObject(object, "slots");
            Map<EquipmentSlotType, Item> map = new EnumMap<>(EquipmentSlotType.class);
            for (Map.Entry<String, JsonElement> entry : slots.entrySet()) {
                EquipmentSlotType slotType;
                try {
                    slotType = EquipmentSlotType.valueOf(entry.getKey().toUpperCase());
                } catch (Exception e) {
                    throw new JsonSyntaxException("Unknown equipment slot: " + entry.getKey());
                }
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(entry.getValue().getAsString()));
                map.put(slotType, item);
            }
            return new EquipmentConditionProvider(conditionType, map);
        }
    }
}
