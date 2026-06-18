package com.wf.firearms.debuff;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import com.wf.firearms.GunsRpg;

public enum DebuffType {
    BLEED("bleed", "debuff.gunsrpg.bleed", "textures/icons/bleed.png"),
    FRACTURE("fracture", "debuff.gunsrpg.fracture", "textures/icons/fracture.png"),
    POISON("poison", "debuff.gunsrpg.poison", "textures/icons/poison.png"),
    INFECTION("infection", "debuff.gunsrpg.infection", "textures/icons/infection.png");

    private final String id;
    private final String translationKey;
    private final ResourceLocation icon;

    DebuffType(String id, String translationKey, String iconPath) {
        this.id = id;
        this.translationKey = translationKey;
        this.icon = ResourceLocation.fromNamespaceAndPath(GunsRpg.MOD_ID, iconPath);
    }

    public String getId() {
        return id;
    }

    public Component displayName() {
        return Component.translatable(translationKey);
    }

    public ResourceLocation icon() {
        return icon;
    }

    public static DebuffType fromId(String raw) {
        for (DebuffType t : values()) {
            if (t.id.equals(raw)) {
                return t;
            }
        }
        return null;
    }
}
