package dev.toma.gunsrpg.common.debuffs;

import dev.toma.gunsrpg.util.IFlags;
import net.minecraft.util.ResourceLocation;

import java.util.Random;

public interface IDebuffType<D extends IDebuff> {

    D onTrigger(IDebuffContext context, Random random);

    D createRaw();

    boolean isDisabled();

    int getFlags();

    ResourceLocation getRegistryName();

    enum TriggerFlags implements IFlags {

        HURT,
        RESPAWN;

        private final int value;

        TriggerFlags() {
            this.value = IFlags.value(ordinal());
        }

        @Override
        public boolean is(int flags) {
            return (flags & value) == value;
        }

        public int getValue() {
            return value;
        }
    }
}
