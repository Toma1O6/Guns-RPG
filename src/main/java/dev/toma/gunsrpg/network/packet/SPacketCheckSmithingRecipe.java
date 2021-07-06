package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.common.tileentity.SmithingTableTileEntity;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import dev.toma.gunsrpg.util.object.OptionalObject;
import dev.toma.gunsrpg.util.recipes.SmithingTableRecipes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.items.IItemHandler;

public class SPacketCheckSmithingRecipe extends AbstractNetworkPacket<SPacketCheckSmithingRecipe> {

    private final BlockPos pos;
    private final boolean shiftKey;
    private final int selectedID;

    public SPacketCheckSmithingRecipe() {
        this(null, false, -1);
    }

    public SPacketCheckSmithingRecipe(BlockPos pos, boolean shiftkey, OptionalObject<SmithingTableRecipes.SmithingRecipe> selected) {
        this(pos, shiftkey, selected.isPresent() ? SmithingTableRecipes.getRecipeId(selected.get()) : -1);
    }

    private SPacketCheckSmithingRecipe(BlockPos pos, boolean shiftKey, int selectedID) {
        this.pos = pos;
        this.shiftKey = shiftKey;
        this.selectedID = selectedID;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeBoolean(shiftKey);
        buffer.writeInt(selectedID);
    }

    @Override
    public SPacketCheckSmithingRecipe decode(PacketBuffer buffer) {
        return new SPacketCheckSmithingRecipe(buffer.readBlockPos(), buffer.readBoolean(), buffer.readInt());
    }

    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        ServerPlayerEntity player = context.getSender();
        World world = player.level;
        if (pos != null && world.isLoaded(pos)) {
            TileEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof SmithingTableTileEntity) {
                SmithingTableTileEntity smithingTable = (SmithingTableTileEntity) tileEntity;
                SmithingTableRecipes.SmithingRecipe recipe = SmithingTableRecipes.findRecipe(smithingTable, selectedID);
                PlayerDataFactory.get(player).ifPresent(data -> {
                    PlayerSkills skills = data.getSkills();
                    smithingTable.getInventory().ifPresent(handler -> {
                        if (recipe != null && SmithingTableRecipes.hasRequiredSkills(recipe, skills)) {
                            if (shiftKey) {
                                while (recipe == SmithingTableRecipes.findRecipe(smithingTable, selectedID) && SmithingTableRecipes.hasRequiredSkills(recipe, skills)) {
                                    craftFromRecipe(recipe, player, handler);
                                }
                            } else {
                                craftFromRecipe(recipe, player, handler);
                            }
                        }
                    });
                });
            }
        }
    }

    private void craftFromRecipe(SmithingTableRecipes.SmithingRecipe recipe, PlayerEntity player, IItemHandler handler) {
        ItemStack out = recipe.getOutput(player);
        player.addItem(out);
        for (SmithingTableRecipes.SmithingIngredient ingredient : recipe.getIngredients()) {
            handler.getStackInSlot(ingredient.getIndex()).shrink(1);
        }
    }
}
