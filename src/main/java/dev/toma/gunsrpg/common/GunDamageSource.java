package dev.toma.gunsrpg.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;

import javax.annotation.Nullable;

public class GunDamageSource extends DamageSource {

    @Nullable
    private final Entity src;
    @Nullable
    private final Entity indirect;
    private final ItemStack stacc;

    public GunDamageSource(Entity src, Entity indirect, ItemStack stacc) {
        super("mob");
        this.src = src;
        this.indirect = indirect;
        this.stacc = stacc;
    }

    @Nullable
    @Override
    public Entity getTrueSource() {
        return src;
    }

    @Nullable
    @Override
    public Entity getImmediateSource() {
        return indirect;
    }

    public Entity getSrc() {
        return src;
    }

    public ItemStack getStacc() {
        return stacc;
    }

    public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn) {
        ItemStack itemstack = this.src instanceof EntityLivingBase ? ((EntityLivingBase) this.src).getHeldItemMainhand() : ItemStack.EMPTY;
        String s = "death.attack." + this.damageType;
        String s1 = s + ".item";
        return !itemstack.isEmpty() && itemstack.hasDisplayName() && I18n.canTranslate(s1) ? new TextComponentTranslation(s1, entityLivingBaseIn.getDisplayName(), this.src.getDisplayName(), itemstack.getTextComponent()) : new TextComponentTranslation(s, entityLivingBaseIn.getDisplayName(), this.src.getDisplayName());
    }
}
