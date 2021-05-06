package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import net.minecraft.entity.player.EntityPlayer;

import java.util.function.BiConsumer;

public class CraftingSkill extends BasicSkill {

    private final int outputAmount;
    private final BiConsumer<CraftingSkill, PlayerSkills> consumer;

    public CraftingSkill(SkillType<?> type) {
        this(type, 0, null);
    }

    public CraftingSkill(SkillType<?> type, BiConsumer<CraftingSkill, PlayerSkills> consumer) {
        this(type, 1, consumer);
    }

    public CraftingSkill(SkillType<?> type, int outputAmount, BiConsumer<CraftingSkill, PlayerSkills> consumer) {
        super(type);
        this.outputAmount = outputAmount;
        this.consumer = consumer;
    }

    public int getOutputAmount() {
        return outputAmount;
    }

    @Override
    public void onPurchase(EntityPlayer player) {
        if(consumer != null) {
            PlayerData data = PlayerDataFactory.get(player);
            PlayerSkills skills = data.getSkills();
            this.consumer.accept(this, skills);
            data.sync();
        }
    }

    public void getGunpowderYield(PlayerSkills skills) {
        skills.setGunpowderCraftYield(Math.max(skills.getGunpowderCraftYield(), outputAmount));
    }

    public void getBonemealYield(PlayerSkills skills) {
        skills.setBonemealCraftYield(Math.max(skills.getBonemealCraftYield(), outputAmount));
    }

    public void getBlazepowderYield(PlayerSkills skills) {
        skills.setBlazePowderCraftYield(Math.max(skills.getBlazePowderCraftYield(), outputAmount));
    }
}
