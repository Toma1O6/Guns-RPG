package dev.toma.gunsrpg.common.item.guns.util;

import dev.toma.gunsrpg.api.common.IAmmoMaterial;
import dev.toma.gunsrpg.util.properties.PropertyContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IAdditionalShootData {

    @OnlyIn(Dist.CLIENT)
    void addExtraData(PropertyContext context, PlayerEntity player, ItemStack stack, IAmmoMaterial material);
}
