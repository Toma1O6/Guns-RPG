package dev.toma.gunsrpg.mixin;

import dev.toma.gunsrpg.common.entity.EntityFlag;
import dev.toma.gunsrpg.common.entity.EntityFlagHolder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.EnumSet;

@Mixin(MobEntity.class)
public abstract class MobEntityFlagExtension extends LivingEntity implements EntityFlagHolder {

    @Unique
    private final EnumSet<EntityFlag> gunsrpg$entityFlags = EnumSet.noneOf(EntityFlag.class);

    public MobEntityFlagExtension(EntityType<? extends LivingEntity> entityType, World level) {
        super(entityType, level);
    }

    @Inject(
            method = "addAdditionalSaveData",
            at = @At("RETURN")
    )
    private void gunsrpg$addMobAdditionalSaveData(CompoundNBT nbt, CallbackInfo ci) {
        if (this.gunsrpg$entityFlags.isEmpty())
            return;
        ListNBT list = new ListNBT();
        this.gunsrpg$entityFlags.forEach(flag -> list.add(IntNBT.valueOf(flag.ordinal())));
        nbt.put("gunsrpg.entityFlags", list);
    }

    @Inject(
            method = "readAdditionalSaveData",
            at = @At("RETURN")
    )
    private void gunsrpg$readMobAdditionalSaveData(CompoundNBT nbt, CallbackInfo ci) {
        this.gunsrpg$entityFlags.clear();
        if (!nbt.contains("gunsrpg.entityFlags", Constants.NBT.TAG_LIST))
            return;
        ListNBT list = nbt.getList("gunsrpg.entityFlags", Constants.NBT.TAG_INT);
        list.forEach(inbt -> {
            IntNBT intNBT = (IntNBT) inbt;
            this.gunsrpg$entityFlags.add(EntityFlag.values()[intNBT.getAsInt()]);
        });
    }

    @Override
    public void gunsrpg$addFlag(EntityFlag flag) {
        this.gunsrpg$entityFlags.add(flag);
    }

    @Override
    public void gunsrpg$removeFlag(EntityFlag flag) {
        this.gunsrpg$entityFlags.remove(flag);
    }

    @Override
    public void gunsrpg$clearFlags() {
        this.gunsrpg$entityFlags.clear();
    }

    @Override
    public boolean gunsrpg$hasFlag(EntityFlag flag) {
        return this.gunsrpg$entityFlags.contains(flag);
    }
}
