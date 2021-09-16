package dev.toma.gunsrpg.common.capability.object;

import dev.toma.gunsrpg.api.common.data.IDebuffs;
import dev.toma.gunsrpg.common.debuffs.IDebuff;
import dev.toma.gunsrpg.common.debuffs.IDebuffContext;
import dev.toma.gunsrpg.common.debuffs.IDebuffType;
import dev.toma.gunsrpg.common.init.ModRegistries;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class PlayerDebuffs implements IDebuffs {

    private final Map<IDebuffType<?>, IDebuff> debuffMap = new LinkedHashMap<>();
    private IClientSynchReq clientSynchReq;

    @SuppressWarnings("unchecked")
    @Override
    public <D extends IDebuff> D getDebuff(IDebuffType<D> type) {
        return (D) debuffMap.get(type);
    }

    @Override
    public <D extends IDebuff> void add(IDebuffType<D> type, D instance) {
        debuffMap.put(type, instance);
        sync();
    }

    @Override
    public void trigger(IDebuffType.TriggerFlags flags, IDebuffContext context) {
        Random random = context.getPlayer().getRandom();
        for (IDebuffType<?> type : ModRegistries.DEBUFFS) {
            if (getDebuff(type) != null) continue; // avoid replacing existing debuffs
            if (!type.isDisabled() && flags.is(type.getFlags())) {
                if (tryCreate(type, context, random)) {
                    break;
                }
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
        clientSynchReq.requestClientSynch();
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
    public void setClientSynch(IClientSynchReq request) {
        clientSynchReq = request;
    }

    @Override
    public Iterable<IDebuff> getActiveAsIterable() {
        return debuffMap.values();
    }

    private void sync() {
        if (clientSynchReq != null)
            clientSynchReq.requestClientSynch();
    }

    private <D extends IDebuff> boolean tryCreate(IDebuffType<D> type, IDebuffContext context, Random random) {
        D debuff = type.onTrigger(context, random);
        if (debuff != null) {
            add(type, debuff);
            return true;
        }
        return false;
    }
}
