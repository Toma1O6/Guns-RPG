package dev.toma.gunsrpg.common.init;

import dev.toma.gunsrpg.common.debuffs.DebuffType;
import dev.toma.gunsrpg.common.debuffs.event.DebuffStageEventType;
import dev.toma.gunsrpg.common.debuffs.sources.DebuffSourceType;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.SimpleRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public final class ModRegistries {

    public static IForgeRegistry<SkillType<?>> SKILLS;
    public static IForgeRegistry<DebuffType<?>> DEBUFFS;

    public static final SimpleRegistry<DebuffStageEventType<?>> DEBUFF_STAGE_EVENT_TYPES = new SimpleRegistry<>("Debuff stage event type");
    public static final SimpleRegistry<DebuffSourceType<?>> DEBUFF_SOURCE_TYPES = new SimpleRegistry<>("Debuff source type");
}
