package dev.toma.gunsrpg.common.item;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.ModTabs;
import dev.toma.gunsrpg.resource.startgear.StartGearManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class StarterGearItem extends BaseItem {

    public StarterGearItem(String name) {
        super(name, new Properties().stacksTo(1).tab(ModTabs.ITEM_TAB));
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!world.isClientSide) {
            StartGearManager manager = GunsRPG.getModLifecycle().getStartingGearManager();
            manager.giveStartingGear(player);
            if (!player.isCreative()) {
                stack.shrink(1);
            }
        }
        return ActionResult.pass(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag tooltipFlag) {
        tooltip.add(new TranslationTextComponent("item.gunsrpg.starter_gear.use"));
    }
}
