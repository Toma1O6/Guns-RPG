package dev.toma.gunsrpg.api.common.event;

import dev.toma.gunsrpg.common.quests.quest.area.QuestArea;
import dev.toma.gunsrpg.common.quests.sharing.GroupInvite;
import dev.toma.gunsrpg.common.quests.sharing.QuestingGroup;
import dev.toma.gunsrpg.util.object.Interaction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

import java.util.Objects;
import java.util.UUID;

public abstract class QuestingEvent extends Event {

    protected final World level;

    public QuestingEvent(World level) {
        this.level = level;
    }

    public World getLevel() {
        return level;
    }

    public static class OnInviteSending extends QuestingEvent {

        private final QuestingGroup group;
        private final UUID invitedById;
        private final UUID invitedId;

        private Interaction<Void> result = Interaction.success(null);

        public OnInviteSending(World world, QuestingGroup group, UUID invitedBy, UUID invitedId) {
            super(world);
            this.group = group;
            this.invitedById = invitedBy;
            this.invitedId = invitedId;
        }

        public QuestingGroup getGroup() {
            return group;
        }

        public UUID getInvitedById() {
            return invitedById;
        }

        public UUID getInvitedId() {
            return invitedId;
        }

        public void setInteractionResult(Interaction<Void> result) {
            this.result = Objects.requireNonNull(result, "Invite result cannot be null!");
        }

        public Interaction<Void> getInteractionResult() {
            return result;
        }
    }

    public static class OnInviteAccept extends QuestingEvent {

        private final QuestingGroup group;
        private final GroupInvite invite;
        private final PlayerEntity invitee;

        private Interaction<Void> result = Interaction.success(null);

        public OnInviteAccept(World level, QuestingGroup group, GroupInvite invite, PlayerEntity invitee) {
            super(level);
            this.group = group;
            this.invite = invite;
            this.invitee = invitee;
        }

        public QuestingGroup getGroup() {
            return group;
        }

        public GroupInvite getInvite() {
            return invite;
        }

        public PlayerEntity getInvitee() {
            return invitee;
        }

        public void setInteractionResult(Interaction<Void> result) {
            this.result = Objects.requireNonNull(result, "Invite result cannot be null!");
        }

        public Interaction<Void> getInteractionResult() {
            return result;
        }
    }

    @Cancelable
    public static final class MobSpawnPreparingEvent extends QuestingEvent {

        private final QuestingGroup group;
        private final int minAmount;
        private final int maxAmount;

        private int toSpawn;

        public MobSpawnPreparingEvent(World level, QuestingGroup group, int minAmount, int maxAmount) {
            super(level);
            this.group = group;
            this.minAmount = minAmount;
            this.maxAmount = maxAmount;
        }

        public void setToSpawn(int toSpawn) {
            this.toSpawn = toSpawn;
        }

        public int getToSpawn() {
            return toSpawn;
        }

        public QuestingGroup getGroup() {
            return group;
        }

        public int getMinAmount() {
            return minAmount;
        }

        public int getMaxAmount() {
            return maxAmount;
        }
    }

    public static final class MobPostProcessingEvent extends QuestingEvent {

        private final QuestingGroup group;
        private final LivingEntity entity;
        private final QuestArea area;

        public MobPostProcessingEvent(World level, QuestingGroup group, LivingEntity entity, QuestArea area) {
            super(level);
            this.group = group;
            this.entity = entity;
            this.area = area;
        }

        public QuestingGroup getGroup() {
            return group;
        }

        public LivingEntity getEntity() {
            return entity;
        }

        public QuestArea getArea() {
            return area;
        }
    }
}
