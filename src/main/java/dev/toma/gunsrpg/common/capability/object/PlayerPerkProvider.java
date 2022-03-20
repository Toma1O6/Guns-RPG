package dev.toma.gunsrpg.common.capability.object;

import dev.toma.gunsrpg.api.common.attribute.IAttributeId;
import dev.toma.gunsrpg.api.common.attribute.IAttributeModifier;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.data.DataFlags;
import dev.toma.gunsrpg.api.common.data.IPerkProvider;
import dev.toma.gunsrpg.api.common.data.IPlayerCapEntry;
import dev.toma.gunsrpg.api.common.perk.IPerkStat;
import dev.toma.gunsrpg.common.item.perk.Crystal;
import dev.toma.gunsrpg.common.item.perk.CrystalAttribute;
import dev.toma.gunsrpg.common.perk.Perk;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;

import java.util.*;

public class PlayerPerkProvider implements IPerkProvider, IPlayerCapEntry {

    private final IAttributeProvider attributeProvider;
    private final Int2ObjectMap<Crystal> slot2CrystalMap = new Int2ObjectOpenHashMap<>();
    private final Map<Perk, PerkStat> perkData = new HashMap<>();
    private IClientSynchReq syncRequestFactory;
    private int perkPoints;

    public PlayerPerkProvider(IAttributeProvider provider) {
        this.attributeProvider = provider;
    }

    @Override
    public Crystal getCrystal(int slot) {
        return slot2CrystalMap.get(slot);
    }

    @Override
    public void setCrystal(int slot, Crystal crystal) {
        if (crystal == null) {
            slot2CrystalMap.put(slot, crystal);
        } else {
            slot2CrystalMap.remove(slot);
        }
        computePerkData();
        syncRequestFactory.makeSyncRequest();
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
        CompoundNBT map = new CompoundNBT();
        slot2CrystalMap.forEach((i, crystal) -> map.put(i.toString(), crystal.toNbt()));
        data.put("crystals", map);
        nbt.put("perks", data);
    }

    @Override
    public void fromNbt(CompoundNBT nbt) {
        slot2CrystalMap.clear();
        CompoundNBT data = nbt.contains("perks", Constants.NBT.TAG_COMPOUND) ? nbt.getCompound("perks") : new CompoundNBT();
        perkPoints = data.getInt("points");
        CompoundNBT map = data.getCompound("crystals");
        map.getAllKeys().forEach(key -> {
            CompoundNBT crystalNbt = map.getCompound(key);
            int index = Integer.parseInt(key);
            Crystal crystal = Crystal.fromNbt(crystalNbt);
            slot2CrystalMap.put(index, crystal);
        });
        computePerkData();
    }

    @Override
    public void setClientSynch(IClientSynchReq request) {
        syncRequestFactory = request;
    }

    @Override
    public Set<Perk> getActivePerks() {
        return perkData.keySet();
    }

    @Override
    public IPerkStat getPerkStat(Perk perk) {
        return perkData.get(perk);
    }

    private void computePerkData() {
        clearAttributes();
        Map<Perk, Collection<CrystalAttribute>> collectionMap = new HashMap<>();
        for (Crystal crystal : slot2CrystalMap.values()) {
            if (crystal == null) continue;
            Collection<CrystalAttribute> attributes = crystal.listAttributes();
            for (CrystalAttribute attribute : attributes) {
                Perk perk = attribute.getPerk();
                collectionMap.computeIfAbsent(perk, key -> new ArrayList<>()).add(attribute);
            }
        }
        for (Map.Entry<Perk, Collection<CrystalAttribute>> entry : collectionMap.entrySet()) {
            Perk perk = entry.getKey();
            CrystalAttribute attribute = CrystalAttribute.merge(entry.getValue());
            IAttributeModifier modifier = attribute.getModifier();
            IAttributeId attributeId = perk.getAttributeId();
            attributeProvider.getAttribute(attributeId).addModifier(modifier);
            perkData.put(perk, new PerkStat(attribute, modifier));
        }
    }

    private void clearAttributes() {
        for (Map.Entry<Perk, PerkStat> entry : perkData.entrySet()) {
            IAttributeId id = entry.getKey().getAttributeId();
            attributeProvider.getAttribute(id).removeModifier(entry.getValue().modifier);
        }
        perkData.clear();
    }

    private static class PerkStat implements IPerkStat {

        private final CrystalAttribute attribute;
        private final IAttributeModifier modifier;

        PerkStat(CrystalAttribute attribute, IAttributeModifier modifier) {
            this.attribute = attribute;
            this.modifier = modifier;
        }

        @Override
        public CrystalAttribute getAttribute() {
            return attribute;
        }

        @Override
        public IAttributeModifier getModifier() {
            return modifier;
        }
    }
}
