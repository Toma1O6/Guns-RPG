package dev.toma.gunsrpg.common.item.heal;

import dev.toma.gunsrpg.api.common.data.IDebuffs;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class DebuffHealItem extends AbstractHealItem<IDebuffs> {

    protected DebuffHealItem(Builder builder) {
        super(builder);
    }

    public static HealBuilder<IDebuffs, DebuffHealItem> define(String name) {
        return new Builder(name);
    }

    @Override
    public IDebuffs getTargetObject(World world, PlayerEntity user, IPlayerData data) {
        return data.getDebuffControl();
    }

    protected static class Builder extends HealBuilder<IDebuffs, DebuffHealItem> {

        protected Builder(String name) {
            super(name);
        }

        @Override
        public DebuffHealItem build() {
            return new DebuffHealItem(this);
        }
    }
}
