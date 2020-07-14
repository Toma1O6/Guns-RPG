package dev.toma.gunsrpg.common.skills.core;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.skills.criteria.CriteriaTypes;
import dev.toma.gunsrpg.common.skills.criteria.UnlockCriteria;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class SkillType<S extends ISkill> extends IForgeRegistryEntry.Impl<SkillType<?>> {

    public final int levelRequirement;
    public final int price;
    public final ResourceLocation icon;
    public final SkillCategory category;
    private final UnlockCriteria criteria;
    private final IFactory<S> instanceFactory;
    private final ITextComponent textComponent;
    private final ITextComponent[] description;
    private final Supplier<SkillType<?>> skillOverride;
    private Supplier<List<SkillType<?>>> uninitializedChildList;
    private List<SkillType<?>> childList;

    private SkillType(Builder<S> builder) {
        this.instanceFactory = builder.factory;
        this.levelRequirement = builder.levelRequirement;
        this.price = builder.skillPointPrice;
        this.uninitializedChildList = builder.childs;
        this.skillOverride = builder.skillOverride;
        this.category = builder.category;
        this.criteria = builder.criteria;
        this.icon = builder.icon != null ? builder.icon : GunsRPG.makeResource(getRegistryName().getResourcePath());
        this.textComponent = builder.textComponent != null ? builder.textComponent : new TextComponentTranslation("skill." + getRegistryName().getResourcePath());
        if (builder.descriptionLines > 0) {
            description = new ITextComponent[builder.descriptionLines];
            for (int i = 0; i < builder.descriptionLines; i++) {
                description[i] = new TextComponentTranslation(this.getRegistryName().getResourcePath() + ".description.line_" + (i + 1));
            }
        } else description = new ITextComponent[0];
    }

    public List<SkillType<?>> getChilds() {
        if (childList == null) {
            childList = uninitializedChildList != null ? uninitializedChildList.get() : Collections.emptyList();
            uninitializedChildList = null;
        }
        return childList;
    }

    public boolean isOverriden() {
        return skillOverride != null;
    }

    public SkillType<?> getOverride() {
        return skillOverride.get();
    }

    public String getDisplayName() {
        return textComponent.getFormattedText();
    }

    public ITextComponent[] getDescription() {
        return description;
    }

    public UnlockCriteria getCriteria() {
        return criteria;
    }

    public boolean areTypesEqual(ISkill skill) {
        return getRegistryName().equals(skill.getType().getRegistryName());
    }

    public S instantiate() {
        return instanceFactory.create(this);
    }

    public interface IFactory<S extends ISkill> {

        S create(SkillType<S> type);
    }

    public static class Builder<S extends ISkill> {

        private final IFactory<S> factory;
        private int levelRequirement;
        private int skillPointPrice;
        private Supplier<List<SkillType<?>>> childs;
        private Supplier<SkillType<?>> skillOverride;
        private SkillCategory category;
        private ResourceLocation registryName;
        private ResourceLocation icon;
        private ITextComponent textComponent;
        private UnlockCriteria criteria = CriteriaTypes.getDefaultCriteria();
        private int descriptionLines = 1;

        private Builder(IFactory<S> factory) {
            this.factory = factory;
        }

        public static <S extends ISkill> Builder<S> newBuilder(IFactory<S> factory) {
            return new Builder<>(factory);
        }

        public Builder<S> requiredLevel(int levelRequirement) {
            this.levelRequirement = levelRequirement;
            return this;
        }

        public Builder<S> price(int price) {
            this.skillPointPrice = price;
            return this;
        }

        public Builder<S> childs(Supplier<List<SkillType<?>>> childs) {
            this.childs = childs;
            return this;
        }

        public Builder<S> setOverride(Supplier<SkillType<?>> skillOverride) {
            this.skillOverride = skillOverride;
            return this;
        }

        public Builder<S> childAndOverride(Supplier<SkillType<?>> childOverride) {
            this.childs(() -> ModUtils.newList(childOverride.get()));
            return this.setOverride(childOverride);
        }

        public Builder<S> descriptionLength(int lines) {
            this.descriptionLines = lines;
            return this;
        }

        public Builder<S> criteria(UnlockCriteria criteria) {
            this.criteria = criteria;
            return this;
        }

        public Builder<S> category(SkillCategory category) {
            this.category = category;
            return this;
        }

        public Builder<S> setGunCategory() {
            return this.category(SkillCategory.GUN);
        }

        public Builder<S> setDebuffCategory() {
            return this.category(SkillCategory.DEBUFF);
        }

        public Builder<S> setMiningCategory() {
            return this.category(SkillCategory.MINING);
        }

        public Builder<S> setSurvivalCategory() {
            return this.category(SkillCategory.SURVIVAL);
        }

        public Builder<S> icon(ResourceLocation location) {
            this.icon = location;
            return this;
        }

        public Builder<S> displayName(ITextComponent component) {
            this.textComponent = component;
            return this;
        }

        public Builder<S> iconPathNormal(String pathInIconsFolder) {
            this.icon = GunsRPG.makeResource("textures/icons/" + pathInIconsFolder + ".png");
            return this;
        }

        public Builder<S> iconPathItem(String itemPath) {
            this.icon = GunsRPG.makeResource("textures/items/" + itemPath + ".png");
            return this;
        }

        public Builder<S> setRegistryName(ResourceLocation location) {
            this.registryName = location;
            return this;
        }

        public Builder<S> setRegistryName(String path) {
            this.registryName = GunsRPG.makeResource(path);
            return this;
        }

        public SkillType<S> build() {
            SkillType<S> type = new SkillType<>(this);
            type.setRegistryName(registryName);
            return type;
        }
    }
}
