package dev.toma.gunsrpg.common.entity.projectile;

import dev.toma.gunsrpg.api.common.IAmmoMaterial;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class MultipartReaction implements IReaction {

    private final IReaction[] reactions;

    private MultipartReaction(IReaction... reactions) {
        this.reactions = reactions;
    }

    public static IReaction multi(IReaction... reactions) {
        return new MultipartReaction(reactions);
    }

    @Override
    public void react(AbstractProjectile projectile, Vector3d impact, World world) {
        for (IReaction reaction : reactions) {
            reaction.react(projectile, impact, world);
        }
    }

    @Override
    public void writeInitialData(AbstractProjectile projectile, IAmmoMaterial material, LivingEntity owner) {
        for (IReaction reaction : reactions) {
            reaction.writeInitialData(projectile, material, owner);
        }
    }
}
