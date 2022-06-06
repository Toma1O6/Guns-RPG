package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.tileentity.SkilledWorkbenchTileEntity;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import dev.toma.gunsrpg.resource.crafting.OutputModifier;
import dev.toma.gunsrpg.resource.crafting.SkilledRecipe;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Optional;

public class C2S_RequestSkilledCraftPacket extends AbstractNetworkPacket<C2S_RequestSkilledCraftPacket> {

    private final BlockPos pos;
    private final boolean shiftKey;

    public C2S_RequestSkilledCraftPacket() {
        this(null, false);
    }

    public C2S_RequestSkilledCraftPacket(BlockPos pos, boolean shiftKey) {
        this.pos = pos;
        this.shiftKey = shiftKey;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeBoolean(shiftKey);
    }

    @Override
    public C2S_RequestSkilledCraftPacket decode(PacketBuffer buffer) {
        return new C2S_RequestSkilledCraftPacket(buffer.readBlockPos(), buffer.readBoolean());
    }

    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        ServerPlayerEntity player = context.getSender();
        World world = player.level;
        if (pos != null && world.isLoaded(pos)) {
            TileEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof SkilledWorkbenchTileEntity) {
                SkilledWorkbenchTileEntity inventoryTileEntity = (SkilledWorkbenchTileEntity) tileEntity;
                if (shiftKey) {
                    while (true) {
                        if (!tryCraft(world, inventoryTileEntity, player))
                            break;
                    }
                } else {
                    tryCraft(world, inventoryTileEntity, player);
                }
            }
        }
    }

    private SkilledRecipe<?> lastRecipe;

    private <T extends SkilledWorkbenchTileEntity> boolean tryCraft(World world, T inventory, PlayerEntity player) {
        RecipeManager manager = world.getRecipeManager();
        IRecipeType<SkilledRecipe<T>> type = inventory.getRecipeType();
        Optional<SkilledRecipe<T>> optional = manager.getRecipeFor(type, inventory, world);
        if (optional.isPresent()) {
            SkilledRecipe<T> recipe = optional.orElseThrow(IllegalStateException::new);
            if (recipe.canCraft(player)) {
                if (lastRecipe != null && lastRecipe != recipe) {
                    return false;
                }
                lastRecipe = recipe;
                consumeIngredients(inventory);
                ItemStack result = recipe.assemble(inventory);
                OutputModifier modifier = recipe.getOutputModifier();
                if (modifier != null) {
                    modifier.applyRaw(result, PlayerData.getUnsafe(player).getAttributes());
                }
                player.addItem(result);
                ItemStack returningItem = recipe.getReturningItem();
                if (!returningItem.isEmpty()) {
                    player.addItem(returningItem.copy());
                }
                return true;
            }
        }
        return false;
    }

    private void consumeIngredients(IInventory inventory) {
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty()) {
                stack.shrink(1);
            }
        }
    }
}
