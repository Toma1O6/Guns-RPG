package dev.toma.gunsrpg.common.capability.object;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.IDisplayableSkill;
import dev.toma.gunsrpg.api.common.ISkill;
import dev.toma.gunsrpg.api.common.ITickableSkill;
import dev.toma.gunsrpg.api.common.data.DataFlags;
import dev.toma.gunsrpg.api.common.data.ILockStateChangeable;
import dev.toma.gunsrpg.api.common.data.IPlayerCapEntry;
import dev.toma.gunsrpg.api.common.data.ISkills;
import dev.toma.gunsrpg.common.init.ModRegistries;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.common.skills.criteria.GunCriteria;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.CPacketNewSkills;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.play.server.SPlaySoundEffectPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.Constants;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.*;
import java.util.function.Supplier;

public class PlayerSkills implements ISkills, ILockStateChangeable, IPlayerCapEntry {

    private static final Marker SKILLS = MarkerManager.getMarker("Skills");
    private final PlayerEntity player;
    private final Map<SkillType<?>, ISkill> unlockedSkills = new HashMap<>();
    private final SkillCache cache = new SkillCache();
    private IClientSynchReq request = () -> {};

    public PlayerSkills(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public void tick(PlayerEntity player) {
        cache.getTickables().forEach(skill -> skill.onUpdate(player));
    }

    @Override
    public boolean hasSkill(SkillType<?> type) {
        return unlockedSkills.containsKey(type);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <S extends ISkill> S getSkill(SkillType<S> type) {
        return (S) unlockedSkills.get(type);
    }

    @Override
    public void setClientSynch(IClientSynchReq request) {
        this.request = request;
    }

    @Override
    public Collection<ISkill> getUnlockedSkills() {
        return unlockedSkills.values();
    }

    @Override
    public void onLevelUp(int level, PlayerEntity player) {
        List<SkillType<?>> newlyAvailableList = new ArrayList<>();
        for (SkillType<?> type : ModRegistries.SKILLS) {
            if (!(type.getCriteria() instanceof GunCriteria) && type.levelRequirement == level) {
                newlyAvailableList.add(type);
            }
        }
        int count = newlyAvailableList.size();
        if (count > 0) {
            player.sendMessage(new StringTextComponent(String.format(TextFormatting.YELLOW + "New skills available: %d", count)), Util.NIL_UUID);
            NetworkManager.sendClientPacket((ServerPlayerEntity) player, new CPacketNewSkills(newlyAvailableList));
        }
        if (!player.level.isClientSide()) {
            ((ServerPlayerEntity) player).connection.send(new SPlaySoundEffectPacket(SoundEvents.PLAYER_LEVELUP, SoundCategory.MASTER, player.getX(), player.getY(), player.getZ(), 0.75F, 1.0F));
        }
    }

    @Override
    public void doUnlock() {
        for (SkillType<?> skill : ModRegistries.SKILLS) {
            unlock(skill, false);
        }
        cache.compute();
    }

    @Override
    public void doLock() {
        for (ISkill skill : unlockedSkills.values()) {
            skill.onDeactivate(player);
        }
        unlockedSkills.clear();
        cache.clear();
    }

    @Override
    public void unlock(SkillType<?> skillType) {
        unlock(skillType, true);
    }

    @Override
    public void unlock(SkillType<?> skillType, boolean sync) {
        ISkill skill = skillType.instantiate();
        skill.onPurchase(player);
        unlockedSkills.put(skillType, skill);
        if (sync)
            request.makeSyncRequest();
    }

    @Override
    public List<IDisplayableSkill> getDisplayableSkills() {
        return cache.getDisplayables();
    }

    @Override
    public int getFlag() {
        return DataFlags.SKILLS;
    }

    @Override
    public void toNbt(CompoundNBT nbt) {
        ListNBT list = new ListNBT();
        unlockedSkills.values().forEach(skill -> skillToNbt(skill, list));
        nbt.put("skills", list);
    }

    @Override
    public void fromNbt(CompoundNBT nbt) {
        unlockedSkills.clear();
        ListNBT list = nbt.contains("skills", Constants.NBT.TAG_LIST) ? nbt.getList("skills", Constants.NBT.TAG_COMPOUND) : new ListNBT();
        for (int i = 0; i < list.size(); i++) {
            CompoundNBT cnbt = list.getCompound(i);
            skillFromNbt(cnbt);
        }
    }

    private void skillToNbt(ISkill skill, ListNBT listNBT) {
        SkillType<?> type = skill.getType();
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("type", type.getRegistryName().toString());
        CompoundNBT data = skill.saveData();
        if (data != null) {
            nbt.put("data", data);
        }
        listNBT.add(nbt);
    }

    private void skillFromNbt(CompoundNBT nbt) {
        ResourceLocation key = new ResourceLocation(nbt.getString("type"));
        SkillType<?> type = ModRegistries.SKILLS.getValue(key);
        if (type == null)
            GunsRPG.log.error(SKILLS, "Unknown skill type: " + key);
        ISkill skill = type.instantiate();
        if (nbt.contains("data", Constants.NBT.TAG_COMPOUND)) {
            CompoundNBT data = nbt.getCompound("data");
            skill.readData(data);
        }
        unlockedSkills.put(type, skill);
        skill.onPurchase(player);
    }

    private class SkillCache {

        private final List<ITickableSkill> tickables = new ArrayList<>();
        private final List<IDisplayableSkill> displayables = new ArrayList<>();
        private boolean requiresComputation = true;

        public void compute() {
            clear();
            Collection<ISkill> collection = PlayerSkills.this.unlockedSkills.values();
            for (ISkill skill : collection) {
                if (skill instanceof ITickableSkill)
                    tickables.add((ITickableSkill) skill);
                if (skill instanceof IDisplayableSkill)
                    displayables.add((IDisplayableSkill) skill);
            }
            requiresComputation = false;
        }

        public void clear() {
            tickables.clear();
            displayables.clear();
            requiresComputation = true;
        }

        public List<ITickableSkill> getTickables() {
            return get(() -> tickables);
        }

        public List<IDisplayableSkill> getDisplayables() {
            return get(() -> displayables);
        }

        private <T> T get(Supplier<T> supplier) {
            if (requiresComputation) {
                compute();
            }
            return supplier.get();
        }
    }
}
