package dev.toma.gunsrpg.common.item;

import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.object.DebuffData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class DebuffHealItem extends AbstractHealItem<DebuffData> {

    protected DebuffHealItem(Builder builder) {
        super(builder);
    }

    public static HealBuilder<DebuffData, DebuffHealItem> define(String name) {
        return new Builder(name);
    }

    @Override
    public DebuffData getTargetObject(World world, PlayerEntity user, PlayerData data) {
        return data.getDebuffData();
    }

    protected static class Builder extends HealBuilder<DebuffData, DebuffHealItem> {

        protected Builder(String name) {
            super(name);
        }

        @Override
        public DebuffHealItem build() {
            return new DebuffHealItem(this);
        }
    }
}
