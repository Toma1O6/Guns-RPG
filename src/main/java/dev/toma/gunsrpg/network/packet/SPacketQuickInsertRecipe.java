package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.common.tileentity.TileEntitySmithingTable;
import dev.toma.gunsrpg.util.recipes.SmithingTableRecipes;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SPacketQuickInsertRecipe implements IMessage {

    private SmithingTableRecipes.SmithingRecipe recipe;
    private BlockPos pos;
    private boolean shift;

    public SPacketQuickInsertRecipe() {}

    public SPacketQuickInsertRecipe(SmithingTableRecipes.SmithingRecipe recipe, BlockPos pos, boolean shift) {
        this.recipe = recipe;
        this.pos = pos;
        this.shift = shift;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(SmithingTableRecipes.getRecipeId(recipe));
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
        buf.writeBoolean(shift);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        recipe = SmithingTableRecipes.getRecipeById(buf.readInt());
        pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        shift = buf.readBoolean();
    }

    public static class Handler implements IMessageHandler<SPacketQuickInsertRecipe, IMessage> {

        @Override
        public IMessage onMessage(SPacketQuickInsertRecipe message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServer().addScheduledTask(() -> {
                World world = player.world;
                TileEntity te = world.getTileEntity(message.pos);
                if(te instanceof TileEntitySmithingTable) {
                    TileEntitySmithingTable smithingTable = (TileEntitySmithingTable) te;
                    SmithingTableRecipes.SmithingRecipe recipe = message.recipe;
                    do {
                        if(!hasSpace(recipe, smithingTable) || !canCraft(player, recipe)) {
                            break;
                        }
                        for(SmithingTableRecipes.SmithingIngredient ingredient : recipe.getIngredients()) {
                            int index = ingredient.getIndex();
                            ItemStack stack = ingredient.getFirstItem().copy();
                            int i = player.inventory.clearMatchingItems(stack.getItem(), stack.getItemDamage(), 1, null);
                            insert(index, stack, smithingTable, player);
                        }
                    } while (message.shift);
                }
            });
            return null;
        }

        void insert(int slot, ItemStack stack, TileEntitySmithingTable smithingTable, EntityPlayer player) {
            ItemStack st = smithingTable.getStackInSlot(slot);
            if(st.isEmpty()) {
                smithingTable.setInventorySlotContents(slot, stack);
            } else {
                if(st.getItem() != stack.getItem()) {
                    ItemStack remove = st.copy();
                    smithingTable.removeStackFromSlot(slot);
                    player.addItemStackToInventory(remove);
                }
                int count = smithingTable.getStackInSlot(slot).getCount();
                smithingTable.setInventorySlotContents(slot, new ItemStack(stack.getItem(), count + stack.getCount(), stack.getItemDamage()));
            }
        }

        boolean hasSpace(SmithingTableRecipes.SmithingRecipe recipe, TileEntitySmithingTable smithingTable) {
            for(SmithingTableRecipes.SmithingIngredient ingredient : recipe.getIngredients()) {
                ItemStack stack = smithingTable.getStackInSlot(ingredient.getIndex());
                if(stack.getCount() >= 64) {
                    return false;
                }
            }
            return true;
        }

        boolean canCraft(EntityPlayer player, SmithingTableRecipes.SmithingRecipe recipe) {
            for(SmithingTableRecipes.SmithingIngredient ingredient : recipe.getIngredients()) {
                Item item = ingredient.getFirstItem().getItem();
                int ingredientCount = ingredientsInRecipe(recipe, ingredient);
                int inInventory = getFromInventory(player, item);
                if(inInventory < ingredientCount) {
                    return false;
                }
            }
            return true;
        }

        int ingredientsInRecipe(SmithingTableRecipes.SmithingRecipe recipe, SmithingTableRecipes.SmithingIngredient ingredient) {
            int i = 0;
            for(SmithingTableRecipes.SmithingIngredient ingredient1 : recipe.getIngredients()) {
                if(ingredient.getFirstItem().getItem() == ingredient1.getFirstItem().getItem()) {
                    ++i;
                }
            }
            return i;
        }

        int getFromInventory(EntityPlayer player, Item item) {
            InventoryPlayer playerInv = player.inventory;
            int count = 0;
            for(int i = 0; i < playerInv.getSizeInventory(); i++) {
                ItemStack stack = playerInv.getStackInSlot(i);
                if(stack.getItem() == item) {
                    count += stack.getCount();
                }
            }
            return count;
        }
    }
}
