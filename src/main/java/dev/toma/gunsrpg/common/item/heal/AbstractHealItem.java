package dev.toma.gunsrpg.common.item.heal;

import dev.toma.gunsrpg.ModTabs;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.client.animation.ModAnimations;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.BaseItem;
import lib.toma.animations.AnimationEngine;
import lib.toma.animations.api.*;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;

public abstract class AbstractHealItem<T> extends BaseItem implements IAnimationEntry {

    private final int useTime;
    private final ITextComponent[] description;
    private final Predicate<T> useCondition;
    private final Consumer<T> useAction;
    private final ResourceLocation useAnimation;

    protected AbstractHealItem(HealBuilder<T, ?> builder) {
        this(builder, new Properties().stacksTo(10));
    }

    protected AbstractHealItem(HealBuilder<T, ?> builder, Properties properties) {
        super(builder.name, properties.tab(ModTabs.ITEM_TAB));
        useTime = builder.useTime;
        description = builder.description;
        useCondition = builder.useCondition;
        useAction = builder.useAction;
        useAnimation = builder.useAnimation;
    }

    public abstract T getTargetObject(World world, PlayerEntity user, IPlayerData data);

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
        LazyOptional<IPlayerData> optional = PlayerData.get(player);
        if (optional.isPresent()) {
            IPlayerData data = optional.orElse(null);
            T target = getTargetObject(level, player, data);
            CooldownTracker tracker = player.getCooldowns();
            if (useCondition.test(target) && !tracker.isOnCooldown(stack.getItem())) {
                if (level.isClientSide) {
                    AnimationEngine engine = AnimationEngine.get();
                    IAnimationLoader loader = engine.loader();
                    IAnimationPipeline pipeline = engine.pipeline();
                    IAnimation animation = constructAnimation(loader.getProvider(useAnimation), this.getAnimationLength());
                    pipeline.insert(ModAnimations.HEAL, animation);
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
            AnimationEngine.get().pipeline().remove(ModAnimations.HEAL);
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity entity) {
        if (!world.isClientSide) {
            if (entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entity;
                PlayerData.get(player).ifPresent(data -> {
                    T target = getTargetObject(world, player, data);
                    applyAction(target);
                    if (data.getSkillProvider().hasSkill(Skills.EFFICIENT_MEDS))
                        player.heal(4.0F);
                    if (!player.isCreative())
                        stack.shrink(1);
                });
                CooldownTracker tracker = player.getCooldowns();
                tracker.addCooldown(stack.getItem(), 30);
            }
        }
        return stack;
    }

    @Override
    public IRenderConfig left() {
        return RenderConfigs.HEAL_CONFIG;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.HEAL_CONFIG;
    }

    @Override
    public boolean disableVanillaAnimations() {
        return false;
    }

    public int getUseTime() {
        return useTime;
    }

    protected void applyAction(T target) {
        useAction.accept(target);
    }

    protected int getAnimationLength() {
        return useTime;
    }

    protected IAnimation constructAnimation(IKeyframeProvider provider, int length) {
        return new Animation(provider, length);
    }

    public static abstract class HealBuilder<T, H extends AbstractHealItem<T>> {

        protected final String name;
        private int useTime;
        private ITextComponent[] description = new ITextComponent[0];
        private Predicate<T> useCondition = t -> true;
        private Consumer<T> useAction;
        private ResourceLocation useAnimation;

        protected HealBuilder(String name) {
            this.name = name;
        }

        private static <T extends ITextComponent> T[] convert(String[] array, Function<String, T> mapper, IntFunction<T[]> arrayFactory) {
            return Arrays.stream(array).map(mapper).toArray(arrayFactory);
        }

        public abstract H build();

        public HealBuilder<T, H> canUse(Predicate<T> useCondition) {
            this.useCondition = useCondition;
            return this;
        }

        public HealBuilder<T, H> onUse(Consumer<T> useAction) {
            this.useAction = useAction;
            return this;
        }

        public HealBuilder<T, H> animate(int useTime, ResourceLocation animationPath) {
            this.useTime = useTime;
            this.useAnimation = animationPath;
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
