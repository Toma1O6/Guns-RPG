package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.asm.Hooks;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.common.skills.interfaces.TickableSkill;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class AdrenalineRushSkill extends BasicSkill implements TickableSkill {

    private static final float[] MODIFIERS = new float[] {0.85F, 0.70F, 0.50F};
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

    public float getAttackSpeedBoost() {
        return MODIFIERS[level];
    }

    public float getReloadMultiplier() {
        return reloadMultiplier;
    }

    @Override
    public boolean apply(PlayerEntity user) {
        return user.getHealth() <= 8;
    }
}
