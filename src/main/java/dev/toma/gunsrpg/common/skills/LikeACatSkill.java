package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.common.skills.interfaces.Clickable;
import dev.toma.gunsrpg.common.skills.interfaces.Cooldown;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.SPacketSkillClicked;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;

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
    public boolean apply(EntityPlayer user) {
        return effectLeft == 0 && cooldown == 0;
    }

    @Override
    public void onUpdate(EntityPlayer player) {
        if(effectLeft > 0) {
            --effectLeft;
            if(effectLeft == 0) {
                setOnCooldown();
            }
        } else if(cooldown > 0) {
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
    public void onUse(EntityPlayer player) {

    }

    @Override
    public void clientHandleClicked() {
        NetworkManager.toServer(new SPacketSkillClicked(this.getType()));
    }

    @Override
    public void clicked(EntityPlayer player) {
        effectLeft = effectLength;
        player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, effectLength, 0, false, false));
        PlayerDataFactory.get(player).sync();
    }

    @Override
    public void drawOnTop(int x, int y, int width, int heigth) {
        float f;
        float r;
        float g;
        float b;
        int time;
        if(effectLeft > 0) {
            f = 1.0F - effectLeft / (float) effectLength;
            if(f == 0) return;
            r = 0.0F;
            g = 1.0F;
            b = 0.2F;
            time = effectLeft;
        } else {
            f = getCooldown() / (float) getMaxCooldown();
            if(f == 0) return;
            r = 1.0F;
            g = 0.2F;
            b = 0.0F;
            time = getCooldown();
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0, 1);
        ModUtils.renderColor(x, y + heigth, x + width, y + heigth + 3, 0.0F, 0.0F, 0.0F, 1.0F);
        ModUtils.renderColor(x + 1, y + heigth + 1, x + 1 + (int)((width - 1) * f), y + heigth + 2, r, g, b, 1.0F);
        String cooldown = ModUtils.formatTicksToTime(time);
        FontRenderer renderer = Minecraft.getMinecraft().fontRenderer;
        renderer.drawString(cooldown, x + (width - renderer.getStringWidth(cooldown)) / 2, y + heigth + 4, 0xffffff);
        GlStateManager.popMatrix();
    }

    @Override
    public void writeExtra(NBTTagCompound nbt) {
        nbt.setInteger("effect", effectLeft);
        nbt.setInteger("cooldown", cooldown);
    }

    @Override
    public void readExtra(NBTTagCompound nbt) {
        effectLeft = nbt.getInteger("effect");
        cooldown = nbt.getInteger("cooldown");
    }
}
