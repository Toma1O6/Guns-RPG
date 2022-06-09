package dev.toma.gunsrpg.client.animation;

import lib.toma.animations.api.Animation;
import lib.toma.animations.api.IKeyframeProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;

public class InfiniteAnimation extends Animation {

    private final Item usedItem;

    public InfiniteAnimation(IKeyframeProvider provider, int length) {
        super(provider, length);
        this.usedItem = Minecraft.getInstance().player.getMainHandItem().getItem();
    }

    @Override
    public float getProgress() {
        return Math.min(1.0F, super.getProgress());
    }

    @Override
    public boolean hasFinished() {
        Item item = Minecraft.getInstance().player.getMainHandItem().getItem();
        return item != usedItem;
    }
}
