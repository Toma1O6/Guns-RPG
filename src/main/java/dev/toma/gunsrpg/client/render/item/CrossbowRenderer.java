package dev.toma.gunsrpg.client.render.item;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.model.ModelCrossbow;
import dev.toma.gunsrpg.client.model.ModelWeapon;
import net.minecraft.util.ResourceLocation;

public class CrossbowRenderer extends AbstractWeaponRenderer {

    @Override
    public ModelWeapon createModelInstance() {
        return new ModelCrossbow();
    }

    @Override
    public ResourceLocation createGunTextureInstance() {
        return GunsRPG.makeResource("textures/item/crossbow.png");
    }
}
