package dev.toma.gunsrpg.api.common.data;

import dev.toma.gunsrpg.api.common.perk.IPerk;
import dev.toma.gunsrpg.api.common.perk.IPerkOptions;
import dev.toma.gunsrpg.common.perk.Perk;

import java.util.Collection;

public interface IPerkProvider extends IPointProvider {

    IPerk getPerk(Perk perk);

    void addPerk(Perk perk, IPerkOptions options);

    void removePerk(Perk perk);

    Collection<IPerk> listPerks();
}
