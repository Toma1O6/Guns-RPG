package dev.toma.gunsrpg.common.entity.projectile;

import dev.toma.gunsrpg.util.properties.Properties;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

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
            world.explode(projectile, impact.x, impact.y, impact.z, power, allowGriefing ? mode : Explosion.Mode.NONE);
        }
    }
}
