package dev.toma.gunsrpg.api.common.event;

import dev.toma.gunsrpg.common.quests.sharing.QuestingGroup;
import dev.toma.gunsrpg.util.object.Interaction;
import net.minecraft.world.World;
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
}
