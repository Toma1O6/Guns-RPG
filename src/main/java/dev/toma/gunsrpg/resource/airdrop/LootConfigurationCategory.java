package dev.toma.gunsrpg.resource.airdrop;

public final class LootConfigurationCategory {

    private final int weight;
    private final String identifier;

    public LootConfigurationCategory(int weight, String identifier) {
        this.weight = weight;
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LootConfigurationCategory that = (LootConfigurationCategory) o;
        return identifier.equals(that.identifier);
    }

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }
}
