package dev.toma.gunsrpg.common.block;

import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.ISkillProvider;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.skills.MotherlodeSkill;
import dev.toma.gunsrpg.util.SkillUtil;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraftforge.common.ToolType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CrystalOre extends BaseBlock implements ICustomizableDrops {

    public CrystalOre(String name) {
        super(name, Properties.of(Material.STONE).strength(3.3F).harvestTool(ToolType.PICKAXE).harvestLevel(2));
    }

    @Override
    public List<ItemStack> getCustomDrops(LootTable table, LootContext context) {
        PlayerEntity player = (PlayerEntity) context.getParamOrNull(LootParameters.THIS_ENTITY);
        IPlayerData data = PlayerData.get(player).orElse(null);
        if (data == null) {
            return Collections.emptyList();
        }
        ISkillProvider provider = data.getSkillProvider();
        MotherlodeSkill skill = SkillUtil.getTopHierarchySkill(Skills.MOTHER_LODE_I, provider);
        List<ItemStack> drops = table.getRandomItems(context);
        if (skill != null) {
            int multiplier = skill.getDropMultiplier(player.getRandom(), data);
            Collection<ItemStack> addedDrops = new ArrayList<>();
            for (ItemStack stack : drops) {
                for (int i = 0; i < multiplier; i++) {
                    addedDrops.add(stack.copy());
                }
            }
            drops.addAll(addedDrops);
        }
        // TODO generate initial NBT data
        return Collections.emptyList();
    }
}
