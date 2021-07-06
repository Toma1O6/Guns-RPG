package dev.toma.gunsrpg.common.item;

import dev.toma.gunsrpg.ModTabs;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class ItemSkillBook extends GRPGItem {

    public ItemSkillBook(String name) {
        super(name, new Properties().tab(ModTabs.ITEM_TAB));
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!world.isClientSide) {
            PlayerDataFactory.get(player).ifPresent(data -> {
                PlayerSkills skills = data.getSkills();
                skills.addSkillPoints(1);
                data.sync();
                if (!player.isCreative())
                    stack.shrink(1);
                player.playSound(SoundEvents.PLAYER_LEVELUP, 0.75F, 1.0F);
            });
        }
        return ActionResult.pass(stack);
    }
}
