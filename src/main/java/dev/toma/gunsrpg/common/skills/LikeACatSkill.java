package dev.toma.gunsrpg.common.skills;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.common.skills.interfaces.Clickable;
import dev.toma.gunsrpg.common.skills.interfaces.Cooldown;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.SPacketSkillClicked;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.vector.Matrix4f;

public class LikeACatSkill extends BasicSkill implements Cooldown, Clickable {

    private final int effectLength;
    private final int totalCooldown;
    private int effectLeft;
    private int cooldown;

    public LikeACatSkill(SkillType<?> type, int totalCooldown, int effectLength) {
        super(type);
        this.totalCooldown = totalCooldown;
        this.effectLength = effectLength;
    }

    @Override
    public boolean apply(PlayerEntity user) {
        return effectLeft == 0 && cooldown == 0;
    }

    @Override
    public void onUpdate(PlayerEntity player) {
        if (effectLeft > 0) {
            --effectLeft;
            if (effectLeft == 0) {
                setOnCooldown();
            }
        } else if (cooldown > 0) {
            --cooldown;
        }
    }

    @Override
    public int getCooldown() {
        return cooldown;
    }

    @Override
    public int getMaxCooldown() {
        return totalCooldown;
    }

    @Override
    public void setOnCooldown() {
        this.cooldown = totalCooldown;
    }

    @Override
    public void onUse(PlayerEntity player) {

    }

    @Override
    public void clientHandleClicked() {
        NetworkManager.sendServerPacket(new SPacketSkillClicked(this.getType()));
    }

    @Override
    public void clicked(PlayerEntity player) {
        effectLeft = effectLength;
        player.addEffect(new EffectInstance(Effects.NIGHT_VISION, effectLength, 0, false, false));
        PlayerDataFactory.get(player).ifPresent(PlayerData::sync);
    }

    @Override
    public void drawOnTop(MatrixStack stack, int x, int y, int width, int heigth) {
        float f;
        float r;
        float g;
        float b;
        int time;
        if (effectLeft > 0) {
            f = 1.0F - effectLeft / (float) effectLength;
            if (f == 0) return;
            r = 0.0F;
            g = 1.0F;
            b = 0.2F;
            time = effectLeft;
        } else {
            f = getCooldown() / (float) getMaxCooldown();
            if (f == 0) return;
            r = 1.0F;
            g = 0.2F;
            b = 0.0F;
            time = getCooldown();
        }
        stack.pushPose();
        stack.translate(0, 0, 1);
        Matrix4f pose = stack.last().pose();
        ModUtils.renderColor(pose, x, y + heigth, x + width, y + heigth + 3, 0.0F, 0.0F, 0.0F, 1.0F);
        ModUtils.renderColor(pose, x + 1, y + heigth + 1, x + 1 + (int) ((width - 1) * f), y + heigth + 2, r, g, b, 1.0F);
        String cooldown = ModUtils.formatTicksToTime(time);
        FontRenderer renderer = Minecraft.getInstance().font;
        renderer.draw(stack, cooldown, x + (width - renderer.width(cooldown)) / 2f, y + heigth + 4, 0xffffff);
        stack.popPose();
    }

    @Override
    public void writeExtra(CompoundNBT nbt) {
        nbt.putInt("effect", effectLeft);
        nbt.putInt("cooldown", cooldown);
    }

    @Override
    public void readExtra(CompoundNBT nbt) {
        effectLeft = nbt.getInt("effect");
        cooldown = nbt.getInt("cooldown");
    }
}
