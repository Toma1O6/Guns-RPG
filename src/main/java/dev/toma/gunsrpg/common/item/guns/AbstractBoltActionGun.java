package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.client.animation.ModAnimations;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.C2S_SetAimingPacket;
import lib.toma.animations.AnimationEngine;
import lib.toma.animations.AnimationUtils;
import lib.toma.animations.api.Animation;
import lib.toma.animations.api.IAnimationPipeline;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class AbstractBoltActionGun extends GunItem {

    protected AbstractBoltActionGun(String name, Properties properties) {
        super(name, properties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public final void onShoot(PlayerEntity player, ItemStack stack) {
        NetworkManager.sendServerPacket(new C2S_SetAimingPacket(false));
        ResourceLocation eject = this.getBulletEjectAnimationPath();
        int delay = 6;
        int length = this.getFirerate(PlayerData.getUnsafe(player).getAttributes()) - delay;
        IAnimationPipeline pipeline = AnimationEngine.get().pipeline();
        Animation animation = AnimationUtils.createAnimation(eject, provider -> new Animation(provider, length));
        pipeline.scheduleInsert(ModAnimations.CHAMBER, animation, delay);
    }
}
