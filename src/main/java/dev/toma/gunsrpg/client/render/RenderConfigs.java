package dev.toma.gunsrpg.client.render;

import lib.toma.animations.IRenderConfig;
import lib.toma.animations.RenderConfig;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

public class RenderConfigs {
    public static final IRenderConfig HEAL_CONFIG = RenderConfig.newDef().withRotation(Vector3f.XN.rotationDegrees(80)).finish();

    public static final IRenderConfig M1911_LEFT = RenderConfig.newDef().withPosition(-0.100, -0.550, 0.330).withScale(1.000F, 1.000F, 1.000F).withRotation(new Quaternion(0.140F, -0.609F, 0.000F, 0.793F)).finish();
    public static final IRenderConfig M1911_RIGHT = RenderConfig.newDef().withPosition(0.300, -0.250, -0.300).withScale(1.000F, 1.000F, 1.000F).withRotation(new Quaternion(0.152F, -0.216F, 0.000F, 0.976F)).finish();
    public static final IRenderConfig M1911_LEFT_DUAL = RenderConfig.newDef().withPosition(-0.670, -0.440, 0.470).withScale(1.000F, 1.000F, 1.000F).withRotation(new Quaternion(0.106F, -0.423F, -0.042F, 0.906F)).finish();
    public static final IRenderConfig M1911_RIGHT_DUAL = RenderConfig.newDef().withPosition(0.280, -0.230, -0.350).withScale(1.000F, 1.000F, 1.000F).withRotation(new Quaternion(0.129F, -0.259F, 0.000F, 0.966F)).finish();


}
