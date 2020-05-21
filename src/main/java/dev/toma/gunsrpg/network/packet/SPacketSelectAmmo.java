package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.common.item.guns.AmmoMaterial;
import dev.toma.gunsrpg.common.item.guns.AmmoType;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.ItemAmmo;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SPacketSelectAmmo implements IMessage {

    private AmmoMaterial material;

    public SPacketSelectAmmo() {}

    public SPacketSelectAmmo(AmmoMaterial material) {
        this.material = material;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(material.ordinal());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        material = AmmoMaterial.values()[buf.readInt()];
    }

    public static class Handler implements IMessageHandler<SPacketSelectAmmo, IMessage> {

        @Override
        public IMessage onMessage(SPacketSelectAmmo message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServer().addScheduledTask(() -> {
                ItemStack stack = player.getHeldItemMainhand();
                if(stack.getItem() instanceof GunItem) {
                    if(!stack.hasTagCompound()) {
                        stack.setTagCompound(new NBTTagCompound());
                    }
                    GunItem item = (GunItem) stack.getItem();
                    int ammo = item.getAmmo(stack);
                    AmmoType type = item.getAmmoType();
                    AmmoMaterial oldMat = item.getMaterialFromNBT(stack);
                    if(oldMat != null && ammo > 0) {
                        Item it = null;
                        for(ItemAmmo itAmm : ItemAmmo.GUN_TO_ITEM_MAP.get(item)) {
                            if(itAmm.getAmmoType() == type && itAmm.getMaterial() == oldMat) {
                                it = itAmm;
                                break;
                            }
                        }
                        if(it != null) {
                            ItemStack oldStack = new ItemStack(it, ammo);
                            player.addItemStackToInventory(oldStack);
                            item.setAmmoCount(stack, 0);
                        }
                    }
                    NBTTagCompound nbt = stack.getTagCompound();
                    nbt.setInteger("material", message.material.ordinal());
                }
            });
            return null;
        }
    }
}
