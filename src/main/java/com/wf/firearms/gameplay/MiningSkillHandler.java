package com.wf.firearms.gameplay;

import com.wf.firearms.GunsRpg;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

/** mother_lode / grave_digger / heavy_pickaxe / sharp_axe / lumberjack / acrobatics 技能效果。 */
@Mod.EventBusSubscriber(modid = GunsRpg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class MiningSkillHandler {
    private MiningSkillHandler() {}

    @SubscribeEvent
    public static void onBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player == null || player.level().isClientSide()) {
            return;
        }
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }
        BlockState state = event.getState();
        BlockPos pos = event.getPos();
        if (state.is(BlockTags.LOGS)) {
            applyLumberjackDrops(player, pos, state);
        }
        if (state.is(Tags.Blocks.ORES) && MiningSkillService.motherLodeTier(player) > 0) {
            applyMotherLodeBonusDrops(player, level, pos, state);
        }
    }

    @SubscribeEvent
    public static void onFall(LivingFallEvent event) {
        if (!(event.getEntity() instanceof Player player) || player.level().isClientSide()) {
            return;
        }
        int tier = MiningSkillService.acrobaticsTier(player);
        if (tier <= 0) {
            return;
        }
        float reduce = tier * 0.12f;
        event.setDistance(Math.max(0, event.getDistance() * (1f - reduce)));
    }

    private static void applyLumberjackDrops(Player player, BlockPos pos, BlockState state) {
        if (MiningSkillService.lumberjackTier(player) <= 0) {
            return;
        }
        Item plank = plankForLog(state.getBlock());
        if (plank == null) {
            return;
        }
        RandomSource random = player.getRandom();
        if (random.nextFloat() < MiningSkillService.lumberjackPlankChance(player)) {
            dropAt(player, pos, new ItemStack(plank, 1));
        }
        if (random.nextFloat() < MiningSkillService.lumberjackStickChance(player)) {
            dropAt(player, pos, new ItemStack(Items.STICK, 2));
        }
    }

    private static void applyMotherLodeBonusDrops(
            Player player, ServerLevel level, BlockPos pos, BlockState state) {
        int mult = MiningSkillService.motherLodeDropMultiplier(player, player.getRandom());
        if (mult <= 1) {
            return;
        }
        ItemStack tool = player.getMainHandItem();
        List<ItemStack> drops = Block.getDrops(state, level, pos, null, player, tool);
        Item blockItem = state.getBlock().asItem();
        for (ItemStack stack : drops) {
            if (stack.isEmpty() || stack.getItem() == blockItem) {
                continue;
            }
            int bonus = stack.getCount() * (mult - 1);
            if (bonus > 0) {
                dropAt(player, pos, new ItemStack(stack.getItem(), bonus));
            }
        }
    }

    private static void dropAt(Player player, BlockPos pos, ItemStack stack) {
        if (!(player.level() instanceof ServerLevel level) || stack.isEmpty()) {
            return;
        }
        ItemEntity entity =
                new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
        level.addFreshEntity(entity);
    }

    private static Item plankForLog(Block logBlock) {
        ResourceLocation id = ForgeRegistries.BLOCKS.getKey(logBlock);
        if (id == null) {
            return null;
        }
        String path = id.getPath();
        if (path.endsWith("_log")) {
            path = path.substring(0, path.length() - 4) + "_planks";
        } else if (path.startsWith("stripped_") && path.endsWith("_log")) {
            path = path.substring("stripped_".length(), path.length() - 4) + "_planks";
        } else {
            return null;
        }
        return ForgeRegistries.ITEMS.getValue(ResourceLocation.fromNamespaceAndPath(id.getNamespace(), path));
    }
}
