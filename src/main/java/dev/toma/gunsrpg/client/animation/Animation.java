package dev.toma.gunsrpg.client.animation;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface Animation {

    void animateItemHands(float partialTicks);

    void animateHands(float partialTicks);

    void animateRightArm(float partialTicks);

    void animateLeftArm(float partialTicks);

    void animateItem(float partialTicks);

    void clientTick();

    boolean isFinished();
}
