package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.common.ModRegistry;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.entity.EntityFlare;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.common.skills.interfaces.Clickable;
import dev.toma.gunsrpg.common.skills.interfaces.Cooldown;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.SPacketSkillClicked;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;

public class GodHelpUsSkill extends BasicSkill implements Cooldown, Clickable {

    private final int maxCooldown;
    private int currentCooldown;

    public GodHelpUsSkill(SkillType<?> type) {
        super(type);
        this.maxCooldown = 108000;
    }

    @Override
    public boolean apply(EntityPlayer user) {
        return currentCooldown == 0;
    }

    @Override
    public void onUse(EntityPlayer player) {
    }

    @Override
    public void clientHandleClicked() {
        NetworkManager.toServer(new SPacketSkillClicked(this.getType()));
    }

    @Override
    public void clicked(EntityPlayer player) {
        currentCooldown = maxCooldown;
        player.world.playSound(null, player.posX, player.posY, player.posZ, ModRegistry.GRPGSounds.FLARE_SHOOT, SoundCategory.MASTER, 10.0F, 1.0F);
        player.world.spawnEntity(new EntityFlare(player.world, player));
        PlayerDataFactory.get(player).sync();
    }

    @Override
    public int getCooldown() {
        return currentCooldown;
    }

    @Override
    public void setOnCooldown() {
        currentCooldown = maxCooldown;
    }

    @Override
    public int getMaxCooldown() {
        return maxCooldown;
    }

    @Override
    public void onUpdate(EntityPlayer player) {
        if(currentCooldown > 0) --currentCooldown;
    }

    @Override
    public void writeExtra(NBTTagCompound nbt) {
        nbt.setInteger("cooldown", currentCooldown);
    }

    @Override
    public void readExtra(NBTTagCompound nbt) {
        currentCooldown = nbt.getInteger("cooldown");
    }
}
