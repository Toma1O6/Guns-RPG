package dev.toma.gunsrpg.common.item.perk;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.attribute.IAttributeModifier;
import dev.toma.gunsrpg.common.perk.Perk;
import dev.toma.gunsrpg.common.perk.PerkRegistry;
import dev.toma.gunsrpg.common.perk.PerkType;
import dev.toma.gunsrpg.resource.perks.CrystalConfiguration;
import dev.toma.gunsrpg.resource.perks.PerkConfiguration;
import dev.toma.gunsrpg.util.Lifecycle;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

public final class CrystalAttribute {

    private final Perk perk;
    private final PerkType type;
    private final int level;

    public CrystalAttribute(Perk perk, PerkType type, int level) {
        this.perk = perk;
        this.type = type;
        this.level = level;
    }

    public static CrystalAttribute merge(Iterable<CrystalAttribute> iterable) {
        return null; // TODO
    }

    public IAttributeModifier getModifier() {
        return null; // TODO
    }

    public Perk getPerk() {
        return perk;
    }

    public PerkType getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    public float getValue() {
        float range = perk.getBounds(type);
        float value = perk.getScaling(type) * level;
        return Math.min(value, range);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CrystalAttribute attribute = (CrystalAttribute) o;
        return getPerk().equals(attribute.getPerk());
    }

    @Override
    public int hashCode() {
        return getPerk().hashCode();
    }

    public CompoundNBT toNbt() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("perk", perk.getPerkId().toString());
        nbt.putInt("level", level);
        nbt.putInt("type", type.ordinal());
        return nbt;
    }

    public static CrystalAttribute fromNbt(CompoundNBT nbt) {
        ResourceLocation perkId = new ResourceLocation(nbt.getString("perk"));
        Perk perk = PerkRegistry.getRegistry().getPerkById(perkId);
        int level = nbt.getInt("level");
        PerkType type = PerkType.values()[nbt.getInt("type")];
        return new CrystalAttribute(perk, type, level);
    }
}
