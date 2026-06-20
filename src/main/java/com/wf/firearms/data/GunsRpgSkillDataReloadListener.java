package com.wf.firearms.data;

import com.wf.firearms.GunsRpg;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * 数据包重载时刷新技能树（支持 datapack 覆盖 jar 内默认 {@code data/gunsrpg/}）。
 */
@Mod.EventBusSubscriber(modid = GunsRpg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GunsRpgSkillDataReloadListener {
    private GunsRpgSkillDataReloadListener() {}

    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new PreparableReloadListener() {
            @Override
            public CompletableFuture<Void> reload(
                    PreparationBarrier barrier,
                    ResourceManager resourceManager,
                    ProfilerFiller preparationsProfiler,
                    ProfilerFiller reloadProfiler,
                    Executor backgroundExecutor,
                    Executor gameExecutor) {
                return barrier.wait(CompletableFuture.completedFuture(null))
                        .thenRunAsync(() -> SkillDatabase.reload(), gameExecutor);
            }

            @Override
            public String getName() {
                return GunsRpg.MOD_ID + ":skill_data";
            }
        });
    }
}
