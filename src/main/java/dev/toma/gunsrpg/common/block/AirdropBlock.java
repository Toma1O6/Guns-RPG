package dev.toma.gunsrpg.common.block;

import dev.toma.gunsrpg.common.tileentity.AirdropTileEntity;
import dev.toma.gunsrpg.common.tileentity.ILootGenerator;
import dev.toma.gunsrpg.common.tileentity.LockableInventoryTileEntity;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class AirdropBlock extends AbstractCrateBlock {

    private static final ITextComponent TITLE = new TranslationTextComponent("container.airdrop");

    public AirdropBlock(String name) {
        super(name, Properties.of(Material.METAL).sound(SoundType.METAL).strength(-1.0F, 3600000.0F).noDrops().randomTicks());
    }

    @Override
    public ITextComponent getContainerTitle() {
        return TITLE;
    }

    @Override
    public boolean shouldDestroyEmptyBlock() {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends LockableInventoryTileEntity & ILootGenerator> T getNewBlockEntity() {
        return (T) new AirdropTileEntity();
    }
}
