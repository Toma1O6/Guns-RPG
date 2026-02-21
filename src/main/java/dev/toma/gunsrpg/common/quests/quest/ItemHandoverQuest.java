package dev.toma.gunsrpg.common.quests.quest;

import dev.toma.gunsrpg.client.render.infobar.QuestDisplayDataModel;
import dev.toma.gunsrpg.common.quests.QuestProperties;
import dev.toma.gunsrpg.common.quests.trigger.Trigger;
import dev.toma.gunsrpg.common.quests.trigger.TriggerResponseStatus;
import dev.toma.gunsrpg.util.math.WeightedRandom;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import it.unimi.dsi.fastutil.objects.Object2IntAVLTreeMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

public class ItemHandoverQuest extends Quest<ItemHandoverData> implements AdditionalObjectiveInfo {

    public static final String DELIVER = "quest.deliver";
    private static final ITextComponent NOTE = new TranslationTextComponent("quest.deliver.info").withStyle(TextFormatting.ITALIC);
    public static final IQuestFactory<ItemHandoverData, ItemHandoverQuest> FACTORY = IQuestFactory.of(ItemHandoverQuest::new, ItemHandoverQuest::new);
    private final Object2IntMap<Item> dataMap = new Object2IntAVLTreeMap<>(this::compareItems);
    private final Random random = new Random();

    public ItemHandoverQuest(IQuestFactory.InstanceContext<ItemHandoverData, ?> context) {
        super(context);
        this.initializeData();
    }

    public ItemHandoverQuest(QuestDeserializationContext<ItemHandoverData> context) {
        super(context);
    }

    @Override
    public void registerTriggers(ITriggerRegistration registration) {
        registration.addEntry(Trigger.ITEM_HANDOVER, this::tryItemHandover, this::handleSuccessfulHandover);
    }

    @Override
    public Object[] getDescriptionArguments() {
        return new Object[0];
    }

    @Override
    public List<ITextComponent> additionalInfo() {
        List<ITextComponent> list = this.dataMap.object2IntEntrySet().stream()
                .map(entry -> {
                    Item item = entry.getKey();
                    int count = entry.getIntValue();
                    return new StringTextComponent("- " + count + "x").append(" ").append(item.getName(ItemStack.EMPTY));
                })
                .collect(Collectors.toList());
        list.add(new StringTextComponent(" "));
        list.add(NOTE);
        return list;
    }

    @Override
    protected void fillDataModel(QuestDisplayDataModel model) {
        model.addQuestHeader(this, false);
        dataMap.forEach((item, remainder) -> model.addInformationRow(
                this,
                q -> new TranslationTextComponent(DELIVER, item.getName(ItemStack.EMPTY)),
                q -> new StringTextComponent(remainder + "x"))
        );
        model.addConditionDisplay(this);
    }

    @Override
    protected void writeQuestData(CompoundNBT nbt) {
        ListNBT list = new ListNBT();
        dataMap.forEach((item, remainder) -> {
            CompoundNBT itemNbt = new CompoundNBT();
            itemNbt.putString("item", item.getRegistryName().toString());
            itemNbt.putInt("remainder", remainder);
            list.add(itemNbt);
        });
        nbt.put("items", list);
    }

    @Override
    protected void readQuestData(CompoundNBT nbt) {
        dataMap.clear();
        ListNBT list = nbt.getList("items", Constants.NBT.TAG_COMPOUND);
        list.forEach(inbt -> {
            CompoundNBT itemNbt = (CompoundNBT) inbt;
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemNbt.getString("item")));
            int remainder = itemNbt.getInt("remainder");
            dataMap.put(item, remainder);
        });
    }

    private TriggerResponseStatus tryItemHandover(Trigger trigger, IPropertyReader reader) {
        ItemStack itemStack = reader.getProperty(QuestProperties.USED_ITEM);
        UUID uuid = reader.getProperty(QuestProperties.UUID);
        UUID questId = this.getMayorUUID();
        if (itemStack.isEmpty()) {
            return TriggerResponseStatus.PASS;
        }
        Item item = itemStack.getItem();
        if (!dataMap.containsKey(item)) {
            return TriggerResponseStatus.PASS;
        }
        int remainder = dataMap.getInt(item);
        if (questId.equals(uuid) || questId.equals(Util.NIL_UUID)) {
            return remainder > 0 ? TriggerResponseStatus.OK : TriggerResponseStatus.PASS;
        }
        return TriggerResponseStatus.PASS;
    }

    private void handleSuccessfulHandover(Trigger trigger, IPropertyReader reader) {
        ItemStack itemStack = reader.getProperty(QuestProperties.USED_ITEM);
        if (itemStack.isEmpty()) return;
        Item item = itemStack.getItem();
        int count = itemStack.getCount();
        int remainder = dataMap.getInt(item);
        int taken = Math.min(count, remainder);
        itemStack.shrink(taken);
        int result = remainder - taken;
        if (result <= 0) {
            dataMap.removeInt(item);
        } else {
            dataMap.put(item, result);
        }
        if (dataMap.isEmpty()) {
            setStatus(QuestStatus.COMPLETED);
        }
        trySyncClient(this.level);
    }

    private void initializeData() {
        ItemHandoverData data = this.getActiveData();
        int min = data.getMinItems();
        int max = data.getMaxItems();
        int items = min + this.random.nextInt(Math.max(0, max - min) + 1);
        int maxRolls = items * 2;
        int currentRolls = 0;
        while (this.dataMap.size() < items && currentRolls < maxRolls) {
            WeightedRandom<ItemHandoverData.ResourceGroup> groups = data.getGroups();
            ItemHandoverData.ResourceGroup group = groups.getRandom();
            ItemStack itemStack = group.generate(this.random);
            if (!this.dataMap.containsKey(itemStack.getItem())) {
                this.dataMap.put(itemStack.getItem(), itemStack.getCount());
            }
            ++currentRolls;
        }
    }

    private int compareItems(Item item1, Item item2) {
        return item1.getRegistryName().compareTo(item2.getRegistryName());
    }
}
