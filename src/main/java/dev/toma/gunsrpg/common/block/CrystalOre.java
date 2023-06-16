package dev.toma.gunsrpg.common.block;

import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.ISkillProvider;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.perk.Crystal;
import dev.toma.gunsrpg.common.skills.MotherlodeSkill;
import dev.toma.gunsrpg.util.SkillUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.common.ToolType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CrystalOre extends BaseBlock implements ICustomizableDrops {

    public CrystalOre(String name) {
        super(name, Properties.of(Material.STONE).strength(3.3F).harvestTool(ToolType.PICKAXE).harvestLevel(2).requiresCorrectToolForDrops());
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
            int multiplier = skill.getDropMultiplier(player.getRandom(), data) - 1;
            Collection<ItemStack> addedDrops = new ArrayList<>();
            for (ItemStack stack : drops) {
                for (int i = 0; i < multiplier; i++) {
                    addedDrops.add(stack.copy());
                }
            }
            drops.addAll(addedDrops);
        }
        drops.forEach(stack -> {
            Crystal crystal = Crystal.generate();
            CompoundNBT nbt = new CompoundNBT();
            CompoundNBT crystalNbt = crystal.toNbt();
            nbt.put("crystal", crystalNbt);
            stack.setTag(nbt);
        });
        return drops;
    }

    @Override
    public int getExpDrop(BlockState state, IWorldReader world, BlockPos pos, int fortune, int silktouch) {
        return MathHelper.nextInt(RANDOM, 2, 5);
    }
}
