package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.common.init.ModRecipeTypes;
import dev.toma.gunsrpg.common.tileentity.SmithingTableTileEntity;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import dev.toma.gunsrpg.resource.smithing.SmithingRecipe;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Optional;

public class C2S_RequestSmithingCraftPacket extends AbstractNetworkPacket<C2S_RequestSmithingCraftPacket> {

    private final BlockPos pos;
    private final boolean shiftKey;

    public C2S_RequestSmithingCraftPacket() {
        this(null, false);
    }

    public C2S_RequestSmithingCraftPacket(BlockPos pos, boolean shiftKey) {
        this.pos = pos;
        this.shiftKey = shiftKey;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeBoolean(shiftKey);
    }

    @Override
    public C2S_RequestSmithingCraftPacket decode(PacketBuffer buffer) {
        return new C2S_RequestSmithingCraftPacket(buffer.readBlockPos(), buffer.readBoolean());
    }

    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        ServerPlayerEntity player = context.getSender();
        World world = player.level;
        if (pos != null && world.isLoaded(pos)) {
            TileEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof SmithingTableTileEntity) {
                SmithingTableTileEntity smithingTable = (SmithingTableTileEntity) tileEntity;
                if (shiftKey) {
                    while (true) {
                        if (!tryCraft(world, smithingTable, player))
                            break;
                    }
                } else {
                    tryCraft(world, smithingTable, player);
                }
            }
        }
    }

    private SmithingRecipe lastRecipe;

    private boolean tryCraft(World world, SmithingTableTileEntity inventory, PlayerEntity player) {
        RecipeManager manager = world.getRecipeManager();
        Optional<SmithingRecipe> optional = manager.getRecipeFor(ModRecipeTypes.SMITHING_RECIPE_TYPE, inventory, world);
        if (optional.isPresent()) {
            SmithingRecipe recipe = optional.orElseThrow(IllegalStateException::new);
            if (recipe.canCraft(player)) {
                if (lastRecipe != null && lastRecipe != recipe) {
                    return false;
                }
                lastRecipe = recipe;
                consumeIngredients(inventory);
                ItemStack result = recipe.assemble(inventory);
                player.addItem(result);
                return true;
            }
        }
        return false;
    }

    private void consumeIngredients(SmithingTableTileEntity inventory) {
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty()) {
                stack.shrink(1);
            }
        }
    }
}
