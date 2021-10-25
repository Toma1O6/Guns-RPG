package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.IAmmoMaterial;
import dev.toma.gunsrpg.api.common.IAmmoProvider;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterialManager;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import dev.toma.gunsrpg.util.Lifecycle;
import dev.toma.gunsrpg.util.locate.ILocatorPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class SPacketSelectAmmo extends AbstractNetworkPacket<SPacketSelectAmmo> {

    private IAmmoMaterial material;

    public SPacketSelectAmmo() {
    }

    public SPacketSelectAmmo(IAmmoMaterial material) {
        this.material = material;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeResourceLocation(material.getMaterialID());
    }

    @Override
    public SPacketSelectAmmo decode(PacketBuffer buffer) {
        IAmmoMaterial material = AmmoMaterialManager.get().findMaterial(buffer.readResourceLocation());
        return new SPacketSelectAmmo(material);
    }

    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        ServerPlayerEntity player = context.getSender();
        ItemStack stack = player.getMainHandItem();
        if (stack.getItem() instanceof GunItem) {
            GunItem gun = (GunItem) stack.getItem();
            int ammo = gun.getAmmo(stack);
            IAmmoMaterial oldMaterial = gun.getMaterialFromNBT(stack);
            if (oldMaterial != null && ammo > 0) {
                clearWeapon(player, gun, stack, ammo);
            }
            CompoundNBT data = stack.getTag();
            data.putString("material", material.getMaterialID().toString());
        }
    }

    private void clearWeapon(PlayerEntity player, GunItem gun, ItemStack stack, int ammoAmount) {
        Item item = null;
        ILocatorPredicate<IAmmoProvider> predicate = provider -> provider.getAmmoType() == gun.getAmmoType() && provider.getMaterial() == gun.getMaterialFromNBT(stack);
        Lifecycle lifecycle = GunsRPG.getModLifecycle();
        for (IAmmoProvider ammo : lifecycle.getAllCompatibleAmmoProviders(gun)) {
            if (predicate.isValidResult(ammo)) {
                item = (Item) ammo;
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
