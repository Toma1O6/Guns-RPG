package dev.toma.gunsrpg.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.item.guns.util.IEntityTrackingGun;
import lib.toma.animations.AnimationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

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
            locked = lockTimer >= gun.getLockTime();
            if (updateTimer >= 10) {
                updateTimer = 0;
                updateTargetedEntity(player, gun);
            }
        }
    }

    @SubscribeEvent
    public static void renderSelection(RenderWorldLastEvent event) {
        /*//if (selectedEntity == -1) return;
        Minecraft minecraft = Minecraft.getInstance();
        World world = minecraft.level;
        Entity entity = world.getEntity(selectedEntity);
        //if (entity == null) return;
        PlayerEntity player = minecraft.player;
        int range = 96;
        Vector3d pos = player.position();
        Vector3d look = rotateVectorYaw(player.yRot, new Vector3d(range, 0, 0));
        Vector3d target = pos.add(look);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuilder();
        RenderSystem.disableTexture();
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        builder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
        float partial = event.getPartialTicks();
        double x = MathHelper.lerp(partial, player.xo, pos.x);
        double y = MathHelper.lerp(partial, player.yo, pos.y);
        double z = MathHelper.lerp(partial, player.zo, pos.z);
        MatrixStack stack = event.getMatrixStack();
        stack.pushPose();
        stack.translate(-x, -y, -z);
        Matrix4f pose = stack.last().pose();
        builder.vertex(pose, (float) pos.x, (float) pos.y, (float) pos.z).color(1.0F, 0.0F, 0.0F, 1.0F).endVertex();
        builder.vertex(pose, (float) target.x, (float) target.y, (float) target.z).color(1.0F, 0.0F, 0.0F, 1.0F).endVertex();
        tessellator.end();
        stack.popPose();
        RenderSystem.enableTexture();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();*/
    }

    public static int getSelectedEntity() {
        return locked ? selectedEntity : -1;
    }

    private static void updateTargetedEntity(PlayerEntity player, IEntityTrackingGun trackingGun) {
        Vector3d position = player.position();
        Vector3d rotationVector = rotateVectorYaw(player.yRot, new Vector3d(trackingGun.getMaxRange(), 0, 0));
        Vector3d targetedLocation = position.add(rotationVector);
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
        System.out.println(selectedEntity);
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

    private static Vector3d rotateVectorYaw(double rotation, Vector3d vec) {
        float pi = (float) (Math.PI / 180.0F);
        return vec.yRot(-(float) rotation * pi - ((float) Math.PI / 2.0F));
    }
}
