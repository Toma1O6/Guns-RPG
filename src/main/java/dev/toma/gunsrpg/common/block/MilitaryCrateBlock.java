package dev.toma.gunsrpg.common.block;

import dev.toma.gunsrpg.common.tileentity.ILootGenerator;
import dev.toma.gunsrpg.common.tileentity.InventoryTileEntity;
import dev.toma.gunsrpg.common.tileentity.MilitaryCrateTileEntity;
import net.minecraft.block.material.Material;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ToolType;

import java.util.Arrays;
import java.util.Comparator;

public class MilitaryCrateBlock extends AbstractCrateBlock {

    private static final ITextComponent TITLE = new TranslationTextComponent("container.crate");
    private final BiomeVariant variant;

    public MilitaryCrateBlock(String name, BiomeVariant variant) {
        super(name, Properties.of(Material.WOOD).strength(2.5F).harvestTool(ToolType.AXE).randomTicks());
        this.variant = variant;
    }

    public BiomeVariant getVariant() {
        return variant;
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
    public <T extends InventoryTileEntity & ILootGenerator> T getNewBlockEntity() {
        return (T) new MilitaryCrateTileEntity();
    }

    public enum BiomeVariant {
        ARTIC(Integer.MIN_VALUE),
        WOODLAND(0.1F),
        DESERT(1.15F);

        public static final BiomeVariant[] BY_TEMPERATURE = Arrays.stream(values())
                .sorted(Comparator.comparingDouble(BiomeVariant::getMinTemperature))
                .toArray(BiomeVariant[]::new);
        private final float minTemperature;

        BiomeVariant(float minTemperature) {
            this.minTemperature = minTemperature;
        }

        public float getMinTemperature() {
            return minTemperature;
        }
    }
}
