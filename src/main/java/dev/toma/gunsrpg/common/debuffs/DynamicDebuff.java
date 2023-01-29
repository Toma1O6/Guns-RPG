package dev.toma.gunsrpg.common.debuffs;

import com.mojang.serialization.Codec;

public interface DynamicDebuff<SRC> {

    Codec<SRC> getDataCodec();

    void onDataLoaded(SRC data);
}
