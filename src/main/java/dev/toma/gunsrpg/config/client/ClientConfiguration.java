package dev.toma.gunsrpg.config.client;

import dev.toma.gunsrpg.config.util.Coord2Di;
import dev.toma.gunsrpg.config.util.ScopeRenderer;
import toma.config.ConfigSubcategory;
import toma.config.object.builder.ConfigBuilder;

public class ClientConfiguration extends ConfigSubcategory {

    public Coord2Di debuffs = new Coord2Di("Debuff offset", 0, -60);
    public boolean loadAnimationTool = false;
    public ScopeRenderer scopeRenderer = ScopeRenderer.IN_MODEL;
    public Coord2Di skillTreeSpacings = new Coord2Di("Skill spacings", 45, 45);

    @Override
    public ConfigBuilder toConfigFormat(ConfigBuilder builder) {
        return builder
                .push().name("Client Config").init()
                .addBoolean(loadAnimationTool).name("Load animation tool").add(t -> loadAnimationTool = t.value())
                .addEnum(scopeRenderer).name("Scope render").add(t -> scopeRenderer = t.value())
                .run(debuffs::asConfig)
                .run(skillTreeSpacings::asConfig)
                .pop();
    }
}
