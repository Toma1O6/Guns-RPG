package com.wf.firearms.combat;

/** 枪械梯度与持枪移速等平衡常量。 */
public final class WeaponBalance {
    private WeaponBalance() {}

    /** 同族内 1=入门、2=进阶、3=顶级，用于流血等命中效果强度。 */
    public static int tier(String weaponKey) {
        return switch (weaponKey) {
            case "m1911", "s1897", "winchester" -> 1;
            case "r45", "thompson", "akm", "s686", "kar98k" -> 2;
            case "desert_eagle", "vector", "s12k", "awm", "pkm", "m249", "gatling", "minigun" -> 3;
            default -> 1;
        };
    }

    /** 持枪移速加成（MULTIPLY_TOTAL，正数加速、负数减速）；0 表示无修正。 */
    public static float heldMoveSpeedBonus(String weaponKey) {
        return switch (weaponKey) {
            case "m1911" -> 0.20f;
            case "r45" -> 0.15f;
            case "desert_eagle" -> 0.10f;
            case "awm" -> -0.20f;
            case "pkm" -> -0.07f;
            case "m249" -> -0.12f;
            case "gatling", "minigun" -> -0.70f;
            default -> 0f;
        };
    }

    /** 枪身基础后坐力倍率（>1 更难压枪）；与配件/天赋叠乘。 */
    public static float recoilMultiplier(String weaponKey, boolean automatic) {
        float mult =
                switch (weaponKey) {
                    case "mk14ebr" -> 1.75f;
                    default -> 1f;
                };
        if (automatic && "mk14ebr".equals(weaponKey)) {
            mult *= 1.8f;
        }
        return mult;
    }

    /** 枪身基础散布倍率（>1 更难控）；全自动可额外放大。 */
    public static float spreadMultiplier(String weaponKey, boolean automatic) {
        float mult =
                switch (weaponKey) {
                    case "mk14ebr" -> 1.3f;
                    default -> 1f;
                };
        if (automatic && "mk14ebr".equals(weaponKey)) {
            mult *= 1.6f;
        }
        return mult;
    }
}
