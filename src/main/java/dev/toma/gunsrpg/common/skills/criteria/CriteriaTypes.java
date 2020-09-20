package dev.toma.gunsrpg.common.skills.criteria;

import dev.toma.gunsrpg.common.init.GRPGItems;

public class CriteriaTypes {

    private static final UnlockCriteria DEFAULT_CRITERIA = new DefaultUnlockCriteria();
    private static final UnlockCriteria PISTOL_CRITERIA = new GunCriteria(() -> GRPGItems.PISTOL);
    private static final UnlockCriteria SMG_CRITERIA = new GunCriteria(() -> GRPGItems.SMG);
    private static final UnlockCriteria CROSSBOW_CRITERIA = new GunCriteria(() -> GRPGItems.CROSSBOW);
    private static final UnlockCriteria SHOTGUN_CRITERIA = new GunCriteria(() -> GRPGItems.SHOTGUN);
    private static final UnlockCriteria ASSALT_RIFLE_CRITERIA = new GunCriteria(() -> GRPGItems.ASSAULT_RIFLE);
    private static final UnlockCriteria SNIPER_RIFLE_CRITERIA = new GunCriteria(() -> GRPGItems.SNIPER_RIFLE);

    public static UnlockCriteria getDefaultCriteria() {
        return DEFAULT_CRITERIA;
    }

    public static UnlockCriteria getPistolCriteria() {
        return PISTOL_CRITERIA;
    }

    public static UnlockCriteria getSmgCriteria() {
        return SMG_CRITERIA;
    }

    public static UnlockCriteria getCrossbowCriteria() {
        return CROSSBOW_CRITERIA;
    }

    public static UnlockCriteria getShotgunCriteria() {
        return SHOTGUN_CRITERIA;
    }

    public static UnlockCriteria getAssaltRifleCriteria() {
        return ASSALT_RIFLE_CRITERIA;
    }

    public static UnlockCriteria getSniperRifleCriteria() {
        return SNIPER_RIFLE_CRITERIA;
    }
}
