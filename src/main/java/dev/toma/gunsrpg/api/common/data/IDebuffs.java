package dev.toma.gunsrpg.api.common.data;

import dev.toma.gunsrpg.common.debuffs.IDebuff;
import dev.toma.gunsrpg.common.debuffs.IDebuffContext;
import dev.toma.gunsrpg.common.debuffs.IDebuffType;
import net.minecraft.entity.player.PlayerEntity;

import javax.annotation.Nullable;
import java.util.Collection;

public interface IDebuffs extends IPlayerCapEntry {

    <D extends IDebuff> D getDebuff(IDebuffType<D> type);

    <D extends IDebuff> void add(IDebuffType<D> type, D instance);

    void trigger(IDebuffType.TriggerFlags flags, IDebuffContext context, @Nullable Object data);

    void toggle(IDebuffType<?> type);

    void heal(IDebuffType<?> type, int amount);

    boolean hasDebuff(IDebuffType<?> type);

    void clearDebuff(IDebuffType<?> type);

    void clearActive();

    void tick(PlayerEntity player);

    Collection<IDebuff> getActiveDebuffs();
}
