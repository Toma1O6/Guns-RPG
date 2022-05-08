package dev.toma.gunsrpg.common.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import java.util.Arrays;

public class ZombieKnightEntity extends ZombieEntity {

    public ZombieKnightEntity(EntityType<? extends ZombieKnightEntity> type, World world) {
        super(type, world);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MonsterEntity.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.FOLLOW_RANGE, 48.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.345D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.ARMOR, 10.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(0, new SwimGoal(this));
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance instance) {
        setItemSlot(EquipmentSlotType.HEAD, new ItemStack(Items.NETHERITE_HELMET));
        setItemSlot(EquipmentSlotType.CHEST, new ItemStack(Items.NETHERITE_CHESTPLATE));
        setItemSlot(EquipmentSlotType.LEGS, new ItemStack(Items.NETHERITE_LEGGINGS));
        setItemSlot(EquipmentSlotType.FEET, new ItemStack(Items.NETHERITE_BOOTS));
        setItemSlot(isLeftHanded() ? EquipmentSlotType.OFFHAND : EquipmentSlotType.MAINHAND, new ItemStack(Items.NETHERITE_SWORD));
        Arrays.fill(armorDropChances, 0.0F);
        Arrays.fill(handDropChances, 0.0F);
    }

    @Override
    protected boolean convertsInWater() {
        return false;
    }
}
