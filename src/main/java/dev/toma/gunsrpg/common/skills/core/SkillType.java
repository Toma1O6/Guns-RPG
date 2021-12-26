package dev.toma.gunsrpg.common.skills.core;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.ISkill;
import dev.toma.gunsrpg.api.common.skill.ISkillDisplay;
import dev.toma.gunsrpg.api.common.skill.ISkillHierarchy;
import dev.toma.gunsrpg.api.common.skill.ISkillProperties;
import dev.toma.gunsrpg.resource.skill.SkillPropertyLoader;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

// TODO major rework
public class SkillType<S extends ISkill> extends ForgeRegistryEntry<SkillType<?>> {

    private final IFactory<S> instanceFactory;
    private final S dataInstance;
    private ISkillHierarchy<S> hierarchy;
    private ISkillProperties properties;
    private ISkillDisplay display;

    private SkillType(Builder<S> builder) {
        this.instanceFactory = builder.factory;
        this.dataInstance = this.instantiate();
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

    public ISkillDisplay getDisplay() {
        return display;
    }

    /**
     * Called when data are loaded from datapack for this particular instance.
     * @param result The loaded data
     */
    public void onDataAssign(SkillPropertyLoader.ILoadResult<S> result) {
        hierarchy = result.hierarchy();
        properties = result.properties();
        display = result.display();
    }

    public interface IFactory<S extends ISkill> {

        S create(SkillType<S> type);
    }

    public static class Builder<S extends ISkill> {

        private final IFactory<S> factory;
        private ResourceLocation registryName;
        private ResourceLocation icon;
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

        public Builder<S> icon(ResourceLocation location) {
            this.icon = location;
            return this;
        }

        public Builder<S> icon(String resource) {
            return icon(new ResourceLocation(resource));
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
