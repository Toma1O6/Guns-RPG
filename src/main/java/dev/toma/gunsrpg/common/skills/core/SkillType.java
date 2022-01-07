package dev.toma.gunsrpg.common.skills.core;

import com.google.common.base.Preconditions;
import dev.toma.gunsrpg.api.common.skill.ISkill;
import dev.toma.gunsrpg.api.common.skill.ISkillHierarchy;
import dev.toma.gunsrpg.api.common.skill.ISkillProperties;
import dev.toma.gunsrpg.resource.skill.SkillPropertyLoader;
import dev.toma.gunsrpg.util.SkillUtil;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.function.Function;

public class SkillType<S extends ISkill> extends ForgeRegistryEntry<SkillType<?>> {

    // builder configurable data
    private final IFactory<S> instanceFactory;
    private final S dataInstance;
    private final Localization<S> localization;
    private final Function<SkillType<S>, DisplayData> displayDataFactory;
    // internal data
    private DisplayData displayData;
    // datapack controlled data
    private ISkillHierarchy<S> hierarchy;
    private ISkillProperties properties;

    private SkillType(Builder<S> builder) {
        this.instanceFactory = builder.factory;
        this.dataInstance = this.instantiate();
        this.localization = new Localization<>(builder.descriptionLines, this, builder.titleBuilder, builder.descriptionBuilder);
        this.displayDataFactory = builder.displayDataFactory;
    }

    public S getDataInstance() {
        return dataInstance;
    }

    public S instantiate() {
        return instanceFactory.create(this);
    }

    public ISkillHierarchy<S> getHierarchy() {
        return hierarchy;
    }

    public ISkillProperties getProperties() {
        return properties;
    }

    /**
     * Called when data are loaded from datapack for this particular instance.
     * @param result The loaded data
     */
    public void onDataAssign(SkillPropertyLoader.ILoadResult<S> result) {
        hierarchy = result.hierarchy();
        properties = result.properties();
        displayData = displayDataFactory.apply(this);
    }

    public ITextComponent getTitle() {
        return localization.getTitle();
    }

    public ITextComponent[] getDescription() {
        return localization.getDescription();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SkillType<?> type = (SkillType<?>) obj;
        return type.getRegistryName().equals(getRegistryName());
    }

    @Override
    public int hashCode() {
        return getRegistryName().hashCode();
    }

    @Override
    public String toString() {
        return "SkillType{" + getRegistryName().toString() + "}";
    }

    @FunctionalInterface
    public interface IFactory<S extends ISkill> {

        S create(SkillType<S> type);
    }

    @FunctionalInterface
    public interface IDescriptionBuilder<S extends ISkill> {

        ITextComponent[] getDescription(int lineCount, SkillType<S> type);
    }

    public static class Builder<S extends ISkill> {

        private final IFactory<S> factory;
        private Function<SkillType<S>, ITextComponent> titleBuilder = SkillUtil.Localizations::getDefaultTitle;
        private IDescriptionBuilder<S> descriptionBuilder = SkillUtil.Localizations::getDefaultDescription;
        private Function<SkillType<S>, DisplayData> displayDataFactory = SkillUtil::getDefaultDisplayData;
        private int descriptionLines = 1;

        private Builder(IFactory<S> factory) {
            this.factory = factory;
        }

        public static <S extends ISkill> Builder<S> create(IFactory<S> factory) {
            return new Builder<>(factory);
        }

        public Builder<S> description(int lines) {
            this.descriptionLines = lines;
            return this;
        }

        public Builder<S> description(int lines, IDescriptionBuilder<S> builder) {
            this.descriptionBuilder = builder;
            return this.description(lines);
        }

        public Builder<S> description(IDescriptionBuilder<S> builder) {
            return this.description(1, builder);
        }

        public Builder<S> title(Function<SkillType<S>, ITextComponent> titleBuilder) {
            this.titleBuilder = titleBuilder;
            return this;
        }

        public Builder<S> render(Function<SkillType<S>, DisplayData> displayDataFactory) {
            this.displayDataFactory = displayDataFactory;
            return this;
        }

        public Builder<S> renderModIcon(String icon) {
            return this.render(type -> DisplayData.create(DisplayType.ICON, SkillUtil.moddedIcon(icon)));
        }

        public SkillType<S> build() {
            validate();
            return new SkillType<>(this);
        }

        private void validate() {
            Preconditions.checkNotNull(factory, "Instance factory cannot be null");
            Preconditions.checkState(descriptionLines >= 0, "Skill type cannot have negative count of description lines");
        }
    }

    protected static class Localization<S extends ISkill> {

        private final int lineCount;
        private final SkillType<S> type;
        private final Function<SkillType<S>, ITextComponent> titleBuilder;
        private final IDescriptionBuilder<S> descriptionBuilder;
        private boolean loaded;
        private ITextComponent title;
        private ITextComponent[] description;

        public Localization(int lines, SkillType<S> type, Function<SkillType<S>, ITextComponent> titleBuilder, IDescriptionBuilder<S> descriptionBuilder) {
            this.lineCount = lines;
            this.type = type;
            this.titleBuilder = titleBuilder;
            this.descriptionBuilder = descriptionBuilder;
        }

        protected ITextComponent getTitle() {
            if (!loaded) {
                loadData();
            }
            return title;
        }

        protected ITextComponent[] getDescription() {
            if (!loaded) {
                loadData();
            }
            return description;
        }

        private void loadData() {
            title = titleBuilder.apply(type);
            description = descriptionBuilder.getDescription(lineCount, type);
            loaded = true;
        }
    }
}
