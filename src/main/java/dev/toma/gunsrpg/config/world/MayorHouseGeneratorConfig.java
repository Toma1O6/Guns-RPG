package dev.toma.gunsrpg.config.world;

import dev.toma.configuration.config.Configurable;

public class MayorHouseGeneratorConfig {

    @Configurable
    @Configurable.Range(min = 0, max = 999)
    @Configurable.Comment("Configure spawn weight for Desert villages")
    public final int desert = 45;

    @Configurable
    @Configurable.Range(min = 0, max = 999)
    @Configurable.Comment("Configure spawn weight for Savanna villages")
    public final int savanna = 100;

    @Configurable
    @Configurable.Range(min = 0, max = 999)
    @Configurable.Comment("Configure spawn weight for Snowy villages")
    public final int snowy = 100;

    @Configurable
    @Configurable.Range(min = 0, max = 999)
    @Configurable.Comment("Configure spawn weight for Plains villages")
    public final int plains = 100;

    @Configurable
    @Configurable.Range(min = 0, max = 999)
    @Configurable.Comment("Configure spawn weight for Taiga villages")
    public final int taiga = 100;

    public int getDesertWeight() {
        return desert;
    }

    public int getSavannaWeight() {
        return savanna;
    }

    public int getSnowyWeight() {
        return snowy;
    }

    public int getPlainsWeight() {
        return plains;
    }

    public int getTaigaWeight() {
        return taiga;
    }
}
