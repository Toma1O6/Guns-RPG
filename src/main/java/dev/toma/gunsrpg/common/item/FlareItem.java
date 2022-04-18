package dev.toma.gunsrpg.common.item;

import dev.toma.gunsrpg.ModTabs;
import dev.toma.gunsrpg.common.entity.FlareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class FlareItem extends BaseItem {

    public FlareItem(String name) {
        super(name, new Properties().tab(ModTabs.ITEM_TAB));
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!world.isClientSide) {
            FlareEntity entity = new FlareEntity(world, player);
            entity.setThrownByPlayer(player);
            world.addFreshEntity(entity);
            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundCategory.MASTER, 1.0F, 1.0F);
            if (!player.isCreative()) {
                stack.shrink(1);
            }
        }
        return ActionResult.pass(stack);
    }
}
