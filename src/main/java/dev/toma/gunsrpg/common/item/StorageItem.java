package dev.toma.gunsrpg.common.item;

import dev.toma.gunsrpg.ModTabs;
import dev.toma.gunsrpg.common.container.GenericStorageContainer;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.math.IDimensions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class StorageItem extends BaseItem {

    private final IStorageFactory storageFactory;
    private final IDimensions dimensions;
    private final IInputFilter inputFilter;
    private final IContainerProvider containerProvider;

    public StorageItem(String name, Properties properties, int size, int width, int height, IInputFilter filter, IContainerProvider containerProvider) {
        super(name, properties.stacksTo(1).tab(ModTabs.ITEM_TAB));
        this.storageFactory = () -> new ItemStackHandler(size);
        this.dimensions = IDimensions.of(width, height);
        this.inputFilter = filter;
        this.containerProvider = containerProvider;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new StorageProvider(storageFactory, dimensions);
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
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
    public interface IStorageFactory {

        IItemHandlerModifiable createStorageHandler();
    }

    @FunctionalInterface
    public interface IInputFilter {

        boolean isValidInput(ItemStack stack);
    }

    public static class StorageProvider implements ICapabilitySerializable<ListNBT> {

        private final IItemHandlerModifiable itemHandler;
        private final LazyOptional<IItemHandlerModifiable> optional;

        public StorageProvider(IStorageFactory factory, IDimensions dimensions) {
            this.itemHandler = factory.createStorageHandler();
            if (!dimensions.validate(itemHandler.getSlots()))
                throw new IllegalArgumentException("Invalid inventory size [" + itemHandler.getSlots() + "]");
            this.optional = LazyOptional.of(() -> itemHandler);
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? optional.cast() : null;
        }

        @Override
        public ListNBT serializeNBT() {
            ListNBT nbt = new ListNBT();
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                ItemStack stack = itemHandler.getStackInSlot(i);
                if (stack.isEmpty())
                    continue;
                CompoundNBT tag = new CompoundNBT();
                tag.putInt("slotId", i);
                tag.put("item", stack.serializeNBT());
                nbt.add(tag);
            }
            return nbt;
        }

        @Override
        public void deserializeNBT(ListNBT nbt) {
            for (int i = 0; i < nbt.size(); i++) {
                CompoundNBT tag = nbt.getCompound(i);
                int slotID = tag.getInt("slotId");
                ItemStack item = ItemStack.of(tag.getCompound("item"));
                itemHandler.setStackInSlot(slotID, item);
            }
        }
    }
}
