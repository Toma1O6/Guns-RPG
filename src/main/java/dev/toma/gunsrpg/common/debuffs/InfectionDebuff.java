package dev.toma.gunsrpg.common.debuffs;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.init.Debuffs;
import net.minecraft.util.ResourceLocation;

public class InfectionDebuff extends Debuff {

    private static final ResourceLocation ICON = GunsRPG.makeResource("textures/icons/infection.png");

    public InfectionDebuff() {
        super(Debuffs.INFECTION);
    }

    @Override
    protected ResourceLocation getIconTexture() {
        return ICON;
    }
}
