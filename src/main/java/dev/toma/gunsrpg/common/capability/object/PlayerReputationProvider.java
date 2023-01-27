package dev.toma.gunsrpg.common.capability.object;

import dev.toma.gunsrpg.api.common.data.DataFlags;
import dev.toma.gunsrpg.api.common.data.IPlayerCapEntry;
import dev.toma.gunsrpg.api.common.data.IReputationProvider;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.Constants;

import java.util.Map;
import java.util.UUID;

public final class PlayerReputationProvider implements IReputationProvider, IPlayerCapEntry {

    private final Object2FloatMap<UUID> map = new Object2FloatOpenHashMap<>();
    private IClientSynchReq syncRequestFactory;

    @Override
    public float getReputation(UUID uuid) {
        return this.map.getOrDefault(uuid, 0);
    }

    @Override
    public void setReputation(UUID uuid, float reputation) {
        this.map.put(uuid, MathHelper.clamp(reputation, 0.0F, 50.0F));
    }

    @Override
    public int getFlag() {
        return DataFlags.REPUTATION;
    }

    @Override
    public void toNbt(CompoundNBT nbt) {
        ListNBT list = new ListNBT();
        for (Map.Entry<UUID, Float> entry : this.map.object2FloatEntrySet()) {
            CompoundNBT traderEntry = new CompoundNBT();
            traderEntry.putUUID("trader", entry.getKey());
            traderEntry.putFloat("value", entry.getValue());
            list.add(traderEntry);
        }
        nbt.put("reputations", list);
    }

    @Override
    public void fromNbt(CompoundNBT nbt) {
        this.map.clear();
        ListNBT list = nbt.contains("reputations", Constants.NBT.TAG_LIST) ? nbt.getList("reputations", Constants.NBT.TAG_COMPOUND) : new ListNBT();
        for (int i = 0; i < list.size(); i++) {
            CompoundNBT traderEntry = list.getCompound(i);
            UUID uuid = traderEntry.getUUID("trader");
            float value = traderEntry.getFloat("value");
            this.map.put(uuid, value);
        }
    }

    @Override
    public void setClientSynch(IClientSynchReq request) {
        this.syncRequestFactory = request;
    }
}
