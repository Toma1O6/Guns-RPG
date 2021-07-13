package dev.toma.gunsrpg.config.client;

import dev.toma.configuration.api.IConfigWriter;
import dev.toma.configuration.api.IObjectSpec;
import dev.toma.configuration.api.type.BooleanType;
import dev.toma.configuration.api.type.ColorType;
import dev.toma.configuration.api.type.EnumType;
import dev.toma.configuration.api.type.ObjectType;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.config.type.IconsType;
import dev.toma.gunsrpg.config.util.ScopeRenderer;
import dev.toma.gunsrpg.util.math.ConfigurableVec2i;
import net.minecraft.util.ResourceLocation;

public class ClientConfiguration extends ObjectType {

    public final BooleanType loadAnimationTool;
    public final EnumType<ScopeRenderer> scopeRenderer;
    public final ConfigurableVec2i debuffOverlay;
    public final ColorType reticleColor;
    public final IconsType reticleVariants;

    private static final ResourceLocation[] VARIANTS = {
            GunsRPG.makeResource("textures/icons/reticle_dot.png"),
            GunsRPG.makeResource("textures/icons/reticle_chevron.png"),
            GunsRPG.makeResource("textures/icons/reticle_cross.png")
    };

    public ClientConfiguration(IObjectSpec spec) {
        super(spec);

        IConfigWriter writer = spec.getWriter();
        loadAnimationTool = writer.writeBoolean("Animation tool", false, "Developer tool for animations");
        scopeRenderer = writer.writeEnum("Scope render type", ScopeRenderer.IN_MODEL);
        debuffOverlay = writer.writeObject(sp -> new ConfigurableVec2i(sp, 0, -60), "Debuff overlay", "Manage position of debuff overlay in HUD");
        reticleColor = writer.writeColorARGB("Reticle color", "#FFFF0000", "Configure color reticle color");
        reticleVariants = IconsType.write(writer, "Reticle variant", 0, VARIANTS, "Configure reticle variant");
    }
}
