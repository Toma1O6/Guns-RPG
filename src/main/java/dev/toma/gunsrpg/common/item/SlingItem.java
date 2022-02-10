package dev.toma.gunsrpg.common.item;

import dev.toma.gunsrpg.ModTabs;
import dev.toma.gunsrpg.common.entity.projectile.Pebble;
import dev.toma.gunsrpg.common.init.ModEntities;
import dev.toma.gunsrpg.common.init.ModItems;
import dev.toma.gunsrpg.util.locate.ammo.ItemLocator;
import lib.toma.animations.Easings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

import java.util.IdentityHashMap;
import java.util.Map;

public class SlingItem extends BaseItem {

    public SlingItem(String name) {
        super(name, new Properties().tab(ModTabs.ITEM_TAB).durability(75));
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        boolean hasAmmo = player.isCreative() || ItemLocator.hasItem(player.inventory, AmmoRegistry::isValidAmmo);
        if (hasAmmo) {
            player.startUsingItem(hand);
            return ActionResult.consume(stack);
        }
        return ActionResult.fail(stack);
    }

    @Override
    public void releaseUsing(ItemStack stack, World world, LivingEntity entity, int timeLeft) {
        if (!(entity instanceof PlayerEntity)) {
            return;
        }
        PlayerEntity player = (PlayerEntity) entity;
        ItemStack ammoStack = ItemLocator.findFirst(player.inventory, AmmoRegistry::isValidAmmo);
        boolean hasAmmo = !ammoStack.isEmpty() || player.isCreative();

        int pullTime = getUseDuration(stack) - timeLeft;
        if (pullTime < 0 || !hasAmmo) {
            return;
        }

        float power = getPowerForTime(pullTime);
        if (power < 0.1) {
            return;
        }

        if (!world.isClientSide) {
            Pebble pebble = new Pebble(ModEntities.PEBBLE.get(), world, entity);
            int damage = AmmoRegistry.getDamage(ammoStack);
            if (damage == 0 && player.isCreative()) {
                damage = 4;
            }
            pebble.setup(damage, power * 2.5F, 0);
            pebble.fire(player.xRot, player.yRot, 0.5F);
            world.addFreshEntity(pebble);
            if (!player.isCreative()) {
                stack.hurtAndBreak(1, player, playerArg -> playerArg.broadcastBreakEvent(player.getUsedItemHand()));
            }
        }

        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + power * 0.5F);
        if (!player.isCreative()) {
            ammoStack.shrink(1);
        }
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.BOW;
    }

    private static float getPowerForTime(int time) {
        float rawPower = time / 15.0F;
        return rawPower >= 1.0F ? 1.0F : Easings.EASE_OUT_QUAD.ease(rawPower);
    }

    public static void initAmmoRegistry() {
        AmmoRegistry.register(ModItems.SMALL_STONE, 2);
        AmmoRegistry.register(Items.IRON_NUGGET, 3);
        AmmoRegistry.register(Items.GOLD_NUGGET, 4);
    }

    public static final class AmmoRegistry {

        private static final Map<Item, Integer> DAMAGE_TABLE = new IdentityHashMap<>();

        public static void register(Item item, int value) {
            if (value <= 0) {
                throw new IllegalArgumentException("Damage must be bigger than 0");
            }
            DAMAGE_TABLE.put(item, value);
        }

        public static int getDamage(ItemStack stack) {
            return DAMAGE_TABLE.getOrDefault(stack.getItem(), 0);
        }

        public static boolean isValidAmmo(ItemStack stack) {
            return DAMAGE_TABLE.containsKey(stack.getItem());
        }
    }
}
