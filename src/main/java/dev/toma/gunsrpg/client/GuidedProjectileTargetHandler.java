package dev.toma.gunsrpg.client;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.item.guns.util.IEntityTrackingGun;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
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
            if (!gun.canBeGuided(player)) {
                selectedEntity = -1;
                return;
            }
            ++updateTimer;
            lockTimer = selectedEntity > -1 ? ++lockTimer : 0;
            boolean wasLocked = locked;
            locked = lockTimer >= gun.getLockTime();
            if (!wasLocked && locked) {
                player.playSound(ModSounds.RL_LOCKED1, 0.6F, 1.0F);
            }
            if (updateTimer >= 5) {
                updateTimer = 0;
                updateTargetedEntity(player, gun);
            }
        }
    }

    public static int getSelectedEntity() {
        return locked ? selectedEntity : -1;
    }

    private static void updateTargetedEntity(PlayerEntity player, IEntityTrackingGun trackingGun) {
        int range = trackingGun.getMaxRange();
        Vector3d position = player.getEyePosition(1.0F);
        Vector3d look = player.getLookAngle();
        Vector3d targetedLocation = position.add(look.x * range, look.y * range, look.z * range);
        AxisAlignedBB axisalignedbb = player.getBoundingBox().expandTowards(look.scale(range)).inflate(1.0D, 1.0D, 1.0D);
        Predicate<Entity> canTarget = target -> target != player && target instanceof LivingEntity && player.canSee(target);
        EntityRayTraceResult result = getEntityHitResult(player, position, targetedLocation, axisalignedbb, canTarget, range * range);
        boolean valid = result != null && result.getEntity() != null;
        if (valid) {
            Entity entity = result.getEntity();
            int lastId = selectedEntity;
            selectedEntity = entity.getId();
            if (lastId != -1 && lastId != selectedEntity) {
                locked = false;
                lockTimer = 0;
            }
            ITextComponent component = new StringTextComponent(locked ? TextFormatting.RED + ("Locked on: " + entity.getName().getString()) : TextFormatting.YELLOW + ("Locking on: " + entity.getName().getString()));
            Minecraft.getInstance().gui.handleChat(ChatType.GAME_INFO, component, Util.NIL_UUID);
        } else {
            selectedEntity = -1;
        }
    }

    private static EntityRayTraceResult getEntityHitResult(Entity shooter, Vector3d vec1, Vector3d vec2, AxisAlignedBB aabb, Predicate<Entity> predicate, double range) {
        World world = shooter.level;
        double d0 = range;
        Entity entity = null;
        Vector3d vector3d = null;
        for(Entity entity1 : world.getEntities(shooter, aabb, predicate)) {
            AxisAlignedBB axisalignedbb = entity1.getBoundingBox().inflate(3.0);
            Optional<Vector3d> optional = axisalignedbb.clip(vec1, vec2);
            if (axisalignedbb.contains(vec1)) {
                if (d0 >= 0.0D) {
                    entity = entity1;
                    vector3d = optional.orElse(vec1);
                    d0 = 0.0D;
                }
            } else if (optional.isPresent()) {
                Vector3d vector3d1 = optional.get();
                double d1 = vec1.distanceToSqr(vector3d1);
                if (d1 < d0 || d0 == 0.0D) {
                    if (entity1.getRootVehicle() == shooter.getRootVehicle() && !entity1.canRiderInteract()) {
                        if (d0 == 0.0D) {
                            entity = entity1;
                            vector3d = vector3d1;
                        }
                    } else {
                        entity = entity1;
                        vector3d = vector3d1;
                        d0 = d1;
                    }
                }
            }
        }
        return entity == null ? null : new EntityRayTraceResult(entity, vector3d);
    }
}
