package dev.toma.gunsrpg.common.quests.quest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class DisplayInfo {

    private final ITextComponent name;
    private final ITextComponent info;

    public DisplayInfo(String title, String description) {
        this.name = new StringTextComponent(title);
        this.info = new StringTextComponent(description);
    }

    public ITextComponent getName() {
        return name;
    }

    public ITextComponent getInfo() {
        return info;
    }

    public static DisplayInfo fromJson(JsonObject object) throws JsonParseException {
        String name = JSONUtils.getAsString(object, "name");
        String info = JSONUtils.getAsString(object, "info");
        return new DisplayInfo(name, info);
    }
}
