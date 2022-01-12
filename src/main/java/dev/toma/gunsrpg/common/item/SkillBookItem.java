package dev.toma.gunsrpg.common.item;

import dev.toma.gunsrpg.ModTabs;
import dev.toma.gunsrpg.api.common.data.DataFlags;
import dev.toma.gunsrpg.api.common.data.IProgressData;
import dev.toma.gunsrpg.common.capability.PlayerData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class SkillBookItem extends BaseItem {

    public SkillBookItem(String name) {
        super(name, new Properties().tab(ModTabs.ITEM_TAB));
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!world.isClientSide) {
            PlayerData.get(player).ifPresent(data -> {
                IProgressData levelData = data.getProgressData();
                levelData.awardPoints(1);
                data.sync(DataFlags.DATA);
                if (!player.isCreative())
                    stack.shrink(1);
                player.playSound(SoundEvents.PLAYER_LEVELUP, 0.75F, 1.0F);
            });
        }
        return ActionResult.pass(stack);
    }
}
