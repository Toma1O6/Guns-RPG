package dev.toma.gunsrpg.common.quests;

import dev.toma.gunsrpg.util.properties.PropertyKey;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public final class QuestProperties {

    public static final PropertyKey<PlayerEntity> PLAYER = PropertyKey.newKey("player");
    public static final PropertyKey<ItemStack> USED_ITEM = PropertyKey.newKey("used_item", ItemStack.EMPTY);
    public static final PropertyKey<LivingEntity> ENTITY = PropertyKey.newKey("entity");
    public static final PropertyKey<Entity> DIRECT_ENTITY = PropertyKey.newKey("direct_entity");
    public static final PropertyKey<Float> HEALTH_STATUS = PropertyKey.newKey("health_status", 0.0F);
    public static final PropertyKey<Float> FOOD_STATUS = PropertyKey.newKey("food_status", 0.0F);
    public static final PropertyKey<DamageSource> DAMAGE_SOURCE = PropertyKey.newKey("damage_source", DamageSource.GENERIC);
}
