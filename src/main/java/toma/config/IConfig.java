package toma.config;

import toma.config.datatypes.ConfigObject;
import toma.config.object.builder.ConfigBuilder;

public interface IConfig {

    ConfigObject getConfig(ConfigBuilder builder);
}
