package dev.toma.gunsrpg.api.common.data;

import java.util.UUID;

public interface ITraderStandings {

    void registerNew(UUID traderId);

    ITraderStatus getStatusWithTrader(UUID traderId);
}
