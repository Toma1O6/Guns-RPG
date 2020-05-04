package dev.toma.gunsrpg.debuffs.effect;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.debuffs.Debuff;
import dev.toma.gunsrpg.debuffs.DebuffTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

public class PoisonDebuff extends Debuff {

    public static final DamageSource POISON_DAMAGE = new DamageSource("poison").setDamageBypassesArmor();
    private static final ResourceLocation ICON = GunsRPG.makeResource("textures/icons/poison.png");

    public PoisonDebuff() {
        super(DebuffTypes.POISON);
    }

    @Override
    public ResourceLocation getIconTexture() {
        return ICON;
    }
}
