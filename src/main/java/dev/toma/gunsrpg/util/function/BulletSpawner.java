package dev.toma.gunsrpg.util.function;

import dev.toma.gunsrpg.common.entity.BulletEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@FunctionalInterface
public interface BulletSpawner {

    BulletEntity createBulletEntity(World world, PlayerEntity player, ItemStack stack);
}
