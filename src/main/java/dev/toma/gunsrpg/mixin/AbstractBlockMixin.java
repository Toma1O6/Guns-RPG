package dev.toma.gunsrpg.mixin;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.ISkillProvider;
import dev.toma.gunsrpg.common.block.ICustomizableDrops;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.skills.MotherlodeSkill;
import dev.toma.gunsrpg.util.Lifecycle;
import dev.toma.gunsrpg.util.SkillUtil;
import dev.toma.gunsrpg.util.object.Pair;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.loot.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.Tags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(AbstractBlock.class)
public abstract class AbstractBlockMixin extends net.minecraftforge.registries.ForgeRegistryEntry<Block> {

    @Shadow public abstract ResourceLocation getLootTable();

    @Inject(method = "getDrops", at = @At("HEAD"), cancellable = true)
    public void gunsrpg_modifyBlockDrops(BlockState state, LootContext.Builder ctxBuilder, CallbackInfoReturnable<List<ItemStack>> ci) {
        ResourceLocation location = this.getLootTable();
        if (location == LootTables.EMPTY) {
            ci.setReturnValue(Collections.emptyList());
        } else {
            LootContext lootContext = ctxBuilder.withParameter(LootParameters.BLOCK_STATE, state).create(LootParameterSets.BLOCK);
            ServerWorld serverWorld = lootContext.getLevel();
            LootTable lootTable = serverWorld.getServer().getLootTables().get(location);
            ci.setReturnValue(this.actuallyModify(lootContext, serverWorld, state, lootTable));
        }
    }

    private List<ItemStack> actuallyModify(LootContext context, ServerWorld world, BlockState state, LootTable table) {
        List<ItemStack> drops = table.getRandomItems(context);
        Entity entity = context.getParamOrNull(LootParameters.THIS_ENTITY);
        if (state == null || !(entity instanceof PlayerEntity)) {
            return drops;
        }
        Block block = state.getBlock();
        if (block instanceof ICustomizableDrops) {
            return ((ICustomizableDrops) block).getCustomDrops(table, context);
        }
        PlayerEntity player = (PlayerEntity) entity;
        IPlayerData data = PlayerData.get(player).orElse(null);
        if (data == null) {
            return drops;
        }
        ISkillProvider skillProvider = data.getSkillProvider();
        if (block.is(BlockTags.LOGS)) {
            this.applyLogDrops(drops, block, player, world, skillProvider);
        } else if (block.is(Tags.Blocks.ORES)) {
            this.applyOreDrops(drops, block, player, data);
        }
        return drops;
    }

    private void applyLogDrops(List<ItemStack> drops, Block block, PlayerEntity player, ServerWorld world, ISkillProvider skillProvider) {
        MinecraftServer server = world.getServer();
        if (server == null || !skillProvider.hasSkill(Skills.LUMBERJACK_I)) {
            return;
        }
        List<ICraftingRecipe> recipes = server.getRecipeManager().getAllRecipesFor(IRecipeType.CRAFTING);
        for (ICraftingRecipe recipe : recipes) {
            NonNullList<Ingredient> ingredients = recipe.getIngredients();
            if (ingredients.size() == 1) {
                Ingredient ingredient = ingredients.get(0);
                if (ingredient.test(new ItemStack(block))) {
                    ItemStack result = recipe.getResultItem();
                    Pair<Float, Float> chances = SkillUtil.getTopHierarchySkill(Skills.LUMBERJACK_I, skillProvider).getDropChances();
                    Random random = player.getRandom();
                    if (random.nextFloat() < chances.getLeft()) {
                        drops.add(new ItemStack(result.getItem(), 1));
                    }
                    if (random.nextFloat() < chances.getRight()) {
                        drops.add(new ItemStack(Items.STICK, 2));
                    }
                    break;
                }
            }
        }
    }

    private void applyOreDrops(List<ItemStack> drops, Block block, PlayerEntity player, IPlayerData data) {
        ISkillProvider provider = data.getSkillProvider();
        MotherlodeSkill skill = SkillUtil.getTopHierarchySkill(Skills.MOTHER_LODE_I, provider);
        Random random = player.getRandom();
        int multiplier = 1;
        if (skill != null) {
            multiplier = skill.getDropMultiplier(random, data);
        }
        Iterator<ItemStack> iterator = drops.iterator();
        List<ItemStack> extraDrops = new ArrayList<>();
        Lifecycle lifecycle = GunsRPG.getModLifecycle();
        while (iterator.hasNext()) {
            ItemStack stack = iterator.next();
            Item dropReplacement = lifecycle.getOreDropReplacement(stack.getItem());
            if (dropReplacement != null) {
                List<ItemStack> extras = this.multiply(new ItemStack(dropReplacement, stack.getCount()), multiplier);
                extraDrops.addAll(extras);
                iterator.remove();
            } else if (stack.getItem() != block.asItem()) {
                List<ItemStack> extras = this.multiply(new ItemStack(stack.getItem(), stack.getCount()), multiplier);
                extraDrops.addAll(extras);
                iterator.remove();
            }
        }
        drops.addAll(extraDrops);
    }

    private List<ItemStack> multiply(ItemStack stack, int multiplier) {
        List<ItemStack> list = new ArrayList<>();
        int count = stack.getCount() * multiplier;
        while (count > 0) {
            int extract = Math.min(stack.getMaxStackSize(), count);
            count =- extract;
            list.add(new ItemStack(stack.getItem(), extract));
        }
        return list;
    }
}
