package dev.toma.gunsrpg.common.item;

import dev.toma.gunsrpg.ModTabs;
import dev.toma.gunsrpg.common.entity.GrenadeEntity;
import dev.toma.gunsrpg.common.init.GRPGEntityTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class ItemGrenade extends GRPGItem {

    private final int blastSize;
    private final boolean explodeOnImpact;

    public ItemGrenade(String name, int blastRadius, boolean explodeOnImpact) {
        super(name, new Properties().tab(ModTabs.ITEM_TAB).stacksTo(10));
        this.blastSize = blastRadius;
        this.explodeOnImpact = explodeOnImpact;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!world.isClientSide) {
            CooldownTracker tracker = player.getCooldowns();
            Item item = stack.getItem();
            if (!tracker.isOnCooldown(item)) {
                world.addFreshEntity(new GrenadeEntity(GRPGEntityTypes.GRENADE.get(), world, player, 80, blastSize, explodeOnImpact, item));
                player.playSound(SoundEvents.SNOWBALL_THROW, 1.0F, 1.0F);
                if (!player.isCreative()) {
                    stack.shrink(1);
                }
                tracker.addCooldown(item, 60);
            }
        }
        return ActionResult.pass(stack);
    }
}
