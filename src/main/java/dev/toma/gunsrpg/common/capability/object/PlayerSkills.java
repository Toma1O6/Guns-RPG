package dev.toma.gunsrpg.common.capability.object;

import dev.toma.gunsrpg.api.common.IDisplayableSkill;
import dev.toma.gunsrpg.api.common.ISkill;
import dev.toma.gunsrpg.api.common.ITickableSkill;
import dev.toma.gunsrpg.api.common.data.DataFlags;
import dev.toma.gunsrpg.api.common.data.ILockStateChangeable;
import dev.toma.gunsrpg.api.common.data.IPlayerCapEntry;
import dev.toma.gunsrpg.api.common.data.ISkills;
import dev.toma.gunsrpg.common.init.ModRegistries;
import dev.toma.gunsrpg.common.skills.core.SkillCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.common.skills.criteria.GunCriteria;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.CPacketNewSkills;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SPlaySoundEffectPacket;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.*;
import java.util.function.Supplier;

public class PlayerSkills implements ISkills, ILockStateChangeable, IPlayerCapEntry {

    private final PlayerEntity player;
    private final Map<SkillCategory, List<ISkill>> unlockedSkills = new HashMap<>();
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
        List<ISkill> list = unlockedSkills.get(type.category);
        if (list == null) return false;
        return ModUtils.contains(type, list, SkillType::areTypesEqual);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <S extends ISkill> S getSkill(SkillType<S> type) {
        SkillCategory category = type.category;
        List<ISkill> list = unlockedSkills.get(category);
        if (list == null) return null;
        for (ISkill skill : list) {
            if (type.areTypesEqual(skill)) {
                return (S) skill;
            }
        }
        return null;
    }

    @Override
    public void setClientSynch(IClientSynchReq request) {
        this.request = request;
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
        for (Map.Entry<SkillCategory, List<ISkill>> entry : unlockedSkills.entrySet()) {
            for (ISkill skill : entry.getValue()) {
                skill.onDeactivate(player);
            }
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
        SkillCategory category = skillType.category;
        List<ISkill> list = unlockedSkills.computeIfAbsent(category, cat -> new ArrayList<>());
        if (!ModUtils.contains(skillType, list, SkillType::areTypesEqual)) {
            cache.clear();
            ISkill skill = skillType.instantiate();
            skill.onPurchase(player);
            list.add(skill);
            if (sync) request.makeSyncRequest();
        }
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
        // TODO
    }

    @Override
    public void fromNbt(CompoundNBT nbt) {
        // TODO
    }

    private class SkillCache {

        private final List<ITickableSkill> tickables = new ArrayList<>();
        private final List<IDisplayableSkill> displayables = new ArrayList<>();
        private boolean requiresComputation = true;

        public void compute() {
            clear();
            Collection<List<ISkill>> collection = PlayerSkills.this.unlockedSkills.values();
            for (List<ISkill> list : collection) {
                for (ISkill skill : list) {
                    if (skill instanceof ITickableSkill)
                        tickables.add((ITickableSkill) skill);
                    if (skill instanceof IDisplayableSkill)
                        displayables.add((IDisplayableSkill) skill);
                }
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
