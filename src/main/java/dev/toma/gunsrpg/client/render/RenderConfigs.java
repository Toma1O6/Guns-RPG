package dev.toma.gunsrpg.client.render;

import lib.toma.animations.IRenderConfig;
import lib.toma.animations.RenderConfig;
import net.minecraft.util.math.vector.Vector3f;

public class RenderConfigs {
    public static final IRenderConfig HEAL_CONFIG = RenderConfig.newDef().withRotation(Vector3f.XN.rotationDegrees(80)).finish();
}
