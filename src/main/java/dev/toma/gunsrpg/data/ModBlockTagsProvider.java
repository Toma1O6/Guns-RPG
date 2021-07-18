package dev.toma.gunsrpg.data;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.init.GRPGBlocks;
import dev.toma.gunsrpg.common.init.ModTags;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockTagsProvider extends BlockTagsProvider {

    public ModBlockTagsProvider(DataGenerator generator, ExistingFileHelper fileHelper) {
        super(generator, GunsRPG.MODID, fileHelper);
    }

    @Override
    protected void addTags() {
        tag(ModTags.Blocks.ORES_AMETHYST).add(GRPGBlocks.AMETHYST_ORE);
        tag(Tags.Blocks.ORES).addTag(ModTags.Blocks.ORES_AMETHYST);
    }
}
