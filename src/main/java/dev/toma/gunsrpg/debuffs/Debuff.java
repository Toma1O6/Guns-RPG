package dev.toma.gunsrpg.debuffs;

import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class Debuff implements INBTSerializable<NBTTagCompound> {

    public final DebuffType<?> type;
    protected boolean active;
    private int level;
    private int timer;

    public Debuff(DebuffType<?> type) {
        this.type = type;
    }

    public abstract ResourceLocation getIconTexture();

    public final void onTick(EntityPlayer player) {
        if(!player.world.isRemote) {
            if(active && level < 100 && ++timer >= type.timecap) {
                ++level;
                timer = 0;
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
            float chance = result.getChance();
            if(new Random().nextFloat() <= chance) {
                this.active = true;
                PlayerDataFactory.get(player).sync();
                break;
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

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setBoolean("active", active);
        nbt.setInteger("level", level);
        nbt.setInteger("timer", timer);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.active = nbt.getBoolean("active");
        this.level = nbt.getInteger("level");
        this.timer = nbt.getInteger("timer");
    }
}
