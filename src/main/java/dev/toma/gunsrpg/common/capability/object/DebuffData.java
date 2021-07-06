package dev.toma.gunsrpg.common.capability.object;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.debuffs.DamageContext;
import dev.toma.gunsrpg.common.debuffs.Debuff;
import dev.toma.gunsrpg.common.debuffs.DebuffType;
import dev.toma.gunsrpg.common.init.GunsRPGRegistries;
import dev.toma.gunsrpg.util.function.ToFloatBiFunction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ForgeRegistry;

import java.util.Random;

public class DebuffData implements INBTSerializable<ListNBT> {

    private final PlayerData data;
    private Debuff[] debuffs;

    public DebuffData(PlayerData data) {
        this.data = data;
        this.debuffs = new Debuff[GunsRPGRegistries.DEBUFFS != null ? GunsRPGRegistries.DEBUFFS.getValues().size() : 0];
    }

    public Debuff[] getDebuffs() {
        return debuffs;
    }

    public void toggle(DebuffType<?> type) {
        int i = getDebuffID(type);
        Debuff v = debuffs[i];
        if(v != null) {
            debuffs[i] = null;
        } else {
            debuffs[i] = createInstance(type);
        }
    }

    public static int getDebuffID(DebuffType<?> type) {
        return ((ForgeRegistry<DebuffType<?>>) GunsRPGRegistries.DEBUFFS).getID(type);
    }

    public Debuff createInstance(DebuffType<?> type) {
        Debuff debuff = type.create();
        debuff.onDebuffObtained();
        return debuff;
    }

    public boolean hasDebuff(DebuffType<?> debuffType) {
        int i = getDebuffID(debuffType);
        return debuffs[i] != null;
    }

    public void heal(DebuffType<?> type, int amount) {
        int k = getDebuffID(type);
        Debuff debuff = debuffs[k];
        if(debuff != null) {
            debuff.heal(amount);
        }
    }

    public void onPlayerAttackedFrom(DamageContext ctx, PlayerEntity player) {
        Random random = player.level.getRandom();
        types:
        for(DebuffType<?> type : GunsRPGRegistries.DEBUFFS) {
            if(type.isBlacklisted())
                continue;
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

    public void tick(PlayerEntity player, PlayerData data) {
        for (int i = 0; i < debuffs.length; i++) {
            Debuff debuff = debuffs[i];
            if(debuff == null) continue;
            if(debuff.isInvalid()) {
                debuffs[i] = null;
                data.sync();
                continue;
            }
            debuff.tick(player, data);
        }
    }

    @Override
    public ListNBT serializeNBT() {
        ListNBT list = new ListNBT();
        for (Debuff debuff : debuffs) {
            if (debuff == null) continue;
            CompoundNBT data = new CompoundNBT();
            data.putString("key", debuff.getType().getRegistryName().toString());
            data.put("data", debuff.serializeNBT());
            list.add(data);
        }
        return list;
    }

    @Override
    public void deserializeNBT(ListNBT list) {
        debuffs = new Debuff[GunsRPGRegistries.DEBUFFS.getValues().size()];
        for (int i = 0; i < list.size(); i++) {
            CompoundNBT nbt = list.getCompound(i);
            ResourceLocation key = new ResourceLocation(nbt.getString("key"));
            CompoundNBT data = nbt.getCompound("data");
            DebuffType<?> type = GunsRPGRegistries.DEBUFFS.getValue(key);
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
