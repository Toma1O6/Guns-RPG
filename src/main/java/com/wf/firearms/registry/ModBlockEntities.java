package com.wf.firearms.registry;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.airdrop.AirdropBlockEntity;
import com.wf.firearms.culinary.CulinaryTableBlockEntity;
import com.wf.firearms.gunsmith.GunsmithTableBlockEntity;
import com.wf.firearms.medical.MedicalStationTableBlockEntity;
import com.wf.firearms.repair.RepairStationBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, GunsRpg.MOD_ID);

    public static final RegistryObject<BlockEntityType<GunsmithTableBlockEntity>> GUNSMITH_TABLE =
            BLOCK_ENTITIES.register(
                    "gunsmith_table",
                    () -> BlockEntityType.Builder.of(
                                    GunsmithTableBlockEntity::new, ModBlocks.GUNSMITH_TABLE.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<CulinaryTableBlockEntity>> CULINARY_TABLE =
            BLOCK_ENTITIES.register(
                    "culinary_table",
                    () -> BlockEntityType.Builder.of(
                                    CulinaryTableBlockEntity::new, ModBlocks.CULINARY_TABLE.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<RepairStationBlockEntity>> REPAIR_STATION =
            BLOCK_ENTITIES.register(
                    "repair_station",
                    () -> BlockEntityType.Builder.of(
                                    RepairStationBlockEntity::new, ModBlocks.REPAIR_STATION.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<MedicalStationTableBlockEntity>> MEDICAL_STATION =
            BLOCK_ENTITIES.register(
                    "medical_station",
                    () -> BlockEntityType.Builder.of(
                                    MedicalStationTableBlockEntity::new, ModBlocks.MEDICAL_STATION.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<AirdropBlockEntity>> AIRDROP =
            BLOCK_ENTITIES.register(
                    "airdrop",
                    () -> BlockEntityType.Builder.of(
                                    AirdropBlockEntity::new, ModBlocks.AIRDROP.get())
                            .build(null));

    private ModBlockEntities() {}
}
