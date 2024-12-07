package dev.toma.gunsrpg.util;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.ISkillProvider;
import dev.toma.gunsrpg.api.common.skill.IDescriptionProvider;
import dev.toma.gunsrpg.api.common.skill.ISkill;
import dev.toma.gunsrpg.api.common.skill.ISkillHierarchy;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.block.ICustomizableDrops;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.skills.MotherlodeSkill;
import dev.toma.gunsrpg.common.skills.core.DisplayData;
import dev.toma.gunsrpg.common.skills.core.DisplayType;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.world.WorldConfiguration;
import dev.toma.gunsrpg.util.object.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.Tags;

import java.util.*;

public class SkillUtil {

    public static final float NO_AMMO_CONSUME_CHANCE = 0.1F;
    public static final float EXTENDED_BARREL_VELOCITY = 1.75F;
    public static final float CHOKE_SPREAD = 0.7F;
    public static final float EVERY_BULLET_COUNTS_DAMAGE = 3.0F;
    public static final float COLD_BLOODED_DAMAGE = 1.3F;
    public static final int HUNTER_LOOTING_LEVEL = 3;

    public static <S extends ISkill> S getTopHierarchySkill(SkillType<S> head, ISkillProvider provider) {
        S value = provider.getSkill(head);
        if (value == null)
            return null;
        if (head.isDisabled()) {
            return null;
        }
        ISkillHierarchy<S> hierarchy = head.getHierarchy();
        SkillType<S> override = hierarchy.getOverride();
        if (override != null && provider.hasSkill(override)) {
            return getTopHierarchySkill(override, provider);
        } else {
            return value;
        }
    }

    public static DisplayData getDefaultDisplayData(SkillType<?> type) {
        ResourceLocation name = type.getRegistryName();
        String namespace = name.getNamespace();
        String path = name.getPath();
        return DisplayData.create(
                DisplayType.ICON,
                new ResourceLocation(namespace, "textures/icons/" + path + ".png")
        );
    }

    public static ResourceLocation moddedIcon(String iconPath) {
        return GunsRPG.makeResource("textures/icons/" + iconPath + ".png");
    }

    public static void applySoulTakerSkill(LivingEntity livingEntity) {
        livingEntity.addEffect(new EffectInstance(Effects.ABSORPTION, 200, 0, false, false));
    }

    public static ITextComponent getMissingSkillText(SkillType<?> type) {
        return new TranslationTextComponent("text.skill.missing", type.getTitle().getString());
    }

    public static void heal(PlayerEntity player, float amount) {
        PlayerData.get(player).ifPresent(data -> {
            IAttributeProvider provider = data.getAttributes();
            float value = provider.getAttribute(Attribs.HEAL_BOOST).floatValue();
            player.heal(amount + value);
        });
    }

    public static List<ItemStack> applyOreDropChanges(LootContext context, ServerWorld world, BlockState state, LootTable table) {
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
            applyLogDrops(drops, block, player, world, skillProvider);
        } else if (block.is(Tags.Blocks.ORES)) {
            applyOreDrops(drops, block, player, data);
        }
        return drops;
    }

    private static void applyLogDrops(List<ItemStack> drops, Block block, PlayerEntity player, ServerWorld world, ISkillProvider skillProvider) {
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

    private static void applyOreDrops(List<ItemStack> drops, Block block, PlayerEntity player, IPlayerData data) {
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
        WorldConfiguration configuration = GunsRPG.config.world;
        while (iterator.hasNext()) {
            ItemStack stack = iterator.next();
            if (stack.getItem() != block.asItem()) {
                List<ItemStack> extras = multiply(new ItemStack(stack.getItem(), stack.getCount()), multiplier);
                extraDrops.addAll(extras);
                iterator.remove();
            }
        }
        drops.addAll(extraDrops);
    }

    private static List<ItemStack> multiply(ItemStack stack, int multiplier) {
        List<ItemStack> list = new ArrayList<>();
        int count = stack.getCount() * multiplier;
        while (count > 0) {
            int extract = Math.min(stack.getMaxStackSize(), count);
            count =- extract;
            list.add(new ItemStack(stack.getItem(), extract));
        }
        return list;
    }

    public static class Localizations {

        public static String convertToLocalizationKey(ResourceLocation location) {
            return location.toString().replaceAll(":", ".");
        }

        public static ITextComponent makeReadable(ResourceLocation location) {
            String[] str = location.toString().split("[.:]");
            String in = str[str.length - 1];
            String[] words = Arrays.stream(in.split("_")).map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase()).toArray(String[]::new);
            return new StringTextComponent(String.join(" ", words));
        }

        public static ITextComponent getDefaultTitle(SkillType<?> type) {
            return new TranslationTextComponent("skill." + ModUtils.convertToLocalization(type.getRegistryName()) + ".title");
        }

        public static ITextComponent[] getDefaultDescription(int lines, SkillType<?> type) {
            ISkill skill = type.getDataInstance();
            if (skill instanceof IDescriptionProvider) {
                IDescriptionProvider provider = (IDescriptionProvider) skill;
                return provider.supplyDescription(lines);
            }
            ITextComponent[] description = new ITextComponent[lines];
            prepareEmptyDescriptionLines(type, lines, description);
            return description;
        }

        public static ITextComponent[] generateAndMerge(int lines, SkillType<?> type, ITextComponent[] components) {
            ITextComponent[] description = new ITextComponent[lines + components.length];
            prepareEmptyDescriptionLines(type, lines, description);
            System.arraycopy(components, 0, description, lines, components.length);
            return description;
        }

        public static ITextComponent[] generateSimpleDescription(int lines, SkillType<?> type) {
            ITextComponent[] components = new ITextComponent[lines];
            prepareEmptyDescriptionLines(type, lines, components);
            return components;
        }

        public static void prepareEmptyDescriptionLines(SkillType<?> type, int lines, ITextComponent[] description) {
            String rawString = String.format("skill.%s.description.", ModUtils.convertToLocalization(type.getRegistryName()));
            for (int i = 0; i < lines; i++) {
                description[i] = new TranslationTextComponent(rawString + i);
            }
        }

        public static ITextComponent translation(SkillType<?> type, String name, Object... data) {
            return new TranslationTextComponent("skill." + ModUtils.convertToLocalization(type.getRegistryName()) + ".description." + name, data);
        }
    }
}
