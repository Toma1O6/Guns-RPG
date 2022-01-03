package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.api.common.skill.IClickableSkill;
import dev.toma.gunsrpg.api.common.skill.ICooldown;
import dev.toma.gunsrpg.api.common.data.DataFlags;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.entity.FlareEntity;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.C2S_SkillClickedPacket;
import dev.toma.gunsrpg.util.Interval;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundCategory;

public class GodHelpUsSkill extends BasicSkill implements ICooldown, IClickableSkill {

    private final int maxCooldown;
    private int currentCooldown;

    public GodHelpUsSkill(SkillType<?> type) {
        super(type);
        this.maxCooldown = Interval.hours(1).append(Interval.minutes(30)).valueIn(Interval.Unit.TICK);
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
        NetworkManager.sendServerPacket(new C2S_SkillClickedPacket(this.getType()));
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
    public CompoundNBT saveData() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("cooldown", currentCooldown);
        return nbt;
    }

    @Override
    public void readData(CompoundNBT nbt) {
        currentCooldown = nbt.getInt("cooldown");
    }
}
