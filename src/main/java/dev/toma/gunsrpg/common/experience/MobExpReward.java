package dev.toma.gunsrpg.common.experience;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.util.JSONUtils;

public final class MobExpReward {

    public static final MobExpReward DEFAULT = new MobExpReward(null, 1);

    private final EntityType<?> entityType;
    private final int expAmount;

    public MobExpReward(EntityType<?> entityType, int expAmount) {
        this.entityType = entityType;
        this.expAmount = expAmount;
    }

    EntityType<?> getEntityType() {
        return entityType;
    }

    public int getExp() {
        return this.expAmount;
    }

    public static MobExpReward parseJson(JsonElement jsonElement) throws JsonParseException {
        JsonObject object = JsonHelper.asJsonObject(jsonElement);
        EntityType<?> type = JsonHelper.resolveEntityType(object.get("entity"));
        int exp = JSONUtils.getAsInt(object, "exp", 1);
        if (exp < 0) {
            throw new JsonSyntaxException("Experience amount cannot be negative number!");
        }
        return new MobExpReward(type, exp);
    }
}
