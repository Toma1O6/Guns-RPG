package dev.toma.gunsrpg.common.entity.projectile;

import dev.toma.gunsrpg.api.common.IAmmoMaterial;
import dev.toma.gunsrpg.util.properties.Properties;
import dev.toma.gunsrpg.util.properties.PropertyKey;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class PropertyTriggerReaction<V> implements IReaction {

    public static final IReaction STICKY = new PropertyTriggerReaction<>(Properties.STICKY, true);
    public static final IReaction IMPACT = new PropertyTriggerReaction<>(Properties.IMPACT, true);

    private final PropertyKey<V> key;
    private final V value;

    public PropertyTriggerReaction(PropertyKey<V> key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public void react(AbstractProjectile projectile, Vector3d impact, World world) {
    }

    @Override
    public void writeInitialData(AbstractProjectile projectile, IAmmoMaterial material, LivingEntity owner) {
        projectile.setProperty(key, value);
    }
}
