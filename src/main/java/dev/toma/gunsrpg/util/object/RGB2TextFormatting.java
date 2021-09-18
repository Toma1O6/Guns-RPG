package dev.toma.gunsrpg.util.object;

import net.minecraft.util.text.TextFormatting;

import java.util.Arrays;

public class RGB2TextFormatting {

    private static final LazyLoader<TextFormatting[]> COLOR_LOADER = new LazyLoader<>(RGB2TextFormatting::initColors);

    public static TextFormatting getClosestFormat(int color) {
        TextFormatting[] colors = COLOR_LOADER.get();
        int minDistIndex = 0;
        int minDist = Integer.MAX_VALUE;
        for (int i = 0; i < colors.length; i++) {
            TextFormatting formatting = colors[i];
            int paletteColor = formatting.getColor();
            int distance = distance(color, paletteColor);
            if (distance < minDist) {
                minDistIndex = i;
                minDist = distance;
            }
        }
        return colors[minDistIndex];
    }

    private static int distance(int color, int paletteColor) {
        int r1 = (color >> 16) & 255;
        int r2 = (paletteColor >> 16) & 255;
        int g1 = (color >> 8) & 255;
        int g2 = (paletteColor >> 8) & 255;
        int b1 = color & 255;
        int b2 = paletteColor & 255;
        return sqr(r2 - r1) + sqr(g2 - g1) + sqr(b2 - b1);
    }

    private static TextFormatting[] initColors() {
        return Arrays.stream(TextFormatting.values()).filter(TextFormatting::isColor).toArray(TextFormatting[]::new);
    }

    private static int sqr(int n) {
        return n*n;
    }
}
