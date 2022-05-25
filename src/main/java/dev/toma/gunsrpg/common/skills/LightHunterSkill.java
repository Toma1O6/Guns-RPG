package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.api.common.attribute.*;
import dev.toma.gunsrpg.api.common.skill.IDescriptionProvider;
import dev.toma.gunsrpg.api.common.skill.ITickableSkill;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.attribute.AttributeModifier;
import dev.toma.gunsrpg.common.attribute.AttributeOps;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.skills.core.DescriptionContainer;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.helper.PlayerHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.util.text.ITextComponent;

public class LightHunterSkill extends SimpleSkill implements ITickableSkill, IDescriptionProvider {

    private static final IAttributeModifier MOVEMENT_SPEED_MODIFIER = new AttributeModifier("9942EE39-CCD1-4F37-88E4-98711C2CF3EB", AttributeOps.MUL, 0.85);
    private static final IAttributeModifier FALLING_MODIFIER = new AttributeModifier("E354F3D8-F665-48C4-9F6D-B1C7714E2337", AttributeOps.MUL, 0.85);
    private static final IAttributeModifier ARROW_DAMAGE_MODIFIER = new AttributeModifier("E509F8AA-1E9C-42F7-9B20-660746968478", AttributeOps.MUL, 1.2);
    private final DescriptionContainer container;
    private boolean validityState;

    public LightHunterSkill(SkillType<?> type) {
        super(type);
        this.container = new DescriptionContainer(type);
        this.container.addProperty("info");
        this.container.addProperty("speed", IValueFormatter.INV_PERCENT.formatAttributeValue(MOVEMENT_SPEED_MODIFIER.getModifierValue()));
        this.container.addProperty("damage", IValueFormatter.BASE_PERCENT.formatAttributeValue(ARROW_DAMAGE_MODIFIER.getModifierValue()));
        this.container.addProperty("fall", IValueFormatter.INV_PERCENT.formatAttributeValue(FALLING_MODIFIER.getModifierValue()));
    }

    @Override
    public ITextComponent[] supplyDescription(int desiredLineCount) {
        return container.getLines();
    }

    @Override
    public boolean canApply(PlayerEntity user) {
        return true;
    }

    @Override
    public void onUpdate(PlayerEntity player) {
        PlayerData.get(player).ifPresent(data -> {
            IAttributeProvider provider = data.getAttributes();
            boolean lastState = validityState;
            validityState = PlayerHelper.hasFullArmorSetOfMaterial(player, ArmorMaterial.LEATHER);
            if (lastState != validityState) {
                toggleModifier(provider, Attribs.MOVEMENT_SPEED, MOVEMENT_SPEED_MODIFIER);
                toggleModifier(provider, Attribs.FALL_RESISTANCE, FALLING_MODIFIER);
                toggleModifier(provider, Attribs.BOW_DAMAGE, ARROW_DAMAGE_MODIFIER);
            }
        });
    }

    private void toggleModifier(IAttributeProvider provider, IAttributeId attributeId, IAttributeModifier modifier) {
        IAttribute attribute = provider.getAttribute(attributeId);
        if (validityState) {
            attribute.addModifier(modifier);
        } else {
            attribute.removeModifier(modifier);
        }
    }
}
