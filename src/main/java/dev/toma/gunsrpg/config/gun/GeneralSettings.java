package dev.toma.gunsrpg.config.gun;

import toma.config.object.builder.ConfigBuilder;

public class GeneralSettings {

    public float carbonBarrel = 0.65F;
    public float verticalGrip = 0.7F;
    public float cheekpad = 0.75F;

    public ConfigBuilder build(ConfigBuilder builder) {
        return builder.push().name("General").init()
                .addFloat(carbonBarrel).name("Carbon Barrel").range(0.0F, 1.0F).sliderRendering().add(t -> carbonBarrel = t.value())
                .addFloat(verticalGrip).name("Vertical Grip").range(0.0F, 1.0F).sliderRendering().add(t -> verticalGrip = t.value())
                .addFloat(cheekpad).name("Cheekpad").range(0.0F, 1.0F).sliderRendering().add(t -> cheekpad = t.value())
                .pop();
    }
}
