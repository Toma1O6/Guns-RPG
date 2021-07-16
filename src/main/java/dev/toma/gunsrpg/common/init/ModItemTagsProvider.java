package dev.toma.gunsrpg.common.init;

import dev.toma.gunsrpg.GunsRPG;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemTagsProvider extends ItemTagsProvider {

    public ModItemTagsProvider(DataGenerator generator, BlockTagsProvider provider, ExistingFileHelper fileHelper) {
        super(generator, provider, GunsRPG.MODID, fileHelper);
    }

    @Override
    protected void addTags() {
        copy(ModTags.Blocks.ORES_AMETHYST, ModTags.Items.ORES_AMETHYST);
    }
}
