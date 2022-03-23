package dev.toma.gunsrpg.api.common.data;

import dev.toma.gunsrpg.api.common.perk.IPerkStat;
import dev.toma.gunsrpg.common.item.perk.Crystal;
import dev.toma.gunsrpg.common.perk.Perk;
import dev.toma.gunsrpg.util.IIntervalProvider;

import java.util.Set;

public interface IPerkProvider extends IPointProvider {

    Crystal getCrystal(int slot);

    void setCrystal(int slot, Crystal crystal);

    Set<Perk> getActivePerks();

    IPerkStat getPerkStat(Perk perk);

    void setCooldown(int ticks);

    void setCooldown(IIntervalProvider provider);

    int getCooldown();

    boolean isOnCooldown();

    void tick();
}
