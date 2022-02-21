package dev.toma.gunsrpg.common.tileentity;

public interface ILootGenerator {

    boolean isEmptyInventory();

    void generateLoot();
}
