package dev.toma.gunsrpg.common.entity.projectile;

import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.properties.Properties;
import dev.toma.gunsrpg.world.WeaponExplosiveDamageSource;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class ExplosiveReaction implements IReaction {

    public static final IReaction EXPLOSION = new ExplosiveReaction(4.0F, Explosion.Mode.NONE);
    public static final IReaction HE_EXPLOSION = new ExplosiveReaction(5.0F, Explosion.Mode.NONE);
    public static final IReaction DESTRUCTIVE_EXPLOSION = new ExplosiveReaction(4.0F, Explosion.Mode.DESTROY);

    private final float explosionPower;
    private final Explosion.Mode mode;

    public ExplosiveReaction(float explosionPower, Explosion.Mode mode) {
        this.explosionPower = explosionPower;
        this.mode = mode;
    }

    @Override
    public void react(AbstractProjectile projectile, Vector3d impact, World world) {
        if (!world.isClientSide) {
            float power = projectile.getProperty(Properties.EXPLOSION_POWER) + explosionPower;
            boolean allowGriefing = world.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
            DamageSource source = new WeaponExplosiveDamageSource(projectile.getOwner(), projectile, projectile.getWeaponSource());
            Explosion explosion = new Explosion(world, projectile, source, null, impact.x, impact.y, impact.z, power, false, allowGriefing ? mode : Explosion.Mode.NONE);
            ModUtils.explode((ServerWorld) world, explosion);
        }
    }
}
