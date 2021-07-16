package dev.toma.gunsrpg.client.render.item;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.model.ModelSR;
import dev.toma.gunsrpg.client.model.ModelWeapon;
import net.minecraft.util.ResourceLocation;

public class SRRenderer extends AbstractWeaponRenderer {

    @Override
    public ModelWeapon createModelInstance() {
        return new ModelSR();
    }

    @Override
    public ResourceLocation createGunTextureInstance() {
        return GunsRPG.makeResource("textures/item/kar98k.png");
    }
}
