package dev.toma.gunsrpg.client.model;

import dev.toma.gunsrpg.common.entity.ZombieGunnerEntity;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelHelper;

public class ModelZombieGunner extends BipedModel<ZombieGunnerEntity> {

    public ModelZombieGunner() {
        this(0.0F, false);
    }

    public ModelZombieGunner(float modelSize, boolean isLayerModel) {
        super(modelSize, 0.0F, 64, isLayerModel ? 32 : 64);
    }

    @Override
    public void setupAnim(ZombieGunnerEntity p_225597_1_, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) {
        super.setupAnim(p_225597_1_, p_225597_2_, p_225597_3_, p_225597_4_, p_225597_5_, p_225597_6_);
        ModelHelper.animateZombieArms(this.leftArm, this.rightArm, this.isAggressive(p_225597_1_), this.attackTime, p_225597_4_);
    }

    protected boolean isAggressive(ZombieGunnerEntity entity) {
        return entity.isAggressive();
    }
}
