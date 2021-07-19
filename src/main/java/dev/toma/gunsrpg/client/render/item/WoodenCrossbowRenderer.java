package dev.toma.gunsrpg.client.render.item;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.model.WoodenCrossbowModel;
import dev.toma.gunsrpg.client.model.AbstractWeaponModel;
import net.minecraft.util.ResourceLocation;

public class WoodenCrossbowRenderer extends AbstractWeaponRenderer {

    @Override
    public AbstractWeaponModel createModelInstance() {
        return new WoodenCrossbowModel();
    }

    @Override
    public ResourceLocation createGunTextureInstance() {
        return GunsRPG.makeResource("textures/item/crossbow.png");
    }
}
