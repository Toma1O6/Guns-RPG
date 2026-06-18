package com.wf.firearms.client.debuff;

import com.wf.firearms.debuff.DebuffType;
import com.wf.firearms.network.DebuffSyncPacket;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.EnumMap;
import java.util.Map;

/** 客户端 debuff HUD 状态（由 {@link DebuffSyncPacket} 驱动，本地 tick 平滑进度与动画）。 */
@OnlyIn(Dist.CLIENT)
public final class ClientDebuffState {
    public static final int ADD_EFFECT_TIME = 35;
    public static final int SLIDE_IN_TIME = 20;
    public static final int EFFECT_LENGTH = 25;

    private static final Map<DebuffType, Entry> ENTRIES = new EnumMap<>(DebuffType.class);
    private static FractureEntry fracture = new FractureEntry();

    static {
        for (DebuffType type : DebuffType.values()) {
            if (type != DebuffType.FRACTURE) {
                ENTRIES.put(type, new Entry());
            }
        }
    }

    private ClientDebuffState() {}

    public static void applySnapshot(DebuffSyncPacket msg) {
        boolean wasFractureActive = fracture.active;
        fracture.active = msg.fractureActive();
        fracture.duration = msg.fractureDuration();
        fracture.maxDuration = Math.max(1, msg.fractureMaxDuration());
        fracture.progress = msg.fractureProgress();
        fracture.progressFill = Math.max(1, fracture.maxDuration / 2);
        if (applyAnim(fracture, msg, DebuffType.FRACTURE)
                && msg.anim() == DebuffSyncPacket.Anim.NONE
                && fracture.active
                && !wasFractureActive) {
            fracture.onApply();
        }

        applyStaged(DebuffType.BLEED, msg.bleedStage(), msg.bleedProgress(), msg.bleedResist(), msg.bleedResistProgress(), msg);
        applyStaged(DebuffType.POISON, msg.poisonStage(), msg.poisonProgress(), false, 0f, msg);
        applyStaged(DebuffType.INFECTION, msg.infectionStage(), msg.infectionProgress(), false, 0f, msg);
    }

    private static void applyStaged(
            DebuffType type,
            int stage,
            int progress,
            boolean resist,
            float resistProgress,
            DebuffSyncPacket msg) {
        Entry e = ENTRIES.get(type);
        if (e == null) {
            return;
        }
        boolean wasActive = e.active;
        e.active = stage > 0;
        e.stage = stage;
        e.progress = progress;
        e.resist = resist;
        e.resistProgress = resistProgress;
        if (applyAnim(e, msg, type) && msg.anim() == DebuffSyncPacket.Anim.NONE && !wasActive && e.active) {
            e.onApply();
        }
    }

    /** @return true if anim was evaluated for this type */
    private static boolean applyAnim(AnimCounters c, DebuffSyncPacket msg, DebuffType type) {
        if (msg.anim() == DebuffSyncPacket.Anim.NONE) {
            return true;
        }
        if (msg.animType() != type) {
            return false;
        }
        switch (msg.anim()) {
            case APPLY -> c.onApply();
            case WORSE -> c.onWorse();
            case HEAL -> c.onHeal();
            case CURE -> c.onCure();
            default -> {
            }
        }
        return true;
    }

    public static void clientTick() {
        fracture.tickCounters();
        if (fracture.active && fracture.duration > 0) {
            fracture.duration--;
            int fill = Math.max(1, fracture.progressFill);
            fracture.progress = Math.min(
                    100, (fracture.maxDuration - fracture.duration) * 100 / fill);
        }
        for (Entry e : ENTRIES.values()) {
            e.tickCounters();
        }
    }

    public static void clear() {
        fracture = new FractureEntry();
        for (DebuffType type : DebuffType.values()) {
            if (type != DebuffType.FRACTURE) {
                ENTRIES.put(type, new Entry());
            }
        }
    }

    public static FractureEntry fracture() {
        return fracture;
    }

    public static Entry entry(DebuffType type) {
        return type == DebuffType.FRACTURE ? null : ENTRIES.get(type);
    }

    public static class AnimCounters {
        public int ticksSinceAdded;
        public int ticksSinceProgressed = 100;
        public int ticksSinceHealed = 100;

        void tickCounters() {
            ticksSinceAdded++;
            ticksSinceProgressed++;
            ticksSinceHealed++;
        }

        void onApply() {
            ticksSinceAdded = 0;
            ticksSinceProgressed = 0;
            ticksSinceHealed = 100;
        }

        void onWorse() {
            ticksSinceProgressed = 0;
            ticksSinceHealed = 100;
        }

        void onHeal() {
            ticksSinceHealed = 0;
            ticksSinceProgressed = 100;
        }

        void onCure() {
            ticksSinceAdded = 0;
            ticksSinceProgressed = 100;
            ticksSinceHealed = 100;
        }
    }

    public static final class Entry extends AnimCounters {
        public boolean active;
        public int stage;
        public int progress;
        public boolean resist;
        public float resistProgress;
    }

    public static final class FractureEntry extends AnimCounters {
        public boolean active;
        public int duration;
        public int maxDuration = 1;
        public int progress;
        public int progressFill = 1;
    }
}
