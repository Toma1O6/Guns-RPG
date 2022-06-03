package dev.toma.gunsrpg.common.perk;

import net.minecraft.network.PacketBuffer;

public final class PerkValueSpec {

    private final float buffValue;
    private final float debuffValue;

    public PerkValueSpec(float buffValue, float debuffValue) {
        this.buffValue = buffValue;
        this.debuffValue = debuffValue;
    }

    public float buff() {
        return buffValue;
    }

    public float debuff() {
        return debuffValue;
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeFloat(buffValue);
        buffer.writeFloat(debuffValue);
    }

    public static PerkValueSpec decode(PacketBuffer buffer) {
        return new PerkValueSpec(
                buffer.readFloat(),
                buffer.readFloat()
        );
    }
}
