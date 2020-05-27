package dev.toma.gunsrpg.client.animation;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class AbstractAnimation implements Animation {

    protected final EntityPlayer player;
    protected float current, prev;
    public float smooth;

    public AbstractAnimation() {
        player = Minecraft.getMinecraft().player;
    }

    public abstract float getCurrentProgress();

    public abstract void update();

    public final void calculateSmooth(float partialTicks) {
        this.current = this.getCurrentProgress();
        this.smooth = this.prev + (this.current - this.prev) * partialTicks;
    }

    @Override
    public final void clientTick() {
        this.prev = current;
        this.update();
    }

    @Override
    public void animateRightArm(float partialTicks) {

    }

    @Override
    public void animateLeftArm(float partialTicks) {

    }

    @Override
    public void animateItem(float partialTicks) {

    }

    @Override
    public void animateHands(float partialTicks) {

    }

    @Override
    public void animateItemHands(float partialTicks) {

    }
}
