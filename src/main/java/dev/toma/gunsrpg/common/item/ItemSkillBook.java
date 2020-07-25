package dev.toma.gunsrpg.common.item;

import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemSkillBook extends GRPGItem {

    public ItemSkillBook(String name) {
        super(name);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if(!worldIn.isRemote) {
            PlayerData data = PlayerDataFactory.get(playerIn);
            data.getSkills().addSkillPoints(1);
            data.sync();
            if(!playerIn.isCreative()) {
                stack.shrink(1);
            }
            playerIn.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 0.75F, 1.0F);
        }
        return ActionResult.newResult(EnumActionResult.PASS, stack);
    }
}
