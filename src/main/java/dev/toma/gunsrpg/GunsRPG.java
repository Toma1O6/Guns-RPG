package dev.toma.gunsrpg;

import dev.toma.gunsrpg.common.CommonEventHandler;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.PlayerDataStorage;
import dev.toma.gunsrpg.common.command.CommandGRPG;
import dev.toma.gunsrpg.common.item.guns.ammo.ItemAmmo;
import dev.toma.gunsrpg.common.skilltree.SkillTreeEntry;
import dev.toma.gunsrpg.common.tileentity.TileEntityAirdrop;
import dev.toma.gunsrpg.common.tileentity.TileEntityBlastFurnace;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.sided.SideManager;
import dev.toma.gunsrpg.util.GuiHandler;
import dev.toma.gunsrpg.util.object.EntitySpawnManager;
import dev.toma.gunsrpg.util.recipes.BlastFurnaceRecipe;
import dev.toma.gunsrpg.world.cap.WorldDataCap;
import dev.toma.gunsrpg.world.cap.WorldDataFactory;
import dev.toma.gunsrpg.world.cap.WorldDataStorage;
import dev.toma.gunsrpg.world.ore.WorldOreGen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
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

import java.util.function.Consumer;

@Mod(modid = GunsRPG.MODID, name = "Guns RPG", version = "0.2.5-private", acceptedMinecraftVersions = "[1.12.2]")
public class GunsRPG {

    public static final String MODID = "gunsrpg";
    public static Logger log = LogManager.getLogger();
    @SidedProxy(modId = MODID, clientSide = "dev.toma.gunsrpg.sided.ClientSideManager", serverSide = "dev.toma.gunsrpg.sided.ServerSideManager")
    public static SideManager sideManager;
    @Mod.Instance
    public static GunsRPG modInstance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        sideManager.preInit(event);
        NetworkManager.init();
        CapabilityManager.INSTANCE.register(PlayerData.class, new PlayerDataStorage(), PlayerDataFactory::new);
        CapabilityManager.INSTANCE.register(WorldDataCap.class, new WorldDataStorage(), WorldDataFactory::new);
        NetworkRegistry.INSTANCE.registerGuiHandler(modInstance, new GuiHandler());
        GameRegistry.registerTileEntity(TileEntityBlastFurnace.class, makeResource("blast_furnace"));
        GameRegistry.registerTileEntity(TileEntityAirdrop.class, makeResource("airdrop"));
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        sideManager.init(event);
        GameRegistry.registerWorldGenerator(new WorldOreGen(), 0);
        SkillTreeEntry.List.init();
        ItemAmmo.init();
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
            if(entry.getEntityClass().equals(EntitySlime.class)) continue;
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
}
