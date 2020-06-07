package dev.toma.gunsrpg.common.item.util;

import dev.toma.gunsrpg.client.animation.Animation;
import dev.toma.gunsrpg.client.animation.AnimationManager;
import dev.toma.gunsrpg.client.animation.Animations;
import dev.toma.gunsrpg.client.animation.IHandRenderer;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.DebuffData;
import dev.toma.gunsrpg.common.item.GRPGItem;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class DebuffHeal extends GRPGItem implements IHandRenderer {

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

    @SideOnly(Side.CLIENT)
    public Animation getUseAnimation(ItemStack stack) {
        return new Animations.Antidotum(this.getMaxItemUseDuration(stack));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderRightArm() {
        GlStateManager.rotate(-80.0F, 1.0F, 0.0F, 0.0F);
        renderArm(EnumHandSide.RIGHT);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderLeftArm() {
        GlStateManager.rotate(-80.0F, 1.0F, 0.0F, 0.0F);
        renderArm(EnumHandSide.LEFT);
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
        return EnumAction.NONE;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if(predicate.test(PlayerDataFactory.get(playerIn).getDebuffData())) {
            if(playerIn.world.isRemote) {
                playerIn.playSound(this.useSound.get(), 1.0F, 1.0F);
                AnimationManager.sendNewAnimation(Animations.HEAL, this.getUseAnimation(stack));
            }
            playerIn.setActiveHand(handIn);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
        return new ActionResult<>(EnumActionResult.FAIL, stack);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        if(worldIn.isRemote) {
            AnimationManager.cancelAnimation(Animations.HEAL);
        }
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
