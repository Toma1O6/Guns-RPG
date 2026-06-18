package com.wf.firearms.mob;

import com.wf.firearms.GunsRpg;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

import java.lang.reflect.Method;
import java.util.OptionalInt;

/** 可选读取 L2 Hostility 区块难度；类不存在或调用失败则返回 empty。 */
public final class L2HostilityBridge {
    private static Method sectionDifficultyMethod;
    private static boolean lookupDone;
    private static boolean available;

    private L2HostilityBridge() {}

    public static OptionalInt sectionDifficulty(ServerLevel level, BlockPos pos) {
        if (!lookupDone) {
            lookupDone = true;
            resolveMethod();
        }
        if (!available || sectionDifficultyMethod == null) {
            return OptionalInt.empty();
        }
        try {
            Object result = sectionDifficultyMethod.invoke(null, level, pos);
            if (result instanceof Integer i) {
                return OptionalInt.of(Math.max(0, i));
            }
            if (result instanceof Number n) {
                return OptionalInt.of(Math.max(0, n.intValue()));
            }
        } catch (ReflectiveOperationException ex) {
            available = false;
            GunsRpg.LOGGER.debug("[gunsrpg] L2 Hostility 难度读取失败，已禁用: {}", ex.toString());
        }
        return OptionalInt.empty();
    }

    private static void resolveMethod() {
        String[] classNames = {
            "karashokleo.l2hostility.content.logic.DifficultyCalculator",
            "karashokleo.l2hostility.content.logic.SectionDifficultyCalculator",
            "karashokleo.l2hostility.content.component.chunk.ChunkDifficultyComponent"
        };
        String[] methodNames = {"getDifficulty", "getSectionDifficulty", "getLevelAt"};
        for (String cn : classNames) {
            try {
                Class<?> clazz = Class.forName(cn);
                for (String mn : methodNames) {
                    try {
                        sectionDifficultyMethod = clazz.getMethod(mn, ServerLevel.class, BlockPos.class);
                        available = true;
                        GunsRpg.LOGGER.info("[gunsrpg] L2 Hostility 难度桥接: {}.{}", cn, mn);
                        return;
                    } catch (NoSuchMethodException ignored) {
                        // try next
                    }
                }
            } catch (ClassNotFoundException ignored) {
                // try next
            }
        }
        GunsRpg.LOGGER.info("[gunsrpg] 未找到 L2 Hostility 难度 API，mob 缩放仅用原版区域难度");
    }
}
