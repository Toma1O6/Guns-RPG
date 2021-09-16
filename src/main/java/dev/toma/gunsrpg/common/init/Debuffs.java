package dev.toma.gunsrpg.common.init;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.debuffs.RespawnDebuffType;
import dev.toma.gunsrpg.common.debuffs.StagedDebuffType;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(GunsRPG.MODID)
public class Debuffs {
    public static final StagedDebuffType<?>     POISON      = null;
    public static final StagedDebuffType<?>     INFECTION   = null;
    public static final StagedDebuffType<?>     FRACTURE    = null;
    public static final StagedDebuffType<?>     BLEED       = null;
    public static final RespawnDebuffType<?>    RESPAWN     = null;
}
