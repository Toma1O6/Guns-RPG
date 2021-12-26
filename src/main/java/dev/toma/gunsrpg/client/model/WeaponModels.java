package dev.toma.gunsrpg.client.model;

import dev.toma.gunsrpg.client.model.component.*;
import dev.toma.gunsrpg.client.model.weapon.*;
import lib.toma.animations.api.AnimationStage;

import java.util.*;
import java.util.stream.Collectors;

public final class WeaponModels {

    private WeaponModels() {}

    public static final AbstractWeaponModel M1911 = new M1911Model();
    public static final AbstractWeaponModel R45 = new R45Model();
    public static final AbstractWeaponModel DESERT_EAGLE = new DesertEagleModel();
    public static final AbstractWeaponModel WOODEN_CROSSBOW = new WoodenCrossbowModel();
    public static final AbstractWeaponModel CHU_KO_NU = new ChuKoNuModel();
    public static final AbstractWeaponModel UMP_45 = new Ump45Model();
    public static final AbstractWeaponModel THOMPSON = new ThompsonModel();
    public static final AbstractWeaponModel VECTOR = new VectorModel();
    public static final AbstractWeaponModel S1897 = new S1897Model();
    public static final AbstractWeaponModel S686 = new S686Model();
    public static final AbstractWeaponModel S12K = new S12kModel();
    public static final AbstractWeaponModel SKS = new SksModel();
    public static final AbstractWeaponModel VSS = new VssModel();
    public static final AbstractWeaponModel AKM = new AKMModel();
    public static final AbstractWeaponModel HK416 = new HK416Model();
    public static final AbstractWeaponModel AUG = new AugModel();
    public static final AbstractWeaponModel KAR98K = new Kar98kModel();
    public static final AbstractWeaponModel WINCHESTER = new WinchesterModel();
    public static final AbstractWeaponModel MK14 = new Mk14Model();
    public static final AbstractWeaponModel AWM = new AwmModel();
    public static final AbstractWeaponModel GRENADE_LAUNCHER = new GrenadeLauncherModel();
    public static final AbstractWeaponModel ROCKET_LAUNCHER = new RocketLauncherModel();

    public static final AbstractAttachmentModel SCOPE = new ScopeModel();
    public static final AbstractAttachmentModel SUPPRESSOR = new SuppressorModel();
    public static final AbstractAttachmentModel REFLEX = new ReflexSightModel();
    public static final AbstractAttachmentModel PSO_SCOPE = new PSOScopeModel();

    public static Map<AbstractWeaponModel, Collection<AnimationStage>> getSupportedPartAnimations() {
        List<AbstractWeaponModel> list = Arrays.asList(
                M1911, R45, DESERT_EAGLE, WOODEN_CROSSBOW, CHU_KO_NU, UMP_45, THOMPSON, VECTOR, S1897, S686,
                S12K, SKS, VSS, AKM, HK416, AUG, KAR98K, WINCHESTER, MK14, AWM, GRENADE_LAUNCHER, ROCKET_LAUNCHER
        );
        Map<AbstractWeaponModel, Collection<AnimationStage>> map = new LinkedHashMap<>();
        for (AbstractWeaponModel weaponModel : list) {
            map.put(
                    weaponModel,
                    weaponModel.getSpecialRenderers().stream().map(AbstractWeaponModel.SpecialRenderer::target).collect(Collectors.toList())
            );
        }
        return map;
    }
}
