package dev.toma.gunsrpg.common.init;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.StunEffect;
import net.minecraft.potion.Effect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModEffects {

    private static final DeferredRegister<Effect> REGISTER = DeferredRegister.create(ForgeRegistries.POTIONS, GunsRPG.MODID);

    public static final RegistryObject<StunEffect> STUN = REGISTER.register("stun", StunEffect::new);

    public static void subscribe(IEventBus eventBus) {
        REGISTER.register(eventBus);
    }
}
