package dev.toma.gunsrpg.common.init;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.debuffs.DataDrivenDebuffType;
import dev.toma.gunsrpg.common.debuffs.DummyDebuffType;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(GunsRPG.MODID)
public class Debuffs {
    public static final DataDrivenDebuffType<?> POISON      = null;
    public static final DataDrivenDebuffType<?> INFECTION   = null;
    public static final DataDrivenDebuffType<?> FRACTURE    = null;
    public static final DataDrivenDebuffType<?> BLEED       = null;
    public static final DummyDebuffType<?> POISON_BLOCK     = null;
    public static final DummyDebuffType<?> INFECTION_BLOCK  = null;
    public static final DummyDebuffType<?> FRACTURE_BLOCK   = null;
    public static final DummyDebuffType<?> BLEED_BLOCK      = null;
}
