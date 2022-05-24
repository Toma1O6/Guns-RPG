package dev.toma.gunsrpg.config.client;

import dev.toma.configuration.api.IConfigWriter;
import dev.toma.configuration.api.IObjectSpec;
import dev.toma.configuration.api.type.BooleanType;
import dev.toma.configuration.api.type.ColorType;
import dev.toma.configuration.api.type.ObjectType;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.config.type.IconsType;
import dev.toma.gunsrpg.util.math.ConfigurableVec2i;
import dev.toma.gunsrpg.util.math.IVec2i;
import net.minecraft.util.ResourceLocation;

public class ClientConfiguration extends ObjectType {

    private static final ResourceLocation[] VARIANTS = {
            GunsRPG.makeResource("textures/icons/reticle_dot.png"),
            GunsRPG.makeResource("textures/icons/reticle_chevron.png"),
            GunsRPG.makeResource("textures/icons/reticle_cross.png")
    };
    public final BooleanType developerMode;
    public final BooleanType alwaysRenderReticles;
    public final BooleanType shaderCompatibilityMode;
    public final IVec2i debuffOverlay;
    public final ColorType reticleColor;
    public final IconsType reticleVariants;
    public final IHeldLayerConfig gunnerHeldItemRenderConfig;
    public final IHeldLayerConfig explosiveSkeletonHeldItemRenderConfig;
    public final OpticsSensitivity optics;

    public ClientConfiguration(IObjectSpec spec) {
        super(spec);

        IConfigWriter writer = spec.getWriter();
        developerMode = writer.writeBoolean("Developer mode", false, "Enables some special developer tools");
        alwaysRenderReticles = writer.writeBoolean("Always show reticles", false, "Will always render reticle even if you're not aiming");
        shaderCompatibilityMode = writer.writeBoolean("Shader compatibility mode", false, "Attempts to render some things differently", "to ensure compatibility with shaders");
        debuffOverlay = writer.writeObject(sp -> new ConfigurableVec2i(sp, 0, -60), "Debuff overlay", "Manage position of debuff overlay in HUD");
        reticleColor = writer.writeColorARGB("Reticle color", "#FFFF0000", "Configure color reticle color");
        reticleVariants = IconsType.write(writer, "Reticle variant", 0, VARIANTS, "Configure reticle variant");
        gunnerHeldItemRenderConfig = writer.writeObject(sp -> new HeldLayerConfig(sp, IHeldLayerConfig.Mode.DEFAULT, "minecraft:crossbow"), "Gunner item render");
        explosiveSkeletonHeldItemRenderConfig = writer.writeObject(sp -> new HeldLayerConfig(sp, IHeldLayerConfig.Mode.DEFAULT, "minecraft:bow"), "Explosive skeleton item render");
        optics = writer.writeObject(OpticsSensitivity::new, "Optics sensitivity");
    }
}
