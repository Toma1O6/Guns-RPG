package dev.toma.gunsrpg.common.skills.criteria;

import dev.toma.gunsrpg.common.init.ModItems;

public class CriteriaTypes {

    private static final IUnlockCriteria DEFAULT_CRITERIA = new DefaultUnlockCriteria();
    private static final IUnlockCriteria PISTOL_CRITERIA = new GunCriteria(() -> ModItems.PISTOL);
    private static final IUnlockCriteria SMG_CRITERIA = new GunCriteria(() -> ModItems.SMG);
    private static final IUnlockCriteria CROSSBOW_CRITERIA = new GunCriteria(() -> ModItems.CROSSBOW);
    private static final IUnlockCriteria SHOTGUN_CRITERIA = new GunCriteria(() -> ModItems.SHOTGUN);
    private static final IUnlockCriteria ASSALT_RIFLE_CRITERIA = new GunCriteria(() -> ModItems.ASSAULT_RIFLE);
    private static final IUnlockCriteria SNIPER_RIFLE_CRITERIA = new GunCriteria(() -> ModItems.SNIPER_RIFLE);

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
