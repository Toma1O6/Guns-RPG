package dev.toma.gunsrpg.resource.crate;

public final class SlotConfiguration {

    private final int weight;
    private final int count;

    public SlotConfiguration(int weight, int count) {
        this.weight = weight;
        this.count = count;
    }

    public int getWeight() {
        return weight;
    }

    public int getCount() {
        return count;
    }
}
