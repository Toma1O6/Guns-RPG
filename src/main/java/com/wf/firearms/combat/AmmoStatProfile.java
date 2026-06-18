package com.wf.firearms.combat;

/** 弹药属性修正（对标 Guns RPG {@code MaterialData}：后坐力/耐久/卡弹率为加算系数）。 */
public record AmmoStatProfile(float recoilAdd, float durabilityAdd, float jamChanceAdd) {
    public static final AmmoStatProfile EMPTY = new AmmoStatProfile(0f, 0f, 0f);

    /** UI 显示用百分数（-0.1 → -10）。 */
    public int recoilPercent() {
        return Math.round(recoilAdd * 100f);
    }

    public int durabilityPercent() {
        return Math.round(durabilityAdd * 100f);
    }

    public int jamPercent() {
        return Math.round(jamChanceAdd * 100f);
    }
}
