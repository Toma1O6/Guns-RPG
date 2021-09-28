package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.api.common.IClickableSkill;
import dev.toma.gunsrpg.api.common.ICooldown;
import dev.toma.gunsrpg.api.common.data.DataFlags;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.entity.FlareEntity;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.SPacketSkillClicked;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundCategory;

public class GodHelpUsSkill extends BasicSkill implements ICooldown, IClickableSkill {

    private final int maxCooldown;
    private int currentCooldown;

    public GodHelpUsSkill(SkillType<?> type) {
        super(type);
        this.maxCooldown = 108000;
    }

    @Override
    public boolean canApply(PlayerEntity user) {
        return currentCooldown == 0;
    }

    @Override
    public void onUse(PlayerEntity player) {
    }

    @Override
    public void clientHandleClicked() {
        NetworkManager.sendServerPacket(new SPacketSkillClicked(this.getType()));
    }

    @Override
    public void clicked(PlayerEntity player) {
        currentCooldown = maxCooldown;
        player.level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.FLARE_SHOOT, SoundCategory.MASTER, 10.0F, 1.0F);
        player.level.addFreshEntity(new FlareEntity(player.level, player));
        PlayerData.get(player).ifPresent(data -> data.sync(DataFlags.SKILLS));
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
    public void onUpdate(PlayerEntity player) {
        if (currentCooldown > 0) --currentCooldown;
    }

    @Override
    public void writeExtra(CompoundNBT nbt) {
        nbt.putInt("cooldown", currentCooldown);
    }

    @Override
    public void readExtra(CompoundNBT nbt) {
        currentCooldown = nbt.getInt("cooldown");
    }
}
