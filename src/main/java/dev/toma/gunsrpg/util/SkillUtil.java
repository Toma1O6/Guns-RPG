package dev.toma.gunsrpg.util;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.ISkill;
import dev.toma.gunsrpg.api.common.data.ISkills;
import dev.toma.gunsrpg.api.common.skill.ISkillHierarchy;
import dev.toma.gunsrpg.api.common.skill.ISkillProperties;
import dev.toma.gunsrpg.common.skills.core.DisplayData;
import dev.toma.gunsrpg.common.skills.core.DisplayType;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

@SuppressWarnings("unchecked")
public class SkillUtil {

    public static <S extends ISkill> S getOverride(S skill, ISkills skills) {
        SkillType<S> type = (SkillType<S>) skill.getType();
        ISkillHierarchy<S> hierarchy = type.getHierarchy();
        SkillType<S> override = hierarchy.getOverride();
        if (skills.hasSkill(override)) {
            return getOverride(skills.getSkill(override), skills);
        } else {
            return skill;
        }
    }

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
        ISkillProperties properties = type.getProperties();
        description[total - 2] = new TranslationTextComponent("skill.requirement.level", properties.getRequiredLevel());
        description[total - 1] = new TranslationTextComponent("skill.requirement.price", properties.getPrice());
        return description;
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
}
