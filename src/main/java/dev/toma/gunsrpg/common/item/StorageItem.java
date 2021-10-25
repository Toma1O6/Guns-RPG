package dev.toma.gunsrpg.common.item;

import dev.toma.gunsrpg.ModTabs;
import dev.toma.gunsrpg.common.container.GenericStorageContainer;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.math.IDimensions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkHooks;

public class StorageItem extends BaseItem {

    private final IDimensions dimensions;
    private final IInputFilter inputFilter;
    private final IContainerProvider containerProvider;

    public StorageItem(String name, Properties properties, int width, int height, IInputFilter filter, IContainerProvider containerProvider) {
        super(name, properties.stacksTo(1).tab(ModTabs.ITEM_TAB));
        this.dimensions = IDimensions.of(width, height);
        this.inputFilter = filter;
        this.containerProvider = containerProvider;
    }

    public IInventory getInventory(ItemStack stack, Runnable callback) {
        CompoundNBT nbt = stack.getTag();
        if (nbt == null)
            throw new IllegalArgumentException("Cannot create inventory from non existent data");
        ListNBT list = nbt.contains("Items", Constants.NBT.TAG_LIST) ? nbt.getList("Items", Constants.NBT.TAG_COMPOUND) : new ListNBT();
        return createInventoryFromData(list, dimensions.getArea(), callback);
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            if (stack.getTag() == null)
                stack.setTag(new CompoundNBT());
            ITextComponent title = getContainerName();
            INamedContainerProvider provider = new SimpleNamedContainerProvider((id, inv, own) -> containerProvider.createContainerInstance(inv, id), title);
            NetworkHooks.openGui((ServerPlayerEntity) player, provider);
            return ActionResult.success(stack);
        }
        return ActionResult.pass(stack);
    }

    public IDimensions getDimensions() {
        return dimensions;
    }

    public IInputFilter getInputFilter() {
        return inputFilter;
    }

    protected ITextComponent getContainerName() {
        return new TranslationTextComponent("screen." + ModUtils.convertToLocalization(getRegistryName()));
    }

    @FunctionalInterface
    public interface IContainerProvider {
        GenericStorageContainer createContainerInstance(PlayerInventory inventory, int windowId);
    }

    @FunctionalInterface
    public interface IInputFilter {
        boolean isValidInput(ItemStack stack);
    }

    public static IInventory createInventoryFromData(ListNBT items, int requiredSize, Runnable callback) {
        GenericStorageContainer.SavingInventory inventory = new GenericStorageContainer.SavingInventory(requiredSize, callback);
        for (int i = 0; i < items.size(); i++) {
            CompoundNBT data = items.getCompound(i);
            int slotId = data.getInt("slotId");
            ItemStack stack = ItemStack.of(data.getCompound("item"));
            inventory.setItem(slotId, stack);
        }
        return inventory;
    }

    public static void saveInventoryContents(ItemStack stack, IInventory inventory) {
        ListNBT list = new ListNBT();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack itemStack = inventory.getItem(i);
            if (itemStack.isEmpty())
                continue;
            CompoundNBT data = new CompoundNBT();
            data.putInt("slotId", i);
            data.put("item", itemStack.serializeNBT());
            list.add(data);
        }
        stack.getOrCreateTag().put("Items", list);
    }
}
