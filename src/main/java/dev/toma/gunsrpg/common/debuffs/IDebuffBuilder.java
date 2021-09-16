package dev.toma.gunsrpg.common.debuffs;

import java.util.function.BooleanSupplier;

public interface IDebuffBuilder<D extends IDebuff> {

    DebuffType.IFactory<D> getFactory();

    BooleanSupplier disabledStatusSupplier();
}
