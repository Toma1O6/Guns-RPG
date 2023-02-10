package dev.toma.gunsrpg.common.item;

import dev.toma.gunsrpg.ModTabs;
import dev.toma.gunsrpg.common.entity.FlareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class FlareItem extends BaseItem {

    public FlareItem(String name) {
        super(name, new Properties().tab(ModTabs.ITEM_TAB));
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!world.isClientSide) {
            CooldownTracker tracker = player.getCooldowns();
            Item item = stack.getItem();
            if (!tracker.isOnCooldown(item)) {
                FlareEntity entity = new FlareEntity(world, player);
                entity.setThrownByPlayer(player);
                world.addFreshEntity(entity);
                world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundCategory.MASTER, 1.0F, 1.0F);
                if (!player.isCreative()) {
                    stack.shrink(1);
                }
                tracker.addCooldown(item, 30);
            }
        }
        return ActionResult.pass(stack);
    }
}
