package dev.toma.gunsrpg.common.capability;

import dev.toma.gunsrpg.common.ModRegistry;
import dev.toma.gunsrpg.common.capability.object.*;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.common.skilltree.Ability;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.CPacketUpdateCap;
import dev.toma.gunsrpg.network.packet.SPacketSetShooting;
import dev.toma.gunsrpg.util.object.ShootingManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class PlayerDataFactory implements PlayerData {

    private final EntityPlayer player;
    private final DebuffData debuffData;
    private final SkillData skillData;
    private final AimInfo aimInfo;
    private final ReloadInfo reloadInfo;
    private final ScopeData scopeData;
    private final PlayerSkills playerSkills;

    private boolean shooting;
    private boolean logged = false;

    public PlayerDataFactory() {
        this(null);
    }

    public PlayerDataFactory(EntityPlayer player) {
        this.player = player;
        this.debuffData = new DebuffData();
        this.skillData = new SkillData(player);
        this.aimInfo = new AimInfo(this);
        this.reloadInfo = new ReloadInfo(this);
        this.scopeData = new ScopeData();
        this.playerSkills = new PlayerSkills(this);
    }

    public static boolean hasActiveSkill(EntityPlayer player, SkillType<?> type) {
        return get(player).getSkills().hasSkill(type);
    }

    @Deprecated
    public static boolean hasActiveSkill(EntityPlayer player, Ability.UnlockableType type) {
        Ability ability = get(player).getSkillData().getAbilityData().unlockedSkills.get(type);
        return ability != null && ability.enabled;
    }

    public static PlayerData get(EntityPlayer player) {
        return player.getCapability(PlayerDataManager.CAPABILITY, null);
    }

    @Override
    public void tick() {
        World world = player.world;
        this.debuffData.onTick(player);
        this.aimInfo.update();
        this.reloadInfo.update();
        this.playerSkills.update();
        if(shooting) {
            ItemStack stack = player.getHeldItemMainhand();
            boolean gun = stack.getItem() instanceof GunItem;
            if(gun && ShootingManager.canShoot(player, stack)) {
                GunItem gunItem = (GunItem) stack.getItem();
                if(world.isRemote) {
                    ShootingManager.shootSingle(player, stack);
                }
            }
            if(!world.isRemote && gun && ((GunItem) stack.getItem()).getAmmo(stack) <= 0) {
                setShooting(false);
                sync();
            }
        }
        if(world.isRemote) {
            this.clientTick();
        }
    }

    protected void clientTick() {
        if(shooting) {
            if(!Minecraft.getMinecraft().gameSettings.keyBindAttack.isKeyDown()) {
                setShooting(false);
                NetworkManager.toServer(new SPacketSetShooting(false));
            }
        }
    }

    @Override
    public DebuffData getDebuffData() {
        return debuffData;
    }

    @Override
    public SkillData getSkillData() {
        return skillData;
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
    public ScopeData getScopeData() {
        return scopeData;
    }

    @Override
    public PlayerSkills getSkills() {
        return playerSkills;
    }

    @Override
    public void setShooting(boolean shooting) {
        this.shooting = shooting;
    }

    @Override
    public boolean isShooting() {
        return shooting;
    }

    @Override
    public void sync() {
        if(player instanceof EntityPlayerMP) {
            NetworkManager.toClient((EntityPlayerMP) player, new CPacketUpdateCap(player.getUniqueID(), this.serializeNBT(), 0));
        }
    }

    @Override
    public void syncCloneData() {
        if(player instanceof EntityPlayerMP) {
            NetworkManager.toClient((EntityPlayerMP) player, new CPacketUpdateCap(player.getUniqueID(), this.writePermanentData(), 1));
        }
    }

    @Override
    public void handleLogin() {
        if(!logged) {
            logged = true;
            player.addItemStackToInventory(new ItemStack(ModRegistry.GRPGItems.ANTIDOTUM_PILLS, 2));
            player.addItemStackToInventory(new ItemStack(ModRegistry.GRPGItems.BANDAGE, 2));
            player.addItemStackToInventory(new ItemStack(ModRegistry.GRPGItems.VACCINE));
            player.addItemStackToInventory(new ItemStack(ModRegistry.GRPGItems.PLASTER_CAST));
            sync();
        }
    }

    public NBTTagCompound writePermanentData() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        nbtTagCompound.setBoolean("logged", logged);
        return nbtTagCompound;
    }

    public void readPermanentData(NBTTagCompound nbt) {
        logged = nbt.getBoolean("logged");
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("permanent", writePermanentData());
        nbt.setTag("debuffs", debuffData.serializeNBT());
        nbt.setTag("skills", skillData.write());
        nbt.setTag("aimData", aimInfo.write());
        nbt.setTag("reloadData", reloadInfo.write());
        nbt.setTag("scopeData", scopeData.write());
        nbt.setTag("playerSkills", playerSkills.writeData());
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        if(nbt.hasKey("permanent")) readPermanentData(nbt.getCompoundTag("permanent"));
        debuffData.deserializeNBT(nbt.hasKey("debuffs") ? nbt.getCompoundTag("debuffs") : new NBTTagCompound());
        skillData.read(nbt.hasKey("skills") ? nbt.getCompoundTag("skills") : new NBTTagCompound());
        aimInfo.read(this.findNBTTag("aimData", nbt));
        reloadInfo.read(this.findNBTTag("reloadData", nbt));
        scopeData.read(this.findNBTTag("scopeData", nbt));
        playerSkills.readData(this.findNBTTag("playerSkills", nbt));
    }

    private NBTTagCompound findNBTTag(String key, NBTTagCompound nbt) {
        return nbt.hasKey(key) ? nbt.getCompoundTag(key) : new NBTTagCompound();
    }

    public EntityPlayer getPlayer() {
        return player;
    }
}
