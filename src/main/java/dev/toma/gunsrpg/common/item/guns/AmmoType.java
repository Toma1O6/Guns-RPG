package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.common.skilltree.Ability;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public enum AmmoType {
    _9MM(data -> {
        data.put(AmmoMaterial.WOOD, Pair.of(Ability.PISTOL_WOOD_AMMO, 5.0F));
        data.put(AmmoMaterial.STONE, Pair.of(Ability.PISTOL_STONE_AMMO, 7.0F));
    }),
    _45ACP(data -> {

    }),
    _556MM(data -> {

    }),
    _762MM(data -> {

    }),
    _12G(data -> {

    });

    private Map<AmmoMaterial, Pair<Ability.Type, Float>> data = new HashMap<>();

    AmmoType(Consumer<Map<AmmoMaterial, Pair<Ability.Type, Float>>> consumer) {
        consumer.accept(data);
    }

    public Map<AmmoMaterial, Pair<Ability.Type, Float>> getData() {
        return data;
    }
}
