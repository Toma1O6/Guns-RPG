package toma.config;

import toma.config.object.builder.ConfigBuilder;

public abstract class ConfigSubcategory {

    public abstract ConfigBuilder toConfigFormat(ConfigBuilder builder);
}
