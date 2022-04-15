package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.api.common.IAmmoMaterial;
import dev.toma.gunsrpg.common.entity.projectile.AbstractProjectile;
import dev.toma.gunsrpg.common.entity.projectile.IReaction;
import dev.toma.gunsrpg.common.item.guns.ammo.IReactiveMaterial;
import dev.toma.gunsrpg.common.item.guns.util.IAdditionalShootData;
import dev.toma.gunsrpg.util.properties.PropertyContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static dev.toma.gunsrpg.util.properties.Properties.REACTION;

public abstract class AbstractExplosiveLauncher extends GunItem implements IAdditionalShootData {

    public AbstractExplosiveLauncher(String name, Properties properties) {
        super(name, properties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addExtraData(PropertyContext context, PlayerEntity player, ItemStack stack, IAmmoMaterial material) {
        if (material instanceof IReactiveMaterial) {
            IReactiveMaterial reactiveMaterial = (IReactiveMaterial) material;
            IReaction reaction = reactiveMaterial.getReaction();
            context.setProperty(REACTION, reaction);
        }
    }

    @Override
    protected void prepareForShooting(AbstractProjectile projectile, LivingEntity shooter) {
        ItemStack stack = shooter.getMainHandItem();
        if (stack.getItem() instanceof GunItem) {
            GunItem gun = (GunItem) stack.getItem();
            IAmmoMaterial material = gun.getMaterialFromNBT(stack);
            if (material instanceof IReactiveMaterial) {
                IReaction reaction = ((IReactiveMaterial) material).getReaction();
                projectile.setProperty(REACTION, reaction);
                reaction.writeInitialData(projectile, material, shooter);
            }
        }
    }
}
