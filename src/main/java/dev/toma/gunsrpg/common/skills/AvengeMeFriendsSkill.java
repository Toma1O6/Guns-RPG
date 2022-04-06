package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.api.common.skill.IDescriptionProvider;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.skills.core.DescriptionContainer;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import java.util.List;

public class AvengeMeFriendsSkill extends SimpleSkill implements IDescriptionProvider {

    private static final int RANGE = 30;

    private final DescriptionContainer container;

    public AvengeMeFriendsSkill(SkillType<?> type) {
        super(type);
        this.container = new DescriptionContainer(type);
        this.container.addProperty("range", RANGE);
    }

    @Override
    public ITextComponent[] supplyDescription(int desiredLineCount) {
        return container.getLines();
    }

    public void applyEffects(PlayerEntity source) {
        World world = source.level;
        if (!world.isClientSide) {
            List<PlayerEntity> list = world.getEntitiesOfClass(PlayerEntity.class, VoxelShapes.block().bounds().move(source.blockPosition()).inflate(RANGE));
            for (PlayerEntity player : list) {
                player.addEffect(new EffectInstance(Effects.ABSORPTION, 400, 2));
                player.addEffect(new EffectInstance(Effects.REGENERATION, 500, 1));
                player.level.playSound(player, player.getX(), player.getY(), player.getZ(), ModSounds.USE_AVENGE_ME_FRIENDS, SoundCategory.MASTER, 1.0F, 1.0F);
            }
        }
    }
}
