package dev.toma.gunsrpg.common.item.util;

import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.DebuffData;
import dev.toma.gunsrpg.common.item.GRPGItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class DebuffHeal extends GRPGItem {

    private final int useTime;
    private final Predicate<DebuffData> predicate;
    private final Consumer<DebuffData> debuffDataConsumer;
    private final String desc;
    private final Supplier<SoundEvent> useSound;

    public DebuffHeal(String name, int useTime, Supplier<SoundEvent> supplier, String desc, Predicate<DebuffData> predicate, Consumer<DebuffData> debuffDataConsumer) {
        super(name);
        this.useTime = useTime;
        this.useSound = supplier;
        this.predicate = predicate;
        this.debuffDataConsumer = debuffDataConsumer;
        this.desc = desc;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(desc);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return useTime;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if(predicate.test(PlayerDataFactory.get(playerIn).getDebuffData())) {
            if(playerIn.world.isRemote) {
                playerIn.playSound(this.useSound.get(), 1.0F, 1.0F);
            }
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
