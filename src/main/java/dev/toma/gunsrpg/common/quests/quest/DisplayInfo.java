package dev.toma.gunsrpg.common.quests.quest;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.io.File;
import java.util.Locale;

public class DisplayInfo {

    private final TextComponent name;
    private final String infoKey;

    private DisplayInfo(String title, String infoKey) {
        this.name = new TranslationTextComponent(title);
        this.infoKey = infoKey;
    }

    public TextComponent getName() {
        return name;
    }

    public TextComponent getInfo(Object... args) {
        return new TranslationTextComponent(this.infoKey, args);
    }

    public static DisplayInfo create(ResourceLocation questId) {
        String prefix = String.format(Locale.ROOT, "quest.%s.%s", questId.getNamespace(), questId.getPath().replaceAll(File.pathSeparator, "_"));
        return new DisplayInfo(
                prefix + ".header",
                prefix + ".description"
        );
    }
}
