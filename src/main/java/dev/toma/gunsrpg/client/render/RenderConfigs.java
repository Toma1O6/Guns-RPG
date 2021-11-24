package dev.toma.gunsrpg.client.render;

import lib.toma.animations.api.IRenderConfig;
import lib.toma.animations.engine.RenderConfig;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

public class RenderConfigs {

    // HANDS ======================================
    // Healing
    public static final IRenderConfig HEAL_CONFIG = RenderConfig.newDef().withRotation(Vector3f.XN.rotationDegrees(80)).finish();

    // Weapons
    public static final IRenderConfig M1911_LEFT = RenderConfig.newDef().withPosition(-0.1, -0.55, 0.33).withScale(1.0F, 1.0F, 1.0F).withRotation(new Quaternion(0.140F, -0.609F, 0.000F, 0.793F)).finish();
    public static final IRenderConfig M1911_RIGHT = RenderConfig.newDef().withPosition(0.3, -0.25, -0.3).withScale(1.0F, 1.0F, 1.0F).withRotation(new Quaternion(0.152F, -0.216F, 0.000F, 0.976F)).finish();
    public static final IRenderConfig M1911_LEFT_DUAL = RenderConfig.newDef().withPosition(-0.67, -0.44, 0.47).withScale(1.0F, 1.0F, 1.0F).withRotation(new Quaternion(0.106F, -0.423F, -0.042F, 0.906F)).finish();
    public static final IRenderConfig M1911_RIGHT_DUAL = RenderConfig.newDef().withPosition(0.28, -0.23, -0.35).withScale(1.0F, 1.0F, 1.0F).withRotation(new Quaternion(0.129F, -0.259F, 0.000F, 0.966F)).finish();
    public static final IRenderConfig R45_LEFT = RenderConfig.newDef().withPosition(0.350, -0.540, 0.300).withScale(1.000F, 1.000F, 1.000F).withRotation(new Quaternion(0.100F, -0.500F, 0.000F, 0.866F)).finish();
    public static final IRenderConfig R45_RIGHT = RenderConfig.newDef().withPosition(0.500, -0.270, -0.500).withScale(1.000F, 1.000F, 1.000F).withRotation(new Quaternion(0.087F, -0.216F, -0.000F, 0.976F)).finish();
    public static final IRenderConfig DESERT_EAGLE_LEFT = RenderConfig.newDef().withPosition(0.300, -0.450, 0.300).withScale(1.000F, 1.000F, 1.000F).withRotation(new Quaternion(0.050F, -0.500F, 0.000F, 0.866F)).finish();
    public static final IRenderConfig DESERT_EAGLE_RIGHT = RenderConfig.newDef().withPosition(0.400, -0.350, -0.500).withScale(1.000F, 1.000F, 1.000F).withRotation(new Quaternion(0.104F, -0.259F, 0.000F, 0.966F)).finish();
    public static final IRenderConfig UMP45_LEFT = RenderConfig.newDef().withPosition(-0.2, -0.53, 0.6).withScale(1.0F, 1.0F, 1.9F).withRotation(new Quaternion(0.122F, -0.609F, 0.000F, 0.793F)).finish();
    public static final IRenderConfig UMP45_RIGHT = RenderConfig.newDef().withPosition(0.3, -0.3, -0.1).withScale(1.0F, 1.0F, 1.0F).withRotation(new Quaternion(0.052F, -0.174F, 0.000F, 0.985F)).finish();
    public static final IRenderConfig THOMPSON_LEFT = RenderConfig.newDef().withPosition(0.100, -0.700, 0.300).withScale(0.700F, 1.000F, 2.000F).withRotation(new Quaternion(0.141F, -0.707F, 0.000F, 0.707F)).finish();
    public static final IRenderConfig THOMPSON_RIGHT = RenderConfig.newDef().withPosition(0.400, -0.500, -0.600).withScale(1.000F, 1.000F, 1.000F).withRotation(new Quaternion(0.153F, -0.383F, 0.000F, 0.924F)).finish();
    public static final IRenderConfig VECTOR_LEFT = RenderConfig.newDef().withPosition(0.130, -0.570, -0.150).withScale(1.000F, 1.000F, 1.000F).withRotation(new Quaternion(0.107F, -0.537F, 0.000F, 0.843F)).finish();
    public static final IRenderConfig VECTOR_RIGHT = RenderConfig.newDef().withPosition(0.250, -0.380, -0.400).withScale(1.000F, 1.000F, 1.000F).withRotation(new Quaternion(0.134F, -0.383F, 0.000F, 0.924F)).finish();
    public static final IRenderConfig CROSSBOW_LEFT = RenderConfig.newDef().withPosition(0.250, -0.900, -0.200).withScale(1.000F, 1.000F, 1.500F).withRotation(new Quaternion(0.122F, -0.609F, 0.000F, 0.793F)).finish();
    public static final IRenderConfig CROSSBOW_RIGHT = RenderConfig.newDef().withPosition(0.600, -0.800, -0.500).withScale(1.000F, 1.000F, 1.000F).withRotation(new Quaternion(0.152F, -0.216F, 0.000F, 0.976F)).finish();
    public static final IRenderConfig CHUKONU_LEFT = RenderConfig.newDef().withPosition(0.100, -0.700, -0.300).withScale(0.700F, 0.700F, 1.000F).withRotation(new Quaternion(0.106F, -0.707F, 0.000F, 0.707F)).finish();
    public static final IRenderConfig CHUKONU_RIGHT = RenderConfig.newDef().withPosition(0.300, -0.400, -0.450).withScale(1.000F, 1.000F, 1.000F).withRotation(new Quaternion(0.085F, -0.423F, 0.000F, 0.906F)).finish();
    public static final IRenderConfig S1897_LEFT = RenderConfig.newDef().withPosition(0.15, -0.423, 0.4).withScale(1.0F, 1.0F, 1.5F).withRotation(new Quaternion(0.1F, -0.5F, 0.0F, 0.866F)).finish();
    public static final IRenderConfig S1897_RIGHT = RenderConfig.newDef().withPosition(0.27, -0.3, 0.0).withScale(1.0F, 1.0F, 1.0F).withRotation(new Quaternion(0.152F, -0.216F, 0.0F, 0.976F)).finish();
    public static final IRenderConfig S686_LEFT = RenderConfig.newDef().withPosition(0.050, -0.420, 0.000).withScale(1.000F, 1.000F, 1.000F).withRotation(new Quaternion(0.086F, -0.574F, 0.000F, 0.819F)).finish();
    public static final IRenderConfig S686_RIGHT = RenderConfig.newDef().withPosition(0.350, -0.270, -0.200).withScale(0.700F, 0.700F, 1.000F).withRotation(new Quaternion(0.085F, -0.423F, 0.000F, 0.906F)).finish();
    public static final IRenderConfig S12K_LEFT = RenderConfig.newDef().withPosition(-0.150, -0.350, 0.180).withScale(0.700F, 0.700F, 1.000F).withRotation(new Quaternion(0.096F, -0.643F, 0.000F, 0.766F)).finish();
    public static final IRenderConfig S12K_RIGHT = RenderConfig.newDef().withPosition(0.050, -0.180, -0.120).withScale(1.000F, 1.000F, 1.000F).withRotation(new Quaternion(0.085F, -0.423F, 0.000F, 0.906F)).finish();
    public static final IRenderConfig AKM_LEFT = RenderConfig.newDef().withPosition(-0.050, -0.450, 0.150).withScale(0.800F, 1.000F, 1.000F).withRotation(new Quaternion(0.122F, -0.609F, 0.000F, 0.793F)).finish();
    public static final IRenderConfig AKM_RIGHT = RenderConfig.newDef().withPosition(0.200, -0.250, -0.120).withScale(1.000F, 1.000F, 1.000F).withRotation(new Quaternion(0.068F, -0.342F, 0.000F, 0.940F)).finish();
    public static final IRenderConfig HK416_LEFT = RenderConfig.newDef().withPosition(0.200, -0.500, -0.100).withScale(0.700F, 0.700F, 1.000F).withRotation(new Quaternion(0.115F, -0.574F, 0.000F, 0.819F)).finish();
    public static final IRenderConfig HK416_RIGHT = RenderConfig.newDef().withPosition(0.280, -0.300, -0.200).withScale(1.000F, 1.000F, 1.000F).withRotation(new Quaternion(0.068F, -0.342F, 0.000F, 0.940F)).finish();
    public static final IRenderConfig AUG_LEFT = RenderConfig.newDef().withPosition(0.100, -0.500, -0.100).withScale(0.700F, 0.700F, 1.000F).withRotation(new Quaternion(0.086F, -0.574F, 0.000F, 0.819F)).finish();
    public static final IRenderConfig AUG_RIGHT = RenderConfig.newDef().withPosition(0.150, -0.340, -0.500).withScale(1.000F, 1.000F, 1.000F).withRotation(new Quaternion(0.077F, -0.383F, 0.000F, 0.924F)).finish();
    public static final IRenderConfig SKS_LEFT = RenderConfig.newDef().withPosition(0.0, -0.6, 1.2).withScale(1.0F, 1.0F, 2.5F).withRotation(new Quaternion(0.115F, -0.574F, 0.000F, 0.819F)).finish();
    public static final IRenderConfig SKS_RIGHT = RenderConfig.newDef().withPosition(0.3, -0.5, -0.05).withScale(1.0F, 1.0F, 1.0F).withRotation(new Quaternion(0.137F, -0.342F, 0.000F, 0.940F)).finish();
    public static final IRenderConfig VSS_LEFT = RenderConfig.newDef().withPosition(0.130, -0.500, -0.150).withScale(0.700F, 0.700F, 1.000F).withRotation(new Quaternion(0.086F, -0.574F, 0.000F, 0.819F)).finish();
    public static final IRenderConfig VSS_RIGHT = RenderConfig.newDef().withPosition(0.150, -0.340, -0.400).withScale(1.000F, 1.000F, 1.000F).withRotation(new Quaternion(0.077F, -0.383F, 0.000F, 0.924F)).finish();
    public static final IRenderConfig MK14_LEFT = RenderConfig.newDef().withPosition(0.060, -0.440, 0.000).withScale(0.600F, 0.600F, 1.000F).withRotation(new Quaternion(0.086F, -0.574F, 0.000F, 0.819F)).finish();
    public static final IRenderConfig MK14_RIGHT = RenderConfig.newDef().withPosition(0.060, -0.280, -0.300).withScale(1.000F, 1.000F, 1.000F).withRotation(new Quaternion(0.085F, -0.423F, 0.000F, 0.906F)).finish();
    public static final IRenderConfig KAR98K_LEFT = RenderConfig.newDef().withPosition(0.400, -1.000, -0.400).withScale(1.300F, 1.000F, 1.500F).withRotation(new Quaternion(0.150F, -0.500F, 0.000F, 0.866F)).finish();
    public static final IRenderConfig KAR98K_RIGHT = RenderConfig.newDef().withPosition(0.400, -0.600, -0.500).withScale(1.000F, 1.000F, 1.000F).withRotation(new Quaternion(0.115F, -0.383F, 0.000F, 0.924F)).finish();
    public static final IRenderConfig WINCHESTER_LEFT = RenderConfig.newDef().withPosition(0.150, -0.500, -0.100).withScale(0.600F, 0.600F, 1.000F).withRotation(new Quaternion(0.086F, -0.574F, 0.000F, 0.819F)).finish();
    public static final IRenderConfig WINCHESTER_RIGHT = RenderConfig.newDef().withPosition(0.150, -0.350, -0.400).withScale(1.000F, 1.000F, 1.000F).withRotation(new Quaternion(0.085F, -0.423F, 0.000F, 0.906F)).finish();
    public static final IRenderConfig AWM_LEFT = RenderConfig.newDef().withPosition(0.000, -0.400, 0.000).withScale(0.500F, 0.500F, 1.000F).withRotation(new Quaternion(0.099F, -0.707F, 0.000F, 0.707F)).finish();
    public static final IRenderConfig AWM_RIGHT = RenderConfig.newDef().withPosition(0.150, -0.270, -0.200).withScale(0.800F, 0.800F, 1.000F).withRotation(new Quaternion(0.085F, -0.423F, 0.000F, 0.906F)).finish();
    public static final IRenderConfig M203_LEFT = RenderConfig.newDef().withPosition(0.100, -0.750, -0.200).withScale(0.600F, 0.600F, 1.400F).withRotation(new Quaternion(0.141F, -0.707F, 0.000F, 0.707F)).finish();
    public static final IRenderConfig M203_RIGHT = RenderConfig.newDef().withPosition(0.300, -0.400, -0.400).withScale(1.000F, 1.000F, 1.000F).withRotation(new Quaternion(0.085F, -0.423F, 0.000F, 0.906F)).finish();
    public static final IRenderConfig ROCKET_LAUNCHER_LEFT = RenderConfig.newDef().withPosition(-0.100, -0.680, 0.000).withScale(1.000F, 1.000F, 1.000F).withRotation(new Quaternion(0.115F, -0.574F, 0.000F, 0.819F)).finish();
    public static final IRenderConfig ROCKET_LAUNCHER_RIGHT = RenderConfig.newDef().withPosition(0.100, -0.300, -0.400).withScale(1.000F, 1.000F, 1.000F).withRotation(new Quaternion(0.085F, -0.423F, 0.000F, 0.906F)).finish();

