package dev.toma.gunsrpg.common.init;

import dev.toma.gunsrpg.common.debuffs.DebuffType;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import net.minecraftforge.registries.IForgeRegistry;

public class GunsRPGRegistries {

    public static IForgeRegistry<SkillType<?>> SKILLS;
    public static IForgeRegistry<DebuffType> DEBUFFS;
}
