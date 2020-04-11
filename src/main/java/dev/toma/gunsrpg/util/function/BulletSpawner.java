package dev.toma.gunsrpg.util.function;

import dev.toma.gunsrpg.common.entity.EntityBullet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@FunctionalInterface
public interface BulletSpawner {

    EntityBullet createBulletEntity(World world, EntityPlayer player, ItemStack stack);
}
