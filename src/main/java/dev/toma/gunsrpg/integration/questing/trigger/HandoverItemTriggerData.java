package dev.toma.gunsrpg.integration.questing.trigger;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class HandoverItemTriggerData {

    private final PlayerEntity player;
    private final ItemStack stack;
    private final Entity entity;

    public HandoverItemTriggerData(PlayerEntity player, ItemStack stack, Entity entity) {
        this.player = player;
        this.stack = stack;
        this.entity = entity;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public ItemStack getStack() {
        return stack;
    }

    public Entity getEntity() {
        return entity;
    }
}
