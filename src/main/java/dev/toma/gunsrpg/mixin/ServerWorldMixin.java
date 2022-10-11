package dev.toma.gunsrpg.mixin;

import dev.toma.gunsrpg.world.MobSpawnManager;
import dev.toma.gunsrpg.world.cap.WorldData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IAngerable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.profiler.IProfiler;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.DimensionType;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.ISpawnWorldInfo;
import net.minecraftforge.common.extensions.IForgeWorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World implements ISeedReader, IForgeWorldServer {

    public ServerWorldMixin(ISpawnWorldInfo p_i241925_1_, RegistryKey<World> p_i241925_2_, DimensionType p_i241925_3_, Supplier<IProfiler> p_i241925_4_, boolean p_i241925_5_, boolean p_i241925_6_, long p_i241925_7_) {
        super(p_i241925_1_, p_i241925_2_, p_i241925_3_, p_i241925_4_, p_i241925_5_, p_i241925_6_, p_i241925_7_);
    }

    @Inject(method = "addFreshEntity", at = @At("HEAD"), cancellable = true)
    public void gunsrpg_addFreshEntity(Entity entity, CallbackInfoReturnable<Boolean> ci) {
        if (entity instanceof MonsterEntity || entity instanceof IAngerable) {
            LivingEntity livingEntity = (LivingEntity) entity;
            ServerWorld world = (ServerWorld) (Object) this;
            boolean isBloodmoon = WorldData.isBloodMoon(world);
            if (!MobSpawnManager.instance().processSpawn(livingEntity, world, isBloodmoon)) {
                ci.setReturnValue(false);
            }
        }
    }
}
