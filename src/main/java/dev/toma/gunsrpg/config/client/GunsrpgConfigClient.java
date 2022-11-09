package dev.toma.gunsrpg.config.client;

import dev.toma.configuration.config.Config;
import dev.toma.configuration.config.Configurable;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.util.math.ConfigurableVec2i;
import dev.toma.gunsrpg.util.math.IVec2i;
import net.minecraft.util.ResourceLocation;

@Config(id = "gunsrpg-client", group = GunsRPG.MODID)
public final class GunsrpgConfigClient {

    @Configurable
    public final boolean developerMode = false;

    @Configurable
    public final boolean alwaysRenderReticles = false;

    @Configurable
    public final boolean shaderCompatibilityMode = false;

    @Configurable
    public final IVec2i debuffOverlay = new ConfigurableVec2i(0, -60);

    @Configurable
    @Configurable.StringPattern("#[A-Fa-f0-9]{1,8}")
    @Configurable.Gui.ColorValue(isARGB = true)
    public final String reticleColor = "#FFFF0000";

    @Configurable
    public final ReticleType reticleType = ReticleType.DOT;

    @Configurable
    public final IHeldLayerSettings gunnerHeldItemRender = new HeldLayerSettingsConfig(IHeldLayerSettings.Mode.DEFAULT, "minecraft:crossbow");

    @Configurable
    public final IHeldLayerSettings explosiveSkeletonHeldItemRender = new HeldLayerSettingsConfig(IHeldLayerSettings.Mode.DEFAULT, "minecraft:bow");

    @Configurable
    public final OpticsSensitivity optics = new OpticsSensitivity();

    @Configurable
    public final QuestOverlayConfig questOverlay = new QuestOverlayConfig();

    public enum ReticleType {

        DOT("textures/icons/reticle_dot.png"),
        CHEVRON("textures/icons/reticle_chevron.png"),
        CROSS("textures/icons/reticle_cross.png");

        private final ResourceLocation path;

        ReticleType(String path) {
            this.path = GunsRPG.makeResource(path);
        }

        public ResourceLocation getPath() {
            return path;
        }
    }
}
