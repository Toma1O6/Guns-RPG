package dev.toma.gunsrpg.common.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.ModTabs;
import dev.toma.gunsrpg.client.animation.Animations;
import dev.toma.gunsrpg.client.animation.IAnimation;
import dev.toma.gunsrpg.client.animation.IHandRenderer;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.sided.ClientSideManager;
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
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.*;

public abstract class ItemHeal extends GRPGItem implements IHandRenderer {

    private final ITextComponent[] description;
    private final Supplier<SoundEvent> useSound;
    private final Predicate<PlayerEntity> useCondition;
    private final Consumer<PlayerEntity> useAction;
    private final Supplier<IAnimation> useAnimation;
    private final int useTime;

    private ItemHeal(Builder builder) {
        super(builder.name, new Properties().tab(ModTabs.ITEM_TAB));
        description = builder.description;
        useSound = builder.useSound;
        useCondition = builder.useCondition;
        useAction = builder.useAction;
        Object temp = DistExecutor.callWhenOn(Dist.CLIENT, builder.useAnimation);
        useAnimation = () -> (IAnimation) temp;
        useTime = builder.useTime;
    }

    @OnlyIn(Dist.CLIENT)
    public IAnimation getUseAnimation(int ticks) {
        return useAnimation.get();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.addAll(Arrays.asList(description));
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
        if (useCondition.test(player)) {
            if (world.isClientSide) {
                player.playSound(useSound.get(), 1.0F, 1.0F);
                ClientSideManager.instance().processor().play(Animations.HEAL, getUseAnimation(stack.getUseDuration()));
            }
            player.startUsingItem(hand);
            return ActionResult.success(stack);
        }
        return ActionResult.pass(stack);
    }


    @Override
    public void releaseUsing(ItemStack stack, World world, LivingEntity entity, int timeLeft) {
        if (world.isClientSide)
            ClientSideManager.instance().processor().stop(Animations.HEAL);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity entity) {
        if (!world.isClientSide) {
            if (entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entity;
                PlayerDataFactory.get(player).ifPresent(data -> {
                    useAction.accept(player);
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

    public static class Builder {
        private final String name;
        private int useTime;
        private Supplier<SoundEvent> useSound;
        private Predicate<PlayerEntity> useCondition = playerEntity -> true;
        private Consumer<PlayerEntity> useAction = playerEntity -> {
        };
        private ITextComponent[] description;
        private Supplier<Callable<IAnimation>> useAnimation;

        private Builder(String name) {
            this.name = name;
        }

        public Builder defineSound(Supplier<SoundEvent> useSound) {
            this.useSound = useSound;
            return this;
        }

        public Builder canUse(Predicate<PlayerEntity> useCondition) {
            this.useCondition = useCondition;
            return this;
        }

        public Builder onUsed(Consumer<PlayerEntity> useAction) {
            this.useAction = useAction;
            return this;
        }

        public Builder animate(int useTime, Function<Integer, Supplier<Callable<IAnimation>>> animation) {
            this.useTime = useTime;
            this.useAnimation = animation.apply(useTime);
            return this;
        }

        public Builder describe(ITextComponent... lines) {
            this.description = lines;
            return this;
        }

        public Builder describe(String... lines) {
            return describe(convert(lines, StringTextComponent::new, StringTextComponent[]::new));
        }

        public Builder translate(String... keys) {
            return describe(convert(keys, TranslationTextComponent::new, TranslationTextComponent[]::new));
        }

        private <T extends ITextComponent> T[] convert(String[] array, Function<String, T> mapper, IntFunction<T[]> arrayFactory) {
            return Arrays.stream(array).map(mapper).toArray(arrayFactory);
        }
    }
}
