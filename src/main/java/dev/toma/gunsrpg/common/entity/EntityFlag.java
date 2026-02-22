package dev.toma.gunsrpg.common.entity;

import net.minecraft.entity.Entity;

import java.util.Collection;
import java.util.Collections;

public enum EntityFlag {

    QUEST_ARENA,
    BLOODMOON;

    public static boolean hasFlag(Entity entity, EntityFlag flag) {
        if (!(entity instanceof EntityFlagHolder))
            return false;
        EntityFlagHolder holder = (EntityFlagHolder) entity;
        return holder.gunsrpg$hasFlag(flag);
    }

    public static Collection<EntityFlag> listFlags(Entity entity) {
        if (!(entity instanceof EntityFlagHolder))
            return Collections.emptyList();
        return ((EntityFlagHolder) entity).gunsrpg$getFlags();
    }

    public static void addFlag(Entity entity, EntityFlag flag) {
        if (!(entity instanceof EntityFlagHolder))
            return;
        EntityFlagHolder holder = (EntityFlagHolder) entity;
        holder.gunsrpg$addFlag(flag);
    }
}
