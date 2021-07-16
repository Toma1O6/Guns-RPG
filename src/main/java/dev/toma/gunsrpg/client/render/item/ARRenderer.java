package dev.toma.gunsrpg.client.render.item;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.model.ModelAR;
import dev.toma.gunsrpg.client.model.ModelWeapon;
import net.minecraft.util.ResourceLocation;

public class ARRenderer extends AbstractWeaponRenderer {

    @Override
    public ModelWeapon createModelInstance() {
        return new ModelAR();
    }

    @Override
    public ResourceLocation createGunTextureInstance() {
        return GunsRPG.makeResource("textures/item/sks.png");
    }
}
