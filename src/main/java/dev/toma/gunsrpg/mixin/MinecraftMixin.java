package dev.toma.gunsrpg.mixin;

import dev.toma.gunsrpg.api.common.data.IQuestingData;
import dev.toma.gunsrpg.world.cap.QuestingDataProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IWindowEventListener;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.profiler.ISnooperInfo;
import net.minecraft.util.concurrent.RecursiveEventLoop;
import net.minecraftforge.client.extensions.IForgeMinecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin extends RecursiveEventLoop<Runnable> implements ISnooperInfo, IWindowEventListener, IForgeMinecraft {

    @Shadow @Nullable public ClientWorld level;

    public MinecraftMixin(String p_i50401_1_) {
        super(p_i50401_1_);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;tick(Ljava/util/function/BooleanSupplier;)V", shift = At.Shift.AFTER))
    private void gunsrpg$tickWorld(CallbackInfo ci) {
        QuestingDataProvider.getData(this.level).ifPresent(IQuestingData::tickQuests);
    }
}
