package dev.toma.gunsrpg.common.item;

import dev.toma.gunsrpg.client.animation.Animation;
import dev.toma.gunsrpg.client.animation.AnimationManager;
import dev.toma.gunsrpg.client.animation.Animations;
import dev.toma.gunsrpg.client.animation.IHandRenderer;
import dev.toma.gunsrpg.common.ModRegistry;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.util.ModUtils;
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

@SuppressWarnings({"UseBulkOperation", "ManualArrayToCollectionCopy"})
public abstract class ItemHeal extends GRPGItem implements IHandRenderer {

    private final String[] desc;
    private final Supplier<SoundEvent> useSound;
    private final Predicate<EntityPlayer> predicate;
    private final Consumer<EntityPlayer> consumer;
    private final int useTime;

    public ItemHeal(String name, int useTime, Supplier<SoundEvent> useSound, Consumer<EntityPlayer> consumer, String... desc) {
        this(name, useTime, useSound, consumer, ModUtils.truePredicate(), desc);
    }

    public ItemHeal(String name, int useTime, Supplier<SoundEvent> useSound, Consumer<EntityPlayer> consumer, Predicate<EntityPlayer> predicate, String... desc) {
        super(name);
        this.useTime = useTime;
        this.useSound = useSound;
        this.consumer = consumer;
        this.predicate = predicate;
        this.desc = desc;
    }

    @SideOnly(Side.CLIENT)
    public abstract Animation getUseAnimation(int ticks);

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        for(String string : desc) {
            tooltip.add(string);
        }
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
        if(predicate.test(playerIn)) {
            if(playerIn.world.isRemote) {
                playerIn.playSound(this.useSound.get(), 1.0F, 1.0F);
                AnimationManager.sendNewAnimation(Animations.HEAL, this.getUseAnimation(stack.getMaxItemUseDuration()));
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
                EntityPlayer player = (EntityPlayer) entityLiving;
                PlayerData data = PlayerDataFactory.get(player);
                consumer.accept(player);
                if(data.getSkills().hasSkill(ModRegistry.Skills.EFFICIENT_MEDS)) {
                    entityLiving.heal(4.0F);
                }
                if(!((EntityPlayer) entityLiving).isCreative()) {
                    stack.shrink(1);
                }
            }
        }
        return stack;
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
}
