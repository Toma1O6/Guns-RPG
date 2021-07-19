package dev.toma.gunsrpg.client.render.item;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.model.M1911Model;
import dev.toma.gunsrpg.client.model.AbstractWeaponModel;
import net.minecraft.util.ResourceLocation;

public class M1911Renderer extends AbstractWeaponRenderer {

    @Override
    public AbstractWeaponModel createModelInstance() {
        return new M1911Model();
    }

    @Override
    public ResourceLocation createGunTextureInstance() {
        return GunsRPG.makeResource("textures/item/p1911.png");
    }
}
