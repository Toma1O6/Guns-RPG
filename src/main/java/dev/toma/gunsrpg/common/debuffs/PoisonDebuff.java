package dev.toma.gunsrpg.common.debuffs;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.init.Debuffs;
import net.minecraft.util.ResourceLocation;

public class PoisonDebuff extends Debuff {

    private static final ResourceLocation ICON = GunsRPG.makeResource("textures/icons/poison.png");

    public PoisonDebuff() {
        super(Debuffs.POISON);
    }

    @Override
    protected ResourceLocation getIconTexture() {
        return ICON;
    }
}
