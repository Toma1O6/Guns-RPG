package dev.toma.gunsrpg.common.debuffs;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.ModRegistry;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class Debuff implements INBTSerializable<NBTTagCompound> {

    public static final DamageSource POISON_DAMAGE = new DamageSource("poison").setDamageBypassesArmor().setDamageIsAbsolute();
    public static final DamageSource INFECTION_DAMAGE = new DamageSource("infection").setDamageBypassesArmor().setDamageIsAbsolute();
    public static final DamageSource BLEED_DAMAGE = new DamageSource("bleeding").setDamageBypassesArmor().setDamageIsAbsolute();

    private final DebuffType type;
    private int debuffLevel = 0;
    private int levelTimer = 0;
    private int currentStage;
    private boolean invalid = false;
    private RenderStat renderStat;

    public Debuff(DebuffType type) {
        this.type = type;
    }

    protected abstract ResourceLocation getIconTexture();

    public final void onDebuffObtained() {
        dispatchRenderAction(20, 0xffffff);
        debuffLevel = 0;
        levelTimer = 0;
    }

    public final void onTick(EntityPlayer player, PlayerData data) {
        if(renderStat != null) {
            if(renderStat.tick()) {
                renderStat = null;
            }
        }
        ++levelTimer;
        int target = this.getType().getProgressDelay(data.getSkills());
        if(this.canContinueSpreading() && levelTimer >= target) {
            ++debuffLevel;
            levelTimer = 0;
            this.dispatchRenderAction(25, 0xbb0000);
            this.updateStageIndex();
            if(!player.world.isRemote) data.sync();
        }
        this.getType().tickStage(currentStage, player);
    }

    public final void heal(int amount) {
        debuffLevel -= amount;
        dispatchRenderAction(20, 0x009944);
        if(debuffLevel <= 0)
            invalidate();
    }

    public void invalidate() {
        invalid = true;
    }

    @SideOnly(Side.CLIENT)
    public void draw(int x, int y, int w, int h, float pt, FontRenderer renderer) {
        ModUtils.renderColor(x, y, x + w, y + h, 0.0F, 0.0F, 0.0F, 0.6F);
        ModUtils.renderTexture(x + 2, y + 1, x + 18, y + 17, this.getIconTexture());
        renderer.drawStringWithShadow(debuffLevel + "%", x + 20, y + 5, 0xffffff);
        if(renderStat != null) {
            renderStat.draw(x, y, w, h, pt);
        }
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger("level", debuffLevel);
        compound.setInteger("timer", levelTimer);
        compound.setInteger("stage", currentStage);
        compound.setBoolean("invalid", invalid);
        if(renderStat != null) compound.setTag("stat", renderStat.serializeNBT());
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        debuffLevel = nbt.getInteger("level");
        levelTimer = nbt.getInteger("timer");
        currentStage = nbt.getInteger("stage");
        invalid = nbt.getBoolean("invalid");
        if(nbt.hasKey("stat", Constants.NBT.TAG_COMPOUND)) {
            renderStat = new RenderStat(nbt.getCompoundTag("stat"));
        }
    }

    public void dispatchRenderAction(int actionLength, int color) {
        this.renderStat = new RenderStat(color, (short) actionLength);
    }

    public DebuffType getType() {
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

    public static class Poison extends Debuff {

        private static final ResourceLocation ICON = GunsRPG.makeResource("textures/icons/poison.png");

        public Poison() {
            super(ModRegistry.Debuffs.POISON);
        }

        @Override
        protected ResourceLocation getIconTexture() {
            return ICON;
        }
    }

    public static class Infection extends Debuff {

        private static final ResourceLocation ICON = GunsRPG.makeResource("textures/icons/infection.png");

        public Infection() {
            super(ModRegistry.Debuffs.INFECTION);
        }

        @Override
        protected ResourceLocation getIconTexture() {
            return ICON;
        }
    }

    public static class Fracture extends Debuff {

        private static final ResourceLocation ICON = GunsRPG.makeResource("textures/icons/fracture.png");

        public Fracture() {
            super(ModRegistry.Debuffs.FRACTURE);
        }

        @Override
        protected ResourceLocation getIconTexture() {
            return ICON;
        }
    }

    public static class Bleeding extends Debuff {

        private static final ResourceLocation ICON = GunsRPG.makeResource("textures/icons/bleed.png");

        public Bleeding() {
            super(ModRegistry.Debuffs.BLEEDING);
        }

        @Override
        protected ResourceLocation getIconTexture() {
            return ICON;
        }
    }

    static class RenderStat implements INBTSerializable<NBTTagCompound> {

        float r, g, b;
        short startTimer;
        short timer;

        RenderStat(NBTTagCompound nbt) {
            deserializeNBT(nbt);
        }

        RenderStat(int color, short timer) {
            this.startTimer = timer;
            this.timer = timer;
            this.r = ((color >> 16) & 0xff) / 255.0F;
            this.g = ((color >>  8) & 0xff) / 255.0F;
            this.b = ( color        & 0xff) / 255.0F;
        }

        void draw(int x, int y, int w, int h, float pt) {
            float f0 = timer / (float) startTimer;
            float f1 = 1.0F / startTimer;
            float f2 = intN(f0, f1, pt);
            ModUtils.renderColor(x, y, x + w, y + h, r, g, b, f2);
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
        public NBTTagCompound serializeNBT() {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setFloat("r", r);
            nbt.setFloat("g", g);
            nbt.setFloat("b", b);
            nbt.setShort("initial", startTimer);
            nbt.setShort("current", timer);
            return nbt;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            r = nbt.getFloat("r");
            g = nbt.getFloat("g");
            b = nbt.getFloat("b");
            startTimer = nbt.getShort("initial");
            timer = nbt.getShort("current");
        }
    }
}
