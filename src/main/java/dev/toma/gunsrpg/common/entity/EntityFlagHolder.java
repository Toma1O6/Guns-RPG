package dev.toma.gunsrpg.common.entity;

public interface EntityFlagHolder {

    void gunsrpg$addFlag(EntityFlag flag);

    void gunsrpg$removeFlag(EntityFlag flag);

    void gunsrpg$clearFlags();

    boolean gunsrpg$hasFlag(EntityFlag flag);
}
