package dev.toma.gunsrpg.common.item.guns.setup;

public interface IMaterialStat {

    int value();

    int requiredLevel();

    static IMaterialStat of(int value, int requiredLevel) {
        return new IMaterialStat() {
            @Override
            public int value() {
                return value;
            }

            @Override
            public int requiredLevel() {
                return requiredLevel;
            }
        };
    }
}
