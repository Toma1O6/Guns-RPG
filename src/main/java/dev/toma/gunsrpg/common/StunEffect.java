package dev.toma.gunsrpg.common;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;

public final class StunEffect extends Effect {

    public StunEffect() {
        super(EffectType.HARMFUL, 0xF2AD0C);
    }

    @Override
    public boolean isDurationEffectTick(int p_76397_1_, int p_76397_2_) {
        return false;
    }
}
