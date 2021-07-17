package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterial;
import dev.toma.gunsrpg.common.item.guns.ammo.ItemAmmo;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.network.NetworkEvent;

public class SPacketSelectAmmo extends AbstractNetworkPacket<SPacketSelectAmmo> {

    private AmmoMaterial material;

    public SPacketSelectAmmo() {
    }

    public SPacketSelectAmmo(AmmoMaterial material) {
        this.material = material;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeVarInt(material.ordinal());
    }

    @Override
    public SPacketSelectAmmo decode(PacketBuffer buffer) {
        AmmoMaterial[] materials = AmmoMaterial.values();
        int index = MathHelper.clamp(buffer.readVarInt(), 0, materials.length - 1);
        return new SPacketSelectAmmo(materials[index]);
    }

    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        ServerPlayerEntity player = context.getSender();
        ItemStack stack = player.getMainHandItem();
        if (stack.getItem() instanceof GunItem) {
            GunItem gun = (GunItem) stack.getItem();
            int ammo = gun.getAmmo(stack);
            AmmoMaterial oldMaterial = gun.getMaterialFromNBT(stack);
            if (oldMaterial != null && ammo > 0) {
                clearWeapon(player, gun, stack, ammo);
            }
            CompoundNBT data = stack.getTag();
            data.putInt("material", material.ordinal());
        }
    }

    private void clearWeapon(PlayerEntity player, GunItem gun, ItemStack stack, int ammoAmount) {
        Item item = null;
        for (ItemAmmo ammo : ItemAmmo.GUN_TO_ITEM_MAP.get(gun)) {
            if (ammo.getAmmoType() == gun.getAmmoType() && ammo.getMaterial() == gun.getMaterialFromNBT(stack)) {
                item = ammo;
                break;
            }
        }
        if (item != null) {
            ItemStack returnAmmo = new ItemStack(item, ammoAmount);
            player.addItem(returnAmmo);
            gun.setAmmoCount(stack, 0);
        }
    }
}
