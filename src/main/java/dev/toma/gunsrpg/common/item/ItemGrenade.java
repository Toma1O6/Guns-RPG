package dev.toma.gunsrpg.common.item;

import dev.toma.gunsrpg.common.entity.EntityGrenade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemGrenade extends GRPGItem {

    private final int blastSize;
    private final boolean explodeOnImpact;

    public ItemGrenade(String name, int blastRadius, boolean explodeOnImpact) {
        super(name);
        this.blastSize = blastRadius;
        this.explodeOnImpact = explodeOnImpact;
        setMaxStackSize(10);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if(!worldIn.isRemote) {
            CooldownTracker tracker = playerIn.getCooldownTracker();
            if(!tracker.hasCooldown(stack.getItem())) {
                worldIn.spawnEntity(new EntityGrenade(worldIn, playerIn, 80, blastSize, explodeOnImpact, stack));
                playerIn.playSound(SoundEvents.ENTITY_SNOWBALL_THROW, 1.0F, 1.0F);
                if(!playerIn.isCreative()) {
                    stack.shrink(1);
                }
                tracker.setCooldown(stack.getItem(), 40);
            }
        }
        return ActionResult.newResult(EnumActionResult.PASS, stack);
    }
}
