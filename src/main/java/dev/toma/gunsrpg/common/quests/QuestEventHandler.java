package dev.toma.gunsrpg.common.quests;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.data.IQuests;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.entity.projectile.AbstractProjectile;
import dev.toma.gunsrpg.common.init.GunDamageSource;
import dev.toma.gunsrpg.common.quests.trigger.Trigger;
import dev.toma.gunsrpg.util.properties.IPropertyHolder;
import dev.toma.gunsrpg.util.properties.Properties;
import dev.toma.gunsrpg.util.properties.PropertyContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GunsRPG.MODID)
public final class QuestEventHandler {

    @SubscribeEvent
    public static void onEntityKilled(LivingDeathEvent event) {
        DamageSource source = event.getSource();
        LivingEntity victim = event.getEntityLiving();
        Entity entity = source.getEntity();
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            Entity directEntity = source.getDirectEntity();
            ItemStack stack = source instanceof GunDamageSource ? ((GunDamageSource) source).getStacc() : player.getMainHandItem();
            PlayerData.get(player).ifPresent(data -> {
                IQuests quests = data.getQuests();
                quests.getActiveQuest().ifPresent(quest -> {
                    IPropertyHolder holder = PropertyContext.create();
                    holder.setProperty(QuestProperties.DAMAGE_SOURCE, source);
                    holder.setProperty(QuestProperties.ENTITY, victim);
                    holder.setProperty(QuestProperties.DIRECT_ENTITY, directEntity);
                    holder.setProperty(QuestProperties.PLAYER, player);
                    holder.setProperty(QuestProperties.USED_ITEM, stack);
                    if (directEntity instanceof AbstractProjectile) {
                        AbstractProjectile projectile = (AbstractProjectile) directEntity;
                        holder.setProperty(Properties.IS_HEADSHOT, projectile.getProperty(Properties.IS_HEADSHOT));
                    }
                    quest.trigger(Trigger.ENTITY_KILLED, holder);
                });
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;
        PlayerData.get(event.player).ifPresent(data -> {
            IQuests quests = data.getQuests();
            quests.getActiveQuest().ifPresent(quest -> {
                IPropertyHolder holder = PropertyContext.create();
                holder.setProperty(QuestProperties.PLAYER, event.player);
                quest.trigger(Trigger.TICK, holder);
            });
        });

    }

    @SubscribeEvent
    public static void onPlayerHurt(LivingAttackEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            if (!event.isCanceled() && event.getAmount() > 0) {
                PlayerData.get(player).ifPresent(data -> {
                    IQuests quests = data.getQuests();
                    quests.getActiveQuest().ifPresent(quest -> {
                        IPropertyHolder holder = PropertyContext.create();
                        holder.setProperty(QuestProperties.PLAYER, player);
                        holder.setProperty(QuestProperties.DAMAGE_SOURCE, event.getSource());
                        quest.trigger(Trigger.DAMAGE_TAKEN, holder);
                    });
                });
            }
        }
        DamageSource source = event.getSource();
        Entity aggressor = source.getEntity();
        if (aggressor instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) aggressor;
            PlayerData.get(player).ifPresent(data -> {
                IQuests quests = data.getQuests();
                quests.getActiveQuest().ifPresent(quest -> {
                    IPropertyHolder holder = PropertyContext.create();
                    holder.setProperty(QuestProperties.PLAYER, player);
                    holder.setProperty(QuestProperties.DAMAGE_SOURCE, source);
                    quest.trigger(Trigger.DAMAGE_GIVEN, holder);
                });
            });
        }
    }
}
