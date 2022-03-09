package dev.toma.gunsrpg.common.container;

import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.UUID;

public class SimpleContainerChangeListener implements IContainerListener {

    private final UUID uuid = UUID.randomUUID();
    private final ISlotChangeCallback callback;

    public SimpleContainerChangeListener(ISlotChangeCallback callback) {
        this.callback = callback;
    }

    @Override
    public void refreshContainer(Container p_71110_1_, NonNullList<ItemStack> p_71110_2_) {

    }

    @Override
    public void slotChanged(Container container, int slot, ItemStack stack) {
        callback.onSlotChanged(slot, stack);
    }

    @Override
    public void setContainerData(Container p_71112_1_, int p_71112_2_, int p_71112_3_) {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleContainerChangeListener that = (SimpleContainerChangeListener) o;
        return uuid.equals(that.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    public interface ISlotChangeCallback {
        void onSlotChanged(int slot, ItemStack stack);
    }
}
