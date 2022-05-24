package dev.toma.gunsrpg.common.quests.quest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;

public class DisplayInfo {

    private final IFormattableTextComponent name;
    private final IFormattableTextComponent info;

    public DisplayInfo(String title, String description) {
        this.name = new StringTextComponent(title);
        this.info = new StringTextComponent(description);
    }

    public IFormattableTextComponent getName() {
        return name;
    }

    public IFormattableTextComponent getInfo() {
        return info;
    }

    public static DisplayInfo fromJson(JsonObject object) throws JsonParseException {
        String name = JSONUtils.getAsString(object, "name");
        String info = JSONUtils.getAsString(object, "info");
        return new DisplayInfo(name, info);
    }
}
