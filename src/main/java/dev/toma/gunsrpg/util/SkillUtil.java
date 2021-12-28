package dev.toma.gunsrpg.util;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.ISkill;
import dev.toma.gunsrpg.api.common.skill.ISkillProperties;
import dev.toma.gunsrpg.common.attribute.IAttributeModifier;
import dev.toma.gunsrpg.common.attribute.Modifiers;
import dev.toma.gunsrpg.common.skills.AttributeSkill;
import dev.toma.gunsrpg.common.skills.core.DisplayData;
import dev.toma.gunsrpg.common.skills.core.DisplayType;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public class SkillUtil {

    public static ITextComponent getDefaultTitle(SkillType<?> type) {
        return new TranslationTextComponent("skill." + ModUtils.convertToLocalization(type.getRegistryName()) + ".title");
    }

    public static ITextComponent[] getDefaultDescription(int lines, SkillType<?> type) {
        int total = lines + 2;
        ITextComponent[] description = new ITextComponent[total];
        String rawString = String.format("skill.%s.description.", ModUtils.convertToLocalization(type.getRegistryName()));
        for (int i = 0; i < lines; i++) {
            description[i] = new TranslationTextComponent(rawString + i);
        }
        appendRequirementData(type, description);
        return description;
    }

    public static <S extends ISkill> ITextComponent[] localizeSkill(SkillType<S> type, Function<SkillType<S>, ITextComponent[]> function) {
        ITextComponent[] data = function.apply(type);
        ITextComponent[] array = new ITextComponent[data.length + 2];
        System.arraycopy(data, 0, array, 0, data.length);
        appendRequirementData(type, array);
        return array;
    }

    public static void appendRequirementData(SkillType<?> type, ITextComponent[] array) {
        int l = array.length;
        ISkillProperties properties = type.getProperties();
        array[l - 2] = new TranslationTextComponent("skill.requirement.level", properties.getRequiredLevel());
        array[l - 1] = new TranslationTextComponent("skill.requirement.price", properties.getPrice());
    }

    public static DisplayData getDefaultDisplayData(SkillType<?> type) {
        ResourceLocation name = type.getRegistryName();
        String namespace = name.getNamespace();
        String path = name.getPath();
        return DisplayData.create(
                DisplayType.ICON,
                new ResourceLocation(namespace, "textures/icons/" + path + ".png")
        );
    }

    public static ResourceLocation moddedIcon(String iconPath) {
        return GunsRPG.makeResource("textures/icons/" + iconPath + ".png");
    }

    public static class Localizations {

        public static ITextComponent[] localizeAcrobaticsI(int lines, SkillType<AttributeSkill> type) {
            return localizeAcrobatics(type, Modifiers.ACROBATICS_FALL_I, Modifiers.ACROBATICS_EXPLOSION_I);
        }

        public static ITextComponent[] localizeAcrobaticsII(int lines, SkillType<AttributeSkill> type) {
            return localizeAcrobatics(type, Modifiers.ACROBATICS_FALL_II, Modifiers.ACROBATICS_EXPLOSION_II);
        }

        public static ITextComponent[] localizeAcrobaticsIII(int lines, SkillType<AttributeSkill> type) {
            return localizeAcrobatics(type, Modifiers.ACROBATICS_FALL_III, Modifiers.ACROBATICS_EXPLOSION_III);
        }

        public static ITextComponent[] localizeResistI(int lines, SkillType<AttributeSkill> type) {
            return localizeResist(type, Modifiers.DEBUFF_RESIST_I, Modifiers.DEBUFF_DELAY_I);
        }

        public static ITextComponent[] localizeResistII(int lines, SkillType<AttributeSkill> type) {
            return localizeResist(type, Modifiers.DEBUFF_RESIST_II, Modifiers.DEBUFF_DELAY_II);
        }

        public static ITextComponent[] localizeResistIII(int lines, SkillType<AttributeSkill> type) {
            return localizeResist(type, Modifiers.DEBUFF_RESIST_III, Modifiers.DEBUFF_DELAY_III);
        }

        private static ITextComponent[] localizeAcrobatics(SkillType<?> type, IAttributeModifier fall, IAttributeModifier explosion) {
            ITextComponent[] components = new ITextComponent[5];
            components[0] = translation(type, "info");
            components[1] = translation(type, "fall", percent(fall));
            components[2] = translation(type, "explosion", percent(explosion));
            appendRequirementData(type, components);
            return components;
        }

        private static ITextComponent[] localizeResist(SkillType<?> type, IAttributeModifier resist, IAttributeModifier delay) {
            ITextComponent[] components = new ITextComponent[4];
            components[0] = translation(type, "chance", percent(resist));
            components[1] = translation(type, "delay", seconds(delay));
            appendRequirementData(type, components);
            return components;
        }

        private static ITextComponent translation(SkillType<?> type, String name, IAttributeModifier... modifiers) {
            return translation(type, name, Arrays.stream(modifiers).map(Localizations::seconds).toArray(Object[]::new));
        }

        private static ITextComponent translation(SkillType<?> type, String name, Object... data) {
            return new TranslationTextComponent("skill." + ModUtils.convertToLocalization(type.getRegistryName()) + ".description." + name, data);
        }

        private static int percent(IAttributeModifier modifier) {
            return (int) (modifier.getModifierValue() * 100);
        }

        private static int seconds(IAttributeModifier modifier) {
            return (int) (modifier.getModifierValue() / 20);
        }
    }
}
