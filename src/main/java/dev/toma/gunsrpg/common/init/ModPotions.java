package dev.toma.gunsrpg.common.init;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.util.Interval;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModPotions {

    public static final DeferredRegister<Potion> REGISTER = DeferredRegister.create(ForgeRegistries.POTION_TYPES, GunsRPG.MODID);

    public static final RegistryObject<Potion> ZOMBIE_NIGHTMARE_DEFENSIVE_POTION = REGISTER.register("zombie_nightmare_defensive", () -> new Potion(
            new EffectInstance(Effects.BLINDNESS, Interval.seconds(60).getTicks()),
            new EffectInstance(Effects.CONFUSION, Interval.seconds(15).getTicks())
    ));

    public static void subscribe(IEventBus bus) {
        REGISTER.register(bus);
    }
}
