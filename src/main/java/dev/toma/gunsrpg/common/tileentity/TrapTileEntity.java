package dev.toma.gunsrpg.common.tileentity;

import dev.toma.gunsrpg.common.init.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;
import java.util.UUID;

public class TrapTileEntity extends TileEntity {

    private UUID owner;

    public TrapTileEntity() {
        super(ModBlockEntities.TRAP.get());
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    @Nullable
    public PlayerEntity getOwnerAsEntity() {
        if (owner == null) {
            return null;
        }
        return level.getPlayerByUUID(owner);
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        super.save(nbt);
        if (owner != null)
            nbt.putUUID("trapOwner", owner);
        return nbt;
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        if (nbt.contains("trapOwner"))
            owner = nbt.getUUID("trapOwner");
        else
            owner = null;
    }
}
