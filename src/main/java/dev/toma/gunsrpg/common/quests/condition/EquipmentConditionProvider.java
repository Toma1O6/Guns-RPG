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
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.EnumMap;
import java.util.Map;

public class EquipmentConditionProvider extends AbstractQuestConditionProvider<EquipmentConditionProvider> implements IQuestCondition {

    private final Map<EquipmentSlotType, Item> map;
    private final String name;
    private final ITextComponent descriptor;

    public EquipmentConditionProvider(QuestConditionProviderType<?> type, Map<EquipmentSlotType, Item> map, String name) {
        super(type);
        this.map = map;
        this.name = name;
        this.descriptor = new TranslationTextComponent(this.getLocalizationString(), name);
    }

    public static EquipmentConditionProvider fromNbt(QuestConditionProviderType<EquipmentConditionProvider> type, CompoundNBT data) {
        String name = data.getString("equipment.name");
        ListNBT equipment = data.getList("equipment", Constants.NBT.TAG_COMPOUND);
        Map<EquipmentSlotType, Item> map = new EnumMap<>(EquipmentSlotType.class);
        for (int i = 0; i < equipment.size(); i++) {
            CompoundNBT tag = equipment.getCompound(i);
            EquipmentSlotType slotType = EquipmentSlotType.byName(tag.getString("slotType"));
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(tag.getString("item")));
            map.put(slotType, item);
        }
        return new EquipmentConditionProvider(type, map, name);
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
    public ITextComponent getDescriptor() {
        return descriptor;
    }

    @Override
    public EquipmentConditionProvider makeConditionInstance() {
        return this;
    }

    @Override
    public IQuestConditionProvider<?> getProviderType() {
        return this;
    }

    @Override
    public void saveInternalData(CompoundNBT nbt) {
        ListNBT list = new ListNBT();
        for (Map.Entry<EquipmentSlotType, Item> entry : map.entrySet()) {
            CompoundNBT data = new CompoundNBT();
            EquipmentSlotType slotType = entry.getKey();
            Item item = entry.getValue();
            data.putString("slotType", slotType.getName());
            data.putString("item", item.getRegistryName().toString());
            list.add(data);
        }
        nbt.put("equipment", list);
        nbt.putString("equipment.name", name);
    }

    public static final class Serializer implements IQuestConditionProviderSerializer<EquipmentConditionProvider> {

        @Override
        public EquipmentConditionProvider deserialize(QuestConditionProviderType<EquipmentConditionProvider> conditionType, JsonElement data) {
            JsonObject object = JsonHelper.asJsonObject(data);
            String typeOfEquipment = JSONUtils.getAsString(object, "equipmentName");
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
            return new EquipmentConditionProvider(conditionType, map, typeOfEquipment);
        }
    }
}
