package dev.toma.gunsrpg.client;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.item.guns.util.IEntityTrackingGun;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;
import java.util.function.Predicate;

@Mod.EventBusSubscriber(modid = GunsRPG.MODID, value = Dist.CLIENT)
public class GuidedProjectileTargetHandler {

    private static int selectedEntity = -1;
    private static int lockTimer;
    private static int updateTimer;
    private static boolean locked;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        if (event.phase == TickEvent.Phase.START || player == null) return;
        ItemStack stack = player.getMainHandItem();
        if (stack.getItem() instanceof IEntityTrackingGun) {
            IEntityTrackingGun gun = (IEntityTrackingGun) stack.getItem();
            if (!gun.canBeGuided(player)) return;
            ++updateTimer;
            lockTimer = selectedEntity > -1 ? ++lockTimer : 0;
            locked = lockTimer >= gun.getLockTime();
            if (updateTimer >= 10) {
                updateTimer = 0;
                updateTargetedEntity(player, gun);
            }
        }
    }

    public static int getSelectedEntity() {
        return locked ? selectedEntity : -1;
    }

    private static void updateTargetedEntity(PlayerEntity player, IEntityTrackingGun trackingGun) {
        Vector3d position = player.position();
        Vector3d look = player.getLookAngle();
        int range = trackingGun.getMaxRange();
        Vector3d targetedLocation = position.add(look).multiply(range, range, range);
        World world = player.level;
        BlockRayTraceResult result = world.clip(new RayTraceContext(position, targetedLocation, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, player));
        if (result.getType() != RayTraceResult.Type.MISS) {
            targetedLocation = result.getLocation();
        }
        Predicate<LivingEntity> canTarget = target -> target != player && player.canSee(target);
        AxisAlignedBB searchArea = new AxisAlignedBB(position.x, position.y - 10, position.z, targetedLocation.x, targetedLocation.y + 10, targetedLocation.z);
        double x = targetedLocation.x;
        double y = targetedLocation.y;
        double z = targetedLocation.z;
        Optional<LivingEntity> potentialTarget = world.getEntitiesOfClass(LivingEntity.class, searchArea, canTarget).stream().min((e1, e2) -> sortTargets(e1, e2, position, x, y, z));
        selectedEntity = potentialTarget.map(Entity::getId).orElse(-1);
    }

    private static int sortTargets(LivingEntity e1, LivingEntity e2, Vector3d v1, double x, double y, double z) {
        int dist1 = getDistance(e1, v1, x, y, z);
        int dist2 = getDistance(e2, v1, x, y, z);
        return dist1 - dist2;
    }

    private static int getDistance(LivingEntity entity, Vector3d v1, double x2, double y2, double z2) {
        double x1 = v1.x;
        double y1 = v1.y;
        double z1 = v1.z;
        double a = getDistanceSqr(x2, y2, z2, entity.getX(), entity.getY(), entity.getZ());
        double b = getDistanceSqr(x1, y1, z1, entity.getX(), entity.getY(), entity.getZ());
        double c = getDistanceSqr(x1, y1, z1, x2, y2, z2);
        double alpha = (b * b + c * c - a * a) / (2 * b * c);
        double angle = Math.acos(alpha);
        return (int) (Math.sin(angle) * c);
    }

    private static double getDistanceSqr(double x1, double y1, double z1, double x2, double y2, double z2) {
        double x = x1 - x2;
        double y = y1 - y2;
        double z = z1 - z2;
        return x * x + y * y + z * z;
    }
}
