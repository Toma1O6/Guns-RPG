package dev.toma.gunsrpg.common.item;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.ModTabs;
import dev.toma.gunsrpg.common.init.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class StashDetectorItem extends BaseItem {

    public static final ResourceLocation CHARGE_BATTERY_ANIMATION = GunsRPG.makeResource("stash_detector/change_batteries");

    public StashDetectorItem(String name) {
        super(name, new Properties().tab(ModTabs.ITEM_TAB).durability(200));
    }

    public static boolean isValidBatterySource(ItemStack stack) {
        return stack.getItem() == ModItems.BATTERY;
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        return super.use(level, player, hand);
    }
}
