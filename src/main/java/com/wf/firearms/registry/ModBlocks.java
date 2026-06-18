package com.wf.firearms.registry;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.airdrop.AirdropBlock;
import com.wf.firearms.culinary.CulinaryTableBlock;
import com.wf.firearms.gunsmith.GunsmithTableBlock;
import com.wf.firearms.medical.MedicalStationTableBlock;
import com.wf.firearms.repair.RepairStationBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, GunsRpg.MOD_ID);
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, GunsRpg.MOD_ID);

    public static final RegistryObject<Block> GUNSMITH_TABLE = BLOCKS.register(
            "gunsmith_table",
            () -> new GunsmithTableBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(3.5f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)));

    public static final RegistryObject<Item> GUNSMITH_TABLE_ITEM =
            ITEMS.register("gunsmith_table", () -> new BlockItem(GUNSMITH_TABLE.get(), new Item.Properties()));

    public static final RegistryObject<Block> CULINARY_TABLE = BLOCKS.register(
            "culinary_table",
            () -> new CulinaryTableBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .strength(2.5f)
                    .sound(SoundType.WOOD)));

    public static final RegistryObject<Item> CULINARY_TABLE_ITEM =
            ITEMS.register("culinary_table", () -> new BlockItem(CULINARY_TABLE.get(), new Item.Properties()));

    public static final RegistryObject<Block> REPAIR_STATION = BLOCKS.register(
            "repair_station",
            () -> new RepairStationBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(3.2f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)
                    .noOcclusion()));

    public static final RegistryObject<Item> REPAIR_STATION_ITEM =
            ITEMS.register("repair_station", () -> new BlockItem(REPAIR_STATION.get(), new Item.Properties()));

    public static final RegistryObject<Block> MEDICAL_STATION = BLOCKS.register(
            "medical_station",
            () -> new MedicalStationTableBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.QUARTZ)
                    .strength(2.5f)
                    .sound(SoundType.METAL)
                    .noOcclusion()));

    public static final RegistryObject<Item> MEDICAL_STATION_ITEM =
            ITEMS.register("medical_station", () -> new BlockItem(MEDICAL_STATION.get(), new Item.Properties()));

    public static final RegistryObject<Block> AIRDROP = BLOCKS.register(
            "airdrop",
            () -> new AirdropBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(4.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)
                    .noOcclusion()));

    public static final RegistryObject<Item> AIRDROP_ITEM =
            ITEMS.register("airdrop", () -> new BlockItem(AIRDROP.get(), new Item.Properties()));

    private ModBlocks() {}
}
