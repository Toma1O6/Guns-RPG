package dev.toma.gunsrpg.common.item;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.ModTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.function.Consumer;

public class ItemModdedFood extends ItemFood {

    private Consumer<EntityPlayer> onEaten;

    public ItemModdedFood(String name, int foodLevel, float saturationLevel, boolean wolfFood) {
        super(foodLevel, saturationLevel, wolfFood);
        setRegistryName(GunsRPG.makeResource(name));
        setUnlocalizedName(name);
        setCreativeTab(ModTabs.ITEM_TAB);
    }

    public ItemModdedFood buff(Consumer<EntityPlayer> consumer) {
        this.onEaten = consumer;
        return this;
    }

    @Override
    protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
        if(!worldIn.isRemote) {
            if(onEaten != null) {
                onEaten.accept(player);
            }
        }
    }
}
