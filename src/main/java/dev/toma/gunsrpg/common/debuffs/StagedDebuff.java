package dev.toma.gunsrpg.common.debuffs;

import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.data.DataFlags;
import dev.toma.gunsrpg.api.common.data.IDebuffs;
import dev.toma.gunsrpg.common.capability.PlayerData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public class StagedDebuff implements IStagedDebuff, ProgressingDebuff {

    private final DataDrivenDebuffType<?> type;
    private int stage;
    private int progression; // debuff pct
    private int progressionCounter; // pct incr delay

    // render variables
    private int ticksSinceAdded;
    private int ticksSinceProgressed;
    private int ticksSinceHealed;
    private boolean frozen;

    public StagedDebuff(DataDrivenDebuffType<?> type) {
        this.type = type;
        this.stage = 0;
        this.ticksSinceHealed = 100;
    }

    @Override
    public void tick(PlayerEntity player) {
        PlayerData.get(player).ifPresent(data -> {
            this.updateRenderCounters();
            int delay = type.getDelay(data);
            if (this.canSpread() && ++progressionCounter >= delay) {
                this.incrementProgression(1);
                if (!player.level.isClientSide) data.sync(DataFlags.DEBUFF);
            }
            if (!isFrozen(data.getAttributes())) {
                DataDrivenDebuffType.DebuffStage debuffStage = this.type.getStage(this.stage);
                debuffStage.apply(player);
            }
        });
    }

    @Override
    public void incrementProgression(int count) {
        int prevProgression = this.progression;
        this.progression = Math.min(this.getProgressionLimit(), this.progression + count);
        if (prevProgression != progression) {
            this.progressionCounter = 0;
            this.ticksSinceProgressed = 0;
            this.updateStage();
        }
    }

    @Override
    public float getBlockingProgress(IAttributeProvider provider) {
        return DataDrivenDebuffType.getBuffedProgress(provider, this.type.getBlockingAttribute());
    }

    @Override
    public boolean isFrozen(IAttributeProvider attributes) {
        return type.isDisabledByAttributes(attributes);
    }

    @Override
    public void heal(int amount, IDebuffs data) {
        if ((progression -= amount) < 0) {
            data.clearDebuff(type);
        }
        ticksSinceHealed = 0;
    }

    @Override
    public int getCurrentProgress() {
        return progression;
    }

    @Override
    public IDebuffType<?> getType() {
        return type;
    }

    @Override
    public boolean canSpread() {
        return progression < 100;
    }

    @Override
    public int ticksSinceAdded() {
        return ticksSinceAdded;
    }

    @Override
    public int ticksSinceHealed() {
        return ticksSinceHealed;
    }

    @Override
    public int ticksSinceProgressed() {
        return ticksSinceProgressed;
    }

    protected int getProgressionLimit() {
        return 100;
    }

    @Override
    public CompoundNBT toNbt() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("progression", progression);
        nbt.putInt("progressionCounter", progressionCounter);
        saveNonZeroInt(nbt, ticksSinceAdded, "ticksSinceAdded");
        saveNonZeroInt(nbt, ticksSinceProgressed, "ticksSinceProgressed");
        saveNonZeroInt(nbt, ticksSinceHealed, "ticksSinceHealed");
        return nbt;
    }

    @Override
    public void fromNbt(CompoundNBT nbt) {
        progression = nbt.getInt("progression");
        progressionCounter = nbt.getInt("progressionCounter");
        ticksSinceAdded = nbt.getInt("ticksSinceAdded");
        ticksSinceProgressed = nbt.getInt("ticksSinceProgressed");
        ticksSinceHealed = nbt.getInt("ticksSinceHealed");
        this.updateStage();
    }

    @Override
    public boolean shouldRemove() {
        return progression < 0;
    }

    private void updateStage() {
        int stageIndex = 0;
        if (this.type.getStages() == null) {
            this.stage = stageIndex;
            return;
        }
        for (int i = 0; i < this.type.getStages().size(); i++) {
            DataDrivenDebuffType.DebuffStage testStage = this.type.getStage(i);
            stageIndex = i;
            if (testStage.isApplicable(this.progression)) {
                break;
            }
        }
        this.stage = stageIndex;
    }

    private void updateRenderCounters() {
        ++ticksSinceAdded;
        ++ticksSinceProgressed;
        ++ticksSinceHealed;
    }

    private void saveNonZeroInt(CompoundNBT nbt, int value, String name) {
        if (value != 0)
            nbt.putInt(name, value);
    }
}
