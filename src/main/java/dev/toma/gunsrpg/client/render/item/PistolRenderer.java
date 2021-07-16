package dev.toma.gunsrpg.client.render.item;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.model.ModelPistol;
import dev.toma.gunsrpg.client.model.ModelWeapon;
import net.minecraft.util.ResourceLocation;

public class PistolRenderer extends AbstractWeaponRenderer {

    @Override
    public ModelWeapon createModelInstance() {
        return new ModelPistol();
    }

    @Override
    public ResourceLocation createGunTextureInstance() {
        return GunsRPG.makeResource("textures/item/p1911.png");
    }
}
