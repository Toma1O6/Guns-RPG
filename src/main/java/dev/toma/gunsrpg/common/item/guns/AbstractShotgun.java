package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.client.animation.BulletEjectAnimation;
import dev.toma.gunsrpg.client.animation.ModAnimations;
import dev.toma.gunsrpg.common.IShootProps;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.entity.projectile.AbstractProjectile;
import dev.toma.gunsrpg.common.entity.projectile.Pellet;
import dev.toma.gunsrpg.common.init.ModEntities;
import lib.toma.animations.AnimationEngine;
import lib.toma.animations.AnimationUtils;
import lib.toma.animations.api.AnimationList;
import lib.toma.animations.api.IAnimationPipeline;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class AbstractShotgun extends GunItem {

    public AbstractShotgun(String name, Properties properties) {
        super(name, properties);
    }

    public abstract int getPelletCount(LivingEntity shooter, ItemStack stack);

    @Override
    protected AbstractProjectile makeProjectile(World level, LivingEntity shooter) {
        return new Pellet(ModEntities.PELLET.get(), level, shooter);
    }

    @Override
    public void handleShootProjectileAction(World world, LivingEntity entity, ItemStack stack, IShootProps props) {
        int pellets = this.getPelletCount(entity, stack);
        for (int i = 0; i < pellets; i++) {
            shootProjectile(world, entity, stack, props);
        }
    }

    @Override
    protected float getInaccuracy(IShootProps props, LivingEntity entity) {
        return 2.4F;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onShoot(PlayerEntity player, ItemStack stack) {
        ResourceLocation animationPath = this.getBulletEjectAnimationPath();
        int animationLength = this.getShootAnimationLength(player);
        IAnimationPipeline pipeline = AnimationEngine.get().pipeline();
        this.handleAnimation(animationPath, animationLength, pipeline);
    }

    @OnlyIn(Dist.CLIENT)
    protected void handleAnimation(ResourceLocation animationPath, int length, IAnimationPipeline pipeline) {
        BulletEjectAnimation animation = AnimationUtils.createAnimation(animationPath, provider -> new BulletEjectAnimation(provider, length));
        AnimationList.enqueue(ModAnimations.BULLET_EJECTION, animation);
    }

    protected int getShootAnimationLength(PlayerEntity player) {
        IPlayerData data = PlayerData.getUnsafe(player);
        IAttributeProvider provider = data.getAttributes();
        return this.getFirerate(provider) - 5;
    }
}
