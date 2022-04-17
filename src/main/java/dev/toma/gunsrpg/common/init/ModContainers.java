package dev.toma.gunsrpg.common.init;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.container.*;
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

    public static final RegistryObject<ContainerType<CrateContainer<?>>> CRATE = register("crate", CrateContainer::new);
    public static final RegistryObject<ContainerType<BlastFurnaceContainer>> BLAST_FURNACE = register("blast_furnace", BlastFurnaceContainer::new);
    public static final RegistryObject<ContainerType<DeathCrateContainer>> DEATH_CRATE = register("death_crate", DeathCrateContainer::new);
    public static final RegistryObject<ContainerType<SmithingTableContainer>> SMITHING_TABLE = register("smithing_table", SmithingTableContainer::new);
    public static final RegistryObject<ContainerType<LunchBoxContainer>> LUNCH_BOX = register("lunch_box", LunchBoxContainer::new);
    public static final RegistryObject<ContainerType<AmmoCaseContainer>> AMMO_CASE = register("ammo_case", AmmoCaseContainer::new);
    public static final RegistryObject<ContainerType<GrenadeCaseContainer>> GRENADE_CASE = register("grenade_case", GrenadeCaseContainer::new);
    public static final RegistryObject<ContainerType<MedsCaseContainer>> MEDS_CASE = register("meds_case", MedsCaseContainer::new);
    public static final RegistryObject<ContainerType<ItemCaseContainer>> ITEM_CASE = register("item_case", ItemCaseContainer::new);
    public static final RegistryObject<ContainerType<CrystalCaseContainer>> CRYSTAL_CASE = register("crystal_case", CrystalCaseContainer::new);
    public static final RegistryObject<ContainerType<CookerContainer>> COOKER = register("cooker", CookerContainer::new);
    public static final RegistryObject<ContainerType<CulinaryTableContainer>> CULINARY_TABLE = register("culinary_table", CulinaryTableContainer::new);
    public static final RegistryObject<ContainerType<MedstationContainer>> MEDSTATION = register("medstation", MedstationContainer::new);
    public static final RegistryObject<ContainerType<RepairStationContainer>> REPAIR_STATION = register("repair_station", RepairStationContainer::new);
    public static final RegistryObject<ContainerType<CrystalStationContainer>> CRYSTAL_STATION = register("crystal_station", CrystalStationContainer::new);
    public static final RegistryObject<ContainerType<CrystalFusionStationContainer>> CRYSTAL_FUSE = register("crystal_fuse", CrystalFusionStationContainer::new);

    private static <C extends Container> RegistryObject<ContainerType<C>> register(String name, IContainerFactory<C> factory) {
        return TYPES.register(name, () -> IForgeContainerType.create(factory));
    }

    public static void subscribe(IEventBus bus) {
        TYPES.register(bus);
    }
}
