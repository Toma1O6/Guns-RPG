package dev.toma.gunsrpg.config.type;

import dev.toma.configuration.api.IConfigWriter;
import dev.toma.configuration.api.NameableWrapper;
import dev.toma.configuration.api.type.ArrayType;
import net.minecraft.util.ResourceLocation;

public class IconsType extends ArrayType<NameableWrapper<ResourceLocation>> {

    public IconsType(String name, NameableWrapper<ResourceLocation> entry, NameableWrapper<ResourceLocation>[] elements, String... desc) {
        super(name, entry, elements, desc);
    }

    public static IconsType write(IConfigWriter writer, String name, int entry, ResourceLocation[] locations, String... desc) {
        NameableWrapper<ResourceLocation>[] wrappers = NameableWrapper.wrap(locations, ResourceLocation::toString, IconsType::formatLocation);
        return writer.write(new IconsType(name, wrappers[entry], wrappers, desc));
    }

    public ResourceLocation getAsResource() {
        return get().getElement();
    }

    private static String formatLocation(String asString) {
        int lastIndex = asString.lastIndexOf('/');
        if (lastIndex > 0) {
            asString = asString.substring(lastIndex);
        }
        asString = asString.replaceAll("\\..+", "");
        return asString.substring(0, 1).toUpperCase() + asString.substring(1);
    }
}
