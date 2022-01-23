package dev.toma.gunsrpg.resource.adapter;

import com.google.gson.*;
import dev.toma.gunsrpg.api.common.IAmmoMaterial;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterialManager;
import dev.toma.gunsrpg.resource.gunner.*;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.item.Item;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Type;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class GunnerLoadoutsAdapter implements JsonDeserializer<GunnerLoadouts> {

    @Override
    public GunnerLoadouts deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = JsonHelper.asJsonObject(json);
        GunnerGlobalProperties globalProperties = context.deserialize(JSONUtils.getAsJsonObject(object, "globalProps"), GunnerGlobalProperties.class);
        JsonArray array = JSONUtils.getAsJsonArray(object, "loadouts");
        GunnerLoadoutInstance[] instances = new GunnerLoadoutInstance[array.size()];
        for (int i = 0; i < array.size(); i++) {
            JsonElement element = array.get(i);
            instances[i] = resolveInstance(globalProperties, JsonHelper.asJsonObject(element));
        }
        return new GunnerLoadouts(globalProperties, instances);
    }

    private GunnerLoadoutInstance resolveInstance(GunnerGlobalProperties globalProps, JsonObject object) {
        GunnerLoadoutInstance.Builder builder = new GunnerLoadoutInstance.Builder();
        builder.setupGlobal(globalProps);
        // loading mandatory
        builder.weight(JSONUtils.getAsInt(object, "weight"));
        builder.weapon(this.resolveWeapon(object));
        builder.ammo(this.resolveAmmo(object));
        builder.magCapacity(JSONUtils.getAsInt(object, "magazine"));
        builder.reloadTime(this.resolveReloadTime(object));
        builder.firerate(this.resolveFirerate(object));
        // loading optional
        loadOptional(object, "capColor", builder, element -> ModUtils.string2colorRgb(element.getAsString()), GunnerLoadoutInstance.Builder::cap);
        if (object.has("shootTask")) {
            loadShootTaskProps(JSONUtils.getAsJsonObject(object, "shootTask"), builder);
        }
        return builder.buildLoadout();
    }

    // helpers

    private void loadShootTaskProps(JsonObject object, GunnerLoadoutInstance.Builder builder) {
        loadOptional(object, "inaccuracy", builder, JsonElement::getAsFloat, GunnerLoadoutInstance.Builder::inaccuracy);
        loadOptional(object, "accuracyBonus", builder, JsonElement::getAsFloat, GunnerLoadoutInstance.Builder::accuracyBonus);
        loadOptional(object, "burstSize", builder, JsonElement::getAsInt, GunnerLoadoutInstance.Builder::burstSize);
        loadOptional(object, "damageMultiplier", builder, this::resolveDamageMultiplier, GunnerLoadoutInstance.Builder::damageMultiplier);
        loadOptional(object, "burstDelay", builder, this::resolveBurstDelay, GunnerLoadoutInstance.Builder::burstDelay);
    }

    private <P> void loadOptional(JsonObject object, String key, GunnerLoadoutInstance.Builder builder, Function<JsonElement, P> resolver, BiConsumer<GunnerLoadoutInstance.Builder, P> consumer) {
        if (!object.has(key)) {
            return;
        }
        JsonElement value = object.get(key);
        P property = resolver.apply(value);
        consumer.accept(builder, property);
    }

    // resolvers

    private GunItem resolveWeapon(JsonObject object) {
        ResourceLocation id = new ResourceLocation(JSONUtils.getAsString(object, "gun"));
        Item item = ForgeRegistries.ITEMS.getValue(id);
        if (!(item instanceof GunItem)) {
            throw new JsonSyntaxException("Not a weapon: " + id);
        }
        return (GunItem) item;
    }

    private IAmmoMaterial resolveAmmoMaterial(JsonElement element) {
        ResourceLocation id = new ResourceLocation(element.getAsString());
        AmmoMaterialManager manager = AmmoMaterialManager.get();
        IAmmoMaterial material = manager.findMaterial(id);
        if (material == null) {
            throw new JsonSyntaxException("Unknown ammo material: " + id);
        }
        return material;
    }

    private IDifficultyProperty<IAmmoMaterial> resolveAmmo(JsonObject object) {
        if (!object.has("ammo")) {
            throw new JsonSyntaxException("Missing mandatory property: ammo");
        }
        JsonElement ammoElement = object.get("ammo");
        return DifficultyPropertyResolver.resolve(ammoElement, this::resolveAmmoMaterial);
    }

    private IDifficultyProperty<Integer> resolveReloadTime(JsonObject object) {
        if (!object.has("reloadTime")) {
            throw new JsonSyntaxException("Missing mandatory property: reloadTime");
        }
        JsonElement element = object.get("reloadTime");
        return DifficultyPropertyResolver.resolve(element, JsonElement::getAsInt);
    }

    private IDifficultyProperty<Integer> resolveFirerate(JsonObject object) {
        if (!object.has("firerate")) {
            throw new JsonSyntaxException("Missing mandatory property: firerate");
        }
        JsonElement element = object.get("firerate");
        return DifficultyPropertyResolver.resolve(element, JsonElement::getAsInt);
    }

    private IDifficultyProperty<Float> resolveDamageMultiplier(JsonElement element) {
        return DifficultyPropertyResolver.resolve(element, JsonElement::getAsFloat);
    }

    private IDifficultyProperty<Integer> resolveBurstDelay(JsonElement element) {
        return DifficultyPropertyResolver.resolve(element, JsonElement::getAsInt);
    }
}
