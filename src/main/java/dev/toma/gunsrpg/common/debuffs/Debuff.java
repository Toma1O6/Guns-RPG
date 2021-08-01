package dev.toma.gunsrpg.common.debuffs;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.common.capability.IPlayerData;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

public abstract class Debuff implements INBTSerializable<CompoundNBT> {

    public static final DamageSource POISON_DAMAGE = new DamageSource("poison").bypassArmor();
    public static final DamageSource INFECTION_DAMAGE = new DamageSource("infection").bypassArmor();
    public static final DamageSource BLEED_DAMAGE = new DamageSource("bleeding").bypassArmor();

    private final DebuffType<?> type;
    private int debuffLevel = 0;
    private int levelTimer = 0;
    private int currentStage;
    private boolean invalid = false;
    private RenderStat renderStat;

    public Debuff(DebuffType<?> type) {
        this.type = type;
    }

    protected abstract ResourceLocation getIconTexture();

    public final void onDebuffObtained() {
        dispatchRenderAction(20, 0xffffff);
        debuffLevel = 0;
        levelTimer = 0;
    }

    public final void tick(PlayerEntity player, IPlayerData data) {
        if (renderStat != null) {
            if (renderStat.tick()) {
                renderStat = null;
            }
        }
        ++levelTimer;
        int target = this.getType().getProgressDelay(data.getSkills());
        if (this.canContinueSpreading() && levelTimer >= target) {
            ++debuffLevel;
            levelTimer = 0;
            this.dispatchRenderAction(25, 0xbb0000);
            this.updateStageIndex();
            if (!player.level.isClientSide) data.sync();
        }
        this.getType().tickStage(currentStage, player);
    }

    public final void heal(int amount) {
        debuffLevel -= amount;
        dispatchRenderAction(20, 0x009944);
        if (debuffLevel <= 0)
            invalidate();
    }

    public void invalidate() {
        invalid = true;
    }

    @OnlyIn(Dist.CLIENT)
    public void draw(MatrixStack stack, int x, int y, int w, int h, float pt, FontRenderer renderer) {
        Matrix4f pose = stack.last().pose();
        ModUtils.renderColor(pose, x, y, x + w, y + h, 0.0F, 0.0F, 0.0F, 0.6F);
        ModUtils.renderTexture(pose, x + 2, y + 1, x + 18, y + 17, this.getIconTexture());
        renderer.drawShadow(stack, debuffLevel + "%", x + 20, y + 5, 0xffffff);
        if (renderStat != null) {
            renderStat.draw(stack, x, y, w, h, pt);
        }
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compound = new CompoundNBT();
        compound.putInt("level", debuffLevel);
        compound.putInt("timer", levelTimer);
        compound.putInt("stage", currentStage);
        compound.putBoolean("invalid", invalid);
        if (renderStat != null) compound.put("stat", renderStat.serializeNBT());
        return compound;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        debuffLevel = nbt.getInt("level");
        levelTimer = nbt.getInt("timer");
        currentStage = nbt.getInt("stage");
        invalid = nbt.getBoolean("invalid");
        if (nbt.contains("stat", Constants.NBT.TAG_COMPOUND)) {
            renderStat = new RenderStat(nbt.getCompound("stat"));
        }
    }

    public void dispatchRenderAction(int actionLength, int color) {
        this.renderStat = new RenderStat(color, (short) actionLength);
    }

    public DebuffType<?> getType() {
        return type;
    }

    public boolean canContinueSpreading() {
        return debuffLevel < 100;
    }

    protected void updateStageIndex() {
        this.currentStage = this.getType().getAppropriateStage(this.debuffLevel);
    }

    public boolean isInvalid() {
        return invalid;
    }

    public static class RenderStat implements INBTSerializable<CompoundNBT> {

        float r, g, b;
        short startTimer;
        short timer;

        RenderStat(CompoundNBT nbt) {
            deserializeNBT(nbt);
        }

        RenderStat(int color, short timer) {
            this.startTimer = timer;
            this.timer = timer;
            this.r = ((color >> 16) & 0xff) / 255.0F;
            this.g = ((color >> 8) & 0xff) / 255.0F;
            this.b = (color & 0xff) / 255.0F;
        }

        void draw(MatrixStack stack, int x, int y, int w, int h, float pt) {
            float f0 = timer / (float) startTimer;
            float f1 = 1.0F / startTimer;
            float f2 = intN(f0, f1, pt);
            ModUtils.renderColor(stack.last().pose(), x, y, x + w, y + h, r, g, b, f2);
        }

        float intN(float in, float add, float pt) {
            float prev = in <= 0.0F ? 0 : Math.min(1.0F, in + add);
            return prev + (in - prev) * pt;
        }

        boolean tick() {
            --timer;
            return timer <= 0;
        }

        @Override
        public CompoundNBT serializeNBT() {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putFloat("r", r);
            nbt.putFloat("g", g);
            nbt.putFloat("b", b);
            nbt.putShort("initial", startTimer);
            nbt.putShort("current", timer);
            return nbt;
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            r = nbt.getFloat("r");
            g = nbt.getFloat("g");
            b = nbt.getFloat("b");
            startTimer = nbt.getShort("initial");
            timer = nbt.getShort("current");
        }
    }
}
