package dev.toma.gunsrpg.common.capability;

import dev.toma.gunsrpg.api.common.ISkill;
import dev.toma.gunsrpg.api.common.data.*;
import dev.toma.gunsrpg.common.capability.object.*;
import dev.toma.gunsrpg.common.init.ModItems;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.CPacketUpdateCap;
import dev.toma.gunsrpg.sided.ClientSideManager;
import dev.toma.gunsrpg.util.IEventHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.DistExecutor;

public class PlayerData implements IPlayerData {

    private final IEventHandler<IPlayerCapEntry> saveHandler = IEventHandler.newEventHandler();
    private final PlayerEntity player;
    private final AimInfo aimInfo;
    private final ReloadInfo reloadInfo;
    private final PlayerSkills playerSkills;
    private final PlayerQuests playerQuests;
    private final PlayerDebuffs debuffs;
    private ISynchCallback callback;

    private boolean logged = false;

    public PlayerData() {
        this(null);
    }

    public PlayerData(PlayerEntity player) {
        this.player = player;
        this.debuffs = new PlayerDebuffs();
        this.aimInfo = new AimInfo(this);
        this.reloadInfo = new ReloadInfo(this);
        this.playerSkills = new PlayerSkills(this);
        this.playerQuests = new PlayerQuests();

        DistExecutor.runWhenOn(Dist.CLIENT, () -> this::setSyncCallback);

        saveHandler.addListener(debuffs);

        saveHandler.invoke(entry -> entry.setClientSynch(this::sync));
    }

    @OnlyIn(Dist.CLIENT)
    private void setSyncCallback() {
        setSyncCallback(ClientSideManager.instance().onDataSync());
    }

    public static boolean hasActiveSkill(PlayerEntity player, SkillType<?> type) {
        LazyOptional<IPlayerData> optional = get(player);
        if (optional.isPresent()) {
            return optional.orElse(null).getSkills().hasSkill(type);
        }
        return false;
    }

    public static <S extends ISkill> S getSkill(PlayerEntity player, SkillType<S> type) {
        LazyOptional<IPlayerData> optional = get(player);
        if (optional.isPresent()) {
            return optional.orElse(null).getSkills().getSkill(type);
        }
        return null;
    }

    public static LazyOptional<IPlayerData> get(PlayerEntity player) {
        return player.getCapability(PlayerDataProvider.CAPABILITY, null);
    }

    public static IPlayerData getUnsafe(PlayerEntity player) {
        return get(player).orElseThrow(NullPointerException::new);
    }

    @Override
    public PlayerQuests getPlayerQuests() {
        return playerQuests;
    }

    @Override
    public IDebuffs getDebuffControl() {
        return debuffs;
    }

    @Override
    public void tick() {
        this.debuffs.tick(player);
        this.aimInfo.update();
        this.reloadInfo.tick();
        this.playerSkills.update();
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
    public PlayerSkills getSkills() {
        return playerSkills;
    }

    @Override
    public void sync() {
        if (player instanceof ServerPlayerEntity) {
            NetworkManager.sendClientPacket((ServerPlayerEntity) player, new CPacketUpdateCap(player.getUUID(), this.serializeNBT()));
        }
    }

    @Override
    public void handleLogin() {
        if (!logged) {
            logged = true;
            player.addItem(new ItemStack(ModItems.ANTIDOTUM_PILLS, 2));
            player.addItem(new ItemStack(ModItems.BANDAGE, 2));
            player.addItem(new ItemStack(ModItems.VACCINE));
            player.addItem(new ItemStack(ModItems.PLASTER_CAST));
            sync();
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
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putBoolean("logged", logged);
        saveHandler.invoke(entry -> entry.toNbt(nbt));
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        logged = nbt.getBoolean("logged");
        saveHandler.invoke(entry -> entry.fromNbt(nbt));
    }

    public PlayerEntity getPlayer() {
        return player;
    }
}
