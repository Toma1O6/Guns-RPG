package dev.toma.gunsrpg.common.capability.object;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.ModRegistry;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.debuffs.DamageContext;
import dev.toma.gunsrpg.common.debuffs.Debuff;
import dev.toma.gunsrpg.common.debuffs.DebuffType;
import dev.toma.gunsrpg.util.function.ToFloatBiFunction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ForgeRegistry;

import java.util.Random;

public class DebuffData implements INBTSerializable<NBTTagList> {

    private final PlayerData data;
    private Debuff[] debuffs;

    public DebuffData(PlayerData data) {
        this.data = data;
        this.debuffs = new Debuff[ModRegistry.DEBUFFS != null ? ModRegistry.DEBUFFS.getValuesCollection().size() : 0];
    }

    public Debuff[] getDebuffs() {
        return debuffs;
    }

    public void toggle(DebuffType type) {
        int i = getDebuffID(type);
        Debuff v = debuffs[i];
        if(v != null) {
            debuffs[i] = null;
        } else {
            debuffs[i] = createInstance(type);
        }
    }

    public static int getDebuffID(DebuffType type) {
        return ((ForgeRegistry<DebuffType>) ModRegistry.DEBUFFS).getID(type);
    }

    public Debuff createInstance(DebuffType type) {
        Debuff debuff = type.create();
        debuff.onDebuffObtained();
        return debuff;
    }

    public boolean hasDebuff(DebuffType debuffType) {
        int i = getDebuffID(debuffType);
        return debuffs[i] != null;
    }

    public void heal(DebuffType type, int amount) {
        int k = getDebuffID(type);
        Debuff debuff = debuffs[k];
        if(debuff != null) {
            debuff.heal(amount);
        }
    }

    public void onPlayerAttackedFrom(DamageContext ctx, EntityPlayer player) {
        Random random = player.world.rand;
        types: for(DebuffType type : ModRegistry.DEBUFFS) {
            int i = getDebuffID(type);
            Debuff v = debuffs[i];
            if(v == null) {
                float resist = type.getResistChance(data.getSkills(), ctx.getSource());
                if(resist > 0 && random.nextFloat() <= resist) continue;
                for(ToFloatBiFunction<PlayerSkills, DamageContext> function : type.getConditions()) {
                    float chance = function.applyAsFloat(data.getSkills(), ctx);
                    if(chance <= 0) continue;
                    if(random.nextFloat() <= chance) {
                        Debuff newInst = createInstance(type);
                        debuffs[i] = newInst;
                        data.sync();
                        break types;
                    }
                }
            }
        }
    }

    public void onTick(EntityPlayer player, PlayerData data) {
        for (int i = 0; i < debuffs.length; i++) {
            Debuff debuff = debuffs[i];
            if(debuff == null) continue;
            if(debuff.isInvalid()) {
                debuffs[i] = null;
                data.sync();
                continue;
            }
            debuff.onTick(player, data);
        }
    }

    @Override
    public NBTTagList serializeNBT() {
        NBTTagList list = new NBTTagList();
        for (Debuff debuff : debuffs) {
            if (debuff == null) continue;
            NBTTagCompound data = new NBTTagCompound();
            data.setString("key", debuff.getType().getRegistryName().toString());
            data.setTag("data", debuff.serializeNBT());
            list.appendTag(data);
        }
        return list;
    }

    @Override
    public void deserializeNBT(NBTTagList list) {
        debuffs = new Debuff[ModRegistry.DEBUFFS.getValuesCollection().size()];
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound nbt = list.getCompoundTagAt(i);
            ResourceLocation key = new ResourceLocation(nbt.getString("key"));
            NBTTagCompound data = nbt.getCompoundTag("data");
            DebuffType type = ModRegistry.DEBUFFS.getValue(key);
            if(type == null) {
                GunsRPG.log.error("Error loading debuff data for key {}", key.toString());
                continue;
            }
            int id = getDebuffID(type);
            Debuff debuff = type.create();
            debuff.deserializeNBT(data);
            debuffs[id] = debuff;
        }
    }
}
