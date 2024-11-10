package dev.toma.gunsrpg.common.capability.object;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.ISyncRequestDispatcher;
import dev.toma.gunsrpg.api.common.data.DataFlags;
import dev.toma.gunsrpg.api.common.data.IDebuffs;
import dev.toma.gunsrpg.common.debuffs.IDebuff;
import dev.toma.gunsrpg.common.debuffs.IDebuffContext;
import dev.toma.gunsrpg.common.debuffs.IDebuffType;
import dev.toma.gunsrpg.common.debuffs.ProgressingDebuff;
import dev.toma.gunsrpg.common.init.ModRegistries;
import dev.toma.gunsrpg.config.debuff.DebuffConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class PlayerDebuffs implements IDebuffs {

    private final Map<IDebuffType<?>, IDebuff> debuffMap = new LinkedHashMap<>();
    private ISyncRequestDispatcher clientSynchReq;

    @SuppressWarnings("unchecked")
    @Override
    public <D extends IDebuff> D getDebuff(IDebuffType<D> type) {
        return (D) debuffMap.get(type);
    }

    @Override
    public <D extends IDebuff> void add(IDebuffType<D> type, D instance) {
        debuffMap.put(type, instance);
    }

    @Override
    public void trigger(IDebuffType.TriggerFlags flags, IDebuffContext context, @Nullable Object data) {
        PlayerEntity player = context.getPlayer();
        Random random = player == null ? null : player.getRandom();
        for (IDebuffType<?> type : ModRegistries.DEBUFFS) {
            if (!type.isDisabled() && flags.is(type.getFlags())) {
                this.addDebuff(type, context, random, data);
            }
        }
    }

    @Override
    public void toggle(IDebuffType<?> type) {
        IDebuff debuff = getDebuff(type);
        if (debuff != null) {
            clearDebuff(type);
        } else {
            debuffMap.put(type, type.createRaw());
        }
        sync();
    }

    @Override
    public boolean hasDebuff(IDebuffType<?> type) {
        return getDebuff(type) != null;
    }

    @Override
    public void clearDebuff(IDebuffType<?> type) {
        debuffMap.remove(type);
    }

    @Override
    public void clearActive() {
        debuffMap.clear();
        sync();
    }

    @Override
    public void heal(IDebuffType<?> type, int amount) {
        IDebuff debuff = getDebuff(type);
        if (debuff != null) {
            debuff.heal(amount, this);
        }
        clientSynchReq.sendSyncRequest();
    }

    @Override
    public void tick(PlayerEntity player) {
        Iterator<IDebuff> iterator = debuffMap.values().iterator();
        while (iterator.hasNext()) {
            IDebuff debuff = iterator.next();
            debuff.tick(player);
            if (debuff.shouldRemove())
                iterator.remove();
        }
    }

    @Override
    public void toNbt(CompoundNBT compound) {
        ListNBT list = new ListNBT();
        for (Map.Entry<IDebuffType<?>, IDebuff> entry : debuffMap.entrySet()) {
            ResourceLocation id = entry.getKey().getRegistryName();
            IDebuff debuff = entry.getValue();
            CompoundNBT debuffNBT = debuff.toNbt();
            debuffNBT.putString("type", id.toString());
            list.add(debuffNBT);
        }
        compound.put("debuffs", list);
    }

    @Override
    public void fromNbt(CompoundNBT compound) {
        ListNBT nbt = compound.contains("debuffs", Constants.NBT.TAG_LIST) ? compound.getList("debuffs", Constants.NBT.TAG_COMPOUND) : new ListNBT();
        debuffMap.clear();
        for (int i = 0; i < nbt.size(); i++) {
            CompoundNBT debuffNbt = nbt.getCompound(i);
            String path = debuffNbt.getString("type");
            ResourceLocation key = new ResourceLocation(path);
            IDebuffType<?> type = ModRegistries.DEBUFFS.getValue(key);
            if (type != null) {
                IDebuff debuff = type.createRaw();
                debuff.fromNbt(debuffNbt);
                debuffMap.put(type, debuff);
            }
        }
    }

    @Override
    public void setSyncRequestTemplate(ISyncRequestDispatcher request) {
        clientSynchReq = request;
    }

    @Override
    public Iterable<IDebuff> getActiveAsIterable() {
        return debuffMap.values();
    }

    @Override
    public int getFlag() {
        return DataFlags.DEBUFF;
    }

    private void sync() {
        if (clientSynchReq != null)
            clientSynchReq.sendSyncRequest();
    }

    private <D extends IDebuff> void addDebuff(IDebuffType<D> type, IDebuffContext context, Random random, @Nullable Object data) {
        D debuff = type.onTrigger(context, random, data);
        if (debuff != null) {
            IDebuff existing = this.getDebuff(type);
            if (existing instanceof ProgressingDebuff) {
                DebuffConfig config = GunsRPG.config.debuffs;
                ((ProgressingDebuff) existing).incrementProgression(config.additionalDebuffIncrement);
            } else {
                this.add(type, debuff);
            }
            this.sync();
        }
    }
}
