package dev.toma.gunsrpg.common.entity;

import java.util.Collection;

public interface EntityFlagHolder {

    void gunsrpg$addFlag(EntityFlag flag);

    void gunsrpg$removeFlag(EntityFlag flag);

    void gunsrpg$clearFlags();

    Collection<EntityFlag> getFlags();

    boolean gunsrpg$hasFlag(EntityFlag flag);
}
