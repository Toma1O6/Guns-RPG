package dev.toma.gunsrpg.api.common.data;

import dev.toma.gunsrpg.common.item.perk.Crystal;

public interface IPerkProvider extends IPointProvider {

    Crystal getCrystal(int slot);

    void setCrystal(int slot, Crystal crystal);
}
