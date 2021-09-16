package dev.toma.gunsrpg.common.debuffs;

import dev.toma.gunsrpg.api.common.data.IPlayerData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;

public interface IDebuffContext {

    DamageSource getSource();

    PlayerEntity getPlayer();

    IPlayerData getData();

    float getReceivedDamage();

    static IDebuffContext of(DamageSource source, PlayerEntity player, IPlayerData data, float amount) {
        return new IDebuffContext() {
            @Override
            public DamageSource getSource() {
                return source;
            }

            @Override
            public PlayerEntity getPlayer() {
                return player;
            }

            @Override
            public IPlayerData getData() {
                return data;
            }

            @Override
            public float getReceivedDamage() {
                return amount;
            }
        };
    }
}
