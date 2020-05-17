package dev.toma.gunsrpg.debuffs.effect;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.debuffs.Debuff;
import dev.toma.gunsrpg.debuffs.DebuffTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

public class BleedDebuff extends Debuff {

    public static final DamageSource BLEED_DAMAGE = new DamageSource("bleeding").setDamageBypassesArmor().setDamageIsAbsolute();
    private static final ResourceLocation BLEED = GunsRPG.makeResource("textures/icons/bleed.png");

    public BleedDebuff() {
        super(DebuffTypes.BLEED);
    }

    @Override
    public ResourceLocation getIconTexture() {
        return BLEED;
    }
}
