package com.wf.firearms.registry;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.airdrop.AirdropBlockEntity;
import com.wf.firearms.airdrop.AirdropMenu;
import com.wf.firearms.culinary.CulinaryMenu;
import com.wf.firearms.gunsmith.GunsmithMenu;
import com.wf.firearms.medical.MedicalStationMenu;
import com.wf.firearms.repair.RepairStationMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, GunsRpg.MOD_ID);

    public static final RegistryObject<MenuType<GunsmithMenu>> GUNSMITH_TABLE = MENUS.register(
            "gunsmith_table", () -> IForgeMenuType.create(GunsmithMenu::fromNetwork));

    public static final RegistryObject<MenuType<CulinaryMenu>> CULINARY_TABLE = MENUS.register(
            "culinary_table", () -> IForgeMenuType.create(CulinaryMenu::fromNetwork));

    public static final RegistryObject<MenuType<RepairStationMenu>> REPAIR_STATION = MENUS.register(
            "repair_station", () -> IForgeMenuType.create(RepairStationMenu::fromNetwork));

    public static final RegistryObject<MenuType<MedicalStationMenu>> MEDICAL_STATION = MENUS.register(
            "medical_station", () -> IForgeMenuType.create(MedicalStationMenu::fromNetwork));

    public static final RegistryObject<MenuType<AirdropMenu>> AIRDROP = MENUS.register(
            "airdrop", () -> IForgeMenuType.create(AirdropMenu::fromNetwork));

    private ModMenuTypes() {}
}
