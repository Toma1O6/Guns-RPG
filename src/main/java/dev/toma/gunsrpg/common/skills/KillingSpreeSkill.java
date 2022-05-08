package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.api.common.attribute.IAttribute;
import dev.toma.gunsrpg.api.common.attribute.IAttributeId;
import dev.toma.gunsrpg.api.common.attribute.IAttributeModifier;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.common.attribute.AttributeOps;
import dev.toma.gunsrpg.common.attribute.ExpiringModifier;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import net.minecraft.util.math.MathHelper;

import java.util.UUID;

public class KillingSpreeSkill extends SimpleSkill {

    private static final int TIME_LIMIT = 100;
    private static final int STACK_LIMIT = 3;
    private static final float SCALE = 0.1F;
    private final UUID modifierId;
    private int currentSpree;

    public KillingSpreeSkill(SkillType<? extends KillingSpreeSkill> type, UUID modifierId) {
        super(type);
        this.modifierId = modifierId;
    }

    public void applyBonus(IAttributeProvider provider, IAttributeId attributeId) {
        this.currentSpree = MathHelper.clamp(currentSpree + 1, 0, STACK_LIMIT);
        IAttribute attribute = provider.getAttribute(attributeId);
        if (!attribute.hasModifier(modifierId)) {
            currentSpree = 1;
        }
        float value = currentSpree * SCALE;
        IAttributeModifier modifier = new ExpiringModifier(modifierId, AttributeOps.MULB, value, TIME_LIMIT);
        attribute.addModifier(modifier);
    }
}
