package dev.toma.gunsrpg.common.quests;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.entity.projectile.AbstractProjectile;
import dev.toma.gunsrpg.common.init.GunDamageSource;
import dev.toma.gunsrpg.common.quests.trigger.Triggers;
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
import net.minecraftforge.event.entity.living.LivingHurtEvent;
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
            Triggers.ENTITY_KILLED.trigger(holder);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;
        IPropertyHolder holder = PropertyContext.create();
        holder.setProperty(QuestProperties.PLAYER, event.player);
        Triggers.TICK.trigger(holder);
    }

    @SubscribeEvent
    public static void onPlayerHurt(LivingAttackEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            if (!event.isCanceled() && event.getAmount() > 0) {
                IPropertyHolder holder = PropertyContext.create();
                holder.setProperty(QuestProperties.PLAYER, player);
                holder.setProperty(QuestProperties.DAMAGE_SOURCE, event.getSource());
                Triggers.DAMAGE_TAKEN.trigger(holder);
            }
        }
    }
}
