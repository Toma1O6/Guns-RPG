package dev.toma.gunsrpg.common.capability.object;

import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.data.DataFlags;
import dev.toma.gunsrpg.api.common.data.IPerkProvider;
import dev.toma.gunsrpg.api.common.data.IPlayerCapEntry;
import dev.toma.gunsrpg.api.common.perk.IPerk;
import dev.toma.gunsrpg.common.item.perk.Crystal;
import dev.toma.gunsrpg.common.item.perk.CrystalAttribute;
import dev.toma.gunsrpg.common.perk.Perk;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;

import java.util.HashMap;
import java.util.Map;

public class PlayerPerkProvider implements IPerkProvider, IPlayerCapEntry {

    private final IAttributeProvider attributeProvider;
    private final Int2ObjectMap<Crystal> slot2CrystalMap = new Int2ObjectOpenHashMap<>();
    private final Map<Perk, CrystalAttribute> perkData = new HashMap<>();
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
        nbt.put("perks", data);
    }

    @Override
    public void fromNbt(CompoundNBT nbt) {
        CompoundNBT data = nbt.contains("perks", Constants.NBT.TAG_COMPOUND) ? nbt.getCompound("perks") : new CompoundNBT();
        perkPoints = data.getInt("points");
    }

    @Override
    public void setClientSynch(IClientSynchReq request) {
        syncRequestFactory = request;
    }

    private void computePerkData() {

    }
}
