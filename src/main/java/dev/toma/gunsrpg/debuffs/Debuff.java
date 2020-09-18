package dev.toma.gunsrpg.debuffs;

import dev.toma.gunsrpg.common.CommonEventHandler;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class Debuff implements INBTSerializable<NBTTagCompound> {

    public final DebuffType type;
    protected boolean active;
    private int level;
    private int timer;
    private short appearTimer;
    private short hurtTimer;

    public Debuff(DebuffType type) {
        this.type = type;
    }

    protected abstract ResourceLocation getIconTexture();

    public final void onTick(EntityPlayer player) {
        if(appearTimer > 0) {
            --appearTimer;
        }
        if(hurtTimer > 0) {
            --hurtTimer;
        }
        if(!player.world.isRemote) {
            if(active && level < 100 && ++timer >= type.timecap + type.extraTime.apply(PlayerDataFactory.get(player).getSkills())) {
                ++level;
                timer = 0;
                hurtTimer = 25;
                PlayerDataFactory.get(player).sync();
            }
            for(Pair<Predicate<Integer>, Consumer<EntityPlayer>> entry : type.effectList) {
                if(isActive() && entry.getLeft().test(level)) {
                    entry.getRight().accept(player);
                }
            }
        }
    }

    public final void onHurt(DamageSource source, EntityPlayer player) {
        if(active) return;
        for(DamageResult result : this.type.entityChances) {
            if(!result.allows(source)) continue;
            float resistance = type.resistance.apply(PlayerDataFactory.get(player).getSkills());
            if(CommonEventHandler.random.nextFloat() > resistance) {
                float chance = result.getChance(player);
                if(CommonEventHandler.random.nextFloat() <= chance) {
                    this.active = true;
                    appearTimer = 20;
                    PlayerDataFactory.get(player).sync();
                    break;
                }
            }
        }
    }

    public void heal(int amount) {
        this.level = Math.max(0, level - amount);
        if(level == 0) this.active = false;
    }

    public void completeHeal() {
        this.active = false;
        this.level = 0;
        this.timer = 0;
    }

    public void toggle() {
        this.active = !active;
        this.level = 0;
    }

    public boolean isActive() {
        return active;
    }

    public int getLevel() {
        return level;
    }

    @SideOnly(Side.CLIENT)
    public void draw(int x, int y, int w, int h, float partial, FontRenderer renderer) {
        ModUtils.renderColor(x, y, x + w, y + h, 0.0F, 0.0F, 0.0F, 0.6F);
        ModUtils.renderTexture(x + 2, y + 1, x + 18, y + 17, this.getIconTexture());
        renderer.drawStringWithShadow(this.getLevel() + "%", x + 20, y + 5, 0xffffff);
        this.renderEffects(x, y, w, h, partial);
    }

    @SideOnly(Side.CLIENT)
    private void renderEffects(int x, int y, int w, int h, float partial) {
        float f = interpNum(appearTimer / 20f, 0.05f, partial);
        if(f > 0) {
            ModUtils.renderColor(x, y, x + w, y + h, 1.0F, 1.0F, 1.0F, f);
        }
        float f1 = interpNum(hurtTimer / 25f, 0.04f, partial);
        if(f1 > 0) {
            ModUtils.renderColor(x, y, x + w, y + h, 0.75F, 0.0F, 0.0F, f1);
        }
    }

    private float interpNum(float v, float p, float part) {
        float prev = v <= 0.0F ? 0 : Math.min(1.0F, v + p);
        return prev + (v - prev) * part;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setBoolean("active", active);
        nbt.setInteger("level", level);
        nbt.setInteger("timer", timer);
        nbt.setShort("appear", appearTimer);
        nbt.setShort("hurt", hurtTimer);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.active = nbt.getBoolean("active");
        this.level = nbt.getInteger("level");
        this.timer = nbt.getInteger("timer");
        this.appearTimer = nbt.getShort("appear");
        this.hurtTimer = nbt.getShort("hurt");
    }
}
