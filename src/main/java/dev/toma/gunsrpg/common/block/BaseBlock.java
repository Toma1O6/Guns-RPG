package dev.toma.gunsrpg.common.block;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.init.CommonRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import java.util.function.Consumer;

public class BaseBlock extends Block {

    public BaseBlock(String name, Properties properties) {
        super(properties);
        this.setRegistryName(GunsRPG.makeResource(name));
        CommonRegistry.registerItemBlock(this);
    }

    public static BaseBlock basic(String name, Material material) {
        return new BaseBlock(name, Properties.of(material));
    }

    public static BaseBlock basic(String name, Material material, Consumer<Properties> propertiesConsumer) {
        Properties properties = Properties.of(material);
        propertiesConsumer.accept(properties);
        return new BaseBlock(name, properties);
    }
}
