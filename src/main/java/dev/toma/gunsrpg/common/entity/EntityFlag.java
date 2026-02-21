package dev.toma.gunsrpg.common.entity;

import net.minecraft.entity.Entity;

public enum EntityFlag {

    QUEST_ARENA,
    BLOODMOON;

    public static boolean hasFlag(Entity entity, EntityFlag flag) {
        if (!(entity instanceof EntityFlagHolder))
            return false;
        EntityFlagHolder holder = (EntityFlagHolder) entity;
        return holder.gunsrpg$hasFlag(flag);
    }

    public static void addFlag(Entity entity, EntityFlag flag) {
        if (!(entity instanceof EntityFlagHolder))
            return;
        EntityFlagHolder holder = (EntityFlagHolder) entity;
        holder.gunsrpg$addFlag(flag);
    }
}
