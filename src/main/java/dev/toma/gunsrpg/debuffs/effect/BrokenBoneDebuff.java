package dev.toma.gunsrpg.debuffs.effect;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.debuffs.Debuff;
import dev.toma.gunsrpg.debuffs.DebuffTypes;
import net.minecraft.util.ResourceLocation;

public class BrokenBoneDebuff extends Debuff {

    private static final ResourceLocation ICON = GunsRPG.makeResource("textures/icons/broken_bone.png");

    public BrokenBoneDebuff() {
        super(DebuffTypes.BROKEN_BONE);
    }

    @Override
    public ResourceLocation getIconTexture() {
        return ICON;
    }
}
