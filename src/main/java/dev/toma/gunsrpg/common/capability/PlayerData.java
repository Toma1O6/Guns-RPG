package dev.toma.gunsrpg.common.capability;

import dev.toma.gunsrpg.api.common.ISyncRequestDispatcher;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.data.*;
import dev.toma.gunsrpg.api.common.skill.ISkill;
import dev.toma.gunsrpg.common.capability.object.*;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.S2C_UpdateCapabilityPacket;
import dev.toma.gunsrpg.sided.ClientSideManager;
import dev.toma.gunsrpg.util.IEventHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.DistExecutor;

import java.util.List;

public class PlayerData implements IPlayerData {

    private final IEventHandler<IPlayerCapEntry> saveHandler = IEventHandler.newEventHandler();
    private final HandState handState;
    private final PlayerEntity player;
    private final AimInfo aimInfo;
    private final ReloadInfo reloadInfo;
    private final JamInfo jamInfo;
    private final PlayerSkillProvider skillProvider;
    private final PlayerDebuffs debuffs;
    private final PlayerAttributes attributes;
    private final PlayerPerkProvider perkProvider;
    private final WeaponSharedPool weaponSharedPool;
    private final PlayerProgressionData data;
    private final PlayerTraderStandings standings;
    private ISynchCallback callback;

    public PlayerData() {
        this(null);
    }

    public PlayerData(PlayerEntity player) {
        this.player = player;
        this.handState = new HandState(() -> sync(0));
        this.debuffs = new PlayerDebuffs();
        this.aimInfo = new AimInfo(handState);
        this.reloadInfo = new ReloadInfo(this::getAttributes, handState);
        this.jamInfo = new JamInfo(handState, player);
        this.attributes = new PlayerAttributes();
        this.skillProvider = new PlayerSkillProvider(player);
        this.perkProvider = new PlayerPerkProvider(attributes);
        this.weaponSharedPool = new WeaponSharedPool();
        this.data = new PlayerProgressionData(player, skillProvider, this.weaponSharedPool);
        this.standings = new PlayerTraderStandings(this.player);

        DistExecutor.runWhenOn(Dist.CLIENT, () -> this::setSyncCallback);

        saveHandler.addListener(debuffs);
        saveHandler.addListener(aimInfo);
        saveHandler.addListener(jamInfo);
        saveHandler.addListener(attributes);
        saveHandler.addListener(skillProvider);
        saveHandler.addListener(perkProvider);
        saveHandler.addListener(data);
        saveHandler.addListener(standings);
        saveHandler.addListener(weaponSharedPool);

        saveHandler.invoke(entry -> entry.setSyncRequestTemplate(() -> requestSync(entry.getFlag())));
    }

    @Override
    public void tick() {
        this.debuffs.tick(player);
        this.reloadInfo.tick(player);
        this.aimInfo.tick(player);
        this.jamInfo.tick();
        this.skillProvider.tick(player);
        this.attributes.tick();
        this.perkProvider.tick();
    }

    @Override
    public IHandState getHandState() {
        return handState;
    }

    @Override
    public IAimInfo getAimInfo() {
        return aimInfo;
    }

    @Override
    public IReloadInfo getReloadInfo() {
        return reloadInfo;
    }

    @Override
    public IJamInfo getJamInfo() {
        return jamInfo;
    }

    @Override
    public ISkillProvider getSkillProvider() {
        return skillProvider;
    }

    @Override
    public IPerkProvider getPerkProvider() {
        return perkProvider;
    }

    @Override
    public IPointProvider getSharedWeaponPoints() {
        return this.weaponSharedPool;
    }

    @Override
    public IDebuffs getDebuffControl() {
        return debuffs;
    }

    @Override
    public IAttributeProvider getAttributes() {
        return attributes;
    }

    @Override
    public IProgressData getProgressData() {
        return data;
    }

    @Override
    public ITraderStandings getMayorReputationProvider() {
        return this.standings;
    }

    @Override
    public void sync(int flags) {
        if (player instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            if (serverPlayer.connection == null)
                return;
            S2C_UpdateCapabilityPacket syncPacket = new S2C_UpdateCapabilityPacket(player.getUUID(), toNbt(flags), flags);
            NetworkManager.sendClientPacket(serverPlayer, syncPacket);
            NetworkManager.sendToAllTracking(serverPlayer, syncPacket);
        }
    }

    @Override
    public void setSyncCallback(ISynchCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onSync() {
        if (callback != null) callback.onSynch();
    }

    @Override
    public List<IPlayerCapEntry> getSaveEntries() {
        return saveHandler.listAll();
    }

    @Override
    public CompoundNBT toNbt(int flags) {
        CompoundNBT nbt = new CompoundNBT();
        saveHandler.invokeIf(entry -> DataFlags.is(entry.getFlag(), flags), entry -> entry.toNbt(nbt));
        nbt.put("handState", handState.serializeNBT());
        return nbt;
    }

    @Override
    public void fromNbt(CompoundNBT nbt, int flags) {
        saveHandler.invokeIf(entry -> DataFlags.is(entry.getFlag(), flags), entry -> entry.fromNbt(nbt));
        handState.deserializeNBT(nbt.contains("handState", Constants.NBT.TAG_COMPOUND) ? nbt.getCompound("handState") : new CompoundNBT());
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    private void requestSync(int flag) {
        sync(flag);
    }

    @OnlyIn(Dist.CLIENT)
    private void setSyncCallback() {
        setSyncCallback(ClientSideManager.instance().onDataSync());
    }

    public static boolean hasActiveSkill(PlayerEntity player, SkillType<?> type) {
        LazyOptional<IPlayerData> optional = get(player);
        if (optional.isPresent()) {
            return optional.orElse(null).getSkillProvider().hasSkill(type);
        }
        return false;
    }

    public static <S extends ISkill> S getSkill(PlayerEntity player, SkillType<S> type) {
        LazyOptional<IPlayerData> optional = get(player);
        if (optional.isPresent()) {
            return optional.orElse(null).getSkillProvider().getSkill(type);
        }
        return null;
    }

    public static LazyOptional<IPlayerData> get(PlayerEntity player) {
        return player.getCapability(PlayerDataProvider.CAPABILITY, null);
    }

    public static IPlayerData getUnsafe(PlayerEntity player) {
        return get(player).orElseThrow(NullPointerException::new);
    }

    private static final class WeaponSharedPool implements IPointProvider, IPlayerCapEntry, ILockStateChangeable {

        private int availablePoints;
        private ISyncRequestDispatcher dispatcher;

        @Override
        public void addPoints(int amount) {
            this.availablePoints = Math.max(this.availablePoints + amount, 0);
            if (this.dispatcher != null)
                this.dispatcher.sendSyncRequest();
        }

        @Override
        public int getPoints() {
            return this.availablePoints;
        }

        @Override
        public int getFlag() {
            return DataFlags.WEAPON_POOL;
        }

        @Override
        public void toNbt(CompoundNBT nbt) {
            nbt.putInt("points", this.availablePoints);
        }

        @Override
        public void fromNbt(CompoundNBT nbt) {
            this.availablePoints = nbt.getInt("points");
        }

        @Override
        public void setSyncRequestTemplate(ISyncRequestDispatcher request) {
            this.dispatcher = request;
        }

        @Override
        public void doUnlock() {
            this.availablePoints = Short.MAX_VALUE;
        }

        @Override
        public void doLock() {
            this.availablePoints = 0;
        }
    }
}
