package com.wf.firearms.compat;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;

/** TaCZ 运行时检测（不 compileOnly 依赖 tacz jar）。 */
public final class TaczCompat {
    private TaczCompat() {}

    public static boolean isTaczLoaded() {
        return net.minecraftforge.fml.ModList.get().isLoaded("tacz");
    }

    public static boolean isTaczBulletDamage(DamageSource source) {
        return source.typeHolder()
                .unwrapKey()
                .map(key -> {
                    ResourceLocation id = key.location();
                    return "tacz".equals(id.getNamespace())
                            && (id.getPath().startsWith("bullet") || id.getPath().contains("bullet"));
                })
                .orElse(false);
    }
}
