package dev.toma.gunsrpg.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

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
    public Entity getDirectEntity() {
        return src;
    }

    @Nullable
    @Override
    public Entity getEntity() {
        return indirect;
    }

    public Entity getSrc() {
        return src;
    }

    public ItemStack getStacc() {
        return stacc;
    }

    @Override
    public ITextComponent getLocalizedDeathMessage(LivingEntity entityLivingBaseIn) {
        ItemStack itemstack = this.src instanceof LivingEntity ? ((LivingEntity) this.src).getMainHandItem() : ItemStack.EMPTY;
        String s = "death.attack." + this.msgId;
        String s1 = s + ".item";
        return !itemstack.isEmpty() ? new TranslationTextComponent(s1, entityLivingBaseIn.getDisplayName(), this.src.getDisplayName(), itemstack.getDisplayName()) : new TranslationTextComponent(s, entityLivingBaseIn.getDisplayName(), this.src.getDisplayName());
    }
}
