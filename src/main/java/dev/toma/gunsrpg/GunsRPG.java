package dev.toma.gunsrpg;

import dev.toma.gunsrpg.client.gui.skills.SkillTreePlacement;
import dev.toma.gunsrpg.common.CommonEventHandler;
import dev.toma.gunsrpg.common.ModRegistry;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.PlayerDataStorage;
import dev.toma.gunsrpg.common.command.CommandGRPG;
import dev.toma.gunsrpg.common.entity.EntityGoldDragon;
import dev.toma.gunsrpg.common.entity.EntityRocketAngel;
import dev.toma.gunsrpg.common.item.guns.ammo.ItemAmmo;
import dev.toma.gunsrpg.common.tileentity.TileEntityAirdrop;
import dev.toma.gunsrpg.common.tileentity.TileEntityBlastFurnace;
import dev.toma.gunsrpg.common.tileentity.TileEntitySmithingTable;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.sided.SideManager;
import dev.toma.gunsrpg.util.GuiHandler;
import dev.toma.gunsrpg.util.object.EntitySpawnManager;
import dev.toma.gunsrpg.util.recipes.BlastFurnaceRecipe;
import dev.toma.gunsrpg.util.recipes.SmithingTableRecipes;
import dev.toma.gunsrpg.world.cap.WorldDataCap;
import dev.toma.gunsrpg.world.cap.WorldDataFactory;
import dev.toma.gunsrpg.world.cap.WorldDataStorage;
import dev.toma.gunsrpg.world.ore.WorldOreGen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Mod(modid = GunsRPG.MODID, name = "Guns RPG", version = "1.0.0", acceptedMinecraftVersions = "[1.12.2]", updateJSON = "https://raw.githubusercontent.com/Toma1O6/Guns-RPG/master/update.json")
public class GunsRPG {

    public static final String MODID = "gunsrpg";
    public static Logger log = LogManager.getLogger();
    @SidedProxy(modId = MODID, clientSide = "dev.toma.gunsrpg.sided.ClientSideManager", serverSide = "dev.toma.gunsrpg.sided.ServerSideManager")
    public static SideManager sideManager;
    @Mod.Instance
    public static GunsRPG modInstance;
    public static Map<Item, Item> oreToChunkMap = new HashMap<>();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        sideManager.preInit(event);
        NetworkManager.init();
        CapabilityManager.INSTANCE.register(PlayerData.class, new PlayerDataStorage(), PlayerDataFactory::new);
        CapabilityManager.INSTANCE.register(WorldDataCap.class, new WorldDataStorage(), WorldDataFactory::new);
        NetworkRegistry.INSTANCE.registerGuiHandler(modInstance, new GuiHandler());
        GameRegistry.registerTileEntity(TileEntityBlastFurnace.class, makeResource("blast_furnace"));
        GameRegistry.registerTileEntity(TileEntityAirdrop.class, makeResource("airdrop"));
        GameRegistry.registerTileEntity(TileEntitySmithingTable.class, makeResource("smithing_table"));
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        sideManager.init(event);
        GameRegistry.registerWorldGenerator(new WorldOreGen(), 0);
        SkillTreePlacement.generatePlacement();
        SmithingTableRecipes.register();
        ItemAmmo.init();
        initOresToChunks();
        GameRegistry.addSmelting(ModRegistry.GRPGItems.IRON_ORE_CHUNK, new ItemStack(Items.IRON_INGOT), 0.7F);
        GameRegistry.addSmelting(ModRegistry.GRPGItems.GOLD_ORE_CHUNK, new ItemStack(Items.GOLD_INGOT), 1.0F);
        LootTableList.register(makeResource("inject/dungeon_inject"));
        ForgeRegistries.ITEMS.getValuesCollection()
                .stream()
                .filter(it -> it instanceof ItemPickaxe)
                .map(i -> (ItemPickaxe) i)
                .forEach(pickaxe -> pickaxe.effectiveBlocks.add(ModRegistry.GRPGBlocks.AMETHYST_ORE)
        );
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        sideManager.postInit(event);
        BlastFurnaceRecipe.init();
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandGRPG());
        CommonEventHandler.HEALTH_MAP.clear();
        World world = event.getServer().getWorld(0);
        for(EntityEntry entry : ForgeRegistries.ENTITIES) {
            Class<? extends Entity> cls = entry.getEntityClass();
            if(cls.equals(EntitySlime.class) || cls.equals(EntityRocketAngel.class) || cls.equals(EntityDragon.class) || cls.equals(EntityGoldDragon.class) || cls.equals(EntityWither.class)) continue;
            Entity entity = EntityList.newEntity(entry.getEntityClass(), world);
            if(entity instanceof EntityLivingBase && !(entity instanceof EntityPlayer)) {
                EntityLivingBase e = (EntityLivingBase) entity;
                double health = e.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue();
                EntitySpawnManager manager = new EntitySpawnManager(health);
                Consumer<Entity> customAction = EntitySpawnManager.getManager(entry.getEntityClass());
                if(customAction != null) {
                    manager.setAction(customAction);
                }
                CommonEventHandler.HEALTH_MAP.put(entry.getEntityClass(), manager);
            }
        }
    }

    public static ResourceLocation makeResource(String path) {
        return new ResourceLocation(MODID, path);
    }

    private static void initOresToChunks() {
        oreToChunkMap.put(Item.getItemFromBlock(Blocks.IRON_ORE), ModRegistry.GRPGItems.IRON_ORE_CHUNK);
        oreToChunkMap.put(Item.getItemFromBlock(Blocks.GOLD_ORE), ModRegistry.GRPGItems.GOLD_ORE_CHUNK);
    }
}
