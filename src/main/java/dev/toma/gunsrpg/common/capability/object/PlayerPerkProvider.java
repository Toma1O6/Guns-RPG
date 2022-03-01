package dev.toma.gunsrpg.common.capability.object;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.attribute.IAttributeModifier;
import dev.toma.gunsrpg.api.common.data.DataFlags;
import dev.toma.gunsrpg.api.common.data.IPerkProvider;
import dev.toma.gunsrpg.api.common.data.IPlayerCapEntry;
import dev.toma.gunsrpg.api.common.perk.IPerk;
import dev.toma.gunsrpg.api.common.perk.IPerkOptions;
import dev.toma.gunsrpg.common.attribute.Attribute;
import dev.toma.gunsrpg.common.perk.Perk;
import dev.toma.gunsrpg.common.perk.PerkImpl;
import dev.toma.gunsrpg.common.perk.PerkRegistry;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PlayerPerkProvider implements IPerkProvider, IPlayerCapEntry {

    private final Map<Perk, IPerk> perkMap = new HashMap<>();
    private IClientSynchReq syncRequestFactory;
    private int perkPoints;

    @Override
    public IPerk getPerk(Perk perk) {
        return perkMap.get(perk);
    }

    @Override
    public void addPerk(Perk perk, IPerkOptions options) {
        IPerk iPerk = perk.createInstance(options);
        perkMap.put(perk, iPerk);
        syncRequestFactory.makeSyncRequest();
    }

    @Override
    public void removePerk(Perk perk) {
        perkMap.remove(perk);
        syncRequestFactory.makeSyncRequest();
    }

    @Override
    public Collection<IPerk> listPerks() {
        return perkMap.values();
    }

    @Override
    public int getPoints() {
        return perkPoints;
    }

    @Override
    public void awardPoints(int amount) {
        perkPoints += amount;
    }

    @Override
    public int getFlag() {
        return DataFlags.PERK;
    }

    @Override
    public void toNbt(CompoundNBT nbt) {
        CompoundNBT data = new CompoundNBT();
        data.putInt("points", perkPoints);
        ListNBT listNBT = new ListNBT();
        for (Map.Entry<Perk, IPerk> entry : perkMap.entrySet()) {
            Perk perk = entry.getKey();
            IPerk iPerk = entry.getValue();
            IAttributeModifier modifier = iPerk.getModifier();
            ResourceLocation id = perk.getPerkId();
            CompoundNBT perkNbt = new CompoundNBT();
            perkNbt.putString("id", id.toString());
            perkNbt.put("modifier", Attribute.serializeModifier(modifier));
            listNBT.add(perkNbt);
        }
        data.put("perkList", listNBT);
        nbt.put("perks", data);
    }

    @Override
    public void fromNbt(CompoundNBT nbt) {
        perkMap.clear();
        CompoundNBT data = nbt.contains("perks", Constants.NBT.TAG_COMPOUND) ? nbt.getCompound("perks") : new CompoundNBT();
        perkPoints = data.getInt("points");
        ListNBT listNBT = data.getList("perkList", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < listNBT.size(); i++) {
            CompoundNBT perkNbt = listNBT.getCompound(i);
            ResourceLocation id = new ResourceLocation(perkNbt.getString("id"));
            Perk perk = PerkRegistry.getRegistry().getPerkById(id);
            if (perk == null) {
                GunsRPG.log.warn("Found unknown perk ID in player data: {}", id);
                continue;
            }
            IAttributeModifier modifier = Attribute.deserializeModifier(perkNbt.getCompound("modifier"));
            perkMap.put(perk, new PerkImpl(perk, modifier));
        }
    }

    @Override
    public void setClientSynch(IClientSynchReq request) {
        syncRequestFactory = request;
    }
}
