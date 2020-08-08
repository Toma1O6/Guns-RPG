package dev.toma.gunsrpg.config.client;

import dev.toma.gunsrpg.config.util.ScopeRenderer;
import dev.toma.gunsrpg.util.math.Vec2Di;
import net.minecraftforge.common.config.Config;

public class ClientConfiguration {

    @Config.Name("Debuff overlay")
    @Config.Comment("Manage position of debuff overlay in HUD")
    public Vec2Di debuffOverlay = new Vec2Di(0, -60);

    @Config.Name("Animation tool")
    @Config.Comment("Developer tool for animations")
    @Config.RequiresMcRestart
    public boolean loadAnimationTool = false;

    @Config.Name("Scope render type")
    @Config.Comment("Manage scope overlay render")
    public ScopeRenderer scopeRenderer = ScopeRenderer.IN_MODEL;

    @Config.Name("Skill tree spacing")
    @Config.Comment("Manage gap size between skills in skill tree display")
    @Config.RequiresMcRestart
    public Vec2Di skillTreeSpacing = new Vec2Di(45, 45);

}
