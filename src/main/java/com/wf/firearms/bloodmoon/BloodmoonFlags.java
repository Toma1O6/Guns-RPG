package com.wf.firearms.bloodmoon;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;

/** 标记血月期间生成/替换的生物。 */
public final class BloodmoonFlags {
    public static final String NBT_KEY = "gunsrpg_bloodmoon";

    private BloodmoonFlags() {}

    public static void mark(LivingEntity entity) {
        CompoundTag data = entity.getPersistentData();
        data.putBoolean(NBT_KEY, true);
    }

    public static boolean isMarked(LivingEntity entity) {
        return entity.getPersistentData().getBoolean(NBT_KEY);
    }
}
