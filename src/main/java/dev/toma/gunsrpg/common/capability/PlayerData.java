package dev.toma.gunsrpg.common.capability;

import dev.toma.gunsrpg.api.common.data.*;
import dev.toma.gunsrpg.api.common.skill.ISkill;
import dev.toma.gunsrpg.common.attribute.IAttributeProvider;
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
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.DistExecutor;

import java.util.List;
import java.util.function.Function;

public class PlayerData implements IPlayerData {

    private final IEventHandler<IPlayerCapEntry> saveHandler = IEventHandler.newEventHandler();
    private final PlayerEntity player;
    private final AimInfo aimInfo;
    private final ReloadInfo reloadInfo;
    private final PlayerSkillProvider skillProvider;
    private final PlayerQuests playerQuests;
    private final PlayerDebuffs debuffs;
    private final PlayerAttributes attributes;
    private final PlayerProgressionData data;
    private ISynchCallback callback;

    public PlayerData() {
        this(null);
    }

    public PlayerData(PlayerEntity player) {
        this.player = player;
        this.debuffs = new PlayerDebuffs();
        this.aimInfo = new AimInfo();
        this.reloadInfo = new ReloadInfo(this::getAttributes);
        this.attributes = new PlayerAttributes();
        this.skillProvider = new PlayerSkillProvider(player);
        this.playerQuests = new PlayerQuests();
        this.data = new PlayerProgressionData(player, skillProvider);

        DistExecutor.runWhenOn(Dist.CLIENT, () -> this::setSyncCallback);

        saveHandler.addListener(debuffs);
        saveHandler.addListener(aimInfo);
        saveHandler.addListener(attributes);
        saveHandler.addListener(skillProvider);
        saveHandler.addListener(playerQuests);
        saveHandler.addListener(data);

        saveHandler.invoke(entry -> entry.setClientSynch(() -> requestSync(entry.getFlag())));
    }

    @Override
    public void tick() {
        this.debuffs.tick(player);
        this.reloadInfo.tick(player);
        this.aimInfo.tick(player, reloadInfo);
        this.skillProvider.tick(player);
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
    public ISkillProvider getSkillProvider() {
        return skillProvider;
    }

    @Override
    public IQuests getPlayerQuests() {
        return playerQuests;
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
    public void sync(int flags) {
        if (player instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            if (serverPlayer.connection == null)
                return;
            NetworkManager.sendClientPacket(serverPlayer, new S2C_UpdateCapabilityPacket(player.getUUID(), toNbt(flags), flags));
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
        return nbt;
    }

    @Override
    public void fromNbt(CompoundNBT nbt, int flags) {
        saveHandler.invokeIf(entry -> DataFlags.is(entry.getFlag(), flags), entry -> entry.fromNbt(nbt));
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

    public static <T> T getValueSafe(PlayerEntity player, Function<IPlayerData, T> func, T fallback) {
        LazyOptional<IPlayerData> optional = get(player);
        if (optional.isPresent()) {
            IPlayerData data = optional.orElse(null);
            return func.apply(data);
        }
        return fallback;
    }
}
