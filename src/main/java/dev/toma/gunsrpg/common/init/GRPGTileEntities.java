package dev.toma.gunsrpg.common.init;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.tileentity.AirdropTileEntity;
import dev.toma.gunsrpg.common.tileentity.BlastFurnaceTileEntity;
import dev.toma.gunsrpg.common.tileentity.DeathCrateTileEntity;
import dev.toma.gunsrpg.common.tileentity.SmithingTableTileEntity;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class GRPGTileEntities {
    private static final DeferredRegister<TileEntityType<?>> TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, GunsRPG.MODID);

    public static final RegistryObject<TileEntityType<AirdropTileEntity>> AIRDROP = register("airdrop", AirdropTileEntity::new, GRPGBlocks.AIRDROP);
    public static final RegistryObject<TileEntityType<BlastFurnaceTileEntity>> BLAST_FURNACE = register("blast_furnace", BlastFurnaceTileEntity::new, GRPGBlocks.BLAST_FURNACE);
    public static final RegistryObject<TileEntityType<DeathCrateTileEntity>> DEATH_CRATE = register("death_crate", DeathCrateTileEntity::new, GRPGBlocks.DEATH_CRATE);
    public static final RegistryObject<TileEntityType<SmithingTableTileEntity>> SMITHING_TABLE = register("smithing_table", SmithingTableTileEntity::new, GRPGBlocks.SMITHING_TABLE);

    private static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(String name, Supplier<T> supplier, Block... blocks) {
        return TYPES.register(name, () -> TileEntityType.Builder.of(supplier, blocks).build(null));
    }

    public static void subscribe(IEventBus bus) {
        TYPES.register(bus);
    }
}
