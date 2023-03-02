package dev.toma.gunsrpg.config.world;

import dev.toma.configuration.config.Configurable;

public final class WorldGenConfig {

    @Configurable
    @Configurable.Comment("Configure amethyst generation")
    public SimpleOreGenConfig amethyst = new SimpleOreGenConfig(3, 1, 16);

    @Configurable
    @Configurable.Comment("Configure black crystal generation")
    public SimpleOreGenConfig blackCrystal = new SimpleOreGenConfig(3, 1, 56);

    @Configurable
    @Configurable.Comment("Configure blue crystal generation")
    public SimpleOreGenConfig blueCrystal = new SimpleOreGenConfig(3, 1, 56);

    @Configurable
    @Configurable.Comment("Configure green crystal generation")
    public SimpleOreGenConfig greenCrystal = new SimpleOreGenConfig(3, 1, 56);

    @Configurable
    @Configurable.Comment("Configure red crystal generation")
    public SimpleOreGenConfig redCrystal = new SimpleOreGenConfig(3, 1, 56);

    @Configurable
    @Configurable.Comment("Configure white crystal generation")
    public SimpleOreGenConfig whiteCrystal = new SimpleOreGenConfig(3, 1, 56);

    @Configurable
    @Configurable.Comment("Configure yellow crystal generation")
    public SimpleOreGenConfig yellowCrystal = new SimpleOreGenConfig(3, 1, 56);

    @Configurable
    @Configurable.Comment("Configure black orb generation")
    public SimpleOreGenConfig blackOrb = new SimpleOreGenConfig(4, 1, 56);

    @Configurable
    @Configurable.Comment("Configure blue orb generation")
    public SimpleOreGenConfig blueOrb = new SimpleOreGenConfig(4, 1, 56);

    @Configurable
    @Configurable.Comment("Configure green orb generation")
    public SimpleOreGenConfig greenOrb = new SimpleOreGenConfig(4, 1, 56);

    @Configurable
    @Configurable.Comment("Configure red orb generation")
    public SimpleOreGenConfig redOrb = new SimpleOreGenConfig(4, 1, 56);

    @Configurable
    @Configurable.Comment("Configure white orb generation")
    public SimpleOreGenConfig whiteOrb = new SimpleOreGenConfig(4, 1, 56);

    @Configurable
    @Configurable.Comment("Configure yellow orb generation")
    public SimpleOreGenConfig yellowOrb = new SimpleOreGenConfig(4, 1, 56);

    @Configurable
    @Configurable.Comment("Configure spawns for mayor house in villages")
    public MayorHouseGeneratorConfig mayorHouseGen = new MayorHouseGeneratorConfig();
}
