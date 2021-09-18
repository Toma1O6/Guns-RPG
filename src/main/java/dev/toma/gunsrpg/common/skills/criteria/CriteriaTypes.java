package dev.toma.gunsrpg.common.skills.criteria;

import dev.toma.gunsrpg.api.common.IUnlockCriteria;
import dev.toma.gunsrpg.common.init.ModItems;

public class CriteriaTypes {

    private static final IUnlockCriteria DEFAULT_CRITERIA = new PlayerLevelCriteria();
    private static final IUnlockCriteria PISTOL_CRITERIA = new GunCriteria(() -> ModItems.M1911);
    private static final IUnlockCriteria SMG_CRITERIA = new GunCriteria(() -> ModItems.UMP45);
    private static final IUnlockCriteria CROSSBOW_CRITERIA = new GunCriteria(() -> ModItems.WOODEN_CROSSBOW);
    private static final IUnlockCriteria SHOTGUN_CRITERIA = new GunCriteria(() -> ModItems.S1897);
    private static final IUnlockCriteria ASSALT_RIFLE_CRITERIA = new GunCriteria(() -> ModItems.SKS);
    private static final IUnlockCriteria SNIPER_RIFLE_CRITERIA = new GunCriteria(() -> ModItems.KAR98K);

    public static IUnlockCriteria getDefaultCriteria() {
        return DEFAULT_CRITERIA;
    }

    public static IUnlockCriteria getPistolCriteria() {
        return PISTOL_CRITERIA;
    }

    public static IUnlockCriteria getSmgCriteria() {
        return SMG_CRITERIA;
    }

    public static IUnlockCriteria getCrossbowCriteria() {
        return CROSSBOW_CRITERIA;
    }

    public static IUnlockCriteria getShotgunCriteria() {
        return SHOTGUN_CRITERIA;
    }

    public static IUnlockCriteria getAssaltRifleCriteria() {
        return ASSALT_RIFLE_CRITERIA;
    }

    public static IUnlockCriteria getSniperRifleCriteria() {
        return SNIPER_RIFLE_CRITERIA;
    }
}
