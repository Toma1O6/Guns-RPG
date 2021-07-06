package dev.toma.gunsrpg.common.init;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.debuffs.Debuff;
import dev.toma.gunsrpg.common.debuffs.DebuffType;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(GunsRPG.MODID)
public class Debuffs {
    public static final DebuffType<Debuff.Poison> POISON = null;
    public static final DebuffType<Debuff.Infection> INFECTION = null;
    public static final DebuffType<Debuff.Fracture> FRACTURE = null;
    public static final DebuffType<Debuff.Bleeding> BLEEDING = null;
}
