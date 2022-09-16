package dev.toma.gunsrpg.config.world;

import dev.toma.configuration.api.IConfigWriter;
import dev.toma.configuration.api.IObjectSpec;
import dev.toma.configuration.api.type.IntType;
import dev.toma.configuration.api.type.ObjectType;

public class MayorHouseGeneratorConfig extends ObjectType {

    private final IntType desert;
    private final IntType savanna;
    private final IntType snowy;
    private final IntType plains;
    private final IntType taiga;

    public MayorHouseGeneratorConfig(IObjectSpec spec) {
        super(spec);

        IConfigWriter writer = spec.getWriter();
        desert = writer.writeBoundedInt("Desert", 45, 0, 1000, "Configure spawn weight for Desert villages");
        savanna = writer.writeBoundedInt("Savanna", 100, 0, 1000, "Configure spawn weight for Savanna villages");
        snowy = writer.writeBoundedInt("Snowy", 100, 0, 1000, "Configure spawn weight for Snowy villages");
        plains = writer.writeBoundedInt("Plains", 100, 0, 1000, "Configure spawn weight for Plains villages");
        taiga = writer.writeBoundedInt("Taiga", 100, 0, 1000, "Configure spawn weight for Taiga villages");
    }

    public int getDesertWeight() {
        return desert.get();
    }

    public int getSavannaWeight() {
        return savanna.get();
    }

    public int getSnowyWeight() {
        return snowy.get();
    }

    public int getPlainsWeight() {
        return plains.get();
    }

    public int getTaigaWeight() {
        return taiga.get();
    }
}
