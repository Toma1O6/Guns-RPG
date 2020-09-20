package dev.toma.gunsrpg.common.capability;

import dev.toma.gunsrpg.common.capability.object.*;
import dev.toma.gunsrpg.common.init.GRPGItems;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.skills.core.ISkill;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.CPacketUpdateCap;
import dev.toma.gunsrpg.network.packet.SPacketSetShooting;
import dev.toma.gunsrpg.util.object.ShootingManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class PlayerDataFactory implements PlayerData {

    private final EntityPlayer player;
    private final DebuffData debuffData;
    private final AimInfo aimInfo;
    private final ReloadInfo reloadInfo;
    private final ScopeData scopeData;
    private final PlayerSkills playerSkills;
    private int reducedHealthTimer;

    private boolean shooting;
    private boolean logged = false;

    public PlayerDataFactory() {
        this(null);
    }

    public PlayerDataFactory(EntityPlayer player) {
        this.player = player;
        this.debuffData = new DebuffData(this);
        this.aimInfo = new AimInfo(this);
        this.reloadInfo = new ReloadInfo(this);
        this.scopeData = new ScopeData();
        this.playerSkills = new PlayerSkills(this);
    }

    public static boolean hasActiveSkill(EntityPlayer player, SkillType<?> type) {
        return get(player).getSkills().hasSkill(type);
    }

    public static <S extends ISkill> S getSkill(EntityPlayer player, SkillType<S> type) {
        return get(player).getSkills().getSkill(type);
    }

    public static PlayerData get(EntityPlayer player) {
        return player.getCapability(PlayerDataManager.CAPABILITY, null);
    }

    @Override
    public void setOnCooldown() {
        reducedHealthTimer = 3600;
    }

    @Override
    public void tick() {
        World world = player.world;
        this.debuffData.onTick(player, this);
        this.aimInfo.update();
        this.reloadInfo.update();
        this.playerSkills.update();
        if(!world.isRemote && reducedHealthTimer > 0) {
            --reducedHealthTimer;
            double value = player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).getBaseValue();
            if(value != 6) {
                player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(6.0);
                if(player.getHealth() > player.getMaxHealth()) {
                    player.setHealth(player.getMaxHealth());
                }
            }
            if(reducedHealthTimer == 0) {
                double d = getSkills().hasSkill(Skills.WAR_MACHINE) ? 40 : 20;
                player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(d);
            }
        }
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
            player.addItemStackToInventory(new ItemStack(GRPGItems.ANTIDOTUM_PILLS, 2));
            player.addItemStackToInventory(new ItemStack(GRPGItems.BANDAGE, 2));
            player.addItemStackToInventory(new ItemStack(GRPGItems.VACCINE));
            player.addItemStackToInventory(new ItemStack(GRPGItems.PLASTER_CAST));
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
        nbt.setTag("aimData", aimInfo.write());
        nbt.setTag("reloadData", reloadInfo.write());
        nbt.setTag("scopeData", scopeData.write());
        nbt.setTag("playerSkills", playerSkills.writeData());
        nbt.setInteger("healthCooldown", reducedHealthTimer);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        if(nbt.hasKey("permanent")) readPermanentData(nbt.getCompoundTag("permanent"));
        debuffData.deserializeNBT(nbt.hasKey("debuffs", Constants.NBT.TAG_LIST) ? nbt.getTagList("debuffs", Constants.NBT.TAG_COMPOUND) : new NBTTagList());
        aimInfo.read(this.findNBTTag("aimData", nbt));
        reloadInfo.read(this.findNBTTag("reloadData", nbt));
        scopeData.read(this.findNBTTag("scopeData", nbt));
        playerSkills.readData(this.findNBTTag("playerSkills", nbt));
        reducedHealthTimer = nbt.getInteger("healthCooldown");
    }

    private NBTTagCompound findNBTTag(String key, NBTTagCompound nbt) {
        return nbt.hasKey(key) ? nbt.getCompoundTag(key) : new NBTTagCompound();
    }

    public EntityPlayer getPlayer() {
        return player;
    }
}
