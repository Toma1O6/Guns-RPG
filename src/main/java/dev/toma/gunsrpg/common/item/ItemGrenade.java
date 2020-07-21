package dev.toma.gunsrpg.common.item;

import dev.toma.gunsrpg.common.entity.EntityGrenade;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemGrenade extends GRPGItem {

    private final int blastSize;
    private final boolean explodeOnImpact;

    public ItemGrenade(String name, int blastRadius, boolean explodeOnImpact) {
        super(name);
        this.blastSize = blastRadius;
        this.explodeOnImpact = explodeOnImpact;
        setMaxStackSize(1);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if(!worldIn.isRemote && entityIn instanceof EntityPlayer) {
            if(isCooking(stack)) {
                int fuse = getFuse(stack);
                if(fuse <= 0) {
                    ((EntityPlayer) entityIn).inventory.removeStackFromSlot(itemSlot);
                    worldIn.spawnEntity(new EntityGrenade(worldIn, (EntityPlayer) entityIn, 0, blastSize, explodeOnImpact, stack));
                }
                --fuse;
                stack.getTagCompound().setInteger("fuse", fuse);
            }
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if(isCooking(stack)) {
            if(!worldIn.isRemote) {
                worldIn.spawnEntity(new EntityGrenade(worldIn, playerIn, this.getFuse(stack), blastSize, explodeOnImpact, stack));
                playerIn.inventory.removeStackFromSlot(playerIn.inventory.currentItem);
            }
        } else {
            setCooking(stack);
            worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.MASTER, 1.0F, 1.0F);
        }
        return ActionResult.newResult(EnumActionResult.PASS, stack);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    private boolean isCooking(ItemStack stack) {
        return stack.hasTagCompound() && stack.getTagCompound().getBoolean("cooking");
    }

    private int getFuse(ItemStack stack) {
        return stack.hasTagCompound() ? stack.getTagCompound().getInteger("fuse") : 100;
    }

    private void setCooking(ItemStack stack) {
        if(!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        NBTTagCompound nbt = stack.getTagCompound();
        nbt.setBoolean("cooking", true);
        nbt.setInteger("fuse", 100);
    }
}
