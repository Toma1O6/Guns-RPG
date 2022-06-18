package dev.toma.gunsrpg.common.item.perk;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.attribute.IAttributeModifier;
import dev.toma.gunsrpg.api.common.attribute.IModifierOp;
import dev.toma.gunsrpg.common.attribute.AttributeModifier;
import dev.toma.gunsrpg.common.attribute.AttributeOps;
import dev.toma.gunsrpg.common.perk.Perk;
import dev.toma.gunsrpg.common.perk.PerkRegistry;
import dev.toma.gunsrpg.common.perk.PerkType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.UUID;

public final class CrystalAttribute {

    private final Perk perk;
    private final PerkType type;
    private final int level;
    private final IAttributeModifier modifier;

    public CrystalAttribute(Perk perk, PerkType type, int level) {
        this.perk = perk;
        this.type = type;
        this.level = Math.abs(level);
        this.modifier = this.createUniqueModifier();
    }

    public CrystalAttribute increaseLevel() {
        return new CrystalAttribute(perk, type, level < 0 ? level - 1 : level + 1);
    }

    public IAttributeModifier createUniqueModifier() {
        long mostSigBits = perk.hashCode();
        long leastSigBits = Boolean.hashCode(perk.shouldInvertCalculation());
        UUID uniqueId = new UUID(mostSigBits, leastSigBits);
        IModifierOp operation = AttributeOps.MUL;
        double modifier = perk.getModifier(level, type);
        if (perk.shouldInvertCalculation()) {
            modifier = -modifier;
        }
        return new AttributeModifier(uniqueId, operation, 1.0F + modifier);
    }

    public static CrystalAttribute flatten(Perk perk, List<CrystalAttribute> list) {
        int totalLevel = 0;
        for (CrystalAttribute attribute : list) {
            int attributeLevel = attribute.getLevel();
            PerkType type = attribute.getType();
            int level = type.getLevel(attributeLevel);
            totalLevel += level;
        }
        PerkType type = totalLevel < 0 ? PerkType.DEBUFF : PerkType.BUFF;
        return new CrystalAttribute(perk, type, totalLevel);
    }

    public IAttributeModifier getModifier() {
        return modifier;
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
        float value = perk.getScaling() * level;
        return value < 0 ? Math.max(value, -range) : Math.min(value, range);
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
        if (perk == null) {
            GunsRPG.log.error("Unknown perk {}", perkId);
            return null;
        }
        int level = nbt.getInt("level");
        PerkType type = PerkType.values()[nbt.getInt("type")];
        return new CrystalAttribute(perk, type, level);
    }
}
