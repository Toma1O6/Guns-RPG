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
    @Configurable.Comment("Enables developer tools")
    public boolean developerMode = false;

    @Configurable
    @Configurable.Comment("Reflex sight reticles will be visible even when not aiming")
    public boolean alwaysRenderReticles = false;

    @Configurable
    @Configurable.Comment("Renders scope reticles differently which may help with some shaders")
    public boolean shaderCompatibilityMode = false;

    @Configurable
    @Configurable.DecimalRange(min = 0.0, max = 0.5)
    @Configurable.Comment("Scaling of tracers")
    @Configurable.Gui.NumberFormat("0.0###")
    public float tracerScale = 0.02F;

    @Configurable
    @Configurable.Comment("Debuff overlay offset on HUD")
    public IVec2i debuffOverlay = new ConfigurableVec2i(0, -60);

    @Configurable
    @Configurable.StringPattern("#[A-Fa-f0-9]{1,8}")
    @Configurable.Comment("Color of reflex sight reticles in ARGB format")
    @Configurable.Gui.ColorValue(isARGB = true)
    @Configurable.Gui.CharacterLimit(9)
    public String reticleColor = "#FFFF0000";

    @Configurable
    @Configurable.Comment({"Type of reticle texture", "Valid values: DOT, CHEVRON, CROSS"})
    public ReticleType reticleType = ReticleType.DOT;

    @Configurable
    @Configurable.Comment("Configure held item rendering for Zombie Gunners")
    public IHeldLayerSettings gunnerHeldItemRender = new HeldLayerSettingsConfig(IHeldLayerSettings.Mode.DEFAULT, "minecraft:crossbow");

    @Configurable
    @Configurable.Comment("Configure held item rendering for Grenadiers")
    public IHeldLayerSettings grenadierHeldItemRender = new HeldLayerSettingsConfig(IHeldLayerSettings.Mode.DEFAULT, "minecraft:bow");

    @Configurable
    @Configurable.Comment("Configure sensitivity multipliers for scopes")
    public OpticsSensitivity optics = new OpticsSensitivity();

    @Configurable
    @Configurable.Comment("Configure positions of quest overlay on HUD")
    public QuestOverlayConfig questOverlay = new QuestOverlayConfig();

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
