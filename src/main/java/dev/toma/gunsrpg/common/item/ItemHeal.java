package dev.toma.gunsrpg.common.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.ModTabs;
import dev.toma.gunsrpg.client.animation.Animations;
import dev.toma.gunsrpg.client.animation.IAnimation;
import dev.toma.gunsrpg.client.animation.IHandRenderer;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.sided.ClientSideManager;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class ItemHeal extends GRPGItem implements IHandRenderer {

    private final String[] desc;
    private final Supplier<SoundEvent> useSound;
    private final Predicate<PlayerEntity> predicate;
    private final Consumer<PlayerEntity> consumer;
    private final int useTime;

    public ItemHeal(String name, int useTime, Supplier<SoundEvent> useSound, Consumer<PlayerEntity> consumer, String... desc) {
        this(name, useTime, useSound, consumer, ModUtils.truePredicate(), desc);
    }

    public ItemHeal(String name, int useTime, Supplier<SoundEvent> useSound, Consumer<PlayerEntity> consumer, Predicate<PlayerEntity> predicate, String... desc) {
        super(name, new Properties().tab(ModTabs.ITEM_TAB));
        this.useTime = useTime;
        this.useSound = useSound;
        this.consumer = consumer;
        this.predicate = predicate;
        this.desc = desc;
    }

    @OnlyIn(Dist.CLIENT)
    public abstract IAnimation getUseAnimation(int ticks);

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        for(String string : desc) {
            tooltip.add(new StringTextComponent(string));
        }
    }

    @Override
    public int getUseDuration(ItemStack p_77626_1_) {
        return useTime;
    }

    @Override
    public UseAction getUseAnimation(ItemStack p_77661_1_) {
        return UseAction.NONE;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (predicate.test(player)) {
            if (world.isClientSide) {
                player.playSound(useSound.get(), 1.0F, 1.0F);
                ClientSideManager.processor().play(Animations.HEAL, getUseAnimation(stack.getUseDuration()));
            }
            player.startUsingItem(hand);
            return ActionResult.success(stack);
        }
        return ActionResult.pass(stack);
    }



    @Override
    public void releaseUsing(ItemStack stack, World world, LivingEntity entity, int timeLeft) {
        if (world.isClientSide)
            ClientSideManager.processor().stop(Animations.HEAL);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity entity) {
        if (!world.isClientSide) {
            if (entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entity;
                PlayerDataFactory.get(player).ifPresent(data -> {
                    consumer.accept(player);
                    if (data.getSkills().hasSkill(Skills.EFFICIENT_MEDS)) {
                        entity.heal(4.0F);
                    }
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }
                });
            }
        }
        return stack;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void transformRightArm(MatrixStack stack) {
        stack.mulPose(Vector3f.XP.rotationDegrees(-80.0F));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void transformLeftArm(MatrixStack stack) {
        stack.mulPose(Vector3f.XP.rotationDegrees(-80.0F));
    }
}
