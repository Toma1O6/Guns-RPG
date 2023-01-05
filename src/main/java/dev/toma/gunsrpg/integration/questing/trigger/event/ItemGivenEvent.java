package dev.toma.gunsrpg.integration.questing.trigger.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class ItemGivenEvent {

    private final ItemStack itemStack;
    private final PlayerEntity source;
    private final Entity receiver;

    public ItemGivenEvent(ItemStack itemStack, PlayerEntity source, Entity receiver) {
        this.itemStack = itemStack;
        this.source = source;
        this.receiver = receiver;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public PlayerEntity getSource() {
        return source;
    }

    public Entity getReceiver() {
        return receiver;
    }
}
