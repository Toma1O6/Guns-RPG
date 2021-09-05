package dev.toma.gunsrpg.common.tileentity;

import dev.toma.gunsrpg.common.init.ModBlockEntities;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlastFurnaceTileEntity extends VanillaInventoryTileEntity implements IRecipeHolder, IRecipeHelperPopulator, ITickableTileEntity, ISidedInventory {

    private static final byte SLOT_INPUT = 0;
    private static final byte SLOT_FUEL = 1;
    private static final byte SLOT_OUTPUT = 2;
    private final Object2IntOpenHashMap<ResourceLocation> usedRecipes = new Object2IntOpenHashMap<>();
    private final IRecipeType<FurnaceRecipe> recipeType;
    private int litTime;
    private int litDuration;
    private int cookingProgress;
    private int cookingTotalTime;
    private final IIntArray data = new IIntArray() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return BlastFurnaceTileEntity.this.litTime;
                case 1:
                    return BlastFurnaceTileEntity.this.litDuration;
                case 2:
                    return BlastFurnaceTileEntity.this.cookingProgress;
                case 3:
                    return BlastFurnaceTileEntity.this.cookingTotalTime;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    BlastFurnaceTileEntity.this.litTime = value;
                    break;
                case 1:
                    BlastFurnaceTileEntity.this.litDuration = value;
                    break;
                case 2:
                    BlastFurnaceTileEntity.this.cookingProgress = value;
                    break;
                case 3:
                    BlastFurnaceTileEntity.this.cookingTotalTime = value;
                    break;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    public BlastFurnaceTileEntity() {
        this(ModBlockEntities.BLAST_FURNACE.get());
    }

    protected BlastFurnaceTileEntity(TileEntityType<? extends BlastFurnaceTileEntity> type) {
        super(type);
        this.recipeType = IRecipeType.SMELTING;
    }

    public IIntArray getData() {
        return data;
    }

    @Override
    public IItemHandlerModifiable createInventory() {
        return new ItemStackHandler(3);
    }

    @Override
    public void read(CompoundNBT nbt) {
        litTime = nbt.getInt("litTime");
        LazyOptional<IItemHandlerModifiable> optional = getInventory();
        litDuration = optional.isPresent() ? getBurnDuration(optional.orElse(null).getStackInSlot(SLOT_FUEL)) : 0;
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

    @Override
    public void tick() {
        boolean wasLit = isLit();
        boolean gotLit = false;
        if (isLit()) {
            --litTime;
        }
        ItemStack fuelStack = itemHandler.getStackInSlot(SLOT_FUEL);
        if (isLit() || !fuelStack.isEmpty() && !itemHandler.getStackInSlot(SLOT_INPUT).isEmpty()) {
            IRecipe<?> recipe = level.getRecipeManager().getRecipeFor(recipeType, this, level).orElse(null);
            if (!isLit() && canBurn(recipe)) {
                litTime = getBurnDuration(fuelStack);
                litDuration = litTime;
                if (isLit()) {
                    gotLit = true;
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
            if (isLit() && canBurn(recipe)) {
                ++cookingProgress;
                if (cookingProgress == cookingTotalTime) {
                    cookingProgress = 0;
                    cookingTotalTime = getTotalCookTime();
                    burn(recipe);
                    gotLit = true;
                }
            } else {
                cookingProgress = 0;
            }
        } else if (!isLit() && cookingProgress > 0) {
            cookingProgress = MathHelper.clamp(cookingProgress - 2, 0, cookingTotalTime);
        }

        if (wasLit != isLit() && !level.isClientSide) {
            gotLit = true;
            level.setBlock(worldPosition, level.getBlockState(worldPosition).setValue(BlockStateProperties.LIT, isLit()), 3);
        }

        if (gotLit && !level.isClientSide) {
            setChanged();
        }
    }

    @Nullable
    @Override
    public IRecipe<?> getRecipeUsed() {
        return null;
    }

    @Override
    public void setRecipeUsed(@Nullable IRecipe<?> recipe) {
        if (recipe != null) {
            ResourceLocation id = recipe.getId();
            usedRecipes.addTo(id, 1);
        }
    }

    @Override
    public void awardUsedRecipes(PlayerEntity p_201560_1_) {

    }

    @Override
    public void fillStackedContents(RecipeItemHelper helper) {
        for (int i = 0; i < getContainerSize(); i++) {
            helper.accountStack(itemHandler.getStackInSlot(i));
        }
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        if (direction == Direction.DOWN) {
            return new int[]{SLOT_FUEL, SLOT_OUTPUT};
        } else return direction == Direction.UP ? new int[]{SLOT_INPUT} : new int[]{SLOT_FUEL};
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction direction) {
        return canPlaceItem(slot, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
        if (direction == Direction.DOWN && slot == 1) {
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
            ItemStack fuelStack = getItem(SLOT_FUEL);
            return ForgeHooks.getBurnTime(stack, recipeType) > 0 || stack.getItem() == Items.BUCKET && fuelStack.getItem() != Items.BUCKET;
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
            cookingTotalTime = getTotalCookTime();
            cookingProgress = 0;
            setChanged();
        }
    }

    public void awardUsedRecipeAndAddExp(PlayerEntity player) {
        List<IRecipe<?>> recipeList = getRecipesForAwardsWithExp(player.level, player.position());
        player.awardRecipes(recipeList);
        usedRecipes.clear();
    }

    public List<IRecipe<?>> getRecipesForAwardsWithExp(World level, Vector3d position) {
        List<IRecipe<?>> list = new ArrayList<>();
        for (Object2IntMap.Entry<ResourceLocation> entry : usedRecipes.object2IntEntrySet()) {
            level.getRecipeManager().byKey(entry.getKey()).ifPresent(recipe -> {
                list.add(recipe);
                createExperience(level, position, entry.getIntValue(), ((FurnaceRecipe) recipe).getExperience());
            });
        }
        return list;
    }

    private void createExperience(World level, Vector3d position, int count, float value) {
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

    private int getBurnDuration(ItemStack stack) {
        return stack.isEmpty() ? 0 : ForgeHooks.getBurnTime(stack, recipeType);
    }

    private int getTotalCookTime() {
        int multiplier = canDoubleResult(itemHandler.getStackInSlot(SLOT_INPUT)) ? 2 : 1;
        return level.getRecipeManager().getRecipeFor(recipeType, this, level).map(AbstractCookingRecipe::getCookingTime).orElse(200) * multiplier;
    }

    @SuppressWarnings("unchecked")
    private boolean canBurn(IRecipe<?> recipe) {
        if (!itemHandler.getStackInSlot(SLOT_INPUT).isEmpty() && recipe != null) {
            ItemStack stack = ((IRecipe<ISidedInventory>) recipe).assemble(this);
            int recipeResultCount = stack.getCount() * 2;
            if (stack.isEmpty()) {
                return false;
            } else {
                ItemStack outputStack = itemHandler.getStackInSlot(SLOT_OUTPUT);
                if (outputStack.isEmpty()) {
                    return true;
                } else if (!outputStack.sameItem(stack)) {
                    return false;
                } else if (outputStack.getCount() + recipeResultCount <= getMaxStackSize() && outputStack.getCount() + recipeResultCount <= outputStack.getMaxStackSize()) {
                    return true;
                } else {
                    return outputStack.getCount() + recipeResultCount <= stack.getMaxStackSize();
                }
            }
        } else {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private void burn(IRecipe<?> recipe) {
        if (recipe != null && canBurn(recipe)) {
            ItemStack inputStack = itemHandler.getStackInSlot(SLOT_INPUT);
            ItemStack resultStack = ((IRecipe<ISidedInventory>) recipe).assemble(this);
            ItemStack outputStack = itemHandler.getStackInSlot(SLOT_OUTPUT);
            if (outputStack.isEmpty()) {
                ItemStack resultCopy = resultStack.copy();
                if (canDoubleResult(inputStack)) {
                    resultCopy.setCount(Math.min(Math.min(getMaxStackSize(), resultCopy.getMaxStackSize()), resultCopy.getCount() * 2));
                }
                itemHandler.setStackInSlot(SLOT_OUTPUT, resultCopy);
            } else if (outputStack.getItem() == resultStack.getItem()) {
                int oldCount = resultStack.getCount();
                int count = canDoubleResult(inputStack) ? oldCount * 2 : oldCount;
                outputStack.grow(count);
            }

            if (!level.isClientSide) {
                setRecipeUsed(recipe);
            }

            inputStack.shrink(1);
        }
    }

    public boolean isLit() {
        return litTime > 0;
    }

    private boolean canDoubleResult(ItemStack input) {
        Item item = input.getItem();
        return item.is(Tags.Items.ORES);
    }
}
