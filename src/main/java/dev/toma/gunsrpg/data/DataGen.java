package dev.toma.gunsrpg.data;

import dev.toma.gunsrpg.GunsRPG;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = GunsRPG.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class DataGen {

    private DataGen() {}

    @SubscribeEvent
    public static void onDataGather(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        if (event.includeServer()) {
            ExistingFileHelper fileHelper = event.getExistingFileHelper();
            ModBlockTagsProvider blockTagsProvider = new ModBlockTagsProvider(generator, fileHelper);
            generator.addProvider(blockTagsProvider);
            generator.addProvider(new ModItemTagsProvider(generator, blockTagsProvider, fileHelper));
        }
    }
}
