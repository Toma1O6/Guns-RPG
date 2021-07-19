package dev.toma.gunsrpg.common.debuffs;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.init.Debuffs;
import net.minecraft.util.ResourceLocation;

public class FractureDebuff extends Debuff {

    private static final ResourceLocation ICON = GunsRPG.makeResource("textures/icons/fracture.png");

    public FractureDebuff() {
        super(Debuffs.FRACTURE);
    }

    @Override
    protected ResourceLocation getIconTexture() {
        return ICON;
    }
}
