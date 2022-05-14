package dev.toma.gunsrpg.common.entity;

import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.IQuests;
import dev.toma.gunsrpg.api.common.data.ITraderStandings;
import dev.toma.gunsrpg.api.common.data.ITraderStatus;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.quests.QuestProperties;
import dev.toma.gunsrpg.common.quests.quest.Quest;
import dev.toma.gunsrpg.common.quests.trigger.Trigger;
import dev.toma.gunsrpg.util.IIntervalProvider;
import dev.toma.gunsrpg.util.Interval;
import dev.toma.gunsrpg.util.properties.IPropertyHolder;
import dev.toma.gunsrpg.util.properties.PropertyContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public class MayorEntity extends CreatureEntity {

    public static final IIntervalProvider REFRESH_LIMIT = Interval.hours(1);
    private final Map<UUID, ListedQuests> playerQuests = Collections.emptyMap();
    private long refreshAtWorldTime;

    public MayorEntity(EntityType<? extends MayorEntity> type, World world) {
        super(type, world);
        setPersistenceRequired();
    }

    @Override
    public void tick() {
        super.tick();
        long time = level.getDayTime();
        if (time >= refreshAtWorldTime) {
            refreshAtWorldTime = time + REFRESH_LIMIT.getTicks();
            playerQuests.clear();
        }
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new LookAtGoal(this, LivingEntity.class, 8.0F));
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(Entity p_82167_1_) {
    }

    @Override
    public void setLeashedTo(Entity entity, boolean bool) {
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        Entity entity = source.getEntity();
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            PlayerData.get(player).ifPresent(data -> {
                ITraderStandings standings = data.getQuests().getTraderStandings();
                UUID uuid = this.getUUID();
                ITraderStatus status = standings.getStatusWithTrader(uuid);
                if (status == null) {
                    standings.registerNew(uuid);
                    status = standings.getStatusWithTrader(uuid);
                }
                status.onTraderAttacked();
            });
        } else {
            super.hurt(source, amount);
        }
        return false;
    }

    @Override
    protected ActionResultType mobInteract(PlayerEntity player, Hand hand) {
        if (player.isCrouching()) {
            IPlayerData data = PlayerData.getUnsafe(player);
            IQuests quests = data.getQuests();
            quests.getActiveQuest().ifPresent(quest -> {
                ItemStack stack = player.getItemInHand(hand);
                IPropertyHolder holder = PropertyContext.create();
                holder.setProperty(QuestProperties.PLAYER, player);
                holder.setProperty(QuestProperties.USED_ITEM, stack);
                quest.trigger(Trigger.ITEM_HANDOVER, holder);
            });
        } else {
            if (level.isClientSide) {
                openQuestScreen();
            } else {
                UUID uuid = player.getUUID();
                ListedQuests quests = playerQuests.get(uuid);
                if (!playerQuests.containsKey(uuid)) {
                    quests = ListedQuests.generate();
                    playerQuests.put(uuid, quests);
                }
                // TODO send data payload with quest information
            }
        }
        return ActionResultType.sidedSuccess(level.isClientSide);
    }

    @OnlyIn(Dist.CLIENT)
    private void openQuestScreen() {
        Minecraft minecraft = Minecraft.getInstance();
        // TODO
    }

    private static final class ListedQuests {

        public static final int QUEST_COUNT = 5;
        private final Quest[] quests;

        public ListedQuests(Quest[] quests) {
            this.quests = quests;
        }

        public static ListedQuests generate() {
            return null;
        }

        public Quest[] getQuests() {
            return quests;
        }
    }
}
