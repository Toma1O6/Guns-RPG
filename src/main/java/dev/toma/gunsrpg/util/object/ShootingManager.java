package dev.toma.gunsrpg.util.object;

import dev.toma.gunsrpg.client.animation.AnimationManager;
import dev.toma.gunsrpg.client.animation.Animations;
import dev.toma.gunsrpg.client.animation.impl.RecoilAnimation;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.AbilityData;
import dev.toma.gunsrpg.common.capability.object.ReloadInfo;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterial;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.common.item.guns.ammo.ItemAmmo;
import dev.toma.gunsrpg.common.item.guns.reload.IReloadManager;
import dev.toma.gunsrpg.common.skilltree.Ability;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.SPacketSetReloading;
import dev.toma.gunsrpg.network.packet.SPacketShoot;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CooldownTracker;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ShootingManager {

    private static final List<EntityPlayer> LIST = new ArrayList<>();

    public static void updateAllShooting(World world) {
        if(world.isRemote) return;
        for(EntityPlayer player : LIST) {
            ItemStack stack = player.getHeldItemMainhand();
            if(!(stack.getItem() instanceof GunItem)) {
                LIST.remove(player);
                continue;
            }
            GunItem item = (GunItem) stack.getItem();
            if(item == null) {
                LIST.remove(player);
                continue;
            }
            if(!canShoot(player, stack)) {
                LIST.remove(player);
                continue;
            }
            item.shoot(world, player, stack);
        }
    }

    public static boolean isShooting(EntityPlayer player) {
        return LIST.contains(player);
    }

    public static boolean canShoot(EntityPlayer player, ItemStack stack) {
        CooldownTracker tracker = player.getCooldownTracker();
        PlayerData data = PlayerDataFactory.get(player);
        ReloadInfo reloadInfo = data.getReloadInfo();
        AbilityData abilityData = data.getSkillData().getAbilityData();
        GunItem item = (GunItem) stack.getItem();
        IReloadManager reloadManager = item.getReloadManager();
        if(!tracker.hasCooldown(stack.getItem())) {
            AmmoType ammoType = item.getAmmoType();
            AmmoMaterial material = item.getMaterialFromNBT(stack);
            Ability.Type type = null;
            for(ItemAmmo a : ItemAmmo.GUN_TO_ITEM_MAP.get(item)) {
                if(a.getAmmoType() == ammoType && a.getMaterial() == material) {
                    type = a.getRequiredProperty();
                    break;
                }
            }
            if(material == null || type == null) return false;
            if(reloadInfo.isReloading()) {
                if(reloadManager.canBeInterrupted()) {
                    reloadInfo.cancelReload();
                    if(!player.world.isRemote) {
                        data.sync();
                    } else {
                        NetworkManager.toServer(new SPacketSetReloading(false, 0));
                    }
                    return item.hasAmmo(stack) && abilityData.hasProperty(type);
                }
                return false;
            }
            return item.hasAmmo(stack) && abilityData.hasProperty(type);
        }
        return false;
    }

    public static void updateShootingState(EntityPlayer player, boolean state) {
        if(state && !isShooting(player)) {
            LIST.add(player);
        } else {
            LIST.remove(player);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void shootSingle(EntityPlayer player, ItemStack stack) {
        if(canShoot(player, stack)) {
            GunItem gun = (GunItem) stack.getItem();
            player.rotationPitch -= gun.getVerticalRecoil(player);
            player.rotationYaw += gun.getHorizontalRecoil(player);
            NetworkManager.toServer(new SPacketShoot((GunItem) stack.getItem()));
            AnimationManager.sendNewAnimation(Animations.RECOIL, RecoilAnimation.newInstance(5));
        }
    }

    @SideOnly(Side.CLIENT)
    public static void startStopShooting(EntityPlayer player, boolean state) {
        updateShootingState(player, state);
        NetworkManager.toServer(new Update(state));
    }

    @Nullable
    public static GunItem getGunFrom(EntityPlayer player) {
        return player.getHeldItemMainhand().getItem() instanceof GunItem ? (GunItem) player.getHeldItemMainhand().getItem() : null;
    }

    public static class Update implements IMessage {

        boolean state;

        public Update() {
        }

        public Update(boolean state) {
            this.state = state;
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeBoolean(state);
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            state = buf.readBoolean();
        }

        public static class Handler implements IMessageHandler<Update, IMessage> {
            @Override
            public IMessage onMessage(Update message, MessageContext ctx) {
                EntityPlayer player = ctx.getServerHandler().player;
                player.getServer().addScheduledTask(() -> {
                    ShootingManager.updateShootingState(player, message.state);
                });
                return null;
            }
        }
    }
}
