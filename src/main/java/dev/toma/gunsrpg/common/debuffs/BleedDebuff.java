package dev.toma.gunsrpg.common.debuffs;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.init.Debuffs;
import net.minecraft.util.ResourceLocation;

public class BleedDebuff extends Debuff {

    private static final ResourceLocation ICON = GunsRPG.makeResource("textures/icons/bleed.png");

    public BleedDebuff() {
        super(Debuffs.BLEEDING);
    }

    @Override
    protected ResourceLocation getIconTexture() {
        return ICON;
    }
}
