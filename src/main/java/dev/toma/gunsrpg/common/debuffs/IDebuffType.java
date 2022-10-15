package dev.toma.gunsrpg.common.debuffs;

import dev.toma.gunsrpg.util.IFlags;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Random;

public interface IDebuffType<D extends IDebuff> {

    D onTrigger(IDebuffContext context, Random random, @Nullable Object data);

    D createRaw();

    boolean isDisabled();

    int getFlags();

    ResourceLocation getRegistryName();

    default boolean isToggleable() {
        return true;
    }

    enum TriggerFlags implements IFlags {

        HURT,
        RESPAWN,
        HEAL;

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
