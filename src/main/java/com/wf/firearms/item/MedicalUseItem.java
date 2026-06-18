package com.wf.firearms.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/** 读条型医疗品（Finish 由 {@link com.wf.firearms.debuff.DebuffMedicalHandler} 处理 debuff）。 */
public class MedicalUseItem extends Item {
    private final int useTicks;

    public MedicalUseItem(Item.Properties props, int useTicks) {
        super(props);
        this.useTicks = useTicks;
    }

    @Override
    public void appendHoverText(
            ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(this);
        if (id != null) {
            String key = "item." + id.getNamespace() + "." + id.getPath() + ".tooltip";
            tooltip.add(Component.translatable(key).withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide && entity instanceof Player player && !player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        return stack;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return useTicks;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

}
