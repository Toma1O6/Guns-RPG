package com.wf.firearms.item;

import com.wf.firearms.client.render.GunsRpgItemRenderer;
import com.wf.firearms.combat.FirearmRegistry;
import com.wf.firearms.combat.FirearmSpec;
import com.wf.firearms.combat.FirearmStackState;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import javax.annotation.Nullable;
import java.util.List;

/** 右键单击切换瞄准（不再长按望远镜动画）。 */
public class FirearmItem extends Item {
    private final String weaponKey;

    public FirearmItem(String weaponKey, Properties properties) {
        super(properties);
        this.weaponKey = weaponKey;
    }

    public String getWeaponKey() {
        return weaponKey;
    }

    public FirearmSpec getSpec() {
        return FirearmRegistry.getOrDefault(weaponKey);
    }

    /** 创造栏平面图标 + 手持 BEWLR 3D（{@link com.wf.firearms.client.model.FirearmDisplayBakedModel} 分离二者）。 */
    @Override
    @OnlyIn(Dist.CLIENT)
    public void initializeClient(java.util.function.Consumer<IClientItemExtensions> consumer) {
        consumer.accept(
                new IClientItemExtensions() {
                    @Override
                    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                        return GunsRpgItemRenderer.getInstance();
                    }
                });
    }

    /** 持枪时左键不破坏方块（含创造模式）。 */
    @Override
    public boolean canAttackBlock(
            BlockState state, Level level, BlockPos pos, Player player) {
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        FirearmStackState.ensureInitialized(stack, getSpec());
        if (FirearmStackState.isBusy(stack)) {
            return InteractionResultHolder.pass(stack);
        }
        if (!level.isClientSide) {
            FirearmStackState.toggleAiming(stack);
        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {}

    @Override
    public int getUseDuration(ItemStack stack) {
        return 0;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    @Override
    public void appendHoverText(
            ItemStack stack,
            @Nullable Level level,
            List<Component> tooltip,
            TooltipFlag flag) {
        FirearmStackState.appendTooltip(stack, getSpec(), tooltip);
        tooltip.add(Component.translatable("gunsrpg.gun.controls_hint"));
    }
}
