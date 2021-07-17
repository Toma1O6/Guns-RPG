package dev.toma.gunsrpg.common.capability;

import dev.toma.gunsrpg.common.capability.object.AimInfo;
import dev.toma.gunsrpg.common.capability.object.DebuffData;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.common.capability.object.ReloadInfo;
import dev.toma.gunsrpg.common.init.GRPGItems;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.skills.core.ISkill;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.GRPGConfig;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.CPacketUpdateCap;
import dev.toma.gunsrpg.sided.ClientSideManager;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerDataFactory implements PlayerData {

    private final PlayerEntity player;
    private final DebuffData debuffData;
    private final AimInfo aimInfo;
    private final ReloadInfo reloadInfo;
    private final PlayerSkills playerSkills;
    private int reducedHealthTimer;
    private ISynchCallback callback;

    private boolean logged = false;

    public PlayerDataFactory() {
        this(null);
    }

    public PlayerDataFactory(PlayerEntity player) {
        this.player = player;
        this.debuffData = new DebuffData(this);
        this.aimInfo = new AimInfo(this);
        this.reloadInfo = new ReloadInfo(this);
        this.playerSkills = new PlayerSkills(this);
        ClientSideManager.runOnClient(() -> () -> setSyncCallback(ClientSideManager.instance()::onDataSync));
    }

    public static boolean hasActiveSkill(PlayerEntity player, SkillType<?> type) {
        LazyOptional<PlayerData> optional = get(player);
        if (optional.isPresent()) {
            return optional.orElse(null).getSkills().hasSkill(type);
        }
        return false;
    }

    public static <S extends ISkill> S getSkill(PlayerEntity player, SkillType<S> type) {
        LazyOptional<PlayerData> optional = get(player);
        if (optional.isPresent()) {
            return optional.orElse(null).getSkills().getSkill(type);
        }
        return null;
    }

    public static LazyOptional<PlayerData> get(PlayerEntity player) {
        return player.getCapability(PlayerDataManager.CAPABILITY, null);
    }

    public static PlayerData getUnsafe(PlayerEntity player) {
        return get(player).orElseThrow(NullPointerException::new);
    }

    @Override
    public void setOnCooldown() {
        if (!GRPGConfig.debuffConfig.disableRespawnDebuff()) {
            reducedHealthTimer = 3600;
        } else {
            reducedHealthTimer = 0;
            double d = getSkills().hasSkill(Skills.WAR_MACHINE) ? 40 : 20;
            player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(d);
            player.setHealth(player.getMaxHealth());
        }
    }

    @Override
    public void tick() {
        World world = player.level;
        this.debuffData.tick(player, this);
        this.aimInfo.update();
        this.reloadInfo.update();
        this.playerSkills.update();
        if (!world.isClientSide && reducedHealthTimer > 0) {
            --reducedHealthTimer;
            double value = player.getAttributeBaseValue(Attributes.MAX_HEALTH);
            if (value != 6) {
                player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(6.0);
                if (player.getHealth() > player.getMaxHealth()) {
                    player.setHealth(player.getMaxHealth());
                }
            }
            if (reducedHealthTimer == 0) {
                double d = getSkills().hasSkill(Skills.WAR_MACHINE) ? 40 : 20;
                player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(d);
            }
        }
    }

    @Override
    public DebuffData getDebuffData() {
        return debuffData;
    }

    @Override
    public AimInfo getAimInfo() {
        return aimInfo;
    }

    @Override
    public ReloadInfo getReloadInfo() {
        return reloadInfo;
    }

    @Override
    public PlayerSkills getSkills() {
        return playerSkills;
    }

    @Override
    public void sync() {
        if (player instanceof ServerPlayerEntity) {
            NetworkManager.sendClientPacket((ServerPlayerEntity) player, new CPacketUpdateCap(player.getUUID(), this.serializeNBT(), 0));
        }
    }

    @Override
    public void handleLogin() {
        if (!logged) {
            logged = true;
            player.addItem(new ItemStack(GRPGItems.ANTIDOTUM_PILLS, 2));
            player.addItem(new ItemStack(GRPGItems.BANDAGE, 2));
            player.addItem(new ItemStack(GRPGItems.VACCINE));
            player.addItem(new ItemStack(GRPGItems.PLASTER_CAST));
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

    public CompoundNBT writePermanentData() {
        CompoundNBT nbtTagCompound = new CompoundNBT();
        nbtTagCompound.putBoolean("logged", logged);
        return nbtTagCompound;
    }

    public void readPermanentData(CompoundNBT nbt) {
        logged = nbt.getBoolean("logged");
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("permanent", writePermanentData());
        nbt.put("debuffs", debuffData.serializeNBT());
        nbt.put("aimData", aimInfo.write());
        nbt.put("reloadData", reloadInfo.write());
        nbt.put("playerSkills", playerSkills.writeData());
        nbt.putInt("healthCooldown", reducedHealthTimer);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (nbt.contains("permanent")) readPermanentData(nbt.getCompound("permanent"));
        debuffData.deserializeNBT(nbt.contains("debuffs", Constants.NBT.TAG_LIST) ? nbt.getList("debuffs", Constants.NBT.TAG_COMPOUND) : new ListNBT());
        aimInfo.read(this.findNBTTag("aimData", nbt));
        reloadInfo.read(this.findNBTTag("reloadData", nbt));
        playerSkills.readData(this.findNBTTag("playerSkills", nbt));
        reducedHealthTimer = nbt.getInt("healthCooldown");
    }

    private CompoundNBT findNBTTag(String key, CompoundNBT nbt) {
        return nbt.contains(key) ? nbt.getCompound(key) : new CompoundNBT();
    }

    public PlayerEntity getPlayer() {
        return player;
    }
}
