package dev.toma.gunsrpg.common.skills.core;

import com.google.gson.JsonSyntaxException;
import dev.toma.gunsrpg.api.common.skill.IDataResolver;
import dev.toma.gunsrpg.api.common.skill.ITransactionValidatorFactory;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class WeaponLevelTransactionValidatorFactory implements ITransactionValidatorFactory<WeaponTransactionValidator, GunItem> {

    @Override
    public WeaponTransactionValidator createFor(GunItem data) {
        return new WeaponTransactionValidator(data);
    }

    @Override
    public boolean isDataMatch(WeaponTransactionValidator handler, GunItem data) {
        return handler.item == data;
    }

    @Override
    public IDataResolver<GunItem> resolver() {
        return json -> {
            if (!json.isJsonPrimitive())
                throw new JsonSyntaxException("Weapon data must be in primitive format!");
            String keyString = json.getAsString();
            ResourceLocation key = new ResourceLocation(keyString);
            Item item = ForgeRegistries.ITEMS.getValue(key);
            if (item instanceof GunItem) {
                return (GunItem) item;
            }
            throw new JsonSyntaxException(keyString + " item either doesn't exist or isn't a gun!");
        };
    }
}
