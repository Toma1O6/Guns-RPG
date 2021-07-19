package dev.toma.gunsrpg.client.render.item;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.model.Ump45Model;
import dev.toma.gunsrpg.client.model.AbstractWeaponModel;
import net.minecraft.util.ResourceLocation;

public class Ump45Renderer extends AbstractWeaponRenderer {

    @Override
    public AbstractWeaponModel createModelInstance() {
        return new Ump45Model();
    }

    @Override
    public ResourceLocation createGunTextureInstance() {
        return GunsRPG.makeResource("textures/item/ump45.png");
    }
}
