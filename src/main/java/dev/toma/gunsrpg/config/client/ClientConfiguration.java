package dev.toma.gunsrpg.config.client;

import dev.toma.configuration.api.IConfigWriter;
import dev.toma.configuration.api.IObjectSpec;
import dev.toma.configuration.api.type.BooleanType;
import dev.toma.configuration.api.type.EnumType;
import dev.toma.configuration.api.type.ObjectType;
import dev.toma.gunsrpg.config.util.ScopeRenderer;
import dev.toma.gunsrpg.util.math.Vec2Di;

public class ClientConfiguration extends ObjectType {

    public BooleanType loadAnimationTool;
    public EnumType<ScopeRenderer> scopeRenderer;
    public Vec2Di debuffOverlay;

    public ClientConfiguration(IObjectSpec spec) {
        super(spec);

        IConfigWriter writer = spec.getWriter();
        loadAnimationTool = writer.writeBoolean("Animation tool", false, "Developer tool for animations");
        scopeRenderer = writer.writeEnum("Scope render type", ScopeRenderer.IN_MODEL);
        debuffOverlay = writer.writeObject(sp -> new Vec2Di(sp, 0, -60), "Debuff overlay", "Manage position of debuff overlay in HUD");
    }
}
