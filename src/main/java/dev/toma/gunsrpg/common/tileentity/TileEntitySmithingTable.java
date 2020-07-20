package dev.toma.gunsrpg.common.tileentity;

public class TileEntitySmithingTable extends IInventoryFactory {

    @Override
    public int getSizeInventory() {
        return 9;
    }

    @Override
    public String getName() {
        return "container.smithing_table";
    }
}
