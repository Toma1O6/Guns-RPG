package dev.toma.gunsrpg.config.client;

import dev.toma.configuration.config.Config;
import dev.toma.configuration.config.Configurable;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.OverlayPlacement;
import net.minecraft.util.ResourceLocation;

@Config(id = "gunsrpg-client", group = GunsRPG.MODID)
public final class GunsrpgConfigClient {

    @Configurable
    @Configurable.Comment("Enables developer tools")
    public boolean developerMode = false;

    @Configurable
    @Configurable.Comment({"Select input type for aimimg", "Values: TOGGLE, HOLD"})
    public MouseInputType aimInputType = MouseInputType.TOGGLE;

    @Configurable
    @Configurable.Comment("Reflex sight reticles will be visible even when not aiming")
    public boolean alwaysRenderReticles = false;

    @Configurable
    @Configurable.Comment("Renders scope reticles differently which may help with some shaders")
    public boolean shaderCompatibilityMode = false;

    @Configurable
    @Configurable.Comment("Will render number with remaining day count to next bloodmoon on upper right corner")
    public ConfigurableOverlay bloodmoonCounterOverlay = new ConfigurableOverlay(true, OverlayPlacement.HorizontalAlignment.RIGHT, OverlayPlacement.VerticalAlignment.TOP, 5, 5);

    @Configurable
    @Configurable.DecimalRange(min = 0.0, max = 0.5)
    @Configurable.Comment("Scaling of tracers")
    @Configurable.Gui.NumberFormat("0.0###")
    public float tracerScale = 0.02F;

    @Configurable
    @Configurable.Comment("Debuff overlay offset on HUD")
    public ConfigurableOverlay debuffOverlay = new ConfigurableOverlay(true, OverlayPlacement.HorizontalAlignment.LEFT, OverlayPlacement.VerticalAlignment.BOTTOM, 0, 60);

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
    @Configurable.Comment("Configure position of quest overlay on HUD")
    public ConfigurableOverlay questOverlay = new ConfigurableOverlay(true, OverlayPlacement.HorizontalAlignment.RIGHT, OverlayPlacement.VerticalAlignment.CENTER, 0, 0);

    @Configurable
    @Configurable.Comment("Configure position of active useable skills on HUD")
    public ConfigurableOverlay activeSkillOverlay = new ConfigurableOverlay(true, OverlayPlacement.HorizontalAlignment.LEFT, OverlayPlacement.VerticalAlignment.BOTTOM, 10, 0);

    @Configurable
    @Configurable.Comment("Configure position of skill progress on HUD")
    public ConfigurableOverlay levelProgressOverlay = new ConfigurableOverlay(true, OverlayPlacement.HorizontalAlignment.RIGHT, OverlayPlacement.VerticalAlignment.BOTTOM, 10, 10);

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
