package dev.toma.gunsrpg.util;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.ISkill;
import dev.toma.gunsrpg.api.common.data.ISkills;
import dev.toma.gunsrpg.api.common.skill.IDescriptionProvider;
import dev.toma.gunsrpg.api.common.skill.ISkillHierarchy;
import dev.toma.gunsrpg.api.common.skill.ISkillProperties;
import dev.toma.gunsrpg.common.skills.core.DisplayData;
import dev.toma.gunsrpg.common.skills.core.DisplayType;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.function.Function;

public class SkillUtil {

    public static <S extends ISkill> S getTopHierarchySkill(SkillType<S> head, ISkills skills) {
        S value = skills.getSkill(head);
        if (value == null)
            return null;
        ISkillHierarchy<S> hierarchy = head.getHierarchy();
        SkillType<S> override = hierarchy.getOverride();
        if (override != null && skills.hasSkill(override)) {
            return getTopHierarchySkill(override, skills);
        } else {
            return value;
        }
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

        public static ITextComponent getDefaultTitle(SkillType<?> type) {
            return new TranslationTextComponent("skill." + ModUtils.convertToLocalization(type.getRegistryName()) + ".title");
        }

        public static ITextComponent[] getDefaultDescription(int lines, SkillType<?> type) {
            int total = lines + 2;
            ISkill skill = type.getDataInstance();
            if (skill instanceof IDescriptionProvider) {
                IDescriptionProvider provider = (IDescriptionProvider) skill;
                return expandWithRequirementData(type, provider.supplyDescription(lines));
            }
            ITextComponent[] description = new ITextComponent[total];
            prepareEmptyDescriptionLines(type, lines, description);
            appendRequirementData(type, description);
            return description;
        }

        public static void prepareEmptyDescriptionLines(SkillType<?> type, int lines, ITextComponent[] description) {
            String rawString = String.format("skill.%s.description.", ModUtils.convertToLocalization(type.getRegistryName()));
            for (int i = 0; i < lines; i++) {
                description[i] = new TranslationTextComponent(rawString + i);
            }
        }

        public static ITextComponent[] expandWithRequirementData(SkillType<?> type, ITextComponent[] desc) {
            ITextComponent[] out = new ITextComponent[desc.length + 2];
            System.arraycopy(desc, 0, out, 0, desc.length);
            appendRequirementData(type, out);
            return out;
        }

        public static void appendRequirementData(SkillType<?> type, ITextComponent[] array) {
            int l = array.length;
            ISkillProperties properties = type.getProperties();
            array[l - 2] = new TranslationTextComponent("skill.requirement.level", properties.getRequiredLevel());
            array[l - 1] = new TranslationTextComponent("skill.requirement.price", properties.getPrice());
        }

        public static <T> ITextComponent[] localizeSingleAttribute(SkillType<?> type, T modifier, String property, Function<T, Integer> toIntFunc) {
            ITextComponent[] components = new ITextComponent[3];
            components[0] = translation(type, property, toIntFunc.apply(modifier));
            appendRequirementData(type, components);
            return components;
        }

        public static ITextComponent translation(SkillType<?> type, String name, Object... data) {
            return new TranslationTextComponent("skill." + ModUtils.convertToLocalization(type.getRegistryName()) + ".description." + name, data);
        }
    }
}
