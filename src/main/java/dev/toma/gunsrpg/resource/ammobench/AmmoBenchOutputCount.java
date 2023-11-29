package dev.toma.gunsrpg.resource.ammobench;

public interface AmmoBenchOutputCount {

    AmmoBenchOutputCountType<?> getType();

    int getCount(int count);
}
