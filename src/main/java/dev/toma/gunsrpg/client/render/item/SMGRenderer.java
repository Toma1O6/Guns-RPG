package dev.toma.gunsrpg.client.render.item;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.model.ModelSMG;
import dev.toma.gunsrpg.client.model.ModelWeapon;
import net.minecraft.util.ResourceLocation;

public class SMGRenderer extends AbstractWeaponRenderer {

    @Override
    public ModelWeapon createModelInstance() {
        return new ModelSMG();
    }

    @Override
    public ResourceLocation createGunTextureInstance() {
        return GunsRPG.makeResource("textures/item/ump45.png");
    }
}
