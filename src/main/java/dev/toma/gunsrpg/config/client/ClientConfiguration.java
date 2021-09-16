package dev.toma.gunsrpg.config.client;

import dev.toma.configuration.api.IConfigWriter;
import dev.toma.configuration.api.IObjectSpec;
import dev.toma.configuration.api.NumberDisplayType;
import dev.toma.configuration.api.type.*;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.config.type.IconsType;
import dev.toma.gunsrpg.config.util.ScopeRenderer;
import dev.toma.gunsrpg.util.math.ConfigurableVec2i;
import dev.toma.gunsrpg.util.math.IVec2i;
import net.minecraft.util.ResourceLocation;

import java.text.DecimalFormat;

public class ClientConfiguration extends ObjectType {

    private static final ResourceLocation[] VARIANTS = {
            GunsRPG.makeResource("textures/icons/reticle_dot.png"),
            GunsRPG.makeResource("textures/icons/reticle_chevron.png"),
            GunsRPG.makeResource("textures/icons/reticle_cross.png")
    };
    public final BooleanType developerMode;
    public final EnumType<ScopeRenderer> scopeRenderer;
    public final IVec2i debuffOverlay;
    public final ColorType reticleColor;
    public final IconsType reticleVariants;
    public final DoubleType recoilAnimationScale;

    public ClientConfiguration(IObjectSpec spec) {
        super(spec);

        IConfigWriter writer = spec.getWriter();
        developerMode = writer.writeBoolean("Developer mode", false, "Enables some special developer tools");
        scopeRenderer = writer.writeEnum("Scope render type", ScopeRenderer.IN_MODEL);
        debuffOverlay = writer.writeObject(sp -> new ConfigurableVec2i(sp, 0, -60), "Debuff overlay", "Manage position of debuff overlay in HUD");
        reticleColor = writer.writeColorARGB("Reticle color", "#FFFF0000", "Configure color reticle color");
        reticleVariants = IconsType.write(writer, "Reticle variant", 0, VARIANTS, "Configure reticle variant");
        recoilAnimationScale = writer.writeBoundedDouble("Recoil animation scale", 1.0, 0.0, 1.0, "Configure how much will be weapon animation affected by recoil").setDisplay(NumberDisplayType.TEXT_FIELD_SLIDER).setFormatting(new DecimalFormat("0.0##"));
    }
}
