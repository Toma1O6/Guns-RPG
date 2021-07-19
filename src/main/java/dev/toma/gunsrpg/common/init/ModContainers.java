package dev.toma.gunsrpg.common.init;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.container.AirdropContainer;
import dev.toma.gunsrpg.common.container.BlastFurnaceContainer;
import dev.toma.gunsrpg.common.container.DeathCrateContainer;
import dev.toma.gunsrpg.common.container.SmithingTableContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainers {
    private static final DeferredRegister<ContainerType<?>> TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, GunsRPG.MODID);

    public static final RegistryObject<ContainerType<AirdropContainer>> AIRDROP = register("airdrop", AirdropContainer::new);
    public static final RegistryObject<ContainerType<BlastFurnaceContainer>> BLAST_FURNACE = register("blast_furnace", BlastFurnaceContainer::new);
    public static final RegistryObject<ContainerType<DeathCrateContainer>> DEATH_CRATE = register("death_crate", DeathCrateContainer::new);
    public static final RegistryObject<ContainerType<SmithingTableContainer>> SMITHING_TABLE = register("smithing_table", SmithingTableContainer::new);

    private static <C extends Container> RegistryObject<ContainerType<C>> register(String name, IContainerFactory<C> factory) {
        return TYPES.register(name, () -> IForgeContainerType.create(factory));
    }

    public static void subscribe(IEventBus bus) {
        TYPES.register(bus);
    }
}
