package dev.toma.gunsrpg.common.quests;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.data.IQuestingData;
import dev.toma.gunsrpg.api.common.event.QuestingEvent;
import dev.toma.gunsrpg.common.entity.projectile.AbstractProjectile;
import dev.toma.gunsrpg.common.init.WeaponDamageSource;
import dev.toma.gunsrpg.common.quests.quest.area.QuestArea;
import dev.toma.gunsrpg.common.quests.sharing.QuestingGroup;
import dev.toma.gunsrpg.common.quests.trigger.Trigger;
import dev.toma.gunsrpg.util.properties.Properties;
import dev.toma.gunsrpg.world.cap.QuestingDataProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ChatType;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingConversionEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(modid = GunsRPG.MODID)
public final class QuestEventHandler {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEntityKilled(LivingDeathEvent event) {
        DamageSource source = event.getSource();
        LivingEntity victim = event.getEntityLiving();
        Entity entity = source.getEntity();
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            Entity directEntity = source.getDirectEntity();
            ItemStack stack = source instanceof WeaponDamageSource ? ((WeaponDamageSource) source).getKillWeapon() : player.getMainHandItem();
            QuestingDataProvider.getData(player.level).ifPresent(questing -> questing.trigger(Trigger.ENTITY_KILLED, player, holder -> {
                holder.setProperty(QuestProperties.DAMAGE_SOURCE, source);
                holder.setProperty(QuestProperties.ENTITY, victim);
                holder.setProperty(QuestProperties.DIRECT_ENTITY, directEntity);
                holder.setProperty(QuestProperties.USED_ITEM, stack);
                if (directEntity instanceof AbstractProjectile) {
                    AbstractProjectile projectile = (AbstractProjectile) directEntity;
                    holder.setProperty(Properties.IS_HEADSHOT, projectile.getProperty(Properties.IS_HEADSHOT));
                }
            }));
        }
        if (!event.isCanceled() && victim instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) victim;
            QuestingDataProvider.getData(player.level).ifPresent(questing -> questing.trigger(Trigger.PLAYER_DIED, player, holder -> {}));
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;
        PlayerEntity player = event.player;
        QuestingDataProvider.getData(player.level).ifPresent(questing -> questing.trigger(Trigger.TICK, player, holder -> {}));
    }

    @SubscribeEvent
    public static void onLevelTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.START)
            return;
        World world = event.world;
        QuestingDataProvider.getData(world).ifPresent(IQuestingData::tickQuests);
    }

    @SubscribeEvent
    public static void onPlayerHurt(LivingAttackEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            if (!event.isCanceled() && event.getAmount() > 0) {
                QuestingDataProvider.getData(player.level).ifPresent(questing -> questing.trigger(Trigger.DAMAGE_TAKEN, player, holder -> holder.setProperty(QuestProperties.DAMAGE_SOURCE, event.getSource())));
            }
        }
        DamageSource source = event.getSource();
        Entity aggressor = source.getEntity();
        if (aggressor instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) aggressor;
            QuestingDataProvider.getData(player.level).ifPresent(questing -> questing.trigger(Trigger.DAMAGE_GIVEN, player, holder -> holder.setProperty(QuestProperties.DAMAGE_SOURCE, source)));
        }
    }

    @SubscribeEvent
    public static void cancelBlockPlacement(PlayerInteractEvent.RightClickBlock event) {
        cancelIfPlayerIsInQuestArea(event);
    }

    @SubscribeEvent
    public static void cancelAirInteraction(PlayerInteractEvent.RightClickItem event) {
        if (event.getItemStack().getItem() instanceof BucketItem) {
            cancelIfPlayerIsInQuestArea(event);
        }
    }

    @SubscribeEvent
    public static void cancelBlockDestruction(PlayerInteractEvent.LeftClickBlock event) {
        cancelIfPlayerIsInQuestArea(event);
    }

    @SubscribeEvent
    public static void cancelEntityConversion(LivingConversionEvent.Pre event) {
        LivingEntity entity = event.getEntityLiving();
        if (entity instanceof AbstractPiglinEntity) {
            AbstractPiglinEntity piglinEntity = (AbstractPiglinEntity) entity;
            piglinEntity.setImmuneToZombification(true);
            event.setCanceled(true);
            event.setConversionTimer(Integer.MIN_VALUE);
        }
    }

    @SubscribeEvent
    public static void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        QuestingDataProvider.getData(event.getPlayer().level).ifPresent(questing -> questing.handlePlayerLogIn(event.getPlayer()));
    }

    @SubscribeEvent
    public static void playerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        QuestingDataProvider.getData(event.getPlayer().level).ifPresent(questing -> questing.handlePlayerLogOut(event.getPlayer()));
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void adjustSpawnWave(QuestingEvent.MobSpawnPreparingEvent event) {
        if (!GunsRPG.config.quests.adjustSpawnWavesForGroup)
            return;
        QuestingGroup group = event.getGroup();
        int count = group.getMemberCount();
        // 50% increase from base value per member
        int newMin = MathHelper.ceil(event.getMinAmount() * (1.0F + 0.5F * (count - 1)));
        int newMax = MathHelper.ceil(event.getMaxAmount() * (1.0F + 0.5F * (count - 1)));
        Random random = event.getLevel().getRandom();
        event.setToSpawn(newMin + random.nextInt(1 + newMax - newMin));
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void adjustSpawnedMob(QuestingEvent.MobPostProcessingEvent event) {
        if (!GunsRPG.config.quests.buffSpawnedMobsForGroup)
            return;
        QuestingGroup group = event.getGroup();
        int count = group.getMemberCount();
        LivingEntity entity = event.getEntity();
        if (count >= 2) {
            int amplifier = MathHelper.clamp(count / 2 - 1, 0, 4);
            entity.addEffect(new EffectInstance(Effects.DAMAGE_BOOST, Integer.MAX_VALUE, amplifier, true, false));
        }
        if (count >= 3) {
            int amplifier = MathHelper.clamp(count / 3 - 1, 0, 2);
            entity.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, Integer.MAX_VALUE, amplifier, true, false));
        }
        if (count >= 5) {
            int amplifier = MathHelper.clamp(count / 5 - 1, 0, 2);
            entity.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, Integer.MAX_VALUE, amplifier, true, false));
        }
        if (count >= 8) {
            int amplifier = MathHelper.clamp(count / 8 - 1, 0, 2);
            entity.addEffect(new EffectInstance(Effects.REGENERATION, Integer.MAX_VALUE, amplifier, true, false));
        }
    }

    private static void cancelIfPlayerIsInQuestArea(PlayerInteractEvent event) {
        boolean areaInteractionDisabled = GunsRPG.config.quests.disableQuestAreaInteractions;
        if (!areaInteractionDisabled) {
            return;
        }
        PlayerEntity player = event.getPlayer();
        BlockPos pos = event.getPos();
        World world = event.getWorld();
        QuestingDataProvider.getData(world).ifPresent(questing -> {
            QuestArea activeArea = questing.getActiveQuestAreas()
                    .filter(area -> area.isActiveArea() && (area.isInArea(player) || area.isInArea(pos.getX(), pos.getZ())))
                    .findFirst()
                    .orElse(null);
            if (activeArea != null) {
                event.setCanceled(true);
                if (!player.level.isClientSide) {
                    ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
                    serverPlayer.sendMessage(QuestArea.INTERACTION_DISABLED, ChatType.GAME_INFO, Util.NIL_UUID);
                }
            }
        });
    }
}
