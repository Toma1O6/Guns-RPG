package dev.toma.gunsrpg.common.item.guns.ammo;

import dev.toma.gunsrpg.common.entity.projectile.IReaction;
import net.minecraft.util.ResourceLocation;

public class ReactiveMaterial extends SimpleMaterial implements IReactiveMaterial {

    private final IReaction reaction;

    ReactiveMaterial(ResourceLocation materialID, int textColor, int defaultLevelRequirement, IReaction reaction) {
        super(materialID, textColor, defaultLevelRequirement, null);
        this.reaction = reaction;
    }

    @Override
    public IReaction getReaction() {
        return reaction;
    }
}
