package dev.toma.gunsrpg.client.animation;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.gameevent.TickEvent;
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

    @Override
    public void setProgress(float progress) {
        this.current = progress;
    }

    public abstract void update();

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

    @Override
    public void renderTick(float partialTicks, TickEvent.Phase phase) {
        if(phase == TickEvent.Phase.END) return;
        this.calculateSmoothValue(partialTicks);
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    @Override
    public boolean cancelsItemRender() {
        return false;
    }

    private void calculateSmoothValue(float partialTicks) {
        this.current = this.getCurrentProgress();
        this.smooth = this.prev + (this.current - this.prev) * partialTicks;
    }
}