    // ATTACHMENTS ===============================
    public static final IRenderConfig M1911_SUPPRESSOR = RenderConfig.newDef().withPosition(0.0, -0.17, 0.0).withScale(1.1F, 1.1F, 1.5F).finish();
    public static final IRenderConfig UMP45_SUPPRESSOR = RenderConfig.newDef().withPosition(0.0, -0.5, -1.0).withScale(1.0F, 1.0F, 1.5F).finish();
    public static final IRenderConfig UMP45_REFLEX = RenderConfig.newDef().withPosition(0.0, -0.815, 0.7).finish();
    public static final IRenderConfig WOODEN_CROSSBOW_SCOPE = RenderConfig.newDef().withPosition(0.0, 0.15, 0.5).withScale(0.6F, 0.6F, 0.6F).finish();
    public static final IRenderConfig SKS_SUPPRESSOR = RenderConfig.newDef().withPosition(0.0, -1.33, -5.0).withScale(1.4F, 1.4F, 2.0F).finish();
    public static final IRenderConfig SKS_REFLEX = RenderConfig.newDef().withPosition(0.0, -1.9, -1.1).withScale(1.3F, 1.3F, 1.3F).finish();
    public static final IRenderConfig KAR98K_SUPPRESSOR = RenderConfig.newDef().withPosition(0.0, -0.6, -5.7).withScale(1.5F, 1.5F, 2.0F).finish();
    public static final IRenderConfig KAR98K_SCOPE = IRenderConfig.pos(0.0, -0.2, 0.1);
}
