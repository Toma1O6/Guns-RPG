package dev.toma.gunsrpg.common.tileentity;

import dev.toma.gunsrpg.common.init.ModBlockEntities;
import dev.toma.gunsrpg.common.init.ModRecipeTypes;
import dev.toma.gunsrpg.resource.cooking.CookingRecipe;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CookerTileEntity extends VanillaInventoryTileEntity implements IRecipeHolder, ITickableTileEntity, ISidedInventory {

    public static final byte SLOT_INPUT = 0;
    public static final byte SLOT_FUEL = 1;
    public static final byte SLOT_OUTPUT = 2;
    private final Object2IntOpenHashMap<ResourceLocation> usedRecipes = new Object2IntOpenHashMap<>();
    private int litTime;
    private int litDuration;
    private int cookingProgress;
    private int cookingTotalTime;

    public CookerTileEntity() {
        this(ModBlockEntities.COOKER.get());
    }

    protected CookerTileEntity(TileEntityType<? extends CookerTileEntity> type) {
        super(type);
    }

    public int getLitTime() {
        return litTime;
    }

    public int getLitDuration() {
        return litDuration;
    }

    public int getCookingProgress() {
        return cookingProgress;
    }

    public int getCookingTotalTime() {
        return cookingTotalTime;
    }

    @Override
    public IItemHandlerModifiable createInventory() {
        return new ItemStackHandler(3);
    }

    @Override
    public void tick() {
        boolean wasLit = isLit();
        boolean gotLit = false;
        if (isLit()) {
            --litTime;
        }
        ItemStack fuelStack = itemHandler.getStackInSlot(SLOT_FUEL);
        if (isLit() || !fuelStack.isEmpty() && !itemHandler.getStackInSlot(SLOT_FUEL).isEmpty()) {
            gotLit = runCookProcess(fuelStack);
        } else if (!isLit() && cookingProgress > 0) {
            cookingProgress = MathHelper.clamp(cookingProgress - 2, 0, cookingTotalTime);
        }

        if (!level.isClientSide) {
            if (wasLit != isLit()) {
                gotLit = true;
                updateBlockLitState(isLit());
            }
            if (gotLit) {
                setChanged();
            }
        }
    }

    public void awardExperienceFromRecipes(PlayerEntity player) {
        List<IRecipe<?>> recipes = getRecipesForAwardsWithExp(player.level, player.position());
        player.awardRecipes(recipes);
        usedRecipes.clear();
    }

    public List<IRecipe<?>> getRecipesForAwardsWithExp(World level, Vector3d position) {
        List<IRecipe<?>> list = new ArrayList<>();
        for (Object2IntMap.Entry<ResourceLocation> entry : usedRecipes.object2IntEntrySet()) {
            level.getRecipeManager().byKey(entry.getKey()).ifPresent(recipe -> {
                if (recipe instanceof CookingRecipe) {
                    list.add(recipe);
                    createExperience(level, position, entry.getIntValue(), (CookingRecipe) recipe);
                }
            });
        }
        return list;
    }

    @Override
    public void setRecipeUsed(@Nullable IRecipe<?> recipe) {
        if (recipe != null) {
            usedRecipes.addTo(recipe.getId(), 1);
        }
    }

    @Nullable
    @Override
    public IRecipe<?> getRecipeUsed() {
        return null;
    }

    @Override
    public void awardUsedRecipes(PlayerEntity p_201560_1_) {

    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        if (direction == Direction.DOWN) {
            return new int[] { SLOT_INPUT, SLOT_FUEL };
        } else return direction == Direction.UP ? new int[] { SLOT_OUTPUT } : new int[] { SLOT_FUEL };
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction direction) {
        return canPlaceItem(slot, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
        if (direction == Direction.DOWN && slot == SLOT_FUEL) {
            Item item = stack.getItem();
            return item == Items.WATER_BUCKET || item == Items.BUCKET;
        }
        return true;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (slot == SLOT_OUTPUT) {
            return false;
        } else if (slot != SLOT_FUEL) {
            return true;
        } else {
            return ForgeHooks.getBurnTime(stack, ModRecipeTypes.COOKING_RECIPE_TYPE) > 0;
        }
    }

    @Override
    public void setItem(int id, ItemStack stack) {
        ItemStack itemStack = itemHandler.getStackInSlot(id);
        boolean flag = !stack.isEmpty() && stack.sameItem(itemStack) && ItemStack.tagMatches(stack, itemStack);
        itemHandler.setStackInSlot(id, stack);
        if (stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }
        if (id == SLOT_INPUT && !flag) {
            cookingTotalTime = getItemCookTimeSlow();
            cookingProgress = 0;
            setChanged();
        }
    }

    @Override
    public void read(CompoundNBT nbt) {
        litTime = nbt.getInt("litTime");
        litDuration = getBurnDuration(itemHandler.getStackInSlot(SLOT_FUEL));
        cookingProgress = nbt.getInt("cookingProgress");
        cookingTotalTime = nbt.getInt("cookingTotalTime");
        CompoundNBT used = nbt.getCompound("usedRecipes");
        for (String key : used.getAllKeys()) {
            usedRecipes.put(new ResourceLocation(key), used.getInt(key));
        }
    }

    @Override
    public void write(CompoundNBT nbt) {
        nbt.putInt("litTime", litTime);
        nbt.putInt("cookingProgress", cookingProgress);
        nbt.putInt("cookingTotalTime", cookingTotalTime);
        CompoundNBT used = new CompoundNBT();
        usedRecipes.forEach((k, v) -> used.putInt(k.toString(), v));
        nbt.put("usedRecipes", used);
    }

    @SuppressWarnings("unchecked")
    public <R extends IRecipe<?>> boolean canCook(R recipe) {
        if (!itemHandler.getStackInSlot(SLOT_INPUT).isEmpty() && recipe != null) {
            ItemStack stack = ((IRecipe<ISidedInventory>) recipe).assemble(this);
            int resultAmount = stack.getCount();
            if (stack.isEmpty()) {
                return false;
            } else {
                ItemStack out = itemHandler.getStackInSlot(SLOT_OUTPUT);
                if (out.isEmpty()) {
                    return true;
                } else if (!out.sameItem(stack)) {
                    return false;
                } else if (out.getCount() + resultAmount <= getMaxStackSize() && out.getCount() + resultAmount <= out.getMaxStackSize()) {
                    return true;
                } else {
                    return out.getCount() + resultAmount <= stack.getMaxStackSize();
                }
            }
        }
        return false;
    }

    public boolean isLit() {
        return litTime > 0;
    }

    public int getBurnDuration(ItemStack fuel) {
        return ForgeHooks.getBurnTime(fuel, ModRecipeTypes.COOKING_RECIPE_TYPE);
    }

    public int getItemCookTimeSlow() {
        CookingRecipe recipe = findSuitableRecipe();
        return getItemCookTime(recipe);
    }

    public int getItemCookTime(CookingRecipe recipe) {
        return recipe != null ? recipe.getCookTime() : 200;
    }

    public void cook(CookingRecipe recipe) {
        if (recipe != null && canCook(recipe)) {
            ItemStack input = itemHandler.getStackInSlot(SLOT_INPUT);
            ItemStack result = recipe.assemble(this);
            ItemStack output = itemHandler.getStackInSlot(SLOT_OUTPUT);
            if (output.isEmpty()) {
                itemHandler.setStackInSlot(SLOT_OUTPUT, result);
            } else if (output.getItem() == result.getItem()) {
                int resultAmount = result.getCount();
                output.grow(resultAmount);
            }
            if (!level.isClientSide) {
                setRecipeUsed(recipe);
            }
            input.shrink(1);
        }
    }

    public void updateBlockLitState(boolean state) {
        level.setBlock(worldPosition, level.getBlockState(worldPosition).setValue(BlockStateProperties.LIT, state), 3);
    }

    private CookingRecipe findSuitableRecipe() {
        RecipeManager manager = level.getRecipeManager();
        Optional<CookingRecipe> cookingRecipeOptional = manager.getRecipeFor(ModRecipeTypes.COOKING_RECIPE_TYPE, this, level);
        return cookingRecipeOptional.orElse(null);
    }

    private void createExperience(World level, Vector3d position, int count, CookingRecipe recipe) {
        float value = recipe.getExperience();
        int i = MathHelper.floor(count * value);
        float f = MathHelper.frac(count * value);
        if (f != 0.0F && Math.random() < f) {
            ++i;
        }
        while (i > 0) {
            int expVal = ExperienceOrbEntity.getExperienceValue(i);
            i -= expVal;
            level.addFreshEntity(new ExperienceOrbEntity(level, position.x, position.y, position.z, expVal));
        }
    }

    private boolean runCookProcess(ItemStack fuelStack) {
        boolean changed = false;
        CookingRecipe recipe = findSuitableRecipe();
        if (!isLit() && canCook(recipe)) {
            litTime = getBurnDuration(fuelStack);
            litDuration = litTime;
            if (isLit()) {
                changed = true;
                if (fuelStack.hasContainerItem()) {
                    itemHandler.setStackInSlot(SLOT_FUEL, fuelStack.getContainerItem());
                } else if (!fuelStack.isEmpty()) {
                    fuelStack.shrink(1);
                    if (fuelStack.isEmpty()) {
                        itemHandler.setStackInSlot(SLOT_FUEL, fuelStack.getContainerItem());
                    }
                }
            }
        }
        if (isLit() && canCook(recipe)) {
            ++cookingProgress;
            if (cookingProgress == cookingTotalTime) {
                cookingProgress = 0;
                cookingTotalTime = getItemCookTime(recipe);
                cook(recipe);
                changed = true;
            }
        } else {
            cookingProgress = 0;
        }
        return changed;
    }
}
