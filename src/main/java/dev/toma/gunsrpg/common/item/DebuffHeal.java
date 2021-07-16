package dev.toma.gunsrpg.common.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.ModTabs;
import dev.toma.gunsrpg.client.animation.Animations;
import dev.toma.gunsrpg.client.animation.IAnimation;
import dev.toma.gunsrpg.client.animation.IHandRenderer;
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
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class DebuffHeal extends GRPGItem implements IHandRenderer {

    private final int useTime;
    private final Predicate<DebuffData> predicate;
    private final Consumer<DebuffData> debuffDataConsumer;
    private final ITextComponent desc;
    private final Supplier<SoundEvent> useSound;

    public DebuffHeal(String name, int useTime, Supplier<SoundEvent> supplier, String desc, Predicate<DebuffData> predicate, Consumer<DebuffData> debuffDataConsumer) {
        super(name, new Properties().tab(ModTabs.ITEM_TAB));
        this.useTime = useTime;
        this.useSound = supplier;
        this.predicate = predicate;
        this.debuffDataConsumer = debuffDataConsumer;
        this.desc = new StringTextComponent(desc);
    }

    @OnlyIn(Dist.CLIENT)
    public IAnimation getAnimation(ItemStack stack) {
        return new Animations.Pills(this.getUseDuration(stack));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void transformRightArm(MatrixStack matrix) {
        matrix.mulPose(Vector3f.XP.rotationDegrees(-80.0F));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void transformLeftArm(MatrixStack matrix) {
        matrix.mulPose(Vector3f.XP.rotationDegrees(-80.0F));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(desc);
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
        PlayerDataFactory.get(player).ifPresent(data -> {
            if (predicate.test(data.getDebuffData())) {
                if (world.isClientSide) {
                    player.playSound(useSound.get(), 1.0F, 1.0F);
                    ClientSideManager.runOnClient(() -> () -> ClientSideManager.instance().processor().play(Animations.HEAL, getAnimation(stack)));
                }
                player.startUsingItem(hand);
            }
        });
        return ActionResult.pass(stack);
    }

    @Override
    public void releaseUsing(ItemStack p_77615_1_, World world, LivingEntity p_77615_3_, int p_77615_4_) {
        if (world.isClientSide)
            ClientSideManager.runOnClient(() -> () -> ClientSideManager.instance().processor().stop(Animations.HEAL));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity entity) {
        if (!world.isClientSide) {
            if (entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entity;
                PlayerDataFactory.get(player).ifPresent(data -> {
                    debuffDataConsumer.accept(data.getDebuffData());
                    if (data.getSkills().hasSkill(Skills.EFFICIENT_MEDS))
                        entity.heal(4.0F);
                    data.sync();
                    if (!player.isCreative())
                        stack.shrink(1);
                });
            }
        }
        return stack;
    }
}
