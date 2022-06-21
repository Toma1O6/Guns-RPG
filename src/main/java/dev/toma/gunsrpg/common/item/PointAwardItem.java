package dev.toma.gunsrpg.common.item;

import dev.toma.gunsrpg.ModTabs;
import dev.toma.gunsrpg.api.common.data.DataFlags;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.IPointProvider;
import dev.toma.gunsrpg.common.capability.PlayerData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class PointAwardItem extends BaseItem {

    private final IPointProviderSource pointSource;

    public PointAwardItem(String name, IPointProviderSource pointSource) {
        super(name, new Properties().tab(ModTabs.ITEM_TAB));
        this.pointSource = pointSource;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!world.isClientSide) {
            PlayerData.get(player).ifPresent(data -> {
                IPointProvider provider = this.pointSource.getProvider(data);
                provider.awardPoints(1);
                data.sync(DataFlags.DATA | DataFlags.PERK);
                if (!player.isCreative())
                    stack.shrink(1);
                world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_LEVELUP, SoundCategory.MASTER, 0.75F, 1.0F);
                ((ServerPlayerEntity) player).sendMessage(new TranslationTextComponent("point.award.success"), ChatType.GAME_INFO, Util.NIL_UUID);
            });
        }
        return ActionResult.pass(stack);
    }

    public interface IPointProviderSource {
        IPointProvider getProvider(IPlayerData data);
    }
}
