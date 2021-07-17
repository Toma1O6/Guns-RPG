package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.entity.EntityFlare;
import dev.toma.gunsrpg.common.init.GRPGSounds;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.common.skills.interfaces.Clickable;
import dev.toma.gunsrpg.common.skills.interfaces.Cooldown;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.SPacketSkillClicked;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundCategory;

public class GodHelpUsSkill extends BasicSkill implements Cooldown, Clickable {

    private final int maxCooldown;
    private int currentCooldown;

    public GodHelpUsSkill(SkillType<?> type) {
        super(type);
        this.maxCooldown = 108000;
    }

    @Override
    public boolean apply(PlayerEntity user) {
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
        player.level.playSound(null, player.getX(), player.getY(), player.getZ(), GRPGSounds.FLARE_SHOOT, SoundCategory.MASTER, 10.0F, 1.0F);
        player.level.addFreshEntity(new EntityFlare(player.level, player));
        PlayerDataFactory.get(player).ifPresent(PlayerData::sync);
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
