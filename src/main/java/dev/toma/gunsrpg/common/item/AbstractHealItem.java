package dev.toma.gunsrpg.common.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.ModTabs;
import dev.toma.gunsrpg.client.animation.Animations;
import dev.toma.gunsrpg.client.animation.IAnimation;
import dev.toma.gunsrpg.client.animation.IHandRenderer;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.DebuffData;
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
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.DistExecutor;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.*;

public abstract class AbstractHealItem<T> extends GRPGItem implements IHandRenderer {

    private final int useTime;
    private final ITextComponent[] description;
    private final Supplier<SoundEvent> useSound;
    private final Predicate<T> useCondition;
    private final Consumer<T> useAction;
    private final Supplier<IAnimation> useAnimation;

    protected AbstractHealItem(HealBuilder<T, ?> builder) {
        super(builder.name, new Properties().tab(ModTabs.ITEM_TAB));
        useTime = builder.useTime;
        description = builder.description;
        useSound = builder.useSound;
        useCondition = builder.useCondition;
        useAction = builder.useAction;
        Object temp = DistExecutor.callWhenOn(Dist.CLIENT, builder.useAnimation);
        useAnimation = () -> (IAnimation) temp;
    }

    public static HealBuilder<PlayerEntity, PlayerHealItem> definePlayerHeal(String name) {
        return PlayerHealItem.define(name);
    }

    public static HealBuilder<DebuffData, DebuffHealItem> defineDebuffHeal(String name) {
        return DebuffHealItem.define(name);
    }

    public abstract T getTargetObject(World world, PlayerEntity user, PlayerData data);

    @Override
    public void appendHoverText(ItemStack stack, World level, List<ITextComponent> list, ITooltipFlag flag) {
        list.addAll(Arrays.asList(description));
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return useTime;
    }

    @Override
    public UseAction getUseAnimation(ItemStack p_77661_1_) {
        return UseAction.NONE;
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        LazyOptional<PlayerData> optional = PlayerDataFactory.get(player);
        if (optional.isPresent()) {
            PlayerData data = optional.orElse(null);
            T target = getTargetObject(level, player, data);
            if (useCondition.test(target)) {
                if (level.isClientSide) {
                    player.playSound(useSound.get(), 1.0F, 1.0F);
                    DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> ClientSideManager.instance().processor().play(Animations.HEAL, useAnimation.get()));
                }
                player.startUsingItem(hand);
                return ActionResult.pass(stack);
            }
        }
        return ActionResult.fail(stack);
    }

    @Override
    public void releaseUsing(ItemStack p_77615_1_, World p_77615_2_, LivingEntity p_77615_3_, int p_77615_4_) {
        if (p_77615_2_.isClientSide) {
            DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> ClientSideManager.instance().processor().stop(Animations.HEAL));
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity entity) {
        if (!world.isClientSide) {
            if (entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entity;
                PlayerDataFactory.get(player).ifPresent(data -> {
                    T target = getTargetObject(world, player, data);
                    useAction.accept(target);
                    if (data.getSkills().hasSkill(Skills.EFFICIENT_MEDS))
                        player.heal(4.0F);
                    if (!player.isCreative())
                        stack.shrink(1);
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

    public static abstract class HealBuilder<T, H extends AbstractHealItem<T>> {

        protected final String name;
        private int useTime;
        private ITextComponent[] description = new ITextComponent[0];
        private Supplier<SoundEvent> useSound;
        private Predicate<T> useCondition = t -> true;
        private Consumer<T> useAction;
        private Supplier<Callable<IAnimation>> useAnimation;

        protected HealBuilder(String name) {
            this.name = name;
        }

        private static <T extends ITextComponent> T[] convert(String[] array, Function<String, T> mapper, IntFunction<T[]> arrayFactory) {
            return Arrays.stream(array).map(mapper).toArray(arrayFactory);
        }

        public abstract H build();

        public HealBuilder<T, H> defineSound(Supplier<SoundEvent> useSound) {
            this.useSound = useSound;
            return this;
        }

        public HealBuilder<T, H> canUse(Predicate<T> useCondition) {
            this.useCondition = useCondition;
            return this;
        }

        public HealBuilder<T, H> onUse(Consumer<T> useAction) {
            this.useAction = useAction;
            return this;
        }

        public HealBuilder<T, H> animate(int useTime, Function<Integer, Supplier<Callable<IAnimation>>> useAnimationFactory) {
            this.useTime = useTime;
            this.useAnimation = useAnimationFactory.apply(useTime);
            return this;
        }

        public HealBuilder<T, H> describe(ITextComponent... lines) {
            this.description = lines;
            return this;
        }

        public HealBuilder<T, H> describe(String... lines) {
            return describe(convert(lines, StringTextComponent::new, StringTextComponent[]::new));
        }

        public HealBuilder<T, H> translate(String... keys) {
            return describe(convert(keys, TranslationTextComponent::new, TranslationTextComponent[]::new));
        }
    }
}
