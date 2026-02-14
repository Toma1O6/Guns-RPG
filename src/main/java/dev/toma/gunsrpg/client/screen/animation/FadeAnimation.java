package dev.toma.gunsrpg.client.screen.animation;

public class FadeAnimation {

    public static final FadeAnimation NO_FADE = new NoFadeAnimation();

    private final long duration;
    private long lastActiveTs;

    public FadeAnimation(long duration) {
        this.duration = duration;
    }

    public static FadeAnimation createDefault() {
        return new FadeAnimation(500L);
    }

    public void reset() {
        this.lastActiveTs = System.currentTimeMillis();
    }

    public float getProgress() {
        long ts = System.currentTimeMillis();
        long diff = ts - this.lastActiveTs;
        if (diff >= this.duration) {
            return 1.0F;
        }
        return diff / (float) this.duration;
    }

    public float getInvertedProgress() {
        return 1.0F - getProgress();
    }

    public int getAdjustedAlphaColor(int color, float amount) {
        float alpha = ((color >> 24) & 0xFF) / 255F;
        float newAlpha = alpha * amount;
        int newAlphaI = (int) (newAlpha * 255);
        return (color & 0xFFFFFF) | (newAlphaI << 24);
    }

    private static final class NoFadeAnimation extends FadeAnimation {

        private NoFadeAnimation() {
            super(0);
        }

        @Override
        public void reset() {
        }

        @Override
        public float getProgress() {
            return 0.0F;
        }

        @Override
        public int getAdjustedAlphaColor(int color, float amount) {
            return color;
        }
    }
}
