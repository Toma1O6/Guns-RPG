package dev.toma.gunsrpg.common.init;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.BaseEffect;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @deprecated Move to attribute system
 */
@Deprecated
public class ModEffects {
    private static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, GunsRPG.MODID);
    public static final RegistryObject<Effect> GUN_DAMAGE_BUFF = EFFECTS.register("gun_damage_buff", () -> new BaseEffect(EffectType.BENEFICIAL, 0xff1111));

    public static void subscribe(IEventBus bus) {
        EFFECTS.register(bus);
    }
}
