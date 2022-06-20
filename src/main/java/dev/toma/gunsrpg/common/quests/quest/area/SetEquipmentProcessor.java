package dev.toma.gunsrpg.common.quests.quest.area;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

public class SetEquipmentProcessor implements IMobSpawnProcessor {

    private final MobSpawnProcessorType<SetEquipmentProcessor> type;
    private final Map<EquipmentSlotType, Item> equipment;

    public SetEquipmentProcessor(MobSpawnProcessorType<SetEquipmentProcessor> type, Map<EquipmentSlotType, Item> equipment) {
        this.type = type;
        this.equipment = equipment;
    }

    @Override
    public MobSpawnProcessorType<?> getType() {
        return type;
    }

    @Override
    public void processMobSpawn(LivingEntity entity, IMobTargettingContext targettingContext) {
        for (Map.Entry<EquipmentSlotType, Item> entry : equipment.entrySet()) {
            EquipmentSlotType slotType = entry.getKey();
            Item item = entry.getValue();
            ItemStack itemStack = new ItemStack(item, 1);
            entity.setItemSlot(slotType, itemStack);
        }
    }

    public static final class Serializer implements IMobSpawnProcessorSerializer<SetEquipmentProcessor> {

        @Override
        public SetEquipmentProcessor deserialize(MobSpawnProcessorType<SetEquipmentProcessor> type, JsonElement element) throws JsonParseException {
            JsonObject object = JsonHelper.asJsonObject(element);
            JsonObject equipment = JSONUtils.getAsJsonObject(object, "equipment");
            Map<EquipmentSlotType, Item> equipmentMap = new EnumMap<>(EquipmentSlotType.class);
            Set<Map.Entry<String, JsonElement>> entrySet = equipment.entrySet();
            for (Map.Entry<String, JsonElement> entry : entrySet) {
                EquipmentSlotType slotType;
                try {
                    slotType = EquipmentSlotType.valueOf(entry.getKey().toUpperCase());
                } catch (Exception e) {
                    throw new JsonSyntaxException("Unknown equipment slot type: " + entry.getKey());
                }
                ResourceLocation itemId = new ResourceLocation(entry.getValue().getAsString());
                Item item = ForgeRegistries.ITEMS.getValue(itemId);
                equipmentMap.put(slotType, item);
            }
            return new SetEquipmentProcessor(type, equipmentMap);
        }

        @Override
        public void toNbt(SetEquipmentProcessor processor, CompoundNBT nbt) {
            ListNBT list = new ListNBT();
            for (Map.Entry<EquipmentSlotType, Item> entry : processor.equipment.entrySet()) {
                CompoundNBT data = new CompoundNBT();
                data.putString("slotType", entry.getKey().name());
                data.putString("item", entry.getValue().getRegistryName().toString());
                list.add(data);
            }
            nbt.put("equipment", list);
        }

        @Override
        public SetEquipmentProcessor fromNbt(MobSpawnProcessorType<SetEquipmentProcessor> type, CompoundNBT nbt) {
            ListNBT list = nbt.getList("equipment", Constants.NBT.TAG_COMPOUND);
            Map<EquipmentSlotType, Item> map = new EnumMap<>(EquipmentSlotType.class);
            list.forEach(inbt -> {
                CompoundNBT data = (CompoundNBT) inbt;
                EquipmentSlotType slotType = EquipmentSlotType.valueOf(data.getString("slotType"));
                ResourceLocation itemId = new ResourceLocation(data.getString("item"));
                Item item = ForgeRegistries.ITEMS.getValue(itemId);
                map.put(slotType, item);
            });
            return new SetEquipmentProcessor(type, map);
        }
    }
}
