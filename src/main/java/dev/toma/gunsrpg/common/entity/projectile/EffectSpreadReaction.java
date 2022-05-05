package dev.toma.gunsrpg.common.entity.projectile;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class EffectSpreadReaction implements IReaction {

    public static final IReaction TEAR_GAS = new EffectSpreadReaction(0x24BA93, () -> new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 120, 1), () -> new EffectInstance(Effects.BLINDNESS, 120));
    public static final IReaction TOXIN = new EffectSpreadReaction(0x7E22B7, () -> new EffectInstance(Effects.WITHER, 200, 1));

    private final int rgb;
    private final Supplier<EffectInstance>[] effects;

    @SafeVarargs
    public EffectSpreadReaction(int rgb, Supplier<EffectInstance>... effects) {
        this.rgb = rgb;
        this.effects = effects;
    }

    @Override
    public void react(AbstractProjectile projectile, Vector3d impact, World world) {
        if (!world.isClientSide) {
            AreaEffectCloudEntity entity = new AreaEffectCloudEntity(world, impact.x, impact.y, impact.z);
            entity.setDuration(300);
            entity.setRadius(6.0F);
            entity.setRadiusPerTick(-0.019F);
            for (Supplier<EffectInstance> supplier : effects) {
                entity.addEffect(supplier.get());
            }
            entity.setFixedColor(rgb);
            world.addFreshEntity(entity);
        }
    }
}
