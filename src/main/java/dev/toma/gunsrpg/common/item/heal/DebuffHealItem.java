package dev.toma.gunsrpg.common.item.heal;

import dev.toma.gunsrpg.api.common.attribute.IAttributeId;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.data.IDebuffs;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.attribute.ModifierFactory;
import dev.toma.gunsrpg.common.debuffs.DebuffType;
import dev.toma.gunsrpg.common.init.Debuffs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class DebuffHealItem extends AbstractHealItem<IPlayerData> {

    protected DebuffHealItem(Builder builder) {
        super(builder);
    }

    public static HealBuilder<IPlayerData, DebuffHealItem> define(String name) {
        return new Builder(name);
    }

    public static ITextComponent createLabel(ITextComponent debuff, int removeAmount) {
        ITextComponent valueLabel = new StringTextComponent(removeAmount + "%").withStyle(TextFormatting.GREEN);
        ITextComponent debuffLabel = debuff.plainCopy().withStyle(TextFormatting.UNDERLINE, TextFormatting.YELLOW);
        return new TranslationTextComponent("stat.debuff.remove", valueLabel, debuffLabel).withStyle(TextFormatting.DARK_GRAY);
    }

    public static void healPoison(IPlayerData data) {
        heal(data, Debuffs.POISON, Attribs.ANTIDOTE_EFFECT);
        AttributeAccessHealItem.applyProtection(data, ModifierFactory::createCalciumShotModifiers, () -> Debuffs.POISON);
    }

    public static void healInfection(IPlayerData data) {
        heal(data, Debuffs.INFECTION, Attribs.VACCINE_EFFECT);
        AttributeAccessHealItem.applyProtection(data, ModifierFactory::createVitaminModifiers, () -> Debuffs.INFECTION);
    }

    public static void healFracture(IPlayerData data) {
        heal(data, Debuffs.FRACTURE, Attribs.SPLINT_EFFECT);
        AttributeAccessHealItem.applyProtection(data, ModifierFactory::createPropitalModifiers, () -> Debuffs.FRACTURE);
    }

    public static void healBleed(IPlayerData data) {
        heal(data, Debuffs.BLEED, Attribs.BANDAGE_EFFECT);
        AttributeAccessHealItem.applyProtection(data, ModifierFactory::createHemostatModifiers, () -> Debuffs.BLEED);
    }

    private static void heal(IPlayerData data, DebuffType<?> type, IAttributeId attributeId) {
        IDebuffs debuffs = data.getDebuffControl();
        IAttributeProvider provider = data.getAttributes();
        debuffs.heal(type, provider.getAttribute(attributeId).intValue());
    }

    @Override
    public IPlayerData getTargetObject(World world, PlayerEntity user, IPlayerData data) {
        return data;
    }

    protected static class Builder extends HealBuilder<IPlayerData, DebuffHealItem> {

        protected Builder(String name) {
            super(name);
        }

        @Override
        public DebuffHealItem build() {
            return new DebuffHealItem(this);
        }
    }
}
