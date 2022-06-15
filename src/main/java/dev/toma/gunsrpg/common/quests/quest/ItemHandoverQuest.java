package dev.toma.gunsrpg.common.quests.quest;

import dev.toma.gunsrpg.client.render.infobar.QuestDisplayDataModel;
import dev.toma.gunsrpg.common.quests.QuestProperties;
import dev.toma.gunsrpg.common.quests.trigger.Trigger;
import dev.toma.gunsrpg.common.quests.trigger.TriggerResponseStatus;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import it.unimi.dsi.fastutil.objects.Object2IntAVLTreeMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;

public class ItemHandoverQuest extends Quest<ItemHandoverData> implements IAdditionalClientInfo {

    public static final String DELIVER = "quest.deliver";
    private static final ITextComponent[] NOTES = { new TranslationTextComponent("quest.deliver.info").withStyle(TextFormatting.ITALIC) };
    public static final IQuestFactory<ItemHandoverData, ItemHandoverQuest> FACTORY = IQuestFactory.of(ItemHandoverQuest::new, ItemHandoverQuest::new);
    private final Object2IntMap<Item> dataMap = new Object2IntAVLTreeMap<>(this::compareItems);

    public ItemHandoverQuest(QuestScheme<ItemHandoverData> scheme, UUID traderId) {
        super(scheme, traderId);
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
    public ITextComponent[] getAdditionalNotes() {
        return NOTES;
    }

    @Override
    protected void fillDataModel(QuestDisplayDataModel model) {
        model.addQuestHeader(this, false);
        model.addConditionDisplay(this);
        dataMap.forEach((item, remainder) -> model.addInformationRow(new TranslationTextComponent(DELIVER, item.getName(ItemStack.EMPTY)), this, q -> new StringTextComponent(remainder + "x")));
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
        if (itemStack.isEmpty()) {
            return TriggerResponseStatus.PASS;
        }
        Item item = itemStack.getItem();
        if (!dataMap.containsKey(item)) {
            return TriggerResponseStatus.PASS;
        }
        int remainder = dataMap.getInt(item);
        return remainder > 0 ? TriggerResponseStatus.OK : TriggerResponseStatus.PASS;
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
        trySyncClient();
    }

    private void initializeData() {
        ItemHandoverData data = this.getActiveData();
        ItemStack[] itemStacks = data.getItems();
        for (ItemStack stack : itemStacks) {
            dataMap.put(stack.getItem(), stack.getCount());
        }
    }

    private int compareItems(Item item1, Item item2) {
        return item1.getRegistryName().compareTo(item2.getRegistryName());
    }
}
