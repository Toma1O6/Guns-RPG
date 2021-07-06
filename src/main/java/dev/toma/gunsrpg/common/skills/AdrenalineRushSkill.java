package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.asm.Hooks;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.common.skills.interfaces.TickableSkill;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;

import java.util.UUID;

public class AdrenalineRushSkill extends BasicSkill implements TickableSkill {

    public static final UUID SKILL_MODIFIER = UUID.fromString("A6FD91CA-A18E-401A-B362-D908BC717C13");
    private static final AttributeModifier[] modifiers = {
            new AttributeModifier(SKILL_MODIFIER, "Skill modifier", 0.15, AttributeModifier.Operation.MULTIPLY_TOTAL),
            new AttributeModifier(SKILL_MODIFIER, "Skill modifier", 0.30, AttributeModifier.Operation.MULTIPLY_TOTAL),
            new AttributeModifier(SKILL_MODIFIER, "Skill modifier", 0.50, AttributeModifier.Operation.MULTIPLY_TOTAL)
    };
    private final int level;
    private final float reloadMultiplier;

    private boolean prevTrigger;

    public AdrenalineRushSkill(SkillType<?> type, int level, float reloadMultiplier) {
        super(type);
        this.level = level;
        this.reloadMultiplier = reloadMultiplier;
    }

    @Override
    public void onUpdate(PlayerEntity player) {
        boolean currentlyTriggered = apply(player);
        if(currentlyTriggered != prevTrigger) {
            Hooks.dispatchApplyAttributesFromItemStack(player, player.getMainHandItem(), EquipmentSlotType.MAINHAND);
        }
        this.prevTrigger = currentlyTriggered;
    }

    public AttributeModifier getAttackSpeedBoost() {
        return modifiers[level];
    }

    public float getReloadMultiplier() {
        return reloadMultiplier;
    }

    @Override
    public boolean apply(PlayerEntity user) {
        return user.getHealth() <= 8;
    }
}
