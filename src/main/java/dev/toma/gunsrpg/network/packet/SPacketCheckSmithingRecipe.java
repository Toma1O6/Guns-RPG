package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.common.tileentity.TileEntitySmithingTable;
import dev.toma.gunsrpg.util.recipes.SmithingTableRecipes;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SPacketCheckSmithingRecipe implements IMessage {

    private BlockPos pos;
    private boolean shiftKey;

    public SPacketCheckSmithingRecipe() {

    }

    public SPacketCheckSmithingRecipe(BlockPos pos, boolean shiftkey) {
        this.pos = pos;
        this.shiftKey = shiftkey;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
        buf.writeBoolean(shiftKey);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        shiftKey = buf.readBoolean();
    }

    public static class Handler implements IMessageHandler<SPacketCheckSmithingRecipe, IMessage> {

        @Override
        public IMessage onMessage(SPacketCheckSmithingRecipe message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServer().addScheduledTask(() -> {
                World world = player.world;
                BlockPos pos = message.pos;
                if(pos != null && world.isBlockLoaded(pos)) {
                    TileEntity tileEntity = world.getTileEntity(pos);
                    if(tileEntity instanceof TileEntitySmithingTable) {
                        TileEntitySmithingTable smithingTable = (TileEntitySmithingTable) tileEntity;
                        SmithingTableRecipes.SmithingRecipe recipe = SmithingTableRecipes.findRecipe(smithingTable);
                        PlayerSkills skills = PlayerDataFactory.get(player).getSkills();
                        if(recipe != null && SmithingTableRecipes.canCraftRecipe(recipe, skills)) {
                            if(message.shiftKey) {
                                while (recipe == SmithingTableRecipes.findRecipe(smithingTable) && SmithingTableRecipes.canCraftRecipe(recipe, skills)) {
                                    ItemStack out = recipe.getOutput(player);
                                    player.addItemStackToInventory(out);
                                    for(SmithingTableRecipes.SmithingIngredient ingredient : recipe.getIngredients()) {
                                        smithingTable.getStackInSlot(ingredient.getIndex()).shrink(1);
                                    }
                                }
                            } else {
                                ItemStack out = recipe.getOutput(player);
                                player.addItemStackToInventory(out);
                                for(SmithingTableRecipes.SmithingIngredient ingredient : recipe.getIngredients()) {
                                    smithingTable.getStackInSlot(ingredient.getIndex()).shrink(1);
                                }
                            }
                        }
                    }
                }
            });
            return null;
        }
    }
}
