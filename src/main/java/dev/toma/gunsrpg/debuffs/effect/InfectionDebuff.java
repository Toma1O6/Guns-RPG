package dev.toma.gunsrpg.debuffs.effect;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.debuffs.Debuff;
import dev.toma.gunsrpg.debuffs.DebuffTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

public class InfectionDebuff extends Debuff {

    public static final DamageSource INFECTION_DAMAGE = new DamageSource("infection").setDamageBypassesArmor().setDamageIsAbsolute();
    private static final ResourceLocation INFECTION = GunsRPG.makeResource("textures/icons/infection.png");

    public InfectionDebuff() {
        super(DebuffTypes.INFECTION);
    }

    @Override
    public ResourceLocation getIconTexture() {
        return INFECTION;
    }
}
