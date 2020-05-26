package dev.toma.gunsrpg.client.animation;

import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.vecmath.Vector3f;
import java.util.function.Consumer;

@SideOnly(Side.CLIENT)
public class AimingAnimation implements Animation {

    private final EntityPlayer player;
    private final Vector3f animation;
    private float current, prev;
    public float smooth;
    private Consumer<AimingAnimation> leftArm = animation -> {};
    private Consumer<AimingAnimation> rightArm = animation -> {};

    public AimingAnimation(float x, float y, float z) {
        this(new Vector3f(x, y, z));
    }

    public AimingAnimation(Vector3f animation) {
        this.player = Minecraft.getMinecraft().player;
        this.animation = animation;
    }

    public AimingAnimation animateRight(Consumer<AimingAnimation> consumer) {
        this.rightArm = consumer;
        return this;
    }

    public AimingAnimation animateLeft(Consumer<AimingAnimation> consumer) {
        this.leftArm = consumer;
        return this;
    }

    @Override
    public void animateItemHands(float partialTicks) {
        PlayerData data = PlayerDataFactory.get(player);
        this.current = data.getAimInfo().getProgress();
        this.smooth = this.prev + (this.current - this.prev) * partialTicks;
    }

    @Override
    public void animateItem(float partialTicks) {
        GlStateManager.translate(animation.x * smooth, animation.y * smooth, animation.z * smooth);
    }

    @Override
    public void animateHands(float partialTicks) {
    }

    @Override
    public void animateLeftArm(float partialTicks) {
        leftArm.accept(this);
    }

    @Override
    public void animateRightArm(float partialTicks) {
        rightArm.accept(this);
    }

    @Override
    public void clientTick() {
        prev = current;
    }
}
