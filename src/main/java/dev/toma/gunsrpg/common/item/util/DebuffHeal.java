package dev.toma.gunsrpg.common.item.util;

import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.DebuffData;
import dev.toma.gunsrpg.common.item.GRPGItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class DebuffHeal extends GRPGItem {

    private int useTime;
    private Predicate<DebuffData> predicate;
    private Consumer<DebuffData> debuffDataConsumer;

    public DebuffHeal(String name, int useTime, Predicate<DebuffData> predicate, Consumer<DebuffData> debuffDataConsumer) {
        super(name);
        this.useTime = useTime;
        this.predicate = predicate;
        this.debuffDataConsumer = debuffDataConsumer;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return useTime;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.EAT;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if(predicate.test(PlayerDataFactory.get(playerIn).getDebuffData())) {
            playerIn.setActiveHand(handIn);
            return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
        }
        return new ActionResult<>(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        if(!worldIn.isRemote) {
            if(entityLiving instanceof EntityPlayer) {
                PlayerData data = PlayerDataFactory.get((EntityPlayer) entityLiving);
                debuffDataConsumer.accept(data.getDebuffData());
                data.sync();
                if(!((EntityPlayer) entityLiving).isCreative()) {
                    stack.shrink(1);
                }
            }
        }
        return stack;
    }
}
