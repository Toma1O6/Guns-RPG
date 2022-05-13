package dev.toma.gunsrpg.world.mayor;

public final class VillageVariantEntry {

    private final String targetVariant;
    private final String resourcePath;
    private final int count;

    public VillageVariantEntry(String targetVariant, String resourcePath, int count) {
        this.targetVariant = targetVariant;
        this.resourcePath = resourcePath;
        this.count = count;
    }

    public String getTargetVariant() {
        return targetVariant;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public int getCount() {
        return count;
    }
}
