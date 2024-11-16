package dev.toma.gunsrpg.config.client;

import dev.toma.configuration.config.Configurable;
import dev.toma.gunsrpg.client.OverlayPlacement;

public class ConfigurableOverlay {

    @Configurable
    @Configurable.Comment("Allows you to disable this overlay")
    public boolean enabled;

    @Configurable
    @Configurable.Comment({"Horizontal alignment of this overlay", "Accepted values: [LEFT, CENTER, RIGHT]"})
    public OverlayPlacement.HorizontalAlignment horizontalAlignment;

    @Configurable
    @Configurable.Comment({"Vertical alignment of this overlay", "Accepted values: [TOP, CENTER, BOTTOM]"})
    public OverlayPlacement.VerticalAlignment verticalAlignment;

    @Configurable
    @Configurable.Comment("X position offset for this overlay")
    public int xOffset;

    @Configurable
    @Configurable.Comment("Y position offset for this overlay")
    public int yOffset;

    public ConfigurableOverlay() {
        this(true, OverlayPlacement.HorizontalAlignment.LEFT, OverlayPlacement.VerticalAlignment.TOP, 0, 0);
    }

    public ConfigurableOverlay(boolean enabled, OverlayPlacement.HorizontalAlignment horizontal, OverlayPlacement.VerticalAlignment vertical, int xOffset, int yOffset) {
        this.enabled = enabled;
        this.horizontalAlignment = horizontal;
        this.verticalAlignment = vertical;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }
}
