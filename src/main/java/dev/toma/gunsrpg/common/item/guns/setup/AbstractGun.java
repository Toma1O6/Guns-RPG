package dev.toma.gunsrpg.common.item.guns.setup;

import dev.toma.gunsrpg.api.common.IAmmoMaterial;
import dev.toma.gunsrpg.common.item.BaseItem;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterialManager;
import dev.toma.gunsrpg.common.item.guns.util.Firemode;
import dev.toma.gunsrpg.util.object.RGB2TextFormatting;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public abstract class AbstractGun extends BaseItem {

    protected static Random random = new Random();

    public AbstractGun(String name, Properties properties) {
        super(name, properties);
    }

    public abstract MaterialContainer getContainer();

    public static int getAmmoCount(ItemStack stack) {
        CompoundNBT nbt = stack.getTag();
        if (nbt != null) {
            return nbt.getInt("ammo");
        }
        return 0;
    }

    public final Firemode getFiremode(ItemStack stack) {
        createNBT(stack);
        return Firemode.get(stack.getTag().getInt("firemode"));
    }

    public void createNBT(ItemStack stack) {
        if (stack.hasTag()) {
            return;
        }
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("ammo", 0);
        nbt.putInt("firemode", 0);
        stack.setTag(nbt);
    }

    public IAmmoMaterial getMaterialFromNBT(ItemStack stack) {
        this.createNBT(stack);
        CompoundNBT nbt = stack.getTag();
        AmmoMaterialManager materialManager = AmmoMaterialManager.get();
        return materialManager.parse(nbt);
    }

    public int getAmmo(ItemStack stack) {
        this.createNBT(stack);
        return stack.getTag().getInt("ammo");
    }

    public void setAmmoCount(ItemStack stack, int count) {
        this.createNBT(stack);
        CompoundNBT nbt = stack.getTag();
        nbt.putInt("ammo", Math.max(0, count));
    }

    public boolean hasAmmo(ItemStack stack) {
        return this.getAmmo(stack) > 0;
    }

    public final int getDamageBonus(ItemStack stack) {
        IAmmoMaterial material = this.getMaterialFromNBT(stack);
        return material != null ? getContainer().getAdditionalDamage(material) : 0;
    }

    @Override
    public void appendHoverText(ItemStack stack, World world, List<ITextComponent> list, ITooltipFlag flags) {
        IAmmoMaterial material = getMaterialFromNBT(stack);
        TextFormatting formatting = material != null ? RGB2TextFormatting.getClosestFormat(material.getTextColor()) : TextFormatting.GRAY;
        list.add(new StringTextComponent("Ammo material: " + formatting + (material != null ? material.getDisplayName().getString() : "???")));
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        return false;
    }
}
