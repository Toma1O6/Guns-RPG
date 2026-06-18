package com.wf.firearms.registry;

import com.wf.firearms.GunsRpg;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public final class ModTags {
    private ModTags() {}

    public static final class Entities {
        private Entities() {}

        public static final TagKey<EntityType<?>> BLOODMOON_DOOR_OPENING = TagKey.create(
                Registries.ENTITY_TYPE,
                ResourceLocation.fromNamespaceAndPath(GunsRpg.MOD_ID, "bloodmoon_door_opening"));
    }
}
