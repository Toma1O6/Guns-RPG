package dev.toma.gunsrpg.world.cap;

import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.IQuestingData;
import dev.toma.gunsrpg.api.common.data.ITraderStandings;
import dev.toma.gunsrpg.api.common.data.ITraderStatus;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.entity.MayorEntity;
import dev.toma.gunsrpg.common.quests.mayor.ReputationStatus;
import dev.toma.gunsrpg.common.quests.reward.QuestReward;
import dev.toma.gunsrpg.common.quests.sharing.QuestingGroup;
import dev.toma.gunsrpg.network.packet.S2C_OpenQuestScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class QuestingDataProvider implements ICapabilitySerializable<CompoundNBT> {

    @CapabilityInject(IQuestingData.class)
    public static final Capability<IQuestingData> QUESTING_DATA_CAPABILITY = null;
    private final LazyOptional<IQuestingData> instance;

    public QuestingDataProvider(World world) {
        this.instance = LazyOptional.of(() -> new QuestingData(world));
    }

    public static LazyOptional<IQuestingData> getData(World world) {
        return world.getCapability(QUESTING_DATA_CAPABILITY);
    }

    public static IQuestingData getQuesting(World world) {
        return getData(world).orElse(null);
    }

    public static S2C_OpenQuestScreen createQuestScreenRequest(MayorEntity mayor, PlayerEntity player) {
        IQuestingData questing = getQuesting(player.level);
        QuestingGroup group = questing.getOrCreateGroup(player);
        IPlayerData data = PlayerData.getUnsafe(player);
        ITraderStandings standings = data.getMayorReputationProvider();
        ITraderStatus traderStatus = standings.getStatusWithTrader(mayor.getUUID());
        ReputationStatus status = ReputationStatus.getStatus(traderStatus.getReputation());
        MayorEntity.ListedQuests quests = mayor.getQuestsForGroup(group.getGroupId());
        QuestReward pendingReward = mayor.findReward(player.getUUID());
        return new S2C_OpenQuestScreen(status, quests.toNbt(), mayor.getId(), mayor.getCurrentRefreshTarget(), pendingReward);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == QUESTING_DATA_CAPABILITY ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return (CompoundNBT) QUESTING_DATA_CAPABILITY.getStorage().writeNBT(QUESTING_DATA_CAPABILITY, instance.orElse(null), null);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        QUESTING_DATA_CAPABILITY.getStorage().readNBT(QUESTING_DATA_CAPABILITY, instance.orElse(null), null, nbt);
    }
}
