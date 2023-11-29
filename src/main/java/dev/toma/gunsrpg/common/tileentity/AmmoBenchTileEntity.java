package dev.toma.gunsrpg.common.tileentity;

import dev.toma.gunsrpg.api.common.data.ISkillProvider;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.ModBlockEntities;
import dev.toma.gunsrpg.common.init.ModRecipeTypes;
import dev.toma.gunsrpg.resource.ammobench.AmmoBenchOutputCount;
import dev.toma.gunsrpg.resource.ammobench.AmmoBenchOutputCountType;
import dev.toma.gunsrpg.resource.ammobench.AmmoBenchOutputModifier;
import dev.toma.gunsrpg.resource.ammobench.AmmoBenchRecipe;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AmmoBenchTileEntity extends VanillaInventoryTileEntity implements ITickableTileEntity {

    public static final int[] SLOT_OUTPUTS = { 6, 7, 8, 9, 10, 11 };
    public static final int[] SLOT_INPUTS = { 0, 1, 2, 3, 4, 5 };

    private boolean crafting;
    private int recipeCraftTime;
    private int timeCrafting;
    private List<AmmoBenchRecipe> recipeSelection = new ArrayList<>();
    private int selectedRecipeIndex;
    private CraftingRecipeImage image;

    public AmmoBenchTileEntity() {
        super(ModBlockEntities.AMMO_BENCH.get());
    }

    @Override
    public void tick() {
        if (crafting) {
            AmmoBenchRecipe recipe = getActiveRecipe();
            if (image == null || recipe == null) {
                interruptCrafting();
                return;
            }

            if (--timeCrafting <= 0) {
                ItemStack[] results = image.assembleItems();
                if (!ModUtils.canFitItems(results, this, SLOT_OUTPUTS)) {
                    setRecipeIndex(0);
                } else {
                    if (!level.isClientSide) {
                        ModUtils.insertItems(results, this, SLOT_OUTPUTS);
                        recipe.getInputs().forEach(input -> input.consume(this, SLOT_INPUTS));
                        onSlotChanged();
                        if (crafting) {
                            timeCrafting = recipeCraftTime;
                        }
                    }
                }
            }
        }
    }

    public boolean isCrafting() {
        return crafting;
    }

    public void startCrafting(ServerPlayerEntity player) {
        AmmoBenchRecipe recipe = getActiveRecipe();
        if (recipe == null) {
            return;
        }
        PlayerData.get(player).ifPresent(data -> {
            image = getImage(data.getSkillProvider());
            ItemStack[] results = image.assembleItems();
            if (!ModUtils.canFitItems(results, this, SLOT_OUTPUTS) || !recipe.canPlayerCraft(player)) {
                return;
            }
            this.crafting = true;
            this.timeCrafting = recipe.getCraftingTimer();
            this.recipeCraftTime = recipe.getCraftingTimer();
        });
        player.server.getPlayerList().broadcastAll(getUpdatePacket());
        setChanged();
    }

    public void interruptCrafting() {
        setRecipeIndex(0);
    }

    @Override
    public IItemHandlerModifiable createInventory() {
        return new ItemStackHandler(12);
    }

    @Override
    public void write(CompoundNBT nbt) {
        nbt.putBoolean("crafting", crafting);
        nbt.putInt("time", timeCrafting);
        nbt.putInt("totalCraftTime", recipeCraftTime);
        nbt.putInt("selectedRecipeIndex", selectedRecipeIndex);
        if (image != null) {
            nbt.put("craftingImage", image.serialize());
        }
        ListNBT selection = new ListNBT();
        recipeSelection.forEach(recipe -> selection.add(StringNBT.valueOf(recipe.getId().toString())));
        nbt.put("selection", selection);
    }

    @Override
    public void read(CompoundNBT nbt) {
        crafting = nbt.getBoolean("crafting");
        timeCrafting = nbt.getInt("time");
        recipeCraftTime = nbt.getInt("totalCraftTime");
        selectedRecipeIndex = nbt.getInt("selectedRecipeIndex");
        image = null;
        if (nbt.contains("craftingImage")) {
            image = CraftingRecipeImage.deserialize(nbt.getList("craftingImage", Constants.NBT.TAG_COMPOUND));
        }

        ListNBT selection = nbt.getList("selection", Constants.NBT.TAG_STRING);
        recipeSelection.clear();
        if (level != null) {
            RecipeManager manager = level.getRecipeManager();
            Map<ResourceLocation, AmmoBenchRecipe> recipeMap = manager.getAllRecipesFor(ModRecipeTypes.AMMO_BENCH_RECIPE_TYPE).stream()
                            .collect(Collectors.toMap(AmmoBenchRecipe::getId, Function.identity()));
            selection.forEach(inbt -> {
                ResourceLocation recipeId = new ResourceLocation(inbt.getAsString());
                AmmoBenchRecipe recipe = recipeMap.get(recipeId);
                if (recipe != null) {
                    recipeSelection.add(recipe);
                }
            });
        }
    }

    public AmmoBenchRecipe getActiveRecipe() {
        if (recipeSelection.isEmpty() || selectedRecipeIndex >= recipeSelection.size()) {
            return null;
        }
        return recipeSelection.get(selectedRecipeIndex);
    }

    public boolean canCraftCurrentRecipe() {
        AmmoBenchRecipe recipe = getActiveRecipe();
        if (recipe != null) {
            return recipe.matches(this, level);
        }
        return false;
    }

    public boolean canSelectNextRecipe() {
        return !recipeSelection.isEmpty() && (selectedRecipeIndex + 1) < recipeSelection.size();
    }

    public boolean canSelectPreviousRecipe() {
        return !recipeSelection.isEmpty() && selectedRecipeIndex > 0;
    }

    public void onSlotChanged() {
        if (level == null)
            return;
        RecipeManager manager = level.getRecipeManager();
        List<AmmoBenchRecipe> recipeList = manager.getAllRecipesFor(ModRecipeTypes.AMMO_BENCH_RECIPE_TYPE);
        this.recipeSelection = recipeList.stream()
                .filter(recipe -> recipe.matches(this, level))
                .collect(Collectors.toList());
        if (selectedRecipeIndex >= recipeSelection.size()) {
            setRecipeIndex(0);
        }
        setChanged();
        if (!level.isClientSide) {
            level.getServer().getPlayerList().broadcastAll(getUpdatePacket());
        }
    }

    public int getSelectedRecipeIndex() {
        return selectedRecipeIndex;
    }

    public void setRecipeIndex(int index) {
        this.selectedRecipeIndex = index;
        this.recipeCraftTime = 0;
        this.crafting = false;

        AmmoBenchRecipe recipe = getActiveRecipe();
        if (recipe != null) {
            this.recipeCraftTime = recipe.getCraftingTimer();
        }
        setChanged();
    }

    public float getCraftingProgress() {
        return crafting ? 1.0F - (timeCrafting / (float) recipeCraftTime) : 0.0F;
    }

    private CraftingRecipeImage getImage(ISkillProvider provider) {
        AmmoBenchRecipe recipe = getActiveRecipe();
        SingleItemImage[] outputImages = new SingleItemImage[recipe.getOutputs().size()];
        for (int i = 0; i < outputImages.length; i++) {
            AmmoBenchRecipe.AmmoBenchOutput output = recipe.getOutputs().get(i);
            ItemStack stack = output.getItemStack();
            AmmoBenchOutputCount[] modifiers = output.getOutputModifiers().stream()
                    .filter(modifier -> modifier.canApply(provider))
                    .map(AmmoBenchOutputModifier::getCountFunction)
                    .toArray(AmmoBenchOutputCount[]::new);
            outputImages[i] = new SingleItemImage(stack, modifiers);
        }
        return new CraftingRecipeImage(outputImages);
    }

    private static final class CraftingRecipeImage {

        private final SingleItemImage[] images;

        private CraftingRecipeImage(SingleItemImage[] images) {
            this.images = images;
        }

        private ItemStack[] assembleItems() {
            return Arrays.stream(images).map(SingleItemImage::toItemStack).toArray(ItemStack[]::new);
        }

        private ListNBT serialize() {
            ListNBT listNBT = new ListNBT();
            for (SingleItemImage image : images) {
                listNBT.add(image.serialize());
            }
            return listNBT;
        }

        private static CraftingRecipeImage deserialize(ListNBT nbt) {
            SingleItemImage[] images = new SingleItemImage[nbt.size()];
            for (int i = 0; i < nbt.size(); i++) {
                images[i] = SingleItemImage.deserialize(nbt.getCompound(i));
            }
            return new CraftingRecipeImage(images);
        }
    }

    private static final class SingleItemImage {

        private final ItemStack stack;
        private final AmmoBenchOutputCount[] modifiers;

        SingleItemImage(ItemStack stack, AmmoBenchOutputCount[] modifiers) {
            this.stack = stack;
            this.modifiers = modifiers;
        }

        private ItemStack toItemStack() {
            ItemStack itemStack = stack.copy();
            for (AmmoBenchOutputCount modifier : modifiers) {
                int count = modifier.getCount(itemStack.getCount());
                itemStack.setCount(Math.min(itemStack.getMaxStackSize(), count));
            }
            return itemStack;
        }

        private CompoundNBT serialize() {
            CompoundNBT nbt = new CompoundNBT();
            nbt.put("itemStack", stack.serializeNBT());
            ListNBT list = new ListNBT();
            for (AmmoBenchOutputCount count : modifiers) {
                list.add(AmmoBenchOutputCountType.toNbt(count));
            }
            nbt.put("modifiers", list);
            return nbt;
        }

        private static SingleItemImage deserialize(CompoundNBT nbt) {
            ItemStack itemStack = ItemStack.of(nbt.getCompound("itemStack"));
            ListNBT modifiers = nbt.getList("modifiers", Constants.NBT.TAG_COMPOUND);
            AmmoBenchOutputCount[] counts = new AmmoBenchOutputCount[modifiers.size()];
            for (int i = 0; i < modifiers.size(); i++) {
                counts[i] = AmmoBenchOutputCountType.fromNbt(modifiers.getCompound(i));
            }
            return new SingleItemImage(itemStack, counts);
        }
    }
}
