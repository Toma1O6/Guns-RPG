package dev.toma.gunsrpg.util;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.data.ISkillProvider;
import dev.toma.gunsrpg.api.common.skill.IDescriptionProvider;
import dev.toma.gunsrpg.api.common.skill.ISkill;
import dev.toma.gunsrpg.api.common.skill.ISkillHierarchy;
import dev.toma.gunsrpg.common.skills.core.DisplayData;
import dev.toma.gunsrpg.common.skills.core.DisplayType;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;

public class SkillUtil {

    public static <S extends ISkill> S getTopHierarchySkill(SkillType<S> head, ISkillProvider provider) {
        S value = provider.getSkill(head);
        if (value == null)
            return null;
        ISkillHierarchy<S> hierarchy = head.getHierarchy();
        SkillType<S> override = hierarchy.getOverride();
        if (override != null && provider.hasSkill(override)) {
            return getTopHierarchySkill(override, provider);
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

        public static ITextComponent makeReadable(ResourceLocation location) {
            String[] str = location.toString().split("[.:]");
            String in = str[str.length - 1];
            String[] words = Arrays.stream(in.split("_")).map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase()).toArray(String[]::new);
            return new StringTextComponent(String.join(" ", words));
        }

        public static ITextComponent getDefaultTitle(SkillType<?> type) {
            return new TranslationTextComponent("skill." + ModUtils.convertToLocalization(type.getRegistryName()) + ".title");
        }

        public static ITextComponent[] getDefaultDescription(int lines, SkillType<?> type) {
            ISkill skill = type.getDataInstance();
            if (skill instanceof IDescriptionProvider) {
                IDescriptionProvider provider = (IDescriptionProvider) skill;
                return provider.supplyDescription(lines);
            }
            ITextComponent[] description = new ITextComponent[lines];
            prepareEmptyDescriptionLines(type, lines, description);
            return description;
        }

        public static ITextComponent[] generateAndMerge(int lines, SkillType<?> type, ITextComponent[] components) {
            ITextComponent[] description = new ITextComponent[lines + components.length];
            prepareEmptyDescriptionLines(type, lines, description);
            System.arraycopy(components, 0, description, lines, components.length);
            return description;
        }

        public static ITextComponent[] generateSimpleDescription(int lines, SkillType<?> type) {
            ITextComponent[] components = new ITextComponent[lines];
            prepareEmptyDescriptionLines(type, lines, components);
            return components;
        }

        public static void prepareEmptyDescriptionLines(SkillType<?> type, int lines, ITextComponent[] description) {
            String rawString = String.format("skill.%s.description.", ModUtils.convertToLocalization(type.getRegistryName()));
            for (int i = 0; i < lines; i++) {
                description[i] = new TranslationTextComponent(rawString + i);
            }
        }

        public static ITextComponent translation(SkillType<?> type, String name, Object... data) {
            return new TranslationTextComponent("skill." + ModUtils.convertToLocalization(type.getRegistryName()) + ".description." + name, data);
        }
    }
}
